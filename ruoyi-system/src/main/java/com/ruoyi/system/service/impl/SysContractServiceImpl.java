package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.ContractExecutionInfo;
import com.ruoyi.common.core.domain.entity.ContractInfo;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.model.ContractListRes;
import com.ruoyi.common.core.domain.model.ExecuteContractParam;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.domain.model.QueryListParam;
import com.ruoyi.common.core.domain.model.SaveContractParam;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.ContractExecution;
import com.ruoyi.common.enums.ContractStatus;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.mapper.SysContractMapper;
import com.ruoyi.system.mapper.SysExecutionMapper;
import com.ruoyi.system.service.ISysContractService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysContractServiceImpl implements ISysContractService {

    private static final Logger log = LoggerFactory.getLogger(SysContractServiceImpl.class);

    @Autowired
    private SysContractMapper contractMapper;

    @Autowired
    private SysExecutionMapper executionMapper;

    @Override
    public String saveContract(SaveContractParam saveContractParam) {
        log.info("start saving contract, param is {}", JSON.toJSONString(saveContractParam));
        ContractInfo contractInfo = new ContractInfo();
        BeanUtils.copyBeanProp(contractInfo, saveContractParam);
        if (StringUtils.isBlank(saveContractParam.getUuid())) {
            contractInfo.setUuid(UUID.randomUUID().toString());
        }
        contractInfo.setContractStatus(ContractStatus.CONTACT_EDITING.getStatus());
        contractInfo.setCreateTime(LocalDateTime.now());
        LoginUser user = SecurityUtils.getLoginUser();
        contractInfo.setOwner(user.getUserId().toString());
        contractMapper.saveContract(contractInfo);
        return contractInfo.getUuid();
    }

    @Override
    public int submitContract(SaveContractParam saveContractParam) {
        log.info("start submitting contract, param is {}", JSON.toJSONString(saveContractParam));
        ContractInfo contractInfo = new ContractInfo();
        BeanUtils.copyBeanProp(contractInfo, saveContractParam);
        if (StringUtils.isBlank(saveContractParam.getUuid())) {
            contractInfo.setUuid(UUID.randomUUID().toString());
        }
        contractInfo.setContractStatus(ContractStatus.CONTRACT_SUPERVISION.getStatus());
        contractInfo.setCreateTime(LocalDateTime.now());
        LoginUser user = SecurityUtils.getLoginUser();
        contractInfo.setOwner(user.getUserId().toString());
        ContractExecutionInfo executionInfo = new ContractExecutionInfo();
        executionInfo.setUuid(contractInfo.getUuid());
        executionInfo.setContractStatus(ContractStatus.CONTACT_EDITING.getStatus());
        executionInfo.setExecutionUser(SecurityUtils.getLoginUser().getUserId().toString());
        executionInfo.setExecutionOperation(ContractExecution.CONTACT_SUBMIT.getStatus());
        executionInfo.setExecuteTime(LocalDateTime.now());
        executionMapper.insertExecution(executionInfo);
        return contractMapper.saveContract(contractInfo);
    }

    @Override
    public int executeContract(ExecuteContractParam executeContractParam) {
        log.info("start executing contract, param is {}", JSON.toJSONString(executeContractParam));
        ContractInfo contractInfo = new ContractInfo();
        if (StringUtils.isBlank(executeContractParam.getUuid())) {
            return -1;
        }
        contractInfo.setUuid(executeContractParam.getUuid());
        contractInfo.setVerifyResult(executeContractParam.getVerifyResult());
        if (ContractExecution.CONTRACT_NOT_PASS.getStatus().equals(executeContractParam.getExecutionOperation())) {
            contractInfo.setContractStatus(ContractStatus.CONTRACT_INVALID.getStatus());
        } else {
            contractInfo.setContractStatus(String.valueOf(Integer.parseInt(executeContractParam.getContractStatus()) + 1));
        }
        contractMapper.updateContract(contractInfo);
        ContractExecutionInfo executionInfo = new ContractExecutionInfo();
        BeanUtils.copyBeanProp(executionInfo, executeContractParam);
        executionInfo.setExecutionUser(SecurityUtils.getLoginUser().getUserId().toString());
        executionInfo.setExecuteTime(LocalDateTime.now());
        return executionMapper.insertExecution(executionInfo);
    }

    @Override
    public TableDataInfo tableDataInfo(QueryListParam queryListParam) {
        LoginUser user = SecurityUtils.getLoginUser();
        List<SysRole> roles = user.getUser().getRoles();
        if (CollectionUtils.isEmpty(roles)) {
            return new TableDataInfo();
        }
        List<Long> roleIdList = roles.stream().map(SysRole::getRoleId).collect(Collectors.toList());
        queryListParam.setUserId(user.getUserId().toString());
        queryListParam.setUserRoles(roleIdList);
        PageHelper.startPage(queryListParam.getPageNum(), queryListParam.getPageSize());
        List<ContractListRes> contractListResList = contractMapper.queryList(queryListParam);
        return getDataTable(contractListResList);
    }

    @Override
    public ContractInfo getContractInfo(String uuid) {
        return contractMapper.getDetail(uuid);
    }

    @Override
    public List<ContractExecutionInfo> getExecutionInfoList(String uuid) {
        return executionMapper.queryExcecutionList(uuid);
    }

    @Override
    public Map<String, String> generateLawSuggestion(String uuid) {
        // todo 生成法律意见文档
        return new HashMap<>();
    }

    private TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }
}
