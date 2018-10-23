package com.sysnet.poc.claimmapping.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 理赔映射描述POJO
 * 
 * @author 段成伟
 * 
 */
public class MappingDescription implements Serializable, Cloneable {

	@Override
	public MappingDescription clone() throws CloneNotSupportedException {
		MappingDescription mappingDescription = new MappingDescription();
		mappingDescription.setDataProvide(this.dataProvide);

		List<Table> tableList = new ArrayList<Table>();
		for (Table _table : this.table) {
			Table _t = _table.clone();
			_t.setParent(mappingDescription);
			tableList.add(_t);
		}

		mappingDescription.setTable(tableList);
		return mappingDescription;
	}

	private static final long serialVersionUID = 1L;

	private List<DataProvide> dataProvide;

	private List<Table> table;

	public List<DataProvide> getDataProvide() {
		return dataProvide;
	}

	public void setDataProvide(List<DataProvide> dataProvide) {
		this.dataProvide = dataProvide;
	}

	public List<Table> getTable() {
		return table;
	}

	public void setTable(List<Table> table) {
		this.table = table;
	}

}
