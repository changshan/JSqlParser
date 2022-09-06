package com.xiaomi.smartql.parser;

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
