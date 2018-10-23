/**
 * FileName: ProvideTypeMappingManager.java Date: 2009-7-22
 *
 * Copyright (c) 1994-2009 北京尚洋信德信息技术股份有限公司 版权所有
 *
 * History:
 *        <p>2009-7-22:此处记录修改历史记录
 *
 */
package com.sysnet.poc.partymapping;

import java.util.HashMap;
import java.util.Map;

/**
 * 保存产品映射配置文件中provideType与具体的MappingAction之间的关联关系。 即：什么provideType使用什么MappingAction实现类来处理
 * 
 * @author luhaibin
 * @version 1.0
 */
public class ParameterTypeMappingManager {
	private static Map<String, String> parameterTypeMap = new HashMap<String, String>();

	static {
		// parameterTypeMap.put(PartyType.PHAROS_ELEMENT_TYPE_PartyVo,
		// "com.sysnet.poc.partymapping.action.PartyVOMappingAction");
		// parameterTypeMap.put(PartyType.PHAROS_ELEMENT_TYPE_PartiesRelationshipVO,
		// "com.sysnet.poc.partymapping.action.PartiesRelationshipVOMappingAction");
		// parameterTypeMap.put(PartyType.PHAROS_ELEMENT_TYPE_CorpRelationshipVO,
		// "com.sysnet.poc.partymapping.action.CorpRelationshipVOMappingAction");
		// parameterTypeMap.put(PartyType.PHAROS_ELEMENT_TYPE_PartyCertIDVO,
		// "com.sysnet.poc.partymapping.action.PartyCertIDVOMappingAction");
		// parameterTypeMap.put(PartyType.PHAROS_ELEMENT_TYPE_PartyNameVO,
		// "com.sysnet.poc.partymapping.action.PartyNameVOMappingAction");
		parameterTypeMap.put(PartyType.PHAROS_ELEMENT_TYPE_GLOBAL, "com.sysnet.poc.partymapping.action.GlobalMappingAction");
		// parameterTypeMap.put(PartyType.PHAROS_ELEMENT_TYPE_PartyDeclarationForm,
		// "com.sysnet.poc.partymapping.action.PartyDeclarationFormMappingAction");
		// parameterTypeMap.put(PartyType.PHAROS_ELEMENT_TYPE_BDeclarationForm,
		// "com.sysnet.poc.partymapping.action.BDeclarationFormMappingAction");
		parameterTypeMap.put(PartyType.PHAROS_ELEMENT_TYPE_PartyVo, "com.sysnet.poc.partymapping.action.ObjectAction");
		parameterTypeMap.put(PartyType.PHAROS_ELEMENT_TYPE_PartiesRelationshipVO, "com.sysnet.poc.partymapping.action.ObjectAction");
		parameterTypeMap.put(PartyType.PHAROS_ELEMENT_TYPE_CorpRelationshipVO, "com.sysnet.poc.partymapping.action.ObjectAction");
		parameterTypeMap.put("OBJECT", "com.sysnet.poc.partymapping.action.ObjectAction");
		parameterTypeMap.put("DFL", "com.sysnet.poc.partymapping.action.DFLAction");
	}

	/**
	 * 返回provideType对应的MappingAction处理类的包名
	 * 
	 * @param provateType
	 * @return
	 */
	public static String getClass(String parameterType) {
		return parameterTypeMap.get(parameterType);
	}
}
