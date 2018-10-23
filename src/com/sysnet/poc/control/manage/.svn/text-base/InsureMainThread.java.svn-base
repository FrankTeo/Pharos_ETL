package com.sysnet.poc.control.manage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;

import com.sysnet.poc.control.config.Constants;
import com.sysnet.poc.control.manage.vo.EveryThreadInfo;
import com.sysnet.poc.control.manage.vo.ShowInfo;
import com.sysnet.poc.control.manage.vo.StatusBean;
import com.sysnet.poc.control.manage.vo.StopBean;
import com.sysnet.poc.control.manage.vo.ThreadCatch;
import com.sysnet.poc.service.dao.InsureScheduleDAO;
import com.sysnet.poc.service.dao.dbc.ConnDB;
import com.sysnet.poc.util.FreeSpaceUtil;
import com.sysnet.poc.util.ResourceBundleUtil;

/**
 * 承保主线程类
 * 
 * @author Li_yanpeng
 * 
 */
public class InsureMainThread extends Thread {
	private ShowInfo si;

	private static final Log log = LogFactory.getLog(InsureMainThread.class);

	private StopBean stopBean;

	private StatusBean statusBean;

	private Document configDocument;
	private String dealMode;

	public InsureMainThread(ShowInfo si) {
		// super();
		this.si = si;
	}

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

	// private ThreadCatch getTC() throws SQLException, NamingException {
	//
	// Connection conn = null;
	// Statement stmt = null;
	// ThreadCatch tc = new ThreadCatch();
	// try {
	// conn = ConnDB.getPharosConn();
	// stmt = conn.createStatement();
	// String sql = "";
	// ResultSet rs = stmt.executeQuery(sql);
	// List<EveryThreadInfo> list = new ArrayList<EveryThreadInfo>();
	// while (rs.next()) {
	// EveryThreadInfo everyThreadInfo = new EveryThreadInfo();
	// everyThreadInfo.setObjectId(rs.getLong("object_Id") + "");
	// everyThreadInfo.setObjectNo(rs.getString("OBJECT_NO"));
	// everyThreadInfo.setObjectType(rs.getString("object_type"));
	// if (rs.getString("CONTRACT_NO") == null || rs.getString("CONTRACT_NO").equals("")) {
	// everyThreadInfo.setPolicNoId("0");
	// } else {
	// everyThreadInfo.setPolicNoId(rs.getString("CONTRACT_NO"));
	// }
	//
	// everyThreadInfo.setVersion(rs.getInt("ASSIST_CODE") + "");
	// everyThreadInfo.setMain_version(rs.getInt("MAIN_VERSION") + "");
	// everyThreadInfo.setProposalNoId(rs.getString("PROPOSAL_NO"));
	// everyThreadInfo.setNodeno(rs.getString("NODE_NO"));
	// String temp = rs.getString("ENDORSE_NO");
	// if (temp == null || temp.equals("")) {
	// temp = "";
	// }
	// everyThreadInfo.setEndorse_no(temp);
	// everyThreadInfo.setProductCD(rs.getString("PRODUCT_CD"));
	// everyThreadInfo.setProductNodeCD(rs.getString("PRODUCT_NODE_CD"));
	// everyThreadInfo.setOperate_node_no(rs.getString("OPERATE_NODE_NO"));
	// list.add(everyThreadInfo);
	// }
	// tc.setEl(list);
	// return tc;
	// } finally {
	// if (stmt != null) {
	// stmt.close();
	// }
	// if (conn != null) {
	// conn.close();
	// }
	// }
	//
	// }

	@SuppressWarnings("unused")
	public void run() {
		// if (true) {
		// ThreadCatch tc = null;
		// try {
		// tc = getTC();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// } else {
		log.info("Task Main thread running...");
		StopBean sbean = new StopBean();
		// List<List<EveryThreadInfo>> list = null;
		ThreadCatch tc = null;
		try {
			// list = this.toAssignAmount(this.getStatusBean().getBAT_ID());
			tc = this.toAssignAmount(this.getStatusBean().getBAT_ID());
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<InsureBusinessThread> threadsPool = new ArrayList<InsureBusinessThread>();
		// if (list != null && list.size() != 0) {
		for (int i = 0; i < this.getStopBean().getThreadC(); i++) {
			InsureBusinessThread insureBusinessThread = new InsureBusinessThread(si);
			insureBusinessThread.setDocument(this.getConfigDocument());
			insureBusinessThread.setDealMode(si.getDealMode());
			insureBusinessThread.setStopBean(sbean);
			insureBusinessThread.setStatusBean(statusBean);
			// insureBusinessThread.setEveryThreadInfo(list.get(i));
			insureBusinessThread.setTc(tc);
			insureBusinessThread.start();
			threadsPool.add(insureBusinessThread);
		}
		// }
		while (true) {
			try {
				log.debug("sbean.getStop() == stopBean.getThreadC()===" + (sbean.getStop() == stopBean.getThreadC()) + "###sbean.getStop()=" + sbean.getStop() + "###stopBean.getThreadC()==" + stopBean.getThreadC());
				if (sbean.getStop() == this.getStopBean().getThreadC()) {
					for (Object object : threadsPool) {
						object = null;
					}
					InsureScheduleDAO dao = new InsureScheduleDAO();
					dao.recoveryNonDeal(statusBean.getBAT_ID());
					stopBean.addStop();
					System.gc();
					log.info("Task Main thread end.");
					break;
				}

//				System.out.println("in sleep");
//				sleep(30000);
//				System.out.println("out sleep");
				if (!FreeSpaceUtil.isHaveSpace()) {
					statusBean.setSuggestStop(true);
				}
			} catch (Exception e) {
				log.info("Task Main thread have error:" + e.getMessage());
				e.printStackTrace();
			}
		}
		// }
	}

	/**
	 * 承保线程子分配处理任务DAO，暂时不拿到DAO层了，因为重构的时候时间太紧 ，该部分处理比较麻烦
	 * 
	 * @param bat_id
	 * @return
	 * @throws NamingException
	 * @throws SQLException
	 */
	// public List<List<EveryThreadInfo>> toAssignAmount(int bat_id) throws NamingException, SQLException {
	// Connection conn = null;
	// Statement stmt = null;
	// List<List<EveryThreadInfo>> list = new ArrayList<List<EveryThreadInfo>>();
	// try {
	// conn = ConnDB.getPharosConn();
	// stmt = conn.createStatement();
	//
	// // --------
	// int threadSize = this.getStopBean().getThreadC();
	// int currenctThread = 0;
	// for (int i = 0; i < threadSize; i++) {
	// List<EveryThreadInfo> el = new ArrayList<EveryThreadInfo>();
	// list.add(el);
	// }
	//
	//
	// // 是发单操作节点
	// ResultSet rs = stmt.executeQuery(sql);
	//
	// while (rs.next()) {
	// EveryThreadInfo everyThreadInfo = new EveryThreadInfo();
	//
	// everyThreadInfo.setObjectId(rs.getLong("object_Id") + "");
	// everyThreadInfo.setObjectNo(rs.getString("OBJECT_NO"));
	// everyThreadInfo.setObjectType(rs.getString("object_type"));
	// if (rs.getString("CONTRACT_NO") == null || rs.getString("CONTRACT_NO").equals("")) {
	// everyThreadInfo.setPolicNoId("0");
	// } else {
	// everyThreadInfo.setPolicNoId(rs.getString("CONTRACT_NO"));
	// }
	//
	// everyThreadInfo.setVersion(rs.getInt("ASSIST_CODE") + "");
	// everyThreadInfo.setMain_version(rs.getInt("MAIN_VERSION") + "");
	// everyThreadInfo.setProposalNoId(rs.getString("PROPOSAL_NO"));
	// everyThreadInfo.setNodeno(rs.getString("NODE_NO"));
	// String temp = rs.getString("ENDORSE_NO");
	// if (temp == null || temp.equals("")) {
	// temp = "";
	// }
	// everyThreadInfo.setEndorse_no(temp);
	// everyThreadInfo.setProductCD(rs.getString("PRODUCT_CD"));
	// everyThreadInfo.setProductNodeCD(rs.getString("PRODUCT_NODE_CD"));
	// everyThreadInfo.setOperate_node_no(rs.getString("OPERATE_NODE_NO"));
	// list.get(currenctThread++ % threadSize).add(everyThreadInfo);
	// }
	//
	// return list;
	// } finally {
	// if (stmt != null) {
	// stmt.close();
	// }
	// if (conn != null) {
	// conn.close();
	// }
	// }
	// }
	public ThreadCatch toAssignAmount(int bat_id) throws NamingException, SQLException {
		Connection conn = null;
		Statement stmt = null;
		ThreadCatch tc = new ThreadCatch();
		try {
			conn = ConnDB.getPharosConn();
			stmt = conn.createStatement();
			String sql = "select object_Id," + "OBJECT_NO,object_type," + "CONTRACT_NO,ASSIST_CODE," + 
					"MAIN_VERSION," + "PROPOSAL_NO," + "NODE_NO," + "PRODUCT_CD," + "PRODUCT_NODE_CD," + 
					"ENDORSE_NO," + "OPERATE_NODE_NO from " + 
					Constants.ETL_PHAROS_INCR_INSURE + ResourceBundleUtil.readValue("riskNodeThreads") + 
					" where bat_id='" + bat_id + "' and STAGE_WRITE_FLAG='0'";// OPERATE_NODE_NO
			ResultSet rs = stmt.executeQuery(sql);
			List<EveryThreadInfo> list = new ArrayList<EveryThreadInfo>();
			while (rs.next()) {
				EveryThreadInfo everyThreadInfo = new EveryThreadInfo();
				everyThreadInfo.setObjectId(rs.getLong("object_Id") + "");
				everyThreadInfo.setObjectNo(rs.getString("OBJECT_NO"));
				everyThreadInfo.setObjectType(rs.getString("object_type"));
				if (rs.getString("CONTRACT_NO") == null || rs.getString("CONTRACT_NO").equals("")) {
					everyThreadInfo.setPolicNoId("0");
				} else {
					everyThreadInfo.setPolicNoId(rs.getString("CONTRACT_NO"));
				}

				everyThreadInfo.setVersion(rs.getInt("ASSIST_CODE") + "");
				everyThreadInfo.setMain_version(rs.getInt("MAIN_VERSION") + "");
				everyThreadInfo.setProposalNoId(rs.getString("PROPOSAL_NO"));
				everyThreadInfo.setNodeno(rs.getString("NODE_NO"));
				String temp = rs.getString("ENDORSE_NO");
				if (temp == null || temp.equals("")) {
					temp = "";
				}
				everyThreadInfo.setEndorse_no(temp);
				everyThreadInfo.setProductCD(rs.getString("PRODUCT_CD"));
				everyThreadInfo.setProductNodeCD(rs.getString("PRODUCT_NODE_CD"));
				everyThreadInfo.setOperate_node_no(rs.getString("OPERATE_NODE_NO"));
				list.add(everyThreadInfo);
			}
			tc.setEl(list);
			return tc;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	public String getDealMode() {
		return dealMode;
	}

	public void setDealMode(String dealMode) {
		this.dealMode = dealMode;
	}
}
