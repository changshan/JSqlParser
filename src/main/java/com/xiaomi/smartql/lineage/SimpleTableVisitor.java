package com.xiaomi.smartql.lineage;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;

/**
 * @author Hawick Mason
 * @date 2022/11/24
 */
public class SimpleTableVisitor implements FromItemVisitor {



    @Override
    public void visit(Table tableName) {

    }

    @Override
    public void visit(ParenthesedSelect selectBody) {

    }

    @Override
    public void visit(LateralSubSelect lateralSubSelect) {

    }


    @Override
    public void visit(TableFunction tableFunction) {

    }

    @Override
    public void visit(ParenthesedFromItem aThis) {

    }
}
