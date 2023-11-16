package com.ruoyi.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum ContractStatus {
    CONTACT_EDITING("1", "合同草拟"),
    CONTRACT_SUPERVISION("2", "内部审核"),
    LAW_SUPERVISION("3", "法律审查"),
    CONTACT_COMPARING("4", "合同比对"),
    CONTRACT_ARCHIVE("5", "归档");


    private final String status;
    private final String name;

    ContractStatus(String status, String name) {
        this.status = status;
        this.name = name;
    }

    public static ContractStatus getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (ContractStatus status : values()) {
            if (status.getStatus().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
