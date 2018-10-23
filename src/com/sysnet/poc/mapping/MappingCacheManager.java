package com.sysnet.poc.mapping;

import java.util.HashMap;
import java.util.Map;
import com.sysnet.poc.mapping.vo.MappingDescription;

/**
 * 此对象乃是一个单例。用于保存所有在外部定义的产品映射文件。
 */
public class MappingCacheManager {
	/**
	 * 所有的产品映射文件均保存此Map中，key值为ProductNodeCode的名称。
	 */
	private static Map<String, MappingDescription> mappings = new HashMap<String, MappingDescription>();

	private static MappingHelper mappingHelper = new MappingHelper();

	/**
	 * 首先通过productNodeCode，查找Map中是否已经有映射文件存在，如果没有，则调用mappingHelper对象加载映射文件， 先放入Map中，再返回此映射文件对象。
	 * 
	 * @param productcode
	 * @return MappingDescription
	 * @throws CloneNotSupportedException
	 */
	public static MappingDescription getMapping(String productCd, String productNodeCd, String object_type) throws Exception {
		MappingDescription mappingDescription = null;
		String key = productCd.trim() + "_" + productNodeCd.trim() + "_" + object_type;// 用产品号
		// +
		// 产品节点号
		// +
		// 对象类型来作为key
		if (mappings.containsKey(key)) {
			mappingDescription = mappings.get(key);
			if (mappingDescription == null) {
				mappingDescription = init(productCd, key);
			}
		} else {
			mappingDescription = init(productCd, key);
		}

		return (MappingDescription) mappingDescription.clone();
	}

	private static synchronized MappingDescription init(String productCd, String key) throws Exception {
		MappingDescription mappingDescription = null;
		if (mappings.containsKey(key)) {
			mappingDescription = mappings.get(key);
		} else {
			String fileName = "/composite/P" + productCd.trim() + "/composite_product_model_" + key + ".xml";// 根据产品号来找到对应产品的文件夹，再根据key找到对应的配置模板
			System.out.println("Processing template:" + fileName); //处理模板
			// mappingDescription = mappingHelper.XmlToObject(fileName);
			mappingDescription = mappingHelper.XmlToObjectByIncludePattern(fileName);
			// 缓存
			mappings.put(key, mappingDescription);
		}
		return mappingDescription;
	}
}
