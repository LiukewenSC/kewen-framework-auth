package com.kewen.framework.auth.core.annotation.data.range;

/**
 * 权限匹配方式
 * 匹配应当遵循小表驱动大表的原则
 * @author kewen
 * @descrpition
 * @since 2022-11-24
 */
public enum MatchMethod {
    /**
     * 通过IN的方式匹配
     * 权限表中的数据远远少于业务表时时
     */
    IN,

    /**
     * 通过EXISTS的方式匹配
     * 权限中的数据远远大于业务表时
     */
    EXISTS
}
