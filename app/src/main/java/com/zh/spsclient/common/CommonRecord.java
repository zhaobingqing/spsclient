package com.zh.spsclient.common;

public class CommonRecord {
	public static final String dbName = "spsDB";
	public static final String PIPELINE_COORDINATE_TABLE_NAME = "plCoornidate"; 
	public static final String PIPELINE_TABLE_NAME = "Pipeline";
	public static final String TASK_LIST_TABLE_NAME = "Tasklist";
	public static final String USER_LOGIN_TABLE_NAME = "Userlogin";
	public static final String REAL_COORDINATE_TABLE_NAME = "realCoordinate";
	public static final String TASK_RESULT_TABLE_NAME = "TaskResult";
	//public static SQLiteDatabase database = null;
	/*
	 * 理论坐标表
	 */
	public static final String sqlCreateTable_Coordinate = "CREATE TABLE IF NOT EXISTS  " + PIPELINE_COORDINATE_TABLE_NAME     
			 + "(coorNumber NVARCHAR(36), "
			 + "pipeID NVARCHAR(36), "
			 + "locLng NVARCHAR(20), "			 
			 + "locLat NVARCHAR(20), "
			 + "height REAL, "
			 + "depth REAL, "
			 + "interval REAL, "
			 + "terrain NVARCHAR(20)) ";
	public static String[] FIELD_COORDINATE = new String[]{
		"coorNumber",
		"pipeID",
		"locLng",
		"locLat",
		"height",
		"depth",
		"interval",
		"terrain"};
	/*
	 * 坐标轨迹表
	 */
	public static final String sqlCreateTable_Real_Coordinate = "CREATE TABLE IF NOT EXISTS  " + REAL_COORDINATE_TABLE_NAME     
			 + "(" 
			 + "coorID INTEGER, "
			 + "taskID NVARCHAR(36), "
			 + "terminalID NVARCHAR(36), "			 
			 + "locLat NVARCHAR(20), "
			 + "locLng NVARCHAR(20), "
			 + "coorType NVARCHAR(20), "
			 + "curDate NVARCHAR(36),"
			 + "coorUpLoad NVARCHAR(1) "
			 + ")";
	public static String[] FIELD_REAL_COORDINATE = new String[]{
		"coorID",
		"taskID",
		"terminalID",
		"locLat",
		"locLng",
		"coorType",
		"curDate",
		"coorUpLoad"
		};
	public static final String REAL_COORDINATE_COLUMN_LIST = "(" 
			+ "coorID," 
			+ "taskID,"
			+ "terminalID,"
			+ "locLat,"
			+ "locLng,"
			+ "coorType,"
			+ "curDate,"
			+ "coorUpLoad"
			+ ")";
	/*
	 * 管线表
	 */
	public static final String sqlCreateTable_pipeline = 
			"CREATE TABLE IF NOT EXISTS  " + PIPELINE_TABLE_NAME     
			 + "(pipeID NVARCHAR(36), "
			 + "pipeCode NVARCHAR(30), "
			 + "pipeName VARCHAR(50), "			 
			 + "pipeStandard NVARCHAR(50), "
			 + "pipeLength REAL, "
			 + "pipeStart VARCHAR(100), "
			 + "pipeEnd VARCHAR(100), "
			 + "pipeFlow REAL, "
			 + "pipeDate VARCHAR(50), "
			 + "pipeRoom NVARCHAR(20), "
			 + "pipeStatus INTEGER, "
			 + "pipePress REAL, "
			 + "coorType NVARCHAR(10), "
			 + "coorVersion NVARCHAR(10)) ";
	public static String[] FIELD_PIPELINE = new String[]{
		"pipeID",
		"pipeCode",
		"pipeName",
		"pipeStandard",
		"pipeLength",
		"pipeStart",
		"pipeEnd",
		"pipeFlow",
		"pipeDate",
		"pipeFlow",
		"pipeRoom",
		"pipeStatus",
		"coorType",
		"coorVersion"};
	// 设备编号
	public static String authCode = "945EB9CC6B53416C95CD90D9A5489CE9";
	// 用户账号
	public static String _currentUser = "";
	/*
	 * 任务单表
	 */
	public static final String sqlCreateTable_tasklist = 
			"CREATE TABLE IF NOT EXISTS  " + TASK_LIST_TABLE_NAME     
			 + "("
			 + "taskID NVARCHAR(36), "
			 + "pipeID NVARCHAR(36), "
			 + "planID NVARCHAR(36), "			 
			 + "terminalID NVARCHAR(36), "
			 + "userID CHAR(10), "
			 + "deptID INTEGER, "
			 + "taskName NVARCHAR(100), "
			 + "taskDesc NVARCHAR(200),"
			 + "planStartDate NVARCHAR(50),"
			 + "planEndDate NVARCHAR(50),"			 
			 + "taskStartPos NVARCHAR(20)," 
			 + "taskEndPos NVARCHAR(20)," 
			 + "taskStatus VARCHAR(10)," 
			 + "taskType NVARCHAR(36),"
			 + "taskStartDate NVARCHAR(50)," 
			 + "taskEndDate NVARCHAR(50)" 
			 + ") ";
	public static String[] FIELD_TASKLIST = new String[]{
		"taskID",
		"pipeID",
		"planID",
		"terminalID",
		"userID",
		"deptID",
		"taskName",
		"taskDesc",
		"taskStartDate",
		"taskEndDate",
		"taskStartPos",
		"taskEndPos",
		"taskStatus",
		"taskType",
		"taskStartDate",
		"taskEndDate"};
	public static final String TASK_LIST_COLUMN_LIST = "(" 
		+ "taskID," 
		+ "pipeID,"
		+ "planID,"
		+ "terminalID,"
		+ "userID,"
		+ "deptID,"
		+ "taskName,"
		+ "taskDesc,"
		+ "taskStartDate,"
		+ "taskEndDate,"
		+ "taskStartPos,"
		+ "taskEndPos,"
		+ "taskStatus,"
		+ "taskType,"
		+ "taskStartDate,"
		+ "taskEndDate"
		+ ")";
	/*
	 * 用户登录信息表
	 */
	public static final String sqlCreateTable_user_login = "CREATE TABLE IF NOT EXISTS  " + USER_LOGIN_TABLE_NAME     
			 + "("
			 + "userID NVARCHAR(10), "
			 + "password NVARCHAR(56), "
			 + "authCode NVARCHAR(56), "			 
			 + "pkID INTEGER, "	
			 + "bIsLogin INTEGER" // 1 ��¼�� 0���ǳ�
			 + ") ";
	public static String[] FIELD_USER_LOGIN = new String[]{
		"userID",
		"password",
		"authCode",
		"pkID",
		"bIsLogin"};
	public static String USER_LOGIN_COLUMN_LIST = "("
			+ "userID,"
			+ "password,"
			+ "authCode,"
			+ "pkID,"
			+ "bIsLogin"
			+ ")";
	/*
	 * 反馈表
	 * 
	 */
	public static final String sqlCreateTable_task_result= "CREATE TABLE IF NOT EXISTS  " + TASK_RESULT_TABLE_NAME     
			 + "("
			 + "FeedBackID NVARCHAR(36), "
			 + "TaskID NVARCHAR(36), "
			 + "TerminalID NVARCHAR(36), "			 
			 + "FeedBackCOOR NVARCHAR(50), "	
			 + "FeedBackTXT NVARCHAR(500),"  
			 + "FeedBackSound NVARCHAR(20)," 
			 + "COORVideo NVARCHAR(20)," 
			 + "COORPhoto NVARCHAR(20)," 
			 + "FeedBackPerson NVARCHAR(10)," 
			 + "FeedBackDate NVARCHAR(50),"
			 + "FeedBackUpLoad NVARCHAR(1)" 
			 + ") ";
	public static String[] FIELD_TASK_RESULT = new String[]{
		"FeedBackID",
		"TaskID",
		"TerminalID",
		"FeedBackCOOR",
		"FeedBackTXT",
		"FeedBackSound",
		"COORVideo",
		"COORPhoto",
		"FeedBackPerson",
		"FeedBackDate",
		"FeedBackUpLoad"
		};
	public static String TASK_RESULT_COLUMN_LIST = "("
			+ "FeedBackID,"
			+ "TaskID,"
			+ "TerminalID,"
			+ "FeedBackCOOR,"
			+ "FeedBackTXT,"
			+ "FeedBackSound,"
			+ "COORVideo,"
			+ "COORPhoto,"
			+ "FeedBackPerson,"
			+ "FeedBackDate,"
			+ "FeedBackUpLoad"
			+ ")";
	public static String QUERY_COUNT = "select count(1) from ";
	public static String USER_LOGIN_QUERY_COUNT = "select count(1) from Userlogin where userID = ? and password = ? and authCode = ? ";
}
