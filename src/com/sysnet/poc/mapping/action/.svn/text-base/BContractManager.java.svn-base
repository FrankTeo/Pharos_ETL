package com.sysnet.poc.mapping.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pharos.ctm.bo.BContractBO;
import pharos.ctm.bo.WContractBO;
import pharos.ctm.condition.nonlife.vo.BConditionNonLife;
import pharos.ctm.exception.CTMException;
import pharos.ctm.vo.BContractNode;
import pharos.ctm.vo.BContractNodeBaseInfo;
import pharos.ctm.vo.BDeclaration;
import pharos.ctm.vo.BDeclarationForm;
import pharos.ctm.vo.BDeclarationFormList;
import pharos.ctm.vo.BRole;
import pharos.ctm.vo.BaseCondition;
import pharos.ctm.vo.Coinsurance;
import pharos.ctm.vo.Installment;
import pharos.ctm.vo.InstallmentDetail;
import pharos.ctm.vo.Joinsurance;
import pharos.ctm.vo.MultiChannelInfo;
import pharos.ctm.vo.SalesFeeDetail;
import pharos.ctm.vo.UnderwriteHistory;
import pharos.ctm.vo.WContractNode;
import pharos.ctm.vo.WContractNodeBaseInfo;
import pharos.framework.element.vo.IElement;
import pharos.framework.p.ElementEnum;

import com.sysnet.poc.mapping.PharosNodeType;
import com.sysnet.poc.mapping.vo.DataProvide;
import com.sysnet.poc.mapping.vo.OD;
import com.sysnet.poc.partymapping.ReflectUtil;

/**
 * BContract管理类
 * 
 * @author li_yanpeng
 * 
 */
@SuppressWarnings("unchecked")
public class BContractManager {
	// public BContractManager(Map<Long, BContractNode> bnMap, String type, BContractBO bBo, WContractBO wBo) {
	public BContractManager(BContractNode thisNode, String type, BContractBO bBo, WContractBO wBo) {
		this.thisNode = thisNode;
		// this.bnMap = bnMap;
		this.type = type;
		this.bBo = bBo;
		this.wBo = wBo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private String type;

	private WContractBO wBo;

	public WContractBO getwBo() {
		return wBo;
	}

	public void setwBo(WContractBO wBo) {
		this.wBo = wBo;
	}

	public BContractBO getbBo() {
		return bBo;
	}

	public void setbBo(BContractBO bBo) {
		this.bBo = bBo;
	}

	private BContractBO bBo;

	private Map<Long, BContractNode> bnMap;

	public Map<Long, BContractNode> getBnMap() {
		return bnMap;
	}

	private BContractNode thisNode;

	public BContractNode getThisNode() {
		return thisNode;
	}

	public void setThisNode(BContractNode thisNode) {
		this.thisNode = thisNode;
	}

	public void setBnMap(Map<Long, BContractNode> bnMap) {
		this.bnMap = bnMap;
	}

	private Map<String, List<?>> providesMap = new HashMap<String, List<?>>();

	// private BContract bContract;// 此对象是帽子节点CS

	// private Map<String, List<BConditionAggregate>> conditionMapLife = new HashMap<String, List<BConditionAggregate>>();// 存放累计责任

	// private Map<String, List<BConditionSurplus>> conditionSurplusMapLife = new HashMap<String, List<BConditionSurplus>>();// 存放增额责任

	private Map<String, String> globalDataMap = new HashMap<String, String>();// 存放全局信息

	// private DCS dcs = null;

	/**
	 * provides中的key是name,value是nodeName，从bContract中查找nodeName节点，并将此放入对应的Map中。
	 * 
	 * @param provides
	 * @throws CTMException
	 */
	public void init(List<DataProvide> provides) throws CTMException {
		for (DataProvide dataProvide : provides) {
			// if (PharosNodeType.PHAROS_ELEMENT_TYPE_BCONTRACT.equalsIgnoreCase(dataProvide.getProvideType())) {
			// providesMap.put(dataProvide.getName(), getBContract(dataProvide));
			// } else
			if (PharosNodeType.PHAROS_ELEMENT_TYPE_DECLARATIONAL.equalsIgnoreCase(dataProvide.getProvideType())) {
				providesMap.put(dataProvide.getName(), getDeclarationAl(dataProvide));
			}
			// else if (PharosNodeType.PHAROS_ELEMENT_TYPE_WCONTRACT.equalsIgnoreCase(dataProvide.getProvideType())) {
			// providesMap.put(dataProvide.getName(), getWContract(dataProvide));
			// }
			else if (PharosNodeType.PHAROS_ELEMENT_TYPE_CSNODE.equalsIgnoreCase(dataProvide.getProvideType())) {
				BContractNode bn = null;
				if (dataProvide.getNodeName() == null || dataProvide.getNodeName().equals("")) {
					List<BContractNode> thisNodeList = new ArrayList<BContractNode>();
					thisNodeList.add(thisNode);
					providesMap.put(dataProvide.getName(), thisNodeList);
				} else {
					List<BContractNode> bnl = new ArrayList<BContractNode>();
					Long id = getParentId((BContractNode) providesMap.get(dataProvide.getNodeName()).get(0));
					// BContractNode bn = bnMap.get(id);
					if (type.equals("1")) {
						bn = bBo.getSingleNode(id);
					} else if (type.equals("2")) {
						bn = wBo.getSingleNode(id, false);
					} else if (type.equals("3")) {
						bn = wBo.getSingleNode(id, false);
					} else if (type.equals("4")) {
						bn = wBo.getSingleNode(id, true);
					}
					bnl.add(bn);
					providesMap.put(dataProvide.getName(), bnl);
				}
				// if (dataProvide.getNodeName() == null || dataProvide.getNodeName().equals("")) {
				// List<BContractNode> thisNodeList = new ArrayList<BContractNode>();
				// thisNodeList.add(thisNode);
				// providesMap.put(dataProvide.getName(), thisNodeList);
				// } else {
				// List<BContractNode> bnl = new ArrayList<BContractNode>();
				// Long id = getParentId((BContractNode) providesMap.get(dataProvide.getNodeName()).get(0));
				// BContractNode bn = bnMap.get(id);
				// if (bn == null) {
				// bn = wBO.getContractNode(id, false);
				// }
				// bnl.add(bn);
				// providesMap.put(dataProvide.getName(), bnl);
				// }
			//} else if (PharosNodeType.PHAROS_ELEMENT_TYPE_DECLARATIONFORM.equalsIgnoreCase(dataProvide.getProvideType())) { zhangfan modify 20140510
			} else if (dataProvide.getProvideType().startsWith(PharosNodeType.PHAROS_ELEMENT_TYPE_DECLARATIONFORM)
					&& !PharosNodeType.PHAROS_ELEMENT_TYPE_DECLARATIONFORMLIST.equalsIgnoreCase(dataProvide.getProvideType()) ) {
				providesMap.put(dataProvide.getName(), getBDeclarationForm(dataProvide));
			} else if (PharosNodeType.PHAROS_ELEMENT_TYPE_ROLE.equalsIgnoreCase(dataProvide.getProvideType())) {// 角色,会有多层取值
				// ftimes = System.currentTimeMillis();
				providesMap.put(dataProvide.getName(), this.getBRoleList(dataProvide));
				// log.info("BContractManager的getRoles共耗时:"+(System.currentTimeMillis()-ftimes));
			} else if (PharosNodeType.PHAROS_ELEMENT_TYPE_NONCONDITION.equalsIgnoreCase(dataProvide.getProvideType())) {// 非寿险责任,会有多层取值
				providesMap.put(dataProvide.getName(), getBConditionNonLife(dataProvide));
			}
			// else if (PharosNodeType.PHAROS_ELEMENT_TYPE_CONDITIONAGGREAGTE.equalsIgnoreCase(dataProvide.getProvideType())) {// 累计责任,会有多层取值
			// // ftimes = System.currentTimeMillis();
			// providesMap.put(dataProvide.getName(), this.getBConditionAggregate(dataProvide));
			// // log.info("BContractManager的getBConditionAggregate共耗时:"+(System.currentTimeMillis()-ftimes));
			// } else if (PharosNodeType.PHAROS_ELEMENT_TYPE_CONDITIONSURPLUS.equalsIgnoreCase(dataProvide.getProvideType())) {// 增额责任,会有多层取值
			// // ftimes = System.currentTimeMillis();
			// providesMap.put(dataProvide.getName(), this.getBConditionSurplus(dataProvide));
			// // log.info("BContractManager的getBConditionSurplus共耗时:"+(System.currentTimeMillis()-ftimes));
			// }
			else if (PharosNodeType.PHAROS_ELEMENT_TYPE_PAYMENTSCHEDULE.equalsIgnoreCase(dataProvide.getProvideType())) {
				providesMap.put(dataProvide.getName(), getPaymentSchedule(dataProvide));
			} else if (PharosNodeType.PHAROS_ELEMENT_TYPE_DECLARATIONFORMLIST.equalsIgnoreCase(dataProvide.getProvideType())) {
				providesMap.put(dataProvide.getName(), getDeclarationFormList(dataProvide));
			} else if (PharosNodeType.PHAROS_ELEMENT_TYPE_DECLARATION.equalsIgnoreCase(dataProvide.getProvideType())) {
				providesMap.put(dataProvide.getName(), getDeclaration(dataProvide));
			} else if (PharosNodeType.PHAROS_ELEMENT_TYPE_INSTALLMENT.equalsIgnoreCase(dataProvide.getProvideType())) {
				providesMap.put(dataProvide.getName(), getInstallment(dataProvide));
			} else if (PharosNodeType.PHAROS_ELEMENT_TYPE_UWHISTORY.equalsIgnoreCase(dataProvide.getProvideType())) {	//20140310 zhangfan
				providesMap.put(dataProvide.getName(), getUWHistory(dataProvide));
			} else if (PharosNodeType.PHAROS_ELEMENT_TYPE_COINSURANCES.equalsIgnoreCase(dataProvide.getProvideType())) {	//20140310 zhangfan
				providesMap.put(dataProvide.getName(), getCoinsurances(dataProvide));
			} else if (PharosNodeType.PHAROS_ELEMENT_TYPE_JOINSURANCES.equalsIgnoreCase(dataProvide.getProvideType())) {	//20140310 zfzhangfan
				providesMap.put(dataProvide.getName(), getJoinsurances(dataProvide));
			} else if (PharosNodeType.PHAROS_ELEMENT_TYPE_SALESFEEDETAIL.equalsIgnoreCase(dataProvide.getProvideType())) {	//20140310 zhangfan
				providesMap.put(dataProvide.getName(), getSalesFeeDeatil(dataProvide));
			} else if (PharosNodeType.PHAROS_ELEMENT_TYPE_MULTICHANNELINFO.equalsIgnoreCase(dataProvide.getProvideType())) {	//20140310 zhangfan
				providesMap.put(dataProvide.getName(), getMultiChannelInfo(dataProvide));
			} else if (PharosNodeType.PHAROS_ELEMENT_TYPE_GLOBAL.equalsIgnoreCase(dataProvide.getProvideType())) {// 全局参数

			}

		}
	}
	
	/* 
	 * @author 张凡 for商险固化的List At 20140310 
	 */
	private List<UnderwriteHistory> getUWHistory(DataProvide dataProvide) {
		List<?> ol = providesMap.get(dataProvide.getNodeName());
		BContractNode bn = (BContractNode) ol.get(0);
		List<UnderwriteHistory> uh = bn.getUnderwriteHistory();
//		if( 0 == uh.size() ) {
//			return null;
//		}
		return uh;
	}
	
	private List<Coinsurance> getCoinsurances(DataProvide dataProvide) {
		List<Coinsurance> retValues = new ArrayList<Coinsurance>();
		List<?> ol = providesMap.get(dataProvide.getNodeName());
		BContractNode bn = (BContractNode) ol.get(0);
		List<Coinsurance> ci = bn.getCoinsurances();
		for (Coinsurance coinsurance : ci) {
			if(coinsurance.getHuatai() != 1) {
				retValues.add(coinsurance);
			}
		}
//		if( 0 == ci.size()) {
//			return null;
//		}
		return retValues;
	}
	
	private List<Joinsurance> getJoinsurances(DataProvide dataProvide) {
		List<Joinsurance> retValues = new ArrayList<Joinsurance>();
		List<?> ol = providesMap.get(dataProvide.getNodeName());
		BContractNode bn = (BContractNode) ol.get(0);
		List<Joinsurance> ji = bn.getJoinsurances();
		for (Joinsurance joinsurance : ji) {
			if(joinsurance.getIsInputer() != 1) {
				retValues.add(joinsurance);
			}
		}
//		if( 0 == ji.size() ) {
//			return null;
//		}
		return retValues;
	}
	
	private List<SalesFeeDetail> getSalesFeeDeatil(DataProvide dataProvide) {
		List<?> ol = providesMap.get(dataProvide.getNodeName());
		BContractNode bn = (BContractNode) ol.get(0);
		List<SalesFeeDetail> sfd = bn.getSalesFeeDetails();
//		if( 0 == sfd.size() ) {
//			return null;
//		}
		return sfd;
	}
	
	private List<MultiChannelInfo> getMultiChannelInfo(DataProvide dataProvide) {
		List<?> ol = providesMap.get(dataProvide.getNodeName());
		BContractNode bn = (BContractNode) ol.get(0);
		List<MultiChannelInfo> mci = bn.getMultiChannelInfos();
//		if( 0 == mci.size() ) {
//			return null;
//		}
		return mci;
	}

	private List<Installment> getInstallment(DataProvide dataProvide) {
		List<?> ol = providesMap.get(dataProvide.getNodeName());
		BContractNode bn = (BContractNode) ol.get(0);
		Installment ii = bn.getInstallment();
		List<Installment> l = new ArrayList<Installment>();
		l.add(ii);
		return l;
	}

	private List<OD> getDeclarationAl(DataProvide dataProvide) {
		List<OD> bdl = new ArrayList<OD>();
		List<?> bnl = providesMap.get(dataProvide.getNodeName());
		for (Object object : bnl) {
			try {
				List<BDeclaration> objL = (List<BDeclaration>) ReflectUtil.getObjByReflect(object, "getDeclarations");
				for (BDeclaration bDeclaration : objL) {
					OD od = new OD();
					od.setObj(object);
					od.setBd(bDeclaration);
					bdl.add(od);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return bdl;
	}

	// private List<BContract> getBContract(DataProvide dataProvide) {
	// List<BContract> bContract = new ArrayList<BContract>();
	// bContract.add(this.bContract);
	// return bContract;
	// }

	public List<?> getDeclaration(DataProvide dataProvide) {
		return providesMap.get(dataProvide.getNodeName());
	}

	public List<BDeclarationFormList> getDeclarationFormList(DataProvide dataProvide) {
		List<BDeclarationFormList> dl = new ArrayList<BDeclarationFormList>();
		String[] temp = dataProvide.getNodeName().split("/");
		List<?> ol = providesMap.get(temp[0]);
		for (Object object : ol) {
			IElement ie = (IElement) object;
			BDeclarationFormList bdfl = (BDeclarationFormList) ie.getElementByName(ElementEnum.BDECLARATIONFORMLIST_CODE, temp[1], false);
			if (bdfl != null) {
				List<BDeclarationFormList> tdl = bdfl.getDecFormValueList();
				for (BDeclarationFormList row : tdl) {
					row.setDDeclarationFormList(bdfl.getDDeclarationFormList());
					row.setParent(bdfl.getParent());
				}
				dl.addAll(tdl);
			}
		}
		return dl;
	}

	/**
	 * 取角色BRole对象集合
	 * 
	 * @param dataProvide
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private List<BRole> getBRoleList(DataProvide dataProvide) {
		List<BRole> resultList = new ArrayList();
		String arr[] = dataProvide.getNodeName().split("/");// 以"/"分割成数组
		List<BContractNode> bnl = (List<BContractNode>) providesMap.get(arr[0]);
		for (BContractNode bContractNode : bnl) {
			List<BRole> brl = (List<BRole>) bContractNode.getRoles();
			for (BRole bRole : brl) {
				if (bRole.getCode().equals(arr[1]) && bRole.getParty() != null) {
					resultList.add(bRole);
				}
			}
		}
		return resultList;
	}

	/**
	 * 取非寿险责任BConditionNonLife对象
	 * 
	 * @param dataProvide
	 * @return
	 */
	private List<?> getBConditionNonLife(DataProvide dataProvide) {
		List<BConditionNonLife> conditions = new ArrayList<BConditionNonLife>();
		List<?> ol = providesMap.get(dataProvide.getNodeName());
		if(ol != null) {
			BContractNode bContractNode = (BContractNode) ol.get(0);// CSNODE节点
			List<BaseCondition<?>> list = (List<BaseCondition<?>>) bContractNode.getConditions();// 从这个节点下获得所有的责任
			for (BaseCondition<?> t : list) {// 循环这个责任列表
				//判断责任是否有效
				if(!t.isValid())
					continue;
				if (t.getElementType().equals(ElementEnum.BCONDITIONNONLIFE_CODE) || t.getElementType().equals(ElementEnum.WCONDITIONNONLIFE_CODE)) {// 如果责任类型是非寿险责任类型，就将这个责任强转成非寿险责任类型，并添加到新创建的List中
					conditions.add((BConditionNonLife) t);
				}
	
			}
		}
		return conditions;
	}

	/**
	 * 获取累计责任BConditionAggregate对象,并缓存
	 * 
	 * @param name
	 * @return BConditionAggregate
	 */
	// public List<BConditionAggregate> getConditionAggregate(DataProvide dataProvide) {
	// List<BConditionAggregate> conditions = null;
	// if (conditionMapLife.containsKey(dataProvide.getName())) {
	// conditions = conditionMapLife.get(dataProvide.getName());
	// } else {
	// conditions = this.getBConditionAggregate(dataProvide);
	// // 缓存
	// conditionMapLife.put(dataProvide.getName(), conditions);
	// }
	// return conditions;
	// }

	/**
	 * 取累计责任BConditionAggregate对象
	 * 
	 * @param dataProvide
	 * @return
	 */
	// @SuppressWarnings("rawtypes")
	// private List getBConditionAggregate(DataProvide dataProvide) {
	// try {
	// List allList = new ArrayList();// 存放List<BConditionAggregate>类型的List
	// List<BConditionAggregate> conditions = null;// 存放累计责任对象
	// BContractNode bContractNode = null;// CSNODE节点
	//
	// List all = bContract.getAllContractNodes();// 获取Contract Tree
	// // 范围内所有的ContractNodes
	// // 包括所有下级和所有的下级的下级...
	// for (int i = 0; i < all.size(); i++) {// 循环每一个CSNODE节点
	// bContractNode = (BContractNode) all.get(i);
	// String name = bContractNode.getName();// 获得节点的名称
	// if (dataProvide.getNodeName().equals(name)) {// 如果模板中配置的dataprovider标签中的nodeName属性的值跟这个节点的名称相同的话
	// if (bContractNode != null) {
	// conditions = new ArrayList();
	// List<BaseCondition> list = (List<BaseCondition>) bContractNode.getConditions();// 从这个节点下获得所有的责任
	// for (BaseCondition t : list) {// 循环这个责任列表
	// if (t.getElementType() == ElementEnum.BCONDITIONAGGREGATE_CODE) {// 如果责任类型是累计责任类型，就将这个责任强转成累计责任类型，并添加到新创建的List中
	// conditions.add((BConditionAggregate) t);
	// }
	// }
	// }
	// allList.add(conditions);
	// }
	//
	// }
	// return allList;
	// } catch (Exception ex) {
	//
	// ex.printStackTrace();
	// return null;
	// }
	// }

	/**
	 * 获取增额责任BConditionSurplus对象,并缓存
	 * 
	 * @param name
	 * @return BConditionSurplus
	 */
	// public List<BConditionSurplus> getConditionSurplus(DataProvide dataProvide) {
	// List<BConditionSurplus> conditions = null;
	// if (conditionSurplusMapLife.containsKey(dataProvide.getName())) {
	// conditions = conditionSurplusMapLife.get(dataProvide.getName());
	// } else {
	// conditions = this.getBConditionSurplus(dataProvide);
	// // 缓存
	// conditionSurplusMapLife.put(dataProvide.getName(), conditions);
	// }
	// return conditions;
	// }

	/**
	 * 取增额责任BConditionSurplus对象
	 * 
	 * @param dataProvide
	 * @return
	 */
	// @SuppressWarnings("rawtypes")
	// private List getBConditionSurplus(DataProvide dataProvide) {
	// try {
	// List allList = new ArrayList();// 存放List<BConditionSurplus>类型的List
	// List<BConditionSurplus> conditions = null;// 存放增额责任对象
	// BContractNode bContractNode = null;// CSNODE节点
	//
	// List all = bContract.getAllContractNodes();// 获取Contract Tree
	// // 范围内所有的ContractNodes
	// // 包括所有下级和所有的下级的下级...
	// for (int i = 0; i < all.size(); i++) {// 循环每一个CSNODE节点
	// bContractNode = (BContractNode) all.get(i);
	// String name = bContractNode.getName();// 获得节点的名称
	// if (dataProvide.getNodeName().equals(name)) {// 如果模板中配置的dataprovider标签中的nodeName属性的值跟这个节点的名称相同的话
	// if (bContractNode != null) {
	// conditions = new ArrayList();
	// List<BaseCondition> list = (List<BaseCondition>) bContractNode.getConditions();// 从这个节点下获得所有的责任
	// for (BaseCondition t : list) {// 循环这个责任列表
	// if (t.getElementType() == ElementEnum.BCONDITIONSURPLUS_CODE) {// 如果责任类型是增额责任类型，就将这个责任强转成增额责任类型，并添加到新创建的List中
	// conditions.add((BConditionSurplus) t);
	// }
	// }
	// }
	// allList.add(conditions);
	// }
	//
	// }
	// return allList;
	// } catch (Exception ex) {
	//
	// ex.printStackTrace();
	// return null;
	// }
	// }

	/**
	 * 取缴费计划PaymentSchedule对象
	 * 
	 * @param dataProvide
	 * @return
	 */
	private List<?> getPaymentSchedule(DataProvide dataProvide) {
		List<?> ol = providesMap.get(dataProvide.getNodeName());
		BContractNode bn = (BContractNode) ol.get(0);
		Installment ii = bn.getInstallment();
		List<InstallmentDetail> l = ii.getDetail();
		return l;

	}

	// public BContract getBContract(String name) {
	//
	// return this.bContract;
	// }

	/**
	 * 获取BContractNode对象,并缓存
	 * 
	 * @param name
	 * @return BContractNode
	 */
	// public List<BContractNode> getBContractNode(DataProvide dataProvide) {
	// try {
	//
	// List<BContractNode> bContractNodes = null;
	//
	// if (dataProvide.getNodeName().indexOf("/") != -1) {// dataprovider标签中的nodeName属性的值有"/"
	// String arrDf[] = dataProvide.getNodeName().split("/");
	//
	// bContractNodes = this.getBContractNodeByNodeNameAndPath(arrDf);
	// } else {
	// bContractNodes = this.getBContractNodeByNodeName(dataProvide);
	// }
	//
	// return bContractNodes;
	// } catch (Exception ex) {
	//
	// ex.printStackTrace();
	// return null;
	// }
	//
	// }

	/**
	 * 
	 * @param nodeId
	 * @return
	 */
	private long getParentId(BContractNode node) {
		try {
			if (node instanceof WContractNode) {
				int[] wFields = { WContractNodeBaseInfo.ID, WContractNodeBaseInfo.PARENT_ID };
				WContractNodeBaseInfo wInfo = wBo.queryWContractNodeBaseInfo(node.getId(), wFields);

				return wInfo.getParentId();
			} else if (node instanceof BContractNode) {
				int[] bFields = { BContractNodeBaseInfo.ID, BContractNodeBaseInfo.PARENT_ID };
				BContractNodeBaseInfo bInfo = bBo.queryBContractNodeBaseInfo(node.getId(), bFields);

				return bInfo.getParentId();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0L;
	}

	/**
	 * 取BContractNode对象
	 * 
	 * @param dataProvide
	 * @return
	 */
	// private List<BContractNode> getBContractNodeByNodeName(DataProvide dataProvide) {
	// List<BContractNode> bContractNodeList = new ArrayList<BContractNode>();
	// if (bContract.getDcs() == null) {
	// System.out.println("空空空");
	// }
	// BContractNode bContractNode = (BContractNode) bContract.getRootNode().getElementByName(ElementEnum.BCONTRACTNODE_CODE, dataProvide.getNodeName(), true);// nodeName没有"/"的情况下，直接通过节点的名称找到这个节点对象
	// bContractNodeList.add(bContractNode);
	// return bContractNodeList;
	// }

	/**
	 * 根据路径取BContractNode对象
	 * 
	 * @param dataProvide
	 * @return
	 */
	// private List<BContractNode> getBContractNodeByNodeNameAndPath(String[] dataProvides) {
	// List<BContractNode> bContractNodeList = new ArrayList<BContractNode>();
	// BContractNode bContractNode = null;
	// for (int i = 0; i < dataProvides.length; i++) {
	// if (i == 0) {// 取第一个"/"左边的值
	// bContractNode = bContract.getRootNode();
	// }
	// bContractNode = (BContractNode) bContractNode.getElementByName(ElementEnum.BCONTRACTNODE_CODE, dataProvides[i], true);
	// }
	// bContractNodeList.add(bContractNode);
	//
	// return bContractNodeList;
	// }

	/**
	 * 取BDeclarationForm对象
	 * 
	 * @param dataProvide
	 * @return
	 */
	private List<BDeclarationForm> getBDeclarationForm(DataProvide dataProvide) {
		List<BDeclarationForm> dbfl = new ArrayList<BDeclarationForm>();
		String[] temp = dataProvide.getNodeName().split("/");
		List<?> ol = providesMap.get(temp[0]);
		for (Object object : ol) {
			IElement ie = (IElement) object;
			dbfl.add((BDeclarationForm) ie.getElementByName(ElementEnum.BDECLARATIONFORM_CODE, temp[1], true));
		}
		return dbfl;
	}

	/**
	 * 获取全局参数缓存
	 * 
	 * @param key
	 * @return
	 */

	// public void setBContract(BContract contract) {
	// this.bContract = contract;
	// }

	public void setGlobalDataMap(Map<String, String> map) {
		this.globalDataMap = map;
		List<Map<String, String>> mapl = new ArrayList<Map<String, String>>();
		mapl.add(globalDataMap);
		providesMap.put("GLOBAL", mapl);
	}

	public List<?> getProvide(String provide) {
		return providesMap.get(provide);
	}

	// public DCS getDcs() {
	// return dcs;
	// }

	// public void setDcs(DCS dcs) {
	// this.dcs = dcs;
	// if(this.dcs == null){
	// System.out.println("空空空");
	// }
	// this.bContract.setDcs(this.dcs);
	// }

}
