package com.ruoyi.common.core.domain.entity;

import lombok.Data;

@Data
public class ContractExecutionInfo {

    /**
     * 自增id
     */
    private Long id;

    /**
     * uuid
     */
    private String uuid;

    /**
     * 合同状态
     */
    private String contractStatus;

    /**
     * 操作用户
     */
    private Integer executionUser;

    private String executionUserName;

    /**
     * 处理操作
     */
    private Integer executionOperation;

    /**
     * 处理意见路径
     */
    private String executionFile;

    /**
     * 处理意见文件名
     */
    private String executionFileName;

    /**
     * 执行时间
     */
    private String executeTime;

}
