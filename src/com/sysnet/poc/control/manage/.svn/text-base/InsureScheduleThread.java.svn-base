package com.sysnet.poc.control.manage;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;

import com.sysnet.poc.control.config.Constants;
import com.sysnet.poc.control.manage.vo.ConfigureBean;
import com.sysnet.poc.control.manage.vo.InsideProcess;
import com.sysnet.poc.control.manage.vo.StatusBean;
import com.sysnet.poc.control.manage.vo.StopBean;
import com.sysnet.poc.control.manage.vo.ThreadContral;
import com.sysnet.poc.service.dao.InsureScheduleDAO;
import com.sysnet.poc.util.FreeSpaceUtil;

/**
 * 承保调度线程
 * 
 * @author Li_yanpeng
 * @since 2009-07-06 修改日期：2010-10-18 修改内容：添加接口，添加自动控制开关 修改人：段成伟
 */
public class InsureScheduleThread extends ThreadContral {
	public InsureScheduleThread() {
		si.setName("承保");
		si.setDivName("insure");
		si.setTableName(Constants.ETL_PHAROS_INCR_INSURE);
		si.run();
	}

	private static final Log log = LogFactory.getLog(InsureScheduleThread.class);

	StatusBean statusBean;
	private Document configDocument;

	public Document getConfigDocument() {
		return configDocument;
	}

	public void setConfigDocument(Document configDocument) {
		this.configDocument = configDocument;
	}

	/**
	 * 承保调度
	 */
	protected void schedule() {
		si.setShowMassageStart();
		InsureMainThread insureMainThread = null;
		String dealMode = "";
		while (true) {
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

				// log.info("跳出睡梦！");

				// 初始化全局配置信息
				InsureScheduleDAO insureScheduleDAO = new InsureScheduleDAO();
				ConfigureBean configureBean = insureScheduleDAO.getConfigure();
				// 处理方式
				dealMode = configureBean.getDEAL_MODE();
				statusBean = insureScheduleDAO.getBatStatus();
				if (FreeSpaceUtil.isHaveSpace()) {

					// 检测上次处理状态
					if (statusBean != null && (statusBean.getSTAGE_DATE_READY_STATUS().trim().equals("0")))// 批次未处理完成
					{
						int c = insureScheduleDAO.getPharosIncr(String.valueOf(statusBean.getBAT_ID()));
						if (c == 0) {
							// 改变标志位
							insureScheduleDAO.updateBatStatus(String.valueOf(statusBean.getBAT_ID()));
							continue;
						}
						statusBean.setCurrentStatus(1);// 上次没处理完
					} else// 批次处理完成
					{
						// 产生一个新的批次
						boolean isBat = insureScheduleDAO.insertOneBat(configureBean, statusBean);
						// log.info("处理新批次：" + isBat + ",没有新数据了，恭喜！！");
						if (!isBat) {
							//
							if (statusBean.getCurrentStatus() == 1) {
								// 设置异常休眠时间
								sleep(configureBean.getEXCEPT_SLEEP_MILLIS_COUNT());
							} else { // 设置正常休眠时间
								sleep(configureBean.getGENERAL_SLEEP_MILLIS_COUNT());
							}
							synchronized (this) {
								while (suspendFlag) {
									si.setShowMassageSuspended();
									wait();
								}
							}

							si.setShowMassageDormancy();
							si.setShowMassageResume();

							insureScheduleDAO = null;
							configureBean = null;
							statusBean = null;

							if (Runtime.getRuntime().freeMemory() < 100 * 1024 * 1024) {
								System.gc();
							}

							continue;
						}
						// 重新初始化批量配置信息
						statusBean = insureScheduleDAO.getBatStatus();
						statusBean.setCurrentStatus(0);// 上次处理完了

					}

					// 线程控制标志
					StopBean stopBean = new StopBean();
//					stopBean.setStop(0);
					if (statusBean.getCurrentStatus() == 0) {
						stopBean.setThreadC(configureBean.getGENERAL_MAX_THREAD_COUNT());
					} else {
						stopBean.setThreadC(configureBean.getEXCEPT_MAX_THREAD_COUNT());
					}

					// 启动主线程
					si.setProcessL(new ArrayList<InsideProcess>());
					si.setDealMode(dealMode);
					insureMainThread = new InsureMainThread(si);
					insureMainThread.setDealMode(dealMode);
					insureMainThread.setConfigDocument(this.getConfigDocument());
					insureMainThread.setStopBean(stopBean);
					insureMainThread.setStatusBean(statusBean);
					insureMainThread.start();

					while (true) {
						// log.debug("============stopBean.getStop()=="+stopBean.getStop());
						if (stopBean.getStop() == 1) {
							insureMainThread = null;
							if (Runtime.getRuntime().freeMemory() < 100 * 1024 * 1024) {
								System.gc();
							}
							si.setShowMassageDormancy();
							insureMainThread = null;
							// 处理完一个批次了，休息一下吧
							if (statusBean.getCurrentStatus() == 1) {
								// 设置异常休眠时间
								sleep(configureBean.getEXCEPT_SLEEP_MILLIS_COUNT());
							} else { // 设置正常休眠时间
								sleep(configureBean.getGENERAL_SLEEP_MILLIS_COUNT());
							}

							si.setShowMassageResume();
							break;
						}
						sleep(2000);
						// log.info("5");
					}

					stopBean = null;
					insureScheduleDAO = null;
					configureBean = null;
					statusBean = null;
				} else {
					suspendFlag = true;
					realSuspend();
					si.setShowMassageNoSpace();
				}
			} catch (Exception e) {
				log.error("Policy Control Thread throw exception ", e);

				// begin = false; // 主线程停止运行
				suspendFlag = true;
				si.setExceptionMessage(e.getMessage());
			}
		}
	}

	/**
	 * 通知子线程，关闭
	 */
	@Override
	protected void realSuspend() {
		if (statusBean != null) {
			statusBean.setSuggestStop(true);
		}
	}
}
