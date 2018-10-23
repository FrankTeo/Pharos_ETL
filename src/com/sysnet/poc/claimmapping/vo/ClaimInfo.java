package com.sysnet.poc.claimmapping.vo;

import java.util.List;

import com.sysnet.poc.service.storage.SColumn;

public class ClaimInfo {
	private String code;
	private MappingDescription md;
	private List<SColumn> sl;
	// private String tt;
	private String reportNo;

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	// public String getTt() {
	// return tt;
	// }
	//
	// public void setTt(String tt) {
	// this.tt = tt;
	// }

	public List<SColumn> getSl() {
		return sl;
	}

	public void setSl(List<SColumn> sl) {
		this.sl = sl;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public MappingDescription getMd() {
		return md;
	}

	public void setMd(MappingDescription md) {
		this.md = md;
	}
}
