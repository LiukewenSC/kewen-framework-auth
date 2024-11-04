package com.kewen.framework.auth.core.data.edit;

/**
 * @descrpition 关联的信息
 * @author kewen
 * @since 2022-11-23
 */
public interface IdDataEdit<ID> {
    /**
     * 获取到关联的ID
     * @return 业务关联ID，业务具体事项的主键
     */
    ID getDataId();
}
