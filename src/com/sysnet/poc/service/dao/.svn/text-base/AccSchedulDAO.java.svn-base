package com.sysnet.poc.service.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.commons.logging.Log;

import com.sysnet.poc.control.config.Constants;
import com.sysnet.poc.control.manage.vo.ConfigureBean;
import com.sysnet.poc.control.manage.vo.EtlPharosIncrAcc;
import com.sysnet.poc.control.manage.vo.StatusBean;
import com.sysnet.poc.service.dao.dbc.ConnDB;
import com.sysnet.poc.transaction.UserTransactionFactory;
import com.sysnet.poc.util.DateHelper;
import com.sysnet.poc.util.OdsLogger;
import com.sysnet.poc.util.ResourceBundleUtil;

public class AccSchedulDAO {
	private Connection pharosConn = null;
	private Connection odsConn = null;

	public Connection getPharosConn() {
		return pharosConn;
	}

	public void setPharosConn(Connection pharosConn) {
		this.pharosConn = pharosConn;
	}

	public Connection getOdsConn() {
		return odsConn;
	}

	public void setOdsConn(Connection odsConn) {
		this.odsConn = odsConn;
	}

	private static final Log log = OdsLogger.getLog("accLog4j", "ACC");

	public ConfigureBean getConfigure() throws NamingException, SQLException {
		Connection conn = null;
		Statement stmt = null;
		ConfigureBean configureBean = null;
		try {
			int currentHour = DateHelper.getHour();
			conn = ConnDB.getODSConn();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from " + Constants.ETL_PHAROS_INCR_ACCC + ResourceBundleUtil.readValue("riskNodeThreads") + " where START_TIME<=" + currentHour + " and END_TIME>" + currentHour);// 查询当前时间在起始时间和结束时间之间的所有数据并将这些数据放到ConfigureBean对象中
			while (rs.next()) {
				configureBean = new ConfigureBean();
				configureBean.setSTART_TIME(rs.getString("start_time"));
				configureBean.setEND_TIME(rs.getString("end_time"));
				configureBean.setDEAL_COUNT(rs.getString("deal_count"));
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
				log.error("查询数据库,根据数据库的配置表ETL_STAGE_ETL_ACC_CONFIG决定取到处理多少个处理线程等信息出错!", e);
				e.printStackTrace();
			}
		}
	}

	public StatusBean getBatStatus() throws NamingException, SQLException {
		StatusBean statusBean = new StatusBean();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnDB.getODSConn();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from " + Constants.ETL_PHAROS_INCR_ACCS + ResourceBundleUtil.readValue("riskNodeThreads") + " order by BAT_ID desc ");// 按照表中的bat_id进行排序，排序之后只要取第一条记录，即第一条记录就是bat_id的最大值
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
				log.error("查询数据库,根据数据库的配置表ETL_STAGE_ETL_ACC_STATUS取得当前批次相关信息!", e);
				e.printStackTrace();
			}
		}
	}

	public int getPharosIncr(String batId) throws NamingException, SQLException {
		int count = 0;
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnDB.getPharosConn();
			stmt = conn.createStatement();
			// ResultSet rs = stmt.executeQuery("select count(OBJECT_ID) from ETL_PHAROS_INCR_ACC where bat_id='" + batId + "' and (STAGE_WRITE_FLAG = 0 or STAGE_WRITE_FLAG is null)");// STAGE_WRITE_FLAG标记是指当ya_stage中相应的表中数据抽取成功之后要回写增量表，来表示ya_stage表处理完毕
			ResultSet rs = stmt.executeQuery("select count(OBJECT_ID) from " + Constants.ETL_PHAROS_INCR_ACC + ResourceBundleUtil.readValue("riskNodeThreads") + " where bat_id='" + batId + "' and STAGE_WRITE_FLAG = 0");// STAGE_WRITE_FLAG标记是指当ya_stage中相应的表中数据抽取成功之后要回写增量表，来表示ya_stage表处理完毕
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

	public void updateBatStatus(String batId) throws NamingException, SQLException {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnDB.getODSConn();
			stmt = conn.createStatement();
			stmt.execute("update " + Constants.ETL_PHAROS_INCR_ACCS + ResourceBundleUtil.readValue("riskNodeThreads") + " set STAGE_DATE_READY_STATUS=1 where BAT_ID=" + batId);
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
				log.error("修ETL_STAGE_ETL_ACC_STATUS表改标志位!", e);
				e.printStackTrace();
			}
		}
	}

	public boolean insertOneBat(ConfigureBean configureBean, StatusBean statusBean) throws Exception {
		boolean rs = false;
		UserTransaction ut = null;
		Connection conn = null;
		Connection conn1 = null;
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
			// ResultSet result = state0.executeQuery("select object_id from ETL_PHAROS_INCR_ACC where (STAGE_WRITE_FLAG='0' or STAGE_WRITE_FLAG is null) and (bat_id is null or bat_id='0') and rownum<=1 order by object_id");
			ResultSet result = state0.executeQuery("select object_id from " + Constants.ETL_PHAROS_INCR_ACC + ResourceBundleUtil.readValue("riskNodeThreads") + " where bat_id='0' and STAGE_WRITE_FLAG='0' and rownum<=1 order by object_id");
			if (!result.next()) {// 查询增量表，如果查询这两个标记位都是0的数据没有了，那么说明增量表中没有增量了，已经全部抽取完毕了，直接返回
				ut.commit();
				conn1.close();
				return false;
			}

			// 产生新的处理批次
			conn = ConnDB.getODSConn();
			Statement stmt = conn.createStatement();
			int maxID = 0;
			int recordC = 0;
			if (statusBean != null) {// 此时就说明
				maxID = statusBean.getBAT_ID() + 1;
				recordC = statusBean.getRECORD_COUNT();
			} else {// 当整个程序开始运行的时候，在批次表中是没数据的，此时是第一次循环，即StatusBean是null，此时批次表的批次号从1开始，记录数就是一个批次处理多少条记录，
				maxID = 1;
				recordC = Integer.parseInt(configureBean.getDEAL_COUNT().trim());
			}
			stmt.execute("insert into " + Constants.ETL_PHAROS_INCR_ACCS + ResourceBundleUtil.readValue("riskNodeThreads") + " (BAT_ID,RECORD_COUNT,STAGE_DATE_READY_STATUS ) values (" + maxID + "," + recordC + ",'0')");

			// ==================================

			Statement state1 = conn1.createStatement();
			// ResultSet rs1 = state1.executeQuery("select object_id from ETL_PHAROS_INCR_ACC where object_type='101' And (STAGE_WRITE_FLAG = '0' or STAGE_WRITE_FLAG is null) and rownum<=" + recordC + " order by object_id");
			ResultSet rs1 = state1.executeQuery("select object_id from " + Constants.ETL_PHAROS_INCR_ACC + ResourceBundleUtil.readValue("riskNodeThreads") + " where object_type='101' And STAGE_WRITE_FLAG = '0' and rownum<=" + recordC + " order by object_id");
//			Statement state2 = conn1.createStatement();
			
			PreparedStatement pstmt = null;
			
			for (int i = 1; rs1.next() && (i <= recordC); i++) {
//				state2.executeUpdate("update " + Constants.ETL_PHAROS_INCR_ACC + ResourceBundleUtil.readValue("riskNodeThreads") + " set bat_id=" + maxID + " where object_id=" + rs1.getLong("object_id"));
	 
				String sql = "update " + Constants.ETL_PHAROS_INCR_ACC + ResourceBundleUtil.readValue("riskNodeThreads") + " set bat_id=? where object_id=?" ;
				pstmt = conn1.prepareStatement(sql);

				pstmt.setInt(1, maxID);
				pstmt.setLong(2, rs1.getLong("object_id"));
				pstmt.executeUpdate();
				pstmt.close();
				
			}
			ut.commit();
			rs = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				ut.rollback();
			} catch (IllegalStateException e) {
				log.error("修ETL_STAGE_ETL_ACC_STATUS表改标志位!", e);
				e.printStackTrace();
			} catch (SecurityException e) {
				log.error("修ETL_STAGE_ETL_ACC_STATUS表改标志位!", e);
				e.printStackTrace();
			} catch (SystemException e) {
				log.error("修ETL_STAGE_ETL_ACC_STATUS表改标志位!", e);
				e.printStackTrace();
			}

			throw ex;
		} finally {
			try {
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

	public List<EtlPharosIncrAcc> incrementList(int batId) throws SQLException, Exception {
		List<EtlPharosIncrAcc> incrList = new ArrayList<EtlPharosIncrAcc>();
		StringBuffer sql = new StringBuffer(100);
		// sql.append("select object_id, " + "object_no, " + "object_type, " + "bat_id, " + "contract_no, " + "node_no, " + "assist_code, " + "time_stamp, " + "deal_flag, " + "stage_write_flag, " + "ods_write_flag, " + "IS_CLAIM  from etl_pharos_incr_acc where BAT_ID=? and (stage_write_flag = '0' or stage_write_flag is null)");
		sql.append("select object_id, " + "object_no, " + "object_type, " + "bat_id, " + "contract_no, " + "node_no, " + "assist_code, " + "time_stamp, " + "deal_flag, " + "stage_write_flag, " + "ods_write_flag, " + "IS_CLAIM  from " + Constants.ETL_PHAROS_INCR_ACC + ResourceBundleUtil.readValue("riskNodeThreads") + " where BAT_ID=? and stage_write_flag = '0'");
		PreparedStatement ps = pharosConn.prepareStatement(sql.toString());
		ps.setInt(1, batId);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			EtlPharosIncrAcc incrAcc = new EtlPharosIncrAcc();
			incrAcc.setObjectId(rs.getLong(1));
			incrAcc.setObjectNo(rs.getString(2));
			incrAcc.setObjectType(rs.getLong(3));
			incrAcc.setBatId(rs.getLong(4));
			incrAcc.setContractNo(rs.getString(5));
			incrAcc.setNodeNo(rs.getString(6));
			incrAcc.setAssistCode(rs.getString(7));
			incrAcc.setTimeStamp(rs.getDate(8));
			incrAcc.setDealFlag(rs.getString(9));
			incrAcc.setStageWriteFlag(rs.getString(10));
			incrAcc.setOdsWriteFlag(rs.getString(11));
			incrAcc.setIsClaim(rs.getBoolean(12));
			incrList.add(incrAcc);
		}
		rs.close();
		ps.close();
		return incrList;
	}

	public void updateAccStatus(Long objectId) throws SQLException, Exception {
		String sql = "update " + Constants.ETL_PHAROS_INCR_ACC + ResourceBundleUtil.readValue("riskNodeThreads") + " set stage_write_flag=? where object_id=?";
		PreparedStatement ps = this.pharosConn.prepareStatement(sql);
		ps.setString(1, Constants.INCREMENT_WRITE_BACK_STATUS_1);
		ps.setLong(2, objectId);
		ps.executeUpdate();
		ps.close();

	}

	public void updateAccStatus(EtlPharosIncrAcc e) throws SQLException, Exception {
		String sql = "update " + Constants.ETL_PHAROS_INCR_ACC + ResourceBundleUtil.readValue("riskNodeThreads") + " set BAT_ID=?," + "STAGE_WRITE_FLAG=? where OBJECT_ID=?";

		PreparedStatement ps = pharosConn.prepareStatement(sql);

		ps.setLong(1, e.getBatId());
		ps.setString(2, e.getStageWriteFlag());
		ps.setLong(3, e.getObjectId());
		ps.executeUpdate();
		ps.close();

	}
}
