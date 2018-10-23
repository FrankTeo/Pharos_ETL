/**
 * FileName: StorageTransAction.java Date: Jul 22, 2009
 *
 * Copyright (c) 1994-2009 北京尚洋信德信息技术股份有限公司 版权所有
 *
 * History:
 *        <p>Jul 22, 2009:此处记录修改历史记录
 *
 */
package com.sysnet.poc.service.storage;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.sysnet.poc.control.manage.vo.ThreadCatchDB;

/**
 * 此对象将Record列表中的相应数据，组织后插入到指定的数据库中
 * 
 * @author <a href=huang_y@sysnet.com.cn>huang_y</a>
 * @version 1.0
 */
public class StorageTransAction extends Thread {
	@Override
	public void run() {
		execute();
		tcdb.add();
	}

	private ThreadCatchDB tcdb;

	public ThreadCatchDB getTcdb() {
		return tcdb;
	}

	public void setTcdb(ThreadCatchDB tcdb) {
		this.tcdb = tcdb;
	}

	/**
	 * 单条Record信息处理对象
	 */
	private ParseSColumn parseColumn = null;

	/**
	 * 数据库连接
	 */
	private Connection conn = null;

	//
	// /**
	// * 需要插入到数据库中的记录列表
	// */
	// private List<SColumn> scolumns = null;
	//
	// /**
	// * 将Record中的数据组织成列表
	// */
	// private final List<PreparedStatement> pstmts = new ArrayList<PreparedStatement>();

	public StorageTransAction() {
		parseColumn = new ParseSColumn();
	}

	// /**
	// * 根据record创建Stateement。
	// */
	// public void buildStatement() throws PocException {
	// for (SColumn record : this.scolumns) {
	// this.parseColumn.setColumn(record);
	// PreparedStatement _pstmt = this.parseColumn.getPreparedStatement(this.conn);
	// this.pstmts.add(_pstmt);
	// }
	// }

	// /**
	// * 通过调用List中的statement对象，执行入库操作 如果提交出现错误或异常，会自动回滚之前提交的操作，并将异常抛出
	// */
	// public void commit() throws Exception, PocException {
	// Boolean oldFlag = null;
	// try {
	// // 记录当前的提交标志，提交以后复原
	// oldFlag = this.conn.getAutoCommit();
	// this.conn.setAutoCommit(false);
	// for (PreparedStatement pstmt : this.pstmts) {
	// try {
	// pstmt.execute();
	// } catch (SQLException sqlEx) {
	// throw new PocException("插入数据时出现错误:" + sqlEx.getMessage());
	// }
	// }
	// this.conn.commit();
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// try {
	// this.conn.rollback();
	// } catch (Exception ex1) {
	// ex1.printStackTrace();
	// throw ex1;
	// }
	// throw ex;
	// } finally {
	// if (oldFlag != null)
	// this.conn.setAutoCommit(oldFlag);
	// }
	// }

	/**
	 * @see StorageTransAction#extendSQL
	 */
	private void execute() {
		PreparedStatement pstmt = null;
		while (tcdb.getEl().size() > 0) {
			try {
				SColumn record = tcdb.getTask();
				// for (SColumn record : this.scolumns) {
				parseColumn.setColumn(record);
				pstmt = this.parseColumn.getPreparedStatement(this.conn);
				if (pstmt != null) {
//					System.out.println("调试模式:SQL-------" + this.parseColumn.getSQL());
					pstmt.execute();
					pstmt.close();
				} else {
					System.err.println("-------------------------------------------pstmt is null");
				}
			} catch (Exception e) {
				tcdb.clear();
				e.printStackTrace();
				tcdb.setErrorMessage(e.toString());
				tcdb.setIsAllRight(false);
				System.out.println("-------" + this.parseColumn.getSQL());
			} finally {
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 特殊的扩展的SQL，用来实现ODS插入后的功能
	 */

	public void extendSQL(String extendSQL, String[] extendsSQLParameters) throws SQLException {
		//暂不执行更新
		boolean type = true;
		
		if( type != true ) {
			//System.out.println("X------------------X:当前为调试模式, 暂不执行存储过程");
			return;
		}
		
		if (extendSQL != null && !extendSQL.equals("")) {
			CallableStatement stmt = null;
			try {
				stmt = this.conn.prepareCall(extendSQL);
				if (extendsSQLParameters != null) {
					for (int i = 0; i < extendsSQLParameters.length; i++) {
						stmt.setString(i + 1, extendsSQLParameters[i]);
					}
				}
				stmt.execute();
			} finally {
				stmt.close();
			}
		}
	}

	/**
	 * 设置数据库连接
	 * 
	 * @param conn
	 */
	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	// /**
	// * 需要插入到数据库的Record列表
	// *
	// * @param records
	// */
	// public void setSColumns(List<SColumn> scolumns) {
	// this.scolumns = scolumns;
	// }
	//
	// /**
	// * 得到封装完成后的PreparedStatement对象
	// *
	// * @return
	// */
	// public List<PreparedStatement> getStatements() {
	// return this.pstmts;
	// }

}
