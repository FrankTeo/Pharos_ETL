/**
 * FileName: PocException.java Date: Jul 22, 2009
 *
 * Copyright (c) 1994-2009 北京尚洋信德信息技术股份有限公司 版权所有
 *
 * History:
 *        <p>Jul 22, 2009:此处记录修改历史记录
 *
 */
package com.sysnet.poc.exception;

/**
 * POC异常处理基类
 * 
 * @author <a href=huang_y@sysnet.com.cn>huang_y</a>
 * @version 1.0
 */
public class PocException extends Exception {

	private static final long serialVersionUID = 1L;

	private int code = 990000; // 错误代码。超类默认为990000
	private String errorMessage = "POC出现未知错误!"; // 错误信息描述

	public PocException() {

	}

	public PocException(int _code, String _msg) {
		code = _code;
		errorMessage = _msg;
	}

	public PocException(String _msg) {
		errorMessage = _msg;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return errorMessage;
	}
}
