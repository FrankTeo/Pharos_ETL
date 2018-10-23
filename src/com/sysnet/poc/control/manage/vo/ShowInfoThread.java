package com.sysnet.poc.control.manage.vo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sysnet.poc.service.dao.dbc.ConnDB;
import com.sysnet.poc.util.ResourceBundleUtil;

public class ShowInfoThread extends Thread {

	private Connection pharosConn;

	public Connection getPharosConn() {
		return pharosConn;
	}

	public void setPharosConn(Connection pharosConn) {
		this.pharosConn = pharosConn;
	}

	private ShowInfoMessage sim;

	public ShowInfoMessage getSim() {
		return sim;
	}

	public void setSim(ShowInfoMessage sim) {
		this.sim = sim;
	}

	@Override
	public void run() {
		while (true) {
			try {
				if (pharosConn == null || pharosConn.isClosed()) {
					pharosConn = ConnDB.getPharosConnJ();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (sim.getTableName() != null || sim.getTableName() != "") {
				ResultSet rs = null;
				Statement sm = null;
				try {
					String tableName = sim.getTableName() + ResourceBundleUtil.readValue("riskNodeThreads");
					// String sqlTotal = "select count(1) from " + sim.getTableName() + " t where t.bat_id>-1 or t.bat_id is null";
					String sqlTotal = "select count(1) from " + tableName + " t where t.bat_id>-1";

					String sqlNow = "select count(1) from " + tableName + " t where t.bat_id>-1 and t.stage_write_flag >0";
					// String sqlError = "select count(1) from " + sim.getTableName() + " t where (t.bat_id>-1 or t.bat_id is null) and t.stage_write_flag =2";
					String sqlError = "select count(1) from " + tableName + " t where t.bat_id>-1 and t.stage_write_flag =2";
					sim.getProcessL().clear();
					InsideProcess ip = new InsideProcess();
					sim.getProcessL().add(ip);
					String sqlIpTotal = "select count(1) from " + tableName + " t where t.bat_id>-1 and t.bat_id=(select max(b.bat_id) from " + tableName + " b)";
					String sqlIpNow = "select count(1) from " + tableName + " t where t.bat_id>-1 and t.bat_id=(select max(b.bat_id) from " + tableName + " b) and t.stage_write_flag >0";
					sm = pharosConn.createStatement();
					rs = sm.executeQuery(sqlTotal);
					if (rs.next()) {
						sim.setTotal(rs.getLong(1));
					}
					rs = sm.executeQuery(sqlNow);
					if (rs.next()) {
						sim.setNow(rs.getLong(1));
					}
					rs = sm.executeQuery(sqlError);
					if (rs.next()) {
						sim.setError(rs.getLong(1));
					}
					rs = sm.executeQuery(sqlIpTotal);
					if (rs.next()) {
						ip.setTotal(rs.getInt(1));
					}
					rs = sm.executeQuery(sqlIpNow);
					if (rs.next()) {
						ip.setNow(rs.getInt(1));
					}
				} catch (Exception e) {
					// e.printStackTrace();
				} finally {
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					if (sm != null) {
						try {
							sm.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					// if (pharosConn != null) {
					// try {
					// pharosConn.close();
					// } catch (SQLException e) {
					// e.printStackTrace();
					// }
					// }
				}
			}
			try {
				sleep(600000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
