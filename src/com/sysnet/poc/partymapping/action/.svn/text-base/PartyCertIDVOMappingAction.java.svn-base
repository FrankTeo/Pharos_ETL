package com.sysnet.poc.partymapping.action;

import java.util.List;

import org.apache.commons.logging.Log;

import pharos.party.vo.PartyCertIDVO;

import com.sysnet.poc.partymapping.ReflectUtil;
import com.sysnet.poc.partymapping.SequenceUtil;
import com.sysnet.poc.partymapping.vo.Item;
import com.sysnet.poc.partymapping.vo.MappingDescription;
import com.sysnet.poc.partymapping.vo.Table;
import com.sysnet.poc.util.OdsLogger;

/**
 * STG_PTY_PARTY_CERTID--当事方_证件信息映射处理
 * 
 * @author lu_haibin
 * 
 */
public class PartyCertIDVOMappingAction extends AbsPharosMappingAction {

	private Item item;
	private static final Log log = OdsLogger.getLog("partyLog4j", "Party");

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
			} else {
				PartyCertIDVO partyCertIDVO = (PartyCertIDVO) this.provideInstnace;
				String methodNm = item.getMethod();
				Object obj = null;
				// 调用反射
				if (methodNm == null) {
					log.error("class PartyCertIDVOMappingAction report:" + item.getParameter() + "get method name is null!");
				} else {
					if (methodNm.indexOf(".") != -1) {
						String temp[] = methodNm.split("[.]");
						for (int i = 0; i < temp.length; i++) {
							if (i == 0) {
								obj = ReflectUtil.getObjByReflect(partyCertIDVO, temp[i]);
							} else {
								obj = ReflectUtil.getObjByReflect(obj, temp[i]);
							}
						}

					} else {
						obj = ReflectUtil.getObjByReflect(partyCertIDVO, methodNm);
					}
				}
				item.setValue(String.valueOf(obj));
			}
			rs = true;
		} catch (Exception ex) {
			log.error("PartyCertIDVOMappingAction:", ex);
			ex.printStackTrace();
		}
		return rs;

	}

	public void setMappingItem(Item item) {

		this.item = item;

	}

}
