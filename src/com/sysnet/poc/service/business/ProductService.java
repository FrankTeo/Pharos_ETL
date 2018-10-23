package com.sysnet.poc.service.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import pharos.framework.ods.service.ODSEFService;
import pharos.framework.ods.vo.ODSCondition;
import pharos.framework.ods.vo.ODSProduct;

import com.sysnet.poc.service.dao.dbc.ConnDB;
import com.sysnet.poc.util.DateHelper;
import com.sysnet.poc.util.PharosServiceContainer;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ProductService extends TimerTask {
	private Logger log = Logger.getLogger(PolicyBusiness.class);
	private ODSEFService service = null;
	private List<Map> products = null;
	private List<Map> conditions = null;
	private List<Map> nexues = null;

	/**
	 * 初始化对象
	 */
	private void init() {
		// Properties prop = new Properties();
		// prop.setProperty("java.naming.factory.initial",
		// "org.jnp.interfaces.NamingContextFactory");
		// prop.setProperty("java.naming.factory.url.pkgs",
		// "org.jboss.naming:org.jnp.interfaces");
		// prop.setProperty("java.naming.provider.url", "192.168.10.199:1099");
		// try {
		// ctx = new InitialContext(prop);
		// service = (ODSEFService) ctx.lookup("ODSEFServiceImpl/remote");
		// } catch (NamingException e) {
		// e.printStackTrace();
		// }
		service = PharosServiceContainer.Instance().getODSEFService();
		this.products = new ArrayList();
		this.conditions = new ArrayList();
		this.nexues = new ArrayList();
	}

	/**
	 * 采集数就
	 */
	private void collectData() throws Exception {
		this.collectProducts();
		this.collectCondition();
		this.collectNexues();
	}

	/**
	 * 采集产品信息
	 * 
	 * @throws Exception
	 */
	private void collectProducts() throws Exception {
		/*
		 * PRODUCT_CD NUMBER(10) not null, NAME_CN VARCHAR2(50), COMPANY_CD NUMBER(10) not null, BAT_ID NUMBER, OBJECT_ID NUMBER, TIME_STAMP DATE, OBJECT_TYPE CHAR(1), SOUC_SYS_START_DATE DATE not null, SOUC_SYS_END_DATE DATE, PRODUCT_ID NUMBER(10) not null
		 */
		List<ODSProduct> pl = service.getAllProducts();
		System.out.println("======================" + pl.size());

		Map map = null;
		for (ODSProduct product : pl) {
			map = new HashMap();
			// 产品代码
			map.put("PRODUCT_CD", product.getProductCD());
			// 中文名称
			// map.put("NAME_CN", product.getProductName());
			map.put("NAME_CN", product.getDescription().getValue("2"));
			// 公司CD
			map.put("COMPANY_CD", product.getCompanyID());
			// 产品ID
			map.put("PRODUCT_ID", product.getProductID());
			this.products.add(map);
		}
	}

	/**
	 * 采集责任信息
	 * 
	 * @throws Exception
	 */
	private void collectCondition() throws Exception {
		/*
		 * ITEM_NO NUMBER(10) not null, ITEM_NAME VARCHAR2(50), TIME_STAMP DATE, OBJECT_TYPE CHAR(1), BAT_ID NUMBER, OBJECT_ID NUMBER, SOUC_SYS_START_DATE DATE not null, SOUC_SYS_END_DATE DATE, ITEM_ID NUMBER(10) not null
		 */
		List<ODSCondition> conditionList = service.getAllConditions();
		Map map = null;
		for (ODSCondition condition : conditionList) {
			map = new HashMap();
			// 责任代码
			map.put("ITEM_NO", condition.getConditionCD());
			// 中文名称
			map.put("ITEM_NAME", condition.getConditionName());
			// 责任ID
			map.put("ITEM_ID", condition.getConditionID());
			this.conditions.add(map);
		}

	}

	/**
	 * 采集关系信息
	 * 
	 * @throws Exception
	 */
	private void collectNexues() throws Exception {
		List<ODSProduct> pl = service.getAllProducts();
		Map map = null;
		for (ODSProduct product : pl) {
			long PRODUCT_ID = product.getProductID();
			long companyID = product.getCompanyID();
			List<Long> conditionIDlist = product.getConditions();
			for (Long l : conditionIDlist) {
				map = new HashMap();
				map.put("PRODUCT_ID", PRODUCT_ID);
				map.put("COMPANY_CD", companyID);
				map.put("ITEM_ID", l.longValue());
				this.nexues.add(map);
			}
		}
	}

	/**
	 * 执行产品数据入库
	 */
	private void instertData() {
		log.info("instertData开始执行");
		// 删除SQL语句
		String sqlpD = "DELETE FROM STG_PRD_PRODUCT";
		String sqlcD = "DELETE FROM STG_PRD_CONDITION";
		String sqlnD = "DELETE FROM STG_PRD_RISK_CONDIT_RELATION";
		// 插入语句
		String sqlp = "INSERT INTO STG_PRD_PRODUCT (" + "PRODUCT_CD," + "NAME_CN," + "COMPANY_CD," + "TIME_STAMP," + "SOUC_SYS_START_DATE," + "SOUC_SYS_END_DATE," + "PRODUCT_ID) VALUES(?,?,?,?,?,?,?)";
		String sqlc = "INSERT INTO STG_PRD_CONDITION (" + "ITEM_NO," + "ITEM_NAME," + "TIME_STAMP," + "SOUC_SYS_START_DATE," + "SOUC_SYS_END_DATE," + "ITEM_ID) VALUES (?,?,?,?,?,?)";
		Connection conn = null;
		try {
			conn = ConnDB.getODSConn();
			conn.setAutoCommit(false);
			// conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);\
			// 插入前删除产品表中的所有信息
			Statement Stmt = conn.createStatement();
			Stmt.execute(sqlpD);
			Stmt.execute(sqlcD);
			Stmt.execute(sqlnD);
			// 提交产品信息
			PreparedStatement p1 = null;
			String PRODUCT_CD = "";
			String NAME_CN = "";
			long COMPANY_CD = 0;
			java.sql.Date TIME_STAMP = new java.sql.Date(DateHelper.currentlyDate().getTime());
			java.sql.Date SOUC_SYS_START_DATE = new java.sql.Date(DateHelper.currentlyDate().getTime());
			java.sql.Date SOUC_SYS_END_DATE = new java.sql.Date(DateHelper.currentlyDate().getTime());
			long PRODUCT_ID = 0;
			for (Map map : this.products) {
				PRODUCT_CD = (String) map.get("PRODUCT_CD");
				NAME_CN = (String) map.get("NAME_CN");
				COMPANY_CD = ((Long) map.get("COMPANY_CD")).longValue();
				PRODUCT_ID = ((Long) map.get("PRODUCT_ID")).longValue();
				p1 = conn.prepareStatement(sqlp);
				p1.setString(1, PRODUCT_CD);
				p1.setString(2, NAME_CN);
				p1.setLong(3, COMPANY_CD);
				p1.setDate(4, TIME_STAMP);
				p1.setDate(5, SOUC_SYS_START_DATE);
				p1.setDate(6, SOUC_SYS_END_DATE);
				p1.setLong(7, PRODUCT_ID);
				p1.executeUpdate();
				p1.close();
			}
			// 提交责任信息
			PreparedStatement p2 = null;
			long ITEM_NO = 0;
			String ITEM_NAME = "";
			java.sql.Date TIME_STAMP2 = new java.sql.Date(DateHelper.currentlyDate().getTime());
			java.sql.Date SOUC_SYS_START_DATE2 = new java.sql.Date(DateHelper.currentlyDate().getTime());
			java.sql.Date SOUC_SYS_END_DATE2 = new java.sql.Date(DateHelper.currentlyDate().getTime());
			long ITEM_ID = 0;
			for (Map map : this.conditions) {
				p2 = conn.prepareStatement(sqlc);
				ITEM_NO = Long.parseLong((String) map.get("ITEM_NO"));
				ITEM_NAME = (String) map.get("ITEM_NAME");
				ITEM_ID = ((Long) map.get("ITEM_ID")).longValue();
				p2.setLong(1, ITEM_NO);
				p2.setString(2, ITEM_NAME);
				p2.setDate(3, TIME_STAMP2);
				p2.setDate(4, SOUC_SYS_START_DATE2);
				p2.setDate(5, SOUC_SYS_END_DATE2);
				p2.setLong(6, ITEM_ID);
				p2.executeUpdate();
				p2.close();
			}
			// 提交关系信息

			// 速度太慢，先注视掉
			// for (Map map : this.nexues) {
			// PRODUCT_ID3 = ((Long) map.get("PRODUCT_ID")).longValue();
			// ITEM_ID = ((Long) map.get("ITEM_ID")).longValue();
			// COMPANY_CD3 = ((Long) map.get("COMPANY_CD")).longValue();
			// p3 = conn.prepareStatement(sqln);
			// p3.setDate(1, TIME_STAMP3);
			// p3.setDate(2, SOUC_SYS_START_DATE3);
			// p3.setDate(3, SOUC_SYS_END_DATE3);
			// p3.setLong(4, PRODUCT_ID3);
			// p3.setLong(5, ITEM_ID);
			// p3.setLong(6, COMPANY_CD3);
			// p3.executeUpdate();
			// p3.close();
			// }

			conn.commit();
			log.info("产品数据更新执行完毕 ！");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			log.info("rollback==============================sqlException");
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (NamingException nameinge) {
			nameinge.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void run() {
		this.init();
		try {
			this.collectData();
		} catch (Exception e) {
			e.printStackTrace();
			log.info("==================================pharos EJB Exception!");
		}
		this.instertData();

	}

	public static void main(String args[]) throws Exception {
		ProductService ps = new ProductService();
		ps.init();
		ps.collectData();
		for (Map map : ps.nexues) {
			System.out.println(map);
		}
	}
}
