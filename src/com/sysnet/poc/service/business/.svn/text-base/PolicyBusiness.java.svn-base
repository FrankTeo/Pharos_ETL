package com.sysnet.poc.service.business;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import org.apache.commons.logging.Log;

import pharos.ctm.bo.BContractBO;
import pharos.ctm.vo.BContractNodeBaseInfo;
import pharos.ctm.vo.BContractNodeBaseInfos;
import pharos.ctm.vo.BQueryCondition;

import com.sysnet.poc.control.config.Constants;
import com.sysnet.poc.control.manage.vo.ShowInfo;
import com.sysnet.poc.control.manage.vo.StopBean;
import com.sysnet.poc.control.manage.vo.ThreadCatchDB;
import com.sysnet.poc.mapping.NodesMapping;
import com.sysnet.poc.service.dao.PlCIncrementDAO;
import com.sysnet.poc.service.dao.RunLogDAO;
import com.sysnet.poc.service.dao.daoFactory.DAOFactory;
import com.sysnet.poc.service.dao.dbc.ConnDB;
import com.sysnet.poc.service.storage.StorageTransAction;
import com.sysnet.poc.transaction.UserTransactionFactory;
import com.sysnet.poc.util.OdsLogger;
import com.sysnet.poc.util.RemoveDataUtil;
import com.sysnet.poc.util.ResourceBundleUtil;
import com.sysnet.poc.vo.StgRunLog;

/**
 * 提供保单业务的服务 采集数据,入库操作
 * 
 */
public class PolicyBusiness {
	private final ShowInfo si;
	BContractBO bContractBo;

	public PolicyBusiness(ShowInfo si, BContractBO bContractBo) {
		super();
		this.si = si;
		this.bContractBo = bContractBo;
	}

	private static final Log log = OdsLogger.getLog("insureLog4j", "Insure");
	private final boolean write = true;
	// private Connection pharosConn = null;
	private List<Connection> connL = new ArrayList<Connection>();
	private Connection pharosConnBack = null;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void excuteByMapping(String contract_No, String version, String main_version, int bat_Id, String object_Id, String object_no, String object_Type, String node_No, String PRODUCT_CD, String PRODUCT_NODE_CD, String incrProposalNo) {
		UserTransaction ut = null;
		try {
			String policyNo = "";
			BQueryCondition queryCon = new BQueryCondition();
//			queryCon.setVersion(1);	//pharos 新版本不需要传这个参数
//			queryCon.setMinVersion(1);//pharos 新版本不需要传这个参数
			queryCon.setContractNo(contract_No);
			queryCon.setNodeNo(node_No, false);
//			queryCon.setPolicyLevel(0);//pharos 新版本不需要传这个参数
			int[] nodeFields = { BContractNodeBaseInfo.ID, BContractNodeBaseInfo.NODE_NO, BContractNodeBaseInfo.PRODUCT_NODE_CODE, BContractNodeBaseInfo.POLICY_NO };
			BContractNodeBaseInfos infos = this.bContractBo.queryBContractNodes(queryCon, nodeFields, 0, 1);
			if (infos != null && infos.size() > 0) {
				policyNo = infos.get(0).getPolicyNo();
			} else {
				throw new Exception("policyNo is null!!!!!");
			}
			if (Runtime.getRuntime().freeMemory() < 100 * 1024 * 1024) {
				log.debug("调用GC");
				System.gc();
			}
			ArrayList<Long> nonRiskNodeIDlist = new ArrayList<Long>();
			BContractNodeBaseInfos bnbis = this.bContractBo.getNodeIdsForPolicy(policyNo);
			List<BContractNodeBaseInfo> bcnbiL = bnbis.getBaseInfoList();
			for (BContractNodeBaseInfo bContractNodeBaseInfo : bcnbiL) {
				if (bContractNodeBaseInfo.getNodeNo().equals("1-0")) {// 如果节点号是0那么跳过不抽
					continue;
				}
				nonRiskNodeIDlist.add(bContractNodeBaseInfo.getId());
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
				j = Integer.parseInt(ResourceBundleUtil.readValue("mailusername"));
			} catch (Exception e) {
				j = 2000;
			}
			if (nonRiskNodeIDlist.size() >= i && nonRiskNodeIDlist.size() < j) {
				pharosConnBack = ConnDB.getPharosConn();
				plCIncrementDAO.updateStatus(pharosConnBack, Long.valueOf(object_Id), "3");
				return;
			}
			NodesMapping nm = new NodesMapping("1", nonRiskNodeIDlist, this.bContractBo, null);
			List<List<Long>> lll = nm.getMap();
			List<NonRiskNodeThead> nonthreadsPool = new ArrayList<NonRiskNodeThead>();
			StopBean nonSbean = new StopBean();
			this.si.getProcessL().clear();
			int threadCount = lll.size();
			if (threadCount == 1) {
				NonRiskNodeThead rt = new NonRiskNodeThead(si.getProcessL());
				rt.setDealMode(si.getDealMode());
				rt.setStopBean(nonSbean);
				rt.setBContractBo(this.bContractBo);
				rt.setPRODUCT_CD(PRODUCT_CD);
				rt.setPRODUCT_NODE_CD(PRODUCT_NODE_CD);
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
					NonRiskNodeThead rt = new NonRiskNodeThead(si.getProcessL());
					rt.setDealMode(si.getDealMode());
					rt.setStopBean(nonSbean);
					rt.setBContractBo(this.bContractBo);
					rt.setPRODUCT_CD(PRODUCT_CD);
					rt.setPRODUCT_NODE_CD(PRODUCT_NODE_CD);
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
				for (NonRiskNodeThead nrnt : nonthreadsPool) {
					rl.addAll(nrnt.getResult());
				}
				ut = UserTransactionFactory.getUserTransaction();
				ut.begin();
				ThreadCatchDB tcdb = new ThreadCatchDB();
				tcdb.setEl(rl);
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
				storage.extendSQL("call UPDATEODS.updatePolicy(?)", new String[] { policyNo });
				ut.commit();
			} else {
				for (NonRiskNodeThead nrnt : nonthreadsPool) {
					nrnt.interrupt();
				}
			}
			pharosConnBack = ConnDB.getPharosConn();
			if (nonSbean.getAllWriteFlag()) {
				plCIncrementDAO.updateStatus(pharosConnBack, Long.valueOf(object_Id), Constants.INCREMENT_WRITE_BACK_STATUS_1);
			} else {
				plCIncrementDAO.updateStatus(pharosConnBack, Long.valueOf(object_Id), Constants.INCREMENT_WRITE_BACK_STATUS_2);
				StgRunLog stgLog = new StgRunLog();
				stgLog.setErrorCode("1");
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
			stgLog.setErrorCode("1");
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
