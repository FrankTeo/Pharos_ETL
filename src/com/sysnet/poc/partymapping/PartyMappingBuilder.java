package com.sysnet.poc.partymapping;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;

import com.sysnet.poc.partymapping.action.MappingAction;
import com.sysnet.poc.partymapping.vo.Item;
import com.sysnet.poc.partymapping.vo.MappingDescription;
import com.sysnet.poc.partymapping.vo.Table;
import com.sysnet.poc.service.storage.SColumn;
import com.sysnet.poc.util.OdsLogger;
import com.sysnet.poc.util.ResourceBundleUtil;

/**
 * 当事方映射构建类
 * 
 * @author lu_haibin
 * 
 */
public class PartyMappingBuilder {
	private List<SColumn> listR = new ArrayList<SColumn>();
	private DataProviderMapping dpm = new DataProviderMapping();

	// public List<SColumn> execute(String object_No, String history_no) throws Exception {
	public List<SColumn> execute(String object_No) throws Exception {
		mapDesc = MappingCacheManager.getMapping("party_" + ResourceBundleUtil.getCompanyId());
		dpm.init(object_No, mapDesc);

		List<SColumn> sl = new ArrayList<SColumn>();
		for (Table table : mapDesc.getTable()) {
			String dataProvide = table.getDataProvide();
			List<?> data = dpm.getDataProvider(dataProvide);
			if (data != null) {
				for (Object object : data) {
					Table t = table.clone();
					listR.add(buildTable(t, object));
				}
			}
			sl.add(table);
		}
		// List<SColumn> rl = RemoveDataUtil.remove(sl, object_No);
		// for (SColumn column : rl) {
		// listR.add(column);
		// }
		return listR;
	}

	private static final Log log = OdsLogger.getLog("partyLog4j", "Party");

	public String getObject_type() {
		return object_type;
	}

	public void setObject_type(String object_type) {
		this.object_type = object_type;
	}

	private MappingDescription mapDesc;

	public MappingDescription getMapDesc() {
		return mapDesc;
	}

	public void setMapDesc(MappingDescription mapDesc) {
		this.mapDesc = mapDesc;
	}

	private String object_type;

	/**
	 * 封装单个Table数据
	 * 
	 * @param t
	 * @param dataProvide
	 * @param obj
	 * @throws Exception
	 */
	private Table buildTable(Table t, Object obj) throws Exception {

		List<Item> items = t.getItems();// 得到当前Table对象的Item对象集合，即当前Table的所有对应字段
		for (Item _item : items) {// 循环遍历这些字段
			String actionName = ParameterTypeMappingManager.getClass(_item.getParameterType());// provideType对应的MappingAction处理类全路径
			if (actionName == null) {
				System.out.println("action name is null");
			}

			MappingAction mappingAction = null;
			try {
				mappingAction = (MappingAction) Class.forName(actionName)// 根据类的全路径动态实例化出来对应的MappingAction
						.newInstance();
			} catch (NullPointerException ex) {
				System.out.println(_item.getFieldName() + ":" + _item.getParent().getTableName());
				throw new NullPointerException("actionName:" + actionName + ":" + ex.getMessage());
			}
			mappingAction.setMappingItem(_item);// 将当前Table中的一个Item放到对应的MappingAction类中

			String provideName = _item.getDataProvider();// 每个Item也可能有数据提供者，所以得到提供者的名称
			if (provideName != null && !provideName.equals("")) {// 如果字段Item有单独的数据提供者(即不使用当前Table自身的数据提供者)
				// find DataProvide
				Object provides = dpm.getOnlyDataProvider(provideName);// 得到这个数据提供者，比如说GLOBAL，只有一条记录
				if (provides == null) {
					log.error("----------Provider:" + provideName + " is null!");
					continue;
				}
				mappingAction.setProvideInstance(provides);// 存放PartyVOManager中得到的数据提供者，因为返回了一条记录，所以这里直接得到第0条记录
			} else {
				// 真正的数据对象，即此时Item没有单独的数据提供者，直接使用Table的数据提供者(即<table>标签的dataprovider)
				mappingAction.setProvideInstance(obj);// 设置从PartyVOManager中得到的数据提供者到mappingAction中对应的属性上
			}

			mappingAction.execute();
		}
		return t;
	}

	public void setMapDocument(MappingDescription mapDesc) {
		this.mapDesc = mapDesc;
	}
}
