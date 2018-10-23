package com.sysnet.poc.control.manage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.jdom.Document;

import com.sysnet.poc.control.config.Constants;
import com.sysnet.poc.control.manage.vo.EveryThreadInfo;
import com.sysnet.poc.control.manage.vo.StatusBean;
import com.sysnet.poc.control.manage.vo.StopBean;
import com.sysnet.poc.control.manage.vo.ThreadCatch;
import com.sysnet.poc.service.dao.dbc.ConnDB;
import com.sysnet.poc.util.OdsLogger;
import com.sysnet.poc.util.ResourceBundleUtil;

/**
 * 当事方主线程类
 * 
 * @author 卢海滨
 * 
 */
public class PartyMainThread extends Thread {

	private static final Log log = OdsLogger.getLog("partyLog4j", "Party");

	private StopBean stopBean;

	private StatusBean statusBean;

	private Document configDocument;

	public Document getConfigDocument() {
		return configDocument;
	}

	public void setConfigDocument(Document configDocument) {
		this.configDocument = configDocument;
	}

	public StatusBean getStatusBean() {
		return statusBean;
	}

	public void setStatusBean(StatusBean statusBean) {
		this.statusBean = statusBean;
	}

	public StopBean getStopBean() {
		return stopBean;
	}

	public void setStopBean(StopBean stopBean) {
		this.stopBean = stopBean;
	}

	// private List<PartyBusinessThread> pl = new ArrayList<PartyBusinessThread>();

	public void run() {
		log.info("Major thread of party task is begining");
		long startAt = System.currentTimeMillis();
		// StopBean sbean = new StopBean();
		// List<List<EveryThreadInfo>> list = null;
		ThreadCatch tc = new ThreadCatch();
		try {
			tc = this.toAssignAmount(this.getStatusBean().getBAT_ID());
		} catch (NamingException e) {
			log.error("PartyMainThread:", e);
			e.printStackTrace();

		} catch (SQLException e) {

			e.printStackTrace();
		}
		// if (list != null && list.size() != 0) {
		int ntotal = 0;
		for (int i = 0; i < stopBean.getThreadC(); i++) {

			// for (int i = 0; i < list.size(); i++) {

			PartyBusinessThread partyBusinessThread = new PartyBusinessThread();
			partyBusinessThread.setStopBean(stopBean);
			partyBusinessThread.setStatusBean(statusBean);
			partyBusinessThread.setTc(tc);
			ntotal = tc.getEl().size();
			// partyBusinessThread.setEveryThreadInfo(list.get(i));
			partyBusinessThread.start();
		}
		// }
		while (true) {
			if (stopBean.getStop() == stopBean.getThreadC()) {
				if(ntotal == 0) ntotal = 1;
				log.info("Thread of party done" + " Threads:" + stopBean.getThreadC()
						+ " Rows:" + ntotal 
						+ " Cost(ms): " + Long.toString(System.currentTimeMillis()-startAt)
						+ " Performance(s/pr):" + ((System.currentTimeMillis()-startAt)/1000.00)/ntotal); 
				stopBean.addThreadStop();
				break;
			}
			try {
				sleep(1);
				// log.info("6");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 客户线程子线程分配处理任务DAO，暂时不拿到DAO层了，因为重构的时候时间太紧 ，该部分处理比较麻烦
	 * 
	 * @param bat_id
	 * @return
	 * @throws NamingException
	 * @throws SQLException
	 */
	public/* List<List<EveryThreadInfo>> */ThreadCatch toAssignAmount(int bat_id) throws NamingException, SQLException {
		Connection conn = null;
		Statement stmt = null;
		// List<List<EveryThreadInfo>> list = new ArrayList<List<EveryThreadInfo>>();
		ThreadCatch tc = new ThreadCatch();
		try {
			conn = ConnDB.getPharosConn();
			stmt = conn.createStatement();
			List<EveryThreadInfo> el = new ArrayList<EveryThreadInfo>();
			List<EveryThreadInfo> eo = new ArrayList<EveryThreadInfo>();
			ResultSet rs = stmt.executeQuery("" +
					"select object_Id,history_no,object_type,OBJECT_NO,TIME_STAMP,DEAL_FLAG " +
					"from " + Constants.ETL_PHAROS_INCR_PARTY + ResourceBundleUtil.readValue("riskNodeThreads") + 
					" where bat_id='" + bat_id + "' and STAGE_WRITE_FLAG = '0' " 
					/* + "Order By object_id" */
					);// 当前批次的增量总数
			while (rs.next()) {
				EveryThreadInfo everyThreadInfo = new EveryThreadInfo();
				everyThreadInfo.setObjectId(rs.getLong("object_Id") + "");
				everyThreadInfo.setObjectType(rs.getString("object_type"));
				everyThreadInfo.setObjectNo(rs.getString("OBJECT_NO"));
				everyThreadInfo.setTime_stamp(rs.getTimestamp("TIME_STAMP") + "");
				everyThreadInfo.setHistory_no(rs.getLong("HISTORY_NO") + "");
				everyThreadInfo.setDeal_flag(rs.getString("DEAL_FLAG"));
				if (el.contains(everyThreadInfo)) {
					eo.add(everyThreadInfo);
				} else {
					el.add(everyThreadInfo);
				}
			}
			for (EveryThreadInfo everyThreadInfo : eo) {
				stmt.execute("update " + Constants.ETL_PHAROS_INCR_PARTY + ResourceBundleUtil.readValue("riskNodeThreads") + " set STAGE_WRITE_FLAG = '3' where object_id=" + everyThreadInfo.getObjectId());
			}
			tc.setEl(el);
			return tc;
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
				e.printStackTrace();
			}
		}
	}
}
