package com.kewen.framework.auth.core;

import lombok.Data;

/**
 * 权限字符串，定义了基础的格式，所有的权限都按照此来转换
 * @author kewen
 * @descrpition
 * @since 2023-12-26
 */

public class BaseAuth {
    String auth;
    String description;

    public BaseAuth() {
    }

    public BaseAuth(String auth, String description) {
        this.auth = auth;
        this.description = description;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "BaseAuth{" +
                "auth='" + auth + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
