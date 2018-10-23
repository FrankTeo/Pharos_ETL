package com.sysnet.poc.service.dao.daoFactory;

import com.sysnet.poc.service.dao.AccDAO;
import com.sysnet.poc.service.dao.AccDataDeleteDAO;
import com.sysnet.poc.service.dao.AccSchedulDAO;
import com.sysnet.poc.service.dao.ClaimDAO;
import com.sysnet.poc.service.dao.ClaimSchduleDAO;
import com.sysnet.poc.service.dao.PartyIncrementDAO;
import com.sysnet.poc.service.dao.PlCIncrementDAO;
import com.sysnet.poc.service.dao.impl.AccDAOimpl;
import com.sysnet.poc.service.dao.impl.AccDataDeleteDAOimpl;
import com.sysnet.poc.service.dao.impl.ClaimDAOimpl;
import com.sysnet.poc.service.dao.impl.ClaimSchduleDAOimpl;
import com.sysnet.poc.service.dao.impl.PartyIncrementDAOimpl;
import com.sysnet.poc.service.dao.impl.PlCIncrementDAOimpl;

public class DAOFactory {

	/**
	 * 返回保单模块的DAO对象
	 * 
	 * @return
	 */
//	public static PolicyDAO getPolicyDAO() {
//		PolicyDAO policyDAO = new PolicyDAOimpl();
//		return policyDAO;
//	}

	/**
	 * 返回保单增量DAO对象
	 * 
	 * @return
	 */
	public static PlCIncrementDAO getPlcIncrecentDAO() {
		PlCIncrementDAO plcIncrementDAO = new PlCIncrementDAOimpl();
		return plcIncrementDAO;
	}

	/**
	 * 返回投保单DAO对象
	 * 
	 * @return proposalDAO
	 */
//	public static ProposalDAO getProposalDAO() {
//		ProposalDAO proposalDAO = new ProposalDAOimpl();
//		return proposalDAO;
//	}

	/**
	 * 返回理赔调度DAO对象
	 * 
	 * @return
	 */
	public static ClaimSchduleDAO getClaimSchduleDAO() {
		ClaimSchduleDAO claimSchduleDAO = new ClaimSchduleDAOimpl();
		return claimSchduleDAO;
	}

	/**
	 * 返回客户DAO对象
	 * 
	 * @return customDAO
	 */
	// public static CustomDAO getCustomDAO() {
	// CustomDAO customDAO = new CustomDAOimpl();
	// return customDAO;
	// }

	/**
	 * 返回保单缴费计划DAO对象
	 * 
	 * @return policyPayPlanDAO
	 */
//	public static PolicyPayPlanDAO getPolicyPayPlanDAO() {
//		PolicyPayPlanDAO policyPayPlanDAO = new PolicyPayPlanDAOimpl();
//		return policyPayPlanDAO;
//	}

	/**
	 * 返回批单缴费计划DAO对象
	 * 
	 * @return EndorPayPlanDAO
	 */
//	public static EndorPayPlanDAO getEndorPayPlanDAO() {
//		EndorPayPlanDAO endorPayPlanDAO = new EndorPayPlanDAOimpl();
//		return endorPayPlanDAO;
//	}

	/**
	 * 返回投保单缴费计划DAO对象
	 * 
	 * @return ProposalPayPlanDAO
	 */
//	public static ProposalPayPlanDAO getProposalPayPlanDAO() {
//		ProposalPayPlanDAO proposalPayPlanDAO = new ProposalPayPlanDAOimpl();
//		return proposalPayPlanDAO;
//	}

	/**
	 * 返回理赔DAO对象
	 * 
	 * @return
	 */
	public static ClaimDAO getClaimDAO() {
		ClaimDAO claimDAO = new ClaimDAOimpl();
		return claimDAO;
	}

	/**
	 * 返回收付费DAO对象
	 * 
	 * @return AccSchedulDAO
	 */
	public static AccSchedulDAO getSchedulDAO() {
		AccSchedulDAO accSchedulDAO = new AccSchedulDAO();
		return accSchedulDAO;
	}

	/**
	 * 交费主表DAO对象
	 * 
	 * @return AccDAO
	 */
	public static AccDAO getAccDAO() {
		AccDAO accDAO = new AccDAOimpl();
		return accDAO;
	}

	/**
	 * 
	 * @return
	 */
	public static AccDataDeleteDAO getAccDataDelDAO() {
		AccDataDeleteDAO dao = new AccDataDeleteDAOimpl();
		return dao;
	}

	public static AccSchedulDAO getAccSchedulDAO() {
		AccSchedulDAO dao = new AccSchedulDAO();
		return dao;
	}

//	public static AccInvoiceDAO getAccInvoiceDAO() {
//		AccInvoiceDAO dao = new AccInvoiceDAOimpl();
//		return dao;
//	}
//
//	public static AccBankDAO getAccBankDAO() {
//		AccBankDAO dao = new AccBankDAOimpl();
//		return dao;
//	}

	/**
	 * 返回当事方Party增量DAO对象
	 * 
	 * @return
	 */

	public static PartyIncrementDAO getPartyIncrecentDAO() {
		PartyIncrementDAO partyIncrementDAO = new PartyIncrementDAOimpl();
		return partyIncrementDAO;
	}
}
