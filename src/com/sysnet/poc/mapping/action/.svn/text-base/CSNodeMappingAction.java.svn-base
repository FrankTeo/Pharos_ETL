package com.sysnet.poc.mapping.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;

import pharos.ctm.vo.BContractNode;
import pharos.framework.base.MultiMoney;

import com.sysnet.poc.mapping.ReflectUtil;
import com.sysnet.poc.mapping.SequenceUtil;
import com.sysnet.poc.mapping.TotalPremiumAndInsure;
import com.sysnet.poc.mapping.vo.Item;
import com.sysnet.poc.mapping.vo.MappingDescription;
import com.sysnet.poc.mapping.vo.Table;
import com.sysnet.poc.util.OdsLogger;

/**
 * CSNODE映射处理
 * 
 * @author li_yanpeng
 * 
 */
public class CSNodeMappingAction extends AbsPharosMappingAction {

	private Item item;
	private static final Log log = OdsLogger.getLog("insureLog4j", "Insure");

	public boolean execute() {
		// if(item.getParameter().equals("policyNo")){
		// System.out.println("policyno");
		// }
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
				BContractNode bNode = (BContractNode) this.provideInstnace;
				String methodNm = item.getMethod();
				Object obj = null;
				// 调用反射
				if (methodNm.equals("insuredvalue")) {
					TotalPremiumAndInsure totalPremiumAndInsure = new TotalPremiumAndInsure();
					List<BContractNode> riskNodeList = new ArrayList<BContractNode>();
					totalPremiumAndInsure.getRiskNode(bNode, riskNodeList);
					MultiMoney multiMoney = totalPremiumAndInsure.getTotalInsuredValue(riskNodeList);
					obj = String.valueOf(totalPremiumAndInsure.getTotalP_I(multiMoney));
				} else if (methodNm.equals("preminumvalue")) {
					TotalPremiumAndInsure totalPremiumAndInsure = new TotalPremiumAndInsure();
					List<BContractNode> riskNodeList = new ArrayList<BContractNode>();
					totalPremiumAndInsure.getRiskNode(bNode, riskNodeList);
					MultiMoney multiMoney = totalPremiumAndInsure.getTotalPremiumValue(riskNodeList);
					obj = String.valueOf(totalPremiumAndInsure.getTotalP_I(multiMoney));
				}
				// else if(methodNm.equals("insure_group_id"))
				// {
				// item.setValue(bNode.getParentContractNode().getNodeNo().replaceAll("-",
				// ""));
				// }
				else if (methodNm.equals("default")) {
					obj = item.getValue();
				} else if (methodNm.indexOf(".") != -1) {

					String temp[] = methodNm.split("[.]");
					for (int i = 0; i < temp.length; i++) {
						if (i == 0) {
							obj = ReflectUtil.getObjByReflect(bNode, temp[i]);
						} else {
							obj = ReflectUtil.getObjByReflect(obj, temp[i]);
						}
					}
				} else {
					obj = ReflectUtil.getObjByReflect(bNode, methodNm);
				}
				item.setValue(String.valueOf(obj));
			}
			rs = true;
		} catch (Exception ex) {
			// ex.printStackTrace();
			log.debug(ex.getMessage());
			return rs;
		}
		return rs;
	}

	public void setMappingItem(Item item) {
		this.item = item;
	}

}
