package com.zh.spsclient.common;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.zh.spsclient.util.AMapUtil;


public class LocationDataAdapter 	implements
	AMapLocationListener {
	private LocationManagerProxy mAMapLocManager = null;
	public LocationDataAdapter(Activity activity){
		mAMapLocManager = LocationManagerProxy.getInstance(activity);
	}
	public void enableMyLocation() {
		// Location API��λ����GPS�������϶�λ��ʽ��ʱ�������5000����
		
		  mAMapLocManager.setGpsEnable(true);//
		 /* 1.0.2�汾��������������true��ʾ��϶�λ�а���gps��λ��false��ʾ�����綨λ��Ĭ����true
		 */
		mAMapLocManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 5000, 10, this);
	}
	
	public void disableMyLocation() {
		if (mAMapLocManager != null) {
			mAMapLocManager.removeUpdates(this);
		}
	}	
	
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		// TODO Auto-generated method stub
		if (location != null) {
			Double geoLat = location.getLatitude();
			Double geoLng = location.getLongitude();
			String cityCode = "";
			String desc = "";
			Bundle locBundle = location.getExtras();
			if (locBundle != null) {
				cityCode = locBundle.getString("citycode");
				desc = locBundle.getString("desc");
			}
			String str = ("��λ�ɹ�:(" + geoLng + "," + geoLat + ")"
					+ "\n��    ��    :" + location.getAccuracy() + "��"
					+ "\n��λ��ʽ:" + location.getProvider() + "\n��λʱ��:"
					+ AMapUtil.convertToTime(location.getTime()) + "\n���б���:"
					+ cityCode + "\nλ������:" + desc + "\nʡ:"
					+ location.getProvince() + "\n�У�" + location.getCity()
					+ "\n��(��)��" + location.getDistrict() + "\n���б��룺"
					+ location.getCityCode() + "\n������룺" + location.getAdCode());
			Message msg = new Message();
			msg.obj = str;			
		}
	}
	public void destoryMyLocation() {
		// TODO Auto-generated method stub
		if (mAMapLocManager != null) {
			mAMapLocManager.removeUpdates(this);
			mAMapLocManager.destory();
		}
		mAMapLocManager = null;
	}

}
