/**
 * FileName: ProvideTypeMappingManager.java Date: 2009-7-22
 *
 * Copyright (c) 1994-2009 北京尚洋信德信息技术股份有限公司 版权所有
 *
 * History:
 *        <p>2009-7-22:此处记录修改历史记录
 *
 */
package com.sysnet.poc.mapping;

import java.util.HashMap;
import java.util.Map;

import com.sysnet.poc.mapping.action.ActionPlus;
import com.sysnet.poc.mapping.action.GlobalMappingAction;
import com.sysnet.poc.mapping.action.MappingAction;
import com.sysnet.poc.mapping.action.ObjectAction;

/**
 * 保存产品映射配置文件中provideType与具体的MappingAction之间的关联关系。 即：什么provideType使用什么MappingAction实现类来处理
 * 
 * @author <a href=huang_y@sysnet.com.cn>huang_y</a>
 * @version 1.0
 */
public class ParameterTypeMappingManager {
	// private static Map<String, String> parameterTypeMap = new HashMap<String,
	// String>();
	//
	// static {
	//
	// // parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_CS,
	// "com.sysnet.poc.mapping.action.CSMappingAction");
	// // parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_CSNODE,
	// "com.sysnet.poc.mapping.action.CSNodeMappingAction");
	// // parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_DECLARATION,
	// "com.sysnet.poc.mapping.action.DeclarationMappingAction");
	// //
	// parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_DECLARATIONFORM,
	// "com.sysnet.poc.mapping.action.DeclarationFormMappingAction");
	// //
	// parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_DECLARATIONFORMLIST,
	// "com.sysnet.poc.mapping.action.DeclarationFormListMappingAction");
	// // parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_ROLE,
	// "com.sysnet.poc.mapping.action.RoleMappingAction");
	// // parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_NONCONDITION,
	// "com.sysnet.poc.mapping.action.ConditionMappingAction");
	// //
	// parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_CONDITIONAGGREAGTE,
	// "com.sysnet.poc.mapping.action.ConditionLifeMappingAction");
	// //
	// parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_CONDITIONSURPLUS,
	// "com.sysnet.poc.mapping.action.ConditionSurplusMappingAction");
	// //
	// parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_CONDITIONSURPLUS,
	// "com.sysnet.poc.mapping.action.PaymentScheduleMappingAction");
	// // parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_GLOBAL,
	// "com.sysnet.poc.mapping.action.GlobalMappingAction");
	//
	// parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_CS,
	// "com.sysnet.poc.mapping.action.ObjectAction");
	// parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_CSNODE,
	// "com.sysnet.poc.mapping.action.ObjectAction");
	// parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_DECLARATION,
	// "com.sysnet.poc.mapping.action.ObjectAction");
	// parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_DECLARATIONFORM,
	// "com.sysnet.poc.mapping.action.ObjectAction");
	// parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_DECLARATIONFORMLIST,
	// "com.sysnet.poc.mapping.action.ObjectAction");
	// parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_ROLE,
	// "com.sysnet.poc.mapping.action.ObjectAction");
	// parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_NONCONDITION,
	// "com.sysnet.poc.mapping.action.ObjectAction");
	// parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_CONDITIONAGGREAGTE,
	// "com.sysnet.poc.mapping.action.ObjectAction");
	// parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_CONDITIONSURPLUS,
	// "com.sysnet.poc.mapping.action.ObjectAction");
	// parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_BCONTRACT,
	// "com.sysnet.poc.mapping.action.ObjectAction");
	// parameterTypeMap.put("OBJECT",
	// "com.sysnet.poc.mapping.action.ObjectAction");
	// parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_GLOBAL,
	// "com.sysnet.poc.mapping.action.GlobalMappingAction");
	// }
	//
	// /**
	// * 返回provideType对应的MappingAction处理类的包名
	// *
	// * @param provateType
	// * @return
	// */
	// public static String getClass(String parameterType) {
	// return parameterTypeMap.get(parameterType);
	// }
	private Map<String, MappingAction> parameterTypeMap = new HashMap<String, MappingAction>();

	public ParameterTypeMappingManager(String ptmm) {
		ObjectAction oa = new ObjectAction();
		ActionPlus ap = null;
		try {
			ap = (ActionPlus) Class.forName("composite." + "P" + ptmm + ".ActionPlusImp").newInstance();
		} catch (Exception e) {
			ap = new ActionPlus();
		}
		oa.setAp(ap);
		parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_CS, oa);
		parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_CSNODE, oa);
		parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_DECLARATION, oa);
		// parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_DECLARATION,
		// new DeclarationMappingAction());
		parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_DECLARATIONFORM, oa);
		parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_DECLARATIONFORMLIST, oa);
		// parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_DECLARATIONFORMLIST,
		// new DFLAction());
		parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_ROLE, oa);
		parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_NONCONDITION, oa);
		parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_CONDITIONAGGREAGTE, oa);
		parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_CONDITIONSURPLUS, oa);
		parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_BCONTRACT, oa);
		parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_DECLARATIONAL, oa);
		parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_GLOBAL, new GlobalMappingAction());
		parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_PAYMENTSCHEDULE, oa);
		parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_INSTALLMENT, oa);
		
		/*
		 * @author 张凡 for商险固化的List At 20140310 
		 */
		parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_UWHISTORY, oa);
		parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_COINSURANCES, oa);
		parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_JOINSURANCES, oa);
		parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_SALESFEEDETAIL, oa);
		parameterTypeMap.put(PharosNodeType.PHAROS_ELEMENT_TYPE_MULTICHANNELINFO, oa);

	}

	public MappingAction getAction(String key) {
		return parameterTypeMap.get(key);
	}
}
