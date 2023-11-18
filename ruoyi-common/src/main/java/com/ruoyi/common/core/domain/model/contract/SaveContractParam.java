package com.ruoyi.common.core.domain.model.contract;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SaveContractParam {

    @ApiModelProperty("合同uuid")
    private String uuid;

    @ApiModelProperty("合同名称")
    private String contractName;

    @ApiModelProperty("合同类别")
    private Integer contractType;

    @ApiModelProperty("合同文件路径")
    private String contractFile;

    @ApiModelProperty("合同文件名")
    private String contractFileName;

    @ApiModelProperty("范本文件路径")
    private String contractTemplate;

    @ApiModelProperty("范本文件名")
    private String contractTemplateName;

    @ApiModelProperty("是否需要法审")
    private Integer needLawSupervise;

    @ApiModelProperty("不需要法审的原因")
    private Integer notSuperviseReason;
}
