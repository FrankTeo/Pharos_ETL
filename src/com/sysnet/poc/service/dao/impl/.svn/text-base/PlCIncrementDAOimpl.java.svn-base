package com.sysnet.poc.service.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.sysnet.poc.control.config.Constants;
import com.sysnet.poc.service.dao.PlCIncrementDAO;
import com.sysnet.poc.util.ResourceBundleUtil;

/**
 * 增量表DAO处理类
 * 
 * @author Administrator
 * 
 */
public class PlCIncrementDAOimpl implements PlCIncrementDAO {
	private PreparedStatement ps = null;

	/**
	 * 回写增量表处理标志
	 */
	public boolean updateStatus(Connection conn, long objectID, String writeBackFlag) throws SQLException {
		boolean flag = false;
		String sql = "update " + Constants.ETL_PHAROS_INCR_INSURE + ResourceBundleUtil.readValue("riskNodeThreads") + " set STAGE_WRITE_FLAG=? where OBJECT_ID=?";
		ps = conn.prepareStatement(sql);
		ps.setString(1, writeBackFlag);// Constants.INCREMENT_WRITE_BACK_STATUS_1
		ps.setLong(2, objectID);
		ps.execute();
		return flag;
	}

	// /**
	// * 回写客户增量表处理标志
	// */
	// public boolean updateCustStatus(Connection conn, int objectID) throws SQLException {
	// boolean flag = false;
	// String sql = "update ETL_PHAROS_INCR_CUST set STAGE_WRITE_FLAG=? where OBJECT_ID=?";
	// ps = conn.prepareStatement(sql);
	// ps.setString(1, Constants.INCREMENT_WRITE_BACK_STATUS_1);
	// ps.setLong(2, objectID);
	// ps.execute();
	// return flag;
	// }
	//
	// /**
	// * 回写缴费计划增量表处理标志
	// *
	// * @throws SQLException
	// */
	// public boolean updatePayPlanStatus(Connection conn, int objectID) throws SQLException {
	// boolean flag = false;
	// String sql = "update ETL_PHAROS_INCR_PAYPLAN set STAGE_WRITE_FLAG=? where OBJECT_ID=?";
	// ps = conn.prepareStatement(sql);
	// ps.setString(1, Constants.INCREMENT_WRITE_BACK_STATUS_1);
	// ps.setLong(2, objectID);
	// ps.execute();
	// return flag;
	// }

}
