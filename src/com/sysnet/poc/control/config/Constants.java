package com.sysnet.poc.control.config;

/**
 * 常量类
 * 
 * @author Administrator
 * 
 */
public class Constants {

	// 定义Mony对象返回那类数据标志
	public static final String MONY_FLAG_A = "A";// Mony对象返回Amount
	public static final String MONY_FLAG_C = "C";// Mony对象返回Currency

	// 定义保单数据对象标志
	// public static final String STG_PLC_POLICY = "STG_PLC_POLICY";
	// public static final String STG_PLC_CARDECLARATION = "STG_PLC_CARDECLARATION";
	// public static final String STG_PLC_CONDITION = "STG_PLC_CONDITION";
	// public static final String STG_PLC_ROLE_INFO = "STG_PLC_ROLE_INFO";
	// public static final String STG_PLC_DCL_ROLE_INFO = "STG_PLC_DCL_ROLE_INFO";
	//
	// // 定义投保单数据对象标志
	// public static final String STG_PRP_PROPOSAL = "STG_PRP_PROPOSAL";
	// public static final String STG_PRP_CAR_DECLARATION = "STG_PRP_CAR_DECLARATION";
	// public static final String STG_PRP_CONDITION = "STG_PRP_CONDITION";
	// public static final String STG_PRP_ROLE_INFO = "STG_PRP_ROLE_INFO";
	//
	// // 定义批单数据对象标志
	// public static final String STG_EDR_ENDORSE = "STG_EDR_ENDORSE";
	// public static final String STG_EDR_CAR_DECLARATION = "STG_EDR_CAR_DECLARATION";
	// public static final String STG_EDR_CONDITION = "STG_EDR_CONDITION";
	// public static final String STG_EDR_ROLE_INFO = "STG_EDR_ROLE_INFO";

	// 定义增量登场区回写标志
	public static final String INCREMENT_WRITE_BACK_STATUS_1 = "1";// 已处理完
	public static final String INCREMENT_WRITE_BACK_STATUS_0 = "0";// 未处理
	public static final String INCREMENT_WRITE_BACK_STATUS_2 = "2";// 出错的增量
	public static final String INCREMENT_WRITE_BACK_STATUS_3 = "3";// 收付费以及理赔过滤不处理的数据update
	// by
	// luhaibin

	// 定义批次表登场区处理状态标志
	public static final String STAGE_DATE_READY_STATUS_0 = "0";// 登场区批次未完成
	public static final String STAGE_DATE_READY_STATUS_1 = "1";// 登场区批次已完成

	// 定义登场区批次处理状态
	public static final int STAGE_CURRENT_STATUS_0 = 0;// 正常处理
	public static final int STAGE_CURRENT_STATUS_1 = 1;// 异常处理

	// 定义登场区线程是否停止标志
	public static final int STAGE_THREAD_STOP_FLAG_0 = 0;// 线程未结束
	public static final int STAGE_THREAD_STOP_FLAG_1 = 1;// 线程结束
	// // 定义角色类型
	// public static final String ROLE_TYPE_DRIVER = "7";
	//
	// // 定义客户数据对象标志
	// public static final String STG_CST_CUST = "STG_CST_CUST";
	//
	// // 定义承保缴费计划数据对象标志
	// public static final String STG_PLC_PAY_PLAN = "STG_PLC_PAY_PLAN";
	// public static final String STG_EDR_PAY_PLAN = "STG_EDR_PAY_PLAN";
	// public static final String STG_PRP_PAY_PLAN = "STG_PRP_PAY_PLAN";

	// 定义增量表名
	public static final String ETL_PHAROS_INCR_CLAIM = "etl_pharos_incr_claim_ct";
	public static final String ETL_PHAROS_INCR_ACC = "etl_pharos_incr_acc";
	public static final String ETL_PHAROS_INCR_INSURE = "etl_pharos_incr_insure";
	public static final String ETL_PHAROS_INCR_PARTY = "etl_pharos_incr_party";
	//public static final String ETL_PHAROS_INCR_PARTY = "etl_pharos_incr_party_ct";
	public static final String ETL_PHAROS_INCR_WORKFLOW = "etl_pharos_incr_workflow";// 流程增量表

	// 定义CONFIG&STATUS
	public static final String ETL_PHAROS_INCR_CLAIMC = "etl_stage_etl_clm_config";
	public static final String ETL_PHAROS_INCR_ACCC = "ETL_STAGE_ETL_ACC_CONFIG";
	public static final String ETL_PHAROS_INCR_INSUREC = "ETL_STAGE_ETL_CONFIG";
	public static final String ETL_PHAROS_INCR_PARTYC = "ETL_STAGE_ETL_PARTY_CONFIG";
	public static final String ETL_PHAROS_INCR_CLAIMS = "etl_stage_etl_clm_status";
	public static final String ETL_PHAROS_INCR_ACCS = "ETL_STAGE_ETL_ACC_STATUS";
	public static final String ETL_PHAROS_INCR_INSURES = "ETL_STAGE_ETL_STATUS";
	public static final String ETL_PHAROS_INCR_PARTYS = "ETL_STAGE_ETL_PARTY_STATUS";

}
