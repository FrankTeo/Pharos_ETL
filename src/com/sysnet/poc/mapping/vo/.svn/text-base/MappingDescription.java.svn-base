package com.sysnet.poc.mapping.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 映射描述POJO
 * 
 * @author li_yanpeng
 * 
 */
public class MappingDescription implements Serializable, Cloneable {

	@Override
	public MappingDescription clone() throws CloneNotSupportedException {
		MappingDescription mappingDescription = new MappingDescription();
		mappingDescription.setDataProvide(this.dataProvide);
		mappingDescription.setProductCode(this.productCode);
		mappingDescription.setProductNodeCode(this.productNodeCode);
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

	private String productCode;

	private String productNodeCode;

	public List<DataProvide> getDataProvide() {
		return this.dataProvide;
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

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductNodeCode() {
		return productNodeCode;
	}

	public void setProductNodeCode(String productNodeCode) {
		this.productNodeCode = productNodeCode;
	}

}
