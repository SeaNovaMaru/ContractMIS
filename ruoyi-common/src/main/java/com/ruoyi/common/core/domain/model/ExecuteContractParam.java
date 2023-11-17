package com.ruoyi.common.core.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExecuteContractParam {

    @ApiModelProperty("合同uuid")
    private String uuid;

    @ApiModelProperty("当前合同状态")
    private String contractStatus;

    @ApiModelProperty("执行操作")
    private Integer executionOperation;

    @ApiModelProperty("处理意见路径")
    private String executionFile;

    @ApiModelProperty("处理意见文件名")
    private String executionFileName;

    @ApiModelProperty("比对结果")
    private String verifyResult;
}
