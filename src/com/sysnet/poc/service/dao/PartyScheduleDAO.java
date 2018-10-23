package com.sysnet.poc.service.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.NamingException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.commons.logging.Log;

import com.sysnet.poc.control.config.Constants;
import com.sysnet.poc.control.manage.vo.ConfigureBean;
import com.sysnet.poc.control.manage.vo.StatusBean;
import com.sysnet.poc.service.dao.dbc.ConnDB;
import com.sysnet.poc.transaction.UserTransactionFactory;
import com.sysnet.poc.util.DateHelper;
import com.sysnet.poc.util.OdsLogger;
import com.sysnet.poc.util.ResourceBundleUtil;

/**
 * 当事方调度DAO
 * 
 * @author 卢海滨
 * 
 */
public class PartyScheduleDAO {

	private static final Log log = OdsLogger.getLog("partyLog4j", "Party");

	/**
	 * 查询数据库,根据数据库的配置表ETL_STAGE_ETL_PARTY_CONFIG决定取到处理多少个处理线程等信息
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
			ResultSet rs = stmt.executeQuery("select * from " + Constants.ETL_PHAROS_INCR_PARTYC + ResourceBundleUtil.readValue("riskNodeThreads") + " where START_TIME<=" + currentHour + " and END_TIME>" + currentHour);// 查询当前时间在起始时间和结束时间之间的所有数据并将这些数据放到ConfigureBean对象中
			while (rs.next()) {
				configureBean = new ConfigureBean();
				configureBean.setSTART_TIME(rs.getString("start_time"));
				configureBean.setEND_TIME(rs.getString("end_time"));
				configureBean.setDEAL_COUNT(rs.getString("deal_count"));
				configureBean.setEXCEPT_MAX_THREAD_COUNT(rs.getInt("except_max_thread_count"));
				configureBean.setGENERAL_MAX_THREAD_COUNT(rs.getInt("general_max_thread_count"));
				configureBean.setEXCEPT_SLEEP_MILLIS_COUNT(rs.getInt("except_sleep_millis_count"));
				configureBean.setGENERAL_SLEEP_MILLIS_COUNT(rs.getInt("general_sleep_millis_count"));
				configureBean.setDEAL_MODE(rs.getString("deal_mode"));
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
				log.error("查询数据库,根据数据库的配置表ETL_STAGE_ETL_PARTY_CONFIG决定取到处理多少个处理线程等信息出错!", e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * 查询数据库,根据数据库的配置表ETL_STAGE_ETL_PARTY_STATUS取得当前批次相关信息
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
			ResultSet rs = stmt.executeQuery("select * from " + Constants.ETL_PHAROS_INCR_PARTYS + ResourceBundleUtil.readValue("riskNodeThreads") + " order by BAT_ID desc ");// 按照表中的bat_id进行排序，排序之后只要取第一条记录，即第一条记录就是bat_id的最大值
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
				log.error("查询数据库,根据数据库的配置表ETL_STAGE_ETL_PARTY_STATUS取得当前批次相关信息!", e);
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
			ResultSet rs = stmt.executeQuery("select count(OBJECT_ID) from " + Constants.ETL_PHAROS_INCR_PARTY + ResourceBundleUtil.readValue("riskNodeThreads") + " where bat_id='" + batId + "' and STAGE_WRITE_FLAG='0'");// STAGE_WRITE_FLAG标记是指当ya_stage中相应的表中数据抽取成功之后要回写增量表，来表示ya_stage表处理完毕
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
				log.error("查询pharos增量表，取得当前批次未处理数据数量!", e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * 修ETL_STAGE_ETL_PARTY_STATUS表改标志位
	 * 
	 * @param batId
	 * @throws NamingException
	 * @throws SQLException
	 */
	public void updateBatStatus(String batId) throws NamingException, SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = ConnDB.getODSConn();
			
			String sql = "update " + Constants.ETL_PHAROS_INCR_PARTYS + ResourceBundleUtil.readValue("riskNodeThreads") + " set STAGE_DATE_READY_STATUS=1 where BAT_ID=?";
			
			pstmt =  conn.prepareStatement(sql);

			pstmt.setString(1, batId);
			pstmt.executeUpdate();
		} catch (NamingException ex) {

			throw ex;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				log.error("修ETL_STAGE_ETL_PARTY_STATUS表改标志位!", e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * 修ETL_STAGE_ETL_PARTY_STATUS表改标志位
	 * 
	 * @param configureBean
	 * @param statusBean
	 * @return
	 * @throws Exception
	 */
	public boolean insertOneBat(ConfigureBean configureBean, StatusBean statusBean) throws Exception {
		boolean rs = false;
		UserTransaction ut = null;
		Connection conn = null;
		Connection conn1 = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		
		ResultSet rs1 = null;
		try {

			ut = (UserTransaction) UserTransactionFactory.getUserTransaction();
			try {
				ut.begin();
			} catch (NotSupportedException e2) {

				e2.printStackTrace();
			} catch (SystemException e2) {

				e2.printStackTrace();
			}

			// 判断是否需要产生新的处理批次
			conn1 = ConnDB.getPharosConn();
			Statement state0 = conn1.createStatement();
			ResultSet result = state0.executeQuery("select object_id from " 
					+ Constants.ETL_PHAROS_INCR_PARTY 
					+ ResourceBundleUtil.readValue("riskNodeThreads") 
					+ " where bat_id='0' and STAGE_WRITE_FLAG='0' and rownum<=1 "
					//+ "order by object_id"
					);
			if (!result.next()) {// 查询增量表，如果查询这两个标记位都是0的数据没有了，那么说明增量表中没有增量了，已经全部抽取完毕了，直接返回
				ut.commit();
				conn1.close();
				return false;
			}

			// 产生新的处理批次
			conn = ConnDB.getODSConn();
	
			int maxID = 0;
			int recordC = 0;
			if (statusBean != null) {// 此时就说明
				maxID = statusBean.getBAT_ID() + 1;
				recordC = Integer.parseInt(configureBean.getDEAL_COUNT().trim());
			} else {// 当整个程序开始运行的时候，在批次表中是没数据的，此时是第一次循环，即StatusBean是null，此时批次表的批次号从1开始，记录数就是一个批次处理多少条记录，
				maxID = 1;
				recordC = Integer.parseInt(configureBean.getDEAL_COUNT().trim());
			}
			
			String sqlinsert = "insert into " + Constants.ETL_PHAROS_INCR_PARTYS + ResourceBundleUtil.readValue("riskNodeThreads") + " (BAT_ID,RECORD_COUNT,STAGE_DATE_READY_STATUS ) values (?,?,'0')" ;
			
			pstmt = conn.prepareStatement(sqlinsert);
			pstmt.setInt(1, maxID);
			pstmt.setInt(2, recordC);
			pstmt.execute();
			
			
			
			// ==================================
			String sql_object_id = "select object_id from " 
					+ Constants.ETL_PHAROS_INCR_PARTY 
					+ ResourceBundleUtil.readValue("riskNodeThreads") 
					+ " where STAGE_WRITE_FLAG = '0' and rownum<=? "
					//+ "order by object_id" 
					;
			pstmt1 = conn1.prepareStatement(sql_object_id);
			
			pstmt1.setInt(1, recordC);
			rs1 = pstmt1.executeQuery();
			String sql1 = "update " + Constants.ETL_PHAROS_INCR_PARTY + ResourceBundleUtil.readValue("riskNodeThreads") + " set bat_id=?  where object_id=?" ;
			
			pstmt1 = conn1.prepareStatement(sql1);

			for (int i = 1; rs1.next() && (i <= recordC); i++) {
				pstmt1.setInt(1, maxID);
				pstmt1.setLong(2, rs1.getLong("object_id"));
				pstmt1.executeUpdate();
			}
			ut.commit();
			rs = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				ut.rollback();
			} catch (IllegalStateException e) {
				log.error("修ETL_STAGE_ETL_PARTY_STATUS表改标志位!", e);
				e.printStackTrace();
			} catch (SecurityException e) {
				log.error("修ETL_STAGE_ETL_PARTY_STATUS表改标志位!", e);
				e.printStackTrace();
			} catch (SystemException e) {
				log.error("修ETL_STAGE_ETL_PARTY_STATUS表改标志位!", e);
				e.printStackTrace();
			}

			throw ex;
		} finally {
			try {
				if (rs1 != null) {
					rs1.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (pstmt1 != null) {
					pstmt1.close();
				}
				if (conn != null) {
					conn.close();
				}
				if (conn1 != null) {
					conn1.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return rs;
	}

}
