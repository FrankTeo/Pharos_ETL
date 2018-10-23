package com.sysnet.poc.partymapping.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pharos.framework.base.Money;
import pharos.party.vo.PartyCertIDVO;
import pharos.party.vo.PartyVO;

import com.sysnet.poc.partymapping.ReflectUtil;
import com.sysnet.poc.util.DateHelper;

/**
 * 对象类型处理类
 * 
 * @author DBC
 * 
 */
public class ObjectAction extends AbsPharosMappingAction {

	@Override
	public boolean execute() throws Exception {
		if (item.getValue() != null && item.getValue().equals("null")) {
			item.setValue(null);
			return true;
		}
		if (item.getValue() != null && !item.getValue().equals("") && item.getValue().startsWith("pass:")) {
			String value = item.getValue();
			item.setValue(value.substring(5));
			return true;
		} else {
			boolean rs = false;
			Object obj = provideInstnace;
			try {

				if (item.getFieldName().equals("CrtTime")) {
					SimpleDateFormat matter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
					item.setValue(matter.format(new Date()));
				} else {
					// 按ID获取不同的方法配制模板
					String methodNm = item.getMethod();
					if (methodNm == null) {
						// log.error("class PartyVOMappingAction report:"+
						// item.getParameter() + "get method name is null!");
					} else {
						String temp[] = methodNm.split("[.]");// 用"."将这个方法的字符串分割
						for (String string : temp) {
							if (string.equals("getCert")) {
								PartyVO p = (PartyVO) obj;
								List<PartyCertIDVO> pcil = p.getCerts();
								for (PartyCertIDVO partyCertIDVO : pcil) {
									if (partyCertIDVO.getGovissuredtype().equals(item.getParameter())) {
										obj = partyCertIDVO;
										break;
									}
								}
							} else if (string.equals("get0")) {
								@SuppressWarnings("unchecked")
								List<Object> ol = (List<Object>) obj;
								if (ol != null && ol.size() > 0) {
									obj = ol.get(0);
								} else {
									obj = null;
								}
							} else if (string.equals("getPartyDeclarationByCode")) {
								PartyVO p = (PartyVO) obj;
								obj = p.getPartyDeclarationByCode(item.getParameter());
							} else {
								obj = ReflectUtil.getObjByReflect(obj, string);
							}
						}
						if (obj == null || obj.toString().equals("")) {
							if (item.getValue() == null || item.getValue().equals("")) {
								item.setValue(null);
							}
						} else if (obj instanceof Money) {
							item.setValue(String.valueOf(((Money) obj).getAmount()));
						} else if (obj instanceof java.util.Date) {
							item.setValue(DateHelper.convertEnDateToCnDateTime(String.valueOf(obj)));
						} else {
							item.setValue(String.valueOf(obj));
						}
					}
				}
				rs = true;
			} catch (Exception e) {
				System.out.println(item.getFieldName());
				System.out.println(item.getParent().getTableName());
				// e.printStackTrace();
				throw e;
			} finally {
			}
			return rs;
		}
	}
}
