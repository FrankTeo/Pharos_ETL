package com.sysnet.poc.service.business;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import org.apache.commons.logging.Log;

import com.sysnet.poc.control.config.Constants;
import com.sysnet.poc.control.manage.vo.ThreadCatchDB;
import com.sysnet.poc.partymapping.PartyMappingBuilder;
import com.sysnet.poc.partymapping.vo.Table;
import com.sysnet.poc.service.dao.PartyIncrementDAO;
import com.sysnet.poc.service.dao.RunLogDAO;
import com.sysnet.poc.service.dao.daoFactory.DAOFactory;
import com.sysnet.poc.service.dao.dbc.ConnDB;
import com.sysnet.poc.service.storage.SColumn;
import com.sysnet.poc.service.storage.StorageTransAction;
import com.sysnet.poc.transaction.UserTransactionFactory;
import com.sysnet.poc.util.OdsLogger;
import com.sysnet.poc.util.RemoveDataUtil;
import com.sysnet.poc.vo.StgRunLog;

/**
 * 提供客户业务的服务 采集数据,入库操作
 * 
 * @author lu_haibin
 * 
 */
public class PartyBusiness {

	private static final Log log = OdsLogger.getLog("partyLog4j", "Party");
	Connection pharosConn = null;

	public void excuteByMapping(String object_No, String object_Id, String object_Type, int bat_Id, String deal_flag, String time_stamp, String history_no) throws Exception {

		UserTransaction ut = null;
		Connection conn = null;

		try {
			// 开启分布式事务
			PartyMappingBuilder pbm = new PartyMappingBuilder();
			ThreadCatchDB tcdb = new ThreadCatchDB();
			tcdb.setEl(pbm.execute(object_No));
			StorageTransAction storageparty = new StorageTransAction();
			storageparty.setTcdb(tcdb);
			ut = (UserTransaction) UserTransactionFactory.getUserTransaction();
			ut.begin();
			conn = ConnDB.getODSConn();
			List<SColumn> sl = new ArrayList<SColumn>();
			List<Table> tl = pbm.getMapDesc().getTable();
			for (Table table : tl) {
				sl.add((SColumn) table);
			}
			RemoveDataUtil.delete(sl, object_No, conn);
			storageparty.setConnection(conn);
			storageparty.run();
			//System.out.println("start-----"+ System.currentTimeMillis());
			storageparty.extendSQL("call UPDATEODS.updateParty(?)", new String[] { object_No });
			//System.out.println("end-------"+ System.currentTimeMillis());
			if (!tcdb.getIsAllRight()) {
				throw new Exception(tcdb.getErrorMessage());
			}
			ut.commit();
			pharosConn = ConnDB.getPharosConn();
			PartyIncrementDAO partyIncrementDAO = DAOFactory.getPartyIncrecentDAO();
			partyIncrementDAO.updateStatus(pharosConn, Long.valueOf(object_Id), Constants.INCREMENT_WRITE_BACK_STATUS_1);
		} catch (Exception e) {

			try {
				// conn.rollback();
				if (ut != null) {
					ut.rollback();
				}
				// 回写Pharos增量表
				if (pharosConn == null) {
					pharosConn = ConnDB.getPharosConn();
				}
				PartyIncrementDAO partyIncrementDAO = DAOFactory.getPartyIncrecentDAO();
				partyIncrementDAO.updateStatus(pharosConn, Long.valueOf(object_Id), Constants.INCREMENT_WRITE_BACK_STATUS_2);// 如果出现异常，那么就直接回写增量表为2
			} catch (Exception e2) {

				e2.printStackTrace();
			}
			log.info("抛出异常:", e);
			e.printStackTrace();// 为了打印异常，实际工作时只要return即可

			// 记录日志到 stg_run_log 表中
			StgRunLog stgLog = new StgRunLog();
			stgLog.setErrorCode("6");
			stgLog.setErrorMessage(e.toString());
			stgLog.setObjectId(object_Id);
			stgLog.setObjectNo(object_No);
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
				if (pharosConn != null) {
					pharosConn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
