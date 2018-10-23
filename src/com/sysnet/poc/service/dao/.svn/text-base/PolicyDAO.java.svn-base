package com.sysnet.poc.service.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.sysnet.poc.vo.StgPlcCarDeclaration;
import com.sysnet.poc.vo.StgPlcPolicy;

public interface PolicyDAO {
	/**
	 * 保单入库
	 * 
	 * @param conn
	 * @param vo
	 * @return
	 */
	public void insertPolicy(Connection conn, StgPlcPolicy vo) throws SQLException, Exception;

	/**
	 * 保单标的入库
	 * 
	 * @param conn
	 * @param vo
	 * @return
	 */
	public void insertPolicyDeclaration(Connection conn, StgPlcCarDeclaration vo) throws SQLException, Exception;

	/**
	 * 保单责任入库
	 * 
	 * @param conn
	 * @param vo
	 * @return
	 */
	public void insertPolicyCondition(Connection conn, List<Serializable> list) throws SQLException, Exception;

	/**
	 * 保单角色入库
	 * 
	 * @param conn
	 * @param vo
	 * @return
	 */
	public void insertPolicyRoleInfo(Connection conn, List<Serializable> list) throws SQLException, Exception;

	/**
	 * 角色之驾驶员入库
	 * 
	 * @param conn
	 * @param list
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public void insertPolicyDclRoleInfo(Connection conn, List<Serializable> list) throws SQLException, Exception;

}
