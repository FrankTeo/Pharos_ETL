package com.sysnet.poc.service.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.sysnet.poc.control.config.Constants;
import com.sysnet.poc.control.manage.vo.EtlPharosIncrAcc;
import com.sysnet.poc.service.dao.AccDAO;
import com.sysnet.poc.util.ResourceBundleUtil;

/**
 * 业务交易DAO 实现类
 * 
 * @author Administrator
 * 
 */
public class AccDAOimpl implements AccDAO {

	private PreparedStatement pstmt = null;

	private Connection pharosConn = null;

	public void setPharosConn(Connection conn) throws SQLException {
		this.pharosConn = conn;
	}

//	public void insertStgAccAmontloi(Connection conn, StgAccAmontloi vo) throws SQLException, Exception {
//		if (vo == null) {
//			throw new Exception("Class of AccDAOimpl mothed insertStgAccAmontloi param StgAccAmontloi is null!");
//		}
//		StringBuilder sqlSB = new StringBuilder();
//		sqlSB.append("insert into STG_ACC_AMONTLOI(");
//		sqlSB.append(" AMONTLOI_ID, ");
//		sqlSB.append(" LOI, ");
//		sqlSB.append(" AMOUNT_TYPE, ");
//		sqlSB.append(" AMOUNT, ");
//		sqlSB.append(" REMOVED, ");
//		sqlSB.append(" TRANS_ID, ");
//		sqlSB.append(" BAT_ID, ");
//		sqlSB.append(" OBJECT_ID, ");
//		sqlSB.append(" OBJECT_TYPE, ");
//		sqlSB.append(" TIME_STAMP, ");
//		sqlSB.append(" INSURE_COMPANY_ID, ");
//		sqlSB.append(" INSURE_COMPANY_CODE ");
//		sqlSB.append(") values (");
//		sqlSB.append("?,?,?,?,?,?,?,?,?,?,?,?)");
//		pstmt = conn.prepareStatement(sqlSB.toString());
//		int i = 1;
//		pstmt.setString(i++, vo.getAmontloiId());
//		pstmt.setLong(i++, vo.getLoi());
//		pstmt.setLong(i++, vo.getAmountType());
//		pstmt.setDouble(i++, vo.getAmount());
//		pstmt.setLong(i++, vo.getRemoved());
//		pstmt.setLong(i++, vo.getTransId());
//		pstmt.setLong(i++, vo.getBatId());
//		pstmt.setLong(i++, vo.getObjectId());
//		pstmt.setString(i++, vo.getObjectType());
//		pstmt.setDate(i++, vo.getTimeStamp());
//		pstmt.setLong(i++, Long.parseLong(ResourceBundleUtil.getCompanyId()));
//		pstmt.setString(i++, ResourceBundleUtil.getCompanyCd());
//		pstmt.execute();
//		pstmt.close();
//
//	}

//	public void insertStgAccApplication(Connection conn, StgAccApplication vo) throws SQLException, Exception {
//		if (vo == null) {
//			throw new Exception("Class of AccDAOimpl mothed insertStgAccApplication param StgAccApplication is null!");
//		}
//		StringBuilder sqlSB = new StringBuilder();
//		sqlSB.append("insert into STG_ACC_APPLICATION(");
//		sqlSB.append(" APP_ID, ");
//		sqlSB.append(" AMOUNT, ");
//		sqlSB.append(" APPLIED_DATE, ");
//		sqlSB.append(" REMOVED, ");
//		sqlSB.append(" STATUS, ");
//		sqlSB.append(" REVERSED_ID, ");
//		sqlSB.append(" APPLIED_ID, ");
//		sqlSB.append(" CREATE_USER_ID, ");
//		sqlSB.append(" CREATE_DATE_TIME, ");
//		sqlSB.append(" CHANGE_USER_ID, ");
//		sqlSB.append(" CHANGE_DATE_TIME, ");
//		sqlSB.append(" BUSINESS_COMPANY_ID, ");
//		sqlSB.append(" BUSINESS_COMPANY_NODE_ID, ");
//		sqlSB.append(" USER_COMPANY_ID, ");
//		sqlSB.append(" USER_COMPANY_NODE_ID, ");
//		sqlSB.append(" BASE_TRANS_ID, ");
//		sqlSB.append(" APPLIED_TRANS_ID, ");
//		sqlSB.append(" TIME_STAMP, ");
//		sqlSB.append(" BAT_ID, ");
//		sqlSB.append(" OBJECT_ID, ");
//		sqlSB.append(" OBJECT_TYPE,");
//		sqlSB.append(" INSURE_COMPANY_ID, ");
//		sqlSB.append(" INSURE_COMPANY_CODE ");
//		sqlSB.append(") values (");
//		sqlSB.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//		pstmt = conn.prepareStatement(sqlSB.toString());
//		int i = 1;
//		pstmt.setLong(i++, vo.getAppId());
//		pstmt.setDouble(i++, vo.getAmount());
//		pstmt.setDate(i++, vo.getAppliedDate());
//		pstmt.setLong(i++, vo.getRemoved());
//		pstmt.setLong(i++, vo.getStatus());
//		pstmt.setLong(i++, vo.getReversedId());
//		pstmt.setLong(i++, vo.getAppliedId());
//		pstmt.setLong(i++, vo.getCreateUserId());
//		pstmt.setDate(i++, vo.getCreateDateTime());
//		pstmt.setLong(i++, vo.getChangeUserId());
//		pstmt.setDate(i++, vo.getChangeDateTime());
//		pstmt.setString(i++, vo.getBusinessCompanyCode());
//		pstmt.setString(i++, vo.getBusinessCompanyNodeCode());
//		pstmt.setString(i++, vo.getUserCompanyCode());
//		pstmt.setString(i++, vo.getUserCompanyNodeCode());
//		pstmt.setLong(i++, vo.getBaseTransId());
//		pstmt.setLong(i++, vo.getAppliedTransId());
//		pstmt.setDate(i++, vo.getTimeStamp());
//		pstmt.setLong(i++, vo.getBatId());
//		pstmt.setLong(i++, vo.getObjectId());
//		pstmt.setString(i++, vo.getObjectType());
//		pstmt.setLong(i++, Long.parseLong(ResourceBundleUtil.getCompanyId()));
//		pstmt.setString(i++, ResourceBundleUtil.getCompanyCd());
//		pstmt.execute();
//		pstmt.close();
//
//	}
//
//	public void insertStgAccApplicationdetail(Connection conn, StgAccApplicationdetail vo) throws SQLException, Exception {
//		if (vo == null) {
//			throw new Exception("Class of AccDAOimpl mothed insertStgAccApplicationdetail param StgAccApplicationdetail is null!");
//		}
//		StringBuilder sqlSB = new StringBuilder();
//		sqlSB.append("insert into STG_ACC_APPLICATIONDETAIL(");
//		sqlSB.append(" APP_DETAIL_ID, ");
//		sqlSB.append(" APPLIED_AMOUNT, ");
//		sqlSB.append(" AMOUNT_TYPE, ");
//		sqlSB.append(" REMOVED, ");
//		sqlSB.append(" APP_ID, ");
//		sqlSB.append(" TRANS_DETAIL_ID, ");
//		sqlSB.append(" INSTALL_DETAIL_ID, ");
//		sqlSB.append(" BAT_ID, ");
//		sqlSB.append(" OBJECT_ID, ");
//		sqlSB.append(" OBJECT_TYPE, ");
//		sqlSB.append(" TIME_STAMP, ");
//		sqlSB.append("INSURE_COMPANY_ID, ");
//		sqlSB.append("INSURE_COMPANY_CODE ");
//		sqlSB.append(") values (");
//		sqlSB.append("?,?,?,?,?,?,?,?,?,?,?,?,?)");
//		pstmt = conn.prepareStatement(sqlSB.toString());
//		int i = 1;
//		pstmt.setLong(i++, vo.getAppDetailId());
//		pstmt.setDouble(i++, vo.getAppliedAmount());
//		pstmt.setLong(i++, vo.getAmountType());
//		pstmt.setLong(i++, vo.getRemoved());
//		pstmt.setLong(i++, vo.getAppId());
//		pstmt.setLong(i++, vo.getTransDetailId());
//		pstmt.setString(i++, vo.getInstallDetailId());
//		pstmt.setLong(i++, vo.getBatId());
//		pstmt.setLong(i++, vo.getObjectId());
//		pstmt.setString(i++, vo.getObjectType());
//		pstmt.setDate(i++, vo.getTimeStamp());
//		pstmt.setLong(i++, Long.parseLong(ResourceBundleUtil.getCompanyId()));
//		pstmt.setString(i++, ResourceBundleUtil.getCompanyCd());
//		pstmt.execute();
//		pstmt.close();
//
//	}
//
//	public void insertStgAccDyinstallment(Connection conn, StgAccDyinstallment vo) throws SQLException, Exception {
//		if (vo == null) {
//			throw new Exception("Class of AccDAOimpl mothed insertStgAccDyinstallment param StgAccDyinstallment is null!");
//		}
//		StringBuilder sqlSB = new StringBuilder();
//		sqlSB.append("insert into STG_ACC_DYINSTALLMENT(");
//		sqlSB.append(" DYINSTALLMENT_ID, ");
//		sqlSB.append(" DAYS, ");
//		sqlSB.append(" AMOUNT, ");
//		sqlSB.append(" DAILY_AMOUNT, ");
//		sqlSB.append(" DAILY_CONTRACT, ");
//		sqlSB.append(" PARTIAL_AMOUNT, ");
//		sqlSB.append(" TOTAL_AMOUNT, ");
//		sqlSB.append(" REMOVED, ");
//		sqlSB.append(" TRANS_ID, ");
//		sqlSB.append(" OBJECT_ID, ");
//		sqlSB.append(" OBJECT_TYPE, ");
//		sqlSB.append(" TIME_STAMP, ");
//		sqlSB.append(" BAT_ID, ");
//		sqlSB.append("INSURE_COMPANY_ID, ");
//		sqlSB.append("INSURE_COMPANY_CODE ");
//		sqlSB.append(") values (");
//		sqlSB.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//		pstmt = conn.prepareStatement(sqlSB.toString());
//		int i = 1;
//		pstmt.setLong(i++, vo.getDyinstallmentId());
//		pstmt.setLong(i++, vo.getDays());
//		pstmt.setDouble(i++, vo.getAmount());
//		pstmt.setDouble(i++, vo.getDailyAmount());
//		pstmt.setDouble(i++, vo.getDailyContract());
//		pstmt.setDouble(i++, vo.getPartialAmount());
//		pstmt.setDouble(i++, vo.getTotalAmount());
//		pstmt.setLong(i++, vo.getRemoved());
//		pstmt.setLong(i++, vo.getTransId());
//		pstmt.setLong(i++, vo.getObjectId());
//		pstmt.setString(i++, vo.getObjectType());
//		pstmt.setDate(i++, vo.getTimeStamp());
//		pstmt.setLong(i++, vo.getBatId());
//		pstmt.setLong(i++, Long.parseLong(ResourceBundleUtil.getCompanyId()));
//		pstmt.setString(i++, ResourceBundleUtil.getCompanyCd());
//		pstmt.execute();
//		pstmt.close();
//
//	}
//
//	public void insertStgAccGeneralledger(Connection conn, StgAccGeneralledger vo) throws SQLException, Exception {
//		if (vo == null) {
//			throw new Exception("Class of AccDAOimpl mothed insertStgAccDyinstallment param StgAccGeneralledger is null!");
//		}
//		StringBuilder sqlSB = new StringBuilder();
//		sqlSB.append("insert into STG_ACC_GENERALLEDGER(");
//		sqlSB.append(" ID, ");
//		sqlSB.append(" BUSINESS_COMPANY_ID, ");
//		sqlSB.append(" BUSINESS_COMPANY_NODE_ID, ");
//		sqlSB.append(" GLNAME, ");
//		sqlSB.append(" GLCODE, ");
//		sqlSB.append(" VOUCHER, ");
//		sqlSB.append(" DEBIT, ");
//		sqlSB.append(" CREDIT, ");
//		sqlSB.append(" IS_APPLIEDGL, ");
//		sqlSB.append(" THIRD_PARTY, ");
//		sqlSB.append(" ISSUE_DATE, ");
//		sqlSB.append(" APPLIED_DATE, ");
//		sqlSB.append(" GENERATE_DATE, ");
//		sqlSB.append(" ORIGINAL_CURRENCY, ");
//		sqlSB.append(" ORIGINAL_AMOUNT, ");
//		sqlSB.append(" BATCH_NO, ");
//		sqlSB.append(" REMOVED, ");
//		sqlSB.append(" ACCOUNT_SOURCE, ");
//		sqlSB.append(" DOCUMENT_TYPE, ");
//		sqlSB.append(" TRANSCATION_NO, ");
//		sqlSB.append(" EXCHANGE_RATE, ");
//		sqlSB.append(" DEBIT_LOCAL, ");
//		sqlSB.append(" CREDIT_LOCAL, ");
//		sqlSB.append(" COSTCENTER_MAIN, ");
//		sqlSB.append(" COSTCENTER_SUB, ");
//		sqlSB.append(" USER_COMPANY_ID, ");
//		sqlSB.append(" USER_COMPANY_NODE_ID, ");
//		sqlSB.append(" BUSINESS_COMPANY_CODE, ");
//		sqlSB.append(" BUSINESS_COMPANY_NODE_CODE, ");
//		sqlSB.append(" USER_COMPANY_CODE, ");
//		sqlSB.append(" USER_COMPANY_NODE_CODE, ");
//		sqlSB.append(" TRANS_DETAIL_ID, ");
//		sqlSB.append(" APP_DETAIL_ID, ");
//		sqlSB.append(" OBJECT_ID, ");
//		sqlSB.append(" OBJECT_TYPE, ");
//		sqlSB.append(" TIME_STAMP, ");
//		sqlSB.append(" BAT_ID, ");
//		sqlSB.append("INSURE_COMPANY_ID, ");
//		sqlSB.append("INSURE_COMPANY_CODE ");
//		sqlSB.append(") values (");
//		sqlSB.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//		pstmt = conn.prepareStatement(sqlSB.toString());
//		int i = 1;
//		pstmt.setLong(i++, vo.getId());
//
//		pstmt.setLong(i++, vo.getBusinessCompanyId());
//		pstmt.setLong(i++, vo.getBusinessCompanyNodeId());
//		pstmt.setString(i++, vo.getGlname());
//		pstmt.setString(i++, vo.getGlcode());
//		pstmt.setLong(i++, vo.getVoucher());
//		pstmt.setDouble(i++, vo.getDebit());
//		pstmt.setDouble(i++, vo.getCredit());
//		pstmt.setLong(i++, vo.getIsAppliedgl());
//		pstmt.setString(i++, vo.getThirdParty());
//		pstmt.setDate(i++, vo.getIssueDate());
//		pstmt.setDate(i++, vo.getAppliedDate());
//		pstmt.setDate(i++, vo.getGenerateDate());
//		pstmt.setLong(i++, vo.getOriginalCurrency());
//		pstmt.setDouble(i++, vo.getOriginalAmount());
//		pstmt.setLong(i++, vo.getBatchNo());
//		pstmt.setLong(i++, vo.getRemoved());
//		pstmt.setLong(i++, vo.getAccountSource());
//		pstmt.setLong(i++, vo.getDocumentType());
//		pstmt.setString(i++, vo.getTransNo());
//		pstmt.setDouble(i++, vo.getExchangeRate());
//		pstmt.setDouble(i++, vo.getDebitLocal());
//		pstmt.setDouble(i++, vo.getCreditLocal());
//		pstmt.setLong(i++, vo.getCostcenterMain());
//		pstmt.setLong(i++, vo.getCostcenterSub());
//		pstmt.setLong(i++, vo.getUserCompanyId());
//		pstmt.setLong(i++, vo.getUserCompanyNodeId());
//		pstmt.setString(i++, vo.getBusinessCompanyCode());
//		pstmt.setString(i++, vo.getBusinessCompanyNodeCode());
//		pstmt.setString(i++, vo.getUserCompanyCode());
//		pstmt.setString(i++, vo.getUserCompanyNodeCode());
//		pstmt.setLong(i++, vo.getTransDetailId());
//		pstmt.setLong(i++, vo.getAppDetailId());
//		pstmt.setLong(i++, vo.getObjectId());
//		pstmt.setString(i++, vo.getObjectType());
//		pstmt.setDate(i++, vo.getTimeStamp());
//		pstmt.setLong(i++, vo.getBatId());
//		pstmt.setLong(i++, Long.parseLong(ResourceBundleUtil.getCompanyId()));
//		pstmt.setString(i++, ResourceBundleUtil.getCompanyCd());
//		pstmt.execute();
//		pstmt.close();
//
//	}
//
//	public void insertStgAccGeneralledgerhistory(Connection conn, StgAccGeneralledgerhistory vo) throws SQLException, Exception {
//		if (vo == null) {
//			throw new Exception("Class of AccDAOimpl mothed insertStgAccGeneralledgerhistory param StgAccGeneralledgerhistory is null!");
//		}
//		StringBuilder sqlSB = new StringBuilder();
//		sqlSB.append("insert into STG_ACC_GENERALLEDGERHISTORY(");
//		sqlSB.append(" GENERAL_LEDGER_HISTORY_ID, ");
//		sqlSB.append(" REMOVED, ");
//		sqlSB.append(" LOI, ");
//		sqlSB.append(" TRANS_DETAIL_ID, ");
//		sqlSB.append(" APP_DETAIL_ID, ");
//		sqlSB.append(" BAT_ID, ");
//		sqlSB.append(" OBJECT_ID, ");
//		sqlSB.append(" OBJECT_TYPE, ");
//		sqlSB.append(" TIME_STAMP, ");
//		sqlSB.append(" INSURE_COMPANY_ID, ");
//		sqlSB.append(" INSURE_COMPANY_CODE ");
//		sqlSB.append(") values (");
//		sqlSB.append("?,?,?,?,?,?,?,?,?,?,?)");
//		pstmt = conn.prepareStatement(sqlSB.toString());
//		int i = 1;
//		pstmt.setString(i++, vo.getGeneralLedgerHistoryId());
//		pstmt.setLong(i++, vo.getRemoved());
//		pstmt.setLong(i++, vo.getLoi());
//		pstmt.setLong(i++, vo.getTransDetailId());
//		pstmt.setLong(i++, vo.getAppDetailId());
//		pstmt.setLong(i++, vo.getBatId());
//		pstmt.setLong(i++, vo.getObjectId());
//		pstmt.setString(i++, vo.getObjectType());
//		pstmt.setDate(i++, vo.getTimeStamp());
//		pstmt.setLong(i++, Long.parseLong(ResourceBundleUtil.getCompanyId()));
//		pstmt.setString(i++, ResourceBundleUtil.getCompanyCd());
//		pstmt.execute();
//		pstmt.close();
//
//	}
//
//	public void insertStgAccInstall(Connection conn, StgAccInstall vo) throws SQLException, Exception {
//		if (vo == null) {
//			throw new Exception("Class of AccDAOimpl mothed insertStgAccInstall param StgAccInstall is null!");
//		}
//		StringBuilder sqlSB = new StringBuilder();
//		sqlSB.append("insert into STG_ACC_INSTALL(");
//		sqlSB.append(" INSTALL_ID, ");
//		sqlSB.append(" DUE_DATE, ");
//		sqlSB.append(" PAID_DATE, ");
//		sqlSB.append(" DUE_AMOUNT, ");
//		sqlSB.append(" APPLIED_AMOUNT, ");
//		sqlSB.append(" REMOVED, ");
//		sqlSB.append(" PARTY_CODE, ");
//		sqlSB.append(" SEQ_NO, ");
//		sqlSB.append(" TRANS_ID, ");
//		sqlSB.append(" OBJECT_ID, ");
//		sqlSB.append(" OBJECT_TYPE, ");
//		sqlSB.append(" TIME_STAMP, ");
//		sqlSB.append(" BAT_ID, ");
//		sqlSB.append(" INSURE_COMPANY_ID, ");
//		sqlSB.append(" INSURE_COMPANY_CODE ");
//		sqlSB.append(") values (");
//		sqlSB.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//		pstmt = conn.prepareStatement(sqlSB.toString());
//		int i = 1;
//		pstmt.setLong(i++, vo.getInstallId());
//		pstmt.setDate(i++, vo.getDueDate());
//		pstmt.setDate(i++, vo.getPaidDate());
//		pstmt.setDouble(i++, vo.getDueAmount());
//		pstmt.setDouble(i++, vo.getAppliedAmount());
//		pstmt.setLong(i++, vo.getRemoved());
//		pstmt.setString(i++, vo.getPartyCode());
//		pstmt.setLong(i++, vo.getSeqNo());
//		pstmt.setLong(i++, vo.getTransId());
//		pstmt.setLong(i++, vo.getObjectId());
//		pstmt.setString(i++, vo.getObjectType());
//		pstmt.setDate(i++, vo.getTimeStamp());
//		pstmt.setLong(i++, vo.getBatId());
//		pstmt.setLong(i++, Long.parseLong(ResourceBundleUtil.getCompanyId()));
//		pstmt.setString(i++, ResourceBundleUtil.getCompanyCd());
//		pstmt.execute();
//		pstmt.close();
//
//	}
//
//	public void insertStgAccInstalldetail(Connection conn, StgAccInstalldetail vo) throws SQLException, Exception {
//		if (vo == null) {
//			throw new Exception("Class of AccDAOimpl mothed insertStgAccInstalldetail param StgAccInstalldetail is null!");
//		}
//		StringBuilder sqlSB = new StringBuilder();
//		sqlSB.append("insert into STG_ACC_INSTALLDETAIL(");
//		sqlSB.append(" INSTALL_DETAIL_ID, ");
//		sqlSB.append(" ORIGINAL_AMOUNT, ");
//		sqlSB.append(" APPLIED_AMOUNT, ");
//		sqlSB.append(" BASE_AMOUNT, ");
//		sqlSB.append(" AMOUNT_GROUP, ");
//		sqlSB.append(" AMOUNT_TYPE, ");
//		sqlSB.append(" REMOVED, ");
//		sqlSB.append(" INSTALL_ID, ");
//		sqlSB.append(" OBJECT_ID, ");
//		sqlSB.append(" OBJECT_TYPE, ");
//		sqlSB.append(" TIME_STAMP, ");
//		sqlSB.append(" BAT_ID, ");
//		sqlSB.append("INSURE_COMPANY_ID, ");
//		sqlSB.append("INSURE_COMPANY_CODE ");
//		sqlSB.append(") values (");
//		sqlSB.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//		pstmt = conn.prepareStatement(sqlSB.toString());
//		int i = 1;
//		pstmt.setString(i++, vo.getInstallDetailId());
//		pstmt.setDouble(i++, vo.getOriginalAmount());
//		pstmt.setDouble(i++, vo.getAppliedAmount());
//		pstmt.setDouble(i++, vo.getBaseAmount());
//		pstmt.setLong(i++, vo.getAmountGroup());
//		pstmt.setLong(i++, vo.getAmountType());
//		pstmt.setLong(i++, vo.getRemoved());
//		pstmt.setLong(i++, vo.getInstallId());
//		pstmt.setLong(i++, vo.getObjectId());
//		pstmt.setString(i++, vo.getObjectType());
//		pstmt.setDate(i++, vo.getTimeStamp());
//		pstmt.setLong(i++, vo.getBatId());
//		pstmt.setLong(i++, Long.parseLong(ResourceBundleUtil.getCompanyId()));
//		pstmt.setString(i++, ResourceBundleUtil.getCompanyCd());
//		pstmt.execute();
//		pstmt.close();
//
//	}
//
//	public void insertStgAccInstrument(Connection conn, StgAccInstrument vo) throws SQLException, Exception {
//		if (vo == null) {
//			throw new Exception("Class of AccDAOimpl mothed insertStgAccInstrument param StgAccInstrument is null!");
//		}
//		StringBuilder sqlSB = new StringBuilder();
//		sqlSB.append("insert into STG_ACC_INSTRUMENT(");
//		sqlSB.append(" INSTRUMENT_ID, ");
//		sqlSB.append(" PAYMENT_TYPE, ");
//		sqlSB.append(" INSTRUMENT_NUMBER, ");
//		sqlSB.append(" ROLE_ID, ");
//		sqlSB.append(" PARTY_CODE, ");
//		sqlSB.append(" CURRENCY, ");
//		sqlSB.append(" AMOUNT, ");
//		sqlSB.append(" COMMISION, ");
//		sqlSB.append(" TAX_BASE, ");
//		sqlSB.append(" ACCOUNT_NUMBER, ");
//		sqlSB.append(" REMOVED, ");
//		sqlSB.append(" ACCOUNT_NAME, ");
//		sqlSB.append(" ISCASHBOX, ");
//		sqlSB.append(" CHECK_BOOK, ");
//		sqlSB.append(" TRANS_ID, ");
//		sqlSB.append(" OBJECT_ID, ");
//		sqlSB.append(" OBJECT_TYPE, ");
//		sqlSB.append(" TIME_STAMP, ");
//		sqlSB.append(" BAT_ID, ");
//		sqlSB.append(" INSURE_COMPANY_ID, ");
//		sqlSB.append(" INSURE_COMPANY_CODE ");
//		sqlSB.append(") values (");
//		sqlSB.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//		pstmt = conn.prepareStatement(sqlSB.toString());
//		int i = 1;
//		pstmt.setLong(i++, vo.getInstrumentId());
//		pstmt.setLong(i++, vo.getPaymentType());
//		pstmt.setString(i++, vo.getInstrumentNumber());
//		pstmt.setLong(i++, vo.getRoleId());
//		pstmt.setString(i++, vo.getPartyCode());
//		pstmt.setLong(i++, vo.getCurrency());
//		pstmt.setDouble(i++, vo.getAmount());
//		pstmt.setDouble(i++, vo.getCommision());
//		pstmt.setDouble(i++, vo.getTaxBase());
//		pstmt.setString(i++, vo.getAccountNumber());
//		pstmt.setLong(i++, vo.getRemoved());
//		pstmt.setString(i++, vo.getAccountName());
//		pstmt.setLong(i++, vo.getIscashbox());
//		pstmt.setLong(i++, vo.getCheckBook());
//		pstmt.setLong(i++, vo.getTransId());
//		pstmt.setLong(i++, vo.getObjectId());
//		pstmt.setString(i++, vo.getObjectType());
//		pstmt.setDate(i++, vo.getTimeStamp());
//		pstmt.setLong(i++, vo.getBatId());
//		pstmt.setLong(i++, Long.parseLong(ResourceBundleUtil.getCompanyId()));
//		pstmt.setString(i++, ResourceBundleUtil.getCompanyCd());
//		pstmt.execute();
//		pstmt.close();
//
//	}
//
//	public void insertStgAccRisk(Connection conn, StgAccRisk vo) throws SQLException, Exception {
//		if (vo == null) {
//			throw new Exception("Class of AccDAOimpl mothed insertStgAccRisk param StgAccRisk is null!");
//		}
//		StringBuilder sqlSB = new StringBuilder();
//		sqlSB.append("insert into STG_ACC_RISK(");
//		sqlSB.append(" RISK_ID, ");
//		sqlSB.append(" AMOUNT, ");
//		sqlSB.append(" AMOUNT_TYPE, ");
//		sqlSB.append(" RISK, ");
//		sqlSB.append(" REMOVED, ");
//		sqlSB.append(" TRANS_ID, ");
//		sqlSB.append(" TIME_STAMP, ");
//		sqlSB.append(" BAT_ID, ");
//		sqlSB.append(" OBJECT_ID, ");
//		sqlSB.append(" OBJECT_TYPE, ");
//		sqlSB.append(" INSURE_COMPANY_ID, ");
//		sqlSB.append(" INSURE_COMPANY_CODE ");
//		sqlSB.append(") values (");
//		sqlSB.append("?,?,?,?,?,?,?,?,?,?,?,?)");
//		pstmt = conn.prepareStatement(sqlSB.toString());
//		int i = 1;
//		pstmt.setLong(i++, vo.getRiskId());
//		pstmt.setDouble(i++, vo.getAmount());
//		pstmt.setString(i++, vo.getAmountType());
//		pstmt.setLong(i++, vo.getRisk());
//		pstmt.setLong(i++, vo.getRemoved());
//		pstmt.setLong(i++, vo.getTransId());
//		pstmt.setDate(i++, vo.getTimeStamp());
//		pstmt.setLong(i++, vo.getBatId());
//		pstmt.setLong(i++, vo.getObjectId());
//		pstmt.setString(i++, vo.getObjectType());
//		pstmt.setLong(i++, Long.parseLong(ResourceBundleUtil.getCompanyId()));
//		pstmt.setString(i++, ResourceBundleUtil.getCompanyCd());
//		pstmt.execute();
//		pstmt.close();
//
//	}
//
//	public void insertStgAccTrans(Connection conn, StgAccTrans vo) throws SQLException, Exception {
//		if (vo == null) {
//			throw new Exception("Class of AccDAOimpl mothed insertStgAccTrans param StgAccTrans is null!");
//		}
//		StringBuilder sqlSB = new StringBuilder();
//		sqlSB.append("insert into STG_ACC_TRANS(");
//		sqlSB.append(" TRANS_ID,");
//		sqlSB.append(" ACCOUNT_SOURCE,");
//		sqlSB.append(" TRANSACTION_TYPE,");
//		sqlSB.append(" STATUS,");
//		sqlSB.append(" ISINCREMENTAL,");
//		sqlSB.append(" ELEMENT_TYPE,");
//		sqlSB.append(" ACCOUNT_DATE,");
//		sqlSB.append(" TRANSACTION_NO,");
//		sqlSB.append(" PRE_PRINTED_NUM,");
//		sqlSB.append(" CURRENCY,");
//		sqlSB.append(" ORIGINAL_AMOUNT,");
//		sqlSB.append(" APPLIED_AMOUNT,");
//		sqlSB.append(" DOC_TYPE,");
//		sqlSB.append(" CONTRACT_NO,");
//		sqlSB.append(" NODE_NO,");
//		sqlSB.append(" PROPOSAL_NO,");
//		sqlSB.append(" CERTIFICATION_NO,");
//		sqlSB.append(" ISSUE_DATE,");
//		sqlSB.append(" APPLIED_DATE,");
//		sqlSB.append(" REMOVED,");
//		sqlSB.append(" INCREMENTAL_NO,");
//		sqlSB.append(" REF_NO,");
//		sqlSB.append(" ADD_PARTY,");
//		sqlSB.append(" USER_ID,");
//		sqlSB.append(" CONCEPT_FLAG,");
//		sqlSB.append(" PRODUCT_ID,");
//		sqlSB.append(" CS_CODE,");
//		sqlSB.append(" CREATE_USER_ID,");
//		sqlSB.append(" CREATE_DATE_TIME,");
//		sqlSB.append(" CHANGE_USER_ID,");
//		sqlSB.append(" CHANGE_DATE_TIME,");
//		sqlSB.append(" BUSINESS_COMPANY_ID,");
//		sqlSB.append(" BUSINESS_COMPANY_NODE_ID,");
//		sqlSB.append(" CLAIMTRANS_ID,");
//		sqlSB.append(" TAX_ZONE,");
//		sqlSB.append(" ACCOUNTING_NODE_NO,");
//		sqlSB.append(" START_DATE,");
//		sqlSB.append(" END_DATE,");
//		sqlSB.append(" ORIGINAL_START_DATE,");
//		sqlSB.append(" ORIGINAL_END_DATE,");
//		sqlSB.append(" PAYMENT_DAY,");
//		sqlSB.append(" MAIN_VERSION,");
//		sqlSB.append(" VERSION,");
//		sqlSB.append(" SECURITY,");
//		sqlSB.append(" APPLIED_RECALL,");
//		sqlSB.append(" PRODUCT_NODE_ID,");
//		sqlSB.append(" CLAIMNO,");
//		sqlSB.append(" MAIN_COST_CENTER,");
//		sqlSB.append(" EXPENSE_TRANSNO,");
//		sqlSB.append(" ISSIMPLE,");
//		sqlSB.append(" UPFRONT,");
//		sqlSB.append(" INTEREST_CURRENCY,");
//		sqlSB.append(" INTEREST_AMOUNT,");
//		sqlSB.append(" USER_COMPANY_ID,");
//		sqlSB.append(" USER_COMPANY_NODE_ID,");
//		sqlSB.append(" POLICY_NO,");
//		sqlSB.append(" PAYMENT_MODE,");
//		sqlSB.append(" OWNER_ID,");
//		sqlSB.append(" IS_SEND_REI,");
//		sqlSB.append(" COMMISSION_RATE,");
//		sqlSB.append(" EXCHANGE_DATE,");
//		sqlSB.append(" OBJECT_TYPE,");
//		sqlSB.append(" TIME_STAMP,");
//		sqlSB.append(" BAT_ID,");
//		sqlSB.append(" OBJECT_ID, ");
//		sqlSB.append(" INSURE_COMPANY_ID, ");
//		sqlSB.append(" INSURE_COMPANY_CODE ");
//		sqlSB.append(") values (");
//		sqlSB.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?,?,?,?)");
//		pstmt = conn.prepareStatement(sqlSB.toString());
//		int i = 1;
//		pstmt.setLong(i++, vo.getTransId());
//		pstmt.setLong(i++, vo.getAccountSource());
//		pstmt.setLong(i++, vo.getTransactionType());
//		pstmt.setLong(i++, vo.getStatus());
//		pstmt.setLong(i++, vo.getIsincremental());
//		pstmt.setLong(i++, vo.getElementType());
//		pstmt.setDate(i++, vo.getAccountDate());
//		pstmt.setString(i++, vo.getTransactionNo());
//		pstmt.setString(i++, vo.getPrePrintedNum());
//		pstmt.setLong(i++, vo.getCurrency());
//		pstmt.setDouble(i++, vo.getOriginalAmount());
//		pstmt.setDouble(i++, vo.getAppliedAmount());
//		pstmt.setLong(i++, vo.getDocType());
//		pstmt.setString(i++, vo.getContractNo());
//		pstmt.setString(i++, vo.getNodeNo());
//		pstmt.setString(i++, vo.getProposalNo());
//		pstmt.setString(i++, vo.getCertificationNo());
//		pstmt.setDate(i++, vo.getIssueDate());
//		pstmt.setDate(i++, vo.getAppliedDate());
//		pstmt.setLong(i++, vo.getRemoved());
//		pstmt.setLong(i++, vo.getIncrementalNo());
//		pstmt.setString(i++, vo.getRefNo());
//		pstmt.setString(i++, vo.getAddParty());
//		pstmt.setLong(i++, vo.getUserId());
//		pstmt.setLong(i++, vo.getConceptFlag());
//		pstmt.setString(i++, vo.getProductCode());
//		pstmt.setString(i++, vo.getCsCode());
//		pstmt.setLong(i++, vo.getCreateUserId());
//		pstmt.setDate(i++, vo.getCreateDateTime());
//		pstmt.setLong(i++, vo.getChangeUserId());
//		pstmt.setDate(i++, vo.getChangeDateTime());
//		pstmt.setString(i++, vo.getBusinessCompanyCode());
//		pstmt.setString(i++, vo.getBusinessCompanyNodeCode());
//		pstmt.setLong(i++, vo.getClaimtransId());
//		pstmt.setString(i++, vo.getTaxZone());
//		pstmt.setString(i++, vo.getAccountingNodeNo());
//		pstmt.setDate(i++, vo.getStartDate());
//		pstmt.setDate(i++, vo.getEndDate());
//		pstmt.setDate(i++, vo.getOriginalStartDate());
//		pstmt.setDate(i++, vo.getOriginalEndDate());
//		pstmt.setLong(i++, vo.getPaymentDay());
//		pstmt.setLong(i++, vo.getMainVersion());
//		pstmt.setLong(i++, vo.getVersion());
//		pstmt.setLong(i++, vo.getSecurity());
//		pstmt.setString(i++, vo.getAppliedRecall());
//		pstmt.setString(i++, vo.getProductNodeCode());
//		pstmt.setString(i++, vo.getClaimno());
//		pstmt.setLong(i++, vo.getMainCostCenter());
//		pstmt.setString(i++, vo.getExpenseTransno());
//		pstmt.setLong(i++, vo.getIssimple());
//		pstmt.setLong(i++, vo.getUpfront());
//		pstmt.setString(i++, vo.getInterestCurrency());
//		pstmt.setDouble(i++, vo.getInterestAmount());
//		pstmt.setString(i++, vo.getUserCompanyCode());
//		pstmt.setString(i++, vo.getUserCompanyNodeCode());
//		pstmt.setString(i++, vo.getPolicyNo());
//		pstmt.setLong(i++, vo.getPaymentMode());
//		pstmt.setString(i++, vo.getOwnerId());
//		pstmt.setLong(i++, vo.getIsSendRei());
//		pstmt.setDouble(i++, vo.getCommissionRate());
//		pstmt.setDate(i++, vo.getExchangeDate());
//		pstmt.setString(i++, vo.getObjectType());
//		pstmt.setDate(i++, vo.getTimeStamp());
//		pstmt.setLong(i++, vo.getBatId());
//		pstmt.setLong(i++, vo.getObjectId());
//		pstmt.setLong(i++, Long.parseLong(ResourceBundleUtil.getCompanyId()));
//		pstmt.setString(i++, ResourceBundleUtil.getCompanyCd());
//		pstmt.execute();
//		pstmt.close();
//		//
//
//	}
//
//	public void insertStgAccTranscostcentre(Connection conn, StgAccTranscostcentre vo) throws SQLException, Exception {
//		if (vo == null) {
//			throw new Exception("Class of AccDAOimpl mothed insertStgAccTranscostcentre param StgAccTranscostcentre is null!");
//		}
//		StringBuilder sqlSB = new StringBuilder();
//		sqlSB.append("insert into STG_ACC_TRANSCOSTCENTRE(");
//		sqlSB.append(" TRANSCOSTCENTRE_ID, ");
//		sqlSB.append(" REMOVED, ");
//		sqlSB.append(" SUB_COST_CENTER, ");
//		sqlSB.append(" PERCENTAGE, ");
//		sqlSB.append(" AMOUNT, ");
//		sqlSB.append(" TRANS_ID, ");
//		sqlSB.append(" OBJECT_TYPE, ");
//		sqlSB.append(" TIME_STAMP, ");
//		sqlSB.append(" BAT_ID, ");
//		sqlSB.append(" OBJECT_ID, ");
//		sqlSB.append(" INSURE_COMPANY_ID, ");
//		sqlSB.append(" INSURE_COMPANY_CODE ");
//		sqlSB.append(") values (");
//		sqlSB.append("?,?,?,?,?,?,?,?,?,?,?,?)");
//		pstmt = conn.prepareStatement(sqlSB.toString());
//		int i = 1;
//
//		pstmt.setLong(i++, vo.getTranscostcentreId());
//		pstmt.setLong(i++, vo.getTransId());
//		pstmt.setLong(i++, vo.getRemoved());
//		pstmt.setLong(i++, vo.getSubCostCenter());
//		pstmt.setDouble(i++, vo.getPercentage());
//		pstmt.setDouble(i++, vo.getAmount());
//		pstmt.setLong(i++, vo.getTransId());
//		pstmt.setString(i++, vo.getObjectType());
//		pstmt.setDate(i++, vo.getTimeStamp());
//		pstmt.setLong(i++, vo.getBatId());
//		pstmt.setLong(i++, vo.getObjectId());
//		pstmt.setLong(i++, Long.parseLong(ResourceBundleUtil.getCompanyId()));
//		pstmt.setString(i++, ResourceBundleUtil.getCompanyCd());
//		pstmt.execute();
//		pstmt.close();
//
//	}
//
//	public void insertStgAccTransdetail(Connection conn, StgAccTransdetail vo) throws SQLException, Exception {
//		if (vo == null) {
//			throw new Exception("Class of AccDAOimpl mothed insertStgAccTransdetail param StgAccTransdetail is null!");
//		}
//		StringBuilder sqlSB = new StringBuilder();
//		sqlSB.append("insert into STG_ACC_TRANSDETAIL(");
//		sqlSB.append(" TRANS_DETAIL_ID, ");
//		sqlSB.append(" ORIGINAL_AMOUNT, ");
//		sqlSB.append(" APPLIED_AMOUNT, ");
//		sqlSB.append(" BASE_AMOUNT, ");
//		sqlSB.append(" AMOUNT_GROUP, ");
//		sqlSB.append(" AMOUNT_TYPE, ");
//		sqlSB.append(" REMOVED, ");
//		sqlSB.append(" TRANS_ID, ");
//		sqlSB.append(" TIME_STAMP, ");
//		sqlSB.append(" BAT_ID, ");
//		sqlSB.append(" OBJECT_ID, ");
//		sqlSB.append(" OBJECT_TYPE, ");
//		sqlSB.append(" INSURE_COMPANY_ID, ");
//		sqlSB.append(" INSURE_COMPANY_CODE ");
//		sqlSB.append(") values (");
//		sqlSB.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//		pstmt = conn.prepareStatement(sqlSB.toString());
//		int i = 1;
//		pstmt.setLong(i++, vo.getTransDetailId());
//		pstmt.setDouble(i++, vo.getOriginalAmount());
//		pstmt.setDouble(i++, vo.getAppliedAmount());
//		pstmt.setDouble(i++, vo.getBaseAmount());
//		pstmt.setLong(i++, vo.getAmountGroup());
//		pstmt.setLong(i++, vo.getAmountType());
//		pstmt.setLong(i++, vo.getRemoved());
//		pstmt.setLong(i++, vo.getTransId());
//		pstmt.setDate(i++, vo.getTimeStamp());
//		pstmt.setLong(i++, vo.getBatId());
//		pstmt.setLong(i++, vo.getObjectId());
//		pstmt.setString(i++, vo.getObjectType());
//		pstmt.setLong(i++, Long.parseLong(ResourceBundleUtil.getCompanyId()));
//		pstmt.setString(i++, ResourceBundleUtil.getCompanyCd());
//		pstmt.execute();
//		pstmt.close();
//
//	}
//
//	public void insertStgAccGldefinition(Connection conn, StgAccGldefinition vo) throws SQLException, Exception {
//		if (vo == null) {
//			throw new Exception("Class of AccDAOimpl mothed insertStgAccGldefinition param StgAccGldefinition is null!");
//		}
//		StringBuilder sqlSB = new StringBuilder();
//		sqlSB.append("insert into STG_ACC_GLDEFINITION(");
//		sqlSB.append(" GLDEF_ID, ");
//		sqlSB.append(" REMOVED, ");
//		sqlSB.append(" NAME, ");
//		sqlSB.append(" TAX_ZONE, ");
//		sqlSB.append(" SOURCE, ");
//		sqlSB.append(" DOCUMENT_TYPE, ");
//		sqlSB.append(" CONCEPT, ");
//		sqlSB.append(" LOI, ");
//		sqlSB.append(" CURRENCY, ");
//		sqlSB.append(" AMOUNT_TYPE, ");
//		sqlSB.append(" AMOUNT_SIGN, ");
//		sqlSB.append(" APPLIED_SOURCE, ");
//		sqlSB.append(" APPLIED_DOCUMENT_TYPE, ");
//		sqlSB.append(" ISAPPLIEDTRANS, ");
//		sqlSB.append(" BAT_ID, ");
//		sqlSB.append(" OBJECT_ID, ");
//		sqlSB.append(" OBJECT_TYPE, ");
//		sqlSB.append(" TIME_STAMP, ");
//		sqlSB.append(" INSURE_COMPANY_ID, ");
//		sqlSB.append(" INSURE_COMPANY_CODE ");
//		sqlSB.append(") values (");
//		sqlSB.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//		pstmt = conn.prepareStatement(sqlSB.toString());
//		int i = 1;
//		pstmt.setLong(i++, vo.getGldefId());
//		pstmt.setLong(i++, vo.getRemoved());
//		pstmt.setString(i++, vo.getName());
//		pstmt.setLong(i++, vo.getTaxZone());
//		pstmt.setLong(i++, vo.getSource());
//		pstmt.setLong(i++, vo.getDocumentType());
//		pstmt.setLong(i++, vo.getConcept());
//		pstmt.setLong(i++, vo.getLoi());
//		pstmt.setLong(i++, vo.getCurrency());
//		pstmt.setLong(i++, vo.getAmountType());
//		pstmt.setString(i++, vo.getAmountSign());
//		pstmt.setLong(i++, vo.getAppliedSource());
//		pstmt.setLong(i++, vo.getAppliedDocumentType());
//		pstmt.setLong(i++, vo.getIsappliedtrans());
//		pstmt.setLong(i++, vo.getBatId());
//		pstmt.setLong(i++, vo.getObjectId());
//		pstmt.setString(i++, vo.getObjectType());
//		pstmt.setDate(i++, vo.getTimeStamp());
//		pstmt.setLong(i++, Long.parseLong(ResourceBundleUtil.getCompanyId()));
//		pstmt.setString(i++, ResourceBundleUtil.getCompanyCd());
//		pstmt.execute();
//		pstmt.close();
//
//	}
//
//	public void insertStgGlbasedefinition(Connection conn, StgAccGlbasedefinition vo) throws SQLException, Exception {
//		if (vo == null) {
//			throw new Exception("Class of AccDAOimpl mothed insertStgGlbasedefinition param StgAccGlbasedefinition is null!");
//		}
//		StringBuilder sqlSB = new StringBuilder();
//		sqlSB.append("insert into STG_ACC_GLBASEDEFINITION(");
//		sqlSB.append(" GLBASEDEFINITION_ID, ");
//		sqlSB.append(" REMOVED, ");
//		sqlSB.append(" NAME, ");
//		sqlSB.append(" VOUCHER, ");
//		sqlSB.append(" GLCODE, ");
//		sqlSB.append(" ISDEBIT, ");
//		sqlSB.append(" HAS_THIRD_PARTY, ");
//		sqlSB.append(" GLDEF_ID, ");
//		sqlSB.append(" TIME_STAMP, ");
//		sqlSB.append(" BAT_ID, ");
//		sqlSB.append(" OBJECT_ID, ");
//		sqlSB.append(" OBJECT_TYPE, ");
//		sqlSB.append(" INSURE_COMPANY_ID, ");
//		sqlSB.append(" INSURE_COMPANY_CODE ");
//		sqlSB.append(") values (");
//		sqlSB.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//		pstmt = conn.prepareStatement(sqlSB.toString());
//		int i = 1;
//		pstmt.setLong(i++, vo.getGlbasedefinitionId());
//		pstmt.setLong(i++, vo.getRemoved());
//		pstmt.setString(i++, vo.getName());
//		pstmt.setLong(i++, vo.getVoucher());
//		pstmt.setString(i++, vo.getGlcode());
//		pstmt.setLong(i++, vo.getIsdebit());
//		pstmt.setLong(i++, vo.getHasThirdParty());
//		pstmt.setLong(i++, vo.getGldefId());
//		pstmt.setDate(i++, vo.getTimeStamp());
//		pstmt.setLong(i++, vo.getBatId());
//		pstmt.setLong(i++, vo.getObjectId());
//		pstmt.setString(i++, vo.getObjectType());
//		pstmt.setLong(i++, Long.parseLong(ResourceBundleUtil.getCompanyId()));
//		pstmt.setString(i++, ResourceBundleUtil.getCompanyCd());
//		pstmt.execute();
//		pstmt.close();
//
//	}

	public void updateIncr(EtlPharosIncrAcc ec) throws SQLException {

		String sql = "update " + Constants.ETL_PHAROS_INCR_ACC + ResourceBundleUtil.readValue("riskNodeThreads") + " set BAT_ID=?," + "STAGE_WRITE_FLAG=? where OBJECT_ID=?";
		pstmt = pharosConn.prepareStatement(sql);

		pstmt.setLong(1, ec.getBatId());
		pstmt.setString(2, ec.getStageWriteFlag());
		pstmt.setLong(3, ec.getObjectId());
		pstmt.executeUpdate();
		pstmt.close();
	}

}
