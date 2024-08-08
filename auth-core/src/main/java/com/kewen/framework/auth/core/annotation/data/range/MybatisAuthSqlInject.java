package com.kewen.framework.auth.core.annotation.data.range;

import com.kewen.framework.auth.core.model.BaseAuth;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.ParenthesedSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;
/**
 * SQL注入工具，注入权限查询条件
 * 构造如下的SQL语句
 * SELECT * FROM meeting_room
 * WHERE id IN (
 *      SELECT data_id FROM sys_auth_data
 *          WHERE authority IN ('ROLE_1', 'USER_kewen')
 *              AND business_function = 'meeting_room'
 *              AND operate = 'edit'
 * );
 * 或者
 * SELECT * FROM meeting_room
 * WHERE EXISTS (
 *      SELECT 1 FROM sys_auth_data
 *          WHERE id = data_id
 *          AND authority IN ('ROLE_1', 'USER_kewen')
 *          AND business_function = 'meeting_room'
 *          AND operate = 'edit'
 * )
 * @author kewen
 * @since 2024-08-08
 */
public class MybatisAuthSqlInject {

    /**
     * 转换成新的SQL
     */
    public static String convert2NewSql(String sql, AuthRange authRange) throws JSQLParserException {
        Select parse = (Select) CCJSqlParserUtil.parse(sql);

        Expression condition;

        if (authRange.getMatchMethod()==MatchMethod.IN){
            //获取到 `id in (a,b,c)` 表达式
            condition = parseIdInExpression(authRange);
        } else if (authRange.getMatchMethod()==MatchMethod.EXISTS){
            condition = parseIdExistsExpression(authRange);
        } else {
            throw new RuntimeException("仅支持 in 和 exists");
        }

        PlainSelect plainSelect = parse.getPlainSelect();
        Expression where = plainSelect.getWhere();
        if (where == null) {
            plainSelect.setWhere(condition);
        } else {
            //组合现在的和原来的
            plainSelect.setWhere(new AndExpression(where, condition));
        }

        return plainSelect.toString();
    }

    /**
     * 构造 id in () 条件表达式
     *  table.id in (select data_id from sys_auth_data where authority in ( 'd_1','r_1' ) )
     *  其中 table.id 则为需要匹配的业务主键ID
     */
    private static Expression parseIdInExpression(AuthRange authRange) throws JSQLParserException {

        //1 构造 select data_id from sys_auth_data
        // where
        // business_function=#{meeting_room} and operate=#{edit} and authority in ( 'd_1','r_1' )
        Select authSqlSelect = ((Select) CCJSqlParserUtil.parse("select data_id from sys_auth_data"));
        PlainSelect authSqlPlainSelect = authSqlSelect.getPlainSelect();
        // 得到权限表的条件
        AndExpression inAndBusinessFunctionAndOperateExpression = authTableWhereExpression(authRange);
        //2 设置where条件
        authSqlPlainSelect.setWhere(inAndBusinessFunctionAndOperateExpression);

        //3 构造外层 table.id in (#{authSqlSelect})
        String leftInStr;
        if (StringUtils.isBlank(authRange.getTable())){
            leftInStr = authRange.getDataColumn();
        } else {
            leftInStr = authRange.getTable()+"."+authRange.getDataColumn();
        }
        //需要将authSqlSelect包裹在括号中
        ParenthesedSelect parenthesedSelect = new ParenthesedSelect();
        parenthesedSelect.setSelect(authSqlSelect);
        InExpression inExpression = new InExpression(new Column(leftInStr), parenthesedSelect);

        //返回最后结果
        return inExpression;
    }
    /**
     *
     * 构造 exists 条件表达式
     *  exists ( select 1 from sys_auth_data where authority in ( 'd_1','r_1' ) and table.id=data_id )
     */
    private static ExistsExpression parseIdExistsExpression(AuthRange authRange) throws JSQLParserException {
        ExistsExpression existsExpression = new ExistsExpression();
        PlainSelect plainSelect = ((Select) CCJSqlParserUtil.parse("select 1 from sys_auth_data")).getPlainSelect();
        AndExpression authedWhereExpression = authTableWhereExpression(authRange);
        //Expression equalsTo = CCJSqlParserUtil.parseCondExpression("id=data_id");
        Expression equalsTo = new EqualsTo(new Column("id"), new Column("data_id"));
        AndExpression where = new AndExpression(equalsTo, authedWhereExpression);
        plainSelect.setWhere(where);
        //需要将plainSelect包裹在括号中
        ParenthesedSelect parenthesedSelect = new ParenthesedSelect();
        parenthesedSelect.setSelect(plainSelect);
        existsExpression.setRightExpression(parenthesedSelect);
        return existsExpression;
    }


    /**
     * 构造 权限表的条件
     * business_function=#{meeting_room} and operate=#{edit} and authority in ( 'd_1','r_1' )
     * @param authRange
     * @return
     */
    private static AndExpression authTableWhereExpression(AuthRange authRange) {
        //2 构造 `authority in ('d_1','r_1')` 条件
        //2.1 构造权限字符串列表
        ParenthesedExpressionList<StringValue> authValues = new ParenthesedExpressionList<>(
                authRange.getAuthorities().stream().map(BaseAuth::getAuth).map(StringValue::new).collect(Collectors.toList())
        );
        //2.2 构造表达式
        InExpression authInExpression = new InExpression(new Column("authority"), authValues);

        //3 构造 business_function 条件
        AndExpression inAndBusinessFunctionExpression = new AndExpression(
                authInExpression,
                new EqualsTo(new Column("business_function"), new StringValue(authRange.getBusinessFunction()))
        );
        //4 构造 operate 条件
        AndExpression inAndBusinessFunctionAndOperateExpression = new AndExpression(
                inAndBusinessFunctionExpression,
                new EqualsTo(new Column("operate"), new StringValue(authRange.getOperate()))
        );
        return inAndBusinessFunctionAndOperateExpression;
    }
}
