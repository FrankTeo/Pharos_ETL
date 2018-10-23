package com.sysnet.poc.control.manage.vo;

import com.sysnet.poc.util.ResourceBundleUtil;

/**
 * 控制线程暂停与开始
 * 
 * @author luhaibin
 * 
 */
public abstract class ThreadContral extends Thread {
	protected ShowInfo si = new ShowInfo();
	protected boolean suspendFlag = true;

	@Override
	public void run() {
		schedule();
	}

	protected abstract void schedule();

	public ShowInfo getSi() {
		return si;
	}

	public void setSi(ShowInfo si) {
		this.si = si;
	}

	public boolean isSuspendFlag() {
		return suspendFlag;
	}

	public void setSuspendFlag(boolean suspendFlag) {
		this.suspendFlag = suspendFlag;
	}

	public void sheduleSuspend(boolean b) {
		if (b) {
			if (Boolean.parseBoolean(ResourceBundleUtil.readValue("autoRunFlag"))) {
				return;
			}
		}
		suspendFlag = true;
		realSuspend();
	}

	/**
	 * 通知子线程，关闭
	 */
	protected void realSuspend() {
	}

	public void sheduleContinue(boolean b) {
		if (b) {
			if (Boolean.parseBoolean(ResourceBundleUtil.readValue("autoRunFlag"))) {
				return;
			}
		}
		sheduleResume();
	}

	protected synchronized void sheduleResume() {
		suspendFlag = false;
		this.notifyAll();
		si.setShowMassageContinue();
		si.setExceptionMessage("");
	}

}
