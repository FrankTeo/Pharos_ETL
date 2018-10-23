package com.sysnet.poc.service.business;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import org.apache.commons.logging.Log;

import pharos.ctm.bo.WContractBO;
import pharos.ctm.vo.WContractNodeBaseInfo;
import pharos.ctm.vo.WContractNodeBaseInfos;

import com.sysnet.poc.control.config.Constants;
import com.sysnet.poc.control.manage.vo.ShowInfo;
import com.sysnet.poc.control.manage.vo.StopBean;
import com.sysnet.poc.control.manage.vo.ThreadCatchDB;
import com.sysnet.poc.mapping.NodesMapping;
import com.sysnet.poc.mapping.vo.Item;
import com.sysnet.poc.mapping.vo.Table;
import com.sysnet.poc.service.dao.PlCIncrementDAO;
import com.sysnet.poc.service.dao.RunLogDAO;
import com.sysnet.poc.service.dao.daoFactory.DAOFactory;
import com.sysnet.poc.service.dao.dbc.ConnDB;
import com.sysnet.poc.service.storage.StorageTransAction;
import com.sysnet.poc.transaction.UserTransactionFactory;
import com.sysnet.poc.util.OdsLogger;
import com.sysnet.poc.util.PharosServiceContainer;
import com.sysnet.poc.util.ResourceBundleUtil;
import com.sysnet.poc.vo.StgRunLog;

/**
 * 提供批单业务的服务 采集数据,入库操作
 * 
 */
public class EndorseBusiness {
	private final ShowInfo si;

	public EndorseBusiness(ShowInfo si) {
		this.si = si;
	}

	private static final Log log = OdsLogger.getLog("insureLog4j", "Insure");
	private final boolean write = true;
	// private Connection pharosConn = null;
	private List<Connection> connL = new ArrayList<Connection>();
	private Connection pharosConnBack = null;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void excuteByMapping(String contract_No, String version, int bat_Id, String object_Id, String object_no, String object_Type, String node_No, String PRODUCT_CD, String PRODUCT_NODE_CD, String main_version, String operate_node_no, String incrProposalNo, String endorseNo) {
		UserTransaction ut = null;
		try {

			PharosServiceContainer service = PharosServiceContainer.Instance();
			WContractBO wContractBo = service.getWContractBO();
			WContractNodeBaseInfos wnbis = wContractBo.getNodeIdsForEndorse(incrProposalNo, endorseNo);
			List<Long> noRiskNodeIDlist = new ArrayList<Long>();
			List<WContractNodeBaseInfo> wbbil = wnbis.getBaseInfoList();
			for (WContractNodeBaseInfo wContractNodeBaseInfo : wbbil) {
				if (wContractNodeBaseInfo.getNodeNo().equals("1-0")) {// 如果节点号是0那么跳过不抽
					continue;
				}
				noRiskNodeIDlist.add(wContractNodeBaseInfo.getId());
			}
			PlCIncrementDAO plCIncrementDAO = DAOFactory.getPlcIncrecentDAO();
			Integer i = 0;
			try {
				i = Integer.parseInt(ResourceBundleUtil.readValue("smtphost"));
			} catch (Exception e) {
				i = 2000;
			}
			Integer j = 0;
			try {
				j = Integer.parseInt(ResourceBundleUtil.readValue("mailusername"));//Nodes
			} catch (Exception e) {
				j = 2000;
			}
			if (noRiskNodeIDlist.size() >= i && noRiskNodeIDlist.size() < j) {
				pharosConnBack = ConnDB.getPharosConn();
				plCIncrementDAO.updateStatus(pharosConnBack, Long.valueOf(object_Id), "3");
				return;
			}
			NodesMapping nm = new NodesMapping("2", noRiskNodeIDlist, null, wContractBo);
			List<List<Long>> lll = nm.getMap();
			List<EndorseNonRiskNodeThead> nonthreadsPool = new ArrayList<EndorseNonRiskNodeThead>();
			StopBean nonSbean = new StopBean();
			this.si.getProcessL().clear();
			int threadCount = lll.size();
			if (threadCount == 1) {
				EndorseNonRiskNodeThead rt = new EndorseNonRiskNodeThead(si.getProcessL());
				rt.setDealMode(si.getDealMode());
				rt.setEndorseNo(endorseNo);
				rt.setStopBean(nonSbean);
				rt.setwContractBo(wContractBo);
				rt.setPRODUCT_CD(PRODUCT_CD);
				rt.setPRODUCT_NODE_CD(PRODUCT_NODE_CD);
				rt.setIncrProposalNo(incrProposalNo);
				rt.setNonRiskNodeIDlist(lll.get(0));
				rt.setObject_Id(object_Id);
				rt.setObject_Type(object_Type);
				rt.setWrite(write);
				rt.setObject_no(object_no);
				rt.run();
				nonthreadsPool.add(rt);
			} else {
				// 多线程
				for (List<Long> list : lll) {
					EndorseNonRiskNodeThead rt = new EndorseNonRiskNodeThead(si.getProcessL());
					rt.setDealMode(si.getDealMode());
					rt.setEndorseNo(endorseNo);
					rt.setStopBean(nonSbean);
					rt.setwContractBo(wContractBo);
					rt.setPRODUCT_CD(PRODUCT_CD);
					rt.setPRODUCT_NODE_CD(PRODUCT_NODE_CD);
					rt.setIncrProposalNo(incrProposalNo);
					rt.setNonRiskNodeIDlist(list);
					rt.setObject_Id(object_Id);
					rt.setObject_Type(object_Type);
					rt.setWrite(write);
					rt.setObject_no(object_no);
					rt.start();
					nonthreadsPool.add(rt);
				}
				while (nonSbean.getAllWriteFlag() && (nonSbean.getStop() != threadCount)) {
					Thread.sleep(threadCount * 100);
//					Thread.sleep(1);
					log.debug(" PolicyBusiness sbean.getStop() == threads" + (nonSbean.getStop() == threadCount));
				}
			}
			if (nonSbean.getAllWriteFlag()) {
				List rl = new ArrayList();
				for (EndorseNonRiskNodeThead nrnt : nonthreadsPool) {
					rl.addAll(nrnt.getResult());
				}
				ut = UserTransactionFactory.getUserTransaction();
				ut.begin();
				ThreadCatchDB tcdb = new ThreadCatchDB();
				tcdb.setEl(rl);
				String policyNo = "";
				for (Object object : rl) {
					Table t = (Table) object;
					if (t.getTableName().equalsIgnoreCase("t_endorse")) {
						List<Item> il = t.getItems();
						for (Item item : il) {
							if (item.getFieldName().equalsIgnoreCase("policyno")) {
								policyNo = item.getValue();
								break;
							}
						}
						break;
					}
				}
				if (threadCount == 1) {
					StorageTransAction storage = new StorageTransAction();
					Connection conn = ConnDB.getODSConn();
					connL.add(conn);
					storage.setConnection(conn);
					storage.setTcdb(tcdb);
					storage.run();
				} else {
					for (int k = 0; k < threadCount; k++) {
						StorageTransAction storage = new StorageTransAction();
						Connection conn = ConnDB.getODSConn();
						connL.add(conn);
						storage.setConnection(conn);
						storage.setTcdb(tcdb);
						storage.start();
					}
					while (tcdb.getIsAllRight() && tcdb.getI() != threadCount) {
						Thread.sleep(threadCount * 100);
//						Thread.sleep(1);
					}
				}
				if (!tcdb.getIsAllRight()) {
					throw new Exception(tcdb.getErrorMessage());
				}
				StorageTransAction storage = new StorageTransAction();
				Connection conn = ConnDB.getODSConn();
				connL.add(conn);
				storage.setConnection(conn);
				storage.extendSQL("call UPDATEODS.updateEndorse(?,?)", new String[] { policyNo, endorseNo });
				ut.commit();
			} else {
				for (EndorseNonRiskNodeThead nrnt : nonthreadsPool) {
					nrnt.interrupt();
				}
			}
			pharosConnBack = ConnDB.getPharosConn();
			if (nonSbean.getAllWriteFlag()) {
				plCIncrementDAO.updateStatus(pharosConnBack, Long.valueOf(object_Id), Constants.INCREMENT_WRITE_BACK_STATUS_1);
			} else {
				plCIncrementDAO.updateStatus(pharosConnBack, Long.valueOf(object_Id), Constants.INCREMENT_WRITE_BACK_STATUS_2);
				StgRunLog stgLog = new StgRunLog();
				stgLog.setErrorCode("2");
				stgLog.setErrorMessage(nonSbean.getErrorMessage());
				stgLog.setObjectId(object_Id);
				stgLog.setObjectNo(object_no);
				RunLogDAO rld = new RunLogDAO();
				try {
					rld.insertLog(stgLog);
				} catch (SQLException e2) {
					log.error("log error:", e2);
				} catch (NamingException e2) {
					log.error("log error:", e2);
				}
			}
		} catch (Exception e) {
			PlCIncrementDAO plCIncrementDAO = DAOFactory.getPlcIncrecentDAO();
			e.printStackTrace();
			try {
				if (ut != null) {
					ut.rollback();
				}
				if (pharosConnBack == null) {
					pharosConnBack = ConnDB.getPharosConn();
				}
				plCIncrementDAO.updateStatus(pharosConnBack, Long.valueOf(object_Id), Constants.INCREMENT_WRITE_BACK_STATUS_2);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			// 记录日志到 stg_run_log 表中
			StgRunLog stgLog = new StgRunLog();
			stgLog.setErrorCode("2");
			stgLog.setErrorMessage(e.toString());
			stgLog.setObjectId(object_Id);
			stgLog.setObjectNo(object_no);
			RunLogDAO rld = new RunLogDAO();
			try {
				rld.insertLog(stgLog);
			} catch (SQLException e2) {
				log.error("log error:", e2);
			} catch (NamingException e2) {
				log.error("log error:", e2);
			}

		} finally {
			try {
				for (Connection conn : connL) {
					if (conn != null) {
						conn.close();
					}
				}
				if (pharosConnBack != null) {
					pharosConnBack.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
