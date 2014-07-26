package com.ws.wsme.fragment;

import com.ws.wsme.R;
import com.ws.wsme.common.CommonLog;
import com.ws.wsme.common.LogFactory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TechFragment extends CommonFragment{
	
	private static final CommonLog log = LogFactory.createLog();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		log.e("TechFragment onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		log.e("TechFragment onCreateView");
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		log.e("TechFragment onActivityCreated");
		
		setupViews();
	}
	
	private void setupViews(){
		mImageView.setBackgroundResource(R.drawable.tech_app_bg);
	}

	@Override
	public void onDestroy() {
		log.e("TechFragment onDestroy");
		super.onDestroy();
	}
}
