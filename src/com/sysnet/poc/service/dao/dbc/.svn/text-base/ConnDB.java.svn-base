package com.sysnet.poc.service.dao.dbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.sysnet.poc.util.AppServerEnv;
import com.sysnet.poc.util.ResourceBundleUtil;

public class ConnDB {

	// private static List<Connection> odsPool = new ArrayList<Connection>();
	// private static List<Connection> pharosPool = new ArrayList<Connection>();
	// private static Logger d = Logger.getLogger(ConnDB.class);

	public static Connection getODSConnJ() throws ClassNotFoundException, SQLException {
		String url = "jdbc:oracle:thin:@(description=(ADDRESS_LIST =(ADDRESS = (PROTOCOL = TCP)(HOST = " + ResourceBundleUtil.readValue("odsURL") + ")(PORT = " + ResourceBundleUtil.readValue("odsPORT") + ")))(CONNECT_DATA =(SERVER = DEDICATED)(SERVICE_NAME = " + ResourceBundleUtil.readValue("odsINSTANCE") + ")))";
		String user = ResourceBundleUtil.readValue("odsUSERNAME");
		String password = ResourceBundleUtil.readValue("odsPASSWORD");
		String driver = "oracle.jdbc.driver.OracleDriver";
		Connection conn = null;
		Class.forName(driver);
		conn = DriverManager.getConnection(url, user, password);
		return conn;
	}

	public static Connection getPharosConnJ() throws ClassNotFoundException, SQLException {
		String url = "jdbc:oracle:thin:@(description=(ADDRESS_LIST =(ADDRESS = (PROTOCOL = TCP)(HOST = " + ResourceBundleUtil.readValue("pharosdb.ip") + ")(PORT = " + ResourceBundleUtil.readValue("pharosdb.port") + ")))(CONNECT_DATA =(SERVER = DEDICATED)(SERVICE_NAME = " + ResourceBundleUtil.readValue("pharosdb.instance") + ")))";
		String user = ResourceBundleUtil.readValue("pharosdb.username");
		String password = ResourceBundleUtil.readValue("pharosdb.password");
		String driver = "oracle.jdbc.driver.OracleDriver";
		Connection conn = null;
		Class.forName(driver);
		conn = DriverManager.getConnection(url, user, password);
		return conn;
	}

	/**
	 * 返回ods库连接
	 */
	public static Connection getODSConn() throws SQLException, NamingException {
		Connection odsConn = null;
		Context ctx = AppServerEnv.getInitialContext();
		DataSource ds = (DataSource) ctx.lookup(ResourceBundleUtil.getProperty("odsjndi"));
		odsConn = ds.getConnection();
		// odsPool.add(odsConn);
		// log.info("ODS当前的活动连接数："+getActionConnectionCount(odsPool));
		return odsConn;
	}

	/**
	 * 返回pharos库连接
	 * 
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	public static Connection getPharosConn() throws SQLException, NamingException {
		Connection pharosConn = null;
		Context ctx = AppServerEnv.getInitialContext();
		DataSource ds = (DataSource) ctx.lookup(ResourceBundleUtil.getProperty("pharosjndi"));
		pharosConn = ds.getConnection();
		// pharosPool.add(pharosConn);
		// log.info("PHAROS当前的活动连接数："+getActionConnectionCount(pharosPool));
		return pharosConn;
	}

	public static Connection getTransplantConn() throws SQLException, NamingException {
		Connection pharosConn = null;
		Context ctx = AppServerEnv.getInitialContext();
		DataSource ds = (DataSource) ctx.lookup(ResourceBundleUtil.getProperty("transplant"));
		pharosConn = ds.getConnection();
		// pharosPool.add(pharosConn);
		// log.info("PHAROS当前的活动连接数："+getActionConnectionCount(pharosPool));
		return pharosConn;
	}

	/**
	 * 线程不安全，调试时临时使用
	 * 
	 * @param connList
	 * @return
	 * @throws ClassNotFoundException
	 */
	// private static int getActionConnectionCount(List<Connection> connList) {
	// int count = 0;
	// try {
	// List<Integer> delIndex = new ArrayList<Integer>();
	// int size = connList.size();
	// Connection conn = null;
	// for (int i = 0; i < size; i++) {
	// conn = connList.get(i);
	// if (conn.isClosed() == false) {
	// count++;
	// } else {
	// delIndex.add(i);
	// }
	// }
	// for (Integer i : delIndex) {
	// connList.remove(connList.get(i));
	// }
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// return count;
	// }

}
