package com.kewen.framework.auth.extension;

import com.kewen.framework.auth.core.BaseAuth;
import com.kewen.framework.auth.util.ClassUtil;
import lombok.Data;

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
public abstract class AbstractIdNameAuthEntity<ID> implements IAuthEntityProvider<ID> {

    /**
     * ID
     */
    protected ID id;

    /**
     * 名字
     */
    protected String name;


    @Override
    public BaseAuth getAuth() {
        String flag = flag();
        String split = split();
        return new BaseAuth(
                flag + split + getId(),
                flag + split + getName()
        );
    }

    /**
     * 填充属性值
     * @param baseAuth
     */
    public void setProperties(BaseAuth baseAuth){
        String split = split();
        String[] authStrs = baseAuth.getAuth().split(split);
        String[] descStrs = baseAuth.getDescription().split(split);
        if (!authStrs[0].equals(flag())){
            throw new IllegalArgumentException();
        }
        Class actualT = ClassUtil.parseSuperActualT(this.getClass(), Object.class);
        //这里也还是硬伤啊
        if (actualT== Integer.class){
            setId((ID)Integer.valueOf(authStrs[1]));
        } else if (actualT == Long.class){
            setId((ID)Long.valueOf(authStrs[1]));
        } else if (actualT == String.class){
            setId((ID)authStrs[1]);
        }
        setName(descStrs[1]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractIdNameAuthEntity)) return false;
        AbstractIdNameAuthEntity<?> that = (AbstractIdNameAuthEntity<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
