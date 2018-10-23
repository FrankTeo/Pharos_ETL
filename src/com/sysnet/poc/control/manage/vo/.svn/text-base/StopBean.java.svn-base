package com.sysnet.poc.control.manage.vo;

public class StopBean {

	// 线程是否结束标志 1结束 0未结束
	private int stop = 0;

	// 需要启动的子线程数
	private int threadC = 0;

	private int threadStop = 0;// party专用

	public synchronized int getThreadStop() {
		return threadStop;
	}

	public synchronized void addThreadStop() {
		threadStop = getThreadStop() + 1;
	}

	private String errorMessage = "";
	private Boolean allWriteFlag = true;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Boolean getAllWriteFlag() {
		return allWriteFlag;
	}

	public void setAllWriteFlag(Boolean allWriteFlag) {
		this.allWriteFlag = allWriteFlag;
	}

	public synchronized int getStop() {
		return stop;
	}

	public synchronized void addStop() {
		stop = getStop() + 1;
	}

	public synchronized int getThreadC() {
		return threadC;
	}

	public synchronized void setThreadC(int threadC) {
		this.threadC = threadC;
	}

	// 理陪专用

	private int threadCC = 0;

	public int getThreadCC() {
		return threadCC;
	}

	public void add() {

		synchronized (this) {

			this.threadCC = this.threadCC + 1;
		}

	}

	public synchronized void setThreadCC(int threadCC) {
		this.threadCC = threadCC;
	}

}
