package com.sysnet.poc.service.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.NamingException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;

import com.sysnet.poc.service.dao.dbc.ConnDB;
import com.sysnet.poc.transaction.UserTransactionFactory;
import com.sysnet.poc.util.DateHelper;
import com.sysnet.poc.util.ResourceBundleUtil;
import com.sysnet.poc.control.config.Constants;
import com.sysnet.poc.control.manage.vo.ConfigureBean;
import com.sysnet.poc.control.manage.vo.StatusBean;

/**
 * 承保调度DAO
 * 
 * @author Li_yanpeng
 * 
 */
public class InsureScheduleDAO {

	private Logger log = Logger.getLogger(InsureScheduleDAO.class);

	/**
	 * 查询要处理的增量总数，查询原理是 bat_id >= 0 的所有增量
	 * 
	 * @param insureTableName
	 * @return
	 * @throws NamingException
	 * @throws SQLException
	 */
	public long getCountOfIncremental(String insureTableName) throws NamingException, SQLException {

		long count = 0;
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnDB.getPharosConn();
			stmt = conn.createStatement();
			String sql = "select count(1) from " + insureTableName + " where bat_id >= 0";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				count = rs.getLong(1);
			}
			return count;

		} catch (NamingException ex) {

			throw ex;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				log.error("InsureScheduleDAO", e);
			}
		}
	}

	/**
	 * 查询增量表中对应回写标志的增量数量
	 * 
	 * @param insureTableName
	 * @param stageWriteFlag
	 * @return
	 * @throws NamingException
	 * @throws SQLException
	 */
	public long getCountOfIncremental(String insureTableName, String stageWriteFlag) throws NamingException, SQLException {

		long count = 0;
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnDB.getPharosConn();
			stmt = conn.createStatement();
			String sql = "select count(1) from " + insureTableName + " where STAGE_WRITE_FLAG=" + stageWriteFlag;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				count = rs.getLong(1);
			}
			return count;

		} catch (NamingException ex) {

			throw ex;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				log.error("InsureScheduleDAO", e);
			}
		}
	}

	/**
	 * 查询数据库,根据数据库的配置表ETL_STAGE_ETL_CONFIG决定取到处理多少个处理线程等信息
	 * 
	 * @return
	 * @throws NamingException
	 * @throws SQLException
	 */
	public ConfigureBean getConfigure() throws NamingException, SQLException {
		Connection conn = null;
		Statement stmt = null;
		ConfigureBean configureBean = null;
		try {
			int currentHour = DateHelper.getHour();
			conn = ConnDB.getODSConn();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from " + Constants.ETL_PHAROS_INCR_INSUREC + ResourceBundleUtil.readValue("riskNodeThreads") + " where START_TIME<=" + currentHour + " and END_TIME>" + currentHour);
			while (rs.next()) {
				configureBean = new ConfigureBean();
				configureBean.setSTART_TIME(rs.getString("start_time"));
				configureBean.setEND_TIME(rs.getString("end_time"));
				configureBean.setDEAL_COUNT(rs.getString("deal_count"));
				configureBean.setDEAL_MODE(rs.getString("deal_mode"));
				configureBean.setEXCEPT_MAX_THREAD_COUNT(rs.getInt("except_max_thread_count"));
				configureBean.setGENERAL_MAX_THREAD_COUNT(rs.getInt("general_max_thread_count"));
				configureBean.setEXCEPT_SLEEP_MILLIS_COUNT(rs.getInt("except_sleep_millis_count"));
				configureBean.setGENERAL_SLEEP_MILLIS_COUNT(rs.getInt("general_sleep_millis_count"));
				
			}
			return configureBean;
		} catch (NamingException ex) {

			throw ex;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				log.error("查询数据库,根据数据库的配置表ETL_STAGE_ETL_CONFIG决定取到处理多少个处理线程等信息出错!", e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * 查询数据库,根据数据库的配置表ETL_STAGE_ETL_STATUS取得当前批次相关信息
	 * 
	 * @return
	 * @throws NamingException
	 * @throws SQLException
	 */
	public StatusBean getBatStatus() throws NamingException, SQLException {
		StatusBean statusBean = new StatusBean();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnDB.getODSConn();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from " 
					+ Constants.ETL_PHAROS_INCR_INSURES 
					+ ResourceBundleUtil.readValue("riskNodeThreads") 
					+ " order by BAT_ID desc ");
			if (rs.next()) {

				statusBean.setBAT_ID(rs.getInt("bat_id"));
				statusBean.setMAX_OBJECT_ID(rs.getInt("max_object_id"));
				statusBean.setMIN_OBJECT_ID(rs.getInt("min_object_id"));
				statusBean.setRECORD_COUNT(rs.getInt("record_count"));
				statusBean.setODS_DATA_DEAL_STATUS(rs.getString("ods_data_deal_status"));
				statusBean.setSTAGE_DATE_READY_STATUS(rs.getString("stage_date_ready_status"));
			} else {
				statusBean = null;
			}
			return statusBean;
		} catch (NamingException ex) {

			throw ex;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			try {

				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				log.error("查询数据库,根据数据库的配置表ETL_STAGE_ETL_STATUS取得当前批次相关信息!", e);
				e.printStackTrace();
			}
		}

	}

	/**
	 * 查询pharos增量表，取得当前批次未处理数据数量
	 * 
	 * @param batId
	 * @return
	 * @throws NamingException
	 * @throws SQLException
	 */
	public int getPharosIncr(String batId) throws NamingException, SQLException {
		int count = 0;
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnDB.getPharosConn();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select count(OBJECT_ID) from " 
					+ Constants.ETL_PHAROS_INCR_INSURE 
					+ ResourceBundleUtil.readValue("riskNodeThreads") 
					+ " where bat_id='" + batId + "' and STAGE_WRITE_FLAG='0'");
			if (rs.next()) {

				count = rs.getInt(1);
			}

			return count;
		} catch (NamingException ex) {

			throw ex;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			try {

				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				log.error("查询pharos增量表，取得当前批次未处理数据数量!" + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * 回收该批次未处理的数据。<br>
	 * 将改批次为处理的数据，批次号至为0.
	 * 
	 * @param batId
	 * @throws NamingException
	 */
	public void recoveryNonDeal(int batId) throws NamingException, SQLException {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnDB.getPharosConn();
			stmt = conn.createStatement();
			stmt.execute("update " 
					+ Constants.ETL_PHAROS_INCR_INSURE 
					+ ResourceBundleUtil.readValue("riskNodeThreads") 
					+ " set bat_id='0' where stage_write_flag='0' and BAT_ID=" + batId);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	/**
	 * 修ETL_STAGE_ETL_STATUS表改标志位
	 * 
	 * @param batId
	 * @throws NamingException
	 * @throws SQLException
	 */
	public void updateBatStatus(String batId) throws NamingException, SQLException {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnDB.getODSConn();
			stmt = conn.createStatement();
			stmt.execute("update " 
					+ Constants.ETL_PHAROS_INCR_INSURES 
					+ ResourceBundleUtil.readValue("riskNodeThreads") 
					+ " set STAGE_DATE_READY_STATUS=1 where BAT_ID=" + batId);
		} catch (NamingException ex) {
			throw ex;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				log.error("修ETL_STAGE_ETL_STATUS表改标志位!" + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * 修ETL_STAGE_ETL_STATUS表改标志位
	 * 
	 * @param configureBean
	 * @param statusBean
	 * @return
	 * @throws Exception
	 */
	public boolean insertOneBat(ConfigureBean configureBean, StatusBean statusBean) throws Exception {
		Connection odsConn = null;
		Connection pharosConn = null;
		Statement pharosStmt = null;
		Statement odsStmt = null;
		ResultSet rs = null;
		try {
			// 判断是否需要产生新的处理批次
			pharosConn = ConnDB.getPharosConn();
			pharosStmt = pharosConn.createStatement();
			rs = pharosStmt.executeQuery("select object_id from " 
					+ Constants.ETL_PHAROS_INCR_INSURE 
					+ ResourceBundleUtil.readValue("riskNodeThreads") 
					+ " where bat_id='0' and STAGE_WRITE_FLAG='0' and rownum<=1 "
					//+ "order by object_id"
					);
			if (!rs.next()) {
				return false;
			}

			// 产生新的处理批次
			odsConn = ConnDB.getODSConn();
			odsStmt = odsConn.createStatement();
			int maxID = 0;
			int recordC = 0;
			if (statusBean != null) {
				maxID = statusBean.getBAT_ID() + 1;
				// recordC = statusBean.getRECORD_COUNT();
				// 20091102 modify
				recordC = Integer.valueOf(configureBean.getDEAL_COUNT().trim());
			} else {
				maxID = 1;
				recordC = Integer.parseInt(configureBean.getDEAL_COUNT().trim());
			}
			odsStmt.execute("insert into " 
					+ Constants.ETL_PHAROS_INCR_INSURES 
					+ ResourceBundleUtil.readValue("riskNodeThreads") 
					+ " (BAT_ID,RECORD_COUNT,STAGE_DATE_READY_STATUS ) values (" 
					+ maxID + "," + recordC + ",'0')");

			// ==================================

			pharosStmt.executeUpdate("update " 
					+ Constants.ETL_PHAROS_INCR_INSURE 
					+ ResourceBundleUtil.readValue("riskNodeThreads") 
					+ " set bat_id=" + maxID + "  where STAGE_WRITE_FLAG='0' and rownum<=" + recordC);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pharosStmt != null) {
					pharosStmt.close();
				}
				if (odsStmt != null) {
					odsStmt.close();
				}
				if (odsConn != null) {
					odsConn.close();
				}
				if (pharosConn != null) {
					pharosConn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

}
