/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2019 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package net.sf.jsqlparser.util.deparser;

import com.xiaomi.smartql.parser.SimpleNode;
import com.xiaomi.smartql.parser.SmartQLEngine;
import com.xiaomi.smartql.parser.SmartQLParserDefaultVisitor;
import com.xiaomi.smartql.parser.SmartQLParserTreeConstants;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.create.view.CreateView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * @author tw
 */
public class CreateViewDeParserTest {

    /**
     * Test of deParse method, of class CreateViewDeParser.
     */
    @Test
    public void testUseExtrnalExpressionDeparser() throws JSQLParserException {
        StringBuilder b = new StringBuilder();
        SelectDeParser selectDeParser = new SelectDeParser();
        selectDeParser.setBuffer(b);
        ExpressionDeParser expressionDeParser = new ExpressionDeParser(selectDeParser, b) {

            @Override
            public void visit(Column tableColumn) {
                final Table table = tableColumn.getTable();
                String tableName = null;
                if (table != null) {
                    if (table.getAlias() != null) {
                        tableName = table.getAlias().getName();
                    } else {
                        tableName = table.getFullyQualifiedName();
                    }
                }
                if (tableName != null && !tableName.isEmpty()) {
                    getBuffer().append("\"").append(tableName).append("\"").append(".");
                }

                getBuffer().append("\"").append(tableColumn.getColumnName()).append("\"");
            }
        };

        selectDeParser.setExpressionVisitor(expressionDeParser);

        CreateViewDeParser instance = new CreateViewDeParser(b, selectDeParser);
        CreateView vc = (CreateView) SmartQLEngine.
                parse("CREATE VIEW test AS SELECT a, b FROM mytable");
        instance.deParse(vc);

        assertEquals("CREATE VIEW test AS SELECT a, b FROM mytable", vc.toString());
        assertEquals("CREATE VIEW test AS SELECT \"a\", \"b\" FROM mytable", instance.getBuffer().
                toString());
    }

    @Test
    public void testCreateViewASTNode() throws JSQLParserException {
        String sql = "CREATE VIEW test AS SELECT a, b FROM mytable";
        final StringBuilder b = new StringBuilder(sql);
        SimpleNode node = (SimpleNode) SmartQLEngine.parseAST(sql);
        node.dump("*");
        assertEquals(SmartQLParserTreeConstants.JJTSTATEMENT, node.getId());

        node.jjtAccept(new SmartQLParserDefaultVisitor() {
            int idxDelta = 0;

            @Override
            public Object visit(SimpleNode node, Object data) {
                if (SmartQLParserTreeConstants.JJTCOLUMN == node.getId()) {
                    b.insert(node.jjtGetFirstToken().beginColumn - 1 + idxDelta, '"');
                    idxDelta++;
                    b.insert(node.jjtGetLastToken().endColumn + idxDelta, '"');
                    idxDelta++;
                }
                return super.visit(node, data);
            }
        }, null);

        assertEquals("CREATE VIEW test AS SELECT \"a\", \"b\" FROM mytable", b.toString());
    }
}
