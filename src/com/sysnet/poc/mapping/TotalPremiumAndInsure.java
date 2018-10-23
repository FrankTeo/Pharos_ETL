package com.sysnet.poc.mapping;

import java.util.List;

import pharos.ctm.bo.BContractBO;
import pharos.ctm.exception.CTMException;
import pharos.ctm.vo.BContractNode;
import pharos.framework.base.Money;
import pharos.framework.base.MultiMoney;

/**
 * 汇总各种费用
 * 
 * @author Administrator
 * 
 */
public class TotalPremiumAndInsure {

	private MultiMoney totalPremium = new MultiMoney();

	private MultiMoney totalInsured = new MultiMoney();

	private MultiMoney totalPreminumValueChange = new MultiMoney();

	private MultiMoney totalInsuredValueChange = new MultiMoney();

	public MultiMoney getTotalPremium() {
		return totalPremium;
	}

	public void setTotalPremium(MultiMoney totalPremium) {
		this.totalPremium = totalPremium;
	}

	public MultiMoney getTotalInsured() {
		return totalInsured;
	}

	public void setTotalInsured(MultiMoney totalInsured) {
		this.totalInsured = totalInsured;
	}

	/**
	 * 得到所有风险结点
	 * 
	 * @param startNode
	 * @param riskNodeList
	 */
	public void getRiskNode(BContractNode startNode, List<BContractNode> riskNodeList) {
		if (startNode.getRiskLevelFlag() == 0) {
			riskNodeList.add(startNode);
		}
		for (BContractNode child : startNode.getChildContractNodes()) {
			this.getRiskNode(child, riskNodeList);
		}
	}

	/**
	 * 保单汇总保费
	 * 
	 * @param riskNodeList
	 * @return
	 */
	public MultiMoney getTotalPremiumValue(List<BContractNode> riskNodeList) {
		
		for (BContractNode riskNode : riskNodeList) {
			MultiMoney mm = new MultiMoney();
			mm.addMoney(riskNode.getPremiumCurrency(), riskNode.getPremiumValue());
			totalPremium.addMultiMoney(mm);
		}

		return totalPremium;
	}

	/**
	 * 批单汇总保费，返回风险节点批改后保费
	 * 
	 * @param riskNodeList
	 * @return
	 */

	public double getTotalPremiumValueOfEndorse(List<BContractNode> riskNodeList) {
		double premium = 0D;
		double earnedPremium = 0D;
		double unearnedPremium = 0D;

		MultiMoney mm = new MultiMoney();
		Money money = new Money();
		for (BContractNode riskNode : riskNodeList) {
			// 已赚
			//earnedPremium = riskNode.getTotal().getEarnedPremium().total().getAmount();
			// 未赚
			//unearnedPremium = riskNode.getTotal().getUnearnedPremium().total().getAmount();
			//premium = earnedPremium + unearnedPremium + riskNode.getTotal().getPremium().total().getAmount();
			premium = riskNode.getPremiumValue();

			money.setAmount(premium);
			mm.addMoney(money);
			totalPremium.addMultiMoney(mm);
		}

		return premium;
	}

	/**
	 * 批单保费变化量汇总
	 * 
	 * @param riskNodeList
	 * @return
	 */
	public MultiMoney getPreminumValueChangeAmountOfEndorse(List<BContractNode> riskNodeList) {
		double preminumValueChangeAmount = 0D;
		MultiMoney mm = new MultiMoney();
		Money money = new Money();
		for (BContractNode riskNode : riskNodeList) {
			//preminumValueChangeAmount = riskNode.getTotal().getPremium().total().getAmount();
			preminumValueChangeAmount = riskNode.getPremiumValue() - riskNode.getPrePremiumValue();

			money.setAmount(preminumValueChangeAmount);
			mm.addMoney(money);
			totalPreminumValueChange.addMultiMoney(mm);
		}

		return totalPreminumValueChange;
	}

	/**
	 * 批单保额变化量，返回风险节点批改后保额
	 * 
	 * @param riskNodeList
	 * @return
	 * @throws CTMException
	 */
	public double getInsuredValueChangeAmount(BContractNode riskNode, BContractBO bContractBo) throws CTMException {
		double newInsuredValue = 0D;
		double insuredValueChangeAmount = 0D;
		MultiMoney mm = new MultiMoney();
		Money money = new Money();
		int currVersion = riskNode.getVersion();

		if (currVersion == 1) {
			/*
			 * modify by zf 20140310
			 */
			//insuredValueChangeAmount = riskNode.getTotal().getInsuredValue().total().getAmount();	
			insuredValueChangeAmount = riskNode.getInsuredValue() - riskNode.getPreInsuredValue();
			money.setAmount(insuredValueChangeAmount);
			mm.addMoney(money);
			totalInsuredValueChange.addMultiMoney(mm);
			newInsuredValue = insuredValueChangeAmount;
		} else if (currVersion >= 2) {
			double lastInsuredValue = 0D;
			/*
			 * modify by zf 20140310
			 */
			//double currInsuredValue = riskNode.getTotal().getInsuredValue().total().getAmount();
			double currInsuredValue  = riskNode.getInsuredValue() - riskNode.getPreInsuredValue();
			// 取上一版本的节点 ID
			long preNodeId = riskNode.getPreNodeId();

			BContractNode lastRiskNode = bContractBo.getContractNode(preNodeId);

			/*
			 * modify by zf 20140310
			 */
			//lastInsuredValue = lastRiskNode.getTotal().getInsuredValue().total().getAmount();
			lastInsuredValue = lastRiskNode.getInsuredValue() - riskNode.getPreInsuredValue();
			newInsuredValue = currInsuredValue - lastInsuredValue;
			money.setAmount(newInsuredValue);
			mm.addMoney(money);
			totalInsuredValueChange.addMultiMoney(mm);
		}

		return newInsuredValue;
	}

	/**
	 * 汇总保额
	 * 
	 * @param riskNodeList
	 * @return
	 */
	public MultiMoney getTotalInsuredValue(List<BContractNode> riskNodeList) {

		for (BContractNode riskNode : riskNodeList) {
			MultiMoney mm = new MultiMoney();
			mm.addMoney(riskNode.getInsuredValueCurrency(), riskNode.getInsuredValue());
			totalInsured.addMultiMoney(mm);
		}

		return totalInsured;
	}

	/**
	 * 取总保费或总保额
	 * 
	 * @param riskNodeList
	 * @return
	 */
	public double getTotalP_I(MultiMoney totalPremium) {
		Money m = totalPremium.total();
		return m.getAmount();
	}

	public MultiMoney getTotalPreminumValueChange() {

		return totalPreminumValueChange;
	}

	public MultiMoney getTotalInsuredValueChange() {

		return totalInsuredValueChange;
	}

}
