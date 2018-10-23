package com.sysnet.poc.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

public class OdsLogger {

	public static Log getLog(String sourceName, String name) {

		PropertyConfigurator.configure(ResourceBundleUtil.readValue(sourceName));

		Log log = LogFactory.getLog(name);

		return log;
	}
}
