package com.sysnet.poc.control.manage;

import org.apache.commons.logging.Log;

import com.sysnet.poc.control.manage.vo.EveryThreadInfo;
import com.sysnet.poc.control.manage.vo.StatusBean;
import com.sysnet.poc.control.manage.vo.StopBean;
import com.sysnet.poc.control.manage.vo.ThreadCatch;
import com.sysnet.poc.service.business.PartyBusiness;
import com.sysnet.poc.util.OdsLogger;

/**
 * 当事方子线程类
 * 
 * @author 卢海滨
 */
public class PartyBusinessThread extends Thread {
	private Boolean isNoError = true;
	private ThreadCatch tc;

	public ThreadCatch getTc() {
		return tc;
	}

	public void setTc(ThreadCatch tc) {
		this.tc = tc;
	}

	private String errorMessage;

	// private String objectId;

	// private String objectNo;

	// public String getObjectNo() {
	// return objectNo;
	// }
	//
	// public void setObjectNo(String objectNo) {
	// this.objectNo = objectNo;
	// }
	//
	// public String getObjectId() {
	// return objectId;
	// }
	//
	// public void setObjectId(String objectId) {
	// this.objectId = objectId;
	// }

	public Boolean getIsNoError() {
		return isNoError;
	}

	public void setIsNoError(Boolean isNoError) {
		this.isNoError = isNoError;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


	private static final Log log = OdsLogger.getLog("partyLog4j", "Party");

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

	// private List result = new ArrayList();
	//
	// public List getResult() {
	// return result;
	// }
	//
	// public void setResult(List result) {
	// this.result = result;
	// }

	public void run() {
		try {
			while (tc.getEl().size() > 0) {
				log.debug("info=======Sub thread is begining");
				// for (int i = 0; i < el.size(); i++) {
				EveryThreadInfo eti = tc.getTask();
				String object_No = eti.getObjectNo();
				// objectNo = object_No;
				String object_Id = eti.getObjectId();
				// objectId = object_Id;
				String object_Type = eti.getObjectType();
				String deal_flag = eti.getDeal_flag();
				String time_stamp = eti.getTime_stamp();
				String history_no = eti.getHistory_no();
				log.info("Current party object_No:" + object_No);
				loop(object_No, object_Id, object_Type, statusBean.getBAT_ID(), deal_flag, time_stamp, history_no);
			}
			log.debug("info=======Sub thread done");

		} catch (Exception e) {
			isNoError = false;
			errorMessage = e.toString();
			e.printStackTrace();
		} finally {
			stopBean.addStop();
		}
	}

	private void loop(String object_No, String object_Id, String object_Type, int bat_Id, String deal_flag, String time_stamp, String history_no) throws Exception {

		// Party相关业务
		log.debug("info=======Party相关业务");
		// result.addAll(new PartyMappingBuilder().execute(object_No));
		PartyBusiness partyBusiness = new PartyBusiness();
		partyBusiness.excuteByMapping(object_No, object_Id, object_Type, bat_Id, deal_flag, time_stamp, history_no);
		// si.getNum();
	}
}
