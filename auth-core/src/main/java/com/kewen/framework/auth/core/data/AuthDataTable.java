package com.kewen.framework.auth.core.data;

import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;

/**
 * 数据范围数据库字段定义
 * @author kewen
 * @since 2023-12-04
 */
@Setter
@Accessors(chain = true)
public class AuthDataTable {
    /**
     * 业务关联表名
     */
    private String tableName;
    /**
     * id字段名
     * 删除使用，插入、查询不使用此字段
     */
    private String idColumn;
    /**
     * 业务功能字段名
     */
    private String businessFunctionColumn;
    /**
     * 业务ID存储字段名
     */
    private String dataIdColumn;
    /**
     * 操作字段名
     */
    private String operateColumn;


    /**
     * 权限存储字段名
     */
    private String authorityColumn;
    /**
     * 权限描述字段名
     */
    private String descriptionColumn;

    public AuthDataTable() {
    }

    public AuthDataTable(String tableName, String idColumn, String businessFunctionColumn, String dataIdColumn, String operateColumn, String authorityColumn, String descriptionColumn) {
        this.tableName = tableName;
        this.idColumn = idColumn;
        this.businessFunctionColumn = businessFunctionColumn;
        this.dataIdColumn = dataIdColumn;
        this.operateColumn = operateColumn;
        this.authorityColumn = authorityColumn;
        this.descriptionColumn = descriptionColumn;
    }

    public Table getTable(){
        return new Table(tableName);
    }
    public Column getIdColumn(){
        return new Column(idColumn);
    }
    public Column getBusinessFunctionColumn(){
        return new Column(businessFunctionColumn);
    }
    public Column getDataIdColumn(){
        return new Column(dataIdColumn);
    }
    public Column getOperateColumn(){
        return new Column(operateColumn);
    }
    public Column getAuthorityColumn(){
        return new Column(authorityColumn);
    }
    public Column getDescriptionColumn(){
        return new Column(descriptionColumn);
    }


}
