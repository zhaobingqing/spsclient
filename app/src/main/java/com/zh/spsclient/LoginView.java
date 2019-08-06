package com.zh.spsclient;


import com.zh.spsclient.R;
import com.zh.spsclient.common.CommonRecord;
import com.zh.spsclient.common.WebServiceAdapter;
import com.zh.spsclient.dao.UserLoginDAO;
import com.zh.spsclient.util.Constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginView extends Activity{
	private EditText edtUserName;
	private EditText edtPwd;
	private EditText edtYzm;

	private Button btnLogon;
	private TextView txtResult;
	private WebServiceAdapter adapter;
	ProgressDialog m_Dialog;
	String sUserName;
	String sEdtPwd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.login_view);
		/*// 隐藏键盘
		this.getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);*/
		
		try {
			this.initViews();
			/*InputMethodManager imm = (InputMethodManager)getSystemService(
				      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(edtUserName.getWindowToken(), 0);*/
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	private void showDialog(String mess) {
		new AlertDialog.Builder(this).setTitle(Constants.LOGIN_VIEW_INFO).setMessage(mess)
				.setNegativeButton(Constants.LOGIN_VIEW_CONFIRM, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}
	
	private void initViews() {
		this.edtUserName = (EditText) this.findViewById(R.id.edtUserName);
		this.edtPwd = (EditText) this.findViewById(R.id.edtPwd);
		this.edtYzm = (EditText) this.findViewById(R.id.edtYzm);
		edtYzm.setCursorVisible(false);
		
		txtResult = (TextView)this.findViewById(R.id.txtResult);
		txtResult.setText("");
		adapter = new WebServiceAdapter();
		this.btnLogon = (Button) this.findViewById(R.id.btnLogon);
		this.btnLogon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sUserName = edtUserName.getText().toString();
				sEdtPwd = edtPwd.getText().toString();
				if (sUserName.trim().equals("")) {
					showDialog(Constants.PLEASE_INPUT_USER_NAME);					
					return;
				}
				if (sEdtPwd.trim().equals("")) {
					showDialog(Constants.PLEASE_INPUT_PASSWORD);
					return;
				}	
				m_Dialog = ProgressDialog.show(LoginView.this, 
						Constants.LOGIN_VIEW_PLEASE_WAITING, 
						Constants.LOGIN_VIEW_REQUESTING_SERVER,true);
				new Thread() {					
					@Override
					public void run() {
						try {
							//创建Message对象
							Message m = new Message();									
							Boolean bIsLogin = false;
							// 调用后台服务器，以验证用户登录是否合法。
							sUserName="admin";
							sEdtPwd = "admin";
							bIsLogin = adapter.AuthorizeUser(CommonRecord.authCode, sUserName.trim(),
									sEdtPwd.trim());
							//bIsLogin = true;
							if (bIsLogin) {
								logonSuccessHandler.sendMessage(m);												 
							}else{
								// 设置验证失败的信息												
								//将消息放到消息队列中
								logonFailorHandler.sendMessage(m);
							}					
						} catch (Exception e) {
							// TODO: handle exception
						}	
						finally{
							m_Dialog.dismiss();
						}
					}
				}.start();
			}					
		});
	}
	
	//执行接收到的消息，执行的顺序是按照队列进行，即先进先出
	Handler logonFailorHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 设置验证失败的信息
			txtResult.setText(R.string.loginview_authorization_failor);
		}
	};
	Handler logonSuccessHandler = new Handler(){
		@Override
		@SuppressLint("HandlerLeak")
		public void handleMessage(Message msg) {
			UserLoginDAO userLoginDAO = new UserLoginDAO();
			userLoginDAO.database = openOrCreateDatabase(CommonRecord.dbName,MODE_PRIVATE,null);
			if(userLoginDAO.database  != null){
				userLoginDAO.insertUserLoginData(sUserName.trim(), sEdtPwd.trim(), CommonRecord.authCode);
				userLoginDAO.database.close();
			}
			Intent intent=new Intent();
			intent.putExtra("user", sUserName);
			CommonRecord._currentUser = sUserName;
			intent.setClass(LoginView.this, MainView.class);
			startActivity(intent);
			//结束Welcome Activity	
			LoginView.this.finish();	
		}
	};
}
