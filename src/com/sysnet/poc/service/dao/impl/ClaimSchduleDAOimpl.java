package com.sysnet.poc.service.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sysnet.poc.control.config.Constants;
import com.sysnet.poc.control.manage.vo.ConfigureBean;
import com.sysnet.poc.control.manage.vo.EtlPharosIncrClaim;
import com.sysnet.poc.control.manage.vo.StatusBean;
import com.sysnet.poc.service.dao.ClaimSchduleDAO;
import com.sysnet.poc.service.dao.dbc.ConnDB;
import com.sysnet.poc.util.DateHelper;
import com.sysnet.poc.util.ResourceBundleUtil;

public class ClaimSchduleDAOimpl implements ClaimSchduleDAO {

	private Connection pharosConn = null;
	private Connection odsConn = null;
	private PreparedStatement ps = null;

	public int incrementCount(int batId) throws SQLException, Exception {
		int count = 0;
		String sql = "select count(object_id) " 
				+ "from " + Constants.ETL_PHAROS_INCR_CLAIM 
				+ ResourceBundleUtil.readValue("riskNodeThreads") 
				+ " t where bat_id=? and stage_write_flag = '0'";
		ps = pharosConn.prepareStatement(sql);
		ps.setInt(1, batId);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			count = rs.getInt(1);
		}
		rs.close();
		ps.close();
		return count;
	}

	public List<EtlPharosIncrClaim> incrementListByBatID(int batId) throws SQLException, Exception {
		List<EtlPharosIncrClaim> list = new ArrayList<EtlPharosIncrClaim>();
		EtlPharosIncrClaim epic = null;
		String sql = "select OBJECT_ID," + "OBJECT_NO," + "OBJECT_TYPE," + "BAT_ID," + "CONTRACT_NO,"
				+ "NODE_ID," + "ASSIST_CODE," + "TIME_STAMP," + "DEAL_FLAG," + "STAGE_WRITE_FLAG,"
				+ "ODS_WRITE_FLAG," + "ACTIVITYID," + "ACTIVITYNAME," + "PROCESSID," + "ACTIVITYTYPE,"
				+ "TRANSACTIONTYPE," + "PROPOSALID," + "PROPOSALNODEID," + "PROPOSALNO," + "CONTRACTNO,"
				+ "NODENO," + "CLAIMID," + "PARTCLAIMID "
				+ "from " + Constants.ETL_PHAROS_INCR_CLAIM
				+ ResourceBundleUtil.readValue("riskNodeThreads")
				+ " where BAT_ID=? and stage_write_flag = '0'"
				+ " and claimid!=0" 
				+ " ";	
		ps = pharosConn.prepareStatement(sql);
		ps.setInt(1, batId);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			epic = new EtlPharosIncrClaim();
			epic.setObjectId(rs.getLong("OBJECT_ID"));
			epic.setObjectNo(rs.getString("OBJECT_NO"));
			epic.setObjectType(rs.getLong("OBJECT_TYPE"));
			epic.setBatId(rs.getLong("BAT_ID"));
			epic.setContractNo(rs.getString("CONTRACT_NO"));
			epic.setNodeId(rs.getLong("NODE_ID"));
			epic.setAssistCode(rs.getString("ASSIST_CODE"));
			epic.setTimeStamp(rs.getTimestamp("TIME_STAMP"));
			epic.setDealFlag(rs.getString("DEAL_FLAG"));
			epic.setStageWriteFlag(rs.getString("STAGE_WRITE_FLAG"));
			epic.setOdsWriteFlag(rs.getString("ODS_WRITE_FLAG"));
			epic.setActivityId(rs.getLong("ACTIVITYID"));
			epic.setActivityName(rs.getString("ACTIVITYNAME"));
			epic.setProcessId(rs.getLong("PROCESSID"));
			epic.setActivityType(rs.getLong("ACTIVITYTYPE"));
			epic.setTransactionType(rs.getString("TRANSACTIONTYPE"));
			epic.setProposalId(rs.getLong("PROPOSALID"));
			epic.setProposalNodeId(rs.getLong("PROPOSALNODEID"));
			epic.setProposalNo(rs.getString("PROPOSALNO"));
			epic.setPolicNo(rs.getString("CONTRACTNO"));
			epic.setNodeNo(rs.getString("NODENO"));
			epic.setClaimId(rs.getLong("CLAIMID"));
			epic.setPartClaimId(rs.getLong("PARTCLAIMID"));
			list.add(epic);
		}
		rs.close();
		ps.close();
		return list;
	}

	public ConfigureBean initConfig() throws SQLException, Exception {
		ConfigureBean config = null;
		int currentHour = DateHelper.getHour();
		String sql = "select * from " 
				+ Constants.ETL_PHAROS_INCR_CLAIMC 
				+ ResourceBundleUtil.readValue("riskNodeThreads") + " where START_TIME<=? and END_TIME>?";
		ps = odsConn.prepareStatement(sql);
		ps.setInt(1, currentHour);
		ps.setInt(2, currentHour);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			config = new ConfigureBean();
			config.setSTART_TIME(rs.getString("start_time"));
			config.setEND_TIME(rs.getString("end_time"));
			config.setDEAL_COUNT(rs.getString("deal_count"));
			config.setGENERAL_MAX_THREAD_COUNT(rs.getInt("general_max_thread_count"));
			config.setEXCEPT_MAX_THREAD_COUNT(rs.getInt("except_max_thread_count"));
			config.setGENERAL_SLEEP_MILLIS_COUNT(rs.getInt("general_sleep_millis_count"));
			config.setEXCEPT_SLEEP_MILLIS_COUNT(rs.getInt("except_sleep_millis_count"));
		}
		return config;
	}

	public boolean isCrateIncrement() throws SQLException, Exception {
		boolean flag = false;
		String sql = "select object_id from " 
				+ Constants.ETL_PHAROS_INCR_CLAIM 
				+ ResourceBundleUtil.readValue("riskNodeThreads") 
				+ " where bat_id ='0' and stage_write_flag = '0' and claimid!=0 and rownum<=1";
		ps = pharosConn.prepareStatement(sql);
		ps.setMaxRows(1);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			flag = true;
		} else {
			flag = false;
		}
		ps.close();
		return flag;
	}

	public void updateBatId(Long objectId, int batId) throws SQLException, Exception {
		String sql = "update " 
				+ Constants.ETL_PHAROS_INCR_CLAIM 
				+ ResourceBundleUtil.readValue("riskNodeThreads") 
				+ " set bat_id=?,stage_write_flag=? where object_id=?";
		ps = pharosConn.prepareStatement(sql);
		ps.setInt(1, batId);
		ps.setString(2, Constants.INCREMENT_WRITE_BACK_STATUS_0);
		ps.setLong(3, objectId);
		ps.executeUpdate();
		ps.close();

	}

	public void updateStatus(int objectId, int batId) throws SQLException, Exception {

	}

	public void setOdsConn(Connection conn) {
		this.odsConn = conn;

	}

	public void setPharosConn(Connection conn) {
		this.pharosConn = conn;

	}

	public StatusBean initStatus() throws SQLException, Exception {
		StatusBean status = null;
		String sql = "select * from " 
				+ Constants.ETL_PHAROS_INCR_CLAIMS 
				+ ResourceBundleUtil.readValue("riskNodeThreads") 
				+ " order by BAT_ID desc"
				;
		ps = odsConn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			status = new StatusBean();
			status.setBAT_ID(rs.getInt("bat_id"));
			status.setMIN_OBJECT_ID(rs.getInt("min_object_id"));
			status.setMAX_OBJECT_ID(rs.getInt("max_object_id"));
			status.setRECORD_COUNT(rs.getInt("record_count"));
			status.setSTAGE_DATE_READY_STATUS(rs.getString("stage_date_ready_status"));
			status.setODS_DATA_DEAL_STATUS(rs.getString("ods_data_deal_status"));
		}
		ps.close();
		return status;
	}

	public void updateStageStatus(int batId, String status) throws SQLException, Exception {
		String sql = "update " 
				+ Constants.ETL_PHAROS_INCR_CLAIMS 
				+ ResourceBundleUtil.readValue("riskNodeThreads") 
				+ " set STAGE_DATE_READY_STATUS=? where BAT_ID=?";
		ps = odsConn.prepareStatement(sql);
		ps.setString(1, status);
		ps.setInt(2, batId);
		ps.executeUpdate();
		ps.close();

	}

	public void createStatus(int batId, int recordCount) throws SQLException, Exception {
		String sql = "insert into " 
				+ Constants.ETL_PHAROS_INCR_CLAIMS 
				+ ResourceBundleUtil.readValue("riskNodeThreads") 
				+ " (BAT_ID,RECORD_COUNT,STAGE_DATE_READY_STATUS ) values (?,?,?)";
		ps = odsConn.prepareStatement(sql);
		ps.setInt(1, batId);
		ps.setInt(2, recordCount);
		ps.setString(3, Constants.STAGE_DATE_READY_STATUS_0);
		ps.executeUpdate();
		ps.close();
	}

	public List<Long> incrementList(int count) throws SQLException, Exception {
		List<Long> list = new ArrayList<Long>();
		String sql = "select object_id from " 
				+ Constants.ETL_PHAROS_INCR_CLAIM 
				+ ResourceBundleUtil.readValue("riskNodeThreads") 
				+ " where bat_id='0' and stage_write_flag='0' "
				+ " and claimid!=0 "
				+ " and rownum<=? "
				+ "order by object_id "
				;
		ps = pharosConn.prepareStatement(sql);
		ps.setInt(1, count);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			list.add(rs.getLong(1));
		}
		rs.close();
		ps.close();
		return list;
	}

}
