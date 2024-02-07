package com.xiaomi.smartql.lineage;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hawick Mason
 * @date 2022/11/24
 */
public class OuterFieldVisitor extends ExpressionVisitorAdapter implements SelectVisitor, SelectItemVisitor {

    private List<Column> fields;

    protected void init() {
        fields = new ArrayList<>();
    }

    public List<Column> getOuterFields(SelectItem expressionItem) {
        init();
        visit(expressionItem);
        return fields;
    }

    public List<Column> getOuterFields(Expression expression) {
        init();
        expression.accept(this);
        return fields;
    }

    @Override
    public void visit(PlainSelect plainSelect) {
        if (plainSelect.getSelectItems() != null) {
            for (SelectItem item : plainSelect.getSelectItems()) {
                item.accept(this);
            }
        }
    }

    @Override
    public void visit(SetOperationList setOpList) {

    }

    @Override
    public void visit(WithItem withItem) {

    }

    @Override
    public void visit(Values aThis) {

    }

    @Override
    public void visit(LateralSubSelect lateralSubSelect) {

    }

    @Override
    public void visit(TableStatement tableStatement) {

    }

    @Override
    public void visit(SelectItem selectExpressionItem) {
        selectExpressionItem.getExpression().accept(this);
    }

    @Override
    public void visit(Column tableColumn) {
        fields.add(tableColumn);
    }
}
