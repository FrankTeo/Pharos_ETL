package com.sysnet.poc.claimmapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sysnet.poc.claimmapping.action.MappingAction;
import com.sysnet.poc.claimmapping.vo.ActionMapCatch;
import com.sysnet.poc.claimmapping.vo.ClaimInfo;
import com.sysnet.poc.claimmapping.vo.Item;
import com.sysnet.poc.claimmapping.vo.Table;
import com.sysnet.poc.control.manage.vo.EtlPharosIncrClaim;
import com.sysnet.poc.service.storage.SColumn;

/**
 * 读取模板
 * 
 * @author duanchengwei
 */
public class ClaimMappingBuilder {

	// 结果集合
	private List<SColumn> list = new ArrayList<SColumn>();
	private DataProviderMapping dm = new DataProviderMapping();

	/**
	 * 构造结果集合
	 */
	public ClaimInfo Collection(EtlPharosIncrClaim ic) throws Exception {
		//Long a = System.currentTimeMillis();
		ClaimInfo ci = dm.initDataProvider(ic);
		//Long b = System.currentTimeMillis();
		//System.out.println("init DataProvider:" + (b - a));
		ActionMapCatch amc = new ActionMapCatch();
		Map<String, MappingAction> actionMap = amc.initAction(ci.getCode());
		List<Table> tl = ci.getMd().getTable();
		// 按table来去数据提供者集合
		//a = System.currentTimeMillis();
		//System.out.println("init Action:" + (a - b));

		for (Table table : tl) {
			String dataProvide = table.getDataProvide();
			List<?> data = dm.getDataProvider(dataProvide);
			// 在数据提供者集合中，每个数据提供者构造一条记录并存入结果集合
			if (data != null) {
				for (Object object : data) {
					// System.out.println(table.getTableName()+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					Table t = table.clone();
					// 封装数据
					buildTable(object, t, actionMap);
					list.add(t);
				}
			}
		}
		//b = System.currentTimeMillis();
		//System.out.println("End:" + (b - a));
		ci.setSl(list);
		return ci;
	}

	/**
	 * 封装数据
	 * 
	 * @param object
	 * @param t
	 * @param id
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	private void buildTable(Object object, Table t, Map<String, MappingAction> actionMap) throws InstantiationException, IllegalAccessException, ClassNotFoundException, Exception {
		List<Item> it = t.getItems();
		for (Item item : it) {
			MappingAction mappingAction = actionMap.get(item.getParameterType());
			mappingAction.setMappingItem(item);
			String provideName = item.getDataProvider();
			if (provideName != null && !provideName.equals("")) {// 如果字段Item有单独的数据提供者(即不使用当前Table自身的数据提供者)
				// find DataProvide
				Object provide = null;
//				System.out.println(provideName);
				provide = dm.getOnlyDataProvider(provideName);
				// if (provide != null) {
				mappingAction.setProvideInstance(provide);// 存放PartyVOManager中得到的数据提供者
				// }
			} else {
				// 真正的数据对象，即此时Item没有单独的数据提供者，直接使用Table的数据提供者(即<table>标签的dataprovider)
				mappingAction.setProvideInstance(object);// 设置从PartyVOManager中得到的数据提供者到mappingAction中对应的属性上
			}
			// 给Item赋值
			mappingAction.execute();
		}
	}

}
