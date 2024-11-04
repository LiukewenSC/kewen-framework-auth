package com.kewen.framework.auth.core.data.range;

import com.kewen.framework.auth.core.data.AuthDataTable;
import com.kewen.framework.auth.core.entity.BaseAuth;
import net.sf.jsqlparser.JSQLParserException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class MybatisAuthSqlInjectTest {

    private static final Logger log = LoggerFactory.getLogger(MybatisAuthSqlInjectTest.class);

    private SqlAuthInject sqlAuthInject;

    @Before
    public void before() {
        AuthDataTable table = new AuthDataTable(
                "sys_auth_data",
                "id",
                "business_function",
                "data_id",
                "operate",
                "authority",
                "description"
        );
        sqlAuthInject = new SqlAuthInject(table);
    }

    @Test
    public void convert2NewSql() throws JSQLParserException {
        //获取新的SQL
        String sql = "select * from meeting_room t_m";
        AuthRange authRange = getAuthRange();

        System.out.println("in 的方式 ："+ sqlAuthInject.convert2NewSql(sql, authRange));

        authRange.setMatchMethod(MatchMethod.EXISTS);
        System.out.println("exists 的方式 ："+ sqlAuthInject.convert2NewSql(sql, authRange));

        System.out.println("-------------------------------");
        sql = "select * from meeting_room where 1=1 ";
        authRange.setMatchMethod(MatchMethod.IN);
        System.out.println("in 的方式 ："+ sqlAuthInject.convert2NewSql(sql, authRange));
        authRange.setMatchMethod(MatchMethod.EXISTS);
        System.out.println("exists 的方式 ："+ sqlAuthInject.convert2NewSql(sql, authRange));
    }

    private static AuthRange getAuthRange() {
        AuthRange authRange = new AuthRange()
                .setBusinessFunction("meeting_room")
                .setOperate("edit")
                .setDataIdColumn("id")
                .setMatchMethod(MatchMethod.IN)
                .setAuthorities(Arrays.asList(
                        new BaseAuth("ROLE_1", "ROLE_admin"),
                        new BaseAuth("USER_kewen", "USER_科文"))
                );
        return authRange;
    }

    @Test
    public void matchTableName() throws JSQLParserException {
        AuthRange authRange = getAuthRange();
        authRange.setTable("sys_role");
        String sql = "select * from meeting_room t1 " +
                "left join sys_dept t2 on t1.id=t2.id " +
                "right join sys_role t3 on t2.id=t3.id and t1.id=t3.id";
        String newSql = sqlAuthInject.convert2NewSql(sql, authRange);
        log.info(newSql);
        Assert.assertEquals(
                "SELECT * FROM meeting_room t1 " +
                "LEFT JOIN sys_dept t2 ON t1.id = t2.id " +
                "RIGHT JOIN sys_role t3 ON t2.id = t3.id AND t1.id = t3.id " +
                "WHERE t3.id IN (SELECT data_id FROM sys_auth_data " +
                        "WHERE authority IN ('ROLE_1', 'USER_kewen') " +
                        "AND business_function = 'meeting_room' AND operate = 'edit')",
                newSql
        );
        authRange.setMatchMethod(MatchMethod.EXISTS);
        newSql = sqlAuthInject.convert2NewSql(sql, authRange);
        log.info(newSql);
        Assert.assertEquals(
                "SELECT * FROM meeting_room t1 " +
                        "LEFT JOIN sys_dept t2 ON t1.id = t2.id " +
                        "RIGHT JOIN sys_role t3 ON t2.id = t3.id AND t1.id = t3.id " +
                        "WHERE EXISTS (" +
                                "SELECT 1 FROM sys_auth_data WHERE t3.id = data_id " +
                                "AND authority IN ('ROLE_1', 'USER_kewen') " +
                                "AND business_function = 'meeting_room' AND operate = 'edit')",
                newSql
        );
    }

    /**
     * 测试在子查询中匹配表并添加条件参数
     */
    @Test
    public void nestedTest() throws JSQLParserException {
        AuthRange authRange = getAuthRange();
        authRange.setTable("sys_user");
        String sql="select * from meeting_room where 1=1 and id in (select id from sys_user t_2 where 1=1) and 2=2";
        String newSql = sqlAuthInject.convert2NewSql(sql, authRange);
        log.info(newSql);
        Assert.assertEquals(
                "SELECT * FROM meeting_room WHERE 1 = 1 " +
                        "AND id IN (SELECT id FROM sys_user t_2 WHERE 1 = 1 " +
                            "AND t_2.id IN (" +
                                "SELECT data_id FROM sys_auth_data WHERE " +
                                    "authority IN ('ROLE_1', 'USER_kewen') AND business_function = 'meeting_room' AND operate = 'edit'" +
                            ")" +
                        ") AND 2 = 2",
                newSql);
    }
    /**
     * 测试 with as 子句
     */
    @Test
    public void withAsTest() throws JSQLParserException {
        AuthRange authRange = getAuthRange();
        authRange.setTable("sys_user");
        String sql="with t_a as (SELECT * from sys_user) SELECT * from t_a where 1=1";
        String newSql = sqlAuthInject.convert2NewSql(sql, authRange);
        log.info(newSql);
        Assert.assertEquals(
                "WITH t_a AS (" +
                            "SELECT * FROM sys_user WHERE sys_user.id IN (" +
                                "SELECT data_id FROM sys_auth_data " +
                                    "WHERE authority IN ('ROLE_1', 'USER_kewen') " +
                                    "AND business_function = 'meeting_room' " +
                                    "AND operate = 'edit'" +
                            ")" +
                        ") SELECT * FROM t_a WHERE 1 = 1",
                newSql);
        authRange.setMatchMethod(MatchMethod.EXISTS);

        newSql = sqlAuthInject.convert2NewSql(sql, authRange);
        log.info(newSql);
        Assert.assertEquals(
                "WITH t_a AS (" +
                            "SELECT * FROM sys_user WHERE EXISTS (" +
                                "SELECT 1 FROM sys_auth_data " +
                                    "WHERE sys_user.id = data_id " +
                                    "AND authority IN ('ROLE_1', 'USER_kewen') " +
                                    "AND business_function = 'meeting_room' " +
                                    "AND operate = 'edit'" +
                            ")" +
                        ") SELECT * FROM t_a WHERE 1 = 1",
                newSql);
        sql="with t_a as (SELECT * from sys_user t_u) SELECT * from t_a where 1=1";
        authRange.setMatchMethod(MatchMethod.IN);
        newSql = sqlAuthInject.convert2NewSql(sql, authRange);
        log.info(newSql);
        Assert.assertEquals(
                "WITH t_a AS (" +
                            "SELECT * FROM sys_user t_u WHERE t_u.id IN (" +
                                "SELECT data_id FROM sys_auth_data " +
                                    "WHERE authority IN ('ROLE_1', 'USER_kewen') " +
                                    "AND business_function = 'meeting_room' " +
                                    "AND operate = 'edit'" +
                            ")" +
                        ") SELECT * FROM t_a WHERE 1 = 1",
                newSql);
        authRange.setMatchMethod(MatchMethod.EXISTS);
        newSql = sqlAuthInject.convert2NewSql(sql, authRange);
        log.info(newSql);
        Assert.assertEquals(
                "WITH t_a AS (" +
                            "SELECT * FROM sys_user t_u WHERE EXISTS (" +
                                "SELECT 1 FROM sys_auth_data " +
                                    "WHERE t_u.id = data_id " +
                                    "AND authority IN ('ROLE_1', 'USER_kewen') " +
                                    "AND business_function = 'meeting_room' " +
                                    "AND operate = 'edit'" +
                            ")" +
                        ") SELECT * FROM t_a WHERE 1 = 1",
                newSql);
    }
}