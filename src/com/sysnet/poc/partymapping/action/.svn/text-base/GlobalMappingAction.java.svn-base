package com.sysnet.poc.partymapping.action;

import java.util.Map;
import com.sysnet.poc.partymapping.vo.Item;

/**
 * 全局参数获取类
 * 
 * @author lu_haibin
 * 
 */
public class GlobalMappingAction extends AbsPharosMappingAction {

	private Item item;

	@SuppressWarnings("unchecked")
	public boolean execute() {
		boolean rs = false;
		try {
			Map<String, String> map = (Map<String, String>) this.provideInstnace;
			item.setValue(map.get(item.getParameter()));
			rs = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	public void setMappingItem(Item item) {
		this.item = item;
	}

}
