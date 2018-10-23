package com.sysnet.poc.partymapping.action;

import java.util.List;

import pharos.ctm.vo.BDeclaration;
import pharos.ctm.vo.BDeclarationForm;
import pharos.framework.p.ElementEnum;

import com.sysnet.poc.partymapping.SequenceUtil;
import com.sysnet.poc.partymapping.vo.Item;
import com.sysnet.poc.partymapping.vo.MappingDescription;
import com.sysnet.poc.partymapping.vo.Table;

/**
 * 从FORM数据项映射处理s
 * 
 * @author li_yanpeng
 * 
 */
public class BDeclarationFormMappingAction extends AbsPharosMappingAction {

	private Item item;

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

				item.setValue("10");// 临时
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
				BDeclarationForm bdform = (BDeclarationForm) this.provideInstnace;
				d = (BDeclaration) bdform.getElementByName(ElementEnum.BDECLARATION_CODE, item.getParameter(), true);
				item.setValue(String.valueOf(d.getValue()));

				// BDeclaration d = null;
				// // 从节点上取数据项
				// PartyDeclarationForm dform = (PartyDeclarationForm)
				// this.provideInstnace;
				// d = (BDeclaration) dform.getElementByName(
				// ElementEnum.BDECLARATION_CODE, item.getParameter(),true);
				// if (d != null) {
				// if (ConstDataType.MONEY == d.getDataType()) {
				//
				// if (d != null && d.getValue() != null) {
				// Money m = (Money) d.getValue();
				// item.setValue(String.valueOf(m.getAmount()));
				// }
				// }
				// else if(ConstDataType.DATETIME == d.getDataType())
				// {
				// if (d != null && d.getValue() != null) {
				// Date date = (Date) d.getValue();
				// String format = "yyyy-MM-dd";
				// SimpleDateFormat sdf = this.getSimpleDateFormat(format);
				// String str = sdf.format(date);
				// item.setValue(str);
				// }
				// }
				// else {
				// item.setValue(String.valueOf(d.getValue()));
				// }
				// }
			}
			rs = true;
		} catch (Exception ex) {
			// System.out.println("DeclarationFormMappingAction取值出错了！！！！！！！！！！！！！！！！！");
			ex.printStackTrace();
		}
		return rs;
	}

	public void setMappingItem(Item item) {
		this.item = item;
	}

}
