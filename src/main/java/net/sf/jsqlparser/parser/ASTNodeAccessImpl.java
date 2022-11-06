package net.sf.jsqlparser.parser;

import com.xiaomi.smartql.parser.SimpleNode;

public class ASTNodeAccessImpl implements ASTNodeAccess {

    private SimpleNode node;

    @Override
    public SimpleNode getASTNode() {
        return node;
    }

    @Override
    public void setASTNode(SimpleNode node) {
        this.node = node;
    }

}
