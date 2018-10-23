package com.sysnet.poc.claimmapping;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import com.sysnet.poc.claimmapping.vo.DataProvide;
import com.sysnet.poc.claimmapping.vo.Item;
import com.sysnet.poc.claimmapping.vo.MappingDescription;
import com.sysnet.poc.claimmapping.vo.Table;
import com.sysnet.poc.util.XmlUtil;

/**
 * XML转换
 * 
 * @author cwduan
 * 
 */
public class MappingHelper {
	/**
	 * 将当事方的XML映射文件转换成POJO对象
	 * 
	 * @param fileName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public MappingDescription XmlToObject(String fileName) throws Exception {
		// 声明需要的对象
		MappingDescription mappingDescription = new MappingDescription();
		List<DataProvide> dataProvideList = new ArrayList<DataProvide>();// 存放对应xml文件中的<dataProvider>对象
		List<Table> tableList = new ArrayList<Table>();// 存放对应xml文件中的<table>对象
		Document document = null;

		// 取得document对象
		document = XmlUtil.getDocument(fileName);
		if (document == null) {
			throw new FileNotFoundException(fileName + " XML File not found!");
		}
		// 找到根节点
		Element root = document.getRootElement();

		// 解析dataProviders
		Element dataProviders = root.getChild("dataProviders");
		List<Element> dataProvidersList = dataProviders.getChildren();
		for (int i = 0; i < dataProvidersList.size(); i++) {
			DataProvide dataProvide = new DataProvide();
			Element element = dataProvidersList.get(i);
			dataProvide.setName(element.getAttributeValue("name"));
			dataProvide.setNodeName(element.getAttributeValue("nodeName"));
			dataProvide.setProvideType(element.getAttributeValue("providerType"));
			dataProvide.setWhen(element.getAttributeValue("when"));
			dataProvide.setDefaultValue("default");
			dataProvideList.add(dataProvide);
		}
		mappingDescription.setDataProvide(dataProvideList);
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
				_table.setWhen(table.getAttributeValue("when"));
				_table.setDefaultValue("default");
				List<Element> items = table.getChildren();
				List<Item> itemList = new ArrayList<Item>();
				for (int m = 0; m < items.size(); m++) {
					Element item = items.get(m);
					Item _item = new Item();
					_item.setFieldName(item.getAttributeValue("fieldName"));
					_item.setFieldType(item.getAttributeValue("fieldType"));
					_item.setParameter(item.getAttributeValue("parameter"));
					_item.setParameterType(item.getAttributeValue("parameterType"));
					_item.setMethod(item.getAttributeValue("method"));
					_item.setDataProvider(item.getAttributeValue("dataProvider"));
					_item.setRef(item.getAttributeValue("ref"));
					_item.setSequenceName(item.getAttributeValue("sequenceName"));
					_item.setValue(item.getAttributeValue("value"));
					_item.setValue(item.getAttributeValue("default"));
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
}
