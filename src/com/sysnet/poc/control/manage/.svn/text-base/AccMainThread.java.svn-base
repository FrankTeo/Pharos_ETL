package com.sysnet.poc.control.manage;

import java.sql.Connection;
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
import com.sysnet.poc.service.dao.AccSchedulDAO;
import com.sysnet.poc.service.dao.daoFactory.DAOFactory;
import com.sysnet.poc.service.dao.dbc.ConnDB;
import com.sysnet.poc.util.OdsLogger;
import com.sysnet.poc.util.ResourceBundleUtil;

/**
 * 收付费主线程类
 * 
 * @author 卢海滨
 * 
 */
public class AccMainThread extends Thread {

	private static final Log log = OdsLogger.getLog("accLog4j", "ACC");

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

	public void run() {
		log.info("Major thread of pay is begining");
		StopBean sbean = new StopBean();

		Connection pharosConn = null;
		AccSchedulDAO accSchedulDAO = null;
		accSchedulDAO = DAOFactory.getAccSchedulDAO();
		accSchedulDAO.setPharosConn(pharosConn);
		ThreadCatch tc = new ThreadCatch();
		try {
			tc = this.toAssignAmount(this.getStatusBean().getBAT_ID());
		} catch (NamingException e) {
			log.error("AccMainThreadLHB:", e);
			e.printStackTrace();

		} catch (SQLException e) {

			e.printStackTrace();
		}
		for (int i = 0; i < stopBean.getThreadC(); i++) {

			AccBusinessThread accBusinessThread = new AccBusinessThread();
			accBusinessThread.setStopBean(sbean);
			accBusinessThread.setStatusBean(statusBean);
			// accBusinessThread.setEveryThreadInfo(list.get(i));
			accBusinessThread.setTc(tc);
			accBusinessThread.start();
		}

		while (true) {

			if (sbean.getStop() == stopBean.getThreadC()) {
				stopBean.addStop();
				log.info("Thread of pay done");
				break;
			}
			try {
				// log.info("1");
				sleep(1);
			} catch (InterruptedException e) {
				log.info("任务主线程出错" + e.getMessage());
				e.printStackTrace();
			}
		}

	}

	/**
	 * 收付费线程子线程分配处理任务DAO，暂时不拿到DAO层了，因为重构的时候时间太紧 ，该部分处理比较麻烦
	 * 
	 * @param bat_id
	 * @return
	 * @throws NamingException
	 * @throws SQLException
	 */
	public ThreadCatch toAssignAmount(int bat_id) throws NamingException, SQLException {
		ThreadCatch tc = new ThreadCatch();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnDB.getPharosConn();
			stmt = conn.createStatement();
			String sql = "select object_Id,object_type,OBJECT_NO,TIME_STAMP,DEAL_FLAG,bat_id,IS_CLAIM from " + Constants.ETL_PHAROS_INCR_ACC + ResourceBundleUtil.readValue("riskNodeThreads") + " where bat_id='" + bat_id + "' and STAGE_WRITE_FLAG = 0 Order By object_id";
			ResultSet rs = stmt.executeQuery(sql);
			List<EveryThreadInfo> el = new ArrayList<EveryThreadInfo>();
			while (rs.next()) {
				EveryThreadInfo everyThreadInfo = new EveryThreadInfo();
				everyThreadInfo.setObjectId(rs.getLong("object_Id") + "");
				everyThreadInfo.setObjectType(rs.getString("object_type"));
				everyThreadInfo.setObjectNo(rs.getString("OBJECT_NO"));
				everyThreadInfo.setTime_stamp(rs.getTimestamp("TIME_STAMP") + "");
				everyThreadInfo.setIs_claim(rs.getBoolean("IS_CLAIM") + "");
				everyThreadInfo.setDeal_flag(rs.getString("DEAL_FLAG"));
				everyThreadInfo.setBat_Id(rs.getLong("bat_id") + "");
				el.add(everyThreadInfo);
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
