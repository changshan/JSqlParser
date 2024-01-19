/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2024 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package net.sf.jsqlparser.expression;


import com.xiaomi.smartql.parser.ASTNodeAccessImpl;

/**
 * 兼容[${...}$],$#@[...]@#$特殊值函数
 * @author Changshan
 * 2022/8/11
 */
public class FuzzyValue extends ASTNodeAccessImpl implements Expression, Cloneable {

    private String value;


    public FuzzyValue(String value) {
        this.value = value;
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return value;
    }
}
