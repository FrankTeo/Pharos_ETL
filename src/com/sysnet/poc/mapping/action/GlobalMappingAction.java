package com.sysnet.poc.mapping.action;

import java.util.Map;
import com.sysnet.poc.mapping.vo.Item;

/**
 * 全局参数获取类
 * 
 * @author li_yanpeng
 * 
 */
public class GlobalMappingAction extends AbsPharosMappingAction {

	private Item item;

	public boolean execute() {
		boolean rs = false;
		try {
			@SuppressWarnings("unchecked")
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
