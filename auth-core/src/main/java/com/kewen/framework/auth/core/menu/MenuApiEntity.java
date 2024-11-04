package com.kewen.framework.auth.core.menu;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * @author kewen
 * @since 2024-08-09
 */
@Data
@Accessors(chain = true)
public class MenuApiEntity<T> {
    T id;
    String path;
    String name;
    T parentId;
    String parentPath;

    public static MenuApiEntity of(String path, String name) {
        return of(path, name, null);
    }

    public static MenuApiEntity of(String path, String name, String parentPath) {
        MenuApiEntity apiEntity = new MenuApiEntity();
        apiEntity.setPath(path);
        apiEntity.setName(name);
        apiEntity.setParentPath(parentPath);
        return apiEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuApiEntity apiEntity = (MenuApiEntity) o;
        return Objects.equals(path, apiEntity.path);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(path);
    }
}
