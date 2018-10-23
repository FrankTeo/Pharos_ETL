package com.sysnet.poc.control.manage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sysnet.poc.control.manage.vo.EtlPharosIncrClaim;

public class FilterIncData {
	private List<EtlPharosIncrClaim> batake = new ArrayList<EtlPharosIncrClaim>();// 去集合
	private List<EtlPharosIncrClaim> remain = new ArrayList<EtlPharosIncrClaim>();// 留集合

	public void filter(List<EtlPharosIncrClaim> list) {
		Collections.sort(list);
		Set<EtlPharosIncrClaim> s = new HashSet<EtlPharosIncrClaim>(list);
		remain = new ArrayList<EtlPharosIncrClaim>(s);
		for (EtlPharosIncrClaim etlPharosIncrClaim : s) {
			list.remove(etlPharosIncrClaim);
		}
		batake = list;
	}

	/**
	 * 返回去集合
	 * 
	 * @return
	 */
	public List<EtlPharosIncrClaim> getBatake() {
		return batake;
	}

	/**
	 * 返回留集合
	 * 
	 * @return
	 */
	public List<EtlPharosIncrClaim> getRemain() {
		return remain;
	}

}
