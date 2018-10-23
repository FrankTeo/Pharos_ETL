package com.sysnet.poc.service.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 增量处理
 * 
 * @author Administrator
 * 
 */
public interface PlCIncrementDAO {
	public boolean updateStatus(Connection conn, long objectID, String writeBackFlag) throws SQLException;
	//
	// public boolean updateCustStatus(Connection conn, int objectID) throws SQLException;
	//
	// public boolean updatePayPlanStatus(Connection conn, int objectID) throws SQLException;
}
