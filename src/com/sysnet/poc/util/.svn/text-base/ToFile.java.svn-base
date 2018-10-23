package com.sysnet.poc.util;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sysnet.poc.mapping.vo.Item;
import com.sysnet.poc.mapping.vo.Table;

public class ToFile {
	private Map<String, List<Table>> mlt = new HashMap<String, List<Table>>();

	public void write(List<Table> tl, String id) throws Exception {
		initMap(tl);
		Set<String> ks = mlt.keySet();
		String path = ResourceBundleUtil.readValue("insureLog4j");
		// String pathSymbol = path.substring(path.length() - 17, path.length() - 16);
		path = path.substring(0, path.length() - 16);
		for (String string : ks) {
			File fileDir = new File(path + id + File.separator);
			if (!fileDir.isDirectory()) {
				fileDir.mkdirs();
			}
			File file = new File(path + id + File.separator + string + ".txt");
			if (!file.isFile()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file, true);
			List<Table> tlf = mlt.get(string);
			for (Table table : tlf) {
				fw.write(changeToSql(table));
			}
			fw.close();
		}
	}

	private String changeToSql(Table t) {
		StringBuffer sb = new StringBuffer();
		StringBuffer c = new StringBuffer();
		StringBuffer v = new StringBuffer();
		sb.append("insert into " + t.getTableName() + " (");
		List<Item> il = t.getItems();
		for (Item item : il) {
			if (item.getValue() == null || item.getValue().equals("")) {
				continue;
			}
			c.append(item.getFieldName() + ",");
			if (item.getFieldType().equalsIgnoreCase("date")) {
				v.append("to_timestamp('" + item.getValue() + "','yyyy-mm-dd hh24:mi:ssxff'),");
			} else if (item.getFieldType().equalsIgnoreCase("Long")) {
				v.append(item.getValue() + ",");
			} else if (item.getFieldType().equalsIgnoreCase("String")) {
				v.append("'" + item.getValue().replaceAll("'", "") + "'" + ",");
			} else if (item.getFieldType().equalsIgnoreCase("Double")) {
				v.append(item.getValue() + ",");
			}
		}
		sb.append(c.deleteCharAt(c.length() - 1) + ") VALUES (" + v.deleteCharAt(v.length() - 1) + ");" + System.getProperty("line.separator"));
		return sb.toString();
	}

	private void initMap(List<Table> tl) {
		for (Table table : tl) {
			List<Table> tlm = null;
			if (mlt.containsKey(table.getTableName()) && (mlt.get(table.getTableName()) != null)) {
				tlm = mlt.get(table.getTableName());
			} else {
				tlm = new ArrayList<Table>();
				mlt.put(table.getTableName(), tlm);
			}
			tlm.add(table);
		}
	}
}
