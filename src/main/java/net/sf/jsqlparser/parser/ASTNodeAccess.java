package net.sf.jsqlparser.parser;

import com.xiaomi.smartql.parser.SimpleNode;

public interface ASTNodeAccess {

    SimpleNode getASTNode();

    void setASTNode(SimpleNode node);
}
