package com.sysnet.poc.partymapping.vo;

import java.io.Serializable;

import com.sysnet.poc.service.storage.SCell;

/**
 * 目标字段POJO
 * 
 * @author lu_haibin
 * 
 */
public class Item extends SCell implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fieldName;// 目标字段名称

	private String fieldType;// 目标字段类型

	private String parameter;// 参数

	private String value;// 值

	private Table parent;// 父表

	private String dataProvider;// 数据提供者

	private String ref;// 引用其他字段

	private String sequenceName;// 自增长序列名称

	private String parameterType;// 参数类型

	private String method;// 方法

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getSequenceName() {
		return sequenceName;
	}

	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}

	public String getParameterType() {
		return parameterType;
	}

	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Table getParent() {
		return parent;
	}

	public void setParent(Table parent) {
		this.parent = parent;
	}

	public String getName() {
		return this.getFieldName();
	}

	public String getType() {
		return this.getFieldType();
	}

	public String getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(String dataProvider) {
		this.dataProvider = dataProvider;
	}

	public Item clone() {
		Item item = new Item();
		item.setDataProvider(this.dataProvider);
		item.setFieldName(this.fieldName);
		item.setFieldType(this.fieldType);
		item.setParameter(this.parameter);
		item.setParameterType(this.parameterType);
		item.setParent(this.parent);
		item.setRef(this.ref);
		item.setSequenceName(this.sequenceName);
		item.setValue(this.value);
		item.setMethod(this.method);
		return item;
	}

}