package com.sysnet.poc.service.business;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import pharos.company.vo.CompanyNode;
import pharos.framework.ods.service.ODSEFService;

import com.sysnet.poc.service.dao.dbc.ConnDB;
import com.sysnet.poc.util.PharosServiceContainer;

public class CompanyService extends TimerTask {
	private Logger log = Logger.getLogger(CompanyService.class);

	InitialContext ctx = null;
	ODSEFService service = null;

	public void init() {
		// Properties prop = new Properties();
		// prop.setProperty("java.naming.factory.initial",
		// "org.jnp.interfaces.NamingContextFactory");
		// prop.setProperty("java.naming.factory.url.pkgs",
		// "org.jboss.naming:org.jnp.interfaces");
		// prop.setProperty("java.naming.provider.url", "192.168.10.199:1099");
		// try {
		// ctx = new InitialContext(prop);
		// //service = (ODSEFService) ctx.lookup("ODSEFServiceImpl/remote");
		//
		// } catch (NamingException e) {
		// e.printStackTrace();
		// }

		service = PharosServiceContainer.Instance().getODSEFService();
	}

	public void CompaniesService() {
		log.info("CompaniesService开始执行");
		try {
			List<CompanyNode> list = service.getAllCompanies();
			if (list.size() == 0)
				System.out.println(list.size());
			/*
			 * for (CompanyNode node : list) { System.out.println("ID:" + node.getId() + " CD:" + node.getCode() + " name:" + node.getName() + " ParentCode:" + node.getParentCode()); }
			 */

			updateCompaniesTable(list);
			log.info("");
		} catch (Exception e) {
			log.info("productService执行失败" + e.getMessage());
			e.printStackTrace();
		}

	}

	public void updateCompaniesTable(List<CompanyNode> list) throws NamingException {
		Connection conn = null;
		try {
			log.info("updateCompaniesTable开始执行！");
			conn = ConnDB.getODSConn();
			conn.setAutoCommit(false);
			// 删除
			String deleSql = "delete from STG_PTY_COMPANY_INNER";
			Statement Stmt = conn.createStatement();
			Stmt.execute(deleSql);
			// 更新
			for (CompanyNode node : list) {
				int PARENT_CODE = 0;
				String COMPANY_NAME = "";
				Date START_DATE = null;
				Date END_DATE = null;
				String COMPANY_DESC = "";
				String COMPANY_CD = "";
				String OBJECT_TYPE = "";
				int BAT_ID = 0;
				int OBJECT_ID = 0;
				Date TIME_STAMP = null;
				int PARTY_ID = 0;
				String TAX_ZONE = "";
				int AVAILABLE = 0;
				int BIZ_COMPANY_NODE_ID = 0;
				long id = 0;
				id = node.getId();

				if ((node.getParentCode()) != null) {
					PARENT_CODE = Integer.parseInt(node.getParentCode());
				}
				if ((node.getName()) != null) {
					COMPANY_NAME = node.getName();
				}
				if ((node.getDescription()) != null) {
					COMPANY_DESC = node.getDescription().getValue("2");
				}
				if ((node.getCode()) != null) {
					COMPANY_CD = node.getCode();
				}
				if ((node.getCode()) != null) {
					// PARTY_ID = Integer.parseInt(node.getPartyCode());
				}
				TAX_ZONE = node.getTaxZone();

				String insertSql = "insert into STG_PTY_COMPANY_INNER (ID,PARENT_CODE,COMPANY_NAME,START_DATE ,END_DATE,COMPANY_DESC,COMPANY_CD ," + "OBJECT_TYPE,BAT_ID,OBJECT_ID,TIME_STAMP ,PARTY_ID,TAX_ZONE,AVAILABLE,BIZ_COMPANY_NODE_ID ) " + "values (" + id + "," + PARENT_CODE + ",'" + COMPANY_NAME + "'," + START_DATE + "," + END_DATE + ",'" + COMPANY_DESC + "','" + COMPANY_CD + "','" + OBJECT_TYPE + "'," + BAT_ID + "," + OBJECT_ID + "," + TIME_STAMP + "," + PARTY_ID + "," + TAX_ZONE + "," + AVAILABLE + "," + BIZ_COMPANY_NODE_ID + ")";
				// System.out.println("insertSql==="+insertSql);
				Stmt.execute(insertSql);
			}
			// 提交
			conn.commit();
			log.info("updateCompaniesTable执行成功！");
		} catch (SQLException e) {
			log.info("updateCompaniesTable执行失败" + e.getMessage());
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (SQLException e1) {

				e1.printStackTrace();
			}

			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {

		System.out.println("CompanyService---------");
		init();
		CompaniesService();
	}

}
