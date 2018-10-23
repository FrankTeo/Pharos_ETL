package com.sysnet.poc.partymapping.action;

import com.sysnet.poc.partymapping.vo.Item;

/**
 * 所有映射控制类的接口
 * 
 * @author 卢海滨
 * 
 */
public interface MappingAction {

	public void setMappingItem(Item item);

	/**
	 * 在初始化Action的时候，如果当前的Provide是一个多记录，则将对象取出，通过setObject传给 Action,Action首先判断是否有object，如果有，则将此对象转型，并作为当前的Provide对象，不再从BC中取。
	 * 
	 * @param obj
	 */
	public void setProvideInstance(Object obj);

	public boolean execute() throws Exception;

}
