package com.sysnet.poc.control.manage;

import java.util.ArrayList;
import java.util.List;

import com.sysnet.poc.control.manage.vo.ThreadContral;
import com.sysnet.poc.util.DateHelper;
import com.sysnet.poc.util.ResourceBundleUtil;

/**
 * 线程自动控制
 * 
 * @author DBC 2010-10-18
 */
public class AutoContralThread extends Thread {
	// public AutoContralThread(){
	// StopBean s = new StopBean();
	// l.add(new InsureScheduleThread());
	// l.add(new ClaimScheduleThread());
	// l.add(new AccScheduleThreadLHB());
	// l.add(new PartyScheduleThread());
	// l.add(new EtlScheduleThread(s));
	// l.add(new EtlBatScheduleThread(s));
	// }

	private List<ThreadContral> l = new ArrayList<ThreadContral>();

	public void run() {
		try {
			sleep(5000L);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		while (true) {
			if (Boolean.parseBoolean(ResourceBundleUtil.readValue("autoRunFlag"))) {
				if (inTimeArea()) {
					for (ThreadContral tc : l) {
						tc.sheduleContinue(false);
					}
				} else {
					for (ThreadContral tc : l) {
						tc.sheduleSuspend(false);
					}
				}
			}
			try {
				sleep(Long.parseLong(ResourceBundleUtil.readValue("autoRunSleepTime")));
			} catch (NumberFormatException e) {
			} catch (InterruptedException e) {
			}
		}
	}

	public List<ThreadContral> getL() {
		return l;
	}

	public void setL(List<ThreadContral> l) {
		this.l = l;
	}

	private boolean inTimeArea() {
		String arst = ResourceBundleUtil.readValue("autoRunStartTime");
		String aret = ResourceBundleUtil.readValue("autoRunEndTime");
		int startTime = timeSToTimeI(arst);
		int endTime = timeSToTimeI(aret);
		int currentTime = DateHelper.getTimeI();
		if (endTime < startTime){
			endTime = endTime + 12*3600;
			currentTime  = currentTime + 12*3600;
		}
		//System.out.println("currentTime:" + currentTime);
		//System.out.println("startTime:" + startTime);
		//System.out.println("endTime:" + endTime);
		
		if (currentTime > startTime && currentTime < endTime) {
			return true;
		} else {
			return false;
		}
	}

	private int timeSToTimeI(String time) {
		String[] tempS = time.split(":");
		int i = Integer.parseInt(tempS[0]) * 3600 + Integer.parseInt(tempS[1]) * 60 + Integer.parseInt(tempS[2]);
		return i;
	}
}
