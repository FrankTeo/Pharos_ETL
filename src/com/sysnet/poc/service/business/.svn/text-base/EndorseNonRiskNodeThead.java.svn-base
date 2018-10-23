package com.sysnet.poc.service.business;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;

import pharos.ctm.bo.WContractBO;
import pharos.ctm.vo.WContractNode;

import com.sysnet.poc.control.config.Constants;
import com.sysnet.poc.control.manage.vo.InsideProcess;
import com.sysnet.poc.control.manage.vo.StopBean;
import com.sysnet.poc.mapping.MappingCacheManager;
import com.sysnet.poc.mapping.ProductMappingBuilder;
import com.sysnet.poc.service.dao.PlCIncrementDAO;
import com.sysnet.poc.service.dao.RunLogDAO;
import com.sysnet.poc.service.dao.daoFactory.DAOFactory;
import com.sysnet.poc.service.dao.dbc.ConnDB;
import com.sysnet.poc.service.storage.StorageTransAction;
import com.sysnet.poc.util.OdsLogger;
import com.sysnet.poc.vo.StgRunLog;

public class EndorseNonRiskNodeThead extends Thread {
	InsideProcess j = new InsideProcess();
	private String dealMode;

	public EndorseNonRiskNodeThead(List<InsideProcess> a) {
		super();
		j.setName("非风险");
		a.add(j);
	}

	private String errorMessage = "";

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	private static final Log log = OdsLogger.getLog("insureLog4j", "Insure");

	private List<Long> nonRiskNodeIDlist = null;
	@SuppressWarnings("rawtypes")
	private List result = new ArrayList();

	@SuppressWarnings("rawtypes")
	public List getResult() {
		return result;
	}

	@SuppressWarnings("rawtypes")
	public void setResult(List result) {
		this.result = result;
	}

	private String endorseNo;

	// private String lastEndorseNo;

	public String getEndorseNo() {
		return endorseNo;
	}

	public void setEndorseNo(String endorseNo) {
		this.endorseNo = endorseNo;
	}

	private int countCharY = 0;

	private WContractBO wContractBo;

	private String PRODUCT_CD;
	// private String productCode;

	// public String getProductCode() {
	// return productCode;
	// }
	//
	// public void setProductCode(String productCode) {
	// this.productCode = productCode;
	// }

	private String PRODUCT_NODE_CD;

	private String incrProposalNo;

	private String object_Id;

	private String bat_Id;

	private String object_Type;

	// private Map<Long, BContractNode> bnMap;

	// public Map<Long, BContractNode> getBnMap() {
	// return bnMap;
	// }

	// public void setBnMap(Map<Long, BContractNode> bnMap) {
	// this.bnMap = bnMap;
	// }

	private boolean write;
	private String object_no;
	// private DCS dcs = null;

	private String riskname;

	public String getRiskname() {
		return riskname;
	}

	// public void setRiskname(String riskname) {
	// this.riskname = riskname;
	// }

	// public String getPolicyNo() {
	// return policyNo;
	// }

	public WContractBO getwContractBo() {
		return wContractBo;
	}

	public void setwContractBo(WContractBO wContractBo) {
		this.wContractBo = wContractBo;
	}

	public String getBat_Id() {
		return bat_Id;
	}

	public void setBat_Id(String bat_Id) {
		this.bat_Id = bat_Id;
	}

	public String getObject_Type() {
		return object_Type;
	}

	public void setObject_Type(String object_Type) {
		this.object_Type = object_Type;
	}

	public String getObject_Id() {
		return object_Id;
	}

	public void setObject_Id(String object_Id) {
		this.object_Id = object_Id;
	}

	public String getIncrProposalNo() {
		return incrProposalNo;
	}

	public void setIncrProposalNo(String incrProposalNo) {
		this.incrProposalNo = incrProposalNo;
	}

	private StopBean stopBean;

	public StopBean getStopBean() {
		return stopBean;
	}

	public void setStopBean(StopBean stopBean) {
		this.stopBean = stopBean;
	}

	public List<Long> getNonRiskNodeIDlist() {
		return nonRiskNodeIDlist;
	}

	public void setNonRiskNodeIDlist(List<Long> nonRiskNodeIDlist) {
		this.nonRiskNodeIDlist = nonRiskNodeIDlist;
	}

	public int getCountCharY() {
		return countCharY;
	}

	public void setCountCharY(int countCharY) {
		this.countCharY = countCharY;
	}

	public String getPRODUCT_CD() {
		return PRODUCT_CD;
	}

	public void setPRODUCT_CD(String product_cd) {
		PRODUCT_CD = product_cd;
	}

	public String getPRODUCT_NODE_CD() {
		return PRODUCT_NODE_CD;
	}

	public void setPRODUCT_NODE_CD(String product_node_cd) {
		PRODUCT_NODE_CD = product_node_cd;
	}

	public void run() {
		try {
			if("1".equalsIgnoreCase(dealMode)){
				//20140827 Frank Zhang FOR Remove before Insert
				StorageTransAction storage = new StorageTransAction();
				Connection odsConn = ConnDB.getODSConn();
				storage.setConnection(odsConn);
				//log.info("Delete Node Data:[" +object_no + "]");
				storage.extendSQL("call rmnode.removenode(?)", new String[] { object_no });
				//log.info("Deleted!!!");
				odsConn.close();
			}
			this.toProcessNonRiskNode();
		} catch (Exception e) {
			stopBean.setErrorMessage(e.toString());
			stopBean.setAllWriteFlag(false);
			write = false;
			PlCIncrementDAO plCIncrementDAO = DAOFactory.getPlcIncrecentDAO();
			Connection conp = null;
			try {
				conp = ConnDB.getPharosConn();
				plCIncrementDAO.updateStatus(conp, Long.valueOf(object_Id), Constants.INCREMENT_WRITE_BACK_STATUS_2);

			} catch (Exception e2) {
				log.error("", e2);
			} finally {
				if (conp != null) {
					try {
						conp.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
			log.error("", e);
			// 记录日志到 stg_run_log 表中
			StgRunLog stgLog = new StgRunLog();
			stgLog.setErrorCode("2");
			stgLog.setErrorMessage(e.toString());
			stgLog.setObjectId(object_Id);
			stgLog.setObjectNo(object_no);
			RunLogDAO rld = new RunLogDAO();
			try {
				rld.insertLog(stgLog);
			} catch (SQLException e2) {
				log.error("log error:", e2);
			} catch (NamingException e2) {
				log.error("log error:", e2);
			}
		} finally {
			stopBean.addStop();
		}
	}

	@SuppressWarnings("unchecked")
	public void toProcessNonRiskNode() throws Exception {
		int countsOfNonRisk = nonRiskNodeIDlist.size();
		SimpleDateFormat matter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		// Long e = System.currentTimeMillis();
		// Long f = System.currentTimeMillis();
		for (int i = 0; i < countsOfNonRisk; i++) {
			// e = System.currentTimeMillis();
			// log.info("循环等待的时间：" + (e - f));
			Long riskNodeid = nonRiskNodeIDlist.get(i);
			// WContractNode node = (WContractNode) bnMap.get(riskNodeid);
			// Long a = System.currentTimeMillis();
			WContractNode node = wContractBo.getSingleNode(riskNodeid, false);
			if (node.getNodeNo().equals("1")) {
				node = wContractBo.getContractNode(riskNodeid);
			}
			// Long b = System.currentTimeMillis();
			// log.info("取节点的时间：" + (b - a));
			//log.info("PolicyNo=" + node.getPolicyNo() + ",node size=" + nonRiskNodeIDlist.size() + ",now:" + i);
			String dcsNodeId = node.getDcsNodeCode();
			// Long b1 = System.currentTimeMillis();
			// log.info("取节点模板的时间1：" + (b1 - b));
			ProductMappingBuilder singleProductMappingBuilder = new ProductMappingBuilder(PRODUCT_CD, node, "2", null, wContractBo);
			// singleProductMappingBuilder.setMcm(mcm);
			// Long b2 = System.currentTimeMillis();
			// log.info("取节点模板的时间2：" + (b2 - b1));
			singleProductMappingBuilder.setMapDocument(MappingCacheManager.getMapping(PRODUCT_CD, PRODUCT_NODE_CD, "2_1_" + dcsNodeId));
//			Long c = System.currentTimeMillis();
			// log.info("取节点模板的时间3：" + (c - b2));
			Map<String, String> globalDataMap = new HashMap<String, String>();
			globalDataMap.put("policyno", node.getPolicyNo());
			globalDataMap.put("proposalno", node.getProposalNo());
			globalDataMap.put("endorseno", node.getEndorseNo());
			globalDataMap.put("contractno", node.getContractNo());
			globalDataMap.put("nodeno", node.getNodeNo());
			globalDataMap.put("objectid", object_Id);
			globalDataMap.put("objectno", object_no);
			//globalDataMap.put("mainversion", node.getMainVersion() + "");
			globalDataMap.put("version", node.getVersion() + "");
			globalDataMap.put("subjectno", node.getDcsNodeCode());
			globalDataMap.put("quotationno", node.getQuotationNo());
			globalDataMap.put("origindate", matter.format(node.getOriginalDate()));
			globalDataMap.put("startdate", matter.format(node.getStartDate()));
			globalDataMap.put("enddate", matter.format(node.getEndDate()));
			globalDataMap.put("lastendorseno", wContractBo.getPreEndorseNo(endorseNo));
			// globalDataMap.put("endorsetext", node.getEndorseText());
			globalDataMap.put("productcode", node.getProductCode());
			
			singleProductMappingBuilder.setBContractNode(node);
			singleProductMappingBuilder.setGlobalDataMap(globalDataMap);
			// Long c1 = System.currentTimeMillis();
			// log.info("处理节点的时间1：" + (c1 - c));
			singleProductMappingBuilder.execute();
			// Long d = System.currentTimeMillis();
			// log.info("处理节点的时间：" + (d - c1));
			result.addAll(singleProductMappingBuilder.getResult());
			if (Runtime.getRuntime().freeMemory() < 100 * 1024 * 1024) {
				// log.info("GC啦！！！！");
				System.gc();
			}
			j.setNow(++countCharY);
			j.setTotal(countsOfNonRisk);
			// f = System.currentTimeMillis();
			// log.info("个人认为可以忽略的时间：" + (f - d));
		}
	}

	public String getObject_no() {
		return object_no;
	}

	public void setObject_no(String object_no) {
		this.object_no = object_no;
	}

	public boolean isWrite() {
		return write;
	}

	public void setWrite(boolean write) {
		this.write = write;
	}

	public String getDealMode() {
		return dealMode;
	}

	public void setDealMode(String dealMode) {
		this.dealMode = dealMode;
	}

}
