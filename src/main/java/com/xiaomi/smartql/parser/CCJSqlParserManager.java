package com.xiaomi.smartql.parser;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.statement.Statement;

import java.io.Reader;

public class CCJSqlParserManager implements JSqlParser {

    @Override
    public Statement parse(Reader statementReader) throws JSQLParserException {
        SmartQLParser parser = new SmartQLParser(new StreamProvider(statementReader));
        try {
            return parser.Statement();
        } catch (Exception ex) {
            throw new JSQLParserException(ex);
        }
    }
}
