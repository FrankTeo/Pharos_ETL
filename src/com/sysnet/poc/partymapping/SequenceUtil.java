package com.sysnet.poc.partymapping;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.NamingException;
import com.sysnet.poc.service.dao.dbc.ConnDB;

/**
 * 取ORACLE序列
 * 
 * @author li_yanpeng
 * 
 */
public class SequenceUtil {

	private static Statement state;

	/**
	 * 先处理异常，以后要抛出去 //sthrows SQLException, NamingException
	 * 
	 * @param seqName
	 * @return
	 * @throws NamingException
	 * @throws SQLException
	 * @throws SQLException
	 * @throws NamingException
	 */
	public static int getSequenceNextValueByName(String seqName) throws SQLException, NamingException {
		int nextValue = 0;
		String sql = "select " + seqName + ".nextval from dual";
		Connection conn = ConnDB.getODSConn();
		state = conn.createStatement();
		ResultSet rs = state.executeQuery(sql);
		while (rs.next()) {
			nextValue = rs.getInt("nextval");
		}
		if (conn != null)
			conn.close();

		return nextValue;
	}

}
