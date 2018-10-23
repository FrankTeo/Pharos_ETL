package com.sysnet.poc.control.manage.vo;

import java.sql.Connection;
import java.util.List;

import com.sysnet.poc.service.dao.dbc.ConnDB;

/**
 * Dec 13, 2010
 * 
 * @author Administrator
 */
public class ShowInfo {

	public ShowInfo() {
		try {
			pharosConn = ConnDB.getPharosConnJ();
		} catch (Exception e) {
		}
	}

	public void run() {
		sit.setPharosConn(pharosConn);
		sit.setSim(sim);
		sit.start();
	}

	private ShowInfoThread sit = new ShowInfoThread();
	private ShowInfoMessage sim = new ShowInfoMessage();
	private Connection pharosConn;
	private String dealMode;

	public String getDivName() {
		return sim.getDivName();
	}

	public void setDivName(String divName) {
		sim.setDivName(divName);
	}

	public String getTableName() {
		return sim.getTableName();
	}

	public void setTableName(String tableName) {
		sim.setTableName(tableName);
	}

	public String getExceptionMessage() {
		return sim.getExceptionMessage();
	}

	public void setExceptionMessage(String exceptionMessage) {
		sim.setExceptionMessage(exceptionMessage);
	}

	public String getName() {
		return sim.getName();
	}

	public void setName(String name) {
		InsideProcess n = new InsideProcess();
		n.setName("未开始");
		n.setNow(0);
		n.setTotal(0);
		sim.getProcessL().add(n);
		sim.setName(name);
	}

	public Long getNow() {
		return sim.getNow();
	}

	public void setNow(Long now) {
		sim.setNow(now);
	}

	public Long getTotal() {
		return sim.getTotal();
	}

	public void setTotal(Long total) {
		sim.setTotal(total);
	}

	public String getShowMassage() {
		return sim.getShowMassage();
	}

	public void setShowMassage(String showMassage) {
		sim.setShowMassage(showMassage);
	}

	public List<InsideProcess> getProcessL() {
		return sim.getProcessL();
	}

	public void setProcessL(List<InsideProcess> processL) {
		sim.setProcessL(processL);
	}

	// public void getNum() {
	// ShowInfoThread sit = new ShowInfoThread();
	// sit.setPharosConn(pharosConn);
	// sit.setSim(sim);
	// sit.start();
	// }

	public void setShowMassageStart() {
		setShowMassage(getName() + "调度开始");
		// getNum();
	}

	public void setShowMassageSuspended() {
		setShowMassage(getName() + "调度线程暂停...");
		// getNum();
	}

	public void setShowMassageContinue() {
		setShowMassage(getName() + "调度线程继续执行...");
		// getNum();
	}

	public void setShowMassageDormancy() {
		setShowMassage(getName() + "调度线程开始休眠");
		// getNum();
	}

	public void setShowMassageResume() {
		setShowMassage(getName() + "等待结束，重新开始处理");
		// getNum();
	}

	public void setShowMassageNoSpace() {
		setShowMassage("数据库空间不足");
		// getNum();
	}

	public Long getError() {
		return sim.getError();
	}

	public void setError(Long error) {
		sim.setError(error);
	}

	public String getDealMode() {
		return dealMode;
	}

	public void setDealMode(String dealMode) {
		this.dealMode = dealMode;
	}
}
