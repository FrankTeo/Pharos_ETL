package com.sysnet.poc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import com.sysnet.poc.mapping.vo.Item;
import com.sysnet.poc.service.dao.dbc.ConnDB;
import com.sysnet.poc.service.storage.SCell;
import com.sysnet.poc.service.storage.SColumn;

/**
 * Feb 11, 2011
 * 
 * @author DBC
 */
public class RemoveDataUtil {

	@SuppressWarnings("unchecked")
	public static List<SColumn> remove(List<SColumn> sColumnL, String key) throws SQLException, CloneNotSupportedException, NumberFormatException, NamingException, ClassNotFoundException {
		Connection odsConn = ConnDB.getODSConnJ();
		List<SColumn> rl = new ArrayList<SColumn>();
		for (int i = sColumnL.size() - 1; i > -1; i--) {
			String sql1 = "select * from " + sColumnL.get(i).getTableName() + " where ObjectNo =?";
			String sql3 = "delete from " + sColumnL.get(i).getTableName() + " where ObjectNo =?";
			String sql2 = "select Max(To_Number(Versionno)) from " + sColumnL.get(i).getTableName() + "log where ObjectNo ='" + key + "'";
			PreparedStatement pm1 = odsConn.prepareStatement(sql1);
			pm1.setString(1, key);
			Statement sm2 = odsConn.createStatement();
			// Statement sm3 = odsConn.createStatement();
			ResultSet rs1 = pm1.executeQuery();

			String versionNo = "1";
			boolean b = true;
			boolean c = false;
			while (rs1.next()) {
				c = true;
				if (b) {
					ResultSet rs2 = sm2.executeQuery(sql2);
					if (rs2.next()) {
						versionNo = rs2.getInt(1) + "";
						versionNo = (Integer.parseInt(versionNo) + 1) + "";
						b = false;
					}
					rs2.close();
				}
				SColumn sc = sColumnL.get(i).clone();
				sc.setTableName(sc.getTableName() + "log");
				List<SCell> sCellL = sc.getCells();
				SCell s = new Item();
				s.setFieldName("VersionNo");
				s.setFieldType("Long");
				s.setValue(versionNo);
				for (SCell cell : sCellL) {
					if (rs1.getObject(cell.getName()) != null) {
						if (cell.getType().equals("Long")) {
							cell.setValue(rs1.getLong(cell.getName()) + "");
						} else if (cell.getType().equals("Double")) {
							cell.setValue(rs1.getDouble(cell.getName()) + "");
						} else if (cell.getType().equals("String")) {
							cell.setValue(rs1.getString(cell.getName()));
						} else if (cell.getType().equals("Date")) {
							cell.setValue(DateHelper.convertEnDateToCnDateTime(String.valueOf(rs1.getTimestamp(cell.getName()))));
						}
					} else {
						cell.setValue(null);
					}
				}
				sCellL.add(s);
				rl.add(sc);
			}
			rs1.close();

			pm1.close();
			sm2.close();
			if (c) {
				PreparedStatement ps = odsConn.prepareStatement(sql3);
				ps.setString(1, key);
				ps.executeUpdate();
				ps.close();
			}
		}

		List<SColumn> temp = new ArrayList<SColumn>();
		for (int i = rl.size() - 1; i > -1; i--) {
			temp.add(rl.get(i));
		}
		return temp;
	}

	public static void delete(List<SColumn> sColumnL, String key, Connection odsConn) throws SQLException {
		// Statement sm = odsConn.createStatement();

		for (int i = sColumnL.size() - 1; i > -1; i--) {
			String sql = "delete from " + sColumnL.get(i).getTableName() + " where ObjectNo=?";
			// System.out.println(sql);

			PreparedStatement ps = odsConn.prepareStatement(sql);

			// System.out.println("表名:" + sColumnL.get(i).getTableName());

			ps.setString(1, key);
			ps.executeUpdate();
			//System.out.println( ps.executeUpdate() + " row(s) was Deleted");
			ps.close();
		}
	}

	/**
	 * 删除旧保单数据
	 * 
	 * @throws SQLException
	 */
	public static void deleteOldData() throws Exception {
		Connection odsConn = ConnDB.getODSConnJ();
		// 把需要更新的 object_id 插入 tmp_object_id 表中
		System.out.println("开始处理要更新的数据");
		PreparedStatement ps = null;
		String sqlDel_1 = "truncate table tmp_contractno";
		String sqlDel_2 = "truncate table tmp_object_id";
		String sql_contractno = "insert into tmp_contractno SELECT distinct contract_no FROM htbiz_new.etl_pharos_incr_insure t where t.stage_write_flag = '3'";
		String sql_insertid = "INSERT INTO htbiz_ods.tmp_object_id  SELECT object_id FROM htbiz_new.etl_pharos_incr_insure t,htbiz_ods.tmp_contractno a WHERE t.contract_no = a.contractno";
		ps = odsConn.prepareStatement(sqlDel_1);
		ps.execute();
		ps = odsConn.prepareStatement(sqlDel_2);
		ps.execute();
		ps = odsConn.prepareStatement(sql_contractno);
		ps.execute();
		ps = odsConn.prepareStatement(sql_insertid);
		ps.execute();

		String object_ids = "select object_id from htbiz_ods.tmp_object_id";
		
		if (getObjectIDcounts() == 0) {
			ps.close();
			odsConn.close();
			System.out.println("没有新增量！");
		} else {
			if (getObjectIDcounts() < 1000) {
				object_ids = resultToStr();
			}
			// 删除旧数据
			String[] tableName = { 
					"T_SubjAgri",
					"T_SubjProfitLoss",
					"T_ReExpensive",
					"T_SubjShip",
					"T_SubjFreight",
					"T_PersonList",
					"T_SubjDuty",
					"T_SubjProductDuty",
					"T_PropertyList",
					"T_ProjectParty",
					"T_AcquisitionCost",
					"T_PolicyUnderWrite",
					"T_SubjEmployerDuty",
					"T_PolicyAgency",
					"T_JoInsurance",
					"T_Liability",
					"T_SubjProject",
					"T_AcquisitionCostDetail",
					"T_PolicyApplicant",
					"T_SubjTrafficDuty",
					"T_SubjAircraftDuty",
					"T_Policy",
					"T_PayPlan",
					"T_ReinsureInfo",
					"T_Node",
					"T_SubjGuarantee",
					"T_PolicyContact",
					"T_Endorse",
					"T_SubjEconomic",
					"T_PolicyBeneficiary",
					"T_SubjMarketDuty",
					"T_PolicyInsure",
					"T_SubjEnterprise",
					"T_SubjFreightForward",
					"T_CoInsurance",
					"T_SubjMedLiab",
					"T_Subject",
					"T_Contract",
					"T_CRM_Policy",
					"T_CRM_PayPlan"
					};

			for (int i = 0; i < tableName.length; i++) {

				String sql = "DELETE FROM " + tableName[i] + " WHERE objectno in (" + object_ids + ")";
				System.out.println(sql);
				ps = odsConn.prepareStatement(sql);
				ps.execute();
			}
			// 更新增量标志
			String sql_update = "update htbiz_new.etl_pharos_incr_insure t set t.bat_id=0,t.stage_write_flag=0 where t.object_id in (" + object_ids + ")";
			ps = odsConn.prepareStatement(sql_update);
			ps.execute();
			ps.close();
			odsConn.close();
			System.out.println("更新增量完毕！");
		}
	}

	private static String resultToStr() throws Exception {
		Connection odsConn = ConnDB.getODSConnJ();
		ResultSet rs = null;
		String sql_object_ids = "SELECT object_id from htbiz_ods.tmp_object_id";
		Statement stmt = odsConn.createStatement();
		rs = stmt.executeQuery(sql_object_ids);

		StringBuilder str_obj_id = new StringBuilder();
		str_obj_id.append("'");
		while (rs.next()) {
			str_obj_id.append(rs.getString(1));
			str_obj_id.append("','");
		}
		stmt.close();
		odsConn.close();
		return str_obj_id.substring(0, str_obj_id.length() - 2);
	}

	private static int getObjectIDcounts() throws Exception {

		int res = 0;
		Connection odsConn = ConnDB.getODSConnJ();
		ResultSet rs = null;
		Statement stmt = odsConn.createStatement();
		String sql_counts = "select count(*) from htbiz_ods.tmp_object_id";
		rs = stmt.executeQuery(sql_counts);
		if (rs.next()) {
			res = rs.getInt(1);
		}

		stmt.close();
		odsConn.close();
		return res;
	}

}
