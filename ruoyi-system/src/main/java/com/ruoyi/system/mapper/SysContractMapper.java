package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.ContractInfo;
import com.ruoyi.common.core.domain.model.contract.ContractListRes;
import com.ruoyi.common.core.domain.model.contract.QueryListParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysContractMapper {

    int saveContract(@Param("contractInfo") ContractInfo contractInfo);

    int updateContract(@Param("contractInfo") ContractInfo contractInfo);

    List<ContractListRes> queryList(@Param("param") QueryListParam param);

    ContractInfo getDetail(@Param("uuid") String uuid);
}
