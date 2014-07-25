package com.ws.wsme.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ws.wsme.CommonLog;
import com.ws.wsme.LogFactory;
import com.ws.wsme.R;

public class SettingFragment extends Fragment{
	
	private static final CommonLog log = LogFactory.createLog();
	
	//private View mView;
	
	public SettingFragment(){
		
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		log.e("SettingFragment onCreate");
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		log.e("SettingFragment onCreateView");
		View view = inflater.inflate(R.layout.setting_layout, null);
		
		return view;
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		log.e("SettingFragment onActivityCreated");
		
		setupViews();
	}
	
	private void setupViews(){
	
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		log.e("SettingFragment onDestroy");
	}
}
