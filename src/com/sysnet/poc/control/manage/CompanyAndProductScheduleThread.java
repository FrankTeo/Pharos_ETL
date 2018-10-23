package com.sysnet.poc.control.manage;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.sysnet.poc.service.business.ProcedureService;
import com.sysnet.poc.util.DateUtil;

/**
 * 产品机构数据更新主开始执行，本版本中没有使用
 * 
 * @author Administrator
 * 
 */
public class CompanyAndProductScheduleThread extends Thread {

	private Logger log = Logger.getLogger(CompanyAndProductScheduleThread.class);

	@Override
	public void run() {

		this.importData();

	}

	private void importData() {
		log.info("产品机构数据更新主开始执行================================================");

		Timer timer = new Timer();

		TimerTask procedureService = new ProcedureService();//

		timer.schedule(procedureService, DateUtil.getDate(2009, 6, 25, 13, 53), 60 * 1000 * 60);

	}

}
