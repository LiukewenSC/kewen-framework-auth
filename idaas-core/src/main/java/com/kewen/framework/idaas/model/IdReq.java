package com.kewen.framework.idaas.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author kewen
 * @descrpition
 */
@Data
public class IdReq {
    @NotNull
    protected Long id;
}
