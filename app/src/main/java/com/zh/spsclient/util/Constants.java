package com.zh.spsclient.util;

import com.amap.api.maps.model.LatLng;

public class Constants {

	public static final int POISEARCH = 1000;

	public static final int ERROR = 1001;
	public static final int FIRST_LOCATION = 1002;

	public static final int ROUTE_START_SEARCH = 2000;
	public static final int ROUTE_END_SEARCH = 2001;
	public static final int ROUTE_SEARCH_RESULT = 2002;
	public static final int ROUTE_SEARCH_ERROR = 2004;

	public static final int REOCODER_RESULT = 3000;
	public static final int DIALOG_LAYER = 4000;
	public static final int POISEARCH_NEXT = 5000;

	public static final int BUSLINE_RESULT = 6000;
	public static final int BUSLINE_DETAIL_RESULT = 6001;
	public static final int BUSLINE_ERROR_RESULT = 6002;

	public static final LatLng BEIJING = new LatLng(39.90403, 116.407525);
	public static final LatLng ZHONGGUANCUN = new LatLng(39.983456, 116.3154950);
	public static final LatLng SHANGHAI = new LatLng(31.239879, 121.499674);
	public static final LatLng FANGHENG = new LatLng(39.989614, 116.481763);
	public static final LatLng CHENGDU = new LatLng(30.679879, 104.064855);
	public static final LatLng XIAN = new LatLng(34.341568, 108.940174);
	public static final LatLng ZHENGZHOU = new LatLng(34.7466, 113.625367);
	public static final LatLng WUSHENQI = new LatLng(38.61513, 108.834615);
	public static final LatLng ZHI_XIN_XI_LU = new LatLng(39.9880,116.3656);
	
	public static final String PLEASE_INPUT_PASSWORD = "请输入密码";
	public static final String PLEASE_INPUT_USER_NAME = "请输入用户名";
	//public static final String LOGIN_VIEW_AUTHORIZATION_FALLOR = "��֤ʧ�ܣ�����ϵ����Ա!";
	public static final String LOGIN_VIEW_PLEASE_WAITING = "请等待...";
	public static final String LOGIN_VIEW_REQUESTING_SERVER = "正在请求服务器验证...";
	public static final String LOGIN_VIEW_INFO = "提示";
	public static final String LOGIN_VIEW_CONFIRM = "确定";
	
	public static final String MAIN_VIEW_CHECKING_TASK = "������Ѳ������...";
	public static final String MAIN_VIEW_START_DOWNLOAD = "开始下载";
	public static final String MAIN_VIEW_FRIST_PAGE = "首页面";
	public static final String MAIN_VIEW_COMMUNICATION_RECORD_PAGE = "管线位置";
	public static final String MAIN_VIEW_SETTING_PAGE = "通讯录";
	public static final String MAIN_VIEW_MY_INFO_PAGE = "我的信息";
	
	public static final String TASK_LIST_VIEW_PLEASE_WAITING = "请等待...";
	public static final String TASK_LIST_VIEW_DOWNLOADING_TASK = "正在获取任务列表...";
	public static final String TASK_LIST_VIEW_STARTING_MAP = "正在进入地图界面...";
	public static final String TASK_LIST_START_CHECKING = "��ʼѲ��";
	public static final String TASK_LIST_RUNNING_CHECKING = "����Ѳ��";
	public static final String TASK_LIST_RESTART_CHECKING = "���¿�ʼ";
	public static final String TASK_RESULT_VIEW_UPLOAD_FEEDBACK = "正在上传任务反馈结果";
	
	public static final String TERMINAL_ID = "terminalid";
	public static final String TASK_ID = "taskID";
	public static final String TASK_STATE = "taskState";
	public static final String WELCOME_VIEW_SIM_NUMBER = "18510630626";
	public static final String CURRENT_LOCATION_IN_BEIJING = "־����·";
}
