package com.kewen.framework.auth.core.annotation.data.range;

import com.kewen.framework.auth.core.model.BaseAuth;
import net.sf.jsqlparser.JSQLParserException;
import org.junit.Test;

import java.util.Arrays;

public class MybatisAuthSqlInjectTest {

    @Test
    public void convert2NewSql() throws JSQLParserException {
        //获取新的SQL
        String sql = "select * from meeting_room";
        AuthRange authRange = new AuthRange()
                .setBusinessFunction("meeting_room")
                .setOperate("edit")
                .setTable("")
                .setDataColumn("id")
                .setMatchMethod(MatchMethod.IN)
                .setAuthorities(Arrays.asList(new BaseAuth("ROLE_1", "ROLE_admin"), new BaseAuth("USER_kewen", "USER_科文")));

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
}