package com.xiaomi.smartql.parser;

import java.io.Serializable;

public interface ASTNodeAccess extends Serializable {

    SimpleNode getASTNode();

    void setASTNode(SimpleNode node);
}
