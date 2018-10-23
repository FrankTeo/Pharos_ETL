package com.sysnet.poc.mapping.action;

import java.util.List;

import org.apache.commons.logging.Log;

import pharos.ctm.vo.PaymentSchedule;
import pharos.framework.pkgen.PKGenerator;

import com.sysnet.poc.mapping.ReflectUtil;
import com.sysnet.poc.mapping.SequenceUtil;
import com.sysnet.poc.mapping.vo.Item;
import com.sysnet.poc.mapping.vo.MappingDescription;
import com.sysnet.poc.mapping.vo.Table;
import com.sysnet.poc.util.DateHelper;
import com.sysnet.poc.util.OdsLogger;

public class PaymentScheduleMappingAction extends AbsPharosMappingAction {

	private Item item;

	private static final Log log = OdsLogger.getLog("insureLog4j", "Insure");

	public boolean execute() {

		boolean rs = false;

		try {

			if ((item.getParameter().trim()).equals("timestamp")) {
				item.setValue("null");
				return true;
			}

			if ((item.getParameter().trim()).equals("payplanid")) {

				item.setValue(PKGenerator.nextPK() + "");
				return true;

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

				PaymentSchedule paymentSchedule = (PaymentSchedule) this.provideInstnace;

				String methodNm = item.getMethod();

				Object obj = null;

				if (methodNm == null) {
					log.debug("CSNodeMappingAction method not found!" + item.getParameter());
					return true;
				} else {

					if (methodNm.indexOf(".") != -1) {
						String temp[] = methodNm.split("[.]");
						for (int i = 0; i < temp.length; i++) {
							if (i == 0) {
								obj = ReflectUtil.getObjByReflect(paymentSchedule, temp[i]);
							} else {
								obj = ReflectUtil.getObjByReflect(obj, temp[i]);
							}
						}

					} else {

						obj = ReflectUtil.getObjByReflect(paymentSchedule, methodNm);

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
			return rs;
		}

		return rs;
	}

	public void setMappingItem(Item item) {
		this.item = item;

	}

}
