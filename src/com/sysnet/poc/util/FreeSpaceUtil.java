package com.sysnet.poc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sysnet.poc.service.dao.dbc.ConnDB;

public class FreeSpaceUtil {
	public static synchronized Boolean isHaveSpace() {
		return true;
//		Integer pro = 0;
//		try {
//			pro = Integer.parseInt(ResourceBundleUtil.readValue("mailpassword"));
//		} catch (Exception e) {
//			pro = 5;
//		}
//		if (pro == 0) {
//			return true;
//		}
//		Connection conn = null;
//		try {
//			conn = ConnDB.getODSConn();
//			PreparedStatement ps = conn.prepareStatement("select f_tablespace_static_freespace(?) from dual");
//			ps.setString(1, ResourceBundleUtil.readValue("toaddress"));
//			ResultSet rs = ps.executeQuery();
//			if (rs.next()) {
//				Integer ts = rs.getInt(1);
//				if (ts > pro) {
//					return true;
//				} else {
//					return false;
//				}
//			} else {
//				return false;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		} finally {
//			try {
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
	}
}
