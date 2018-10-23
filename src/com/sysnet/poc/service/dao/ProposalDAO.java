package com.sysnet.poc.service.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.sysnet.poc.vo.StgPrpCarDeclaration;
import com.sysnet.poc.vo.StgPrpCondition;
import com.sysnet.poc.vo.StgPrpDclRoleInfo;
import com.sysnet.poc.vo.StgPrpProposal;
import com.sysnet.poc.vo.StgPrpRoleInfo;

public interface ProposalDAO {
	/**
	 * 投保单入库
	 * 
	 * @param conn
	 * @param vo
	 * @return
	 */
	public boolean insertProposal(Connection conn, StgPrpProposal vo) throws SQLException, Exception;

	/**
	 * 投保单标的入库
	 * 
	 * @param conn
	 * @param vo
	 * @return
	 */
	public boolean insertProposalDeclaration(Connection conn, StgPrpCarDeclaration vo) throws SQLException, Exception;

	/**
	 * 投保单责任入库
	 * 
	 * @param conn
	 * @param vo
	 * @return
	 */
	public boolean insertProposalCondition(Connection conn, List<StgPrpCondition> list) throws SQLException, Exception;

	/**
	 * 投保单角色入库
	 * 
	 * @param conn
	 * @param vo
	 * @return
	 */
	public boolean insertProposalRoleInfo(Connection conn, List<StgPrpRoleInfo> list) throws SQLException, Exception;

	/**
	 * 投保单驾驶员入库
	 * 
	 * @param conn
	 * @param vo
	 * @return
	 */
	public boolean insertProposalDclRoleInfo(Connection conn, List<StgPrpDclRoleInfo> list) throws SQLException, Exception;

}
