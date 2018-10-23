/**
 * FileName: Record.java Date: Jul 22, 2009
 *
 * Copyright (c) 1994-2009 北京尚洋信德信息技术股份有限公司 版权所有
 *
 * History:
 *        <p>Jul 22, 2009:此处记录修改历史记录
 *
 */
package com.sysnet.poc.service.storage;

import java.util.List;

/**
 * 描述一行记录信息
 * 
 * @author <a href=huang_y@sysnet.com.cn>huang_y</a>
 * @version 1.0
 */
public interface SColumn {
	/**
	 * 得到当前的表名称
	 * 
	 * @return
	 */
	public String getTableName();

	public void setTableName(String tableName);

	/**
	 * 得到当前行的列对象。此对象描述了列的名称、类型和值
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public List getCells();

	public SColumn clone() throws CloneNotSupportedException;
}
