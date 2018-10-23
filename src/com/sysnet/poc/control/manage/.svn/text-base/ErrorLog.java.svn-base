package com.sysnet.poc.control.manage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.sysnet.poc.service.dao.RunLogDAO;
import com.sysnet.poc.util.ResourceBundleUtil;
import com.sysnet.poc.vo.Select;
import com.sysnet.poc.vo.StgRunLog;

/**
 * 错误日志功能，但是由于POCManager这个类不再使用
 * 
 * @author cwduan
 * 
 */
public class ErrorLog {
	private static boolean b = false;
	public static List<StgRunLog> errList = null;
	public static List<StgRunLog> baseList = null;
	public static List<StgRunLog> selectList = null;
	public static int i = 0;
	public static int total = 0;
	private static Select s = null;

	public String print() {
		List<StgRunLog> l = null;
		if (s == null) {
			l = baseList;
		} else {
			l = selectList;
		}
		File f = new File(ResourceBundleUtil.getProperty("printPath"));
		if (!f.isDirectory()) {
			f.mkdirs();
		}
		StringBuilder sb = new StringBuilder();
		f = new File(ResourceBundleUtil.getProperty("printPath") + "/" + ResourceBundleUtil.getProperty("printName") + ".csv");
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "GBK"));
			for (StgRunLog stgRunLog : l) {
				sb.append("\"LOG_ID:" + stgRunLog.getLogId() + "\",");
				sb.append("\"OBJECT_ID:" + stgRunLog.getObjectId() + "\",");
				sb.append("\"OBJECT_NO:" + stgRunLog.getObjectNo() + "\",");
				sb.append("\"ERROR_CODE:" + stgRunLog.getErrorCode() + "\",");
				sb.append("\"TIME_STAMP:" + stgRunLog.getTimeStamp() + "\",");
				sb.append("\"ERROR_MESSAGE:" + stgRunLog.getErrorMessage() + "\"");
				sb.append("\r\n");
			}
			bw.write(sb.toString());
			bw.close();
			l = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public void select() {
		if (s.getB()) {
			i = 0;
			selectList = new ArrayList<StgRunLog>();
			for (StgRunLog srl : baseList) {
				String format = "yyyy-MM-dd HH:mm:ss";
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				try {
					Long temp = sdf.parse(srl.getTimeStamp()).getTime();
					if ((s.getObjectNo() == null ? true : s.getObjectNo().equals(srl.getObjectNo())) && (s.getObjectId() == null ? true : s.getObjectId().equals(srl.getObjectId())) && (s.getErrorCode() == null ? true : s.getErrorCode().equals(srl.getErrorCode())) && (s.getStartTime() == null ? true : s.getStartTime() < temp) && (s.getEndTime() == null ? true : s.getEndTime() > temp)) {
						selectList.add(srl);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			part(0, selectList);
			getMax(selectList);
		} else {
			reset();
			i = 0;
			part(0, baseList);
			getMax(baseList);
			s = null;
		}
	}

	private static void getMax(List<StgRunLog> l) {
		if (l.size() % 10 == 0) {
			total = l.size() / 10;
		} else {
			total = l.size() / 10 + 1;
		}
		if (i > total) {
			i = total;
		}
	}

	public void reset() {
		RunLogDAO rldao = new RunLogDAO();
		baseList = rldao.selectAll();
		getMax(baseList);
		part(0, baseList);
		selectList = null;
	}

	public void part(Integer d, List<StgRunLog> l) {
		Integer temp = (d + 1) * 10;
		if (l.size() < temp) {
			temp = l.size();
		}
		errList = new ArrayList<StgRunLog>();
		for (int j = d * 10; j < temp; j++) {
			errList.add(l.get(j));
		}
	}

	public void part(Integer d) {
		List<StgRunLog> l = null;
		if (s == null) {
			l = baseList;
		} else {
			l = selectList;
		}
		Integer temp = (d + 1) * 10;
		if (l.size() < temp) {
			temp = l.size();
		}
		errList = new ArrayList<StgRunLog>();
		for (int j = d * 10; j < temp; j++) {
			errList.add(l.get(j));
		}
	}

	public boolean elUpdate(String id) {
		RunLogDAO rldao = new RunLogDAO();
		boolean t = rldao.update(id);
		StgRunLog temp = null;
		for (StgRunLog srl : errList) {
			if (id.equals(srl.getLogId())) {
				temp = srl;
			}
		}
		if (baseList != null) {
			baseList.remove(temp);
		}
		if (errList != null) {
			errList.remove(temp);
		}
		if (selectList != null) {
			selectList.remove(temp);
		}

		if (s == null) {
			getMax(baseList);
			if (errList.size() == 0) {
				if ((i - 1) < 0) {
					part(0, baseList);
					i = 0;
				} else {
					part(i - 1, baseList);
					i = i - 1;
				}
			}
		} else {
			getMax(selectList);
			if (errList.size() == 0) {
				if ((i - 1) < 0) {
					part(0, selectList);
					i = 0;
				} else {
					part(i - 1, selectList);
					i = i - 1;
				}
			}
		}
		return t;
	}

	public static boolean isB() {
		return b;
	}

	public static void setB(boolean b) {
		ErrorLog.b = b;
	}

	public static int getI() {
		return i;
	}

	public static void setI(int i) {
		ErrorLog.i = i;
	}

	public static int getTotal() {
		return total;
	}

	public static void setTotal(int total) {
		ErrorLog.total = total;
	}

	public static Select getS() {
		return s;
	}

	public static void setS(Select s) {
		ErrorLog.s = s;
	}

}
