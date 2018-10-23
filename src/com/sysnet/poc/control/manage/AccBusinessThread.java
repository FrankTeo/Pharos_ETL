package com.sysnet.poc.control.manage;

import org.apache.commons.logging.Log;

import com.sysnet.poc.control.manage.vo.EtlPharosIncrAcc;
import com.sysnet.poc.control.manage.vo.EveryThreadInfo;
import com.sysnet.poc.control.manage.vo.StatusBean;
import com.sysnet.poc.control.manage.vo.StopBean;
import com.sysnet.poc.control.manage.vo.ThreadCatch;
import com.sysnet.poc.service.business.AccBusiness;
import com.sysnet.poc.util.OdsLogger;

/**
 * 收付费子线程类
 * 
 * @author 卢海滨
 */
public class AccBusinessThread extends Thread {

	public ThreadCatch getTc() {
		return tc;
	}

	public void setTc(ThreadCatch tc) {
		this.tc = tc;
	}

	private ThreadCatch tc;

	private static final Log log = OdsLogger.getLog("accLog4j", "ACC");

	private StopBean stopBean;

	private StatusBean statusBean;

	// private List<EveryThreadInfo> el;

	// public List<EveryThreadInfo> getEveryThreadInfo() {
	// return el;
	// }
	//
	// public void setEveryThreadInfo(List<EveryThreadInfo> el) {
	// this.el = el;
	// }

	public StopBean getStopBean() {
		return stopBean;
	}

	public void setStopBean(StopBean stopBean) {
		this.stopBean = stopBean;
	}

	public StatusBean getStatusBean() {
		return statusBean;
	}

	public void setStatusBean(StatusBean statusBean) {
		this.statusBean = statusBean;
	}

	public void run() {

		EtlPharosIncrAcc ec = new EtlPharosIncrAcc();
		log.debug("info=======Sub thread of pay is begining");
		while (tc.getEl().size() > 0) {
			// for (int i = 0; i < el.size(); i++) {
			EveryThreadInfo eti = tc.getTask();
			String object_No = eti.getObjectNo();
			String object_Id = eti.getObjectId();
			String object_Type = eti.getObjectType();
			String deal_flag = eti.getDeal_flag();
			String bat_id = eti.getBat_Id();
			Boolean b = Boolean.parseBoolean(eti.getIs_claim());
			ec.setObjectId(Long.parseLong(object_Id));
			ec.setObjectNo(object_No);
			ec.setObjectType(Long.parseLong(object_Type));
			ec.setDealFlag(deal_flag);
			ec.setBatId(Long.parseLong(bat_id));
			ec.setIsClaim(b);
			log.info("Acc Current object_No:" + object_No);

			loop(ec);
		}
		log.debug("info=======Sub thread of pay done");

		stopBean.addStop();
		ec = null;
	}

	private void loop(EtlPharosIncrAcc ec) {

		// 收付费相关业务
		log.debug("info=======Around pay");

		AccBusiness accBusiness = new AccBusiness();
		accBusiness.excute(ec);
		accBusiness = null;
		// si.getNum();

	}
}
