package com.xiaomi.smartql.parser;

import com.xiaomi.smart.exception.SmartException;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;

/**
 * @author Changshan
 * 2022/9/7
 */
public class ParserUtil {
    public Expression object2Exp(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Expression) {
            return (Expression) obj;
        }

        if (obj instanceof String) {
            return new StringValue((String) obj);
        }

        if (obj instanceof Long) {
            return new LongValue((Long) obj);
        }
        if (obj instanceof Double) {
            return new DoubleValue(obj.toString());
        }

        String sql = obj.toString();

        Expression expr = null;
        try {
            expr = SmartQLEngine.parseExpression(sql);
        } catch (SmartException e) {
        }
        return expr;

    }
}
