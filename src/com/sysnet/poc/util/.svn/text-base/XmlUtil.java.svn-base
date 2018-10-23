package com.sysnet.poc.util;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * XML文件读取类
 * 
 * @author make
 * @since 2009-07-06
 */
public class XmlUtil {

	private static final Log log = OdsLogger.getLog("insureLog4j", "Insure");

	public static Document getDocument() {
		SAXBuilder build = new SAXBuilder();
		java.io.InputStream in = (XmlUtil.class).getResourceAsStream("/product_model.xml");
		Document document = null;
		try {
			document = build.build(in);
		} catch (JDOMException e) {

			log.error("读取XML文件解析出现异常:" + e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {

			log.error("读取配置文件出现异常:" + e.getMessage(), e);
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return document;
	}

	public static Document getDocument(String filename) {
		log.debug("处理配置文件：" + filename);
		SAXBuilder build = new SAXBuilder();
		java.io.InputStream in = (XmlUtil.class).getResourceAsStream(filename);
		Document document = null;
		if(in == null){
			return null;
		}
		try {
			document = build.build(in);
		} catch (JDOMException e) {
			log.error("读取XML文件解析出现异常:" + filename, e);
		} catch (IOException e) {
			log.error("读取配置文件出现异常:" + filename, e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return document;
	}

	// public static void main(String args[]) {
	// MappingHelper mappingHelper = new MappingHelper();
	// mappingHelper.XmlToObjectByIncludePattern("/simple/product_model_301_30001_1.xml");
	// Document document = getDocument("/product_model_301_30001_1.xml");
	// String str = null;
	// }

	/**
	 * 输出漂亮的XML
	 * 
	 * @param Document
	 * @return GBK 编码的XML
	 */
	public synchronized static String convertPrettyFormat(Document idoc) {
		String retStr = "";
		Format formatPrety = Format.getPrettyFormat();
		formatPrety.setEncoding("gbk");
		XMLOutputter output = new XMLOutputter();
		output.setFormat(formatPrety);
		if (idoc != null) {
			return output.outputString(idoc);
		}
		return retStr;
	}

}
