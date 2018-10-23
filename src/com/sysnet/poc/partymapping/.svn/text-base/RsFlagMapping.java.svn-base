package com.sysnet.poc.partymapping;

import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

import com.sysnet.poc.util.ResourceBundleUtil;
import com.sysnet.poc.util.XmlUtil;

public class RsFlagMapping {

	@SuppressWarnings("unchecked")
	public static int getRsFlag(String rsCodeIncrValue) {

		Document doc = XmlUtil.getDocument("/com/sysnet/poc/partymapping/rsmapping/CorpRelationMapping" + ResourceBundleUtil.getCompanyId() + ".xml");
		Element el = doc.getRootElement();
		List<Element> tl = el.getChildren();

		if (tl.size() <= 0) {
			System.out.println("没有找到字节点！");
		} else {

			for (Element e : tl) {
				List<Element> ttel = e.getChildren();
				for (Element e2 : ttel) {

					Attribute rsCode = e2.getAttribute("rscode");
					String rsCodeValue = rsCode.getValue();

					if (rsCodeValue.equals(rsCodeIncrValue)) {

						Attribute rsFlag = e2.getAttribute("rsflag");
						String rsFlagValue = rsFlag.getValue();
						if (rsFlagValue != null && (!("".equals(rsFlagValue)))) {

							return Integer.parseInt(rsFlagValue);

						}

					}
				}

			}
		}

		return 0;
	}

}
