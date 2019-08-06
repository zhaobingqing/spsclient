package com.zh.spsclient.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zh.spsclient.R;
import com.zh.spsclient.common.CommonRecord;
import com.zh.spsclient.dao.TaskResultDAO;
import com.zh.spsclient.entity.TaskEntity;
import com.zh.spsclient.entity.TaskResultEntity;
import com.zh.spsclient.util.AMapUtil;
import com.zh.spsclient.util.Converts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressWarnings("unused")
public class TaskDetailView extends Activity {
	private TextView taskName;
	private TextView taskDes;
	private TextView taskState;
	private TextView taskPlanTime;
	private TextView taskFinishedTime;
	private TextView pipeLineName;
	private Button btnReturn;
	private Button retrieveMap;
	private TextView taskFinishedPos;
	private TextView taskStartPos;
	Map<String, Object> taskDetail;
	private LinearLayout myListLayout;
	private ListView tripListView;
	private TextView txtHistoryRecord;
	private View splitView;
	private Boolean bIsShowHistory = false;
	private TextView txtPerson;
	
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
		this.setContentView(R.layout.taskdetail_view);
		
		taskName = (TextView)this.findViewById(R.id.taskName);
		taskDes = (TextView)this.findViewById(R.id.taskDes);
		taskState = (TextView)this.findViewById(R.id.taskState);
		taskPlanTime = (TextView)this.findViewById(R.id.taskPlanTime);
		taskFinishedTime = (TextView)this.findViewById(R.id.taskFinishedTime);
		pipeLineName = (TextView)this.findViewById(R.id.pipeLineName);
		taskStartPos = (TextView)this.findViewById(R.id.taskStartPos);
		taskFinishedPos = (TextView)this.findViewById(R.id.taskFinishedPos);
		btnReturn = (Button)this.findViewById(R.id.btnReturn);
		retrieveMap = (Button)this.findViewById(R.id.retrieveMap);
		myListLayout = (LinearLayout) this.findViewById(R.id.myListView);
		txtHistoryRecord = (TextView)this.findViewById(R.id.txtHistoryRecord);
		splitView = this.findViewById(R.id.splitView);
		txtPerson = (TextView)this.findViewById(R.id.txtPerson);
		//返回主页面的操作
		btnReturn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub				
		 		TaskDetailView.this.finish();
			}			
		});
		//"查看更多"的操作
        retrieveMap.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				bIsShowHistory = !bIsShowHistory;
				if(bIsShowHistory){
					myListLayout.setVisibility(View.VISIBLE);
					txtHistoryRecord.setVisibility(View.VISIBLE);
					splitView.setVisibility(View.VISIBLE);
				}
				else{
					myListLayout.setVisibility(View.GONE);
					txtHistoryRecord.setVisibility(View.GONE);
					splitView.setVisibility(View.GONE);
				}
			}        	
        });
		getTaskDetail();
		tripListView = new ListView(this);
		tripListView.setDivider(getResources().getDrawable(R.color.task_detail_divider));
		tripListView.setDividerHeight(1);
		LinearLayout.LayoutParams tripListViewParam = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		tripListView.setCacheColorHint(Color.WHITE);
		myListLayout.addView(tripListView, tripListViewParam);
		
		SimpleAdapter adapter = new SimpleAdapter(this, getTripListData(),
				R.layout.taskresult_row, new String[] { "resultContent", "resultTime",
						"resultPosition" }, new int[] {  R.id.resultContent,
						R.id.resultTime, R.id.resultPosition});
		tripListView.setAdapter(adapter);
		
		tripListView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(TaskDetailView.this,
						"" + list.get(position).get("resultContent").toString(),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 *
	 */
	@SuppressWarnings("unchecked")
	public void getTaskDetail() {
		Bundle bundle = this.getIntent().getExtras();
		taskDetail = (Map<String, Object>) bundle.getSerializable("tasklist");
		
		final TaskEntity task = new TaskEntity();
		//task.setId(1);
		task.setTaskName(taskDetail.get("taskName").toString() + getString(R.string.detail));
		TextView taskDetailName = (TextView) this.findViewById(R.id.taskDetailName);
		//taskDetailName.setText(task.getTaskName());
		taskDetailName.setText("任务详情");
		
		/*ImageView goodsPic = (ImageView) this.findViewById(R.id.goodsPic);
		try {
			URL picUrl = new URL(goods.getDir() + "/" + goods.getPic());
			Bitmap pngBM = BitmapFactory.decodeStream(picUrl.openStream());
			goodsPic.setImageBitmap(pngBM);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
			
		taskName.setText((String)taskDetail.get("taskName"));
		taskDes.setText((String)taskDetail.get("taskDes"));
		taskState.setText(Converts.ConvertToTaskStatus((String)taskDetail.get("taskState")));
		String sPlanTimeStart = (String)taskDetail.get("Planstartdate");
		sPlanTimeStart = AMapUtil.convertToDate(Long.parseLong(sPlanTimeStart.substring(6, sPlanTimeStart.length()-2)));
		String sPlanTimeEnd = (String)taskDetail.get("Planenddate");
		sPlanTimeEnd = AMapUtil.convertToDate(Long.parseLong(sPlanTimeEnd.substring(6, sPlanTimeEnd.length()-2)));
		String sPlanTime = "从 " + sPlanTimeStart + " 到 " + sPlanTimeEnd;
		taskPlanTime.setText(sPlanTime);
		String sActualTime = "从" + " " + "到" + " ";
		taskFinishedTime.setText(sActualTime);
		pipeLineName.setText((String)taskDetail.get("PipeName"));
		taskStartPos.setText((String)taskDetail.get("Taskstartposition"));
		taskFinishedPos.setText((String)taskDetail.get("Taskendposition"));
		//txtPerson.setText((String)taskDetail.get("Person"));
		txtPerson.setText(CommonRecord._currentUser);
	}

	/**
	 * 
	 * @return List
	 */
	public List<Map<String, Object>> getTripListData() {

		Map<String, Object> map = new HashMap<String, Object>();
		TaskResultDAO manager = new TaskResultDAO();
		manager.database = openOrCreateDatabase(CommonRecord.dbName,MODE_PRIVATE,null);
		List<TaskResultEntity> entities = manager.retrieveData();
		for(int i = 0; i < entities.size(); i++){
			map = new HashMap<String, Object>();
			map.put("resultContent", entities.get(i).getContents());
			map.put("resultTime", entities.get(i).getFeedbackDate());
			map.put("resultPosition", entities.get(i).getCoordinates());		
			
			list.add(map);
		}
		
		manager.database.close();
		return list;
	}
	//
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuItem mnuxq = menu.add(0, 0, 0, "�˵�");
		mnuxq.setIcon(R.drawable.cart);
		return super.onCreateOptionsMenu(menu);
	}*/

	//
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case 0:
			Intent it = new Intent();
			//it.setClass(TaskDetailView.this, CartListView.class);
			startActivity(it);
			break;		
		}
		return true;
	}
}
