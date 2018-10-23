package com.sysnet.poc.control.manage.vo;

/**
 * 
 * @author Administrator
 * 
 */
public class ConfigureBean {

	private String START_TIME;// 开始时间

	private String END_TIME;// 结束时间

	private String DEAL_COUNT;// 处理条数

	private int GENERAL_MAX_THREAD_COUNT;// 正常最大线程数

	private int EXCEPT_MAX_THREAD_COUNT;// 异常最大线程数

	private int GENERAL_SLEEP_MILLIS_COUNT;// 正常休眠毫秒数

	private int EXCEPT_SLEEP_MILLIS_COUNT;// 异常休眠毫秒数
	
	private String DEAL_MODE;	//处理方式 Frank Zhang 20140828

	public String getSTART_TIME() {
		return START_TIME;
	}

	public void setSTART_TIME(String start_time) {
		START_TIME = start_time;
	}

	public String getEND_TIME() {
		return END_TIME;
	}

	public void setEND_TIME(String end_time) {
		END_TIME = end_time;
	}

	public String getDEAL_COUNT() {
		return DEAL_COUNT;
	}

	public void setDEAL_COUNT(String deal_count) {
		DEAL_COUNT = deal_count;
	}

	public int getGENERAL_MAX_THREAD_COUNT() {
		return GENERAL_MAX_THREAD_COUNT;
	}

	public void setGENERAL_MAX_THREAD_COUNT(int general_max_thread_count) {
		GENERAL_MAX_THREAD_COUNT = general_max_thread_count;
	}

	public int getEXCEPT_MAX_THREAD_COUNT() {
		return EXCEPT_MAX_THREAD_COUNT;
	}

	public void setEXCEPT_MAX_THREAD_COUNT(int except_max_thread_count) {
		EXCEPT_MAX_THREAD_COUNT = except_max_thread_count;
	}

	public int getGENERAL_SLEEP_MILLIS_COUNT() {
		return GENERAL_SLEEP_MILLIS_COUNT;
	}

	public void setGENERAL_SLEEP_MILLIS_COUNT(int general_sleep_millis_count) {
		GENERAL_SLEEP_MILLIS_COUNT = general_sleep_millis_count;
	}

	public int getEXCEPT_SLEEP_MILLIS_COUNT() {
		return EXCEPT_SLEEP_MILLIS_COUNT;
	}

	public void setEXCEPT_SLEEP_MILLIS_COUNT(int except_sleep_millis_count) {
		EXCEPT_SLEEP_MILLIS_COUNT = except_sleep_millis_count;
	}

	public String getDEAL_MODE() {
		return DEAL_MODE;
	}

	public void setDEAL_MODE(String dEAL_MODE) {
		DEAL_MODE = dEAL_MODE;
	}
}
