package com.ruoyi.common.core.domain.model.contract;

import lombok.Data;

@Data
public class ContractPermissionInfo {

    /**
     * 修改按钮
     */
    private boolean edit = false;

    /**
     * 提交按钮
     */
    private boolean submit = false;

    /**
     * 同意按钮
     */
    private boolean agree = false;

    /**
     * 驳回按钮
     */
    private boolean disagree = false;
}
