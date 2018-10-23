package com.sysnet.poc.partymapping.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pharos.ctm.vo.BDeclaration;
import pharos.ctm.vo.BDeclarationForm;
import pharos.ctm.vo.BDeclarationFormList;

import com.sysnet.poc.partymapping.ReflectUtil;

/**
 * DFL处理类
 * 
 * @author Administrator
 * 
 */
public class DFLAction extends AbsPharosMappingAction {

	@Override
	public boolean execute() {
		try {
			if (item.getFieldName().equals("CrtTime")) {
				SimpleDateFormat matter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				item.setValue(matter.format(new Date()));
				return true;
			}
			Object obj = provideInstnace;
			String parameter = item.getParameter();
			String method = item.getMethod();
			if (parameter == null || "".equals(parameter)) {
				String[] mtemp = method.split("[.]");
				for (String string : mtemp) {
					obj = ReflectUtil.getObjByReflect(obj, string);
				}
				if (obj == null) {
					item.setValue(null);
				} else {
					item.setValue(obj.toString());
				}
			} else {
				if (method == null || "".equals(method)) {
					BDeclaration d = getBDeclaration((BDeclarationFormList) obj, item.getParameter());
					if (d != null) {
						item.setValue(d.getValue() + "");
					} else {
						item.setValue(null);
					}
				} else {
					String[] temp = method.split("/");
					String[] mtemp = temp[0].split("[.]");
					for (String string : mtemp) {
						if (!"".equals(string)) {
							obj = ReflectUtil.getObjByReflect(obj, string);
						}
					}
					BDeclaration d = getBDeclaration((BDeclarationFormList) obj, item.getParameter());
					if (temp.length == 1) {
						item.setValue(d.getValue() + "");
					} else {
						String[] tempM = temp[1].split("[.]");
						obj = d;
						for (String string : tempM) {
							obj = ReflectUtil.getObjByReflect(obj, string);
						}
						if (obj == null) {
							item.setValue(null);
						} else {
							item.setValue(obj.toString());
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(item.getFieldName());
			System.out.println(item.getParent().getTableName());
			e.printStackTrace();
		}
		return true;
	}

	private BDeclaration getBDeclaration(BDeclarationFormList bdfl, String name) {
		BDeclaration d = null;
		String[] temp = name.split(":");
		String dName = "";
		List<BDeclaration> l = null;
		if (temp.length == 1) {
			l = bdfl.getDeclarations();
			dName = name;
		} else {
			dName = temp[1];
			String[] dfnl = temp[0].split("/");
			BDeclarationForm declarationFormNow = null;
			List<BDeclarationForm> ldf = null;
			for (String string : dfnl) {
				if (ldf == null) {
					ldf = bdfl.getDeclarationForms();
				} else {
					ldf = declarationFormNow.getDeclarationForms();
				}
				for (BDeclarationForm declarationForm : ldf) {
					if (declarationForm.getName().equals(string)) {
						declarationFormNow = declarationForm;
						break;
					}
				}
			}
			l = declarationFormNow.getDeclarations();
		}
		d = getBDeclarationFromList(l, dName);
		return d;
	}

	private BDeclaration getBDeclarationFromList(List<BDeclaration> ld, String name) {
		for (BDeclaration declaration : ld) {
			if (declaration.getName().equals(name)) {
				return declaration;
			}
		}
		return null;
	}
}
