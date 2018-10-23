package com.sysnet.poc.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 读取资源文件
 * 
 * @author SuiZhiwei
 * 
 */
public class ResourceBundleUtil {

	private static ResourceBundle bundle = null;

	static {
		bundle = ResourceBundle.getBundle("com/sysnet/poc/resources/poc", Locale.CHINESE);
	}

	/**
	 * 返回ejb服务地址
	 * 
	 * @return
	 */
	public static String getServiceURL() {
		return readValue("ods.ejbURL");
	}

	/**
	 * 返回指定ke的value
	 * 
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		return bundle.getString(key);
	}

	/**
	 * 取出保险公司ID
	 * 
	 * @return
	 */
	public static String getCompanyId() {
		return bundle.getString("companyId");
	}

	/**
	 * 取出保险公司CD
	 * 
	 * @return
	 */
	public static String getCompanyCd() {
		return bundle.getString("companyCd");
	}

	private static String getPathFromClass(Class<?> cls) throws IOException {
		String path = null;
		if (cls == null) {
			throw new NullPointerException();
		}
		URL url = getClassLocationURL(cls);
		if (url != null) {
			path = url.getPath();
			if ("jar".equalsIgnoreCase(url.getProtocol())) {
				try {
					path = new URL(path).getPath();
				} catch (MalformedURLException e) {
				}
				int location = path.indexOf("!/");
				if (location != -1) {
					path = path.substring(0, location);
				}
			}
			File file = new File(path);
			path = file.getCanonicalPath();
		}
		return path;
	}

	private static URL getClassLocationURL(final Class<?> cls) {
		if (cls == null)
			throw new IllegalArgumentException("null input: cls");
		URL result = null;
		final String clsAsResource = cls.getName().replace('.', '/').concat(".class");
		final ProtectionDomain pd = cls.getProtectionDomain();
		// java.lang.Class contract does not specify
		// if 'pd' can ever be null;
		// it is not the case for Sun's implementations,
		// but guard against null
		// just in case:
		if (pd != null) {
			final CodeSource cs = pd.getCodeSource();
			// 'cs' can be null depending on
			// the classloader behavior:
			if (cs != null)
				result = cs.getLocation();

			if (result != null) {
				// Convert a code source location into
				// a full class file location
				// for some common cases:
				if ("file".equals(result.getProtocol())) {
					try {
						if (result.toExternalForm().endsWith(".jar") || result.toExternalForm().endsWith(".zip"))
							result = new URL("jar:".concat(result.toExternalForm()).concat("!/").concat(clsAsResource));
						else if (new File(result.getFile()).isDirectory())
							result = new URL(result, clsAsResource);
					} catch (MalformedURLException ignore) {
					}
				}
			}
		}

		if (result == null) {
			final ClassLoader clsLoader = cls.getClassLoader();
			result = clsLoader != null ? clsLoader.getResource(clsAsResource) : ClassLoader.getSystemResource(clsAsResource);
		}
		return result;
	}

	public static String readValue(String key) {
		String filePath = "";
		try {
			String temp = getPathFromClass(ResourceBundleUtil.class);
			filePath = temp.substring(0, temp.lastIndexOf(File.separator) + 1) + "poc.properties";
		} catch (IOException e) {
			System.err.println("filePath=" + filePath);
			e.printStackTrace();
		}

		Properties props = new Properties();
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(filePath));
			props.load(in);
			if ("riskNodeThreads".equalsIgnoreCase(key)) {
				String temp = props.getProperty("logfile");
				Boolean b = Boolean.parseBoolean(temp);
				if (!b) {
					return "";
				}
			}
			String value = props.getProperty(key);
			return value;
		} catch (Exception e) {
			System.err.println("filePath=" + filePath);
			e.printStackTrace();
			return null;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
