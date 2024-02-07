package com.xiaomi.smartql.lineage;

import com.xiaomi.smart.exception.SmartException;
import com.xiaomi.smartql.parser.ParseException;
import com.xiaomi.smartql.parser.SmartQLEngine;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hawick Mason, may be used in the future
 * @date 2022/12/1
 */
public class FieldLineageFinder {
    public static Map<String /*查询最外层的字段*/, FieldLineage /*字段血缘信息*/> parseLineageOfAnalysis(String sql)
            throws ParseException {
        Map<String, FieldLineage> res = new HashMap<>();
        try {
            Statement statement = SmartQLEngine.parse(sql);
            if (statement instanceof Select) {
                Select select = (Select) statement;
                PlainSelect selectBody = (PlainSelect) select.getSelectBody();
                FromItem fromItem = selectBody.getFromItem();
                List<Column> outerFields = new OuterFieldVisitor().getOuterFields(selectBody);
                for (Column c : outerFields) {
                    findParentField(c, fromItem, res);
                }
            }
        } catch (SmartException e) {
            throw new ParseException("SQL parser failed!");
        }
        return res;
    }

    @SuppressWarnings("PMD.CyclomaticComplexity")
    private static void findParentField(Column c, FromItem from, Map<String, FieldLineage> map) {
        // TODO: handle more cases
        String curField = c.toString();
        if (from instanceof LateralSubSelect) {
            // 子查询
            LateralSubSelect subSelect = (LateralSubSelect) from;
            PlainSelect selectBody = (PlainSelect) subSelect.getSelectBody();
            FromItem fromItem = selectBody.getFromItem();
            List<Join> joins = selectBody.getJoins();
            Alias alias = subSelect.getAlias();
            if (c.getTable() != null
                    && c.getTable().getName() != null
                    && c.getTable().getName().equals(alias.getName())) {
                List<SelectItem<?>> selectItems = selectBody.getSelectItems();
                for (SelectItem item : selectItems) {
                    String aaName = item.getAlias() != null ? item.getAlias().getName() : item.toString();
                    if (c.getColumnName().equals(retrieveBareFieldName(aaName))) {
                        recordLineage(curField, aaName, alias.getName(), map);
                        List<Column> outerFields = new OuterFieldVisitor().getOuterFields(item);
                        for (Column ic : outerFields) {
                            findParentField(ic, fromItem, map); // 递归查找
                            for (Join join : joins) {
                                findParentField(ic, join.getRightItem(), map);
                            }
                        }
                    }
                }
            }
        }

        if (from instanceof Table) {
            // 表, 递归终止
            Table t = (Table) from;
            String tName = t.getAlias() != null ? t.getAlias().getName() : t.getName();
            if (c.getTable() != null
                    && c.getTable().getName() != null
                    && c.getTable().getName().equals(tName)) {
                recordLineage(curField, curField, tName, map);
            }
        }
    }

    private static String retrieveBareFieldName(String expr) {
        if (expr == null || expr.length() <= 0) {
            return expr;
        }
        if (expr.contains(".")) {
            String[] split = expr.split("\\.");
            if (split.length != 2) {
                return expr;
            } else {
                return split[1];
            }
        }
        return expr;
    }

    private static void recordLineage(
            String curField,
            String lineageFieldName,
            String lineageTableName,
            Map<String, FieldLineage> map) {
        FieldLineage.LineageBO lineageBO =
                FieldLineage.LineageBO.builder()
                        .fieldName(lineageFieldName)
                        .tableName(lineageTableName)
                        .build();
        if (map.containsKey(curField)) {
            FieldLineage fieldLineage = map.get(curField);
            fieldLineage.getLineageFields().add(lineageBO);
        } else {
            List<FieldLineage.LineageBO> lineageList = new ArrayList<>();
            lineageList.add(lineageBO);
            map.put(
                    curField,
                    FieldLineage.builder().currentField(curField).lineageFields(lineageList).build());
        }
    }
}
