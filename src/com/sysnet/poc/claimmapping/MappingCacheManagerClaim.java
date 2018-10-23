package com.sysnet.poc.claimmapping;

import java.util.HashMap;
import java.util.Map;

import com.sysnet.poc.claimmapping.vo.MappingDescription;

/**
 * 此对象乃是一个单例。用于保存所有在外部定义的理赔映射文件。
 * 
 * @author cwduan
 * 
 */
public class MappingCacheManagerClaim {
	/**
	 * 所有的当事方映射文件均保存此Map中，key值为xml文件中的一部分名称，由MappingCacheManager .getMapping("party")方法来传入。
	 */
	private static Map<String, MappingDescription> mappings = new HashMap<String, MappingDescription>();// MappingDescription用来保存当事方xml映射文件的对象

	private static MappingHelper mappingHelper = new MappingHelper();

	/**
	 * 首先通过key，查找Map中是否已经有映射文件存在，如果没有，则调用mappingHelper对象加载映射文件， 先放入Map中，再返回此映射文件对象。
	 * 
	 * @param productcode
	 * @return MappingDescription
	 * @throws CloneNotSupportedException
	 */
	public static MappingDescription getMapping(String subject, String tt) throws Exception {
		MappingDescription mappingDescription = null;
		if (mappings.containsKey(subject)) {// 如果Map中有这个对象，那么就直接取出来
			mappingDescription = mappings.get(subject);
			if (mappingDescription == null) {
				mappingDescription = init(subject, tt);
			}
		} else {// 如果Map中没有这个对象，那么就先将这个xml文件转换成MappingDescription对象，然后再放入到Map中
			mappingDescription = init(subject, tt);
		}

		return (MappingDescription) mappingDescription.clone();
	}

	private static synchronized MappingDescription init(String key, String tt) throws Exception {
		MappingDescription mappingDescription = null;
		if (mappings.containsKey(key)) {// 如果Map中有这个对象，那么就直接取出来
			mappingDescription = mappings.get(key);
		} else {
			String fileName = "/mappingforclaim/p" + key + "/claim_model_claim_" + tt + ".xml";
			mappingDescription = mappingHelper.XmlToObject(fileName);
			// 缓存
			mappings.put(key, mappingDescription);
		}
		return mappingDescription;
	}
}
