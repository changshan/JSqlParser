package com.xiaomi.smartql.parser;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SmartQLParserTest {

    /**
     * 打印token
     *
     * @param sql
     */
    private void printToken(String sql) {
        SmartQLParser parser = SmartQLEngine.newParser(sql);
        Token tk = null;
        do {
            tk = parser.getNextToken();
            System.out.println(tk.toString());

        } while (StringUtils.isNotEmpty(tk.toString()));
    }

    @Test
    public void testNum() throws Exception {
        String sql = "select abc.1123abc from abc";
        printToken(sql);
        SmartQLEngine.parse(sql);
        assertTrue(Boolean.TRUE);
    }

    @Test
    public void testNum_1() throws Exception {
        String sql = "select (SUM(ads_appstore_shurufa_di.dau)) `A_11561_135_1628671995672`, (SUM(ads_appstore_shurufa_di.7_wakeup_dau)) `A_11561_234_1628671995677`, (SUM(ads_appstore_shurufa_di.14_wakeup_dau)) `A_11561_151_1628671995682` from ads_appstore_shurufa_di   group by `A_11561_63_1628672025663`;";
        SmartQLEngine.parse(sql);
        assertTrue(Boolean.TRUE);
    }

    @Test
    public void testDigitalField() throws Exception {
        Statements result = SmartQLEngine.parseStatements("select (domainQueryCount.time) A_39634_793_1657099508271, (domainQueryCount.http_host) A_39634_630_1657099508271, (SUM(domainQueryCount.count)) A_39634_883_1657099508271, (SUM(domainQueryCount.`5xx_count`)) A_39634_872_1657099508271, (domainQueryCount.count) A_39634_823_1657099590046, (domainQueryCount.xx_count) A_39634_763_1657099612818, (1 - domainQueryCount.xx_count/domainQueryCount.count) A_39634_272_1657101240162, (domainQueryCount.xx_count) A_39634_241_1657102108488, (domainQueryCount.count) A_39634_903_1657102134709 from domainQueryCount   group by A_39634_793_1657099508271,A_39634_630_1657099508271,A_39634_823_1657099590046,A_39634_763_1657099612818,A_39634_272_1657101240162,A_39634_241_1657102108488,A_39634_903_1657102134709;\n" +
                "\n;");
        System.out.println(result.toString());
    }

    @Test
    public void testKeys() throws Exception {
        Statement result = SmartQLEngine.parse("select database,check,current_date from abc");
        System.out.println(result.toString());
    }

    @Test
    public void testBaseSQL() throws Exception {
        Statements result = SmartQLEngine.parseStatements("select * from dual;\n");
        assertEquals("SELECT * FROM dual;\n", result.toString());
    }

    @Test
    public void testComment() throws Exception {
        Statements result = SmartQLEngine.parseStatements("select * from dual;---test");
        assertEquals("SELECT * FROM dual;\n", result.toString());
    }

    @Test
    public void testNest() throws Exception {
        Statement result =
                SmartQLEngine.parse(
                        "Select test.* from (Select * from sch.PERSON_TABLE // root test\n) as test");
        assertEquals("SELECT test.* FROM (SELECT * FROM sch.PERSON_TABLE) AS test", result.toString());
    }


    /**
     * 表名以数字开头
     *
     * @throws Exception
     */
    @Test
    public void test2() throws Exception {
        Statement result =
                SmartQLEngine.parse("select * from (SELECT * FROM test) sql_model_virtual_table_4776_98 where abc>100;");
        System.out.println(result.toString());
    }


    /**
     * 表名以数字开头
     *
     * @throws Exception
     */
    @Test
    public void test3() throws Exception {
        Statement result =
                SmartQLEngine.parse("SELECT substring(`t1_日期`,1,4) AS `c0`\n" +
                        "FROM\n" +
                        "  (SELECT excel_4_预警.`是否正常` AS `t1_是否正常`,\n" +
                        "          excel_4_预警.`工序` AS `t1_工序`,\n" +
                        "          excel_4_预警.`抽屉号` AS `t1_抽屉号`,\n" +
                        "          excel_4_预警.`槽位号` AS `t1_槽位号`,\n" +
                        "          excel_4_预警.`制式` AS `t1_制式`,\n" +
                        "          excel_4_预警.`频率` AS `t1_频率`,\n" +
                        "          excel_4_预警.`txrx_new` AS `t1_txrx_new`,\n" +
                        "          excel_4_预警.`loss值` AS `t1_loss值`,\n" +
                        "          excel_4_预警.`设备ID` AS `t1_设备ID`,\n" +
                        "          excel_4_预警.`上限` AS `t1_上限`,\n" +
                        "          excel_4_预警.`下限` AS `t1_下限`,\n" +
                        "          excel_4_预警.`日期` AS `t1_日期`\n" +
                        "   FROM\n" +
                        "     (SELECT *\n" +
                        "      FROM excel_4_预警) excel_4_预警) AS `sql_model_virtual_table_95ae79a478b74a4384210fa3cf6b31df_local`\n" +
                        "WHERE UPPER(substring(`t1_日期`,1,4)) = UPPER('L_59391_500_1663741323109')\n" +
                        "GROUP BY substring(`t1_日期`,1,4)\n" +
                        "ORDER BY ISNULL(`c0`) ASC, `c0` ASC");
        System.out.println(result.toString());
    }


    @Test
    public void testAnnotation() throws Exception {
        String sql = "select #注释内容 \n a,/* 这条SELECT语句，  \n" +
                "    是一个注释*/ b,c -- 这是一个注释 from dual;#注释内容 \n";

        printToken(sql);
        Statement result =
                SmartQLEngine.parse(sql);
        assertTrue(Boolean.TRUE);
    }

    @Test
    public void testAnnotation2() throws Exception {
        Statement result =
                SmartQLEngine.parse("select\n" +
                        "    (周数) A_27701_404_1651806744648,\n" +
                        "    (应用名) A_27701_635_1651820468876,\n" +
                        "    (应用中文名) A_27701_536_1651820468876,\n" +
                        "    (业务负责人) A_27701_598_1651820468876,\n" +
                        "    (应用所属部门) A_27701_133_1651820468876,\n" +
                        "    (SUM(新增实验数)) A_27701_72_1651820468876\n" +
                        "from\n" +
                        "    (\n" +
                        "        select\n" +
                        "            e.app_name as '应用名',\n" +
                        "            e.week as '周数',\n" +
                        "            e.exp_num as '新增实验数',\n" +
                        "            f.chinese_app_name as '应用中文名',\n" +
                        "            f.owner as '业务负责人',\n" +
                        "            f.dept as '应用所属部门'\n" +
                        "        from\n" +
                        "            (\n" +
                        "                select\n" +
                        "                    app_name as app_name,\n" +
                        "                    concat(\n" +
                        "                        date_format(create_time, '%Y'),\n" +
                        "                        '',\n" +
                        "                        date_format(create_time, '%u')\n" +
                        "                    ) as week,\n" +
                        "                    count(id) as exp_num\n" +
                        "                from\n" +
                        "                    `exp_entity_info`\n" +
                        "                where\n" +
                        "                    entity_type = 4\n" +
                        "                    and app_name not in (\n" +
                        "                        'Demo',\n" +
                        "                        'Online-Test',\n" +
                        "                        'UserProfile',\n" +
                        "                        'YoupinSearchPreview',\n" +
                        "                        'jiekouceshi',\n" +
                        "                        'YouPinFeedsPre',\n" +
                        "                        'YPfeeds_waterfall_homepage_Pre',\n" +
                        "                        'YPfeeds_homepage_tab_Pre',\n" +
                        "                        'YPfeeds_center_Pre',\n" +
                        "                        'YPfeeds_detail_waist_Pre',\n" +
                        "                        'YPfeeds_detail_bottom_Pre',\n" +
                        "                        'YPfeeds_feed_back_Pre',\n" +
                        "                        'YPfeeds_waterfall_common_Pre',\n" +
                        "                        'Smfeeds_waterfall_homepage_Pre',\n" +
                        "                        'Ts_waterfall_homepage_Pre',\n" +
                        "                        'YPcomment_Pre',\n" +
                        "                        'SplitCrowd'\n" +
                        "                    ) and date_format(create_time,'%Y') = '2022' and substring_index(name,'',1) not like '测试%' -- 排除画像中的测试数据 \n group by app_name ,concat(date_format(create_time,'%Y'),'',date_format(create_time,'%u'))) e left join ( select d.app_name as app_name, d.owner as owner, c.chinese_app_name as chinese_app_name, d.dept as dept from( select a.app_name as app_name, b.chinese_user_name as owner, concat( ifnull(b.first_dept, ''), '', ifnull(b.second_dept, ''), '', ifnull(b.third_dept, ''), '', ifnull(b.fourth_dept, '')) as dept from ( select app_name, user_name from exp_authority where status = 0 and authority = 4 ) a left join ( select user_name, chinese_user_name, app_name, first_dept, second_dept, third_dept, fourth_dept from exp_user_department where user_name is not null and status = 0 ) b on a.user_name = b.user_name and a.app_name = b.app_name ) d left join ( select domain_name as app_name, chinese_domain_name as chinese_app_name from exp_root_domain_info ) c on c.app_name = d.app_name order by app_name) f on e.app_name = f.app_name ) smart_sql_chu7jm group by A_27701_404_1651806744648,A_27701_635_1651820468876,A_27701_536_1651820468876,A_27701_598_1651820468876,A_27701_133_1651820468876;");
        System.out.println(result.toString());
    }

    /**
     * mysql shell boolean test
     *
     * @throws Exception
     */
    @Test
    public void test4() throws Exception {
        Statement result =
                SmartQLEngine.parse("select * from abc where 1=1 and abc_1 is True;");
        System.out.println(result.toString());
    }


    /**
     * keyworkd all
     *
     * @throws Exception
     */
    @Test
    public void test5() throws Exception {
        SmartQLEngine.parse("select now.zdt, (all.countAll - now.countNow)/all.countAll * 100 as progress\n" +
                "from(\n" +
                "    select count(*) as countAll from iceberg_zjyprc_hadoop.iceberg.derora_flink_jobs \n" +
                "    where zdt >= to_date( '2022-02-15') and zdt < to_date(date_sub('2022-02-16', 0))\n" +
                ") all1,\n" +
                "(\n" +
                "select zdt, count(*) as countNow from iceberg_zjyprc_hadoop.iceberg.derora_flink_jobs\n" +
                "where zdt >= to_date('2022-02-15')\n" +
                "group by zdt\n" +
                ")now");
        assertTrue(Boolean.TRUE);
    }


    /**
     * interval extract(
     *
     * @throws Exception
     */
    @Test
    public void test6() throws Exception {
        String sql="SELECT A_13104_722_1631695157575, A_13104_324_1631695157575 FROM ( SELECT 产品线 AS A_13104_722_1631695157575, SUM(与上个月的用量变化) AS A_13104_324_1631695157575 FROM (SELECT\n" +
                "date_sub(date_format(now(),'%y-%m-%d'),interval extract(day from now()) day) AS '上月日期', REV.S2 AS '产品线', REV.S3 AS '加速类型',(REV.S1-WIN.S1) AS '与上个月的用量变化',REV.S1 AS '上个月的用量', WIN.S1 AS '上上个月的用量'\n" +
                "FROM\n" +
                "(\n" +
                "SELECT SUM(resource_cost.usage) AS 'S1',resource_cost.account  AS 'S2', resource_cost.usage_type_cname AS 'S3' FROM resource_cost WHERE resource_cost.period_day = date_sub(date_sub(date_format(now(),'%y-%m-%d'),interval extract(\n" +
                "day from now()) day),interval, 1, month)  AND resource_cost.`service_cname` = 'cdn' AND resource_cost.`region1` = '国内' GROUP BY S2, S3\n" +
                ") WIN,\n" +
                " (\n" +
                "SELECT SUM(resource_cost.usage) AS 'S1',resource_cost.account  AS 'S2', resource_cost.usage_type_cname AS 'S3' FROM resource_cost WHERE resource_cost.period_day = date_sub(date_sub(date_format(now(),'%y-%m-%d'),interval extract(\n" +
                "day from now()) day),interval, 0, month)  AND resource_cost.`service_cname` = 'cdn' AND resource_cost.`region1` = '国内' GROUP BY S2, S3\n" +
                ") REV\n" +
                "where WIN.S2 = REV.S2 AND WIN.S3 = REV.S3) sql_model_virtual_table_new_13104_1581 WHERE 1=1 GROUP BY 产品线 HAVING  SUM(与上个月的用量变化) > 50000)orderBy_nested ORDER BY A_13104_324_1631695157575 desc LIMIT 5000";
        SmartQLEngine.parse(sql);
        assertTrue(Boolean.TRUE);
    }


    @Test
    public void test6_1() throws Exception {
        String sql = "SELECT SUM(resource_cost.usage) AS 'S1',resource_cost.account  AS 'S2', resource_cost.usage_type_cname AS 'S3' FROM resource_cost WHERE resource_cost.period_day = date_sub(date_sub(date_format(now(),'%y-%m-%d'), interval, extract(\n" +
                "day from now()), day), interval, 1, month)  AND resource_cost.`service_cname` = 'cdn' AND resource_cost.`region1` = '国内' GROUP BY S2, S3";
        SmartQLParser parser = SmartQLEngine.newParser(sql);
//        printToken(parser);
        SmartQLEngine.parse(sql);

        System.out.println("11");
    }


    /**
     * key word 'update'
     * 解决方案：增加``限制
     *
     * @throws Exception
     */
    @Test
    public void test7() throws Exception {
        SmartQLEngine.parse("SELECT\n" +
                "branch_name,\n" +
                "org_name,\n" +
                "category,\n" +
                "org_sale_id,\n" +
                "org_id,\n" +
                "create_time,\n" +
                "diff_month,\n" +
                "SUM(CASE WHEN xsed>=1 then xsed else 0 end)+SUM(CASE WHEN org_sale_id<>org_id and shed>=1 then shed else 0 end) as KYED,\n" +
                "CASE WHEN dim_ym<>'' THEN dim_ym else LEFT(CURRENT_DATE(),7) end as dim_ym,\n" +
                "sum(CASE WHEN renshu_a>=1 then renshu_a else 0 end)+sum(CASE WHEN org_sale_id<>org_id and renshu_b>=1 then renshu_b else 0 end) as sjrs,\n" +
                "CASE WHEN category='授权店' and sum(WDL)>60 then '1人'\n" +
                "    WHEN category='授权店' and sum(WDL)<60 then '兼职'\n" +
                "    WHEN category<>'授权店' and sum(WDL)>250 then '2人'\n" +
                "    WHEN category<>'授权店' and sum(WDL)<250 then '1人'\n" +
                "    else '1人'\n" +
                "    end as jyrs,\n" +
                "CASE WHEN category='授权店' and sum(WDL)>0 then '盈利'\n" +
                "    WHEN category='授权店' and sum(WDL)<1 then '亏损'\n" +
                "    WHEN category<>'授权店' and sum(WDL)>=250  then  '盈利'\n" +
                "    WHEN dim_ym<LEFT(CURRENT_DATE(),7) and category<>'授权店' and sum(WDL)<250 and sum(WDL)*67>=6000  then  '盈利'\n" +
                "    WHEN dim_ym=LEFT(CURRENT_DATE(),7) and category<>'授权店' and sum(WDL)<250 and sum(WDL)*67>=day(CURRENT_DATE())*200  then  '盈利'\n" +
                "    ELSE  '亏损'\n" +
                "    end as ylcs,\n" +
                "CASE WHEN rate<>'' then rate else '0.0%' end as rate,\n" +
                "sum(WDL) as WDL,\n" +
                "sum(THL) as THL,\n" +
                "sum(QXL) as QXL,\n" +
                "CASE WHEN dim_ym<>'' then '运营中'  else '长期无量' end as dabiao\n" +
                "\n" +
                "FROM\n" +
                "(\n" +
                "SELECT\n" +
                "yl.branch_name,yl.org_name,yl.org_sale_id,yl.org_id,yl.category,\n" +
                "CASE WHEN yl.org_sale_id=yl.org_id then substr(yl.`update`,1,10)\n" +
                "     ELSE from_unixtime(org.create_time,'yyyy-MM-dd') end as create_time,\n" +
                "CASE WHEN yl.org_sale_id=yl.org_id then Round((DATEDIFF(CURRENT_DATE(),substr(yl.`update`,1,10))/30),0) \n" +
                "    ELSE Round((DATEDIFF(CURRENT_DATE(),from_unixtime(org.create_time,'yyyy-MM-dd'))/30),0)  end as diff_month,\n" +
                "wxl.dim_ym,wxl.WDL,wxl.QXL,wxl.THL,\n" +
                "rate.rate,\n" +
                "rsa.renshu_a,rsb.renshu_b,\n" +
                "ed_a.xsed,ed_b.shed\n" +
                "FROM\n" +
                "hive_zjyprc_hadoop.tmp.excel_store_org_id_all yl\n" +
                "left join hive_zjyprc_hadoop.info.ods_xmsbe_t_base_org org on org.id=yl.org_id\n" +
                "left join --业务量\n" +
                "    (SELECT \n" +
                "    dim.dim_ym,\n" +
                "    o.org_sales_code,\n" +
                "    SUM(case WHEN c.service_status_desc='业务完成' and c.service_type_desc not in('退货') then c.total else 0 end)as WDL,--完单量不含退货\n" +
                "    SUM(case WHEN c.service_status_desc='取消完成' then c.total else 0 end) as QXL,--取消总量\n" +
                "    SUM(case WHEN c.service_status_desc='业务完成' and c.service_sub_type_desc in('退货') then c.total else 0 end) as THL--退货\n" +
                "    FROM (SELECT * FROM hive_zjyprc_hadoop.info_afdata.etl_xms_srv_close_num where service_status_desc in('业务完成','取消完成') and service_way_desc<>'到家' and org_type_desc in('米家','米家专卖店','第三方网点','授权店') and service_close_date>'2022-01-01') c\n" +
                "    LEFT JOIN hive_zjyprc_hadoop.info_excel.excel_store_org_id o on c.service_org_id=o.org_id\n" +
                "    LEFT JOIN hive_zjyprc_hadoop.info_afdata.base_format_dim_date dim on c.service_close_date = dim.dim_date\n" +
                "    Group by dim.dim_ym,o.org_sales_code\n" +
                "    ) wxl on wxl.org_sales_code= yl.org_sale_id\n" +
                "left join --转化率\n" +
                "    (SELECT\n" +
                "    dim.dim_ym,\n" +
                "    r.org_sales_code,\n" +
                "    Concat(Round((sum(r.order_cnt)/sum(r.service_cnt)*100),2),'%')  as rate\n" +
                "    FROM\n" +
                "    hive_zjyprc_hadoop.info_afdata.dws_service_sale_convert_rate r \n" +
                "    left join hive_zjyprc_hadoop.info_afdata.base_format_dim_date dim on dim.dim_date = r.service_close_date\n" +
                "    Group by dim.dim_ym,r.org_sales_code\n" +
                "    order by dim.dim_ym\n" +
                "    ) rate on rate.org_sales_code =yl.org_sale_id and rate.dim_ym = wxl.dim_ym\n" +
                "left join --销售机构工程师人数\n" +
                "    (SELECT\n" +
                "    org_id,COUNT(DISTINCT miliao) as renshu_a\n" +
                "    FROM hive_zjyprc_hadoop.info_afdata.dwd_certificate_assign_detail\n" +
                "    WHERE \n" +
                "    miliao_status<>'无效' and authenticate_status='已通过' and org_type_desc in ('米家专卖店','第三方网点','米家','授权店') and (certificate_name ='送修-初级技术-手机' or certificate_name ='送修-中级技术-手机'or certificate_name ='送修-初级技术-笔记本')\n" +
                "    group by org_id\n" +
                "    ) rsa on rsa.org_id = yl.org_sale_id\n" +
                "left join --售后机构工程师人数\n" +
                "    (SELECT\n" +
                "    org_id,COUNT(DISTINCT miliao) as renshu_b\n" +
                "    FROM hive_zjyprc_hadoop.info_afdata.dwd_certificate_assign_detail\n" +
                "    WHERE \n" +
                "    miliao_status<>'无效' and authenticate_status='已通过' and org_type_desc in ('米家专卖店','第三方网点','米家','授权店') and (certificate_name ='送修-初级技术-手机' or certificate_name ='送修-中级技术-手机'or certificate_name ='送修-初级技术-笔记本')\n" +
                "    group by org_id\n" +
                "    ) rsb on rsb.org_id = yl.org_id\n" +
                "\n" +
                "left join --销售机构额度\n" +
                "    (SELECT\n" +
                "    org_id,total_deposit as xsed\n" +
                "    FROM hive_zjyprc_hadoop.info_afdata.etl_org_deposit_amount \n" +
                "    ) ed_a on ed_a.org_id = yl.org_sale_id\n" +
                "\n" +
                "left join --售后机构额度\n" +
                "    (SELECT\n" +
                "    org_id,total_deposit as shed\n" +
                "    FROM hive_zjyprc_hadoop.info_afdata.etl_org_deposit_amount \n" +
                "    ) ed_b on ed_b.org_id = yl.org_id\n" +
                "where yl.operate='是'\n" +
                ")\n" +
                "Group by\n" +
                "branch_name,\n" +
                "org_name,\n" +
                "category,\n" +
                "org_sale_id,\n" +
                "org_id,\n" +
                "create_time,\n" +
                "diff_month,\n" +
                "dim_ym,\n" +
                "rate");
        assertTrue(Boolean.TRUE);
    }


    /**
     * with
     *
     * @throws Exception
     */
    @Test
    public void test8() throws Exception {
        SmartQLEngine.parse("with a as(\n" +
                "SELECT year,month,income_type,\n" +
                "revenue\n" +
                "from appstore_budget_m\n" +
                "where `update`='V1'\n" +
                "),--预算表 \n" +
                "b as(\n" +
                "SELECT year,month,income_type,\n" +
                "revenue\n" +
                "from appstore_budget_m\n" +
                "where `update`='V4'\n" +
                "),--预算表 \n" +
                "\n" +
                "sdgg as (\n" +
                "    select date,\n" +
                "    mediaType as type,\n" +
                "    sum(feeEffect+ feeBrand+ feertb+feeincomeinternal-feeexpenseinternal) as income,\n" +
                "    SUM(feeEffect+feeRtb+feeBrand+feeIncomeInternal-feeExpenseInternal)*0.8 as revenue\n" +
                "    from new_palo_event_cube_appstore_di\n" +
                "    group by date,type\n" +
                "    ),--商店广告\n" +
                "\n" +
                "yyly as (\n" +
                "\n" +
                "SELECT date,type,income,revenue,gross_profit as profit\n" +
                "from appstore_game_income_d\n" +
                "where position='all'\n" +
                "union all\n" +
                "SELECT date,type,sum(income) as income,sum(revenue) as revenue,sum(gross_profit) as profit\n" +
                "from appstore_game_income_d\n" +
                "where type='sdk'\n" +
                "group by date,type\n" +
                "),--游戏收入\n" +
                "\n" +
                "\n" +
                "\n" +
                "taginfo as (\n" +
                "    select distinct tag_id,ext_id from dim_app_info_all_df where tag_id in(60,62)\n" +
                "),\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "lyzz as (\n" +
                "    select '应用联运-增值' as type,\n" +
                "    date,\n" +
                "    sum(pay.pay_amt) income,\n" +
                "    sum(pay.predict_income) revenue,\n" +
                "    sum(profit) profit\n" +
                "    from (\n" +
                "        select date,ext_id,pay_amt,predict_income,profit\n" +
                "        from ads_lianyunsdk_payment_ltv_d where cluster_name='zjyprc-hadoop' and model='ALL' and cid='ALL' and ext_id<>'ALL' \n" +
                "    ) pay\n" +
                "    join\n" +
                "    taginfo\n" +
                "    on pay.ext_id=taginfo.ext_id\n" +
                "    group by date,type\n" +
                ")--联运增值\n" +
                "\n" +
                ",lygg as (\n" +
                "    select '应用联运-广告' as type,\n" +
                "    date,\n" +
                "    sum(origin_gain)/100 as income,\n" +
                "    sum(origin_gain)/100*0.83 as revenue,\n" +
                "    sum(origin_gain)/100-sum(gain)/100 as profit\n" +
                "    from (\n" +
                "        select date,ext_id,origin_gain,gain from ads_lianyunsdk_mimeng_detail_d  where cluster_name='zjyprc-hadoop' and ext_id<>'ALL'\n" +
                "    ) mm\n" +
                "    join\n" +
                "    taginfo\n" +
                "    on mm.ext_id=taginfo.ext_id\n" +
                "    group by date,type\n" +
                "    \n" +
                "),--联运广告\n" +
                "all_data as (\n" +
                "\n" +
                "select (case \n" +
                "when type='sdk' then '游戏-sdk'\n" +
                "when type='tencent' then '游戏-腾讯'\n" +
                "when type='APP_STORE' then '商店广告-商店'\n" +
                "when type='SAFE_CENTER' or type='PACKAGE_INSTALLER' then '商店广告-安装器'\n" +
                "else type end) as type,\n" +
                "date,\n" +
                "y,\n" +
                "q,\n" +
                "m,\n" +
                "income,\n" +
                "revenue,\n" +
                "profit\n" +
                "    from(\n" +
                "        select \n" +
                "        date,\n" +
                "        type,\n" +
                "        cast(substr(date,1,4) as INTEGER) as y,\n" +
                "        cast((substr(date,5,2)/3.1) as INTEGER)+1 as q,\n" +
                "        cast(substr(date,5,2) as INTEGER) as m,\n" +
                "        sum(income) as income ,\n" +
                "        sum(revenue) as revenue,\n" +
                "        sum(profit) as profit\n" +
                "        from lygg\n" +
                "        group by date,type\n" +
                "        order by date desc \n" +
                "    union ALL\n" +
                "        select \n" +
                "        date,\n" +
                "        type,\n" +
                "        cast(substr(date,1,4) as INTEGER) as y,\n" +
                "        cast((substr(date,5,2)/3.1) as INTEGER)+1 as q,\n" +
                "        cast(substr(date,5,2) as INTEGER) as m,\n" +
                "        sum(income) as income ,\n" +
                "        sum(revenue) as revenue,\n" +
                "        sum(profit) as profit\n" +
                "        from lyzz\n" +
                "        group by date,type\n" +
                "        order by date desc\n" +
                "\n" +
                "    union ALL\n" +
                "        select \n" +
                "        date,\n" +
                "        type,\n" +
                "        cast(substr(date,1,4) as INTEGER) as y,\n" +
                "        cast((substr(date,5,2)/3.1) as INTEGER)+1 as q,\n" +
                "        cast(substr(date,5,2) as INTEGER) as m,\n" +
                "        sum(income) as income ,\n" +
                "        sum(revenue) as revenue,\n" +
                "        0 as profit\n" +
                "        from sdgg\n" +
                "        group by date,type\n" +
                "        order by date desc\n" +
                "    union all\n" +
                "        select \n" +
                "        date,\n" +
                "        type,\n" +
                "        cast(substr(date,1,4) as INTEGER) as y,\n" +
                "        cast((substr(date,5,2)/3.1) as INTEGER)+1 as q,\n" +
                "        cast(substr(date,5,2) as INTEGER) as m,\n" +
                "        sum(income) as income ,\n" +
                "        sum(revenue) as revenue,\n" +
                "        sum(profit) as profit\n" +
                "        from yyly\n" +
                "        group by date,type\n" +
                "        order by date desc \n" +
                "    )aa     \n" +
                ")\n" +
                "select all_data.date,y,m,q,type,a.revenue as budget_revenue,b.revenue as Q4_budget_revenue,income,all_data.revenue,profit\n" +
                "from\n" +
                "all_data left join  a on y=a.year and m=a.month and all_data.type=a.income_type \n" +
                "left join  b on y=b.year and m=b.month and all_data.type=b.income_type \n");
        assertTrue(Boolean.TRUE);
    }


    /**
     * 特殊字符"%�%"
     */
    @Test
    public void test9() throws Exception {
        SmartQLEngine.parse("select\n" +
                "\ta.date\n" +
                "\t,lower(a.dsp_level2) as dspLevel2\n" +
                "\t,lower(d.dsp_level1) as dspLevel1\n" +
                "\t,(case when g.tag_id is not null and g.tag_id !=\"-\" then g.tag_id else a.tag_id end) as tagId \n" +
                "\t,a.region\n" +
                "\t,(case when h.ssp_placement_id is not null and h.ssp_placement_id !=\"-\" then h.ssp_placement_id else a.tag_id end) as dspPlacementId\n" +
                "\t,a.package_name \n" +
                "\t,a.max_receive_count as maxReceiveCount\n" +
                "\t,a.max_request_count as maxRequestCount\n" +
                "\t,a.max_request_ads_count as maxRequestAdsCount\n" +
                "\t,a.dsp_return_count as dspReturnCount\n" +
                "\t,a.dsp_return_ads_count as dspReturnAdsCount\n" +
                "\t,a.dsp_win_count as dspWinCount\n" +
                "\t,a.dsp_win_ads_count as dspWinAdsCount\n" +
                "\t,a.max_win_count  as maxWinCount\n" +
                "\t,a.raw_view as rawView\n" +
                "\t,a.raw_click as rawClick\n" +
                "\t,a.distinct_raw_view as DistinctRawView\n" +
                "    ,a.distinct_raw_click as DistinctRawClick\n" +
                "    ,a.ecpm_profit as ecpmProfit\n" +
                "\t,a.dsp_fee as dspFee\n" +
                "\t,a.dsp_view as dspView\n" +
                "\t,a.dsp_click as dspClick\n" +
                "\t,a.dsp_active as dspActive\n" +
                "\t,a.distinct_raw_view_day\n" +
                "\t,a.distinct_raw_click_day\n" +
                "\t,a.ecpm_profit_day\n" +
                "\t,a.proportion_ecpm_day\n" +
                "    ,concat(e.ad_form_id,\"_\",e.ad_form_name) as tag_form\n" +
                "    ,concat(b.media_type,\"_\",b.media_name) as media\n" +
                "\t,c.region_name\n" +
                "\t,a.media_pkg\n" +
                "\t,a.source\n" +
                "from \n" +
                "\t(select * from doris_alsgprc_xiaomi.global_ad_bi.columbus_full_link_analysis where dsp_level2 not like \"%�%\" and tag_id not like \"%�%\") a\n" +
                "left join\n" +
                "\t( \n" +
                "\tselect \n" +
                "\t\tmi_dsp_level2 as dsp_level2\n" +
                "\t\t,mi_dsp_level1 as dsp_level1\n" +
                "\tfrom doris_alsgprc_xiaomi.global_ad_bi.dim_global_bi_mapping_table\n" +
                "\tgroup by mi_dsp_level2,mi_dsp_level1\n" +
                ") d\n" +
                "on lower(a.dsp_level2)=lower(d.dsp_level2)\n" +
                "left join \n" +
                "\tdoris_alsgprc_xiaomi.global_ad_bi.dim_global_region c\n" +
                "on a.region=c.region\n" +
                "left join\n" +
                "    (select mi_placement_id\n" +
                "                , ssp_placement_id\n" +
                "                from  doris_alsgprc_xiaomi.global_ad_bi.dim_global_bi_mapping_table\n" +
                "                where mi_placement_id !=\"-\"\n" +
                "                group by\n" +
                "                mi_placement_id\n" +
                "                ,ssp_placement_id) h\n" +
                "on a.tag_id = h.mi_placement_id\n" +
                "left join\n" +
                "    (select  distinct ssp_placement_id ,tag_id\n" +
                "                from  doris_alsgprc_xiaomi.global_ad_bi.dim_global_bi_mapping_table\n" +
                "                where ssp_placement_id !=\"-\"\n" +
                "\t) g\n" +
                "on h.ssp_placement_id=g.ssp_placement_id\n" +
                "left join  \n" +
                "(\n" +
                "    select ssp_placement_id,media_type,media_name\n" +
                "    from doris_alsgprc_xiaomi.global_ad_bi.dim_global_tag_pid\n" +
                "    group by ssp_placement_id,media_type,media_name\n" +
                ") b\n" +
                "on g.tag_id = b.ssp_placement_id\n" +
                "left join \n" +
                "( \n" +
                "    select ssp_placement_id,ad_form_id,ad_form_name\n" +
                "    from doris_alsgprc_xiaomi.global_ad_bi.dim_global_tag_pid\n" +
                "    group by ssp_placement_id,ad_form_id,ad_form_name\n" +
                " ) e\n" +
                "on g.ssp_placement_id=e.ssp_placement_id\n");
        assertTrue(Boolean.TRUE);
    }


    /**
     * current row
     */
    @Test
    public void test10() throws Exception {
        SmartQLEngine.parse("WITH polaris AS\n" +
                "(\n" +
                "SELECT grow.date\n" +
                ",grow.hour\n" +
                ",grow.package_name\n" +
                ",grow.deeplink_app_name\n" +
                ",grow.app_id\n" +
                ",SUM(grow.button_ad_mnt) AS button_ad_mnt\n" +
                ",SUM(grow.ad_mnt) AS ad_mnt\n" +
                ",SUM(grow.cost) AS cost\n" +
                ",SUM(grow.view_cnt) AS view_cnt\n" +
                ",SUM(grow.click) AS click\n" +
                ",SUM(grow.conversions) AS conversions\n" +
                ",SUM(ifnull(threshold,0)) AS threshold\n" +
                "FROM\n" +
                "(\n" +
                "SELECT t1.date\n" +
                ",t1.hour\n" +
                ",t2.package_name\n" +
                ",t2.deeplink_app_name\n" +
                ",t3.app_id\n" +
                ",t1.advertiser_id\n" +
                ",SUM(t1.button_ad_mnt) AS button_ad_mnt\n" +
                ",SUM(t1.ad_mnt) AS ad_mnt\n" +
                ",SUM(cost) AS cost\n" +
                ",SUM(view_cnt) AS view_cnt\n" +
                ",SUM(click) AS click\n" +
                ",SUM(conversions) AS conversions\n" +
                "FROM\n" +
                "(\n" +
                "SELECT `date`\n" +
                ",hour\n" +
                ",advertiser_id\n" +
                ",creative_id\n" +
                ",SUM(button_ad_mnt) AS button_ad_mnt\n" +
                ",SUM(ad_mnt) AS ad_mnt\n" +
                "FROM ads_polaris_realtime_indicators\n" +
                "WHERE `date`>=''\n" +
                "AND product_id='1630659810998'\n" +
                "AND is_session=1\n" +
                "GROUP BY date,hour,advertiser_id,creative_id\n" +
                ") t1\n" +
                "INNER JOIN\n" +
                "(\n" +
                "SELECT game_id\n" +
                ",media_station\n" +
                ",advertiser_id\n" +
                ",creative_id\n" +
                ",dp_package_name AS package_name\n" +
                ",dp_app_name AS deeplink_app_name\n" +
                "FROM dim_p_media_creative_sub\n" +
                "WHERE game_id='1630659810998'\n" +
                "AND dp_package_name!=''\n" +
                ") t2\n" +
                "ON t1.advertiser_id=t2.advertiser_id AND t1.creative_id=t2.creative_id\n" +
                "INNER JOIN game_info_all t3\n" +
                "ON t2.package_name=t3.package_name\n" +
                "LEFT JOIN\n" +
                "(\n" +
                "SELECT date\n" +
                ",concat(str_to_date(cast(date AS string),'%Y%m%d'),' ',lpad(cast(hour AS string),2,'0')) AS hour\n" +
                ",advertiser_id\n" +
                ",creative_id\n" +
                ",SUM(cost) AS cost\n" +
                ",SUM(view_cnt) AS view_cnt\n" +
                ",SUM(click) AS click\n" +
                ",SUM(conversions) AS conversions\n" +
                "FROM ads_media_report_data_hour\n" +
                "WHERE date>=''\n" +
                "AND data_type='CREATIVITY'\n" +
                "GROUP BY `date`,hour,advertiser_id,creative_id\n" +
                ") t4\n" +
                "ON t1.date=t4.date AND t1.hour=t4.hour AND t1.advertiser_id=t4.advertiser_id AND t1.creative_id=t4.creative_id\n" +
                "GROUP BY t1.date,t1.hour,t2.package_name,t2.deeplink_app_name,t3.app_id,t1.advertiser_id\n" +
                ") grow\n" +
                "LEFT JOIN p_media_account\n" +
                "ON grow.date=p_media_account.date AND grow.advertiser_id=p_media_account.account_id\n" +
                "WHERE p_media_account.date>=''\n" +
                "GROUP BY grow.date,grow.hour,grow.package_name,grow.deeplink_app_name,grow.app_id\n" +
                "), base AS\n" +
                "(\n" +
                "SELECT polaris.date\n" +
                ",polaris.hour\n" +
                ",polaris.package_name\n" +
                ",polaris.deeplink_app_name\n" +
                ",polaris.app_id\n" +
                ",button_ad_mnt\n" +
                ",ad_mnt\n" +
                ",cost\n" +
                ",view_cnt\n" +
                ",click\n" +
                ",conversions\n" +
                ",threshold\n" +
                ",ifnull(new_active_cnt,0) AS new_active_cnt\n" +
                ",ifnull(iaa_income1,0) AS iaa_income1\n" +
                "FROM polaris\n" +
                "JOIN\n" +
                "(\n" +
                "SELECT dt\n" +
                ",hour\n" +
                ",package_name\n" +
                ",SUM(new_active_cnt) AS new_active_cnt\n" +
                ",SUM(iaa_income1/100000) AS iaa_income1\n" +
                "FROM ads_emi_postback_realtime_statistics\n" +
                "WHERE dt>=''\n" +
                "GROUP BY dt,hour,package_name\n" +
                ") emi\n" +
                "ON polaris.date=emi.dt AND polaris.hour=emi.hour AND polaris.package_name=emi.package_name\n" +
                ")\n" +
                "SELECT date\n" +
                ",hour\n" +
                ",package_name\n" +
                ",deeplink_app_name\n" +
                ",app_id\n" +
                ",ifnull(ad_mnt/100,0) AS ad_mnt\n" +
                ",ifnull(button_ad_mnt/100,0) AS button_ad_mnt\n" +
                ",ifnull(cost/100,0) AS cost\n" +
                ",ifnull(view_cnt,0) AS view_cnt\n" +
                ",ifnull(click,0) AS click\n" +
                ",ifnull(conversions,0) AS conversions\n" +
                ",ifnull(threshold,0) AS threshold\n" +
                ",ifnull(new_active_cnt,0) AS new_active_cnt\n" +
                ",ifnull(iaa_income1,0) AS iaa_income1\n" +
                "FROM\n" +
                "(\n" +
                "SELECT `date`\n" +
                ",hour\n" +
                ",package_name\n" +
                ",deeplink_app_name\n" +
                ",app_id\n" +
                ",SUM(ad_mnt) over(partition by date,package_name,deeplink_app_name,base.app_id ORDER BY hour rows BETWEEN unbounded preceding AND current row) AS ad_mnt\n" +
                ",SUM(button_ad_mnt) over(partition by date,package_name,deeplink_app_name,base.app_id ORDER BY hour rows BETWEEN unbounded preceding AND current row) AS button_ad_mnt\n" +
                ",SUM(view_cnt) over(partition by date,package_name,deeplink_app_name,base.app_id ORDER BY hour rows BETWEEN unbounded preceding AND current row) AS view_cnt\n" +
                ",SUM(click) over(partition by date,package_name,deeplink_app_name,base.app_id ORDER BY hour rows BETWEEN unbounded preceding AND current row) AS click\n" +
                ",SUM(conversions) over(partition by date,package_name,deeplink_app_name,base.app_id ORDER BY hour rows BETWEEN unbounded preceding AND current row) AS conversions\n" +
                ",SUM(cost) over(partition by date,package_name,deeplink_app_name,base.app_id ORDER BY hour rows BETWEEN unbounded preceding AND current row) AS cost\n" +
                ",threshold\n" +
                ",SUM(new_active_cnt) over(partition by date,package_name,deeplink_app_name,base.app_id ORDER BY hour rows BETWEEN unbounded preceding AND current row) AS new_active_cnt\n" +
                ",SUM(iaa_income1) over(partition by date,package_name,deeplink_app_name,base.app_id ORDER BY hour rows BETWEEN unbounded preceding AND current row) AS iaa_income1\n" +
                "FROM base\n" +
                ") temp");
        assertTrue(Boolean.TRUE);
    }


    /**
     * broadcast
     * 通过Holdor 函数来解决，即通过holdor占位符做最后替换
     */
    @Test
    public void test11() throws Exception {
        String sql = "SELECT if(retain2_cnt>0,from_unixtime(basetime,'%Y%m%d'),dt) AS date\n" +
                ",if(retain2_cnt>0,from_unixtime(basetime,'%Y-%m-%d %H'),hour) AS hour\n" +
                ",if(retain2_cnt>0,from_unixtime(basetime,'%Y-%m-%d %H:%i'),minute) AS minute\n" +
                ",tagid\n" +
                ",mediatype\n" +
                ",industrylevel1\n" +
                ",industrylevel2\n" +
                ",t1.app_id\n" +
                ",t1.package_name\n" +
                ",t2.game_name\n" +
                ",campaign_id\n" +
                ",campaign_name\n" +
                ",campaign_status\n" +
                ",campaign_day_budget AS campaign_day_budget\n" +
                ",cost/100000 AS cost\n" +
                ",click_cnt\n" +
                ",start_down_cnt\n" +
                ",new_active_cnt\n" +
                ",active_cnt\n" +
                ",register_cnt\n" +
                ",down_success_cnt\n" +
                ",install_success_cnt\n" +
                ",retain2_cnt\n" +
                ",pay_cnt\n" +
                ",buy_cnt\n" +
                ",order_cnt\n" +
                ",credit_cnt\n" +
                ",upload_idcard_cnt\n" +
                ",iaa_income/100000 as iaa_income\n" +
                ",iaa_income1/100000 as iaa_income1\n" +
                "FROM ads_emi_postback_realtime_statistics t1\n" +
                "LEFT JOIN holdor('[broadcast]') game_info_all t2\n" +
                "ON t1.package_name=t2.package_name";
        printToken(sql);
        SmartQLEngine.parse(sql);
        assertTrue(Boolean.TRUE);
    }


    @Test
    public void test1() throws Exception {
        String sql = "SELECT abc,edf from abc union all (select edf,addf from efg)";
        SmartQLParser parser = SmartQLEngine.newParser(sql);
        Token tk = null;
        do {
            tk = parser.getNextToken();
            System.out.println(tk.toString());

        } while (StringUtils.isNotEmpty(tk.toString()));

        SmartQLEngine.parse(sql);
        assertTrue(Boolean.TRUE);
    }

    /**
     * keyword "row"
     *
     * @throws Exception
     */
    @Test
    public void test12() throws Exception {
        String sql = "WITH t_domain_total AS(\n" +
                "    SELECT\n" +
                "        date,\n" +
                "        indicator_name,\n" +
                "        indicator_pv AS indicator_pv_sum\n" +
                "    FROM\n" +
                "        dss_negative_feedback_indicator\n" +
                "    where\n" +
                "        device_name='miai'\n" +
                "        and indicator_name in ('repeat_intention')\n" +
                "        and domain='all'\n" +
                "),\n" +
                "\n" +
                "t_domain_v as (\n" +
                "SELECT\n" +
                "    *\n" +
                "FROM\n" +
                "(\n" +
                "SELECT\n" +
                "    *,\n" +
                "    row_number() over(PARTITION BY domain order by date desc) as rank\n" +
                "FROM(\n" +
                "SELECT\n" +
                "    --concat(substr(date,1,4),'-',if(weekofyear(date)<10,concat('0',weekofyear(date)),weekofyear(date))) as date,\n" +
                "    date,\n" +
                "    domain,\n" +
                "    indicator_name,\n" +
                "    indicator_pv,\n" +
                "    indicator_pv_week,\n" +
                "    indicator_value,\n" +
                "    indicator_value_week,\n" +
                "    influnce_value,\n" +
                "    influnce_value_week,\n" +
                "    influnce_nums,\n" +
                "    influnce_nums_week,\n" +
                "    indicator_nums,\n" +
                "    indicator_nums_week,\n" +
                "    domain_rate_week\n" +
                "FROM\n" +
                "(\n" +
                "SELECT\n" +
                "    date,\n" +
                "    domain,\n" +
                "    case \n" +
                "    when indicator_name='repeat_intention' then '重说'\n" +
                "    else '其他'\n" +
                "    end as indicator_name,\n" +
                "    indicator_pv,\n" +
                "     avg(1.0*indicator_pv) OVER(PARTITION BY domain,indicator_name ORDER BY date  ROWS BETWEEN 6 PRECEDING AND CURRENT ROW) AS indicator_pv_week,\n" +
                "    indicator_value,\n" +
                "     avg(1.0*indicator_value) OVER(PARTITION BY domain,indicator_name ORDER BY date  ROWS BETWEEN 6 PRECEDING AND CURRENT ROW) AS indicator_value_week,\n" +
                "    influnce_value,\n" +
                "     avg(1.0*influnce_value) OVER(PARTITION BY domain,indicator_name ORDER BY date  ROWS BETWEEN 6 PRECEDING AND CURRENT ROW) AS influnce_value_week,\n" +
                "    influnce_nums,\n" +
                "     avg(1.0*influnce_nums) OVER(PARTITION BY domain,indicator_name ORDER BY date  ROWS BETWEEN 6 PRECEDING AND CURRENT ROW) AS influnce_nums_week,\n" +
                "    indicator_nums,\n" +
                "     avg(1.0*indicator_nums) OVER(PARTITION BY domain,indicator_name ORDER BY date  ROWS BETWEEN 6 PRECEDING AND CURRENT ROW) AS indicator_nums_week,\n" +
                "    avg(1.0*domain_rate) OVER(PARTITION BY domain,indicator_name ORDER BY date  ROWS BETWEEN 6 PRECEDING AND CURRENT ROW) AS domain_rate_week\n" +
                "FROM(\n" +
                "SELECT\n" +
                "    concat(substr(t1.date,1,4),'-',substr(t1.date,5,2),'-',substr(t1.date,7,2)) as date,\n" +
                "    domain,\n" +
                "    indicator_pv,\n" +
                "    t1.indicator_name,\n" +
                "    round(indicator_pv/expose_pv,4) as indicator_value,\n" +
                "    round(indicator_pv/server_pv,4) as influnce_value,\n" +
                "    round(indicator_pv/server_pv*10000,2) as influnce_nums,\n" +
                "    round(indicator_pv/expose_pv*10000,2) as indicator_nums,\n" +
                "    round(1.0*indicator_pv/indicator_pv_sum,4) as domain_rate\n" +
                "FROM\n" +
                "    dss_negative_feedback_indicator t1 LEFT JOIN t_domain_total t2 ON t1.date=t2.date and t1.indicator_name=t2.indicator_name\n" +
                "where\n" +
                "    device_name='miai'\n" +
                "    and t1.indicator_name in ('repeat_intention')\n" +
                ") t\n" +
                ")  t1\n" +
                "where\n" +
                "    pmod(datediff(date, '1920-01-01') - 3, 7)=4\n" +
                ") t\n" +
                ") t2\n" +
                "where rank<=24\n" +
                "),\n" +
                "t_std as (\n" +
                "    SELECT\n" +
                "        concat(substr(date,1,4),'-',substr(date,5,2),'-',substr(date,7,2)) as date,\n" +
                "        domain,\n" +
                "        std,\n" +
                "        average\n" +
                "    FROM\n" +
                "        ads_user_repeat_rate_std_inc_d\n" +
                "    where\n" +
                "        device_name='miai'\n" +
                ")\n" +
                "SELECT\n" +
                "    t1.date as date1,\n" +
                "    t2.date as date2,\n" +
                "    t1.domain,\n" +
                "    t1.domain_rate_week as domain_rate_week1,\n" +
                "    t2.domain_rate_week as domain_rate_week2,\n" +
                "    t1.indicator_value_week as indicator_value_week1,\n" +
                "    t2.indicator_value_week as indicator_value_week2,\n" +
                "    t3.std*0.7 as std,\n" +
                "    t3.average as average,\n" +
                "    concat(round(100.0*t3.average,2),'%','(', round(100.0*t3.std*0.7,2),'%',')') as ma30,\n" +
                "    case when t2. indicator_value_week>t3.average+t3.std*0.7*2 and t2. indicator_value_week<t3.average+t3.std*0.7*3 THEN '偏高(超过2σ)'\n" +
                "    when t2. indicator_value_week>t3.average+t3.std*0.7*3 THEN '异常(超过3σ)'\n" +
                "    else '正常' end as is_exception\n" +
                "FROM\n" +
                "    t_domain_v as t1  join t_domain_v as t2 on t1.domain=t2.domain\n" +
                "    left join t_std t3 ON t1.date=t3.date and t1.domain=t3.domain\n" +
                "where\n" +
                "    t1.date!=t2.date\n" +
                "\n" +
                "\n";
        SmartQLEngine.parse(sql);
        assertTrue(Boolean.TRUE);
    }


    /**
     * 不支持此种语法 lateral view explode([field]) [alias] as [column]
     *
     * @throws Exception
     */
    @Test
    public void test13() throws Exception {
        String sql = "select t0.*,t1.*\n" +
                "from \n" +
                "(select distinct \n" +
                "adId,\n" +
                "videourl,\n" +
                "`素材类型`,\n" +
                "`标题`,\n" +
                "customerId,\n" +
                "companyName,\n" +
                "campaignId,\n" +
                "campaignName,\n" +
                "groupId,\n" +
                "groupName,\n" +
                "creativeName,\n" +
                "appId,\n" +
                "`app名称`,\n" +
                "accountIndustry,\n" +
                "accountIndustryName,\n" +
                "accountSecondIndustry,\n" +
                "accountSecondIndustryName,\n" +
                "placementtype,\n" +
                "stage,\n" +
                " get_json_object(imgmaterials, '$.id') id,\n" +
                " get_json_object(imgmaterials, '$.type') tp,\n" +
                " get_json_object(imgmaterials, '$.url') imgurl\n" +
                "from\n" +
                "(select \n" +
                "adId,\n" +
                "imgurl,\n" +
                "regexp_replace(video, '\"|\\\\[|\\\\]', '')as videourl,\n" +
                "if(video is null or video='',if(img is null or img='','','图片'),'视频')`素材类型`,\n" +
                "get_json_object(text,'$.title')`标题`,\n" +
                "customerId,\n" +
                "companyName,\n" +
                "campaignId,\n" +
                "campaignName,\n" +
                "groupId,\n" +
                "groupName,\n" +
                "creativeName,\n" +
                "appId,\n" +
                "get_json_object(product,'$.app_name.zh_CN')`app名称`,\n" +
                "accountIndustry,\n" +
                "accountIndustryName,\n" +
                "accountSecondIndustry,\n" +
                "accountSecondIndustryName,\n" +
                "placementtype,\n" +
                "stage,\n" +
                "split(\n" +
                "              regexp_replace(\n" +
                "                regexp_extract (imgmaterials, '(\\\\[)(.*?)(\\\\])', 2),\n" +
                "                '\\\\},\\\\{',\n" +
                "                '\\\\}|\\\\{'\n" +
                "              ),\n" +
                "              '\\\\|'\n" +
                "            ) as a_list\n" +
                "from miuiads.miui_ad_info lateral view explode(split(img,',')) t as imgurl\n" +
                "where imgmaterials!=''\n" +
                "and imgurl != 'undefined'\n" +
                ")a lateral view explode(a_list) a_list_tab as imgmaterials\n" +
                ")t1\n" +
                "left join\n" +
                "(select a.*,b.`新增激活`,b.`当日激活`,b.`新增当日激活`,b.`打点次留`,b.`当日次留`,b.`新增次留`,b.`新增当日次留`,\n" +
                "c.`ocpc激活`,c.`ocpc注册`,c.`ocpc留存`,c.`ocpc新增激活`,c.`ocpc召回`,c.`ocpc拉活`\n" +
                "from\n" +
                "(select from_unixtime(cast(ts/1000 as bigint),'yyyy-MM-dd') `日期`,\n" +
                "ad_id,\n" +
                "trigger_id,\n" +
                "tagid,\n" +
                "dspType,\n" +
                "dsp,\n" +
                "sum(e_view)`计费曝光`,\n" +
                "sum(e_click)`计费点击`,\n" +
                "sum(fee)/100000 `效果收入`,\n" +
                "sum(e_start_download)`开始下载`,\n" +
                "sum(e_end_download)`下载成功`,\n" +
                "sum(e_start_install)`开始安装`,\n" +
                "sum(e_install)`安装成功`,\n" +
                "sum(if(action_type='APP_ACTIVE',1,0))`打点激活`\n" +
                "from miuiads.stats_billing_multi_media\n" +
                "where date='20221101'\n" +
                "and bot_type <= 0\n" +
                "and bot_type_value <= 0\n" +
                "and dspType='effect'\n" +
                "group by from_unixtime(cast(ts/1000 as bigint),'yyyy-MM-dd'),\n" +
                "ad_id,\n" +
                "trigger_id,\n" +
                "tagid,\n" +
                "dspType,\n" +
                "dsp)a \n" +
                "left join \n" +
                "(select adId,\n" +
                "triggerId,\n" +
                "tagId,\n" +
                "\tsum(newActive)`新增激活`,\n" +
                "\tsum(dayActive)`当日激活`,\n" +
                "\tsum(newDayActive)`新增当日激活`,\n" +
                "\tsum(retention2)`打点次留`,\n" +
                "\tsum(dayRetention2)`当日次留`,\n" +
                "\tsum(newRetention2)`新增次留`,\n" +
                "\tsum(newDayRetention2)`新增当日次留`\n" +
                "from miuiads.dwm_ad_active_retention_d\n" +
                "where date='20221101'\n" +
                "group by adId,\n" +
                "triggerId,\n" +
                "tagId)b on (a.trigger_id=b.triggerId and a.tagid=b.tagId)\n" +
                "left join\n" +
                "(select \n" +
                "adId,\n" +
                "triggerId,\n" +
                "tagId,\n" +
                "\tsum(if(convType=1,1,0))`ocpc激活`,\n" +
                "\tsum(if(convType=2,1,0)) `ocpc注册`,\n" +
                "\tsum(if(convType=3,1,0))`ocpc留存`,\n" +
                "\tsum(if(convType=5,1,0))`ocpc新增激活`,\n" +
                "\tsum(if(convType=10009,1,0))`ocpc召回`,\n" +
                "\tsum(if(convType=10013,1,0)) `ocpc拉活`,\n" +
                "\tsum(if(convType=9,1,0)) `ocpc首次拉活`\n" +
                "from miuiads.dwm_ad_convert_data_d\n" +
                "where date='20221101'\n" +
                "group by adId,\n" +
                "triggerId,\n" +
                "tagId\n" +
                ")c on (a.trigger_id=c.triggerId and a.tagid=c.tagId)\n" +
                ")t0 on (t0.ad_id=t1.adId)";
//        printToken(sql);
        try {
            SmartQLEngine.parse(sql);
        } catch (Exception e) {
            assertTrue(Boolean.TRUE);
            return;
        }
        assertTrue(Boolean.FALSE);
    }


    /**
     * 问题原因，缺少",",'com.fzandroiduz.usbctsoptic.bp'
     *
     * @throws Exception
     */
    @Test
    public void test14() throws Exception {
        String sql = "select event_day,\n" +
                "SUM(origin_gain)/100/SUM(query_dau) as ARPU\n" +
                "from miuiads.media_publisher_stat\n" +
                "where date<=`20220101`\n" +
                "--and source_name='第三方APP'\n" +
                "and package_name in ('com.phonefangdajing.word','com.cssq.key','com.highmultiple.glass','com.happy.walker','com.moji.mjweather','com.shucha.find','cn.etouch.ecalendar','com.shyz.toutiao','com.clandroidxpe.pvctsspeed','com.cssq.walker','com.qianniu.dingwei','com.xqz.gao.qing.fdj','com.csxx.walker','com.cssq.wifi','com.shucha.jiejing','com.calendar2345','com.nineton.weatherforecast','com.ooyun.zongapp','cn.nineton.recordpro','com.textmagnification.king','com.kidguard360.parent','com.ffwei.d3map','com.zzlllll.mbh','com.keliandong.location','com.fubixing.fbxweather','com.kuxiong.phonelocation','com.nowcasting.activity','com.mengbinhe.earthmap','com.droi.adocker.pro','com.cssq.weather','com.jinyufen.d3qqmap','com.bnywl.weixing','com.hd.chargePlatform','com.bee.weathesafety','com.flakesnet.teleprompter','com.chif.weather','com.shanzhi.clicker','com.fnmei.gqwxjj','cn.xiuying.photoeditor.free','com.hudun.androidrecorder','com.position.radar','com.zgzx.weather','com.boniu.saomiaoquannengwang','com.weather.gorgeous','com.qianniu.earth','com.ajxun.sjzw','com.axiny.skdw','com.sdx.widget','com.growth.fgcalfun','com.radara.location','com.qsdwl.bxtq','com.graphic.enlarge','com.bianfeng.place','com.protect.family','com.flakesnet.kuaidingwei','com.ark.beautyweather.cn','com.example.android_youth','com.wifidashi.crack','com.xldposition.app.oledu','com.ai.face.play','com.qiguan.handwnl','com.splingsheng.ringtone','com.beidoujie.main','com.wifikey.wn','com.guangying.ai.solid','com.lml.phantomwallpaper','com.ai.faceshow','com.aikanqua.main','com.example.zqyears_java','com.zxhl.phonelocation','com.nixiang.faces','com.zxym.qqgqjj','com.supertuwen.tool','com.map.world.streetview','com.xunrui.duokai_box','com.moji.calendar','com.maiya.weather'\n" +
                "--3月8日新增4个包名\n" +
                ",'com.fzandroiduz.usbctsoptic.bp','com.powerful.magnifier','com.ubestkid.beilehu.android','com.mampod.ergedd',\n" +
                "--3月25日新增3个包名\n" +
                "'com.ark.careweather.cn','com.omnipotent.clean.expert','com.muhandroidlyf.rsuctsratio',\n" +
                "--4月22日新增3个包名\n" +
                "'com.ezlandroidpcd.brctseasy','com.mvideoc.qp','com.oqandroidtxz.hjctstech',\n" +
                "--6月30日新增\n" +
                "'com.snda.lantern.wifilocating','com.tianqiyubao2345','com.tianqi2345','com.browser2345','com.app3600eyes','com.app360eyes','com.qihoo.cleandroid_cn','com.geek.weathergj365','com.leaf.image.video.download','com.snda.wifilocating','com.shawn.nfcwriter','coding.yu.pythoncompiler.new','com.tony.usbcamera.molink','com.zdtc.ue.school','com.sagasoft.myreader','com.msc.tasker','com.hainanyksg.ynxx','com.boost.yidianclean.ydql','com.one.click.ido.screenshot','com.syid.measure','com.xiaoniu.master.cleanking','com.iodkols.onekeylockscreen','com.simi.screenlock','com.suyanapps.tuner','com.earn.zysx','com.idotools.qrcode','lf.example.wubi','com.geek.xgweather','com.wjp.gzhread','com.syido.weightpad','com.dotools.note','com.hodanet.charge','com.android.nwctsiu.dkhctstech','cn.ffxivsc','com.lmiot.autotool','com.almighty.speedy.king.app','com.omnipotent.clean.expert','com.neicunjiasu.boost.clean.ncjszs','com.boost.clean.ncjsql.cleaner','com.congbing.nonglin','com.mvtrail.mi.soundmeter','com.syido.decibel','com.github.crazyorr.interestcalculator','com.zhuoyue.cleaner.qingli.zyql','com.geek.jk.calendar.app','com.geek.jk.weather','com.jklight.weather','com.geek.jk.weather.jishi','com.huakaihualuo.pianpianbuyouji.musicarc','com.pd.dbmeter','com.supertuwen.tool','com.xunruifairy.wallpaper','com.sunnight.weather.wqtq','com.ibox.flashlight','com.ltt.compass','com.ibox.calculators','com.weatherday','com.cc.haokan.browser','com.syido.timer','com.likerain.weather.rytq','com.xiaoqiangiot','com.jimi.xsbrowser','io.iftech.android.box','cn.mediaio.mediaio','com.wtkj.app.clicker','com.fzandroiduz.usbctsoptic.bp','com.weatherfz2345','com.qiangli.cleaner.qlql','com.jy.recorder','com.Kingdee.Express','com.yueclean.toolcleaner','com.nasoft.socmark','com.hodanet.radiator','com.lich.lichlotter','com.jimi.zrwnl','com.ancda.parents','com.eternity.appstream','com.phonefangdajing.word','com.mhh.daytimeplay','com.geek.zwweather','com.geek.xycalendar','com.hyww.wisdomtree','com.zhineng.boost.qingli.znyhzs','com.franchise.booster.cn4.cube.clay','com.smartguard.qingli.cleaner.tool','com.icoolme.android.weather','com.shenyaocn.android.barmaker','com.nxtech.app.booster','com.tidy.trash.cleaner','com.jeejen.family','com.limited.cleaner.app','com.smkj.formatconverter','com.shenmi.jiuguan','com.maoxiaodan.fingerttest','com.sea.seaweatherapp','com.shyz.toutiao','com.hellogeek.cleanking','com.gangclub.gamehelper','com.bmx.apackage','com.aicleanyouhua.toolcleaner','com.i13.love.cleaner.tools','com.cleanmaster.mguard_cn','dian.chi','com.isyezon.kbatterydoctor','per.zhj.seefunc','com.xiaoniu.aidou','com.geek.jk.weather.fission','net.joydao.spring2011','com.furui.weather.happy','com.accurate.weather.local','com.xitong.cleaner.xtyhzs','cn.tools123.nettools','org.daai.netcheck','com.cylloveghj.metronomeplus','com.pd.metronome','com.huajiao.app.browser','com.mushroom.app.browser','cn.mediaio.rotate','com.pd.tongxuetimer','com.account.professor','com.geek.luck.calendar.app','com.yitong.weather','com.super.ss.phone.cleaner','com.mywallpaper.customizechanger','com.qujianpan.client','com.qujianpan.client.fast','com.xunlei.downloadprovider','com.lmiot.xyclick','com.hellogeek.iheshui','com.tiqiaa.icontrol','com.mvtrail.metaldetector.cn','com.jinpai.cleaner.qingli.jpql','com.Mqwjo.FCRby','cn.jiyihezi.happi123','com.readily.calculators','com.jijia.sjwnl','com.dongchu.yztq','com.geek.hxcalendar','com.mmc.fengshui.pass','cn.wind.speed.booster.cleaner','com.highmultiple.glass')\n" +
                "group by event_day";
        SmartQLEngine.parse(sql);
        assertTrue(Boolean.TRUE);
    }


    /**
     * start with connect by
     *
     * @throws Exception
     */
    @Test
    public void test15() throws Exception {
        String sql = "SELECT date AS A_10818_679_1626767695214,start AS A_10818_4_1626767683282,end AS A_10818_153_1626767683282,seq AS A_10818_594_1626767683282,radio AS " +
                "A_10792_350_1626766351771,radio2 AS A_10792_679_1626766351775,protocol_ver AS A_10792_110_1626766351782,app_ver AS A_10818_519_1626767683282 FROM " +
                "(select app_ver ,region ,date ,sum(total_count) as total ,sum(`start_b_count`) / sum(total_count) as start ,sum(`end_b_count`) / sum(total_count) " +
                "asend,sum(`radio_b_count`) / sum(total_count) as radio ,sum(`radio2_b_count`) / sum(total_count) as radio2,sum(`seq_b_count`) / sum(total_count) as seq ," +
                "sum(`protocol_ver_b_count`) / sum(total_count) as protocol_ver from miui_bi_onetrack_usage_count group by app_ver ,region ,date ) sql_model_virtual_table_new_10818_813 " +
                "WHERE 1 = 1 AND ( region in ('cn')AND app_ver in ('3.6.0' ,'3.4.0')AND date >= 20221031and date < 20221107) LIMIT 5000";
        printToken(sql);
        SmartQLEngine.parse(sql);
        assertTrue(Boolean.TRUE);
    }



    /**
     *  多个with
     * update关键字增加 ``
     * @throws Exception
     */
    @Test
    public void test17() throws Exception {
        String sql = "with a as(\n" +
                "SELECT year,month,income_type,\n" +
                "revenue\n" +
                "from appstore_budget_m\n" +
                "where  `update`='V1'\n" +
                ")," +
                "--预算表 \n" +
                "b as(\n" +
                "SELECT year,month,income_type,\n" +
                "revenue\n" +
                "from appstore_budget_m\n" +
                "where `update`='V4'\n" +
                ")," +
                "--预算表 \n" +
                "sdgg as (\n" +
                "    select date,\n" +
                "    mediaType as type,\n" +
                "    sum(feeEffect+ feeBrand+ feertb+feeincomeinternal-feeexpenseinternal) as income,\n" +
                "    SUM(feeEffect+feeRtb+feeBrand+feeIncomeInternal-feeExpenseInternal)*0.8 as revenue\n" +
                "    from new_palo_event_cube_appstore_di\n" +
                "    group by date,type\n" +
                "    ),--商店广告\n" +
                "\n" +
                "yyly as (\n" +
                "\n" +
                "SELECT date,type,income,revenue,gross_profit as profit\n" +
                "from appstore_game_income_d\n" +
                "where position='all'\n" +
                "union all\n" +
                "SELECT date,type,sum(income) as income,sum(revenue) as revenue,sum(gross_profit) as profit\n" +
                "from appstore_game_income_d\n" +
                "where type='sdk'\n" +
                "group by date,type\n" +
                "),--游戏收入\n" +
                "\n" +
                "\n" +
                "\n" +
                "taginfo as (\n" +
                "    select distinct tag_id,ext_id from dim_app_info_all_df where tag_id in(60,62)\n" +
                "),\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "lyzz as (\n" +
                "    select '应用联运-增值' as type,\n" +
                "    date,\n" +
                "    sum(pay.pay_amt) income,\n" +
                "    sum(pay.predict_income) revenue,\n" +
                "    sum(profit) profit\n" +
                "    from (\n" +
                "        select date,ext_id,pay_amt,predict_income,profit\n" +
                "        from ads_lianyunsdk_payment_ltv_d where cluster_name='zjyprc-hadoop' and model='ALL' and cid='ALL' and ext_id<>'ALL' \n" +
                "    ) pay\n" +
                "    join\n" +
                "    taginfo\n" +
                "    on pay.ext_id=taginfo.ext_id\n" +
                "    group by date,type\n" +
                ")--联运增值\n" +
                "\n" +
                ",lygg as (\n" +
                "    select '应用联运-广告' as type,\n" +
                "    date,\n" +
                "    sum(origin_gain)/100 as income,\n" +
                "    sum(origin_gain)/100*0.83 as revenue,\n" +
                "    sum(origin_gain)/100-sum(gain)/100 as profit\n" +
                "    from (\n" +
                "        select date,ext_id,origin_gain,gain from ads_lianyunsdk_mimeng_detail_d  where cluster_name='zjyprc-hadoop' and ext_id<>'ALL'\n" +
                "    ) mm\n" +
                "    join\n" +
                "    taginfo\n" +
                "    on mm.ext_id=taginfo.ext_id\n" +
                "    group by date,type\n" +
                "    \n" +
                "),--联运广告\n" +
                "all_data as (\n" +
                "\n" +
                "select (case \n" +
                "when type='sdk' then '游戏-sdk'\n" +
                "when type='tencent' then '游戏-腾讯'\n" +
                "when type='APP_STORE' then '商店广告-商店'\n" +
                "when type='SAFE_CENTER' or type='PACKAGE_INSTALLER' then '商店广告-安装器'\n" +
                "else type end) as type,\n" +
                "date,\n" +
                "y,\n" +
                "q,\n" +
                "m,\n" +
                "income,\n" +
                "revenue,\n" +
                "profit\n" +
                "    from(\n" +
                "        select \n" +
                "        date,\n" +
                "        type,\n" +
                "        cast(substr(date,1,4) as INTEGER) as y,\n" +
                "        cast((substr(date,5,2)/3.1) as INTEGER)+1 as q,\n" +
                "        cast(substr(date,5,2) as INTEGER) as m,\n" +
                "        sum(income) as income ,\n" +
                "        sum(revenue) as revenue,\n" +
                "        sum(profit) as profit\n" +
                "        from lygg\n" +
                "        group by date,type\n" +
                "        order by date desc \n" +
                "    union ALL\n" +
                "        select \n" +
                "        date,\n" +
                "        type,\n" +
                "        cast(substr(date,1,4) as INTEGER) as y,\n" +
                "        cast((substr(date,5,2)/3.1) as INTEGER)+1 as q,\n" +
                "        cast(substr(date,5,2) as INTEGER) as m,\n" +
                "        sum(income) as income ,\n" +
                "        sum(revenue) as revenue,\n" +
                "        sum(profit) as profit\n" +
                "        from lyzz\n" +
                "        group by date,type\n" +
                "        order by date desc\n" +
                "\n" +
                "    union ALL\n" +
                "        select \n" +
                "        date,\n" +
                "        type,\n" +
                "        cast(substr(date,1,4) as INTEGER) as y,\n" +
                "        cast((substr(date,5,2)/3.1) as INTEGER)+1 as q,\n" +
                "        cast(substr(date,5,2) as INTEGER) as m,\n" +
                "        sum(income) as income ,\n" +
                "        sum(revenue) as revenue,\n" +
                "        0 as profit\n" +
                "        from sdgg\n" +
                "        group by date,type\n" +
                "        order by date desc\n" +
                "    union all\n" +
                "        select \n" +
                "        date,\n" +
                "        type,\n" +
                "        cast(substr(date,1,4) as INTEGER) as y,\n" +
                "        cast((substr(date,5,2)/3.1) as INTEGER)+1 as q,\n" +
                "        cast(substr(date,5,2) as INTEGER) as m,\n" +
                "        sum(income) as income ,\n" +
                "        sum(revenue) as revenue,\n" +
                "        sum(profit) as profit\n" +
                "        from yyly\n" +
                "        group by date,type\n" +
                "        order by date desc \n" +
                "    )aa     \n" +
                ")\n" +
                "select all_data.date,y,m,q,type,a.revenue as budget_revenue,b.revenue as Q4_budget_revenue,income,all_data.revenue,profit\n" +
                "from\n" +
                "all_data left join  a on y=a.year and m=a.month and all_data.type=a.income_type \n" +
                "left join  b on y=b.year and m=b.month and all_data.type=b.income_type \n";
        SmartQLEngine.parse(sql);
        assertTrue(Boolean.TRUE);
    }

    /**
     * testcase模版
     *
     * @throws Exception
     */
    @Test
    public void testTemp() throws Exception {
        String sql = "";
        SmartQLEngine.parse(sql);
        assertTrue(Boolean.TRUE);
    }

}
