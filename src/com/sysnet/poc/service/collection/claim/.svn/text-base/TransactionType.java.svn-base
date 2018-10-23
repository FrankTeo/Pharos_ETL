package com.sysnet.poc.service.collection.claim;

import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

import com.sysnet.poc.util.XmlUtil;

public class TransactionType {
	private Document doc;

	public TransactionType(String id) {
		doc = XmlUtil.getDocument("/mappingforclaim/p" + id + "/claimMaping.xml");
	}

	/**
	 * 按公司ID取不同的值
	 * 
	 * @param code
	 * @param id
	 * @return
	 */
	public String[] getType(String code) {
		String[] types = new String[0];
		if (doc == null) {
			System.out.println("空文件！");
			return types;
		}
		Element el = doc.getRootElement();
		if (el == null) {
			System.out.println("没有找到根节点！");
			return types;
		}
		List<Element> tl = el.getChildren();

		if (tl.size() <= 0) {
			System.out.println("没有找到子节点！");
			return types;
		}
		for (Element e : tl) {
			List<Element> ttel = e.getChildren();
			for (Element e2 : ttel) {
				Attribute a = e2.getAttribute("code");
				String codeValue = a.getValue();
				if (codeValue.equals(code)) {
					List list = e2.getChildren();
					types = new String[list.size()];
					for (int i = 0; i < list.size(); i++) {
						Element e3 = (Element) list.get(i);
						types[i] = e3.getValue();
					}
					break;
				}
			}

		}

		return types;
	}

}
