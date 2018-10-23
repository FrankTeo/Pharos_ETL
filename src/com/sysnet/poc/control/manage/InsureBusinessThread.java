package com.sysnet.poc.control.manage;

import org.apache.commons.logging.Log;
import org.jdom.Document;

import pharos.ctm.bo.BContractBO;

import com.sysnet.poc.control.manage.vo.EveryThreadInfo;
import com.sysnet.poc.control.manage.vo.ShowInfo;
import com.sysnet.poc.control.manage.vo.StatusBean;
import com.sysnet.poc.control.manage.vo.StopBean;
import com.sysnet.poc.control.manage.vo.ThreadCatch;
import com.sysnet.poc.service.business.EndorseBusiness;
import com.sysnet.poc.service.business.PolicyBusiness;
import com.sysnet.poc.service.business.ProposalBusiness;
import com.sysnet.poc.service.business.QuotationBusiness;
import com.sysnet.poc.util.OdsLogger;
import com.sysnet.poc.util.PharosServiceContainer;

/**
 * 承保子线程类
 * 
 * @author Ma_Ke
 * @since 2009-07-06
 */
public class InsureBusinessThread extends Thread {
	private ShowInfo si;
	private static final Log log = OdsLogger.getLog("insureLog4j", "Insure");

	private BContractBO bContractBo;
	private StopBean stopBean;

	private StatusBean statusBean;
	private ThreadCatch tc;
	private String dealMode;

	// private List<EveryThreadInfo> el;

	public ThreadCatch getTc() {
		return tc;
	}

	public void setTc(ThreadCatch tc) {
		this.tc = tc;
	}

	private Document document;

	public InsureBusinessThread(ShowInfo si) {
		this.si = si;
		this.bContractBo = PharosServiceContainer.Instance().getBContractBO();
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

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

//		log.info("Thread " + this.getId() + "is launching, number of task：" + tc.getEl().size());
//		long a = System.currentTimeMillis();
		try {
			// for (int i = 0; i < el.size(); i++) {
			
			while (tc.getEl().size() > 0) {
				if (this.statusBean.isSuggestStop()) {
					// 由InsureMainThread 统一处理批次把未完成的处理一下
					tc.clear();
					break;
				}
				EveryThreadInfo eti = tc.getTask();
				// 如果接受到结束信号，就不处理下一条
				if (eti != null) {
					String contract_No = eti.getPolicNoId();// 合同号
					String object_Id = eti.getObjectId();// 对象序号
					String object_no = eti.getObjectNo();// 对象code，对应于保单号、批单号、投保单号等等，由pharos产生。具体放置对象依赖于对象类型object_type的值
					String object_Type = eti.getObjectType();// 业务对象的类型，1---保单，OBJECT_NO中存放保单号；2---批单，OBJECT_NO中存放批单号；3---投保单，OBJECT_NO中存放投保单号；
					String version = eti.getVersion();
					String main_version = eti.getMain_version();
					String node_No = eti.getNodeno();
					String proposal_No = eti.getProposalNoId();
					//log.info(this.getId() + ":proposal_No:" + proposal_No + " object_id:" + eti.getObjectId());
					String PRODUCT_CD = eti.getProductCD();
					String PRODUCT_NODE_CD = eti.getProductNodeCD();
					String OPERATE_NODE_NO = eti.getOperate_node_no();
					if (OPERATE_NODE_NO == null) {
						OPERATE_NODE_NO = "1";
					}
					String endorse_NO = eti.getEndorse_no();

					loop(contract_No, version, main_version, object_Id, object_no, object_Type, statusBean.getBAT_ID(), node_No, proposal_No, PRODUCT_CD, PRODUCT_NODE_CD, OPERATE_NODE_NO, endorse_NO);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			stopBean.addStop();
//			log.info("Batch is finished, spend[" + (System.currentTimeMillis() - a) + "]" );
		}
	}

	private void loop(String contractNo, String version, String main_version, String objectId, String object_no, String objectType, int batId, String nodeNo, String proposalNo, String PRODUCT_CD, String PRODUCT_NODE_CD, String operate_node_no, String endorse_NO) {
		long a = System.currentTimeMillis();
//		log.info("contract[" + contractNo + "] Proposal[" + proposalNo + "]");
		if (objectType.trim().equals("1")) {
			// 保单业务,将投保人递交的初始申请进行核保之后的正式合同
			PolicyBusiness policyBusiness = new PolicyBusiness(si, this.bContractBo);
			policyBusiness.excuteByMapping(contractNo, version, main_version, batId, objectId, object_no, objectType, nodeNo, PRODUCT_CD, PRODUCT_NODE_CD, proposalNo);
			//
		} else if (objectType.trim().equals("2")) {
			// 批单业务，对保单进行修改，批改
			EndorseBusiness endorseBusiness = new EndorseBusiness(si);
			endorseBusiness.excuteByMapping(contractNo, version, batId, objectId, object_no, objectType, nodeNo, PRODUCT_CD, PRODUCT_NODE_CD, main_version, operate_node_no, proposalNo, endorse_NO);
		} else if (objectType.trim().equals("3")) {
			// 投保单业务,投保人递交给保险人的初始申请
			ProposalBusiness proposalBusiness = new ProposalBusiness(si);
			proposalBusiness.excuteByMapping(proposalNo, version, main_version, batId, objectId, object_no, objectType, nodeNo, PRODUCT_CD, PRODUCT_NODE_CD, proposalNo);
		} else if (objectType.trim().equals("9")) {
			// 询价单业务
			QuotationBusiness inquiryBusiness = new QuotationBusiness(si);
			inquiryBusiness.excuteByMapping(proposalNo, version, main_version, batId, objectId, object_no, objectType, nodeNo, PRODUCT_CD, PRODUCT_NODE_CD, proposalNo);
		}
		log.info( "BIZ:" + objectType.trim() 
				+ " PROPOSAL:" + String.format("%-30s", proposalNo) 
				+ " THREAD:" + String.format("%1$-9d",Thread.currentThread().getId()) 
				+ " COST:" + String.format("%1$-6d",(System.currentTimeMillis() - a)) + " ms");
	}

	public String getDealMode() {
		return dealMode;
	}

	public void setDealMode(String dealMode) {
		this.dealMode = dealMode;
	}
}
