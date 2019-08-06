package com.zh.spsclient.common;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public class WebServiceAdapter {
	private static String NameSpace="http://tempuri.org/";
	/*private static String u="http://192.168.0.99"	*/
	private static String u="http://218.21.217.162:8801";
	private static String webService="/Sys.WcfService/AndroidWebService.asmx";
	private static String MethodName="RealUploadData";
	private static String soapAction=NameSpace+MethodName;
	private static String url=u+webService;
	private static final String TAG = "WebServiceAdapter";
	private String sText = "";
	public String getsText() {
		return sText;
	}
	public void setsText(String sText) {
		this.sText = sText;
	}
	
	public WebServiceAdapter() {
		super();		
	}
	/*
	 * 返回设备ID
	 * SIMNumber: SIM卡号
	 */
	public String Init(String SIMNumber){
		String result="";
    	try{
    		String s_MethodName="Init";
    		String s_soapAction=NameSpace + s_MethodName;
    		SoapObject request=new SoapObject(NameSpace,s_MethodName);
    		request.addProperty("SIMNumber",SIMNumber);
    		SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(
    				SoapEnvelope.VER11);    		
    		envelope.dotNet=true;   		
    		envelope.setOutputSoapObject(request);    		
    		HttpTransportSE ht=new HttpTransportSE(url);    		
    		ht.call(s_soapAction, envelope); 		
    		if(envelope.getResponse()!=null){
    			SoapPrimitive response=(SoapPrimitive)envelope.getResponse();
    			result=response.toString();  			
    		}
    		Log.d(TAG, result);   
    	}catch(Exception e){
    		result=e.getMessage();
    	}finally{    
    		sText = result;		    		  		
    	}    	
    	return result;
	}
	
	/*
	 * 验证用户是否有效
	 * authCode: 验证码
	 * user: 用户名
	 * password: 密码
	 */
	public Boolean AuthorizeUser(String authCode,String user,String password){
		String result="";
		Boolean bResult = false;
    	try{
    		String s_MethodName="AuthorizeUser";
    		String s_soapAction = NameSpace + s_MethodName;
    		SoapObject request=new SoapObject(NameSpace,s_MethodName);  		
    		request.addProperty("authCode",authCode);
    		request.addProperty("user", user);
    		request.addProperty("password", password);    				
    		SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(
    				SoapEnvelope.VER11);
    		envelope.dotNet=true;    		
    		envelope.setOutputSoapObject(request);    		
    		HttpTransportSE ht=new HttpTransportSE(url);   
    		ht.call(s_soapAction, envelope);
    		if(envelope.getResponse()!=null){
    			SoapPrimitive response=(SoapPrimitive)envelope.getResponse();
    			result=response.toString(); 	
    			if(result != "") bResult = Boolean.parseBoolean(result);
    		}    		
    	}catch(Exception e){
    		result=e.getMessage();
    		Log.i(TAG, result);
    	}finally{    
    		sText = result;	
    	}
		return bResult;    	
	}
	/*
	 *从手持端定时向支撑端发送时间戳数据
	 * authCode: 验证码
	 * terminalID:设备ID
	 * loginDate:登陆日期
	 * outDate:退出日期
	 * timeStamp:时间戳
	 */
	public void insertConTimeStamp(String authCode,String terminalID,String loginDate,String outDate,String timeStamp){
		String result="";
    	try{
    		
    		String s_MethodName="InsertConTimeStamp";
    		String s_soapAction=NameSpace+s_MethodName;
    		SoapObject request=new SoapObject(NameSpace,s_MethodName);
    		request.addProperty("authCode",authCode);
    		request.addProperty("terminalID", terminalID); 
    		request.addProperty("loginDate", loginDate);  
    		request.addProperty("outDate", outDate);  
    		request.addProperty("timeStamp", timeStamp);  
    		SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(
    				SoapEnvelope.VER11);    		
    		envelope.dotNet=true;		
    		envelope.setOutputSoapObject(request);    		
    		HttpTransportSE ht=new HttpTransportSE(url);    		
    		ht.call(s_soapAction, envelope);		
    		if(envelope.getResponse()!=null){
    			SoapPrimitive response=(SoapPrimitive)envelope.getResponse();
    			result=response.toString();		
    		}
    	}catch(Exception e){
    		result=e.getMessage();
    	}finally{    
    		sText = result;		
    		Log.d(TAG, sText);     		
    	}    	
	}
	/*
	 *从手持端实时上传坐标轨迹，存入支撑端数据库 手持终端获取时间戳 此方法解决坐标点偏移问题
	 * authCode: 验证码
	 * locLng: 经度
	 * logLat: 纬度
	 * pipelineID: 管线id
	 * pdmTime:手持机时间戳
	 */
	public String uploadCoor(String authCode,double locLng,double locLat,String taskID,String pdmTime,String coorType){
		String result="";
    	try{
    		SoapObject request=new SoapObject(NameSpace,MethodName); 		
    		request.addProperty("authCode",authCode);
    		request.addProperty("locLng", Double.toString(locLng));
    		request.addProperty("locLat", Double.toString(locLat));    		
    		request.addProperty("taskID", taskID);   
    		request.addProperty("pdmTime",pdmTime);
    		request.addProperty("coorType",coorType);
    		SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(
    				SoapEnvelope.VER11);
    		envelope.dotNet=true;
    		envelope.setOutputSoapObject(request);
    		HttpTransportSE ht=new HttpTransportSE(url);
    		ht.call(soapAction, envelope);
    		if(envelope.getResponse()!=null){
    			SoapPrimitive response=(SoapPrimitive)envelope.getResponse();
    			result=response.toString();  			
    		}
    	}catch(Exception e){
    		result=e.getMessage();
    	}finally{    
    		sText = result;		
    		Log.d(TAG, sText);     		
    	}  
    	return result;
	}
	/*
	 * 从手持设备上传 反馈信息，存入支撑端数据库
	 * authCode: 验证码
	 * list任务列表
	 */
	public String uploadResult(String authCode,List<Map<String, Object>> list ){
		String result="";
    	try{
    		JSONObject json = new JSONObject();
    		json.put("Taskid", list.get(0).get("Taskid"));
    		json.put("Terminalid", list.get(0).get("Terminaid"));
    		json.put("Feedbackcoor", list.get(0).get("Feedbackcoordinate"));
    		json.put("Feedbacktxt", list.get(0).get("Feedbacktxt"));
    		json.put("Feedbackdate", list.get(0).get("Feedbackdate"));
    		json.put("Feedbackperson", list.get(0).get("Feedbackperson"));
    		json.put("Coorphoto", list.get(0).get("Coorphoto"));
    		JSONArray array = new JSONArray();
    		array.put(json);
    		Log.d(TAG, array.toString());
    		String s_MethodName="UploadResultByOneTask";
    		String s_soapAction=NameSpace+s_MethodName;
    		SoapObject request=new SoapObject(NameSpace,s_MethodName);//NameSpace    		
    		
    		request.addProperty("authCode",authCode);
    		request.addProperty("list", array.toString());   			
    		
    		SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(
    				SoapEnvelope.VER11);    		
    		envelope.dotNet=true;
    		envelope.setOutputSoapObject(request);    		
    		HttpTransportSE ht=new HttpTransportSE(url);    		
    		ht.call(s_soapAction, envelope);
    		if(envelope.getResponse()!=null){
    			SoapPrimitive response=(SoapPrimitive)envelope.getResponse();
    			result=response.toString();
    		}    		
    	}catch(Exception e){
    		result=e.getMessage();
    	}finally{    
    		sText = result;		
    		Log.d(TAG, sText);     		
    	}    	
    	return result;
	}
	/*20140326 lee
	 * 上传图片方法
	 * name 为文件名
	 * uploadBuffer 为图片流数据
	 */
	public String uploadImage(String fileName, String context) {
		String result="";
		try {
			String s_MethodName="FileUploadImage";
    		String s_soapAction=NameSpace+s_MethodName;
			SoapObject rpc = new SoapObject(NameSpace, s_MethodName);
			rpc.addProperty("name", fileName);
			rpc.addProperty("bytestr", context);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			//与.net webservice 有很好的兼容性
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			new MarshalBase64().register(envelope);
			HttpTransportSE aht = new HttpTransportSE(url);
			//调用webservice 方法
			aht.call(s_soapAction, envelope);
			if(envelope.getResponse()!=null){
    			SoapPrimitive response=(SoapPrimitive)envelope.getResponse();
    			result=response.toString();
    		} 
			
			//Log.d(TAG, result.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			result=e.getMessage();
		}
		finally
		{
			sText = result;	
    		Log.d(TAG, sText); 
		}
		return result;
	}
	
	
	/*
	 * 获取任务单列表信息，从支撑端向手持端传递
	 * orgid:组织机构代码 
	 * deviceid：设备id
	 */
	public String getTaskByCode(String authCode,String userid ){
		String result="";
    	try{
    		String s_MethodName="GetTaskByCode";
    		String s_soapAction=NameSpace+s_MethodName;
    		SoapObject request=new SoapObject(NameSpace,s_MethodName);		
    		request.addProperty("authCode",authCode);
    		request.addProperty("userid", userid);   			
    		SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(
    				SoapEnvelope.VER11);    		
    		envelope.dotNet=true;
    		envelope.setOutputSoapObject(request);    		
    		HttpTransportSE ht=new HttpTransportSE(url);    		
    		ht.call(s_soapAction, envelope);
    		if(envelope.getResponse()!=null){
    			SoapPrimitive response=(SoapPrimitive)envelope.getResponse();
    			result=response.toString();
    		}
    		Log.d(TAG, result);   
    	}catch(Exception e){
    		result=e.getMessage();
    	}finally{    
    		sText = result;		    		  		
    	}    	
    	return result;
	}
	
	 /*上传任务状态和时间
	 * authCode: 验证码
	 * taskID：任务ID
	 * status:状态
	 * startTime:开始时间
	 * endTime: 结束时间
	 */
	public String updateTaskState(String authCode,String taskid,String state,String startTime,String endTime){
		String result="";
    	try{
    		
    		String s_MethodName="UpdateTaskState";
    		String s_soapAction=NameSpace+s_MethodName;
    		SoapObject request=new SoapObject(NameSpace,s_MethodName);
    		request.addProperty("authCode",authCode);
    		request.addProperty("taskid", taskid);
    		request.addProperty("taskState", state);
    		request.addProperty("startTime", startTime);   
    		request.addProperty("endTime", endTime);
    		SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(
    				SoapEnvelope.VER11);    		
    		envelope.dotNet=true;  		
    		envelope.setOutputSoapObject(request);    		
    		HttpTransportSE ht=new HttpTransportSE(url);    		
    		ht.call(s_soapAction, envelope); 		
    		if(envelope.getResponse()!=null){
    			SoapPrimitive response=(SoapPrimitive)envelope.getResponse();
    			result=response.toString(); 			
    		}
    		Log.d(TAG, result);   
    	}catch(Exception e){
    		result=e.getMessage();
    	}finally{    
    		sText = result;		    		  		
    	}    	
    	return result;
	}
	/*
	 *从支撑端下载管线信息，存储到手持端，终端机初始化时使用
	 */
	public String getPipeline(){
		String result="";
    	try{
    		
    		String s_MethodName="GetPipeline";
    		String s_soapAction=NameSpace+s_MethodName;
    		SoapObject request=new SoapObject(NameSpace,s_MethodName);//NameSpace    
    		SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(
    				SoapEnvelope.VER11);    		
    		envelope.dotNet=true;  		
    		envelope.setOutputSoapObject(request);    		
    		HttpTransportSE ht=new HttpTransportSE(url);    		
    		ht.call(s_soapAction, envelope); 		
    		if(envelope.getResponse()!=null){
    			SoapPrimitive response=(SoapPrimitive)envelope.getResponse();
    			result=response.toString();  			
    		}
    		Log.d(TAG, result);   
    	}catch(Exception e){
    		result=e.getMessage();
    	}finally{    
    		sText = result;		    		  		
    	}    	
    	return result;
	}
	
	/*
	 *从支撑端下载管线信息，存储到手持端，终端机初始化时使用
	 */
	public String getPLCoordinate(int index){
		String result="";
    	try{
    		
    		String s_MethodName="getPLCoordinate";
    		String s_soapAction=NameSpace+s_MethodName;
    		SoapObject request=new SoapObject(NameSpace,s_MethodName);
    		request.addProperty("index",index);
    		SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(
    				SoapEnvelope.VER11);    		
    		envelope.dotNet=true; 		
    		envelope.setOutputSoapObject(request);    		
    		HttpTransportSE ht=new HttpTransportSE(url);    		
    		ht.call(s_soapAction, envelope);		
    		if(envelope.getResponse()!=null){
    			SoapPrimitive response=(SoapPrimitive)envelope.getResponse();
    			result=response.toString();	
    		}
    		Log.d(TAG, result);   
    	}catch(Exception e){
    		result=e.getMessage();
    	}finally{    
    		sText = result;		    		  		
    	}    	
    	return result;
	}
    /*private void ws(){
    	String result="";
    	try{
    		SoapObject request=new SoapObject(NameSpace,MethodName);//NameSpace
    		//webService�����еĲ������������webservice��������û�С�
    		//��ע�⣬������ƺͲ������ͣ��ͻ��˺ͷ����һ��Ҫһ�£����򽫿��ܻ�ȡ��������Ҫ��ֵ
    		request.addProperty("authCode","0");
    		request.addProperty("locLng", Double.toString(100.333));
    		request.addProperty("locLat", Double.toString(100.777));
    		request.addProperty("pipeLineID", 2);
    		request.addProperty("index", 2);
    		
    		SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(
    				SoapEnvelope.VER11);
    		
    		envelope.dotNet=true;//����.NET��webservice
    		
    		envelope.setOutputSoapObject(request);
    		
    		HttpTransportSE ht=new HttpTransportSE(url);
    		
    		ht.call(soapAction, envelope);//����call����������webservice
    		
    		if(envelope.getResponse()!=null){
    			SoapPrimitive response=(SoapPrimitive)envelope.getResponse();
    			//���Ҫ���ض��󼯺ϣ��ڷ���˿��Խ�����򼯺����л���json�ַ��أ�����ٷ����л��ɶ���򼯺�
    			result=response.toString();//��������webService�ķ���ֵ    		
    
    		}
    		
    	}catch(Exception e){
    		result=e.getMessage();
    	}finally{    
    		sText = result;		
    		Log.d("....................xuming", sText);     		
    	}
    	//return result;
    }*/
}
