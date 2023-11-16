package com.ruoyi.common.core.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContractInfo {

    /**
     * 自增id
     */
    private Long id;

    /**
     * 合同uuid
     */
    private String uuid;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 合同类别
     */
    private Integer contractType;

    /**
     * 范本文件路径
     */
    private String contractTemplate;

    /**
     * 范本文件名
     */
    private String contractTemplateName;

    /**
     * 合同文件路径
     */
    private String contractFile;

    /**
     * 合同文件名
     */
    private String contractFileName;

    /**
     * 是否需要法审
     */
    private Integer needLawSupervise;

    /**
     * 不需要法审的原因
     */
    private Integer notSuperviseReason;

    /**
     * 合同审批状态
     */
    private String contractStatus;

    /**
     * 合同发起人
     */
    private String owner;

    /**
     * 比对意见
     */
    private String verifyResult;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
