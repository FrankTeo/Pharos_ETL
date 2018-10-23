package com.sysnet.poc.service.dao;

import java.sql.Connection;
import java.sql.SQLException;
import com.sysnet.poc.vo.StgProcedureDetail;
import com.sysnet.poc.vo.StgProcedureGroup;

/**
 * 收费项目相关信息操作的DAO接口
 * 
 * @author li_yanpeng
 * 
 */
public interface ProcedureDAO {

	/**
	 * 插入收费项目组信息
	 * 
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean insertStgProcedureGroup(Connection conn, StgProcedureGroup vo) throws SQLException;

	/**
	 * 插入收费项目明细信息
	 * 
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean insertStgProcedureDetail(Connection conn, StgProcedureDetail vo) throws SQLException;

}
