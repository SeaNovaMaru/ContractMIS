package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.ContractExecutionInfo;
import com.ruoyi.common.core.domain.entity.ContractInfo;
import com.ruoyi.common.core.domain.model.ExecuteContractParam;
import com.ruoyi.common.core.domain.model.QueryListParam;
import com.ruoyi.common.core.domain.model.SaveContractParam;
import com.ruoyi.common.core.page.TableDataInfo;

import java.util.List;
import java.util.Map;

public interface ISysContractService {

    String saveContract(SaveContractParam saveContractParam);

    int submitContract(SaveContractParam saveContractParam);

    int executeContract(ExecuteContractParam executeContractParam);

    TableDataInfo tableDataInfo(QueryListParam queryListParam);

    ContractInfo getContractInfo(String uuid);

    List<ContractExecutionInfo> getExecutionInfoList(String uuid);

    Map<String, String> generateLawSuggestion(String uuid);
}
