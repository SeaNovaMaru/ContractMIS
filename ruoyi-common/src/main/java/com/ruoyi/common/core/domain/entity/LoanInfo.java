package com.ruoyi.common.core.domain.entity;

import org.json.JSONObject;

/**
 * 贷款信息
 *
 * @author wangyanan
 */
public class LoanInfo {
    /**
     * 合同编号
     */
    private String contractId;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 文本类型
     */
    private String docType;

    /**
     * 合同本文
     */
    private String document;

    /**
     * 是否需要法审
     */
    private String needForensicReview;

    /**
     * 状态编号
     */
    private String statusId;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间（使用时间戳）
     */
    private Long createDate;

    public LoanInfo() {
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getNeedForensicReview() {
        return needForensicReview;
    }

    public void setNeedForensicReview(String needForensicReview) {
        this.needForensicReview = needForensicReview;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public String toJSONString() {
        return new JSONObject(this).toString();
    }

    public static LoanInfo fromJSONString(String json) {
        JSONObject obj = new JSONObject(json);
        LoanInfo loanInfo = new LoanInfo();
        loanInfo.setContractId(obj.getString("contractId"));
        loanInfo.setContractName(obj.getString("contractName"));
        loanInfo.setDocType(obj.getString("docType"));
        loanInfo.setDocument(obj.getString("document"));
        loanInfo.setNeedForensicReview(obj.getString("needForensicReview"));
        loanInfo.setStatusId(obj.getString("statusId"));
        loanInfo.setCreator(obj.getString("creator"));
        loanInfo.setCreateDate(obj.getLong("createDate"));
        return loanInfo;
    }
}
