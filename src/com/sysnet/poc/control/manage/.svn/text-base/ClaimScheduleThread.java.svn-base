package com.sysnet.poc.control.manage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.commons.logging.Log;

import com.sysnet.poc.control.config.Constants;
import com.sysnet.poc.control.manage.vo.ConfigureBean;
import com.sysnet.poc.control.manage.vo.StatusBean;
import com.sysnet.poc.control.manage.vo.StopBean;
import com.sysnet.poc.control.manage.vo.ThreadContral;
import com.sysnet.poc.service.dao.ClaimSchduleDAO;
import com.sysnet.poc.service.dao.daoFactory.DAOFactory;
import com.sysnet.poc.service.dao.dbc.ConnDB;
import com.sysnet.poc.transaction.UserTransactionFactory;
import com.sysnet.poc.util.FreeSpaceUtil;
import com.sysnet.poc.util.OdsLogger;

/**
 * 
 * @author DBC 修改日期：2010-10-18 修改内容：添加接口，添加自动控制开关 修改人：段成伟
 */
public class ClaimScheduleThread extends ThreadContral {
	private static final Log log = OdsLogger.getLog("claimLog4j", "Claim");

	// 理赔线程暂停标志 true 为暂停
	public ClaimScheduleThread() {
		si.setName("理赔");
		si.setDivName("claim");
		si.setTableName(Constants.ETL_PHAROS_INCR_CLAIM);
		si.run();
	}

	// 理赔调度
	protected void schedule() {
		si.setShowMassageStart();
		while (true) {// 循环每个批次
			// ClaimScheduleThread.exceptionMessage = null;
			Long batStartTime = System.currentTimeMillis();
			Long totalNum = 1L;
			
			Connection pharosConn = null;
			Connection odsConn = null;
			UserTransaction ut = null;
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
				if (FreeSpaceUtil.isHaveSpace()) {
					ut = UserTransactionFactory.getUserTransaction();
					ut.begin();
					// 获取理赔调度的DAO对象
					ClaimSchduleDAO claimSchduleDAO = DAOFactory.getClaimSchduleDAO();
					// 设置DAO的数据库连接
					pharosConn = ConnDB.getPharosConn();
					odsConn = ConnDB.getODSConn();
					claimSchduleDAO.setPharosConn(pharosConn);
					claimSchduleDAO.setOdsConn(odsConn);
					// 初始化配置和批次，分别对应登场区数据库中的配置表与批次表
					ConfigureBean config = claimSchduleDAO.initConfig();
					StatusBean status = claimSchduleDAO.initStatus();

					if ((status != null) && (Constants.STAGE_DATE_READY_STATUS_0.equals(status.getSTAGE_DATE_READY_STATUS().trim()))) {// 登场区准备状态为0，说明上个批次未处理完
						// 检查本批次的未处理完记录数
						int count = claimSchduleDAO.incrementCount(status.getBAT_ID());
						if (count == 0) {// 如果上个批次都处理完了，这里可能有一种情况，即当这个批次所有增量完成的时候，突然数据库断掉了，此时回写登场区数据库的登场区准备状态就会失败，所以在这里单独加上这句话
							// 设置本批次登场区已状态为完成
							claimSchduleDAO.updateStageStatus(status.getBAT_ID(), Constants.STAGE_DATE_READY_STATUS_1);
							ut.commit();
							continue;// 跳出去，继续执行下个批次
						}
						status.setCurrentStatus(Constants.STAGE_CURRENT_STATUS_1);// 否则就说明上次的批次中还有没处理的增量，即上个批次出异常了，currentStatus这个变量到现在还没什么用，只是标记一下
					} else {// 未产生批次或上次批次已处理完
						boolean flag = claimSchduleDAO.isCrateIncrement();
						if (flag) {// 有新增量
							// 产生一个批次
							int batId = 1;
							int record_count = 5000;
							String dealCount = config.getDEAL_COUNT();
							if (dealCount != null) {// 设置最大处理条数
								record_count = Integer.parseInt(dealCount.trim());
							}

							if (status != null) {// 上次批次号
								batId = status.getBAT_ID() + 1;
							} else {// 第一次产生批次号
								batId = 1;
							}
							// 创建一个新的批次
							claimSchduleDAO.createStatus(batId, record_count);
							// 返回处理数以内的增量ID
							List<Long> list = claimSchduleDAO.incrementList(record_count);
							totalNum = Long.valueOf(list.size());
							for (int i = 0; i < list.size(); i++) {
								Long objectId = list.get(i);
								claimSchduleDAO.updateBatId(objectId, batId);// 将增量表中对应的增量的批次字段更新成为当前批次
							}

						} else {
							// claimSuspendFlag = true;
							// synchronized (this) {
							// while (claimSuspendFlag) {
							// log.info("理赔调度线程暂停...");
							// wait();
							// }
							// }

							si.setShowMassageDormancy();
							sleep(config.getGENERAL_SLEEP_MILLIS_COUNT());
							si.setShowMassageResume();

							claimSchduleDAO = null;
							config = null;
							status = null;

							if (Runtime.getRuntime().freeMemory() < 100 * 1024 * 1024) {
								System.gc();
							}

							ut.commit();

							continue;
						}
						// 加载已创建的批次，上面的那个status是判断上次批次处理的情况，但是这次新的批处理创建之后(新批次的增量还没处理)，还没有重新放入到status变量中，所以重新加载一下
						status = claimSchduleDAO.initStatus();
						status.setCurrentStatus(Constants.STAGE_CURRENT_STATUS_0);
					}
					ut.commit();
					// 线程控制标志
					StopBean stopBean = new StopBean();
//					stopBean.setStop(Constants.STAGE_THREAD_STOP_FLAG_0);// 0表示线程没停止，1表示线程停止
					// 设置启动线程数
					if (status.getCurrentStatus() == Constants.STAGE_CURRENT_STATUS_0) {// 表示本批次正常处理
						stopBean.setThreadC(config.getGENERAL_MAX_THREAD_COUNT());
					} else {// 表示上个批次是异常处理
						stopBean.setThreadC(config.getEXCEPT_MAX_THREAD_COUNT());
					}
					ClaimMainThread cmt = new ClaimMainThread();// 开启主线程，一个批次对应一个主线程
					cmt.setStatusBean(status);// 将批次信息传过去
					cmt.setStopBean(stopBean);// 将线程控制标志传过去
					if (cmt.run()) {
						si.setShowMassageDormancy();
						claimSchduleDAO.updateStageStatus(status.getBAT_ID(), Constants.STAGE_DATE_READY_STATUS_1);
						cmt = null;
						if (Runtime.getRuntime().freeMemory() < 100 * 1024 * 1024) {
							System.gc();
						}// 当一个批次结束之后，进行垃圾回收
							// 正常休眠
						sleep(config.getGENERAL_SLEEP_MILLIS_COUNT());
						si.setShowMassageResume();
					} else {
						// 异常休眠
						si.setShowMassageDormancy();
						sleep(config.getEXCEPT_SLEEP_MILLIS_COUNT());
						si.setShowMassageResume();
					}

					claimSchduleDAO = null;
					config = null;
					status = null;
					stopBean = null;
				} else {
					suspendFlag = true;
					realSuspend();
					si.setShowMassageNoSpace();
				}
			} catch (Exception e) {
				log.error("理赔调度线程出现异常！", e);
				e.printStackTrace();
				suspendFlag = true;
				si.setExceptionMessage(e.getMessage());
				try {
					ut.rollback();
				} catch (IllegalStateException e1) {
					e1.printStackTrace();
				} catch (SecurityException e1) {
					e1.printStackTrace();
				} catch (SystemException e1) {
					e1.printStackTrace();
				}
			} finally {
				try {
					if (pharosConn != null) {
						pharosConn.close();
					}
					if (odsConn != null) {
						odsConn.close();

					}
				} catch (SQLException e) {
					log.error("ClaimScheduleThread", e);
					e.printStackTrace();
				}

			}
			
			Long batEndTime = System.currentTimeMillis();
			log.info("Bat Cost Time:" + (batEndTime - batStartTime) + " Performance:" + (batEndTime - batStartTime)/(totalNum.doubleValue()));
		}
	}
}
