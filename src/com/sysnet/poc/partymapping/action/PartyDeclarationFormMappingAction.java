package com.sysnet.poc.partymapping.action;

import java.util.List;

import pharos.ctm.vo.BDeclaration;
import pharos.party.vo.PartyDeclarationForm;

import com.sysnet.poc.partymapping.SequenceUtil;
import com.sysnet.poc.partymapping.vo.Item;
import com.sysnet.poc.partymapping.vo.MappingDescription;
import com.sysnet.poc.partymapping.vo.Table;

/**
 * 从FORM数据项映射处理
 * 
 * @author li_yanpeng
 * 
 */
public class PartyDeclarationFormMappingAction extends AbsPharosMappingAction {

	private Item item;

	public boolean execute() throws Exception {
		boolean rs = false;
		// try {
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
			PartyDeclarationForm pdform = (PartyDeclarationForm) this.provideInstnace;
			if (pdform == null) {
				item.setValue("");

			} else {
				d = pdform.getPartyDeclByCode(item.getParameter());
				item.setValue(String.valueOf(d.getValue()));

			}

		}
		rs = true;
		// }
		// catch (Exception ex) {
		// //System.out.println("DeclarationFormMappingAction取值出错了！！！！！！！！！！！！！！！！！");
		// ex.printStackTrace();
		// }
		return rs;
	}

	public void setMappingItem(Item item) {
		this.item = item;
	}

}
