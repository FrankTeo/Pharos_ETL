package com.sysnet.poc.mapping.action;

import java.util.List;

import org.apache.commons.logging.Log;

import pharos.ctm.vo.BRole;

import com.sysnet.poc.mapping.ReflectUtil;
import com.sysnet.poc.mapping.SequenceUtil;
import com.sysnet.poc.mapping.vo.Item;
import com.sysnet.poc.mapping.vo.MappingDescription;
import com.sysnet.poc.mapping.vo.Table;
import com.sysnet.poc.util.DateHelper;
import com.sysnet.poc.util.OdsLogger;

/**
 * 角色映射处理
 * 
 * @author li_yanpeng
 * 
 */
public class RoleMappingAction extends AbsPharosMappingAction {

	private Item item;
	private static final Log log = OdsLogger.getLog("insureLog4j", "Insure");

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
				BRole brole = (BRole) this.provideInstnace;
				if (item.getParameter().equals("insurancecode")) {
					brole.getParty().getCode();

				}
				String methodNm = item.getMethod();
				Object obj = null;
				// 调用反射
				if (methodNm == null) {
				} else {
					if (methodNm.indexOf(".") != -1) {
						String temp[] = methodNm.split("[.]");
						for (int i = 0; i < temp.length; i++) {
							if (i == 0) {
								obj = ReflectUtil.getObjByReflect(brole, temp[i]);
							} else {
								obj = ReflectUtil.getObjByReflect(obj, temp[i]);
							}
						}

					} else {

						if (item.getParameter().equals("education")) {
							obj = brole.getParty().getPartyDeclarationByCode("10060").getValue();
						} else if (item.getParameter().equals("marriage_status")) {
							obj = brole.getParty().getPartyDeclarationByCode("10062").getValue();
						} else if (item.getParameter().equals("layer")) {

							if ((brole.getParentContractNode().getProductCode()).equals("301")) {
								obj = brole.getParty().getPartyDeclarationByCode("10000").getValue();
							} else if ((brole.getParentContractNode().getProductCode()).equals("302")) {
								obj = brole.getParty().getPartyDeclarationByCode("10000").getValue();
							} else if ((brole.getParentContractNode().getProductCode()).equals("298")) {
								obj = brole.getParty().getPartyDeclarationByCode("10004").getValue();
							} else {
								obj = brole.getParty().getPartyDeclarationByCode("10004").getValue();
							}

						} else {
							obj = ReflectUtil.getObjByReflect(brole, methodNm);
						}

					}
				}
				if (obj instanceof java.util.Date) {
					item.setValue(DateHelper.convertEnDateToCnDateTime(String.valueOf(obj)));
				} else {
					item.setValue(String.valueOf(obj));
				}

			}
			rs = true;
		} catch (Exception ex) {
			log.debug(ex.getMessage());
		}
		return rs;
	}

	public void setMappingItem(Item item) {
		this.item = item;
	}

}
