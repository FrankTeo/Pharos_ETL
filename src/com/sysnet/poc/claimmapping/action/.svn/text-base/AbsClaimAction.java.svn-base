package com.sysnet.poc.claimmapping.action;

import com.sysnet.poc.claimmapping.vo.Item;

/**
 * 具体Action的父类，并且是默认处理类
 * 
 * @author Administrator
 * 
 */
public class AbsClaimAction implements MappingAction {
	protected Object provideInstance;
	protected Item item;

	public void setProvideInstance(Object obj) {
		this.provideInstance = obj;
	}

	public void setMappingItem(Item item) {
		this.item = item;
	}

	public boolean execute() throws Exception {
		boolean rs = false;
		item.setValue(null);
		return rs;
	}

}
