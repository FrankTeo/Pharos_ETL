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

import pharos.ctm.bo.BContractBO;
import pharos.ctm.bo.WContractBO;
import pharos.ctm.vo.BContractNode;
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

public class ProposalNonRiskNodeThead {
	private Map<Long, BContractNode> bnMap;
	private String dealMode;

	public Map<Long, BContractNode> getBnMap() {
		return bnMap;
	}

	public void setBnMap(Map<Long, BContractNode> bnMap) {
		this.bnMap = bnMap;
	}

	InsideProcess j = new InsideProcess();
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

	public ProposalNonRiskNodeThead(List<InsideProcess> a) {
		super();
		j.setName("非风险");
		a.add(j);
	}

	private static final Log log = OdsLogger.getLog("insureLog4j", "Insure");
	private String errorMessage = "";

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	private List<Long> nonRiskNodeIDlist = null;

	private int countCharY = 0;

	private BContractBO bContractBo;
	private boolean write;
	private String object_no;
	private String node_No;

	private WContractBO wContractBO;

	private String PRODUCT_CD;

	private String PRODUCT_NODE_CD;

	private String object_Id;

	private String bat_Id;

	private String object_Type;

	private String riskname;

	public String getRiskname() {
		return riskname;
	}

	public void setRiskname(String riskname) {
		this.riskname = riskname;
	}

	// private DCS dcs = null;

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

	private StopBean stopBean;

	public WContractBO getWContractBO() {
		return wContractBO;
	}

	public void setWContractBO(WContractBO contractBO) {
		wContractBO = contractBO;
	}

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

	public BContractBO getBContractBo() {
		return bContractBo;
	}

	public void setBContractBo(BContractBO contractBo) {
		bContractBo = contractBo;
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
				storage.extendSQL("call rmnode.removeproposal(?)", new String[] { object_no });
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
			stgLog.setErrorCode("3");
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
		}
		stopBean.addStop();
	}

	@SuppressWarnings("unchecked")
	public void toProcessNonRiskNode() throws Exception {
		SimpleDateFormat matter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		// long ftimes = 0;
		int countsOfNonRisk = nonRiskNodeIDlist.size();
		for (int temp = 0; temp < countsOfNonRisk; temp++) {
			Long riskNodeid = (Long) nonRiskNodeIDlist.get(temp);
			// BContractNode node = bContractBo.getContractNode(riskNodeid);
			// WContractNode node = (WContractNode) bnMap.get(riskNodeid);
			//Long a = System.currentTimeMillis();
			WContractNode node = wContractBO.getSingleNode(riskNodeid, false);
			if (node.getNodeNo().equals("1")) {
				node = wContractBO.getContractNode(riskNodeid, false);
			}
			//Long b = System.currentTimeMillis();
			//log.info("取节点的时间：" + (b - a));
			String policyNo = node.getPolicyNo();
			String nodeProposalNo = node.getProposalNo();
			String dcsNodeId = node.getDcsNodeCode();

			// 调用子模版
			// ftimes = System.currentTimeMillis();
			ProductMappingBuilder singleProductMappingBuilder = new ProductMappingBuilder(PRODUCT_CD, node, "3", null, wContractBO);
			// MappingCacheManager mcm = new MappingCacheManager();
			// singleProductMappingBuilder.setMcm(mcm);
			singleProductMappingBuilder.setMapDocument(MappingCacheManager.getMapping(PRODUCT_CD, PRODUCT_NODE_CD, "3_1_" + dcsNodeId));
			// log.info("ProposalNonRiskNodeThead的singleBranch模版共耗时:"+(System.currentTimeMillis()-ftimes));

			// ftimes = System.currentTimeMillis();
			// WContract wContract = wContractBO.getContractSingleBranch(0L, riskNodeid, false);
			// singleBContract.setDcs(this.dcs);
			// log.info("ProposalNonRiskNodeThead取非风险节点共耗时:"+(System.currentTimeMillis()-ftimes));

			Map<String, String> globalDataMap = new HashMap<String, String>();
			globalDataMap.put("policyno", policyNo);
			globalDataMap.put("proposalno", nodeProposalNo);
			globalDataMap.put("nodeno", node.getNodeNo());
			globalDataMap.put("objectno", object_no);
			globalDataMap.put("objectid", object_Id);
			//globalDataMap.put("mainversion", node.getMainVersion() + "");
			globalDataMap.put("version", node.getVersion() + "");
			globalDataMap.put("quotationno", node.getQuotationNo());
			globalDataMap.put("subjectno", node.getDcsNodeCode());
			if(null != node.getOriginalDate() && !"".equals(node.getOriginalDate())) {
				globalDataMap.put("origindate", matter.format(node.getOriginalDate()));
			} else {
				globalDataMap.put("origindate", null);
			}
			if(null != node.getStartDate() && !"".equals(node.getStartDate())) {
				globalDataMap.put("startdate", matter.format(node.getStartDate()));
			} else {
				globalDataMap.put("startdate", null);
			}
			if(null != node.getEndDate() && !"".equals(node.getEndDate())) {
				globalDataMap.put("enddate", matter.format(node.getEndDate()));
			} else {
				globalDataMap.put("enddate", null);
			}
			globalDataMap.put("productcode", node.getProductCode());
			// ftimes = System.currentTimeMillis();
			singleProductMappingBuilder.setBContractNode(node);
			singleProductMappingBuilder.setGlobalDataMap(globalDataMap);
			singleProductMappingBuilder.execute();
			// log.info("ProposalNonRiskNodeThead的196共耗时:"+(System.currentTimeMillis()-ftimes));

			// ftimes = System.currentTimeMillis();
			// StorageTransAction storage = new StorageTransAction();
			// storage.setConnection(conn);
			// storage.setSColumns(singleProductMappingBuilder.getResult());
			// storage.buildStatement();
			// log.info("ProposalNonRiskNodeThead的singleBranch拼sql共耗时"+(System.currentTimeMillis()-ftimes));

			// ftimes = System.currentTimeMillis();
			// storage.execute();
			// log.info("ingleBranch入库共耗时"+System.currentTimeMillis()+";共耗时:"+(System.currentTimeMillis()-ftimes));

			// singleProductMappingBuilder = null;
			result.addAll(singleProductMappingBuilder.getResult());
			/* if(countCharY%20==9) */
			if (Runtime.getRuntime().freeMemory() < 100 * 1024 * 1024) {

				// ftimes = System.currentTimeMillis();
				// log.debug("垃圾回收前=========="+ftimes);
				System.gc();
				// log.debug("垃圾回收后
				// =========="+System.currentTimeMillis()+";共耗时:"+(System.currentTimeMillis()-ftimes));
			}
			// log.info("==投保单==非风险节点总数：" + countsOfNonRisk + "==当前：" +
			// countCharY++);
			j.setNow(++countCharY);
			j.setTotal(countsOfNonRisk);
		}
	}

	public boolean isWrite() {
		return write;
	}

	public void setWrite(boolean write) {
		this.write = write;
	}

	public String getObject_no() {
		return object_no;
	}

	public void setObject_no(String object_no) {
		this.object_no = object_no;
	}

	public String getNode_No() {
		return node_No;
	}

	public void setNode_No(String node_No) {
		this.node_No = node_No;
	}
	
	public String getDealMode() {
		return dealMode;
	}

	public void setDealMode(String dealMode) {
		this.dealMode = dealMode;
	}
}
