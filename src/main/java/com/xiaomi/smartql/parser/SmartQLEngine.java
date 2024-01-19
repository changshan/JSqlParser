package com.xiaomi.smartql.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.xiaomi.smart.exception.SmartException;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.parser.feature.Feature;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;

/**
 * SmartQL Engine
 */
@SuppressWarnings("PMD.CyclomaticComplexity")
public final class SmartQLEngine {
    public final static Logger LOGGER = Logger.getLogger(SmartQLEngine.class.getName());
    static {
        LOGGER.setLevel(Level.OFF);
    }

    public final static int ALLOWED_NESTING_DEPTH = 20;
    public static final int PARSER_TIMEOUT = 6000;

    private SmartQLEngine() {}


    public Expression parseExpression(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Expression) {
            return (Expression) obj;
        }

        if (obj instanceof String) {
            return new StringValue((String) obj);
        }

        if (obj instanceof Long) {
            return new LongValue((Long) obj);
        }
        if (obj instanceof Double) {
            return new DoubleValue(obj.toString());
        }

        String sql = obj.toString();

        Expression expr = null;
        try {
            expr = SmartQLEngine.parseExpression(sql);
        } catch (SmartException e) {
            LOGGER.fine(e.getMessage());
        }
        return expr;
    }

    public static Statement parse(Reader statementReader) throws JSQLParserException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Statement statement = null;
        SmartQLParser parser = new SmartQLParser(new StreamProvider(statementReader));
        try {
            statement = parseStatement(parser, executorService);
        } finally {
            executorService.shutdown();
        }
        return statement;
    }

    public static Statement parse(String sql) throws JSQLParserException {
        return parse(sql, null);
    }

    /**
     * Parses an sql statement while allowing via consumer to configure the used parser before.
     *
     * For instance to activate SQLServer bracket quotation on could use:
     *
     * {@code
     * SmartQLEngine.parse("select * from [mytable]", parser -> parser.withSquareBracketQuotation(true));
     * }
     *
     * @param sql
     * @param consumer
     * @return
     * @throws JSQLParserException
     */
    public static Statement parse(String sql, Consumer<SmartQLParser> consumer)
            throws JSQLParserException {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Statement statement = null;
        try {
            statement = parse(sql, executorService, consumer);
        } finally {
            executorService.shutdown();
        }
        return statement;
    }

    public static Statement parse(String sql, ExecutorService executorService,
            Consumer<SmartQLParser> consumer)
            throws JSQLParserException {
        Statement statement = null;

        // first, try to parse fast and simple
        SmartQLParser parser = newParser(sql);
        if (consumer != null) {
            consumer.accept(parser);
        }
        boolean allowComplex = parser.getConfiguration().getAsBoolean(Feature.allowComplexParsing);
        LOGGER.info("Allowed Complex Parsing: " + allowComplex);
        try {
            LOGGER.info("Trying SIMPLE parsing " + (allowComplex ? "first" : "only"));
            statement = parseStatement(parser.withAllowComplexParsing(false), executorService);
        } catch (JSQLParserException ex) {
            LOGGER.info("Nesting Depth" + getNestingDepth(sql));
            if (allowComplex && getNestingDepth(sql) <= ALLOWED_NESTING_DEPTH) {
                LOGGER.info("Trying COMPLEX parsing when SIMPLE parsing failed");
                // beware: the parser must not be reused, but needs to be re-initiated
                parser = newParser(sql);
                if (consumer != null) {
                    consumer.accept(parser);
                }
                statement = parseStatement(parser.withAllowComplexParsing(true), executorService);
            } else {
                throw ex;
            }
        }
        return statement;
    }

    public static SmartQLParser newParser(String sql) {
        return new SmartQLParser(new StringProvider(sql));
    }

    public static SmartQLParser newParser(InputStream is) throws IOException {
        return new SmartQLParser(new StreamProvider(is));
    }

    public static SmartQLParser newParser(InputStream is, String encoding) throws IOException {
        return new SmartQLParser(new StreamProvider(is, encoding));
    }

    public static Node parseAST(String sql) throws JSQLParserException {
        SmartQLParser parser = newParser(sql);
        try {
            parser.Statement();
            return parser.jjtree.rootNode();
        } catch (Exception ex) {
            throw new JSQLParserException(ex);
        }
    }

    public static Statement parse(InputStream is) throws JSQLParserException {
        try {
            SmartQLParser parser = newParser(is);
            return parser.Statement();
        } catch (Exception ex) {
            throw new JSQLParserException(ex);
        }
    }

    public static Statement parse(InputStream is, String encoding) throws JSQLParserException {
        try {
            SmartQLParser parser = newParser(is, encoding);
            return parser.Statement();
        } catch (Exception ex) {
            throw new JSQLParserException(ex);
        }
    }

    public static Expression parseExpression(String expression) throws JSQLParserException {
        return parseExpression(expression, true);
    }

    public static Expression parseExpression(String expression, boolean allowPartialParse)
            throws JSQLParserException {
        return parseExpression(expression, allowPartialParse, p -> {
        });
    }

    @SuppressWarnings("PMD.CyclomaticComplexity")
    public static Expression parseExpression(String expressionStr, boolean allowPartialParse,
            Consumer<SmartQLParser> consumer) throws JSQLParserException {
        Expression expression = null;

        // first, try to parse fast and simple
        try {
            SmartQLParser parser = newParser(expressionStr).withAllowComplexParsing(false);
            if (consumer != null) {
                consumer.accept(parser);
            }
            try {
                expression = parser.Expression();
                if (parser.getNextToken().kind != SmartQLParserTokenManager.EOF) {
                    throw new JSQLParserException(
                            "could only parse partial expression " + expression.toString());
                }
            } catch (ParseException ex) {
                throw new JSQLParserException(ex);
            }
        } catch (JSQLParserException ex1) {
            // when fast simple parsing fails, try complex parsing but only if it has a chance to
            // succeed
            if (getNestingDepth(expressionStr) <= ALLOWED_NESTING_DEPTH) {
                SmartQLParser parser = newParser(expressionStr).withAllowComplexParsing(true);
                if (consumer != null) {
                    consumer.accept(parser);
                }
                try {
                    expression = parser.Expression();
                    if (!allowPartialParse
                            && parser.getNextToken().kind != SmartQLParserTokenManager.EOF) {
                        throw new JSQLParserException(
                                "could only parse partial expression " + expression.toString());
                    }
                } catch (JSQLParserException ex) {
                    throw ex;
                } catch (ParseException ex) {
                    throw new JSQLParserException(ex);
                }
            }
        }
        return expression;
    }

    /**
     * Parse an conditional expression. This is the expression after a where clause. Partial parsing
     * is enabled.
     *
     * @param condExpr
     * @return the expression parsed
     * @see #parseCondExpression(String, boolean)
     */
    public static Expression parseCondExpression(String condExpr) throws JSQLParserException {
        return parseCondExpression(condExpr, true);
    }

    /**
     * Parse an conditional expression. This is the expression after a where clause.
     *
     * @param condExpr
     * @param allowPartialParse false: needs the whole string to be processed.
     * @return the expression parsed
     * @see #parseCondExpression(String)
     */
    public static Expression parseCondExpression(String condExpr, boolean allowPartialParse)
            throws JSQLParserException {
        return parseCondExpression(condExpr, allowPartialParse, p -> {
        });
    }

    @SuppressWarnings("PMD.CyclomaticComplexity")
    public static Expression parseCondExpression(String conditionalExpressionStr,
            boolean allowPartialParse, Consumer<SmartQLParser> consumer) throws JSQLParserException {
        Expression expression = null;

        // first, try to parse fast and simple
        try {
            SmartQLParser parser =
                    newParser(conditionalExpressionStr).withAllowComplexParsing(false);
            if (consumer != null) {
                consumer.accept(parser);
            }
            try {
                expression = parser.Expression();
                if (parser.getNextToken().kind != SmartQLParserTokenManager.EOF) {
                    throw new JSQLParserException(
                            "could only parse partial expression " + expression.toString());
                }
            } catch (ParseException ex) {
                throw new JSQLParserException(ex);
            }
        } catch (JSQLParserException ex1) {
            if (getNestingDepth(conditionalExpressionStr) <= ALLOWED_NESTING_DEPTH) {
                SmartQLParser parser =
                        newParser(conditionalExpressionStr).withAllowComplexParsing(true);
                if (consumer != null) {
                    consumer.accept(parser);
                }
                try {
                    expression = parser.Expression();
                    if (!allowPartialParse
                            && parser.getNextToken().kind != SmartQLParserTokenManager.EOF) {
                        throw new JSQLParserException(
                                "could only parse partial expression " + expression.toString());
                    }
                } catch (JSQLParserException ex) {
                    throw ex;
                } catch (ParseException ex) {
                    throw new JSQLParserException(ex);
                }
            }
        }
        return expression;
    }

    /**
     * @param parser the Parser armed with a Statement text
     * @param executorService the Executor Service for parsing within a Thread
     * @return the parsed Statement
     * @throws JSQLParserException when either the Statement can't be parsed or the configured
     *         timeout is reached
     */

    public static Statement parseStatement(SmartQLParser parser, ExecutorService executorService)
            throws JSQLParserException {
        Statement statement = null;
        Future<Statement> future = executorService.submit(new Callable<Statement>() {
            @Override
            public Statement call() throws ParseException {
                return parser.Statement();
            }
        });
        try {
            statement = future.get(parser.getConfiguration().getAsLong(Feature.timeOut),
                    TimeUnit.MILLISECONDS);
        } catch (TimeoutException ex) {
            parser.interrupted = true;
            future.cancel(true);
            throw new JSQLParserException("Time out occurred.", ex);
        } catch (Exception ex) {
            throw new JSQLParserException(ex);
        }
        return statement;
    }

    /**
     * Parse a statement list.
     *
     * @return the statements parsed
     */
    public static Statements parseStatements(String sqls) throws JSQLParserException {
        return parseStatements(sqls, null);
    }

    public static Statements parseStatements(String sqls, Consumer<SmartQLParser> consumer)
            throws JSQLParserException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        final Statements statements = parseStatements(sqls, executorService, consumer);
        executorService.shutdown();

        return statements;
    }

    /**
     * Parse a statement list.
     *
     * @return the statements parsed
     */
    public static Statements parseStatements(String sqls, ExecutorService executorService,
            Consumer<SmartQLParser> consumer)
            throws JSQLParserException {
        Statements statements = null;

        SmartQLParser parser = newParser(sqls);
        if (consumer != null) {
            consumer.accept(parser);
        }
        boolean allowComplex = parser.getConfiguration().getAsBoolean(Feature.allowComplexParsing);

        // first, try to parse fast and simple
        try {
            statements = parseStatements(parser.withAllowComplexParsing(false), executorService);
        } catch (JSQLParserException ex) {
            // when fast simple parsing fails, try complex parsing but only if it has a chance to
            // succeed
            if (allowComplex && getNestingDepth(sqls) <= ALLOWED_NESTING_DEPTH) {
                // beware: parser must not be re-used but needs to be re-initiated
                parser = newParser(sqls);
                if (consumer != null) {
                    consumer.accept(parser);
                }
                statements = parseStatements(parser.withAllowComplexParsing(true), executorService);
            }
        }
        return statements;
    }

    /**
     * @param parser the Parser armed with a Statement text
     * @param executorService the Executor Service for parsing within a Thread
     * @return the Statements (representing a List of single statements)
     * @throws JSQLParserException when either the Statement can't be parsed or the configured
     *         timeout is reached
     */
    public static Statements parseStatements(SmartQLParser parser, ExecutorService executorService)
            throws JSQLParserException {
        Statements statements = null;
        Future<Statements> future = executorService.submit(new Callable<Statements>() {
            @Override
            public Statements call() throws ParseException {
                return parser.Statements();
            }
        });
        try {
            statements = future.get(parser.getConfiguration().getAsLong(Feature.timeOut),
                    TimeUnit.MILLISECONDS);
        } catch (TimeoutException ex) {
            parser.interrupted = true;
            future.cancel(true);
            throw new JSQLParserException("Time out occurred.", ex);
        } catch (Exception ex) {
            throw new JSQLParserException(ex);
        }
        return statements;
    }

    public static void streamStatements(StatementListener listener, InputStream is, String encoding)
            throws JSQLParserException {
        try {
            SmartQLParser parser = newParser(is, encoding);
            while (true) {
                Statement stmt = parser.SingleStatement();
                listener.accept(stmt);
                if (parser.getToken(1).kind == SmartQLParserTokenManager.ST_SEMICOLON) {
                    parser.getNextToken();
                }

                if (parser.getToken(1).kind == SmartQLParserTokenManager.EOF) {
                    break;
                }
            }
        } catch (Exception ex) {
            throw new JSQLParserException(ex);
        }
    }

    public static int getNestingDepth(String sql) {
        int maxlevel = 0;
        int level = 0;

        char[] chars = sql.toCharArray();
        for (char c : chars) {
            switch (c) {
                case '(':
                    level++;
                    break;
                case ')':
                    if (maxlevel < level) {
                        maxlevel = level;
                    }
                    level--;
                    break;
                default:
                    // Codazy/PMD insists in a Default statement
            }
        }
        return maxlevel;
    }

    public static int getUnbalancedPosition(String text) {
        Stack<Character> stack = new Stack<>();
        boolean insideQuote = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '"' || c == '\'') {
                if (!insideQuote) {
                    stack.push(c); // Add quote to stack
                } else if (stack.peek() == c) {
                    stack.pop(); // Matching quote found, remove from stack
                }
                insideQuote = !insideQuote; // Toggle insideQuote flag
            } else if (!insideQuote && (c == '(' || c == '[' || c == '{')) {
                stack.push(c); // Add opening bracket to stack
            } else if (!insideQuote && (c == ')' || c == ']' || c == '}')) {
                if (stack.isEmpty()) {
                    return i; // Return position of unbalanced closing bracket
                }
                char top = stack.pop();
                if (c == ')' && top != '(' || c == ']' && top != '[' || c == '}' && top != '{') {
                    return i; // Return position of unbalanced closing bracket
                }
            }
        }

        if (!stack.isEmpty()) {
            char unbalanced = stack.peek();
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) == unbalanced) {
                    return i; // Return position of unbalanced opening bracket or quote
                }
            }
        }

        return -1; // Return -1 if all brackets and quotes are balanced
    }

}
