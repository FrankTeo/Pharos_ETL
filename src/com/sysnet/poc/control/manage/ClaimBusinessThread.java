package com.sysnet.poc.control.manage;

import org.apache.commons.logging.Log;

import com.sysnet.poc.claimmapping.vo.ThreadCatchClaim;
import com.sysnet.poc.control.manage.vo.EtlPharosIncrClaim;
import com.sysnet.poc.control.manage.vo.StatusBean;
import com.sysnet.poc.control.manage.vo.StopBean;
import com.sysnet.poc.service.business.ClaimBusiness;
import com.sysnet.poc.util.OdsLogger;

/**
 * 子线程处理类
 * 
 * @author Administrator
 * 
 */
public class ClaimBusinessThread extends Thread {
	private ThreadCatchClaim tcc;

	public ThreadCatchClaim getTcc() {
		return tcc;
	}

	public void setTcc(ThreadCatchClaim tcc) {
		this.tcc = tcc;
	}

	private static final Log log = OdsLogger.getLog("claimLog4j", "Claim");
	private StopBean stop = null;// 控制线程是否停止
	@SuppressWarnings("unused")
	private StatusBean status = null;// 对应当前批次
	// private List<EtlPharosIncrClaim> epic = null;// 当前子线程需要处理的增量

	// public void setEpic(List<EtlPharosIncrClaim> epic) {
	// this.epic = epic;
	// }

	public void setStop(StopBean stop) {
		this.stop = stop;
	}

	public void setStatus(StatusBean status) {
		this.status = status;
	}

	public void run() {
		while (tcc.getEl().size() > 0) {
			// for (int i = 0; i < epic.size(); i++) {// 循环当前子线程需要处理的增量数据，即处理过滤之后的单个增量数据
			EtlPharosIncrClaim incr = tcc.getTask();
			long a = System.currentTimeMillis();
			//log.info("Current object_No:" + incr.getObjectNo());
			ClaimBusiness cb = new ClaimBusiness();// 调用Pharos中相关服务对当前的一条增量进行抽取
			cb.excute(incr);
			log.info("Current object_No:" + incr.getObjectNo() + " TimeCost:" + (System.currentTimeMillis() - a));
			// si.getNum();
		}

		stop.add();
		this.stop.setThreadCC(stop.getThreadCC());

		System.out.println("Finished Counts of sub thread =" + stop.getThreadCC());
	}
}
