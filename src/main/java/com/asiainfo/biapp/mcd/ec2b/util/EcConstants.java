package com.asiainfo.biapp.mcd.ec2b.util;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月23日  上午11:29:16
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class EcConstants {

	/**
	 * ec流程执行阶段定义
	 */
	/** mcd_camp_task_date表生成调度任务和初步过滤后的客户群数据表 */
	public static int EC_STAGE_SCHEDULE = 0;
	/** mcd_ec_schedule_task根据配额拆分发送任务 */
	public static int EC_STAGE_SPLIT = 1;
	/** mcd_ec_send_task发送短信 */
	public static int EC_STAGE_SEND = 2;
	/** 轮询发送结果 */
	public static int EC_STAGE_QUERY = 3;
	
	/**
	 * mcd_camp_task_date表任务状态定义
	 */
	/** 就绪 */
	public static int EC_TASK_STATUS_READY = 50;
	/** 调度中，正在处理的任务 */
	public static int EC_TASK_STATUS_SCHEDULING = 51;
	/** 周期发送完成 */
	public static int EC_TASK_STATUS_CYCLEFINISH = 54;
	/** 完成调度，发送中 */
	public static int EC_TASK_STATUS_RUNNING = 70;
	/** 一次性任务，直接完成 */
	public static int EC_TASK_STATUS_FINISH = 71;
	
	/**
	 * mcd_ec_schedule_task表调度任务状态定义
	 */
	/** 就绪 */
	public static int EC_SCHEDULE_STATUS_READY = 0;
	/** 调度中，正在进行任务拆分 */
	public static int EC_SCHEDULE_STATUS_SCHEDULING = 1;
	/** 调度完成 */
	public static int EC_SCHEDULE_STATUS_FINISH = 2;
	
	/**
	 * mcd_ec_send_task表发送状态定义
	 */
	/** 就绪 */
	public static int EC_SEND_STATUS_READY = 0;
	/** 发送中，正在进行发送处理 */
	public static int EC_SEND_STATUS_SENDING = 1;
	/** 发送调用完成，等待mcd-sms的发送结果 */
	public static int EC_SEND_STATUS_WAITTING = 2;
	/** 发送完成，由轮询线程更新状态 */
	public static int EC_SEND_STATUS_FINISH = 3;
	
	/**
	 * mcd-sms批量短信发送结果状态定义
	 */
	/** 发送中 */
	public static int EC_SEND_RESULT_PROCESSING = 0;
	/** 发送异常 */
	public static int EC_SEND_RESULT_ERROR = 3;
	/** 发送完成 */
	public static int EC_SEND_RESULT_COMPLETE = 4;
	
	/**
	 * 活动状态定义
	 */
	public static int EC_CAMPSEG_STATUS_FINISH = 90;
	public static int EC_CAMPSEG_STATUS_TERMINATE = 91;
	
	/**
	 * ec短信配额类型
	 */
	/** 批量短信配额 */
	public static int EC_QUOTA_TYPE_BATCH = 1;
	/** 实时短信配额 */
	public static int EC_QUOTA_TYPE_REALTIME = 2;
	
	/**
	 * ec批量短信发送策略
	 */
	/** 顺序发送：拆分任务时，直到一个活动拆分完成才会进行下个活动的任务拆分 */
	public static int EC_SEND_POLICY_ORDER = 1;
	/** 等量发送：拆分任务时，每个活动拆分一个发送任务，轮流拆分 */
	public static int EC_SEND_POLICY_EQUAL = 2;
	
	/**
	 * 客户群周期编码
	 */
	/** 一次性 */
	public static int EC_CUSTGROUP_CYCLE_ONCE = 1;
	/** 月周期 */
	public static int EC_CUSTGROUP_CYCLE_DAY = 2;
	/** 日周期 */
	public static int EC_CUSTGROUP_CYCLE_MONTH = 3;
	
	/** 数据表名前缀 */
	public static String EC_DATA_TABLE_PREFIX = "mcd_";
	/** 数据表模板 */
	public static String EC_DATA_TABLE_TEMPLATE = "mcd_ec_data_template";
	
	/**
	 * 分布式调度锁名称定义
	 */
	public static String EC_LOCKNAME_SCHEDULE = "ec_lock_schedule";
	public static String EC_LOCKNAME_SPLIT = "ec_lock_slit";
	public static String EC_LOCKNAME_SEND = "ec_lock_send";
	public static String EC_LOCKNAME_QUERY = "ec_lock_query";
	
	/**
	 * 缓存key定义
	 */
	public static String EC_CACHE_KEY_HOLIDAY = "ec_cache_key_holiday";
	public static String EC_CACHE_KEY_BOTHERAVOID_PERIOD = "ec_cache_key_botheravoid_period";
	public static String EC_CACHE_KEY_CAMPSEG_PREFIX = "ec_cache_key_campseg_";
}
