package com.kewen.framework.auth.rabc.composite.model;


import com.kewen.framework.auth.core.exception.AuthEntityException;
import com.kewen.framework.auth.core.entity.IFlagAuthEntity;
import com.kewen.framework.auth.core.entity.AuthConstant;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 部门角色结合使用的标记
 *
 * @author kewen
 * @since 2024-09-05
 */
@Data
public class DeptRole implements IFlagAuthEntity<Long> {

    private static final long serialVersionUID = 1243051459093859539L;
    private static final Logger log = LoggerFactory.getLogger(DeptRole.class);
    private Dept dept;
    private Role role;

    public DeptRole() {
    }

    public DeptRole(Dept dept, Role role) {
        this.dept = dept;
        this.role = role;
    }

    @Override
    public String flag() {
        return "DEPT" + AuthConstant.AUTH_SUB_SPLIT + "ROLE";
    }

    public void deSerialize(int pos, String posAuth, String posDescription) {
        // 1-1 第一个为部门，第二个为角色
        if (pos == 0) {
            if (dept == null) {
                dept = new Dept();
            }
            dept.deSerialize(pos, posAuth, posDescription);
        } else if (pos == 1) {
            if (role == null) {
                role = new Role();
            }
            role.deSerialize(pos, posAuth, posDescription);
        } else {
            log.error("权限字符串格式错误{}-{}-{}", pos, posAuth, posDescription);
            throw new AuthEntityException("权限字符串格式错误");
        }
    }

    @Override
    public Pair<String, String> serialize(int pos) {
        if (pos == 0) {
            return dept.serialize(0);
        } else if (pos == 1) {
            return role.serialize(0);
        } else {
            throw new AuthEntityException("权限转换错误");
        }
    }
}
