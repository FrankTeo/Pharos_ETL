package com.sysnet.poc.partymapping.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.sysnet.poc.service.storage.SColumn;

/**
 * 目标表POJO
 * 
 * @author lu_haibin
 * 
 */
public class Table implements Serializable, Cloneable, SColumn {

	private static final long serialVersionUID = 1L;

	private String tableName;// 目标表名称

	private String dataProvide;// 数据提供者

	private List<Item> items = new ArrayList<Item>();// 该表的字段

	private MappingDescription parent;// 父

	private String as;// 主表别名

	private String dependent;// 从表依赖那个主表(别名 )

	public String getAs() {
		return as;
	}

	public void setAs(String as) {
		this.as = as;
	}

	public String getDependent() {
		return dependent;
	}

	public void setDependent(String dependent) {
		this.dependent = dependent;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDataProvide() {
		return dataProvide;
	}

	public void setDataProvide(String dataProvide) {
		this.dataProvide = dataProvide;
	}

	public MappingDescription getParent() {
		return parent;
	}

	public void setParent(MappingDescription parent) {
		this.parent = parent;
	}

	public Table clone() throws CloneNotSupportedException {
		Table table = new Table();
		table.setTableName(this.tableName);
		table.setDataProvide(this.dataProvide);
		table.setParent(this.parent);
		table.setAs(this.as);
		table.setDependent(this.dependent);

		List<Item> list = new ArrayList<Item>();

		for (Item item : this.items) {
			Item _item = item.clone();
			_item.setParent(table);
			list.add(_item);
		}

		table.setItems(list);

		return table;
	}

	public List<Item> getCells() {
		return this.items;
	}

}
