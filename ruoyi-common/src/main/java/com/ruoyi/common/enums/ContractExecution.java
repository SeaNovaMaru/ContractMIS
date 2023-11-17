package com.ruoyi.common.enums;

import lombok.Getter;

@Getter
public enum ContractExecution {

    CONTACT_SUBMIT(0, "提交"),
    CONTRACT_PASS(1, "通过"),
    CONTRACT_NOT_PASS(2, "不通过");

    private final Integer status;
    private final String name;

    ContractExecution(Integer status, String name) {
        this.status = status;
        this.name = name;
    }

    public static ContractExecution getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ContractExecution status : values()) {
            if (status.getStatus().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
