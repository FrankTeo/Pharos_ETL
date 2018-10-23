package com.sysnet.poc.util;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			RemoveDataUtil.deleteOldData();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
