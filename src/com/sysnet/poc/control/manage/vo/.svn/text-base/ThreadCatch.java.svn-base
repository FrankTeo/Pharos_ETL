package com.sysnet.poc.control.manage.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ThreadCatch {
	private static final Log log = LogFactory.getLog(ThreadCatch.class);;
	private List<EveryThreadInfo> el = new ArrayList<EveryThreadInfo>();

	public List<EveryThreadInfo> getEl() {
		return el;
	}

	public void setEl(List<EveryThreadInfo> el) {
		this.el = el;
	}

	public synchronized EveryThreadInfo getTask() {
		EveryThreadInfo eti = null;
		if (el != null && el.size() >= 1) {
			eti = el.get(0);
			el.remove(eti);
		}
		log.debug("-------------after get task el:size:" + el.size());
		return eti;
	}

	public synchronized void clear() {
		el.clear();
		log.debug("-------------after clear el:size:" + el.size());
	}
}
