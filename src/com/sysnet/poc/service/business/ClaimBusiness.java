package com.sysnet.poc.service.business;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import org.apache.commons.logging.Log;

import com.sysnet.poc.claimmapping.ClaimMappingBuilder;
import com.sysnet.poc.claimmapping.vo.ClaimInfo;
import com.sysnet.poc.claimmapping.vo.Table;
import com.sysnet.poc.control.config.Constants;
import com.sysnet.poc.control.manage.vo.EtlPharosIncrClaim;
import com.sysnet.poc.control.manage.vo.ThreadCatchDB;
import com.sysnet.poc.service.dao.ClaimDAO;
import com.sysnet.poc.service.dao.RunLogDAO;
import com.sysnet.poc.service.dao.daoFactory.DAOFactory;
import com.sysnet.poc.service.dao.dbc.ConnDB;
import com.sysnet.poc.service.storage.SColumn;
import com.sysnet.poc.service.storage.StorageTransAction;
import com.sysnet.poc.transaction.UserTransactionFactory;
import com.sysnet.poc.util.OdsLogger;
import com.sysnet.poc.util.RemoveDataUtil;
import com.sysnet.poc.vo.StgRunLog;

public class ClaimBusiness {
	private static final Log log = OdsLogger.getLog("claimLog4j", "Claim");
	private final ClaimMappingBuilder cmb = new ClaimMappingBuilder();

	public boolean excute(EtlPharosIncrClaim epic) {

		Connection odsConn = null;
		Connection pharosConn = null;
		UserTransaction ut = null;
		ClaimDAO claimdao = DAOFactory.getClaimDAO();// 获取理赔DAO
		try {
			// 录入登场区

			ut = UserTransactionFactory.getUserTransaction();// 打开事务
			ut.begin();// 开启事务
			ClaimInfo ci = cmb.Collection(epic);// 封装一层有可能修改

			odsConn = ConnDB.getODSConn();// 获取osd连接
			claimdao.setOdsConn(odsConn);// 设置DAO的ODS连接
			List<Table> tl = ci.getMd().getTable();
			List<SColumn> sl = new ArrayList<SColumn>();
			for (Table table : tl) {
				sl.add(table);
			}
			//删除老数据
			RemoveDataUtil.delete(sl, ci.getReportNo(), odsConn);
			StorageTransAction storage = new StorageTransAction();
			storage.setConnection(odsConn);
			ThreadCatchDB tcdb = new ThreadCatchDB();

			tcdb.setEl(ci.getSl());

			storage.setTcdb(tcdb);
			storage.run();
			storage.extendSQL("call UPDATEODS.updateClaim(?)", new String[] { ci.getReportNo() });

			storage = null;
			if (!tcdb.getIsAllRight()) {
				throw new Exception(tcdb.getErrorMessage());
			}
			// 回写增量表
			epic.setStageWriteFlag(Constants.INCREMENT_WRITE_BACK_STATUS_1);
			pharosConn = ConnDB.getPharosConn();// 获取pharos连接
			claimdao.setPharosConn(pharosConn);// 设置DAO的pharos连接
			claimdao.updateIncr(epic);
			ut.commit();// 提交事务

			return true;
		} catch (Exception e) {
			log.error("ClaimBusiness:", e);
			e.printStackTrace();
			if (ut != null) {
				try {
					ut.rollback();
				} catch (IllegalStateException e1) {
					e1.printStackTrace();
				} catch (SecurityException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			// 回写增量表
			try {
				if (pharosConn == null || pharosConn.isClosed()) {
					pharosConn = ConnDB.getPharosConn();
				}
				// ClaimDAO claimdao = DAOFactory.getClaimDAO();// 获取理赔DAO
				claimdao.setPharosConn(pharosConn);// 设置DAO的pharos连接
				epic.setStageWriteFlag(Constants.INCREMENT_WRITE_BACK_STATUS_2);
				claimdao.updateIncr(epic);
			} catch (Exception e1) {
				log.error("回写增量表出错:", e);
				e1.printStackTrace();
				// 获取pharos连接
			}

			// 往登场区数据库的日志表中记录异常信息
			StgRunLog stgLog = new StgRunLog();
			stgLog.setErrorCode("12");
			stgLog.setErrorMessage(e.toString());
			stgLog.setObjectId(String.valueOf(epic.getObjectId()));
			stgLog.setObjectNo(String.valueOf(epic.getObjectNo()));
			RunLogDAO rld = new RunLogDAO();
			try {
				rld.insertLog(stgLog);
			} catch (SQLException e2) {
				log.error("log error:", e2);
			} catch (NamingException e2) {
				log.error("log error:", e2);
			}
			return false;
		} finally {
			try {
				if (odsConn != null) {
					odsConn.close();
				}
				if (pharosConn != null) {
					pharosConn.close();
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

	}
}
