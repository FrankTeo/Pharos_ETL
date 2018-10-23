package com.sysnet.poc.claimmapping.action;

import java.util.Map;

/**
 * global处理类
 * 
 * @author DBC
 * 
 */
public class GLOBALAction extends AbsClaimAction {

	@SuppressWarnings("unchecked")
	public boolean execute() {
		boolean rs = false;
		try {
			Map<String, String> map = (Map<String, String>) this.provideInstance;
			item.setValue(map.get(item.getParameter()));
			rs = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

}
