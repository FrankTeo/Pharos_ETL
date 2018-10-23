package com.sysnet.poc.control.manage.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sysnet.poc.service.storage.SColumn;

public class ThreadCatchDB {
	private Boolean isAllRight = true;
	private Integer i = 0;
	private String errorMessage;

	public synchronized void add() {
		i = getI() + 1;
	}

	public Boolean getIsAllRight() {
		return isAllRight;
	}

	public void setIsAllRight(Boolean isAllRight) {
		this.isAllRight = isAllRight;
	}

	public synchronized Integer getI() {
		return i;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	private static final Log log = LogFactory.getLog(ThreadCatchDB.class);
	private List<SColumn> el = new ArrayList<SColumn>();

	public List<SColumn> getEl() {
		return el;
	}

	public void setEl(List<SColumn> el) {
		this.el = el;
	}

	public synchronized SColumn getTask() {
		SColumn eti = null;
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
