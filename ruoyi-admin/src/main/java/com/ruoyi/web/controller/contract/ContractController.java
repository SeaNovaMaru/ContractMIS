package com.ruoyi.web.controller.contract;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.SaveContractParam;
import com.ruoyi.system.service.ISysContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用请求处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/contract")
public class ContractController extends BaseController {

    @Autowired
    private ISysContractService contractService;

    /**
     * 保存合同
     */
    @PostMapping("/save")
    public AjaxResult saveContract(@RequestBody SaveContractParam saveContractParam) {
        return toAjax(contractService.saveContract(saveContractParam));
    }
}
