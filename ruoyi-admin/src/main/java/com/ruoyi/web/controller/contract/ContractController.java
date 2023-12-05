package com.ruoyi.web.controller.contract;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.contract.ExecuteContractParam;
import com.ruoyi.common.core.domain.model.contract.QueryListParam;
import com.ruoyi.common.core.domain.model.contract.SaveContractParam;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.service.ISysContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        return AjaxResult.success(contractService.saveContract(saveContractParam));
    }

    /**
     * 提交合同
     */
    @PostMapping("/submit")
    public AjaxResult submitContract(@RequestBody SaveContractParam saveContractParam) {
        return toAjax(contractService.submitContract(saveContractParam));
    }

    /**
     * 执行合同流程操作
     */
    @PostMapping("/execute")
    public AjaxResult executeContract(@RequestBody ExecuteContractParam executeContractParam) {
        return toAjax(contractService.executeContract(executeContractParam));
    }

    /**
     * @description:
     * @author: Rui Zhang
     * @date: 2023/11/17 14:15
     * @param:
     * @return:
     **/
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody QueryListParam queryListParam) {
        return contractService.tableDataInfo(queryListParam);
    }

    @GetMapping("/detail")
    public AjaxResult detail(@RequestParam("uuid") String uuid) {
        return contractService.getContractInfo(uuid);
    }

    @GetMapping("/execution/list")
    public AjaxResult executionList(@RequestParam("uuid") String uuid) {
        return AjaxResult.success(contractService.getExecutionInfoList(uuid));
    }

    @GetMapping("/verify/info")
    public AjaxResult getVerifyInfo(@RequestParam("uuid") String uuid) {
        return AjaxResult.success(contractService.getVerifyResult(uuid));
    }

    @GetMapping("/genration/suggestion")
    public AjaxResult generateLawSuggestion(@RequestParam("uuid") String uuid) {
        // TODO 接入并生成法律意见
        return AjaxResult.success(contractService.generateLawSuggestion(uuid));
    }
}
