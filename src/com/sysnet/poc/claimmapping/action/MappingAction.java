package com.sysnet.poc.claimmapping.action;

import com.sysnet.poc.claimmapping.vo.Item;

/**
 * 处理类接口
 * 
 * @author DBC
 * 
 */
public interface MappingAction {
	public void setMappingItem(Item item);

	public void setProvideInstance(Object obj);

	public boolean execute() throws Exception;
}
