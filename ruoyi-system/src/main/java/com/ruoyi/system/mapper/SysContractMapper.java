package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.ContractInfo;
import org.apache.ibatis.annotations.Param;

public interface SysContractMapper {

    int saveContract(@Param("contractInfo") ContractInfo contractInfo);
}
