package com.kewen.framework.auth.core.data;

import com.kewen.framework.auth.core.exception.AuthServiceException;
import lombok.Setter;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.Values;
import net.sf.jsqlparser.util.SelectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author kewen
 * @since 2024-08-14
 */
@Setter
public class JdbcAuthDataPersistent implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(JdbcAuthDataPersistent.class);
    private JdbcTemplate jdbcTemplate;

    private AuthDataTable authDataTable;

    /**
     * 批量插入
     *
     * @param datas
     * @return
     */
    public boolean insertBatchAuthData(Collection<AuthDataDO> datas) {
        String sql = buildInsertBatchSql(datas);
        int update = jdbcTemplate.update(sql);
        return update > 0;
    }

    private String buildInsertBatchSql(Collection<AuthDataDO> datas) {
        Insert insert = new Insert();
        insert.withTable(authDataTable.getTable())
                .withColumns(new ExpressionList().withExpressions(
                        authDataTable.getBusinessFunctionColumn(),
                        authDataTable.getDataIdColumn(),
                        authDataTable.getOperateColumn(),
                        authDataTable.getAuthorityColumn(),
                        authDataTable.getDescriptionColumn()
                ))
                .withSelect(new Values().withExpressions(new ExpressionList(
                        datas.stream().map(d -> new ParenthesedExpressionList(
                                new StringValue(d.getBusinessFunction()),
                                new StringValue(d.getDataId().toString()),
                                new StringValue(d.getOperate()),
                                new StringValue(d.getAuthority()),
                                new StringValue(d.getDescription())
                        )).collect(Collectors.toList())
                )));
        return insert.toString();
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    public <Object> int deleteBatchAuthData(List ids) {
        String sql = removeByIdsSql(ids);
        return jdbcTemplate.update(sql);
    }

    private  String removeByIdsSql(List ids) {
        ArrayList<Expression> expressions = new ArrayList<>();
        for (Object id : ids) {
            if (id instanceof Long) {
                Long id1 = (Long) id;
                expressions.add(new LongValue(id1));
            } else if (id instanceof Integer) {
                Integer id1 = (Integer) id;
                expressions.add(new LongValue(id1));
            } else if (id instanceof StringValue) {
                expressions.add(new StringValue(id.toString()));
            } else {
                throw new RuntimeException("ID类型异常");
            }
        }
        Delete delete = new Delete();
        delete.withTable(authDataTable.getTable())
                .withWhere(new InExpression()
                        .withLeftExpression(authDataTable.getIdColumn())
                        .withRightExpression(new ParenthesedExpressionList(expressions))
                );
        return delete.toString();
    }

    /**
     * 根据业务功能和dataID删除数据权限，即清空功能下特定ID的所有权限
     * @param businessFunction
     * @param dataId
     * @param <Object>
     * @return
     */
    public <Object> int deleteBusinessFunctionDataIdAuth(String businessFunction, Object dataId) {
        String sql = removeBusinessFunctionDataIdSql(businessFunction,dataId);
        return jdbcTemplate.update(sql);
    }
    private <Object> String removeBusinessFunctionDataIdSql(String businessFunction, Object dataId) {
        Delete delete = new Delete();

        Expression dataIdExpression;
        if (dataId instanceof String){
            dataIdExpression = new StringValue(dataId.toString());
        } else if (dataId instanceof Long){
            dataIdExpression = new LongValue(dataId.toString());
        } else if (dataId instanceof Integer){
            dataIdExpression = new LongValue(dataId.toString());
        } else {
            throw new AuthServiceException("ID类型不正确");
        }

        delete.withTable(authDataTable.getTable())
                .withWhere(
                        new AndExpression(
                                new EqualsTo(authDataTable.getBusinessFunctionColumn(),new StringValue(businessFunction)),
                                new EqualsTo(authDataTable.getDataIdColumn(), dataIdExpression)
                        )
                );
        return delete.toString();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (jdbcTemplate == null || authDataTable == null) {
            throw new RuntimeException("jdbcTemplate or authDataTable is null");
        }
    }

    /**
     * 根据业务方法，数据ID，操作类型查询权限
     *
     * @param businessFunction
     * @param dataId
     * @param operate
     * @return
     */
    public List<AuthDataDO> listAuthData(String businessFunction, Object dataId, String operate) {
        String sql = listAuthDataSql(businessFunction, dataId, operate);

        List<AuthDataDO> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Object id;
            if (dataId instanceof Long) {
                id = Long.valueOf(rs.getLong(1));
            } else if (dataId instanceof Integer) {
                id = Integer.valueOf(rs.getInt(1));
            } else if (dataId instanceof String) id = (Object) rs.getString(1);
            else {
                log.error("ID类型不正确 {}", rs);
                id = null;
            }
            String authority = rs.getString(2);
            String description = rs.getString(3);
            return new AuthDataDO(id, businessFunction, dataId, operate, authority, description);
        });
        if (list == null) {
            return Collections.emptyList();
        }
        return list;

    }

    private <Object> String listAuthDataSql(String businessFunction, Object dataId, String operate) {
        PlainSelect plainSelect = SelectUtils.buildSelectFromTable(authDataTable.getTable()).getPlainSelect();
        plainSelect.withWhere(
                new AndExpression(
                        new AndExpression(
                                new EqualsTo(authDataTable.getBusinessFunctionColumn(), new StringValue(businessFunction)),
                                new EqualsTo(authDataTable.getDataIdColumn(), new StringValue(dataId.toString()))
                        ),
                        new EqualsTo(authDataTable.getOperateColumn(), new StringValue(operate))
                )
        ).withSelectItems(Arrays.asList(
                new SelectItem<>(authDataTable.getIdColumn()),
                new SelectItem<>(authDataTable.getAuthorityColumn()),
                new SelectItem<>(authDataTable.getDescriptionColumn())
        ))
        ;
        return plainSelect.toString();
    }
}
