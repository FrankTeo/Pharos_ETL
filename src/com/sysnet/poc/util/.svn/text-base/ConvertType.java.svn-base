package com.sysnet.poc.util;

import java.sql.Timestamp;

import pharos.framework.base.Money;

import com.sysnet.poc.control.config.Constants;

/**
 * 
 * @author SuiZhiwei 完成Declaration中不确定类型参数的转换
 * @since 2009-07-06
 */
public class ConvertType {
	/**
	 * 转换成String类型
	 * 
	 * @param obj
	 *            Declaration.getValue()获得的对象
	 * @param monyFlag
	 *            Money取值标志
	 * @return 默认""
	 */
	public static String convertString(Object obj, String monyFlag) {
		String tmp = "";
		// 判断对象为空不做任何处理
		if (obj == null) {
			return tmp;
		}
		// 判断对象是否为String类型
		if (obj instanceof String) {
			tmp = (String) obj;
			// 如果对项为true设置为1
			if (tmp.equalsIgnoreCase("true")) {
				tmp = "1";
			} else if (tmp.equalsIgnoreCase("false")) {// 如果对象为false设置为0
				tmp = "0";
			}
		} else if (obj instanceof Money) {// 对象为money类型止返回总额
			Money money = (Money) obj;
			if (monyFlag != null && monyFlag.equalsIgnoreCase(Constants.MONY_FLAG_A)) {
				tmp = String.valueOf(money.getAmount());
			} else if (monyFlag != null && monyFlag.equalsIgnoreCase(Constants.MONY_FLAG_C)) {
				tmp = String.valueOf(money.getCurrency());
			}

		} else if (obj instanceof Boolean) {
			tmp = String.valueOf(obj);
			if (tmp.equalsIgnoreCase("true")) {
				tmp = "1";
			} else if (tmp.equalsIgnoreCase("false")) {// 如果对象为false设置为0
				tmp = "0";
			}
		} else {// 返回对象的String表现形式
			tmp = String.valueOf(obj);
		}
		return tmp;
	}

	/**
	 * 转换成int类型数据
	 * 
	 * @param obj
	 *            Declaration.getValue()获得的对象
	 * @return 默认0
	 */
	public static int convertInt(Object obj) {
		int tmp = 0;
		String str = "";
		if (obj == null) {
			return tmp;
		}
		try {
			if (obj instanceof Integer) {
				tmp = ((Integer) obj).intValue();
			} else if (obj instanceof String) {// 如果对象为String类型
				str = (String) obj;
				if (str.equalsIgnoreCase("true")) {// 为true返回1
					tmp = 1;
				}
				if (str.equalsIgnoreCase("false")) {// 为false返回0
					tmp = 0;
				} else {// 否则转换成int类型数据（此处可能有异常）
					tmp = Integer.parseInt(str);
				}
			} else if (obj instanceof Money) {// 如果对象为Money类型取货币
				Money money = (Money) obj;
				tmp = Integer.parseInt(money.getCurrency());
			} else if (obj instanceof Double) {// 如果对象为float类型截断取整
				str = String.valueOf(obj);
				str = str.split("\\.")[0];
				tmp = Integer.parseInt(str);
			} else if (obj instanceof Float) {// 如果对象为float类型截断取整
				str = String.valueOf(obj);
				str = str.split("\\.")[0];
				tmp = Integer.parseInt(str);
			}
		} catch (NumberFormatException ne) {
			ne.printStackTrace();
			tmp = 0;
		} catch (Exception e) {
			e.printStackTrace();
			tmp = 0;
		}
		return tmp;
	}

	/**
	 * 转换成double类型数据
	 * 
	 * @param obj
	 *            Declaration.getValue()获得的对象
	 * @return 默认0.0
	 */
	public static double convertDouble(Object obj) {
		double tmp = 0.0;
		if (obj == null) {
			return tmp;
		}
		try {
			if (obj instanceof Double) {// 对象为double类型强转
				tmp = ((Double) obj).doubleValue();
			} else if (obj instanceof String) {// 如果对象为String类型直接转（此处可能会抛异常）
				tmp = Double.parseDouble((String) obj);
			} else if (obj instanceof Money) {// 如果对象为Money取总计
				tmp = ((Money) obj).getAmount();
			}
		} catch (NumberFormatException ne) {
			ne.printStackTrace();
			tmp = 0.0;
		} catch (Exception e) {
			e.printStackTrace();
			tmp = 0.0;
		}
		return tmp;
	}

	/**
	 * 将对象转换成Long类型数据
	 * 
	 * @param obj
	 *            Declaration.getValue()获得的对象
	 * @return
	 */
	public static long convertLong(Object obj) {
		long tmp = 0;
		if (obj == null) {
			return tmp;
		}
		try {
			if (obj instanceof Integer) {// 对象为integer类型这个样子转
				tmp = ((Long) obj).intValue();
			} else if (obj instanceof String) {// 对象为String类型这个样子转 (此处可能有异常)
				tmp = Long.parseLong((String) obj);
			}
		} catch (NumberFormatException ne) {
			ne.printStackTrace();
			tmp = 0;
		} catch (Exception e) {
			e.printStackTrace();
			tmp = 0;
		}
		return tmp;
	}

	/**
	 * 转换日期
	 * 
	 * @param obj
	 * @return
	 */
	public static java.sql.Date convertDate(Object obj) {
		java.sql.Date date = null;
		if (obj != null) {
			date = new java.sql.Date(((java.util.Date) obj).getTime());
		}
		return date;
	}

	/**
	 * 将日期转换成Timestamp类型
	 * 
	 * @param obj
	 * @return
	 */
	public static Timestamp convertTimeStamp(Object obj) {
		Timestamp tt = null;
		if (obj != null) {
			tt = new Timestamp(((java.util.Date) obj).getTime());
		}
		return tt;
	}
}
