package com.sysnet.poc.claimmapping.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import pharos.framework.base.Money;

import com.sysnet.poc.claimmapping.vo.ActionPlus;
import com.sysnet.poc.partymapping.ReflectUtil;
import com.sysnet.poc.util.DateHelper;

/**
 * 对象类型处理类
 * 
 * @author DBC
 * 
 */
public class ObjectAction extends AbsClaimAction {
	private ActionPlus ap;

	public ActionPlus getAp() {
		return ap;
	}

	public void setAp(ActionPlus ap) {
		this.ap = ap;
	}

	@Override
	public boolean execute() throws Exception {
		// if (item.getFieldName().equalsIgnoreCase("ExpenseType")) {
		// System.out.println();
		// }
		if (ap.execute(item, provideInstance)) {
			return true;
		} else {
			if (item.getValue() != null && !item.getValue().equals("") && item.getValue().startsWith("pass:")) {
				String value = item.getValue();
				item.setValue(value.substring(5));
				return true;
			}
			// else if ("LossType".equals(item.getFieldName())) {
			// item.setValue("NUL");
			// if (provideInstance instanceof Procedure) {
			// Procedure p = (Procedure) provideInstance;
			// BDeclaration bd = p.getDeclarationByName("lossCategory");
			// if (bd != null && bd.getValue() != null) {
			// Object oo = bd.getValue();
			// if (!oo.toString().equals("")) {
			// item.setValue(oo.toString());
			// }
			// }
			//
			// } else if (provideInstance instanceof ConditionPayment) {
			// ConditionPayment cp = (ConditionPayment) provideInstance;
			//
			// BDeclaration bd = (BDeclaration) cp.getElementByName(ElementEnum.BDECLARATION_CODE, "lossCategory", false);
			// if (bd != null && bd.getValue() != null) {
			// Object oo = bd.getValue();
			// if (!oo.toString().equals("")) {
			// item.setValue(oo.toString());
			// }
			// }
			// } else {
			// System.out.println("no type for LossType");
			// }
			// return true;
			//
			// }
			else {
				try {
					boolean rs = false;
					Object obj = provideInstance;
					// 按ID获取不同的方法配制模板
					String methodNm = item.getMethod();
					if (item.getFieldName().equals("CrtTime")) {
						SimpleDateFormat matter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
						item.setValue(matter.format(new Date()));
					} else {
						if (methodNm == null || methodNm.equals("")) {
							System.out.println(item.getParent().getTableName() + ":" + item.getFieldName() + " get method name is null!");
						} else {
							String temp[] = methodNm.split("[.]");// 用"."将这个方法的字符串分割
							for (String string : temp) {
								Class<?>[] parameters = new Class[3];// 1代表要反射的参数的个数
								parameters[0] = String.class;// 将parameters作为参数传到反射类的方法中
								parameters[1] = String.class;
								parameters[2] = boolean.class;
								
								Class<?>[] parameters2 = new Class[2];// 1代表要反射的参数的个数
								parameters2[0] = String.class;// 将parameters作为参数传到反射类的方法中
								parameters2[1] = String.class;
								if (string.equals("getElementByName")) {
									obj = ReflectUtil.getObjByReflectByName(obj, string, parameters, item.getParameter());
								} else if( string.equals("getElementByCode") ) {	// add by Frank Zhang at 20140616
									obj = ReflectUtil.getObjByReflectByCodeDeclSS(obj, string, parameters2, item.getParameter());
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
								if( !"".equals(item.getDefaultValue())) {
									if("".equals(String.valueOf(obj))) {
										item.setValue(item.getDefaultValue());
									} else {
										item.setValue(String.valueOf(obj));
									}
								} else {
									item.setValue(String.valueOf(obj));
								}
							}
						}
					}
					rs = true;
					return rs;
				} catch (Exception e) {
					System.out.println(item.getFieldName());
					System.out.println(item.getParent().getTableName());
					// e.printStackTrace();
					throw e;
				} finally {

				}

			}
		}
	}
}