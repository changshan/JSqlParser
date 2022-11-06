package net.sf.jsqlparser.expression;


import net.sf.jsqlparser.parser.ASTNodeAccessImpl;

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
