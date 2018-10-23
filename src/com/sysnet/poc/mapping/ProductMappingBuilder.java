package com.sysnet.poc.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;

import pharos.ctm.bo.BContractBO;
import pharos.ctm.bo.WContractBO;
import pharos.ctm.exception.CTMException;
import pharos.ctm.vo.BContractNode;

import com.sysnet.poc.mapping.action.BContractManager;
import com.sysnet.poc.mapping.action.MappingAction;
import com.sysnet.poc.mapping.vo.DataProvide;
import com.sysnet.poc.mapping.vo.Item;
import com.sysnet.poc.mapping.vo.MappingDescription;
import com.sysnet.poc.mapping.vo.Table;
import com.sysnet.poc.util.OdsLogger;

/**
 * 承保产品映射构建类
 * 
 * @author li_yanpeng
 * 
 */
public class ProductMappingBuilder implements MappingBuilder {

	public ProductMappingBuilder(String ptmm, BContractNode thisNode, String type, BContractBO bBo, WContractBO wBo) {
		// public ProductMappingBuilder(String ptmm, Map<Long, BContractNode> bnMap, String type, BContractBO bBo, WContractBO wBo) {
		// bContractManager = new BContractManager(bnMap, type, bBo, wBo);// 在构造ProductMappingBuilder对象的时候同时构造BContractManager对象
		bContractManager = new BContractManager(thisNode, type, bBo, wBo);// 在构造ProductMappingBuilder对象的时候同时构造BContractManager对象
		this.ptmm = new ParameterTypeMappingManager(ptmm);
		
		this.contractNode = thisNode;
	}

	// private MappingCacheManager mcm;

	// private BContract bContract;

	// public MappingCacheManager getMcm() {
	// return mcm;
	// }
	//
	// public void setMcm(MappingCacheManager mcm) {
	// this.mcm = mcm;
	// }

	private ParameterTypeMappingManager ptmm;

	private static final Log log = OdsLogger.getLog("insureLog4j", "Insure");

	public String getObject_type() {
		return object_type;
	}

	public void setObject_type(String object_type) {
		this.object_type = object_type;
	}

	private MappingDescription mapDesc;// 封装了当前模板中所有的元素信息

	private BContractNode contractNode;// 提供基础数据的对象

	private Map<String, String> globalDataMap;// 全局的

	private BContractManager bContractManager;// 数据提供者存放到这里面

	private String object_type;// 增量表中的对象类型

	private List<String> list = new ArrayList<String>();// 没有数据提供者的主表的list，一个配置文件中可能有多个主表

	// private DCS dcs = null;

	// public DCS getDcs() {
	// return dcs;
	// }
	//
	// public void setDcs(DCS dcs) {
	// this.dcs = dcs;
	// if(this.dcs == null){
	//
	// System.out.println("空空空");
	// }
	// }

	// private List dList = new ArrayList();此变量没用，多余的

	/**
	 * 初始化提供基础数据的对象以及全局变量
	 */
	public void initBContractAndGlobalDataMap() {
		bContractManager.setThisNode(contractNode);
		// bContractManager.setBContract(this.bContract);// 将PolicyBusiness中设置的bContract对象，再次设置到bContractManager对象中
		// if(this.dcs == null){
		//
		// System.out.println("空空空");
		// }
		// bContractManager.setDcs(this.dcs);
		bContractManager.setGlobalDataMap(this.globalDataMap);// 将PolicyBusiness中设置的globalDataMap对象，再次设置到bContractManager对象中
	}

	private void initProvide() throws CTMException {
		/*
		 * 从mapDesc封装的整个xml配置对象中得到xml中<dataProvider>对应的DataProvide对象的一个Map集合( 即xml文件中有几个dataprovider， 那么这个Map中就有多少个DataProvide对象)
		 */
		// long ftimes = 0;
		// ftimes = System.currentTimeMillis();
		List<DataProvide> provideMap = mapDesc.getDataProvide();
		// log.info("mapDesc.getDataProvide()共耗时:"+(System.currentTimeMillis()-ftimes));

		// ftimes = System.currentTimeMillis();
		bContractManager.init(provideMap);// 初始化这些数据提供者
		// log.info("bContractManager.init共耗时:"+(System.currentTimeMillis()-ftimes));
	}

	/**
	 * 初始化配置，并且克隆多个table对象
	 * 
	 * @throws CloneNotSupportedException
	 */
	private void initMapping() throws CloneNotSupportedException {
		List<Table> _tableList = new ArrayList<Table>();// 创建一个新的List
		List<Table> tableList = mapDesc.getTable();// 从mapDesc中得到Table对象的集合，即xml中有几个<table>标签，就得到几个Table对象
		
		for (Table t : tableList) {// 对这个Table列表进行循环
			t.setObjectId("" + bContractManager.getThisNode().getId());
			t.setObjectNo(bContractManager.getThisNode().getProposalNo());
			/*
			 * 在基础模板中的table标签中，可能会引用多个dataprovider， 所以得到Table对象中对应的String类型的dataprovider的值， 这个数据提供者可能会有多个，所以要判断一下是否有逗号
			 */
			String str = t.getDataProvide();
			if (str == null) {
				str = "";
			}
			/*
			 * 如果有逗号，说明有多个数据提供者，一个<table>标签里面的所有字段只对应一个SQL语句，插入一条记录，所以这里有逗号， 就要克隆多个Table对象
			 */
			if (str.indexOf(",") != -1) {
				String strArray[] = str.split("[,]");
				int len = strArray.length;
				for (int i = 0; i < len; i++) {
					/*
					 * 克隆每个数据提供者对应的这个Table对象，克隆之后的对象就是单个dataprovider的Table对象了， 将这些Table对象一个一个的添加到新创建的_tableList里面 ，再将一开始mapDesc里面封装的Table对象的集合覆盖掉， 这样就是一个dataprovider对应一个Table对象了
					 */
					Table _t = (Table) t.clone();
					_t.setDataProvide(strArray[i]);// 将各个数据提供者分别设置到各自对应的Table里
					_tableList.add(_t);// 将克隆后的对象添加到新创建的List里面
				}
			} else {
				/*
				 * 否则的话，在xml文件中每一个<table>标签中只有一个dataprovider，这样就不用克隆了， 直接将原来的Table对象一个一个的再添加到新创建的_tableList里面， 此时新创建的_tableList里面存放的Table对象仍然还是原来mapDesc里面存放的Table对象的集合
				 */

				_tableList.add(t);
			}
		}
		mapDesc.setTable(_tableList);// 最后将新创建的_tableList再放入到mapDesc里面，此时mapDesc里面存放的就是只有一个dataprovider的Table对象了

	}

	/**
	 * 解析每一条item对象，
	 * 
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws CloneNotSupportedException
	 */
	private void doAction() throws InstantiationException, IllegalAccessException, ClassNotFoundException, CloneNotSupportedException, NullPointerException {

		List<Table> tableList = mapDesc.getTable();// 从对应的模板中取得所有的Table对象
		List<Table> newtableList = new ArrayList<Table>();// 一个Table对象只能拼一条SQL语句，所以如果数据提供者是返回多条记录的话，就要在刚才initMapping方法中的mapDesc基础之上再克隆多个Table对象了
		mapDesc.setTable(newtableList);// 将最新的Table的集合放入，实际上将各个Table对象放入newtableList中
		for (Table t : tableList) {
			
			// System.out.println(t.getDataProvide());
			// 如果表的数据提供者只有一个
			if( -1 == t.getDataProvide().indexOf("|") ) {
				List<?> ol = bContractManager.getProvide(t.getDataProvide());
				if(ol == null){
					System.out.println(t.getTableName() + "找不到：" + t.getDataProvide());
				}else{
					for (Object object : ol) {
						Table temp = t.clone();
						//System.out.println("========" + temp.getTableName());
						buildTable(temp, object);
						newtableList.add(temp);
					}
				}
			} else {
				Map<String, Object> dataProviders = new HashMap<String, Object>();
				
				String[] dps = t.getDataProvide().split("\\|");
				//默认的dataProvider
				Object defDp = bContractManager.getProvide(dps[0]).get(0);
				
				for ( String dp : dps ) {
					if( null != dp ) {
						dataProviders.put(dp, bContractManager.getProvide(dp).get(0));
					}
				}

				Table temp = t.clone();
				//System.out.println("========" + temp.getTableName());
				buildTable(temp, dataProviders, defDp );
				newtableList.add(temp);
				
			}
		}
	}
	
	/* 如果希望将一条数据库记录使用多个数据提供者 */
	private void buildTable(Table t, Map<String, Object> dataProviders, Object defDp ) {
		if (0 == dataProviders.size()) {// 不管是主表还是从表，还是普通的单独一张表，只要数据提供者为空，那么就直接return
			if (t.getAs() != null) {
				list.add(t.getAs());// 如果是主表，那么就把别名放到主表别名的List里面，主表数据为空，那么没必要录入从表的数据
			}
			return;
		}

		if (t.getDependent() != null) {// 如果数据提供者存在 ，该表是从表并且 依赖的 主表 被记录到list里
			// ，直接返回，这里容易迷惑人，可以不看这句话
			for (String string : list) {
				if (t.getDependent().equals(string)) {// 如果从表引用的主表中没有数据，那么就不要给从表字段赋值了，即不要插入从表中的数据了
					return;
				}
			}
		}
		List<Item> items = t.getItems();// 得到当前Table对象的Item对象集合，即当前Table的所有对应字段
		for (Item _item : items) {// 循环遍历这些字段
			MappingAction mappingAction = ptmm.getAction(_item.getParameterType());// provideType对应的MappingAction处理类全路径
			if (mappingAction == null) {
				System.out.println(_item.getParent().getTableName() + ":" + _item.getFieldName() + " Action is null!!!!");
			}
			mappingAction.setProvideInstance(null);
			mappingAction.setMappingItem(null);
			mappingAction.setGlobalDataMap(globalDataMap);

			mappingAction.setMappingItem(_item);// 将当前Table中的一个Item放到对应的MappingAction类中
			String provideName = _item.getDataProvider();// 每个Item也可能有数据提供者，所以得到提供者的名称

			
			if (provideName != null && !provideName.equals("")) {// 如果字段Item有单独的数据提供者(即不使用当前Table自身的数据提供者)
				
				if (null != dataProviders.get(provideName)) {	//先从传入的提供者上找，找不到再调bContractManager.getProvide从
					mappingAction.setProvideInstance(dataProviders.get(provideName));// 存放BContractManager中得到的数据提供者，因为返回了一条记录，所以这里直接得到第0条记录
				} else {
					List<?> provides = bContractManager.getProvide(provideName);
					if (provides == null) {
						log.error("----------Provider:" + provideName + " is null!");
						continue;
					}
					if (provides.size() > 0) {
						mappingAction.setProvideInstance(provides.get(0));// 存放BContractManager中得到的数据提供者，因为返回了一条记录，所以这里直接得到第0条记录
					} else {

					}

				}
			} else {//如果ITEM没有特殊的dataProvider
				mappingAction.setProvideInstance(defDp);
			}
			mappingAction.execute();
		}
	}

	private void buildTable(Table t, Object obj) {
		if (obj == null) {// 不管是主表还是从表，还是普通的单独一张表，只要数据提供者为空，那么就直接return
			if (t.getAs() != null) {
				list.add(t.getAs());// 如果是主表，那么就把别名放到主表别名的List里面，主表数据为空，那么没必要录入从表的数据
			}
			return;
		}

		if (t.getDependent() != null) {// 如果数据提供者存在 ，该表是从表并且 依赖的 主表 被记录到list里
			// ，直接返回，这里容易迷惑人，可以不看这句话
			for (String string : list) {
				if (t.getDependent().equals(string)) {// 如果从表引用的主表中没有数据，那么就不要给从表字段赋值了，即不要插入从表中的数据了
					return;
				}
			}
		}
		List<Item> items = t.getItems();// 得到当前Table对象的Item对象集合，即当前Table的所有对应字段
		for (Item _item : items) {// 循环遍历这些字段
			MappingAction mappingAction = ptmm.getAction(_item.getParameterType());// provideType对应的MappingAction处理类全路径
			if (mappingAction == null) {
				System.out.println(_item.getParent().getTableName() + ":" + _item.getFieldName() + " Action is null!!!!");
			}
			mappingAction.setProvideInstance(null);
			mappingAction.setMappingItem(null);
			mappingAction.setGlobalDataMap(globalDataMap);

			mappingAction.setMappingItem(_item);// 将当前Table中的一个Item放到对应的MappingAction类中
			String provideName = _item.getDataProvider();// 每个Item也可能有数据提供者，所以得到提供者的名称

			if (provideName != null && !provideName.equals("")) {// 如果字段Item有单独的数据提供者(即不使用当前Table自身的数据提供者)

				List<?> provides = bContractManager.getProvide(provideName);// 得到这个数据提供者，比如说GLOBAL和CSNODE，List里面只有一条记录，
				if (provides == null) {
					log.error("----------Provider:" + provideName + " is null!");
					continue;
				}
				if (provides.size() > 0) {
					mappingAction.setProvideInstance(provides.get(0));// 存放BContractManager中得到的数据提供者，因为返回了一条记录，所以这里直接得到第0条记录
				} else {

				}
			} else {
				mappingAction.setProvideInstance(obj);
			}
			mappingAction.execute();
		}
	}

	/**
	 * 此方法抛出异常，才能回滚
	 */
	public boolean execute() throws Exception {
		boolean rs = false;
		// try {
		if (this.mapDesc == null) {
			String productNodeID = contractNode.getProductNodeCode();
			String productID = contractNode.getProductCode();
			this.mapDesc = MappingCacheManager.getMapping(productID, productNodeID, object_type);
		}
		// long ftimes = 0;
		// log.info("333=========="+ftimes);
		initBContractAndGlobalDataMap();// 设置BContractManager中的BContract和globalDataMap
		// log.info("335共耗时:"+(System.currentTimeMillis()-ftimes));
		// ftimes = System.currentTimeMillis();
		initProvide();// 得到xml中标签<dataprovider>对应的DataProvider对象的Map集合，并且调用BContractManager.init(provideMap)
		// log.info("337共耗时:"+(System.currentTimeMillis()-ftimes));
		// ftimes = System.currentTimeMillis();
		initMapping();// 初始化配置
		// log.info("339共耗时:"+(System.currentTimeMillis()-ftimes));
		// ftimes = System.currentTimeMillis();
		doAction();// 解析每个item，去找到对应的MappingAction，然后给item赋值，用于拼凑SQL语句的时候给"?"赋值
		// log.info("341共耗时:"+(System.currentTimeMillis()-ftimes));
		rs = true;
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		return rs;
	}

	@SuppressWarnings("rawtypes")
	public List getResult() {
		return mapDesc.getTable();
	}

	public void setBContractNode(BContractNode contractNode) {
		this.contractNode = contractNode;
	}

	public void setMapDocument(MappingDescription mapDesc) {
		this.mapDesc = mapDesc;
	}

	public void setGlobalDataMap(Map<String, String> globalDataMap) {
		this.globalDataMap = globalDataMap;
	}

	// @Override
	// public void setBContract(BContract bContract) {
	// this.bContract = bContract;
	// }

}
