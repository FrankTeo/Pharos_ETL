package com.sysnet.poc.util;

import javax.naming.InitialContext;

/**
 * 得到当前服务器的运行环境
 * 
 * @author SYSNET
 * 
 */
public class AppServerEnv {
	final public static int SERVER_JBOSS = 0;
	final public static int SERVER_WEBLOGIC = 1;
	private static int currentServer = SERVER_JBOSS;
	static {
		// 系统目前暂时默认如果不是JBOSS，则是WebLogic。可以以后再修改
		String jboss = System.getProperty("jboss.server.home.dir");
		if (jboss == null) {
			currentServer = SERVER_WEBLOGIC;
		} else {
			currentServer = SERVER_JBOSS;
		}
	}

	/**
	 * 得到当前的应用服务器类型
	 */
	public static int getAppType() {
		return currentServer;
	}

	/**
	 * 根据当前的服务器环境，返回对应的InitialContext对象
	 */
	public static InitialContext getInitialContext() {
		InitialContext ctx = null;
		try {
			if (AppServerEnv.getAppType() == AppServerEnv.SERVER_JBOSS) {
				ctx = new InitialContext();
			} else {
				ctx = new InitialContext();
			}
		} catch (Exception ex) {
			return null;
		}
		return ctx;
	}

	public static void main(String[] args) {
		System.out.println("Current Server Name is :" + getAppType());
	}
}
