package com.sysnet.poc.claimmapping.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sysnet.poc.control.manage.vo.EtlPharosIncrClaim;

public class ThreadCatchClaim {
	private static final Log log = LogFactory.getLog(ThreadCatchClaim.class);;
	private List<EtlPharosIncrClaim> el = new ArrayList<EtlPharosIncrClaim>();

	public List<EtlPharosIncrClaim> getEl() {
		return el;
	}

	public void setEl(List<EtlPharosIncrClaim> el) {
		this.el = el;
	}

	public synchronized EtlPharosIncrClaim getTask() {
		EtlPharosIncrClaim eti = null;
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
