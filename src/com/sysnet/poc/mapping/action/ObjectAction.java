package com.sysnet.poc.mapping.action;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import pharos.ctm.vo.BContractNode;
import pharos.ctm.vo.BDeclaration;
import pharos.framework.base.Money;
import pharos.framework.element.vo.IElement;
import pharos.framework.element.vo.PElement;
import pharos.party.vo.PartyVO;

import com.sysnet.poc.mapping.vo.Item;
import com.sysnet.poc.partymapping.ReflectUtil;
import com.sysnet.poc.service.dao.RunLogDAO;
import com.sysnet.poc.util.DateHelper;
import com.sysnet.poc.vo.StgRunLog;

/**
 * 对象类型处理类
 * 
 * @author DBC
 * 
 */
public class ObjectAction extends AbsPharosMappingAction {

	private ActionPlus ap;

	public ActionPlus getAp() {
		return ap;
	}

	public void setAp(ActionPlus ap) {
		this.ap = ap;
	}

	private Item item;

	public void setMappingItem(Item item) {

		this.item = item;
	}
	
	private Map<String, String> globalDataMap;
	
	public void setGlobalDataMap(Map<String, String> globalDataMap) {
		this.globalDataMap = globalDataMap;
	}
	
	public Map<String, String> getGlobalDataMap() {
		return this.globalDataMap;
	}


	@Override
	public boolean execute() {
		if (ap.execute(item, provideInstnace)) {
			return true;
		} else {
			if (item.getValue() != null && item.getValue().equals("null")) {
				item.setValue(null);
				return true;
			} else if (item.getValue() != null && !item.getValue().equals("") && item.getValue().startsWith("pass:")) {
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
					} else if (item.getFieldName().equals("IsRenewal") && item.getParent().getTableName().equals("t_policy")) {
						BContractNode bn = (BContractNode) provideInstnace;
						if (bn.getSourceContractNo() != null && !bn.getSourceContractNo().equals("")) {
							item.setValue("true");
						} else {
							item.setValue("false");
						}
						return true;
					}
					// else if (item.getFieldName().equals("ItemGroup") && item.getParent().getTableName().equals("t_liability")) {
					// BConditionNonLife bnl = (BConditionNonLife) provideInstnace;
					// BDeclaration bd = bnl.getDeclarationByName("category", false);
					// item.setValue("NUL");
					// if (bd != null && bd.getValue() != null) {
					// Object oo = bd.getValue();
					// if (!oo.toString().equals("")) {
					// item.setValue(oo.toString());
					// }
					// }
					// return true;
					// }
					else {

						String methodNm = item.getMethod();
						if (methodNm == null || methodNm.equals("")) {
							System.out.println(item.getParent().getTableName() + ":" + item.getFieldName() + "get method name is null!");
						} else {
							String temp[] = methodNm.split("[.]");// 用"."将这个方法的字符串分割
							for (String string : temp) {
								Class<?>[] parameters = new Class[3];// 1代表要反射的参数的个数
								parameters[0] = String.class;// 将parameters作为参数传到反射类的方法中
								parameters[1] = String.class;
								parameters[2] = boolean.class;
								
								Class<?>[] codeparameters = new Class[2];// 1代表要反射的参数的个数
								codeparameters[0] = String.class;// 将parameters作为参数传到反射类的方法中
								codeparameters[1] = String.class;
								
								Class<?>[] roleparameters = new Class[2];// 1代表要反射的参数的个数
								roleparameters[0] = String.class;// 将parameters作为参数传到反射类的方法中
								roleparameters[1] = boolean.class;
								if (obj != null) {
									if (string.equals("getElementByName")) {
										obj = ReflectUtil.getObjByReflectByName(obj, string, parameters, item.getParameter());
									} else if (string.equals("getElementByCode") ) {	//zhangfan 20140316
										obj = ReflectUtil.getObjByReflectByCodeDeclSS(obj, string, codeparameters, item.getParameter());
									} else if (string.equals("getRoleByCode") ) {
										obj = ReflectUtil.getObjByReflectByCodeRoleSB(obj, string, roleparameters, item.getParameter());
									} else if (string.equals("getDescription")) {
										IElement ie = (IElement) obj;
										obj = ie.getDescription("2");
									} else if (string.equals("getDeclarationByCodeFromList")) {
										@SuppressWarnings("unchecked")
										List<BDeclaration> dl = (List<BDeclaration>) obj;
										obj = null;
										for (BDeclaration bDeclaration : dl) {
											if (bDeclaration.getCode().equals(item.getParameter())) {
												obj = bDeclaration;
												break;
											}
										}
										
									} else if (string.equals("get0")) {	//zhangfan 20140610
										@SuppressWarnings("unchecked")
										List<Object> ol = (List<Object>) obj;
										if (ol != null && ol.size() > 0) {
											obj = ol.get(0);
										} else {
											obj = null;
										}
									} else if (string.equals("getValueDescription")) {
										BDeclaration ie = (BDeclaration) obj;
										obj = ie.getValueDescription("2");
									} else if (string.equals("getPartyDeclarationByCode")) {
										PartyVO p = (PartyVO) obj;
										obj = p.getPartyDeclarationByCode(item.getParameter());
									} else {
										obj = ReflectUtil.getObjByReflect(obj, string);
									}
								}
								
								if( null != item.getIfNull() && !"".equals(item.getIfNull())
										&& (null == obj || "".equals(obj.toString())) ) {
									obj = item.getIfNull();
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
					StgRunLog stgLog = new StgRunLog();
					stgLog.setErrorCode("88");
					stgLog.setErrorMessage("从元素Code:" + ((PElement)obj).getCode() + " Type:" + ((PElement)obj).getElementType() + "上, 获取方法:" + item.getMethod() + " 参数:" + item.getParameter() + ", 放入" + item.getParent().getTableName() + "." + item.getFieldName() + "失败");
					stgLog.setObjectId(globalDataMap.get("object_Id"));
					stgLog.setObjectNo(globalDataMap.get("object_no"));
					RunLogDAO rld = new RunLogDAO();
					try {
						rld.insertLog(stgLog);
					} catch (SQLException e2) {
						System.out.println("log error:" + e2);
					} catch (NamingException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
					//System.out.println("Object:" + this.globalDataMap.get("object_no") + " Mapping error, TableName: " + item.getParent().getTableName() + " Column:" + item.getFieldName());
					e.printStackTrace();
				} finally {
				}
				return rs;
			}
		}
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

}
