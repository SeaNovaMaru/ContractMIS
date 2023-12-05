package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.ContractExecutionInfo;
import com.ruoyi.common.core.domain.entity.ContractInfo;
import com.ruoyi.common.core.domain.entity.LoanInfo;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.domain.model.contract.ContractListRes;
import com.ruoyi.common.core.domain.model.contract.ContractPermissionInfo;
import com.ruoyi.common.core.domain.model.contract.ExecuteContractParam;
import com.ruoyi.common.core.domain.model.contract.QueryListParam;
import com.ruoyi.common.core.domain.model.contract.SaveContractParam;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.ContractExecution;
import com.ruoyi.common.enums.ContractStatus;
import com.ruoyi.common.utils.FileHashGenerator;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.SmartContractUtils;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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

    @Autowired
    private SmartContractUtils smartContractUtils;


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
        executionInfo.setExecutionUser(Math.toIntExact(SecurityUtils.getLoginUser().getUserId()));
        executionInfo.setExecutionOperation(ContractExecution.CONTACT_SUBMIT.getStatus());
        executionMapper.insertExecution(executionInfo);
        try {
            LoanInfo loanInfo = formLoanInfo(contractInfo);
            smartContractUtils.operateContract(loanInfo, "create");
        } catch (Exception e) {
            log.info(e.getMessage());
        }
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
        executionInfo.setExecutionUser(Math.toIntExact(SecurityUtils.getLoginUser().getUserId()));
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
    public AjaxResult getContractInfo(String uuid) {
        LoginUser user = SecurityUtils.getLoginUser();
        List<SysRole> roles = user.getUser().getRoles();
        AjaxResult ajaxResult = AjaxResult.success();
        ContractInfo contractInfo = contractMapper.getDetail(uuid);
        ajaxResult.put("contractInfo", contractInfo);
        if (CollectionUtils.isEmpty(roles)) {
            ajaxResult.put("permissionInfo", new ContractPermissionInfo());
            return ajaxResult;
        }
        ajaxResult.put("contractInfo", contractInfo);
        List<Long> roleIdList = roles.stream().map(SysRole::getRoleId).collect(Collectors.toList());
        ContractPermissionInfo permissionInfo = new ContractPermissionInfo();
        if (contractInfo.getOwner().equals(user.getUserId().toString())
                && contractInfo.getContractStatus().equals(ContractStatus.CONTACT_EDITING.getStatus())) {
            permissionInfo.setEdit(true);
        }
        if (roleIdList.contains(3L)
                && contractInfo.getContractStatus().equals(ContractStatus.CONTRACT_SUPERVISION.getStatus())) {
            permissionInfo.setAgree(true);
            permissionInfo.setDisagree(true);
        }
        if (roleIdList.contains(4L)
                && contractInfo.getContractStatus().equals(ContractStatus.LAW_SUPERVISION.getStatus())) {
            permissionInfo.setSubmit(true);
        }
        if (roleIdList.contains(5L)
                && contractInfo.getContractStatus().equals(ContractStatus.CONTACT_COMPARING.getStatus())) {
            permissionInfo.setVerify(true);
        }
        ajaxResult.put("permissionInfo", permissionInfo);
        return ajaxResult;
    }

    @Override
    public List<ContractExecutionInfo> getExecutionInfoList(String uuid) {
        return executionMapper.queryExecutionList(uuid);
    }

    @Override
    public Map<String, String> generateLawSuggestion(String uuid) {
        // todo 生成法律意见文档
        return new HashMap<>();
    }

    @Override
    public String getVerifyResult(String uuid) {
        ContractInfo contractInfo = contractMapper.getDetail(uuid);
        if (contractInfo == null) {
            return "合同不存在";
        }
        try {
            String fileHash = FileHashGenerator.generateFileHash(getFileUrl(contractInfo.getContractFile()));
            LoanInfo loanInfo = smartContractUtils.getLoanInfo(uuid);
            if (StringUtils.equals(fileHash, loanInfo.getDocument())) {
                return "比对结果一致";
            } else {
                return "比对结果不一致";
            }
        } catch (Exception e) {
            return "比对异常";
        }
    }

    private TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    private LoanInfo formLoanInfo(ContractInfo contractInfo) throws IOException, NoSuchAlgorithmException {
        LoanInfo loanInfo = new LoanInfo();
        loanInfo.setContractId(contractInfo.getUuid());
        loanInfo.setContractName(contractInfo.getContractName());
        loanInfo.setCreator(contractInfo.getOwner());
        loanInfo.setDocType("formal");
        loanInfo.setDocument(FileHashGenerator.generateFileHash(getFileUrl(contractInfo.getContractFile())));
        loanInfo.setNeedForensicReview(contractInfo.getNeedLawSupervise() == 0 ? "N" : "Y");
        loanInfo.setStatusId("1");
        loanInfo.setCreateDate(System.currentTimeMillis());
        return loanInfo;
    }

    private String getFileUrl(String fileUrl) {
        HttpServletRequest request = ServletUtils.getRequest();
        StringBuffer requestURL = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        String prefixUrl = requestURL.delete(requestURL.length() -
                        request.getRequestURI().length(), requestURL.length())
                .append(contextPath) + Constants.RESOURCE_PREFIX;
        return fileUrl.replace(prefixUrl, RuoYiConfig.getProfile());
    }
}
