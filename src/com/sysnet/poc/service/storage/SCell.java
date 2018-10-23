/**
 * FileName: Column.java Date: Jul 22, 2009
 *
 * Copyright (c) 1994-2009 北京尚洋信德信息技术股份有限公司 版权所有
 *
 * History:
 *        <p>Jul 22, 2009:此处记录修改历史记录
 *
 */
package com.sysnet.poc.service.storage;

/**
 * 描述一个字段信息
 * 
 * @author <a href=huang_y@sysnet.com.cn>huang_y</a>
 * @version 1.0
 */
public abstract class SCell {

	@Override
	public String toString() {
		return getName() + ":" + getValue().length() + ":" + getValue();
	}

	/**
	 * 得到当前的字段名称
	 * 
	 * @return
	 */
	public abstract String getName();

	public abstract void setFieldName(String fieldName);

	/**
	 * 得到当前的字段类型
	 * 
	 * @return
	 */
	public abstract String getType();

	public abstract void setFieldType(String fieldType);

	/**
	 * 得到当前字段的值
	 * 
	 * @return
	 */
	public abstract String getValue();

	public abstract void setValue(String value);
}
