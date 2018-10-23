package com.sysnet.poc.service.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import javax.naming.NamingException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.commons.logging.Log;

import com.sysnet.poc.control.config.Constants;
import com.sysnet.poc.control.manage.vo.EtlPharosIncrAcc;
import com.sysnet.poc.service.dao.AccDAO;
import com.sysnet.poc.service.dao.AccSchedulDAO;
import com.sysnet.poc.service.dao.RunLogDAO;
import com.sysnet.poc.service.dao.daoFactory.DAOFactory;
import com.sysnet.poc.service.dao.dbc.ConnDB;
import com.sysnet.poc.transaction.UserTransactionFactory;
import com.sysnet.poc.util.OdsLogger;
import com.sysnet.poc.vo.StgRunLog;

/**
 * 提供收付费业务的服务 采集数据,入库操作
 * 
 * @author shao_hw
 * @since 2009-09-15
 * 
 */
public class AccBusiness {

	// private Logger log = Logger.getLogger(AccBusiness.class);
	private static final Log log = OdsLogger.getLog("accLog4j", "ACC");

	private AccSchedulDAO accSchedulDAO = new AccSchedulDAO();

	/**
	 * 执行数据抽取入库
	 * 
	 * @param ec
	 *            批次信息
	 * @param servesType
	 *            增量类型
	 */
	public void excute(EtlPharosIncrAcc ec) {

		UserTransaction ut = null;
		Connection conn = null;
		Connection pharosConn = null;

		try {

			// 开启分布式事务
			ut = UserTransactionFactory.getUserTransaction();
			ut.begin();
			conn = ConnDB.getODSConn();
			pharosConn = ConnDB.getPharosConn();
			accSchedulDAO.setOdsConn(conn);
			accSchedulDAO.setPharosConn(pharosConn);
			String sqlp = "";
			if (ec.getIsClaim()) {
				sqlp = "Select t.claimno,t.claim_num,t.Account_source,t.Seq_no,t.Account_date,t.Currency,t.Original_amount,t.Applied_Amount From t_acctrans t where t.transaction_no=?";
			} else {
				sqlp = "Select t.Policy_no,t.Edr_no,t.Account_source,t.Seq_no,t.Start_date,t.End_date,t.Account_date,t.Currency,Original_amount,t.Applied_Amount From t_acctrans t where t.transaction_no=?";
			}
			PreparedStatement psp = pharosConn.prepareStatement(sqlp);
			psp.setString(1, ec.getObjectNo());
			ResultSet rsp = psp.executeQuery();
			rsp.next();
			String sqlo = "";
			PreparedStatement pso = null;
			if (ec.getIsClaim()) {
				sqlo = "Insert Into t_ClaimPayTrans t (Reportno, Claimtimes, Transsource, Transserial, Transdate, Currency, Amount, Objectno, Crttime, ApplyAmount)Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				pso = conn.prepareStatement(sqlo);
				pso.setString(1, rsp.getString("claimno"));
				pso.setInt(2, rsp.getInt("claim_num"));
				pso.setString(3, rsp.getString("Account_source"));
				pso.setInt(4, rsp.getInt("Seq_no"));
				pso.setTimestamp(5, rsp.getTimestamp("Account_date"));
				pso.setString(6, rsp.getString("Currency"));
				pso.setDouble(7, rsp.getDouble("Original_amount"));
				pso.setString(8, ec.getObjectNo());
				pso.setTimestamp(9, new Timestamp(new Date().getTime()));
				pso.setDouble(10, rsp.getDouble("Applied_Amount"));
				pso.execute();
			} else {
				sqlo = "Insert Into t_PolicyRecTrans t (Policyno, Endorseno, Transsource, Transserial, Startdate, Enddate, Transdate, Currency, Amount, Objectno, Crttime, ApplyAmount)Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				pso = conn.prepareStatement(sqlo);
				pso.setString(1, rsp.getString("Policy_no"));
				pso.setString(2, rsp.getString("Edr_no"));
				pso.setString(3, rsp.getString("Account_source"));
				pso.setInt(4, rsp.getInt("Seq_no"));
				pso.setTimestamp(5, rsp.getTimestamp("Start_date"));
				pso.setTimestamp(6, rsp.getTimestamp("End_date"));
				pso.setTimestamp(7, rsp.getTimestamp("Account_date"));
				pso.setString(8, rsp.getString("Currency"));
				pso.setDouble(9, rsp.getDouble("Original_amount"));
				pso.setString(10, ec.getObjectNo());
				pso.setTimestamp(11, new Timestamp(new Date().getTime()));
				pso.setDouble(12, rsp.getDouble("Applied_Amount"));
				pso.execute();
			}
			accSchedulDAO.updateAccStatus(ec.getObjectId());

			ut.commit();
		} catch (Exception e) {

			log.info("当前异常出现在：" + ec.getObjectNo());
			e.printStackTrace();
			try {
				if (ut != null) {
					ut.rollback();
					log.info("回滚了！----------------------------" + e.getMessage());
				}

			} catch (IllegalStateException e1) {

				e1.printStackTrace();
			} catch (SecurityException e1) {

				e1.printStackTrace();
			} catch (SystemException e1) {

				e1.printStackTrace();
			}

			// 回写增量表
			try {
				AccDAO accdao = DAOFactory.getAccDAO();// 获取理赔DAO
				accdao.setPharosConn(pharosConn);// 设置DAO的pharos连接
				ec.setStageWriteFlag(Constants.INCREMENT_WRITE_BACK_STATUS_2);
				accdao.updateIncr(ec);
			} catch (Exception e1) {
				log.error("回写增量表出错:", e);
				e1.printStackTrace();
			}// 获取pharos连接

			// 记录日志到 stg_run_log 表中
			StgRunLog stgLog = new StgRunLog();
			stgLog.setErrorCode("13");
			stgLog.setErrorMessage(e.toString());
			stgLog.setObjectId(ec.getObjectId().toString());
			stgLog.setObjectNo(ec.getObjectNo());
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