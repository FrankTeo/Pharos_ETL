package com.sysnet.poc.service.business;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import org.apache.commons.logging.Log;

import pharos.ctm.bo.BContractBO;
import pharos.ctm.bo.WContractBO;
import pharos.ctm.vo.WContractNodeBaseInfo;
import pharos.ctm.vo.WContractNodeBaseInfos;

import com.sysnet.poc.control.config.Constants;
import com.sysnet.poc.control.manage.vo.ShowInfo;
import com.sysnet.poc.control.manage.vo.StopBean;
import com.sysnet.poc.control.manage.vo.ThreadCatchDB;
import com.sysnet.poc.service.dao.PlCIncrementDAO;
import com.sysnet.poc.service.dao.RunLogDAO;
import com.sysnet.poc.service.dao.daoFactory.DAOFactory;
import com.sysnet.poc.service.dao.dbc.ConnDB;
import com.sysnet.poc.service.storage.StorageTransAction;
import com.sysnet.poc.transaction.UserTransactionFactory;
import com.sysnet.poc.util.OdsLogger;
import com.sysnet.poc.util.PharosServiceContainer;
import com.sysnet.poc.vo.StgRunLog;

/**
 * 提供投保单业务的服务 采集数据,入库操作
 * 
 * @author Ma_Ke
 * @since 2009-07-08
 * 
 */
public class ProposalBusiness {
	private ShowInfo si;

	public ProposalBusiness(ShowInfo si) {
		super();
		this.si = si;
	}

	private static final Log log = OdsLogger.getLog("insureLog4j", "Insure");
	private boolean write = true;
	// private Connection pharosConn = null;
	private Connection conn = null;
	private Connection pharosConnBack = null;

	@SuppressWarnings("unchecked")
	public void excuteByMapping(String proposalNo, String version, String main_version, int bat_Id, String object_Id, String object_no, String object_Type, String node_No, String PRODUCT_CD, String PRODUCT_NODE_CD, String incrProposalNo) {
		UserTransaction ut = null;
		try {
			ut = (UserTransaction) UserTransactionFactory.getUserTransaction();
			ut.begin();

			PharosServiceContainer service = PharosServiceContainer.Instance();

			BContractBO bContractBo = service.getBContractBO();
			WContractBO wContractBO = service.getWContractBO();
			if (Runtime.getRuntime().freeMemory() < 100 * 1024 * 1024) {
				System.gc();
			}
			List<Long> noneRiskNodeList = new ArrayList<Long>();
			WContractNodeBaseInfos wcnbis = wContractBO.getNodeIdsForProposal(proposalNo);
			List<WContractNodeBaseInfo> wcnbiL = wcnbis.getBaseInfoList();
			for (WContractNodeBaseInfo wContractNodeBaseInfo : wcnbiL) {
				if (wContractNodeBaseInfo.getNodeNo().equals("1-0")) {// 如果节点号是0那么跳过不抽
					continue;
				}
				if (wContractNodeBaseInfo.getPolicyLevel() == 0) {
					noneRiskNodeList.add(wContractNodeBaseInfo.getId());
				}
			}
			List<ProposalNonRiskNodeThead> nonthreadsPool = new ArrayList<ProposalNonRiskNodeThead>();
			StopBean nonSbean = new StopBean();
			this.si.getProcessL().clear();
			ProposalNonRiskNodeThead rt = new ProposalNonRiskNodeThead(si.getProcessL());
			rt.setDealMode(si.getDealMode());
			rt.setStopBean(nonSbean);
			rt.setBContractBo(bContractBo);
			rt.setWContractBO(wContractBO);
			rt.setPRODUCT_CD(PRODUCT_CD);
			rt.setPRODUCT_NODE_CD(PRODUCT_NODE_CD);
			rt.setNonRiskNodeIDlist(noneRiskNodeList);
			rt.setBat_Id(String.valueOf(bat_Id));
			rt.setObject_Id(object_Id);
			rt.setObject_Type(object_Type);
			rt.setWrite(write);
			rt.setObject_no(object_no);
			rt.run();
			nonthreadsPool.add(rt);
			conn = ConnDB.getODSConn();
			ThreadCatchDB tcdb = new ThreadCatchDB();
			tcdb.setEl(rt.getResult());
			StorageTransAction storage = new StorageTransAction();
			storage.setConnection(conn);
			storage.setTcdb(tcdb);
			storage.run();
			if (!tcdb.getIsAllRight()) {
				throw new Exception(tcdb.getErrorMessage());
			}
			if (nonSbean.getAllWriteFlag()) {
				ut.commit();
			} else {
				ut.rollback();
			}
			pharosConnBack = ConnDB.getPharosConn();
			PlCIncrementDAO plCIncrementDAO = DAOFactory.getPlcIncrecentDAO();
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
			log.error("", e);

			// 记录日志到 stg_run_log 表中
			StgRunLog stgLog = new StgRunLog();
			stgLog.setErrorCode("3");
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
				if (conn != null) {
					conn.close();
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
