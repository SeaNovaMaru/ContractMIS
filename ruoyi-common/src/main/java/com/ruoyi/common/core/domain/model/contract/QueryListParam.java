package com.ruoyi.common.core.domain.model.contract;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QueryListParam {

    @ApiModelProperty("页号")
    private Integer pageNum = 1;

    @ApiModelProperty("页面大小")
    private Integer pageSize = 15;

    @ApiModelProperty("合同名称")
    private String contractName;

    @ApiModelProperty("合同状态")
    private String contractStatus;

    @ApiModelProperty("查询用户角色")
    private List<Long> userRoles = new ArrayList<>();

    @ApiModelProperty("当前用户id")
    private String userId;
}
