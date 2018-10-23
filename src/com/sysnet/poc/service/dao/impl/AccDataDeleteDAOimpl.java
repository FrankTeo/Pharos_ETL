package com.sysnet.poc.service.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.sysnet.poc.service.dao.AccDataDeleteDAO;

public class AccDataDeleteDAOimpl implements AccDataDeleteDAO {

	private PreparedStatement pstmt = null;

	public int deleteAccBankAccount(String objId, Connection con) throws SQLException, Exception {
		String delSql = "delete from stg_acc_bankaccount t1 where account_code=?";
		int i = this.executeSql(pstmt, delSql, con, objId);
		return i;
	}

	/**
	 * --交易类型代码：108 交易类型：银行存款明细
	 */
	public int deleteAccBankDeposit(String objId, Connection con) throws SQLException, Exception {

		String delSql = " delete from stg_acc_bankdeposit  t1 where depositno=?";
		int i = this.executeSql(pstmt, delSql, con, objId);
		return i;
	}

	public int deleteAccBankTransfer(String objId, Connection con) throws SQLException, Exception {
		String delSql = "delete from stg_acc_banktransferdetail t1 where banktransfer_id = ?";
		int i = this.executeSql(pstmt, delSql, con, objId);
		return i;
	}

	public int deleteAccBankTransferFailureLog(String objId, Connection con) throws SQLException, Exception {
		String delSql = " delete from stg_acc_banktransferfailurelog  t1 where banktransferfailurelog_id = ?";
		int i = this.executeSql(pstmt, delSql, con, objId);
		return i;
	}

	public int deleteAccCashBox(String objId, Connection con) throws SQLException, Exception {
		String delSql = " delete from stg_acc_cashbox  t1 where boxno=?";
		int i = this.executeSql(pstmt, delSql, con, objId);
		return i;
	}

	public int deleteAccCashier_Close(String objId, Connection con) throws SQLException, Exception {
		String delSql = "delete from stg_acc_cashier_close  t1 where cashier_close_id = ?";
		int i = this.executeSql(pstmt, delSql, con, objId);
		return i;
	}

	public int deleteAccCheckBook(String objId, Connection con) throws SQLException, Exception {
		String delSql = "delete from stg_acc_checkbook  t1  where checkbook_id=?";
		int i = this.executeSql(pstmt, delSql, con, objId);
		return i;
	}

	public int deleteAccGldefinition(String objId, Connection con) throws SQLException, Exception {
		String delSql = "";
		int i = 0;
		delSql = "delete from stg_acc_glbasedefinition t  where gldef_id=?";
		i = this.executeSql(pstmt, delSql, con, objId);
		delSql = "delete from stg_acc_gldefinition  t  where gldef_id=?";
		i = this.executeSql(pstmt, delSql, con, objId);
		return i;
	}

	public int deleteAccInvoice(String objId, Connection con) throws SQLException, Exception {
		String delSql = "";
		int i = 0;
		delSql = "delete from stg_acc_invoicebasedetail  t1 where invoice_detail_id in  (select invoice_detail_id  from stg_acc_invoicedetail t2  where invoice_id in (select invoice_id from stg_acc_invoice where invoice_number=?))";
		i = this.executeSql(pstmt, delSql, con, objId);
		delSql = "delete from stg_acc_invoicedetail t  where invoice_id in (select invoice_id from stg_acc_invoice where invoice_number=?) ";
		i = this.executeSql(pstmt, delSql, con, objId);
		delSql = "delete from stg_acc_invoice t where t.invoice_number=?";
		i = this.executeSql(pstmt, delSql, con, objId);
		return i;
	}

	public int deleteAccInvoiceBook(String objId, Connection con) throws SQLException, Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	public int deleteAccMonthClose(String objId, Connection con) throws SQLException, Exception {
		String delSql = "delete from stg_acc_monthclose  t1 where monthclose_id = ?";
		int i = this.executeSql(pstmt, delSql, con, objId);
		return i;
	}

	/**
	 * --交易类型代码：106 交易类型：收付方案
	 */
	public int deleteAccReference(String objId, Connection con) throws SQLException, Exception {
		String delSql = "";
		int i = 0;
		delSql = "delete from tg_acc_paymentmech t1where slice_id in (select ref_id from tg_acc_paymentslice t2 where ref_id=:?)";
		i = this.executeSql(pstmt, delSql, con, objId);
		delSql = "delete from tg_acc_paymentslice t1 where ref_id=:?";
		i = this.executeSql(pstmt, delSql, con, objId);
		delSql = "delete from tg_acc_reference  t1 where ref_id=:?";
		i = this.executeSql(pstmt, delSql, con, objId);
		return i;
	}

	/**
	 * 交易主表
	 */
	public int deleteAccTrans(String objId, Connection con) throws SQLException, Exception {
		String delSql = "";
		int i = 0;
		delSql = "delete from stg_acc_generalledgerhistory t  where trans_detail_id in ( select trans_detail_id from stg_acc_transdetail  where trans_id in  (select trans_id from stg_acc_trans   where transaction_no=?))";
		i = this.executeSql(pstmt, delSql, con, objId);
		delSql = "delete from stg_acc_generalledger t   where TRANSCATION_NO=?";
		i = this.executeSql(pstmt, delSql, con, objId);
		delSql = "delete from stg_acc_dyinstallment t  where trans_id in  (select trans_id from stg_acc_trans  where transaction_no=?)";
		i = this.executeSql(pstmt, delSql, con, objId);
		delSql = "delete from stg_acc_transcostcentre t  where trans_id in  (select trans_id from stg_acc_trans  where transaction_no=?)";
		i = this.executeSql(pstmt, delSql, con, objId);
		delSql = "delete from stg_acc_risk t  where trans_id in  (select trans_id from stg_acc_trans  where transaction_no=?)";
		i = this.executeSql(pstmt, delSql, con, objId);
		delSql = "delete from stg_acc_amontloi t  where trans_id in  (select trans_id from stg_acc_trans  where transaction_no=?)";
		i = this.executeSql(pstmt, delSql, con, objId);
		delSql = "delete from stg_acc_installdetail  t  where install_id in ( select install_id from stg_acc_install where trans_id in  (select trans_id from stg_acc_trans   where transaction_no=?))";
		i = this.executeSql(pstmt, delSql, con, objId);
		delSql = "delete from stg_acc_install  t  where trans_id in  (select trans_id from stg_acc_trans  where transaction_no=?)";
		i = this.executeSql(pstmt, delSql, con, objId);
		delSql = "delete from stg_acc_instrument t  where trans_id in  (select trans_id from stg_acc_trans  where transaction_no=?)";
		i = this.executeSql(pstmt, delSql, con, objId);
		delSql = "delete from stg_acc_transdetail t  where trans_id in  (select trans_id from stg_acc_trans  where transaction_no=?)";
		i = this.executeSql(pstmt, delSql, con, objId);
		delSql = "delete from stg_acc_trans t where transaction_no=?";
		i = this.executeSql(pstmt, delSql, con, objId);
		return i;
	}

	public int deleteAccApplication(String objId, Connection con) throws SQLException, Exception {
		String delSql = "";
		int i = 0;
		delSql = "delete from stg_acc_applicationdetail t where  t.app_id=? ";
		i = this.executeSql(pstmt, delSql, con, objId);
		delSql = "delete from stg_acc_application t where t.app_id=?";
		i = this.executeSql(pstmt, delSql, con, objId);
		return i;
	}

	private int executeSql(PreparedStatement pm, String sql, Connection con, String objId) throws SQLException {

		pm = con.prepareStatement(sql);
		pm.setString(1, objId);
		int i = pm.executeUpdate();
		pm.cancel();
		return i;
	}
}
