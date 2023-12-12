package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.ContractExecutionInfo;
import com.ruoyi.common.core.domain.entity.LoanInfo;
import com.ruoyi.common.core.domain.model.contract.ExecuteContractParam;
import com.ruoyi.common.core.domain.model.contract.QueryListParam;
import com.ruoyi.common.core.domain.model.contract.SaveContractParam;
import com.ruoyi.common.core.page.TableDataInfo;

import java.util.List;

public interface ISysContractService {

    String saveContract(SaveContractParam saveContractParam);

    LoanInfo submitContract(SaveContractParam saveContractParam);

    int executeContract(ExecuteContractParam executeContractParam);

    TableDataInfo tableDataInfo(QueryListParam queryListParam);

    AjaxResult getContractInfo(String uuid);

    List<ContractExecutionInfo> getExecutionInfoList(String uuid);

    String generateLawSuggestion(String uuid);

    String getVerifyResult(String uuid);

    void updateGenerationResult(String url, Integer status, String contractUuid);
}
