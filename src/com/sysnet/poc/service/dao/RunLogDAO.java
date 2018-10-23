package com.sysnet.poc.service.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sysnet.poc.mapping.SequenceUtil;
import com.sysnet.poc.service.dao.dbc.ConnDB;
import com.sysnet.poc.vo.StgRunLog;

public class RunLogDAO {

	private Logger log = Logger.getLogger(InsureScheduleDAO.class);

	/**
	 * 保存日志
	 * 
	 * @param stgRunLog
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	public boolean insertLog(StgRunLog stgRunLog) throws SQLException, NamingException {

		long logId = getId();
		String objectId = stgRunLog.getObjectId() == null ? "" : stgRunLog.getObjectId();
		String objectNo = stgRunLog.getObjectNo() == null ? "" : stgRunLog.getObjectNo(); // 对象CD
		String errorCode = stgRunLog.getErrorCode() == null ? "" : stgRunLog.getErrorCode(); // 错误代码
		String errorMessage = stgRunLog.getErrorMessage() == null ? "" : stgRunLog.getErrorMessage(); // 错误信息

		String sql = "insert into STG_RUN_LOG  values(?,?,?,?,?,sysdate ,'N')";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = ConnDB.getODSConn();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, logId);
			ps.setString(2, objectId);
			ps.setString(3, objectNo);
			ps.setString(4, errorCode);
			ps.setString(5, errorMessage);
			ps.execute();
			return true;
		} catch (SQLException e) {
			log.error("insertLog", e);
		} catch (NamingException e) {
			log.error("insertLog", e);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				log.error("insertLog", e);
			}
		}

		return false;
	}

	/**
	 * 生成主键 ID
	 * 
	 * @return 使用序列 CREATE SEQUENCE "YA_STAGE"."SEQ_MYLOG" MINVALUE 1 MAXVALUE 10000000 INCREMENT BY 1 START WITH 2 NOCACHE NOORDER NOCYCLE ;
	 */
	private long getId() {
		long id = 0L;
		try {
			id = SequenceUtil.getSequenceNextValueByName("SEQ_MYLOG");
		} catch (SQLException e) {
			log.error("RunLog getId()", e);
		} catch (NamingException e) {
			log.error("RunLog getId()", e);
		}

		return id;
	}

	public boolean update(String id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnDB.getODSConn();
			stmt = conn.prepareStatement("Update stg_run_log Set flag='Y' Where log_id=?");
			stmt.setLong(1, Long.parseLong(id));
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public List<StgRunLog> selectAll() {
		List<StgRunLog> errList = new ArrayList<StgRunLog>();
		Connection con = null;
		Statement s = null;
		ResultSet r = null;
		try {
			con = ConnDB.getODSConn();
			// Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			// con =
			// DriverManager.getConnection("jdbc:oracle:thin:@172.19.151.240:1521:orcl",
			// "ods", "ods");
			s = con.createStatement();
			String sql = "Select ERROR_CODE,ERROR_MESSAGE,OBJECT_ID,OBJECT_NO,LOG_ID,TIME_STAMP,FLAG From stg_run_log where FLAG<>'Y' order by LOG_ID";
			s.execute(sql);
			r = s.getResultSet();
			int i = 0;
			while (r.next() && i < 65535) {
				StgRunLog e = new StgRunLog();
				e.setErrorCode(r.getString("ERROR_CODE"));
				e.setErrorMessage(r.getString("ERROR_MESSAGE"));
				e.setObjectId(r.getLong("OBJECT_ID") + "");
				e.setObjectNo(r.getString("OBJECT_NO"));
				e.setLogId(r.getLong("LOG_ID") + "");
				e.setTimeStamp(r.getTimestamp("TIME_STAMP") + "");
				e.setFlag(r.getString("FLAG"));
				errList.add(e);
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (r != null) {
					r.close();
				}
				if (s != null) {
					s.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return errList;
	}
}
