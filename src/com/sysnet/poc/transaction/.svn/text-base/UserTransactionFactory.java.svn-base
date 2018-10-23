package com.sysnet.poc.transaction;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;
import com.sysnet.poc.util.AppServerEnv;

/**
 * JTA事务控制
 * 
 * @author shw
 * 
 */
public class UserTransactionFactory {

	// private static UserTransaction utx = null;

	public static UserTransaction getUserTransaction() {
		UserTransaction utx = null;
		try {
			InitialContext ctx = AppServerEnv.getInitialContext();
			if (AppServerEnv.getAppType() == AppServerEnv.SERVER_WEBLOGIC) {
				utx = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
			} else {
				utx = (UserTransaction) ctx.lookup("UserTransaction");
			}

		} catch (NamingException err) {
			err.printStackTrace();
			return null;
		}
		if (utx == null) {
			System.out.println("[UserTransactionFactory]UserTransaction is null!");
		}
		return utx;
	}
}
