package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.ContractExecutionInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysExecutionMapper {

    int insertExecution(@Param("executionInfo") ContractExecutionInfo executionInfo);

    List<ContractExecutionInfo> queryExcecutionList(@Param("uuid") String uuid);
}
