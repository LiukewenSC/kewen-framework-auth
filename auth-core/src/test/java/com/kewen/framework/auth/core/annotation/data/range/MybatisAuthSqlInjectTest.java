package com.kewen.framework.auth.core.annotation.data.range;

import com.kewen.framework.auth.core.model.BaseAuth;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.schema.Table;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MybatisAuthSqlInjectTest {

    @Test
    public void convert2NewSql() throws JSQLParserException {
        //获取新的SQL
        String sql = "select * from meeting_room t_m";
        AuthRange authRange = getAuthRange();

        System.out.println("in 的方式 ："+ MybatisAuthSqlInject.convert2NewSql(sql, authRange));

        authRange.setMatchMethod(MatchMethod.EXISTS);
        System.out.println("exists 的方式 ："+ MybatisAuthSqlInject.convert2NewSql(sql, authRange));

        System.out.println("-------------------------------");
        sql = "select * from meeting_room where 1=1 ";
        authRange.setMatchMethod(MatchMethod.IN);
        System.out.println("in 的方式 ："+ MybatisAuthSqlInject.convert2NewSql(sql, authRange));
        authRange.setMatchMethod(MatchMethod.EXISTS);
        System.out.println("exists 的方式 ："+ MybatisAuthSqlInject.convert2NewSql(sql, authRange));
    }

    private static AuthRange getAuthRange() {
        AuthRange authRange = new AuthRange()
                .setBusinessFunction("meeting_room")
                .setOperate("edit")
                .setDataColumn("id")
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
        String newSql = MybatisAuthSqlInject.convert2NewSql(sql, authRange);
        System.out.println(newSql);
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
        newSql = MybatisAuthSqlInject.convert2NewSql(sql, authRange);
        System.out.println(newSql);
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
    public void matchNested() throws JSQLParserException {
        AuthRange authRange = getAuthRange();
        authRange.setTable("meeting_room");
        String sql="select * from meeting_room where id in (select * from meet2 t_2 where 1=1)";
        String newSql = MybatisAuthSqlInject.convert2NewSql(sql, authRange);

        System.out.println(newSql);
    }
}