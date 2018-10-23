package com.sysnet.poc.control.manage.vo;

import java.sql.Timestamp;

public class EtlPharosIncrClaim implements Comparable<EtlPharosIncrClaim>  {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((claimId == null) ? 0 : claimId.hashCode());
		result = prime * result + ((transactionType == null) ? 0 : transactionType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EtlPharosIncrClaim other = (EtlPharosIncrClaim) obj;
		if (claimId == null) {
			if (other.claimId != null)
				return false;
		} else if (!claimId.equals(other.claimId))
			return false;
		if (transactionType == null) {
			if (other.transactionType != null)
				return false;
		} else if (!transactionType.equals(other.transactionType))
			return false;
		return true;
	}

	Long objectId = 0L;
	String objectNo = "";
	Long objectType = 0L;
	Long batId = 0L;
	String contractNo = "";

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public String getObjectNo() {
		return objectNo;
	}

	public void setObjectNo(String objectNo) {
		this.objectNo = objectNo;
	}

	public Long getObjectType() {
		return objectType;
	}

	public void setObjectType(Long objectType) {
		this.objectType = objectType;
	}

	public Long getBatId() {
		return batId;
	}

	public void setBatId(Long batId) {
		this.batId = batId;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public String getAssistCode() {
		return assistCode;
	}

	public void setAssistCode(String assistCode) {
		this.assistCode = assistCode;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getDealFlag() {
		return dealFlag;
	}

	public void setDealFlag(String dealFlag) {
		this.dealFlag = dealFlag;
	}

	public String getStageWriteFlag() {
		return stageWriteFlag;
	}

	public void setStageWriteFlag(String stageWriteFlag) {
		this.stageWriteFlag = stageWriteFlag;
	}

	public String getOdsWriteFlag() {
		return odsWriteFlag;
	}

	public void setOdsWriteFlag(String odsWriteFlag) {
		this.odsWriteFlag = odsWriteFlag;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public Long getActivityType() {
		return activityType;
	}

	public void setActivityType(Long activityType) {
		this.activityType = activityType;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Long getProposalId() {
		return proposalId;
	}

	public void setProposalId(Long proposalId) {
		this.proposalId = proposalId;
	}

	public Long getProposalNodeId() {
		return proposalNodeId;
	}

	public void setProposalNodeId(Long proposalNodeId) {
		this.proposalNodeId = proposalNodeId;
	}

	public String getProposalNo() {
		return proposalNo;
	}

	public void setProposalNo(String proposalNo) {
		this.proposalNo = proposalNo;
	}

	public String getPolicNo() {
		return policNo;
	}

	public void setPolicNo(String policNo) {
		this.policNo = policNo;
	}

	public String getNodeNo() {
		return NodeNo;
	}

	public void setNodeNo(String nodeNo) {
		NodeNo = nodeNo;
	}

	public Long getClaimId() {
		return claimId;
	}

	public void setClaimId(Long claimId) {
		this.claimId = claimId;
	}

	public Long getPartClaimId() {
		return partClaimId;
	}

	public void setPartClaimId(Long partClaimId) {
		this.partClaimId = partClaimId;
	}

	Long nodeId = 0L;
	String assistCode = "";
	Timestamp timeStamp = null;
	String dealFlag = "";
	String stageWriteFlag = null;
	String odsWriteFlag = null;
	Long activityId = 0L;
	String activityName = "";
	Long processId = 0L;
	Long activityType = 0L;
	String transactionType = "";
	Long proposalId = 0L;
	Long proposalNodeId = 0L;
	String proposalNo = "";
	String policNo = "";
	String NodeNo = "";
	Long claimId = 0L;
	Long partClaimId = 0L;

	public EtlPharosIncrClaim() {

	}

	@Override
	public int compareTo(EtlPharosIncrClaim o) {
		return o.getObjectId().compareTo(getObjectId());
	}

}
