package com.kewen.framework.auth.core.annotation.data;

import lombok.Setter;
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
    public <ID> boolean insertBatchAuthData(Collection<AuthDataDO<ID>> datas) {
        String sql = buildInsertBatchSql(datas);
        int update = jdbcTemplate.update(sql);
        return update > 0;
    }

    private <ID> String buildInsertBatchSql(Collection<AuthDataDO<ID>> datas) {
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
    public <ID> int deleteBatchAuthData(List<ID> ids) {
        String sql = removeByIdsSql(ids);
        return jdbcTemplate.update(sql);
    }

    private <ID> String removeByIdsSql(List<ID> ids) {
        Delete delete = new Delete();
        delete.withTable(authDataTable.getTable())
                .withWhere(new InExpression()
                        .withLeftExpression(authDataTable.getIdColumn())
                        .withRightExpression(new ParenthesedExpressionList<>(
                                ids.stream().map(id -> {
                                    if (id instanceof Long) {
                                        Long id1 = (Long) id;
                                        return new LongValue(id1);
                                    } else if (id instanceof Integer) {
                                        Integer id1 = (Integer) id;
                                        return new LongValue(id1);
                                    } else if (id instanceof StringValue) {
                                        return new StringValue((String) id);
                                    } else {
                                        throw new RuntimeException("ID类型异常");
                                    }
                                }).collect(Collectors.toList())
                        ))
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
     * @param <ID>
     * @return
     */
    public <ID> List<AuthDataDO<ID>> listAuthData(String businessFunction, ID dataId, String operate) {
        String sql = listAuthDataSql(businessFunction, dataId, operate);

        List<AuthDataDO<ID>> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            ID id;
            if (dataId instanceof Long) {
                id = ((ID) Long.valueOf(rs.getLong(1)));
            } else if (dataId instanceof Integer) {
                id = (ID) Integer.valueOf(rs.getInt(1));
            } else if (dataId instanceof String) {
                id = (ID) rs.getString(1);
            } else {
                log.error("ID类型不正确 {}", rs);
                id = null;
            }
            String authority = rs.getString(2);
            String description = rs.getString(3);
            return new AuthDataDO<>(id, businessFunction, dataId, operate, authority, description);
        });
        if (list == null) {
            return Collections.emptyList();
        }
        return list;

    }

    private <ID> String listAuthDataSql(String businessFunction, ID dataId, String operate) {
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
