/**
 * FileName: ParseColumn.java Date: Jul 22, 2009
 *
 * Copyright (c) 1994-2009 北京尚洋信德信息技术股份有限公司 版权所有
 *
 * History:
 *        <p>Jul 22, 2009:此处记录修改历史记录
 *
 */
package com.sysnet.poc.service.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import com.sysnet.poc.mapping.FieldType;
import com.sysnet.poc.util.DateHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 此对象通过分析Column，组织相应的SQL，并返回SQL或Statement对象给调用者。
 * 
 * @author <a href=huang_y@sysnet.com.cn>huang_y</a>
 * @version 1.0
 */
public class ParseSColumn {
	private SColumn column = null;
	private static final Log log = LogFactory.getLog(ParseSColumn.class);
	/**
	 * 得到封装后的SQL语句
	 * 
	 * @return
	 */
	public String getSQL() {
		StringBuffer sb = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
		sb.append("INSERT INTO ");
		sb.append(this.column.getTableName());
		sb.append(" (");
		@SuppressWarnings("unchecked")
		List<SCell> cells = this.column.getCells();
		int size = cells.size();
		for (int i = 0; i < size; i++) {
			SCell cell = cells.get(i);
			sb.append(cell.getName());
			sb1.append("?");
			if (i < size - 1) {
				sb.append(",");
				sb1.append(",");
			}
		}
		sb.append(") VALUES (");
		sb.append(sb1);
		sb.append(")");
		return sb.toString();
	}

	/**
	 * 得到封装后的PreparedStatement对象
	 * 
	 * @return
	 */
	public PreparedStatement getPreparedStatement(Connection conn) {
		String sql = "";
		PreparedStatement pstmt = null;
		try {
			sql = this.getSQL();
//			System.out.println("PaserSColumn.class 输出 sql = " + sql);
			pstmt = conn.prepareStatement(sql);
			@SuppressWarnings("unchecked")
			List<SCell> cells = this.column.getCells();
			int count = 1;
			for (SCell cell : cells) {
//				if(sql.contains("t_policy" )){
//					System.out.print(cell.getType() + " " + cell.getName() + " " + cell.getValue());
//				}
				String type = cell.getType();
				String value = cell.getValue();
				
//				//相关编号内容过长，超过表中字段的长度，截取到60个字符
//				String name = cell.getName();
//				if("RelateNo".equalsIgnoreCase(name) && value!=null){
//					value = value.substring(0,60);
//				}
				
				if(type == null){
					log.info("Cell Type is null，Cell name:" + cell.getName());
				}
				if (type.equalsIgnoreCase(FieldType.FIELDTYPE_STRING)) {
					if (value != null && !value.equals("null")) {
						if (value.trim().equalsIgnoreCase("true")) {
							value = "1";
						} else if (value.trim().equalsIgnoreCase("false")) {
							value = "0";
						} else {
							byte[] bs = value.trim().getBytes("UTF-8");
							if (bs.length > 4000) {
								// value = new String(bs, "UTF-8");
								// bs = value.trim().getBytes("UTF-8");
								byte[] by = new byte[4000];
								for (int i = 0; i < by.length; i++) {
									by[i] = bs[i];
								}
								value = new String(by, "UTF-8");
							}
						}
						pstmt.setString(count, value.trim());
					} else {
						pstmt.setObject(count, null);
					}

				} else if (type.equalsIgnoreCase(FieldType.FIELDTYPE_DATE)) {
					// lyp added begin
					String format = "yyyy-MM-dd HH:mm:ss.SSS";
					SimpleDateFormat sdf = this.getSimpleDateFormat(format);
					java.util.Date utilDate = new java.util.Date();
					if( "0".equals(value)) {	//zhangfan 20140516 set a default for the migration data which is un-parsable
						value = "1900-01-01";
					}
					if (value != null && !value.equals("null") && !value.equals("")) {
						try {
							value = DateHelper.convertEnDateToCnDateTime(value);
							if (value.length() == 10) {
								value = value + " 00:00:00.000";
							}
							utilDate = sdf.parse(value);
						} catch (Exception tex) {
							System.err.println(cell.getName() + " has Error!");
							tex.printStackTrace();
						}

						String TIME_STAMP = sdf.format(utilDate);
						// log.info("TIME_STAMP:"+TIME_STAMP);
						java.sql.Date sqlDate = new java.sql.Date(sdf.parse(TIME_STAMP).getTime());
						// lyp added end
						Timestamp t = new Timestamp(sqlDate.getTime());
						// log.info("TIME_STAMP============"+TIME_STAMP);
						// log.info("t====================="+t);
						pstmt.setTimestamp(count, t);
					} else {
						pstmt.setTimestamp(count, null);
					}

				} else if (type.equalsIgnoreCase(FieldType.FIELDTYPE_DOUBLE)) {
					if (value == null || value.equals("null") || value.equals("")) {
						pstmt.setObject(count, null);
					} else {
						pstmt.setDouble(count, Double.valueOf(value));
					}
				} else if (type.equalsIgnoreCase(FieldType.FIELDTYPE_LONG)) {
					if (value != null && !value.equals("null") && !"".equals(value)) {
						if (value.trim().equalsIgnoreCase("true")) {
							value = "1";
						} else if (value.trim().equalsIgnoreCase("false")) {
							value = "0";
						} else if (value.indexOf(".") != -1) {// update by
							// lu_haibin
							// 2009-11-05

							value = Double.valueOf(value).longValue() + "";

						}
						pstmt.setLong(count, Long.valueOf(value));
					} else {
						pstmt.setObject(count, null);
					}
				} else {
					pstmt.setObject(count, value);
				}

				count++;
			}
		} catch (Exception ex) {
			log.error(sql);
			ex.printStackTrace();
			return null;
		}
		return pstmt;
	}

	/**
	 * 设置当前对象需要处理的record对象实例。
	 * 
	 * @param record
	 */
	public void setColumn(SColumn column) {
		this.column = column;
	}

	/**
	 * 获取时间格式类
	 * 
	 * @param format
	 * @return
	 */
	private SimpleDateFormat getSimpleDateFormat(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf;
	}
//
//	/**
//	 * 测试
//	 * 
//	 * @param args
//	 */
//	public static void main(String[] args) {
//
//	}

}
