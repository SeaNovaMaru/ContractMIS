package com.ruoyi.common.core.domain.model.contract;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ContractListRes {

    @ApiModelProperty("合同uuid")
    private String uuid;

    @ApiModelProperty("合同名称")
    private String contractName;

    @ApiModelProperty("合同类别")
    private Integer contractType;

    @ApiModelProperty("合同状态")
    private Integer contractStatus;

    @ApiModelProperty("是否需要法审")
    private Integer needLawSupervise;

    @ApiModelProperty("不需要法审的原因")
    private Integer notSuperviseReason;

    @ApiModelProperty("合同发起人")
    private String owner;

    @ApiModelProperty("合同发起人id")
    private Integer userId;

    @ApiModelProperty("合同发起时间")
    private String createTime;

    @ApiModelProperty("合同更新时间")
    private String updateTime;
}
