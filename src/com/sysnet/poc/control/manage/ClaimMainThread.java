package com.sysnet.poc.control.manage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;

import com.sysnet.poc.claimmapping.vo.ThreadCatchClaim;
import com.sysnet.poc.control.config.Constants;
import com.sysnet.poc.control.manage.vo.EtlPharosIncrClaim;
import com.sysnet.poc.control.manage.vo.StatusBean;
import com.sysnet.poc.control.manage.vo.StopBean;
import com.sysnet.poc.service.dao.ClaimDAO;
import com.sysnet.poc.service.dao.ClaimSchduleDAO;
import com.sysnet.poc.service.dao.daoFactory.DAOFactory;
import com.sysnet.poc.service.dao.dbc.ConnDB;
import com.sysnet.poc.util.OdsLogger;

/**
 * 理赔主线程
 * 
 * @author Administrator
 * 
 */
public class ClaimMainThread {

	private static final Log log = OdsLogger.getLog("claimLog4j", "Claim");

	private StopBean stop = null;
	private StatusBean status = null;

	/**
	 * 设置线程控制对象
	 * 
	 * @param stop
	 */
	public void setStopBean(StopBean stop) {
		this.stop = stop;
	}

	/**
	 * 设置当前批次对象
	 * 
	 * @param status
	 */
	public void setStatusBean(StatusBean status) {
		this.status = status;
	}

	@SuppressWarnings({ "unchecked" })
	public boolean run() {
		Connection pharosConn = null;
		Connection odsConn = null;
		FilterIncData fid = new FilterIncData();
		try {
			StopBean stopM = new StopBean();
			// 获取数据连结
			pharosConn = ConnDB.getPharosConn();
			odsConn = ConnDB.getODSConn();
			// 获取调度DAO
			ClaimSchduleDAO claimShduleDAO = DAOFactory.getClaimSchduleDAO();
			ClaimDAO claimdao = DAOFactory.getClaimDAO();
			// 设置DAO的连接
			claimShduleDAO.setPharosConn(pharosConn);
			claimShduleDAO.setOdsConn(odsConn);
			claimdao.setPharosConn(pharosConn);
			claimdao.setOdsConn(odsConn);
			// 获取批次号
			int batID = this.status.getBAT_ID();
			// 获取本批次的所有增量对象
			List<EtlPharosIncrClaim> list = claimShduleDAO.incrementListByBatID(batID);
			// 筛选出需要进入子线程和不需要进入子线程的数据，因为相同理赔号(即claimid)可能对应多条数据，比如说报案，立案，这样就取最新的这条立案的数据，报案的取消掉

			// *********************************************************************************************
			// long ftimes = 0;//计算代码执行时间
			// ftimes = System.currentTimeMillis();
			fid.filter(list);
			List<EtlPharosIncrClaim> batake = fid.getBatake();
			List<EtlPharosIncrClaim> remain = fid.getRemain();
			// 处理不需要进入子线程的增量，将增量表回写标志回写为2
			for (EtlPharosIncrClaim eic : batake) {
				eic.setStageWriteFlag(Constants.INCREMENT_WRITE_BACK_STATUS_3);
				claimdao.updateIncr(eic);
			}

			// log.info("过滤增量并回写2共耗时: " + (System.currentTimeMillis()-ftimes) +
			// "毫秒");

			// *********************************************************************************************
			ThreadCatchClaim tcc = new ThreadCatchClaim();
			tcc.setEl(remain);
			int tc = this.stop.getThreadC();// 开启子线程数，对应登场区数据库中配置表中的子线程数字段
			// int otc = count / tc;// 每条线程处理记录数
			for (int i = 0; i < tc; i++) {

				// ClaimBusinessThread cbt = null;
				// if ((tc == 1) || (otc == 0)) {// 增量数小于开启的线程数只做一条线程处理
				ClaimBusinessThread cbt = new ClaimBusinessThread();// 开启子线程
				cbt.setTcc(tcc);
				cbt.setStatus(this.status);
				cbt.setStop(stopM);
				// cbt.setEpic(remain);
				cbt.start();
			}
			// else {// 增量数大于线程数给每条线程分流
			// List partFlowList = partFlow(remain, tc, otc);
			// List pf = null;// 每个子线程对应要处理的增量集合
			// for (int i = 0; i < partFlowList.size(); i++) {// 有几个就开几个子线程进行处理
			// pf = (List) partFlowList.get(i);
			// cbt = new ClaimBusinessThread(si);// 开启子线程
			// cbt.setStatus(this.status);
			// cbt.setStop(stopM);
			// cbt.setEpic(pf);
			// cbt.start();
			// }
			// }
			while (true) {
				log.debug("Waiting sub thread runing：" + stopM.getThreadCC());

				try {
					Thread.sleep(100);
//					Thread.sleep(1 + 3000 * tc);
				} catch (InterruptedException e) {

					log.error("ClaimMainThread:", e);
					e.printStackTrace();
				}
				// if (otc == 0) {// 当理赔的增量小于开启线程数的时候，此时是只开了一条子线程的情况，所以还要判断子线程是否加1了，加1说明子线程成功执行完毕了
				// if (stopM.getThreadCC() == 1) {
				// this.stop.setStop(Constants.STAGE_THREAD_STOP_FLAG_1);
				// log.info("理赔主线程执行结束");
				// return true;
				// }
				// } else {// 否则就开启多个子线程来处理，此时是开了多条子线程的情况，所以要判断各个子线程加1之后的总数是否等于总的开启的线程数
				if (stopM.getThreadCC() == this.stop.getThreadC()) {
					stop.addStop();
					log.info("Claim main thread end.");
					return true;
				}
				// }
				// Thread.sleep(1);
				// log.info("3");
			}
		} catch (Exception e) {
			log.error("ClaimMainThread:", e);
			e.printStackTrace();

			return false;
		} finally {
			try {
				if (pharosConn != null) {
					pharosConn.close();
				}
				if (odsConn != null) {
					odsConn.close();
				}
			} catch (SQLException e) {
				log.error("ClaimMainThread:", e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据线程数给每条线程分流
	 * 
	 * @param source
	 *            需要往登场区数据库中插入的当前批次的所有增量
	 * @param tc
	 *            需要开启的子线程数量
	 * @param otc
	 *            每条线程处理多少条增量
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	private List partFlow(List source, int tc, int otc) {
		List tcList = new ArrayList();// 返回一个大List，这个大List里面存放的是多个小List，每个小List里面存放的是各个子线程中处理的多条增量
		List temp = null;// 临时变量，接收screenData方法返回的各个子线程处理的增量的小List
		int size = source.size();// 当前批次过滤之后的增量的总条数
		int start = 0;// 起始小标，要在调用screenData方法的时候从这个下标开始，从当前批次过滤之后的增量List中取当前子线程需要处理的增量
		for (int i = 0; i < tc; i++) {// 有几个子线程就循环几次
			if (i == (tc - 1)) {// 是最后一个子线程的时候
				temp = screenData(source, start, source.size());// 取小List
				tcList.add(temp);// 将各个子线程需要处理的小List放入大List中
			} else {// 处理不是最后一个子线程的情况
				temp = screenData(source, start, start + otc);// 取小List
				tcList.add(temp);// 将各个子线程需要处理的小List放入大List中
				start = start + otc;// 只要不是最后一个子线程，那么得到每个子线程需要处理的增量之后，下一个子线程的起始下标就加一下otc
			}
		}
		return tcList;
	}

	// 按下标抽取区间书据
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List screenData(List list, int start, int end) {
		List partOfList = new ArrayList();
		for (int i = start; i < end; i++) {
			partOfList.add(list.get(i));// 将当前子线程需要处理的增量数据放入小List中并返回
		}
		return partOfList;
	}

}
