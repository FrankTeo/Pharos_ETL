package com.sysnet.poc.mapping.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;

import pharos.ctm.vo.BContractNode;
import pharos.ctm.vo.BDeclaration;
import pharos.framework.base.Money;
import pharos.framework.constparameter.constant.ConstDataType;
import pharos.framework.p.ElementEnum;

import com.sysnet.poc.mapping.ReflectUtil;
import com.sysnet.poc.mapping.SequenceUtil;
import com.sysnet.poc.mapping.vo.Item;
import com.sysnet.poc.mapping.vo.MappingDescription;
import com.sysnet.poc.mapping.vo.Table;
import com.sysnet.poc.util.DateHelper;
import com.sysnet.poc.util.OdsLogger;

/**
 * 从CSNODE数据项映射处理
 * 
 * @author li_yanpeng
 * 
 */
public class DeclarationMappingAction extends AbsPharosMappingAction {

	private static final Log log = OdsLogger.getLog("insureLog4j", "Insure");

	private Item item;

	@SuppressWarnings("rawtypes")
	public boolean execute() {
		boolean rs = false;
		try {
			if ((item.getParameter().trim()).equals("timestamp")) {
				item.setValue("null");
			}
			// 如果是 序列取序列的值
			if (item.getSequenceName() != null && !item.getSequenceName().equals("")) {
				String seq = String.valueOf(SequenceUtil.getSequenceNextValueByName(item.getSequenceName()));// 取序列
				item.setValue(seq);
			}
			// 如果是引用取引用的值
			else if (item.getRef() != null && !item.getRef().equals("")) {
				String arr[] = item.getRef().split("[/]");
				MappingDescription mapDesc = item.getParent().getParent();
				List<Table> tableList = mapDesc.getTable();
				for (Table table : tableList) {
					if (table.getTableName().equalsIgnoreCase(arr[1]) && table.getDataProvide().equalsIgnoreCase(arr[0])) {
						List<Item> itemList = table.getItems();
						for (Item _item : itemList) {
							if (_item.getName().equalsIgnoreCase(arr[2])) {
								item.setValue(_item.getValue());
								break;
							}
						}
						break;
					}
				}
			}
			// 取数据项的值
			else {
				BDeclaration d = null;
				// 从节点上取数据项
				BContractNode bnode = null;
				if (this.provideInstnace instanceof ArrayList) {
					bnode = (BContractNode) (((ArrayList) this.provideInstnace).get(0));
				} else {
					bnode = (BContractNode) this.provideInstnace;
				}
				if (item.getMethod() != null && !item.getMethod().equals("")) {// 从数据项上获取相关属性
					d = (BDeclaration) bnode.getElementByName(ElementEnum.BDECLARATION_CODE, item.getParameter(), false);
					String methodNm = item.getMethod();
					Object obj = null;
					// 调用反射
					if (methodNm.indexOf(".") != -1) {
						String temp[] = methodNm.split("[.]");
						for (int i = 0; i < temp.length; i++) {
							if (i == 0) {
								obj = ReflectUtil.getObjByReflect(d, temp[i]);
							} else {
								obj = ReflectUtil.getObjByReflect(obj, temp[i]);
							}
						}

					} else {
						if (methodNm.equals("getDDeclaration")) {
							obj = d.getDescription("2");
						} else if (methodNm.equals("getValueDescription")) {
							obj = d.getValueDescription("0");
						} else {
							obj = ReflectUtil.getObjByReflect(d, methodNm);
						}

					}
					item.setValue(String.valueOf(obj));
				} else {// 直接获取数据项
					if (bnode == null) {
						log.error("item---------------" + item.getName());
						// 直接返回 rs
						return rs;
					}
					if (bnode.getElementByName(ElementEnum.BDECLARATION_CODE, item.getParameter(), true) == null) {
						// 直接返回 rs
						return rs;
					}

					d = (BDeclaration) bnode.getElementByName(ElementEnum.BDECLARATION_CODE, item.getParameter(), false);

					if (d != null) {
						if (ConstDataType.MONEY == d.getDataType()) {

							if (d != null && d.getValue() != null) {
								Money m = (Money) d.getValue();
								item.setValue(String.valueOf(m.getAmount()));
							}
						} else if (ConstDataType.DATETIME == d.getDataType()) {
							if (d != null && d.getValue() != null) {
								Date date = (Date) d.getValue();
								String format = "yyyy-MM-dd HH:mm:ss";
								SimpleDateFormat sdf = this.getSimpleDateFormat(format);
								String str = sdf.format(date);
								str = DateHelper.convertEnDateToCnDateTime(String.valueOf(date));
								item.setValue(str);
							}
						} else {
							item.setValue(String.valueOf(d.getValue()));
						}
					}
				}
			}
			rs = true;
		} catch (Exception ex) {
			System.out.println("item name:" + item.getFieldName() + "----table name:" + item.getParent().getTableName());
			log.error("DeclarationMappingAction", ex);

		}
		return rs;
	}

	public void setMappingItem(Item item) {
		this.item = item;
	}

	/**
	 * 获取时间格式类
	 * 
	 * @param format
	 * @return
	 */
	private SimpleDateFormat getSimpleDateFormat(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf;
	}

}
