package com.kewen.framework.auth.core.extension;

import com.alibaba.fastjson.JSONObject;
import com.kewen.framework.auth.core.exception.AuthEntityException;
import com.kewen.framework.auth.core.model.BaseAuth;
import com.kewen.framework.auth.core.exception.AuthServiceException;
import com.kewen.framework.auth.core.util.ClassUtil;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Objects;

/**
 * 抽象权限对象，提供基础的数据，用于转换成BaseAuth权限对象
 * 自定义的权限类型最好继承此类，不能继承的需要约定好权限字符串格式
 * 其实现类需要保留无参构造，否则项目启动会抛异常
 *
 * 此处只支持 ID+Name的形式，若有其他规则如 某部门某岗位 DEPTPOSSION_${deptId}_${positionId}，请自行扩展 IAuthEntityProvider
 * @author kewen
 * @since 2023-12-26
 */
@Data
public abstract class AbstractIdNameFlagAuthEntity<ID> implements IFlagAuthEntity<ID> {

    private static final long serialVersionUID = 5347275476740629152L;
    /**
     * ID
     */
    protected ID id;

    /**
     * 名字
     */
    protected String name;

    @Override
    public Pair<String, String> serialize(int pos) {
        return Pair.of(id.toString(), name);
    }

    @Override
    public void deSerialize(int pos, String idStr, String descStr) {
        //只有一个
        Class actualT = ClassUtil.parseSuperActualT(this.getClass(), Object.class);
        //这里也还是硬伤啊
        if (actualT== Integer.class){
            setId((ID)Integer.valueOf(idStr));
        } else if (actualT == Long.class){
            setId((ID)Long.valueOf(idStr));
        } else if (actualT == String.class){
            setId((ID) idStr);
        } else {
            throw new AuthEntityException("ID的 格式 仅支持基本数据类型 int long String");
        }
        setName(descStr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractIdNameFlagAuthEntity)) return false;
        AbstractIdNameFlagAuthEntity<?> that = (AbstractIdNameFlagAuthEntity<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
