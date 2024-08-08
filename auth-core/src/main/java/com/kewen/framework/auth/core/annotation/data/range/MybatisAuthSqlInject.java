package com.kewen.framework.auth.core.annotation.data.range;

import com.kewen.framework.auth.core.model.BaseAuth;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.util.Optional;
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

    private static final Logger log = LoggerFactory.getLogger(MybatisAuthSqlInject.class);

    /**
     * 转换成新的SQL
     */
    public static String convert2NewSql(String sql, AuthRange authRange) throws JSQLParserException {
        Select parse = (Select) CCJSqlParserUtil.parse(sql);
        PlainSelect plainSelect = parse.getPlainSelect();
        Table mainTable = parseMainTable(plainSelect,authRange.getTable());

        Expression condition;
        if (authRange.getMatchMethod()==MatchMethod.IN){
            //获取到 `id in (a,b,c)` 表达式
            condition = parseIdInExpression(authRange,mainTable);
        } else if (authRange.getMatchMethod()==MatchMethod.EXISTS){
            condition = parseIdExistsExpression(authRange,mainTable);
        } else {
            throw new RuntimeException("仅支持 in 和 exists");
        }

        Expression where = plainSelect.getWhere();
        if (where == null) {
            plainSelect.setWhere(condition);
        } else {
            //组合现在的和原来的
            plainSelect.setWhere(new AndExpression(where, condition));
        }

        return plainSelect.toString();
    }
    private static Table parseMainTable(PlainSelect plainSelect,@Nullable String tableName) throws JSQLParserException {
        //没有设置则以from后面的为主
        FromItem fromItem = plainSelect.getFromItem();
        if (StringUtils.isBlank(tableName)){
            if (fromItem instanceof Table){
                return ((Table) fromItem);
            }
        }
        //设置的主表和From后的一致
        if (fromItem instanceof Table) {
            if (tableName.equals(((Table) fromItem).getName())){
                return (Table) fromItem;
            }
        }

        for (Join join : plainSelect.getJoins()) {
            FromItem joinFromItem = join.getFromItem();
            if (joinFromItem instanceof Table) {
                Table joinTable = ((Table) joinFromItem);
                if (tableName.equals(joinTable.getName())){
                    return joinTable;
                }
            }
        }
        log.warn("SQL中没有找到表名对应的表");
        throw new JSQLParserException("sql 解析异常，SQL("+plainSelect+")中没有找到表名"+tableName+"对应的表");
    }

    /**
     * 构造 id in () 条件表达式
     *  table.id in (select data_id from sys_auth_data where authority in ( 'd_1','r_1' ) )
     *  其中 table.id 则为需要匹配的业务主键ID
     */
    private static Expression parseIdInExpression(AuthRange authRange,Table mainTable) throws JSQLParserException {

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
        String tableName = parseAliseOrTableName(mainTable);
        //需要将authSqlSelect包裹在括号中
        ParenthesedSelect parenthesedSelect = new ParenthesedSelect();
        parenthesedSelect.setSelect(authSqlSelect);

        InExpression inExpression = new InExpression(new Column(tableName+"."+authRange.getDataColumn()), parenthesedSelect);

        //返回最后结果
        return inExpression;
    }
    /**
     *
     * 构造 exists 条件表达式
     *  exists ( select 1 from sys_auth_data where authority in ( 'd_1','r_1' ) and table.id=data_id )
     */
    private static ExistsExpression parseIdExistsExpression(AuthRange authRange,Table mainTable) throws JSQLParserException {
        ExistsExpression existsExpression = new ExistsExpression();
        PlainSelect plainSelect = ((Select) CCJSqlParserUtil.parse("select 1 from sys_auth_data")).getPlainSelect();
        AndExpression authedWhereExpression = authTableWhereExpression(authRange);
        //Expression equalsTo = CCJSqlParserUtil.parseCondExpression("id=data_id");
        String tableName = parseAliseOrTableName(mainTable);
        Expression equalsTo = new EqualsTo(getMainTableColumn(tableName,authRange.getDataColumn()), new Column("data_id"));
        AndExpression where = new AndExpression(equalsTo, authedWhereExpression);
        plainSelect.setWhere(where);
        //需要将plainSelect包裹在括号中
        ParenthesedSelect parenthesedSelect = new ParenthesedSelect();
        parenthesedSelect.setSelect(plainSelect);
        existsExpression.setRightExpression(parenthesedSelect);
        return existsExpression;
    }
    private static String parseAliseOrTableName(Table table){
        return Optional.ofNullable(table).map(Table::getAlias).map(Alias::getName).orElse(table.getName());
    }
    private static Column getMainTableColumn(String tableName, String column){
        return new Column(tableName + "." + column);
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
