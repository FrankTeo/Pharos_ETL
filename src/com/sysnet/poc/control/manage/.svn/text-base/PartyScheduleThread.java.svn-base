package com.sysnet.poc.control.manage;

import org.apache.commons.logging.Log;
import org.jdom.Document;

import com.sysnet.poc.control.config.Constants;
import com.sysnet.poc.control.manage.vo.ConfigureBean;
import com.sysnet.poc.control.manage.vo.StatusBean;
import com.sysnet.poc.control.manage.vo.StopBean;
import com.sysnet.poc.control.manage.vo.ThreadContral;
import com.sysnet.poc.service.dao.PartyScheduleDAO;
import com.sysnet.poc.util.FreeSpaceUtil;
import com.sysnet.poc.util.OdsLogger;

/**
 * 当事方调度线程
 * 
 * @author 卢海滨 修改日期：2010-10-18 修改内容：添加接口，添加自动控制开关 修改人：段成伟
 */
public class PartyScheduleThread extends ThreadContral {
	public PartyScheduleThread() {
		si.setName("当事方");
		si.setDivName("party");
		si.setTableName(Constants.ETL_PHAROS_INCR_PARTY);
		si.run();
	}

	private static final Log log = OdsLogger.getLog("partyLog4j", "Party");

	private Document configDocument;

	public Document getConfigDocument() {
		return configDocument;
	}

	public void setConfigDocument(Document configDocument) {
		this.configDocument = configDocument;
	}

	/**
	 * 当事方调度
	 */
	protected void schedule() {
		si.setShowMassageStart();
		PartyMainThread partyMainThread = null;

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
				PartyScheduleDAO partyScheduleDAO = new PartyScheduleDAO();
				ConfigureBean configureBean = partyScheduleDAO.getConfigure();// ConfigureBean对应ya_stage用户库里面的ETL_STAGE_ETL_PARTY_CONFIG表
				StatusBean statusBean = partyScheduleDAO.getBatStatus();// StatusBean对应ya_stage用户库里面的ETL_STAGE_ETL_PARTY_STATUS表，存放批次信息
				if (FreeSpaceUtil.isHaveSpace()) {
					// 检测上次处理状态
					if (statusBean != null && (statusBean.getSTAGE_DATE_READY_STATUS().trim().equals("0"))) {
						int c = partyScheduleDAO.getPharosIncr(String.valueOf(statusBean.getBAT_ID()));
						if (c == 0) {
							// 改变标志位
							partyScheduleDAO.updateBatStatus(String.valueOf(statusBean.getBAT_ID()));
							continue;
						}
						// 上次没处理完
						statusBean.setCurrentStatus(1);
					} else {

						// 上次处理完成,产生一个新的批次，包括第一次程序运行，刚运行的时候StatusBean对应的表没数据，所以肯定为null，此时产生一个新批次
						boolean isBat = partyScheduleDAO.insertOneBat(configureBean, statusBean);
						if (!isBat) {// 如果没有产生新的批次，说明增量表中没有增量数据了

							si.setShowMassageDormancy();
							sleep(configureBean.getGENERAL_SLEEP_MILLIS_COUNT());
							si.setShowMassageResume();

							partyScheduleDAO = null;
							configureBean = null;
							statusBean = null;

							if (Runtime.getRuntime().freeMemory() < 100 * 1024 * 1024) {
								System.gc();
							}

							continue;
						}
						// 从新初始化批量配置信息
						statusBean = partyScheduleDAO.getBatStatus();
						statusBean.setCurrentStatus(0);// 上次处理完了，如果是第一次运行，那么也设置为0
					}
					// 线程控制标志
					StopBean stopBean = new StopBean();
					// stopBean.setStop(0);
					if (statusBean.getCurrentStatus() == 0) {
						stopBean.setThreadC(configureBean.getGENERAL_MAX_THREAD_COUNT());
					} else {
						stopBean.setThreadC(configureBean.getEXCEPT_MAX_THREAD_COUNT());
					}
					// 启动主线程一个主线程对应一个批次
					partyMainThread = new PartyMainThread();
					partyMainThread.setConfigDocument(this.getConfigDocument());
					partyMainThread.setStopBean(stopBean);
					partyMainThread.setStatusBean(statusBean);
					partyMainThread.start();

					while (true) {// 判断主线程(即一个批次)是否结束
						if (stopBean.getThreadStop() == 1) {
							si.setShowMassageDormancy();
							partyMainThread = null;// 进行优化

							if (Runtime.getRuntime().freeMemory() < 100 * 1024 * 1024) {
								System.gc();
							}

							if (statusBean.getCurrentStatus() == 1) {
								// 设置异常休眠时间
								sleep(configureBean.getEXCEPT_SLEEP_MILLIS_COUNT());
							} else { // 设置正常休眠时间
								sleep(configureBean.getGENERAL_SLEEP_MILLIS_COUNT());
							}

							si.setShowMassageResume();
							break;
						}
						sleep(1);
						// log.info("7");
					}
					partyScheduleDAO = null;
					configureBean = null;
					statusBean = null;
					stopBean = null;
				} else {
					suspendFlag = true;
					realSuspend();
					si.setShowMassageNoSpace();
				}
			} catch (Exception e) {
				log.error("当事方调度线程抛出异常:", e);
				si.setExceptionMessage(e.getMessage());
				suspendFlag = true;
			}
		}
	}
}
