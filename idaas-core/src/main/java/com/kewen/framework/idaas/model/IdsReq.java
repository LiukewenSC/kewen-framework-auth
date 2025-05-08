package com.kewen.framework.idaas.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author kewen
 * @descrpition
 */
@Data
public class IdsReq {

    @NotEmpty
    protected List<Long> ids;
}
