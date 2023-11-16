package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.domain.entity.ContractInfo;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.domain.model.SaveContractParam;
import com.ruoyi.common.enums.ContractStatus;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.mapper.SysContractMapper;
import com.ruoyi.system.service.ISysContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SysContractServiceImpl implements ISysContractService {

    private static final Logger log = LoggerFactory.getLogger(SysContractServiceImpl.class);

    @Autowired
    private SysContractMapper contractMapper;

    @Override
    public int saveContract(SaveContractParam saveContractParam) {
        log.info("start saving contract, param is {}", JSON.toJSONString(saveContractParam));
        ContractInfo contractInfo = new ContractInfo();
        BeanUtils.copyBeanProp(contractInfo, saveContractParam);
        contractInfo.setUuid(UUID.randomUUID().toString());
        contractInfo.setContractStatus(ContractStatus.CONTACT_EDITING.getStatus());
        contractInfo.setCreateTime(LocalDateTime.now());
        LoginUser user = SecurityUtils.getLoginUser();
        contractInfo.setOwner(user.getUserId().toString());
        return contractMapper.saveContract(contractInfo);
    }
}
