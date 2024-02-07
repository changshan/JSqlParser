package com.xiaomi.smartql.parser;


import com.xiaomi.smartql.lineage.FieldLineage;
import com.xiaomi.smartql.lineage.FieldLineageFinder;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author Hawick Mason
 * @date 2022/12/1
 */
public class FieldLineageFinderTest {
    @Test
    void testGetSqlLineage() throws ParseException {
        String sql =
                "SELECT temp.name, max(temp.price) as price \n"
                        + "FROM (\n"
                        + "  SELECT `user`.name,\n"
                        + "         `order`.price\n"
                        + "  FROM   `user`,\n"
                        + "         `order`\n"
                        + "  WHERE  user.id = order.uid \n"
                        + " ) as temp\n"
                        + "GROUP BY temp.name";
        Map<String, FieldLineage> fieldLineageMap = FieldLineageFinder.parseLineageOfAnalysis(sql);
        System.out.println(fieldLineageMap);
    }
}
