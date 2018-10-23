package com.sysnet.poc.util;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import pharos.account.bo.IAccountInvoiceBO;
import pharos.account.bo.IApplicationBO;
import pharos.account.bo.IBankAccountBO;
import pharos.account.bo.IBankTranferBO;
import pharos.account.bo.IGeneralLedgerBO;
import pharos.account.bo.IODSBO;
import pharos.account.bo.IReferenceServiceBO;
import pharos.account.bo.ITransactionBO;
import pharos.claim.core.bo.ClaimQueryBO;
import pharos.ctm.bo.BContractBO;
import pharos.ctm.bo.WContractBO;
import pharos.framework.health.bo.DProcedureBO;
import pharos.framework.health.bo.DProcedureGroupBO;
import pharos.framework.ods.service.ODSEFService;
import pharos.framework.parameter.bo.ParameterBO;
import pharos.framework.servicectrl.ServiceLocator;
import pharos.party.bo.PartyBO;
import pharos.party.bo.PartyQueryBO;
import pharos.security.bo.SearchUserBO;
import pharos.security.bo.UserEntryBO;
import pharos.wf.bo.WorkflowWAPI;

public class PharosServiceContainer {
	private static Context ctx = null;

	private SearchUserBO searchUserBO = null;
	private PartyBO partyBO = null;
	private UserEntryBO userEntryBo = null;

	private ODSEFService oDSEFService = null;
	private BContractBO bContractBO = null;
	private WContractBO wContractBO = null;
	private ClaimQueryBO claimQueryBo = null;
	private PartyQueryBO partyQueryBO = null;
	private ParameterBO parameterBO = null;
	private IODSBO iODSBO = null;
	private ITransactionBO iTransactionBO = null;

	private IAccountInvoiceBO iAccountInvoiceBO = null; // 发票本
	private IReferenceServiceBO iReferenceServiceBO = null; // 收付
	private IBankAccountBO iBankAccountBO = null; // 支票本
	private IBankTranferBO iBankTranferBO = null; // 银行转帐
	private IGeneralLedgerBO iGeneralLedgerBO = null;

	private IApplicationBO iApplicationBO = null;
	private DProcedureGroupBO dProcedureGroupBO = null;// 收费项目分组
	private DProcedureBO dProcedureBO = null;// 收费项目
	private WorkflowWAPI wordflowWAPI = null;
	/**
	 * 初始化
	 */
	static {
		String url = ResourceBundleUtil.getServiceURL();
		System.out.println("程序连接 pharos ：" + url);
		Properties prop = new Properties();
		prop.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		prop.setProperty(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
		prop.setProperty(Context.PROVIDER_URL, url);
		try {
			ctx = new InitialContext(prop);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		// 卫建军修改 20090821
		System.getProperties().put("defaultServerAddress", ResourceBundleUtil.getServiceURL());
		System.getProperties().put("isStartCache", "true");
		ServiceLocator.initClient();
		ServiceLocator.setServiceContextGetter(new MyServiceContextGetter());
	}
	private static PharosServiceContainer psc = null;

	/**
	 * 基本构造方法
	 */
	private PharosServiceContainer() {

	}

	public SearchUserBO getSearchUserBO() {
		try {
			searchUserBO = (SearchUserBO) ServiceLocator.lookupEJBService(SearchUserBO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return searchUserBO;
	}

	public PartyBO getPartyBO() {
		try {
			partyBO = (PartyBO) ServiceLocator.lookupEJBService(PartyBO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return partyBO;
	}

	/**
	 * 返回用户对象服务
	 * 
	 * @return
	 */
	public UserEntryBO getUserEntryBO() {
		try {
			userEntryBo = (UserEntryBO) ServiceLocator.lookupEJBService(UserEntryBO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userEntryBo;
	}

	/**
	 * 返回服务容器对象
	 * 
	 * @return
	 */
	public static PharosServiceContainer Instance() {
		if (psc == null) {
			psc = new PharosServiceContainer();
		}
		return psc;
	}

	/**
	 * 返回产品服务
	 * 
	 * @param jndiName
	 * @return
	 */
	public ODSEFService getODSEFService() {
		try {
			oDSEFService = (ODSEFService) ctx.lookup("ODSEFServiceImpl/remote");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return oDSEFService;
	}

	/**
	 * 返回承保服务BContractBO
	 * 
	 * @return
	 */
	public BContractBO getBContractBO() {
		try {
			bContractBO = (BContractBO) ServiceLocator.lookupEJBService(BContractBO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bContractBO;
	}

	/**
	 * 返回承保服务WContractBO
	 * 
	 * @return
	 */
	public WContractBO getWContractBO() {
		try {
			wContractBO = (WContractBO) ServiceLocator.lookupEJBService(WContractBO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wContractBO;
	}

	/**
	 * 返回理赔查询服务
	 * 
	 * @return
	 */
	public ClaimQueryBO getClaimQueryBo() {
		try {
			claimQueryBo = (ClaimQueryBO) ServiceLocator.lookupEJBService(ClaimQueryBO.class);
		} catch (java.lang.RuntimeException re) {
			re.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return claimQueryBo;
	}

	/**
	 * 返回客户服务
	 * 
	 * @return
	 */
	public PartyQueryBO getPartyQueryBO() {
		try {
			partyQueryBO = (PartyQueryBO) ServiceLocator.lookupEJBService(PartyQueryBO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return partyQueryBO;
	}

	/**
	 * 返回客户参数服务
	 * 
	 * @return
	 */
	public ParameterBO getParameterBO() {
		try {
			parameterBO = (ParameterBO) ServiceLocator.lookupEJBService(ParameterBO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parameterBO;
	}

	/**
	 * 缴费计划
	 * 
	 * @return
	 */
	public IODSBO getIODSBO() {
		try {
			iODSBO = (IODSBO) ServiceLocator.lookupEJBService(IODSBO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iODSBO;
	}

	/**
	 * 收付费服务
	 * 
	 * @return
	 */
	public ITransactionBO getITransactionBO() {
		try {
			iTransactionBO = (ITransactionBO) ServiceLocator.lookupEJBService(ITransactionBO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iTransactionBO;
	}

	/**
	 * 发票本
	 * 
	 * @return
	 */
	public IAccountInvoiceBO getIAccountInvoiceBO() {
		try {
			iAccountInvoiceBO = (IAccountInvoiceBO) ServiceLocator.lookupEJBService(IAccountInvoiceBO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iAccountInvoiceBO;
	}

	/**
	 * 收付方案、收付比例、收付机制表
	 * 
	 * @return
	 */
	// public IReferenceServiceBO getIReferenceServiceBO() {
	// try {
	// iReferenceServiceBO =
	// (IReferenceServiceBO)ctx.lookup("ReferenceServiceBOImpl/remote");
	// } catch (NamingException e) {
	// e.printStackTrace();
	// }
	// return iReferenceServiceBO;
	// }
	/**
	 * 支票本
	 * 
	 * @return
	 */
	public IBankAccountBO getIBankAccountBO() {
		try {
			iBankAccountBO = (IBankAccountBO) ServiceLocator.lookupEJBService(IBankAccountBO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iBankAccountBO;
	}

	/**
	 * 银行转帐
	 * 
	 * @return
	 */
	public IBankTranferBO getIBankTranferBO() {
		try {
			iBankTranferBO = (IBankTranferBO) ServiceLocator.lookupEJBService(IBankTranferBO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iBankTranferBO;
	}

	public DProcedureGroupBO getDProcedureGroupBO() {
		try {

			dProcedureGroupBO = (DProcedureGroupBO) ServiceLocator.lookupEJBService(DProcedureGroupBO.class);
			// dProcedureGroupBO =
			// (DProcedureGroupBO)ctx.lookup("DProcedureGroupBO/remote");

			dProcedureGroupBO = (DProcedureGroupBO) ServiceLocator.lookupEJBService(DProcedureGroupBO.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dProcedureGroupBO;
	}

	/**
	 * 收费项目信息服务
	 * 
	 * @return
	 */
	public DProcedureBO getDProcedureBO() {
		try {
			dProcedureBO = (DProcedureBO) ServiceLocator.lookupEJBService(DProcedureBO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dProcedureBO;
	}

	public IGeneralLedgerBO getIGeneralLedgerBO() {
		try {
			iGeneralLedgerBO = (IGeneralLedgerBO) ServiceLocator.lookupEJBService(IGeneralLedgerBO.class);
			// dProcedureGroupBO =
			// (DProcedureGroupBO)ctx.lookup("DProcedureGroupBO/remote");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iGeneralLedgerBO;
	}

	public IReferenceServiceBO getIReferenceServiceBO() {
		try {
			iReferenceServiceBO = (IReferenceServiceBO) ServiceLocator.lookupEJBService(IReferenceServiceBO.class);
			// dProcedureGroupBO =
			// (DProcedureGroupBO)ctx.lookup("DProcedureGroupBO/remote");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iReferenceServiceBO;

	}

	public IApplicationBO getIApplicationBO() {
		try {
			iApplicationBO = (IApplicationBO) ServiceLocator.lookupEJBService(IApplicationBO.class);
			// dProcedureGroupBO =
			// (DProcedureGroupBO)ctx.lookup("DProcedureGroupBO/remote");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iApplicationBO;
	}

	public WorkflowWAPI getWorkflowWAPI() {
		try {
			wordflowWAPI = (WorkflowWAPI) ServiceLocator.lookupEJBService(WorkflowWAPI.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wordflowWAPI;
	}
}
