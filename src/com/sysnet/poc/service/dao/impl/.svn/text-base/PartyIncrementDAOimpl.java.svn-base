package com.sysnet.poc.service.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.sysnet.poc.control.config.Constants;
import com.sysnet.poc.service.dao.PartyIncrementDAO;
import com.sysnet.poc.util.ResourceBundleUtil;

public class PartyIncrementDAOimpl implements PartyIncrementDAO {

	private PreparedStatement ps = null;

	/**
	 * 回写增量表处理标志
	 */

	public boolean updateStatus(Connection conn, long objectID, String writeBackFlag) throws SQLException {
		boolean flag = false;
		String sql = "update " 
				+ Constants.ETL_PHAROS_INCR_PARTY 
				+ ResourceBundleUtil.readValue("riskNodeThreads") 
				+ " set STAGE_WRITE_FLAG=? where OBJECT_ID=?";
		ps = conn.prepareStatement(sql);
		ps.setString(1, writeBackFlag);// Constants.INCREMENT_WRITE_BACK_STATUS_1
		ps.setLong(2, objectID);
		ps.execute();
		return flag;
	}

}
