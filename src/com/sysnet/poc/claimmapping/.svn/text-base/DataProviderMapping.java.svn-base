package com.sysnet.poc.claimmapping;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pharos.claim.core.bo.ClaimQueryBO;
import pharos.claim.core.dao.qo.ClaimVersionQO;
import pharos.claim.core.domain.Accident;
import pharos.claim.core.domain.Claim;
import pharos.claim.core.domain.ConditionPayment;
import pharos.claim.core.domain.PartOfClaim;
import pharos.claim.core.domain.PolicyRiskPayment;
import pharos.claim.core.domain.Procedure;
import pharos.claim.core.domain.Transaction;
import pharos.ctm.vo.BDeclarationForm;
import pharos.ctm.vo.BDeclarationFormList;
import pharos.framework.core.PharosEJBException;
import pharos.framework.element.vo.IElement;
import pharos.framework.p.ElementEnum;

import com.sysnet.poc.claimmapping.vo.ClaimInfo;
import com.sysnet.poc.claimmapping.vo.DataProvide;
import com.sysnet.poc.claimmapping.vo.MappingDescription;
import com.sysnet.poc.control.manage.vo.EtlPharosIncrClaim;
import com.sysnet.poc.util.PharosServiceContainer;
import com.sysnet.poc.util.ResourceBundleUtil;

/**
 * 数据提供者初始化
 * 
 * @author 段成伟
 * 
 */
public class DataProviderMapping {

	// private static final Log log = OdsLogger.getLog("claimLog4j", "Claim");
	private Map<String, List<?>> dataProvider = new HashMap<String, List<?>>();

	/**
	 * 获取数据提供者List
	 * 
	 * @param name
	 * @return
	 */
	public List<?> getDataProvider(String name) {
		return dataProvider.get(name);
	}

	/**
	 * 获取只有一个的数据提供者
	 * 
	 * @param name
	 * @return
	 */
	public Object getOnlyDataProvider(String name) {
		if (dataProvider.get(name) != null) {
			return dataProvider.get(name).get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 取最大版本的赔案
	 * @author Frank Zhang
	 * @throws Exception
	 */
	public Claim getLastestClaimInList(List<Claim> claimList) throws Exception {
		if( 0 == claimList.size() || null == claimList ) {
			return null;
		}
		
		Claim claim = claimList.get(0);
		
		for( int i=0; i < claimList.size(); i++) {
			if( claimList.get(i).getVersionNo() > claim.getVersionNo() ) {
				claim = claimList.get(i);
			}
		}
		
		return claim;
	}
	
	/**
	 * 取最大有效版本的赔案
	 * @author Frank Zhang
	 * @throws Exception
	 */
	public Claim getLastestEffectClaimInList(List<Claim> claimList) throws Exception {
		if( 0 == claimList.size() || null == claimList ) {
			return null;
		}
		
		Claim claim = claimList.get(0);
		
		for( int i=0; i < claimList.size(); i++ ) {
			if( claimList.get(i).getVersionNo() > claim.getVersionNo() && !"200011".equals(claimList.get(i).getBizPoint()) ) {
				claim = claimList.get(i);
			}
		}
		
		return claim;
	}
	
	/**
	 * 取追偿版本
	 * @author Frank Zhang
	 * @throws Exception
	 */
	public List<Claim> getRecClaimInList(List<Claim> claimList) throws Exception {
		
		if( 0 == claimList.size() || null == claimList ) {
			return null;
		}
		
		List<Claim> newClaimList = new ArrayList<Claim>();
		
		for( int i=0; i < claimList.size(); i++ ) {
			if( claimList.get(i).getBizPoint().equals("200016") || claimList.get(i).getBizPoint().equals("200017") || claimList.get(i).getBizPoint().equals("200018")) {
				newClaimList.add(claimList.get(i));
			}
		}
		
		return newClaimList;
	}
	
	/**
	 * 取某岗位的所有赔案版本
	 * @author Frank Zhang
	 * @throws Exception
	 */
	public List<Claim> getClaimSetByBizpoint(List<Claim> claimList, String bp)  throws Exception {
		List<Claim> cl = new ArrayList<Claim>();
		
		if( 0 == claimList.size() || null == claimList ) {
			return cl;
		}
		
		for( int i = 0; i < claimList.size(); i++) {
			if(bp.equals(claimList.get(i).getBizPoint())) {
				cl.add(claimList.get(i));
			}
		}
		
		return cl;
	}

	/**
	 * 初始化数据提供者
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ClaimInfo initDataProvider(EtlPharosIncrClaim ic) throws Exception {
		ClaimInfo ci = new ClaimInfo();
		List<Claim> claimVersionSet = null, recClaimSet = null;
		Claim claim = null, newestClaim, recClaim = null;
		PharosServiceContainer pc = PharosServiceContainer.Instance();
		ClaimQueryBO claimQueryBO = pc.getClaimQueryBo();
		if (claimQueryBO == null) {
			return null;
		}
		try {
			// claim
//			Long a = System.currentTimeMillis();
			//claim = claimQueryBO.getClaim(ic.getClaimId(), ic.getTransactionType());
			claimVersionSet = claimQueryBO.getClaimVersions4ODS(ic.getClaimId(), "", ic.getTransactionType());
			if( null == claimVersionSet ) {
				return null;
			}
			newestClaim = claimQueryBO.getClaim4ODS(ic.getClaimId(), ic.getTransactionType());
			if( null == newestClaim ) {
				return null;
			}
	
			claim = getLastestEffectClaimInList(claimVersionSet);
			if (claim == null) {
				return null;
			}
			
//			Long b = System.currentTimeMillis();
//			System.out.println("getLastVersion:" + (b - a));
			
			List<Claim> l = new ArrayList<Claim>();
			l.add(claim);
			dataProvider.put("Claim", l);
			recClaimSet = getRecClaimInList(claimVersionSet);	//追偿版本集合
			recClaim = getLastestClaimInList(recClaimSet);
		} catch (PharosEJBException e) {
			throw e;
		}
		List<Accident> al = new ArrayList<Accident>();
		al.add(claim.getAccident());
		dataProvider.put("Accident", al);
		// dataProvider.put("PartOfClaim", claim.getParts());
		Map<String, String> globalData = new HashMap<String, String>();
		globalData.put("batid", ic.getBatId() + "");
		globalData.put("objectid", claim.getReportNo());
		ci.setReportNo(claim.getReportNo());
		globalData.put("objecttype", ic.getObjectType() + "");
		globalData.put("insurecompanyid", ResourceBundleUtil.getCompanyId());
		globalData.put("insurecompanycode", ResourceBundleUtil.getCompanyCd());
		
		//Claim上的各种号
		globalData.put("claimid", claim.getId() + "");			//理赔ID
		globalData.put("policyno", claim.getPolicyNo());		//保单号
		globalData.put("claimno", claim.getClaimNo());			//赔案号
		globalData.put("contractno", claim.getContractNo());	//合同号
		globalData.put("casereportno", claim.getReportNo());	//报案号
		globalData.put("registno", claim.getRegistNo());		//立案号
		globalData.put("reopenno", claim.getReopenNo());		//重开号
		globalData.put("archiveno", newestClaim.getArchiveNo());		//归档号
		globalData.put("closeno", newestClaim.getCloseNo());			//结案号
		globalData.put("claimstatus", newestClaim.getClaimStatus());	//赔案状态
		if( null != newestClaim.getIfRepay() ) {
			if( true == newestClaim.getIfRepay() ) {	//是否追偿
				globalData.put("ifrepay", "1");	
			} else {
				globalData.put("ifrepay", "0");	
			}
		} else {
			globalData.put("ifrepay", "0");	
		}
		globalData.put("recoveryregno", claim.getRecoveryRegNo());	//追偿立案号
		globalData.put("recoverycloseno", claim.getRecoveryCloseNo());	//追偿结案号
		globalData.put("tempreportno", claim.getTempReportNo());	//临时报案号
		if( "".equals(claim.getEndorseNo()) || null == claim.getEndorseNo()) {//批单号
			globalData.put("endorseno", claim.getPolicyNo());
		} else {
			globalData.put("endorseno", claim.getEndorseNo());		
		}
		
		//Claim上的各种时间
		SimpleDateFormat matter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		
//		globalData.put("claimdate", matter.format(claim.getClaimDate() == null ? new Date(0) : claim.getLossDate()));	//立案日期----不使用！！
//		globalData.put("lossdate", matter.format(claim.getLossDate() == null ? new Date(0) : claim.getLossDate()));		//出险日期
//		globalData.put("closedate", matter.format(claim.getCloseDate() == null ? new Date(0) : claim.getCloseDate()));		//出险日期
//		globalData.put("reopendate", matter.format(claim.getReopenDate() == null ? new Date(0) : claim.getReopenDate()));		//重开日期
//		globalData.put("reportdate", matter.format(claim.getReportDate() == null ? new Date(0) : claim.getReportDate()));		//报案日期
//		globalData.put("registerdate", matter.format(claim.getRegisterDate() == null ? new Date(0) : claim.getRegisterDate()));	//立案日期
//		globalData.put("rejectdate", matter.format(claim.getRejectDate() == null ? new Date(0) : claim.getRejectDate()));		//拒绝立案日期、拒赔日期
//		globalData.put("archivedate", matter.format(claim.getArchiveDate() == null ? new Date(0) : claim.getArchiveDate()));	//结案归档日期
//		globalData.put("recoveryregdate", matter.format(claim.getRecoveryRegDate() == null ? new Date(0) : claim.getRecoveryRegDate()));		//追偿立案日期
//		globalData.put("recoveryclosedate", matter.format(claim.getRecoveryCloseDate() == null ? new Date(0) : claim.getRecoveryCloseDate()));	//追偿结案日期
		
		if( null == claim.getClaimDate() ) {//报案登记日期
			globalData.put("claimdate", null);
		} else {
			globalData.put("claimdate", matter.format(claim.getClaimDate()));	
		}

		if( null == claim.getLossDate() ) {//出险日期
			globalData.put("lossdate", null);		
		} else {
			globalData.put("lossdate", matter.format(claim.getLossDate()));	
		}
		
		if( null == claim.getCloseDate() ) {//结案日期
			globalData.put("closedate", null);
		} else {
			globalData.put("closedate", matter.format(claim.getCloseDate()));	
		}
		
		if( null == claim.getReopenDate() ) {//重开日期
			globalData.put("reopendate", null);		
		} else {
			globalData.put("reopendate", matter.format(claim.getReopenDate()));	
		}
		
		if( null == claim.getReportDate() ) {//报案日期
			globalData.put("reportdate", null);		
		} else {
			globalData.put("reportdate", matter.format(claim.getReportDate()));	
		}
		
		if( null == claim.getRegisterDate() ) {//立案日期
			globalData.put("registerdate", null);		
		} else {
			globalData.put("registerdate", matter.format(claim.getRegisterDate()));	
		}
		
		if( null == claim.getRejectDate() ) {//拒绝立案日期、拒赔日期
			globalData.put("rejectdate", null);		
		} else {
			globalData.put("rejectdate", matter.format(claim.getRejectDate()));	
		}
		
		if( null == claim.getArchiveDate() ) {//结案归档日期
			globalData.put("archivedate", null);		
		} else {
			globalData.put("archivedate", matter.format(claim.getArchiveDate()));	
		}
		
		if( null == claim.getRecoveryRegDate() ) {//追偿立案日期
			globalData.put("recoveryregdate", null);		
		} else {
			globalData.put("recoveryregdate", matter.format(claim.getRecoveryRegDate()));	
		}
		
		if( null == claim.getRecoveryCloseDate() ) {//追偿结案日期
			globalData.put("recoveryclosedate", null);		
		} else {
			globalData.put("recoveryclosedate", matter.format(claim.getRecoveryCloseDate()));	
		}
		
		if( null == claim.getPutInClaimTime() ) {
			globalData.put("putinclaimtime", null);
		} else {
			globalData.put("putinclaimtime", matter.format(claim.getPutInClaimTime()));	
		}
				
		//Claim上的其他东西
		globalData.put("losslocation", claim.getLossLocation());	//出险地点
		globalData.put("lossreason", claim.getLossReason());	//出险原因
		globalData.put("policydept", claim.getCompanyNodeCode());	//承保机构
		
		List<Map<String, String>> global = new ArrayList<Map<String, String>>();
		global.add(globalData);
		dataProvider.put("GLOBAL", global);
		MappingDescription md = MappingCacheManagerClaim.getMapping(claim.getCode(), ic.getTransactionType());
		List<DataProvide> dpl = md.getDataProvide();
		//Long a = System.currentTimeMillis();
		for (DataProvide dp : dpl) {
			if (dp.getNodeName() != null && !"".equals(dp.getNodeName())) {
				if (dp.getProvideType().equals("DECLARATIONFORMLIST")) {
					List<BDeclarationFormList> dl = new ArrayList<BDeclarationFormList>();
					String[] temp = dp.getNodeName().split("/");
					List<IElement> le = (List<IElement>) getDataProvider(temp[0]);
					for (IElement element : le) {
						BDeclarationFormList bdfl = (BDeclarationFormList) element.getElementByName(ElementEnum.BDECLARATIONFORMLIST_CODE, temp[1], false);
						if (bdfl != null) {
							List<BDeclarationFormList> dlinsert = bdfl.getDecFormValueList();
							for (BDeclarationFormList declarationFormList : dlinsert) {
								declarationFormList.setParent(bdfl.getParent());
								declarationFormList.setDDeclarationFormList(bdfl.getDDeclarationFormList());
							}
							dl.addAll(dlinsert);
						}
					}
					dataProvider.put(dp.getName(), dl);
				} else if (dp.getProvideType().equals("DECLARATIONFORM")) {
					String[] temp = dp.getNodeName().split("/");
					List<IElement> le = (List<IElement>) getDataProvider(temp[0]);
					List<BDeclarationForm> bdfl = new ArrayList<BDeclarationForm>();
					for (IElement element : le) {
						BDeclarationForm dform = (BDeclarationForm) element.getElementByName(ElementEnum.BDECLARATIONFORM_CODE, temp[1], true);
						bdfl.add(dform);
					}
					dataProvider.put(dp.getName(), bdfl);
				} else if (dp.getProvideType().equals("CLAIMVERSON")) {
					//dataProvider.put(dp.getName(), claimQueryBO.getClaimVersions(ic.getClaimId(), dp.getNodeName(), ic.getTransactionType()));
					//dataProvider.put(dp.getName(), claimQueryBO.getClaimVersions4ODS(ic.getClaimId(), dp.getNodeName(), ic.getTransactionType()));
					dataProvider.put(dp.getName(), getClaimSetByBizpoint(claimVersionSet, dp.getNodeName()));
				} else if (dp.getProvideType().equals("NEWESTCLAIM")) {
					List<Claim> l = new ArrayList<Claim>();
					l.add(newestClaim);
					dataProvider.put(dp.getName(), l);
				} else if (dp.getProvideType().equals("CLAIMWHEN")) {
					List<Claim> l = new ArrayList<Claim>();
					
					if( dp.getWhen().equals("ifrepay") && !globalData.get(dp.getWhen()).equalsIgnoreCase("") ) {
						if( null != recClaim ) {
							l.add(recClaim);
						}
					}
					dataProvider.put(dp.getName(), l);
				} else if (dp.getProvideType().equals("LASTCLAIMVERSION") ) {
					List<Claim> l = new ArrayList<Claim>();
					l.add(getLastestClaimInList(claimVersionSet));
					dataProvider.put(dp.getName(), l);
				} else if (dp.getProvideType().equals("LASTEFFECTCLAIMVERSION") ) {
					List<Claim> l = new ArrayList<Claim>();
					l.add(getLastestEffectClaimInList(claimVersionSet));
					dataProvider.put(dp.getName(), l);
				} else if (dp.getProvideType().equals("CVPRP")) {
					List<PolicyRiskPayment> prpl = new ArrayList<PolicyRiskPayment>();
					String[] temp = dp.getNodeName().split("/");
					List<Claim> le = (List<Claim>) getDataProvider(temp[0]);
					for (Claim claimv : le) {
						prpl.addAll(Arrays.asList(claimv.getPolicyRiskPaymentByCode(temp[1])));
					}
					dataProvider.put(dp.getName(), prpl);
				} else if (dp.getProvideType().equals("PARTOFCLAIM")) {
					List<PartOfClaim> pocl = new ArrayList<PartOfClaim>();
					String[] temp = dp.getNodeName().split("/");
					List<Claim> cl = (List<Claim>) getDataProvider(temp[0]);
					for (Claim claimv : cl) {
						// Modify by Frank Zhang at 20140616, cause the immigrate-data can't provide the correct "effect name" to him.
						//pocl.addAll(claimv.getPartOfClaimsByEffectName(temp[1]));
						pocl.addAll(claimv.getPartOfClaimsByEffect(temp[1]));
					}
					dataProvider.put(dp.getName(), pocl);
					// } else if (dp.getProvideType().equals("PRPVPOC")) {
					// List<PartOfClaim> pocl = new ArrayList<PartOfClaim>();
					// List<PolicyRiskPayment> cl = (List<PolicyRiskPayment>) getDataProvider(dp.getNodeName());
					// for (PolicyRiskPayment prp : cl) {
					// pocl.addAll(prp.getPartOfClaims());
					// }
					// dataProvider.put(dp.getName(), pocl);
					// }else if (dp.getProvideType().equals("CPVP")) {
					// List<Procedure> pl = new ArrayList<Procedure>();
					// List<ConditionPayment> le = (List<ConditionPayment>) getDataProvider(dp.getNodeName());
					// for (ConditionPayment cp : le) {
					// cp.getConditionCode()
					// pl.addAll(cp.getProcedures());
					// }
					// dataProvider.put(dp.getName(), pl);
				} else if (dp.getProvideType().equals("PRPVCP")) {
					List<ConditionPayment> cpl = new ArrayList<ConditionPayment>();
					String[] temp = dp.getNodeName().split("/");
					List<PolicyRiskPayment> le = (List<PolicyRiskPayment>) getDataProvider(temp[0]);
					for (PolicyRiskPayment prp : le) {
						cpl.addAll(prp.getConditionPaymentsByDCode(temp[1]));
					}
					dataProvider.put(dp.getName(), cpl);
				} else if (dp.getProvideType().equals("PVDPR")) {
					String[] temp = dp.getNodeName().split("/");
					List<PartOfClaim> pl = (List<PartOfClaim>) dataProvider.get(temp[0]);
					List<Procedure> pll = new ArrayList<Procedure>();
					for (PartOfClaim partOfClaim : pl) {
						pll.addAll(partOfClaim.getProceduresByDCode(temp[1]));
					}
					dataProvider.put(dp.getName(), pll);
				} else if (dp.getProvideType().equals("PRPVTransactionAll")) {
					List<PolicyRiskPayment> prpl = (List<PolicyRiskPayment>) dataProvider.get(dp.getNodeName());
					List<Transaction> tl = new ArrayList<Transaction>();
					for (PolicyRiskPayment policyRiskPayment : prpl) {
						tl.addAll(policyRiskPayment.getTransactions());
					}
					dataProvider.put(dp.getName(), tl);
				} else if (dp.getProvideType().equals("PRPVTransaction")) {
					List<Transaction> trans = new ArrayList<Transaction>();
					String[] temp = dp.getNodeName().split("/");
					List<PolicyRiskPayment> le = (List<PolicyRiskPayment>) getDataProvider(temp[0]);
					for (PolicyRiskPayment prp : le) {
						trans.addAll(prp.getTransactions(0, temp[1]));
					}
					dataProvider.put(dp.getName(), trans);
				} else if (dp.getProvideType().equals("PRPVTransactionCode")) {
					List<Transaction> trans = new ArrayList<Transaction>();
					String[] temp = dp.getNodeName().split("/");
					List<PolicyRiskPayment> prp = (List<PolicyRiskPayment>) getDataProvider(temp[0]);
					for (PolicyRiskPayment policyRiskPayment : prp) {
						for( Transaction transaction : policyRiskPayment.getTransactions()) {
							if( transaction.getdTransTypeCode().equals(temp[1]))
								trans.add(transaction);
						}
					}
					dataProvider.put(dp.getName(), trans);
				}
			}
		}
		//System.out.println("getLastVersion:" + (System.currentTimeMillis() - a));
		ci.setCode(claim.getCode());
		ci.setMd(md);
		return ci;
	}
}
