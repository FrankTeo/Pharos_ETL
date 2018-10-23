package com.sysnet.poc.mapping;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import com.sysnet.poc.mapping.vo.DataProvide;
import com.sysnet.poc.mapping.vo.Item;
import com.sysnet.poc.mapping.vo.MappingDescription;
import com.sysnet.poc.mapping.vo.Table;
import com.sysnet.poc.util.XmlUtil;

public class MappingHelper {
	static Logger log = Logger.getLogger(MappingHelper.class);

	/**
	 * 将XML文件转换成POJO
	 * 
	 * @param fileName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public MappingDescription XmlToObject(String fileName) {
		// 声明需要的对象
		MappingDescription mappingDescription = new MappingDescription();
		List<DataProvide> dataProvideMap = new ArrayList<DataProvide>();
		List<Table> tableList = new ArrayList<Table>();
		Document document = null;

		// 取得document对象
		document = XmlUtil.getDocument(fileName);
		if (document == null) {
			log.error(fileName + " XML File not found!");
		}
		// 找到根节点
		Element root = document.getRootElement();
		// 设置产品ID和产品节点ID
		mappingDescription.setProductCode(root.getAttributeValue("productCode"));
		mappingDescription.setProductNodeCode(root.getAttributeValue("prodectNodeCode"));
		// 解析dataProviders
		Element dataProviders = root.getChild("dataProviders");
		List<Element> dataProvidersList = dataProviders.getChildren();
		for (int i = 0; i < dataProvidersList.size(); i++) {
			DataProvide dataProvide = new DataProvide();
			Element element = dataProvidersList.get(i);
			dataProvide.setName(element.getAttributeValue("name"));
			dataProvide.setNodeName(element.getAttributeValue("nodeName"));
			dataProvide.setProvideType(element.getAttributeValue("providerType"));
			dataProvideMap.add(dataProvide);
		}
		mappingDescription.setDataProvide(dataProvideMap);
		// 解析tables
		Element mapping = root.getChild("mapping");
		List<Element> areas = mapping.getChildren();
		for (int i = 0; i < areas.size(); i++) {
			Element area = areas.get(i);
			List<Element> tables = area.getChildren();
			for (int j = 0; j < tables.size(); j++) {
				Element table = tables.get(j);
				Table _table = new Table();
				_table.setDataProvide(table.getAttributeValue("dataProvider"));
				_table.setTableName(table.getAttributeValue("tableName"));
				_table.setDependent(table.getAttributeValue("dependent"));
				_table.setAs(table.getAttributeValue("as"));
				List<Element> items = table.getChildren();
				List<Item> itemList = new ArrayList<Item>();
				for (int m = 0; m < items.size(); m++) {
					Element item = items.get(m);
					Item _item = new Item();
					_item.setFieldName(item.getAttributeValue("fieldName"));
					_item.setFieldType(item.getAttributeValue("fieldType"));
					_item.setParameter(item.getAttributeValue("parameter"));
					_item.setParameterType(item.getAttributeValue("parameterType"));
					_item.setRef(item.getAttributeValue("ref"));
					_item.setSequenceName(item.getAttributeValue("sequenceName"));
					_item.setIfNull(item.getAttributeValue("ifNull"));
					_item.setValue(item.getAttributeValue("value"));

					if (item.getAttributeValue("dataProvider") == null || item.getAttributeValue("dataProvider").equals("")) {
						// _item.setDataProvider(table.getAttributeValue("dataProvider"));
					} else {
						_item.setDataProvider(item.getAttributeValue("dataProvider"));
					}
					_item.setParent(_table);
					itemList.add(_item);
				}
				_table.setItems(itemList);
				_table.setParent(mappingDescription);
				tableList.add(_table);
			}
		}
		mappingDescription.setTable(tableList);
		return mappingDescription;
	}

	/**
	 * @param doc
	 */
	public MappingDescription XmlToObject(Document doc) {
		return null;
	}

	/**
	 * 加载包含文件方式的模版，并将XML文件转换成POJO
	 * 
	 * @param fileName
	 * @return MappingDescription
	 */
	@SuppressWarnings("unchecked")
	public MappingDescription XmlToObjectByIncludePattern(String fileName) throws Exception {
		// System.out.println("处理模板文件：" + fileName);

		// 声明需要的对象
		MappingDescription mappingDescription = new MappingDescription();// 此对象最终存放的是模板中所有的东西
		List<DataProvide> dataProvideMap = new ArrayList<DataProvide>();// 存放模版中所有的数据提供者相关信息
		List<Table> tableList = new ArrayList<Table>();// 存放模版中所有的table相关信息
		Document document = null;

		// 取得document对象
		document = XmlUtil.getDocument(fileName);
		if (document == null) {
			log.error(fileName + " XML File not found!");
			throw new FileNotFoundException(fileName + " XML File not found!");
		}
		// 找到根节点
		Element root = document.getRootElement();
		// 设置产品编码和产品节点编码
		mappingDescription.setProductCode(root.getAttributeValue("productCode"));
		mappingDescription.setProductNodeCode(root.getAttributeValue("prodectNodeCode"));
		// 解析dataProviders
		Element dataProviders = root.getChild("dataProviders");
		List<Element> dataProvidersList = dataProviders.getChildren();
		for (int i = 0; i < dataProvidersList.size(); i++) {
			DataProvide dataProvide = new DataProvide();
			Element element = dataProvidersList.get(i);
			dataProvide.setName(element.getAttributeValue("name"));
			dataProvide.setNodeName(element.getAttributeValue("nodeName"));
			dataProvide.setProvideType(element.getAttributeValue("providerType"));
			dataProvideMap.add(dataProvide);
		}
		mappingDescription.setDataProvide(dataProvideMap);
		// 解析tables
		Element mapping = root.getChild("mapping");
		List<Element> areas = mapping.getChildren();

		Map<String, String> hdataProvide = null;
		for (int i = 0; i < areas.size(); i++) {
			Element area = areas.get(i);
			String include = area.getAttributeValue("include");
			String replace = area.getAttributeValue("replace");
			System.out.println("Processing template:" + include); //处理模板
			// 存储要替换的部分
			Element inArea = null;// 基础模板中的area标签
			Table re_table = null;// 主模板中的table标签
			hdataProvide = new HashMap<String, String>();// 存放主模板中table标签中的dataprovider
			if (replace.equalsIgnoreCase("true")) {
				re_table = new Table();// 主模板中的table
				Document inDocument = XmlUtil.getDocument("/simple/" + include);// 解析基础模板，即公共模板(存放公共的字段)，每个特有的字段需要在主模板中配置
				if (inDocument == null) {
					System.err.println(include + " Template file missed!!!!!!!!!!!!!!!!!!!!:");//缺少模板文件!
					continue;
				}
				Element inRoot = inDocument.getRootElement();// 得到基础模板的根节点
				Element inMapping = inRoot.getChild("mapping");// 得到基础模板的mapping节点
				inArea = inMapping.getChild("area");// 得到基础模板的area节点

				Element reTable = area.getChild("table");// 得到主模板中的table节点

				String[] dt1 = reTable.getAttributeValue("dataProvider").split(";");// "CSNODE=CSNODE;DECLARATION=Group_D"，注意：即使这里没有分号也会将值存放进去
				for (int j = 0; j < dt1.length; j++) {
					String[] dt2 = dt1[j].split("=");
					for (int k = 0; k < dt2.length; k++) {
						hdataProvide.put(dt2[0], dt2[1]);// 将主模板中table标签中的dataProvider存储在Map里，dataProviderType作为key,dataProvider的name作为value，放到hdataProvide这个Map中
					}
				}
				// -----------
				// re_table.setTableName(reTable.getAttributeValue("dataProviderType"));//李雁鹏这里有点疑惑
				// re_table.setDataProvide(reTable.getAttributeValue("dataProvider"));//海滨修改
				re_table.setTableName(reTable.getAttributeValue("tableName"));
				re_table.setDependent(reTable.getAttributeValue("dependent"));
				re_table.setAs(reTable.getAttributeValue("as"));

				List<Element> re_items = reTable.getChildren();
				List<Item> re_itemList = new ArrayList<Item>();// 解析主模板中需要替换掉基础模板中的字段
				for (int r = 0; r < re_items.size(); r++) {
					Element item = re_items.get(r);
					Item _item = new Item();
					_item.setFieldName(item.getAttributeValue("fieldName"));
					_item.setFieldType(item.getAttributeValue("fieldType"));
					_item.setParameter(item.getAttributeValue("parameter"));
					_item.setParameterType(item.getAttributeValue("parameterType"));
					_item.setRef(item.getAttributeValue("ref"));
					_item.setSequenceName(item.getAttributeValue("sequenceName"));
					_item.setValue(item.getAttributeValue("value"));
					_item.setDataProvider(item.getAttributeValue("dataProvider"));
					_item.setMethod(item.getAttributeValue("method"));
					_item.setIfNull(item.getAttributeValue("ifNull"));
					re_itemList.add(_item);
				}
				re_table.setItems(re_itemList);
			}
			// 主模板解析与存储完毕 -----

			List<Element> tables = inArea.getChildren();// 解析基础模板中的所有table
			for (int j = 0; j < tables.size(); j++) {
				Element table = tables.get(j);
				Table _table = new Table();
				if (re_table != null)// 说明主模板中的replace属性是true，并且主模板中有table标签，此时用主模板中的相关数据来填充基础模板中的相关数据
				{
					_table.setDataProvide(hdataProvide.get(table.getAttributeValue("dataProviderType")));// 从主模板中得到数据提供者，这里即使在基础模板中的table标签中不配置dataprovider属性也可以，这是因为在主模板中的table标签中已经配置了dataprovider属性了
					_table.setTableName(re_table.getTableName());// 将主模板的表名作为基础模板的表名
					_table.setDependent(re_table.getDependent());// 将主模板的子表引用的主表名称作为基础模板的这个名称
					_table.setAs(re_table.getAs());// 将主模板的主表别名作为基础模板的主表别名
				} else// 说明主模板中的replace属性是false，并且主模板中没有table标签，此时用基础模板本身的相关数据去填充它自己的相关数据
				{
					_table.setDataProvide(table.getAttributeValue("dataProvider"));
					_table.setTableName(table.getAttributeValue("tableName"));
					_table.setDependent(table.getAttributeValue("dependent"));
					_table.setAs(table.getAttributeValue("as"));
				}

				List<Element> items = table.getChildren();// 得到基础模板中table所有的item元素
				List<Item> itemList = new ArrayList<Item>();// 用来存放主模板中和基础模板中的字段
				for (int m = 0; m < items.size(); m++)// 循环基础模板中的item元素
				{
					Element item = items.get(m);
					Item _item = new Item();
					List<Item> re_itemList = re_table.getItems();// 得到主模板中当前table元素所有item元素
					int flag = 0;// 用来标记主模板中的字段是否要替换掉基础模板中的字段，0表示不需要替换基础表中相关的字段元素，1表示需要替换基础表中相关的字段元素
					for (int mm = 0; mm < re_itemList.size(); mm++)// 循环主模板中的当前table元素中的每个item元素
					{
						Item re_item = re_itemList.get(mm);
						if (re_item.getFieldName().equalsIgnoreCase(item.getAttributeValue("fieldName"))) {// 如果主模板中的item元素跟基础模板中的item元素有字段重复的话，那么就用主模板中的item元素的相关值替换掉基础模板中的这个item元素的相关值
							// 这样做的目的是为了处理特别约定跟告知这两个特殊的结构，因为程序现在不支持直接将特别约定或者告知中的所有数据项都拿过来，直接for循环每个数据项的功能，
							// 为了适应模板的代码，就需要在配置模板的主模板中配置多个有类似模样的特别约定这种数据项模板，来处理这种情况

							_item.setFieldName(re_item.getFieldName());
							_item.setFieldType(re_item.getFieldType());
							_item.setParameter(re_item.getParameter());
							_item.setParameterType(re_item.getParameterType());
							_item.setIfNull(re_item.getIfNull());
							_item.setRef(re_item.getRef());
							_item.setSequenceName(re_item.getSequenceName());
							_item.setValue(re_item.getValue());
							_item.setDataProvider(re_item.getDataProvider());
							_item.setMethod(re_item.getMethod());
							flag = 1;
							break;
						}
					}
					if (flag == 0)// 此时直接用基础模板中的item元素即可
					{
						_item.setFieldName(item.getAttributeValue("fieldName"));
						_item.setFieldType(item.getAttributeValue("fieldType"));
						_item.setParameter(item.getAttributeValue("parameter"));
						_item.setParameterType(item.getAttributeValue("parameterType"));
						_item.setIfNull(item.getAttributeValue("ifNull"));
						_item.setRef(item.getAttributeValue("ref"));
						_item.setSequenceName(item.getAttributeValue("sequenceName"));
						_item.setValue(item.getAttributeValue("value"));
						_item.setMethod(item.getAttributeValue("method"));
						_item.setDataProvider(item.getAttributeValue("dataProvider"));	//add by Frank Zhang 20140903
						// if(item.getAttributeValue("dataProvider")==null ||
						// item.getAttributeValue("dataProvider").equals(""))
						if (table.getAttributeValue("dataProviderType").equals(item.getAttributeValue("parameterType"))) {
							// 如果item元素没有对应的数据提供者，那么就什么也不做，直接使用table自己的那个数据提供者
						} else if (item.getAttributeValue("parameterType").equals("GLOBAL")) {// 如果数据提供者是全局的，那么就使用这个全局的为这个item的数据提供者
							_item.setDataProvider("GLOBAL");
						} else if (item.getAttributeValue("parameterType").equals("OBJECT")) {// 如果数据提供者是全局的，那么就使用这个全局的为这个item的数据提供者
							_item.setDataProvider("OBJECT");
						} else {// 否则就是当前这个item是有单独的数据提供者的，这个数据提供者就使用主模板中提供的那个即可，这里还说明了一个问题，即在基础模板中item的dataprovider属性都是没有任何作用的
							_item.setDataProvider(hdataProvide.get(item.getAttributeValue("parameterType")));
						}
					}

					_item.setParent(_table);// 设置基础模板中的每一个item元素的父亲为基础模板的这个item当前所属的table元素
					itemList.add(_item);
				}
				_table.setItems(itemList);
				_table.setParent(mappingDescription);// 设置基础模板中的table元素的父亲为包括主模板以及基础模板合并之后的所有元素，即mappingDescription
				/*
				 * 设置以上这两个的Parent属性是为了在模板中使用ref这个属性，如果前面的table中已经取了某一个字段的值了， 那么直接使用ref属性来引用到刚才已经取的这个字段上面，将值取出来即可
				 */
				tableList.add(_table);
			}
		}
		mappingDescription.setTable(tableList);
		return mappingDescription;
	}

}
