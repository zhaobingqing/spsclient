package com.zh.spsclient.activity;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import com.zh.spsclient.R;
import com.zh.spsclient.common.CommonRecord;
import com.zh.spsclient.dao.TaskResultDAO;
import com.zh.spsclient.entity.TaskResultEntity;
import com.zh.spsclient.service.MapService;
import com.zh.spsclient.util.Constants;
import com.zh.spsclient.util.ImageUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TaskResultView extends Activity{
	private EditText taskContent;
	private Button btnReturn;
	private Button submitBill;
	private TextView taskName;
	private TextView finishedPerson;
	private TextView finishedTime;
	private TextView recordSelector;
	private ImageView imgPhoto;
	private ImageView imgArrow;
	private ImageView mImageView;
	private TextView posCoor;
	/*private WebServiceAdapter adapter;*/
	private String currentCoor;
	private String mCurrentPhotoPath;// 图片路径
	private String imageFileName="";//图片名称
	private static final int REQUEST_TAKE_PHOTO = 0;//此标记为照相
	
	//private static final int PHOTO_ITEM = Menu.FIRST;
	private static final int VIDEO_ITEM = Menu.FIRST + 1;
	private static final int AUDIO_ITEM = Menu.FIRST + 2;
	static int nIndex = 0;
	private String sContextText = "";
	
	/*List<Map<String, Object>> list;	*/
	Map<String, Object> taskResult;
	Boolean IsEnd;
	private String status;
	TaskResultEntity entity = new TaskResultEntity();
	TaskResultDAO manager = null;
	//private String sUser = "";
	/*
	 * 
	 */
	Handler EndCheckingHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {			
			Toast.makeText(TaskResultView.this, "任务上传成功" 	, Toast.LENGTH_LONG).show();			
			if(status.equals("2")){
				Intent intent=new Intent(TaskResultView.this,MapService.class);
				stopService(intent);
			}
			if(IsEnd){ 
				Intent intent = new Intent();
				intent.setClass(TaskResultView.this, TaskMapView.class);
				Bundle bundle = new Bundle();
		 		bundle.putSerializable("tasklist", (Serializable)taskResult);
		 		intent.putExtras(bundle);  
				startActivity(intent);
			}
			TaskResultView.this.finish();
		}
	};
	
	@SuppressWarnings("unchecked")
	@SuppressLint({ "NewApi", "SimpleDateFormat" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
		this.setContentView(R.layout.taskresult_view);
		// 隐藏键盘
		this.getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		Bundle bundle = this.getIntent().getExtras();		
		taskResult = (Map<String, Object>) bundle.getSerializable("tasklist");
		//sUser = bundle.getString("user");
		currentCoor = bundle.getString("currentCoor");		
		IsEnd = bundle.getBoolean("IsEnd");
		btnReturn=(Button)this.findViewById(R.id.btnReturn);
		taskName = (TextView)this.findViewById(R.id.getTaskName);
		taskContent=(EditText) this.findViewById(R.id.taskContent);
		finishedPerson = (TextView)this.findViewById(R.id.finishedPerson);
		finishedTime = (TextView)this.findViewById(R.id.finishedTime);
		recordSelector = (TextView)this.findViewById(R.id.recordSelector);
		imgPhoto=(ImageView)this.findViewById(R.id.imgPhoto);
		imgArrow = (ImageView)this.findViewById(R.id.imgArrow);
		mImageView=(ImageView)this.findViewById(R.id.imgView);
		posCoor = (TextView)this.findViewById(R.id.posCoor);
		//任务名称
		taskName.setText((String) taskResult.get("taskName"));		
		//任务状态
		status = (String)taskResult.get("taskState");
		// 任务内容
		taskContent.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);  
		taskContent.setGravity(Gravity.TOP); 
		taskContent.setVerticalScrollBarEnabled(true);
		//当前坐标
		posCoor.setText(currentCoor);
		//返回监控页面
		btnReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			TaskResultView.this.finish();
			}
		});
		//拍照图片事件
		imgPhoto.setImageResource(R.drawable.ic_action_photo);
		imgPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				takePhoto();//拍照
			}
		});
		
		//设置箭头选择的操作
		imgArrow.setImageResource(R.drawable.arrow);
		imgArrow.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(TaskResultView.this)
				.setTitle("请选择")
				.setItems(R.array.arrcontent,
					new DialogInterface.OnClickListener() {
					
						@Override
						public void onClick(DialogInterface dialog,int whichcountry) {
							
							String[] travelcountries = getResources().getStringArray(R.array.arrcontent);
							sContextText = travelcountries[whichcountry];
							recordSelector.setText(sContextText);
							
						}
					}).setNegativeButton("", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									
							}
					}).show();	
			}
		});
		//完成人
		//finishedPerson.setText((String) taskResult.get("Userid"));
		finishedPerson.setText(CommonRecord._currentUser);
		Date date = new Date();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		final String endTime = df.format(date);
		//完成时间
		finishedTime.setText(endTime);
		
		//提交反馈结果的操作
		submitBill=(Button) this.findViewById(R.id.submitResult);
		submitBill.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				final ProgressDialog m_Dialog = ProgressDialog.show(TaskResultView.this, 
						Constants.TASK_LIST_VIEW_PLEASE_WAITING, 
						Constants.TASK_RESULT_VIEW_UPLOAD_FEEDBACK,true);
				
				new Thread()
	    	    {
	    	    	@SuppressLint("SimpleDateFormat")
					@Override
	    	    	public void run()
	    	    	{ 
	    	    		try {
							
							//final String status = "5"; //状态
							
							/*SimpleDateFormat df = new SimpleDateFormat(
									"yyyy-MM-dd hh:mm:ss");*/
							//final String startTime = "";
							
							//final String endTime = df.format(date); //结束时间
							
							/*if (IsEnd) { 
								adapter.updateTaskState(authCode, taskID,
										status, startTime, endTime);
							}*/
	    	    			final String authCode = (String) taskResult
									.get(Constants.TERMINAL_ID);//设备ID
							final String taskID = (String) taskResult
									.get(Constants.TASK_ID);//任务ID
							Date date = new Date();
							SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    	   		 	final String feedBackTime = df.format(date);
		    	   		    final String imgName=imageFileName.toString();
		    	   		    if(((String) recordSelector.getText()).trim().equals(R.string.taskresult_selector_message))
								recordSelector.setText("");
							String content = taskContent.getText().toString()
									+ sContextText;  //反馈内容
							/*adapter = new WebServiceAdapter();*/
							/*list = new ArrayList<Map<String, Object>>();
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("Taskid", taskResult.get(Constants.TASK_ID));
							map.put("Terminaid",
									taskResult.get(Constants.TERMINAL_ID));
							map.put("Feedbackcoordinate", currentCoor); //坐标
							
							if(((String) recordSelector.getText()).trim().equals(R.string.taskresult_selector_message))
								recordSelector.setText("");
							String content = taskContent.getText().toString()
									+ sContextText;  //反馈内容
							map.put("Feedbacktxt", content);
							map.put("Feedbackdate", feedBackTime);
							map.put("Feedbackperson", CommonRecord._currentUser);
							if(mCurrentPhotoPath!=null)
							    map.put("Coorphoto", imgName);//插入图片名称
							list.add(map);*/
							//保存进入本地数据库//
							insertResultData(taskID,authCode,feedBackTime,currentCoor,content,imgName,CommonRecord._currentUser);
							//上传至服务器
							/*adapter.uploadResult(authCode, list);
							if(mCurrentPhotoPath!=null)
							{
								//上传图片
								upload();
							}*/
							/*if(IsEnd){
								deleteRealCoorData(taskID);
							}*/
							if(EndCheckingHandler != null){
								EndCheckingHandler.sendMessage(new Message());
							}
						} catch (Exception e) {
							// TODO: handle exception
						} finally{
							m_Dialog.dismiss();
						}
	    	    	}
	    	    }.start();
			}			
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,Intent intent) {
		if (requestCode == REQUEST_TAKE_PHOTO) {
			if (resultCode == Activity.RESULT_OK) {
				
				
				// 添加到图库,这样可以在手机的图库程序中看到程序拍摄的照片
				ImageUtil.galleryAddPic(this, mCurrentPhotoPath);

				//通过ImageView显示照片信息
				mImageView.setImageBitmap(ImageUtil
						.getCompressionBitmap(mCurrentPhotoPath));
				mImageView.setVisibility(View.VISIBLE);
			} else {
				// 取消照相后，删除已经创建的临时文件。
				ImageUtil.deleteTempFile(mCurrentPhotoPath);
				mImageView.setVisibility(View.GONE);
			}
		}
	}
	
	

	
	/**
	 * 拍照
	 */
	private void takePhoto() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			// 指定存放拍摄照片的位置
			File f = createImageFile();
			takePictureIntent
					.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 把程序拍摄的照片放到 SD卡的 Pictures目录中 SmartPatrolSystem 文件夹中
	 * 照片的命名规则为：sps_20130125_173729.jpg
	 * @return
	 * @throws IOException
	 */
	@SuppressLint("SimpleDateFormat")
	private File createImageFile() throws IOException {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String timeStamp = format.format(new Date());
	    imageFileName = "sps" + timeStamp + ".jpg";
		File image = new File(ImageUtil.getAlbumDir(), imageFileName);
		mCurrentPhotoPath = image.getAbsolutePath();
		return image;
	}
	/**
	 * 上传到服务器
	 */
	/*private void upload() {

		if (mCurrentPhotoPath != null) {
			
			sendImage(mCurrentPhotoPath, "0");
		}
//		else {
//			Toast.makeText(this, "请先点击拍照按钮拍摄照片", Toast.LENGTH_SHORT).show();
//		}
	}*/
	/*
	 * 上传图片
	 */
	/*protected String sendImage(String... params) {
		String filePath = params[0];
		ImageFileEntity bean = new ImageFileEntity();
		String type = params[1];// 上传图片还是视频，图片需要压缩
		String content;
			content = ImageUtil.bitmapToString(filePath);
		bean.setImageContent(content);
		File f = new File(filePath);
		String fileName = f.getName();
		bean.setImageName(fileName);
		String imgJson=bean.getImageContent();
	    return adapter.uploadImage(fileName,imgJson);//使用webservice
	}*/
	
	
	/*
	 * 插入数据
	 */
	private void insertResultData(
			String taskID,
			String terminalID,
			String endtime,
			String coors,
			String contents,String coorphoto,String feedBackPerson){
		manager = new TaskResultDAO();
		manager.database = openOrCreateDatabase(CommonRecord.dbName,MODE_PRIVATE,null);
		entity.setTaskResultID(String.valueOf(nIndex));
		entity.setTaskID(taskID);
		entity.setTerminalID(terminalID);
		entity.setFeedbackDate(endtime);
		entity.setCoordinates(coors);
		entity.setContents(contents);
		entity.setFeedBackPerson(feedBackPerson);
		entity.setCoorphoto(coorphoto);
		entity.setIfUpLoad("0");
		manager.insertTable(entity);
		//manager.queryData();
		manager.closeDB();
	}
	/*private void deleteRealCoorData(String taskID){
		RealCoorDAO realCoorDAO = new RealCoorDAO();
		realCoorDAO.database = openOrCreateDatabase(CommonRecord.dbName,MODE_PRIVATE,null);
		realCoorDAO.deleteData();
		realCoorDAO.closeDB();
	}*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//MenuItem mnudt = menu.add(0, PHOTO_ITEM, 0, R.string.view_photo).setIcon(R.drawable.bill);
		menu.add(0, VIDEO_ITEM, 0, R.string.view_video).setIcon(R.drawable.bill);
		menu.add(0, AUDIO_ITEM, 0, R.string.view_audio).setIcon(R.drawable.bill);
		return super.onCreateOptionsMenu(menu);
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {		
		case 0:			
			break;
		case 1:
			break;
		case 2:
			break;
		}
		return true;
	}
	
	
//	private class FileUploadTask extends AsyncTask<String, Void, String> {
//
//		@Override
//		protected String doInBackground(String... params) {
//			String filePath = params[0];
//			ImageFileEntity bean = new ImageFileEntity();
//			String type = params[1];// 上传图片还是视频，图片需要压缩
//			String content;
//				content = ImageUtil.bitmapToString(filePath);
//			bean.setImageContent(content);
//			File f = new File(filePath);
//			String fileName = f.getName();
//			bean.setImageName(fileName);
//			String imgJson=bean.getImageContent();
//		    return adapter.uploadImage(fileName,imgJson);//使用webservice
//		}
//	}
}
	
	
