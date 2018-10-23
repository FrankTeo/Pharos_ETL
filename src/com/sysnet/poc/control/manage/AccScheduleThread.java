package com.sysnet.poc.control.manage;

import org.apache.commons.logging.Log;
import org.jdom.Document;

import com.sysnet.poc.control.config.Constants;
import com.sysnet.poc.control.manage.vo.ConfigureBean;
import com.sysnet.poc.control.manage.vo.StatusBean;
import com.sysnet.poc.control.manage.vo.StopBean;
import com.sysnet.poc.control.manage.vo.ThreadContral;
import com.sysnet.poc.service.dao.AccSchedulDAO;
import com.sysnet.poc.util.FreeSpaceUtil;
import com.sysnet.poc.util.OdsLogger;

/**
 * 收付费调度线程
 * 
 * @author 卢海滨
 */
public class AccScheduleThread extends ThreadContral {
	public AccScheduleThread() {
		si.setName("收付费");
		si.setDivName("acc");
		si.setTableName(Constants.ETL_PHAROS_INCR_ACC);
		si.run();
	}

	private static final Log log = OdsLogger.getLog("accLog4j", "ACC");

	private Document configDocument;

	public Document getConfigDocument() {
		return configDocument;
	}

	public void setConfigDocument(Document configDocument) {
		this.configDocument = configDocument;
	}

	/**
	 * 收付费调度
	 */
	public void schedule() {
		si.setShowMassageStart();
		AccMainThread accMainThread = null;

		while (true) {// 这里是个死循环，因为整个的系统是要不断的监控demo_new用户数据库中的增量表中的数据，如果stage_write_flag=0并且bat_id=0，那么就说明这条增量没被抽取，此时就会进行抽取，否则就会显示没有增量数据，线程等待休眠
			try {
				synchronized (this) {
					while (suspendFlag) {
						if (FreeSpaceUtil.isHaveSpace()) {
							si.setShowMassageSuspended();
						} else {
							si.setShowMassageNoSpace();
						}
						wait();
					}
				}
				// 初始化全局配置信息
				AccSchedulDAO accSchedulDAO = new AccSchedulDAO();
				ConfigureBean configureBean = accSchedulDAO.getConfigure();// ConfigureBean对应ya_stage用户库里面的ETL_STAGE_ETL_ACC_CONFIG表
				StatusBean statusBean = accSchedulDAO.getBatStatus();// StatusBean对应ya_stage用户库里面的ETL_STAGE_ETL_ACC_STATUS表，存放批次信息
				if (FreeSpaceUtil.isHaveSpace()) {
					// 检测上次处理状态
					if (statusBean != null && (statusBean.getSTAGE_DATE_READY_STATUS().trim().equals("0"))) {
						int c = accSchedulDAO.getPharosIncr(String.valueOf(statusBean.getBAT_ID()));
						if (c == 0) {
							// 改变标志位
							accSchedulDAO.updateBatStatus(String.valueOf(statusBean.getBAT_ID()));
							continue;
						}

						// 上次没处理完
						statusBean.setCurrentStatus(1);
					} else {

						// 上次处理完成,产生一个新的批次，包括第一次程序运行，刚运行的时候StatusBean对应的表没数据，所以肯定为null，此时产生一个新批次
						boolean isBat = accSchedulDAO.insertOneBat(configureBean, statusBean);
						if (!isBat) {// 如果没有产生新的批次，说明增量表中没有增量数据了
							si.setShowMassageDormancy();
							sleep(configureBean.getGENERAL_SLEEP_MILLIS_COUNT());
							si.setShowMassageResume();
							if (Runtime.getRuntime().freeMemory() < 100 * 1024 * 1024) {
								System.out.println("内存不足10M");
								System.gc();
							}
							continue;
						}

						// 从新初始化批量配置信息
						statusBean = accSchedulDAO.getBatStatus();
						statusBean.setCurrentStatus(0);// 上次处理完了，如果是第一次运行，那么也设置为0
					}

					// 线程控制标志
					StopBean stopBean = new StopBean();
//					stopBean.setStop(0);
					if (statusBean.getCurrentStatus() == 0) {
						stopBean.setThreadC(configureBean.getGENERAL_MAX_THREAD_COUNT());
					} else {
						stopBean.setThreadC(configureBean.getEXCEPT_MAX_THREAD_COUNT());
					}

					// 启动主线程一个主线程对应一个批次
					accMainThread = new AccMainThread();
					accMainThread.setConfigDocument(this.getConfigDocument());
					accMainThread.setStopBean(stopBean);
					accMainThread.setStatusBean(statusBean);
					accMainThread.start();

					while (true) {// 判断主线程(即一个批次)是否结束
						if (stopBean.getStop() == 1) {
							si.setShowMassageDormancy();
							accMainThread = null;// 进行优化

							if (statusBean.getCurrentStatus() == 1) {
								// 设置异常休眠时间
								sleep(configureBean.getEXCEPT_SLEEP_MILLIS_COUNT());
							} else { // 设置正常休眠时间
								sleep(configureBean.getGENERAL_SLEEP_MILLIS_COUNT());
							}

							si.setShowMassageResume();

							System.gc();// 释放内存
							break;
						}
						// log.info("2");
						sleep(1);
					}
				} else {
					suspendFlag = true;
					realSuspend();
					si.setShowMassageNoSpace();
				}
			} catch (Exception e) {
				log.error("收付费调度线程抛出异常:", e);
				si.setExceptionMessage(e.getMessage());
				suspendFlag = true;
			}
		}
	}
}
