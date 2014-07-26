package com.ws.wsme;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.ws.wsme.common.FragmentControlCenter;
import com.ws.wsme.common.FragmentModel;
import com.ws.wsme.fragment.NavigationFragment;
import com.ws.wsme.fragment.SettingFragment;

/**
 * 
 * @author LCL
 */
public class WsMainActivity extends WsBaseActivity implements OnClickListener{

	//private static final CommonLog log = LogFactory.createLog();
	
	private int mTitle;
	private Fragment mContent;
	
	private ImageView mLeftIcon;
	private ImageView mRightIcon;
	private TextView mTitleTextView;
	
	private FragmentControlCenter mControlCenter;
	public static  String KeyString="";
	private FragmentModel fragmentModel ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mControlCenter = FragmentControlCenter.getInstance(this);
		
		setupViews();
		
		initData();
	}
	
	
	
	private void setupViews(){

		setContentView(R.layout.main_slidemenu_layout);
		
		initActionBar();
		
		initSlideMenu();
	
	}
	
	private void initSlideMenu(){
		fragmentModel = mControlCenter.getBlogFragmentModel();
		switchContent(fragmentModel);

		
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT_RIGHT);

		setBehindContentView(R.layout.left_menu_frame);
		sm.setSlidingEnabled(true);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.left_menu_frame, new NavigationFragment())
		.commit();
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setBehindScrollScale(0);
		sm.setFadeDegree(0.25f);

		
		sm.setSecondaryMenu(R.layout.right_menu_frame);
		sm.setSecondaryShadowDrawable(R.drawable.shadow);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.right_menu_frame, new SettingFragment())
		.commit();
	}
	
	private void initActionBar(){
		ActionBar actionBar = getSupportActionBar();
		actionBar.setCustomView(R.layout.actionbar_layout);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		
		mLeftIcon = (ImageView) findViewById(R.id.iv_left_icon);
		mRightIcon = (ImageView) findViewById(R.id.iv_right_icon);
		mLeftIcon.setOnClickListener(this);
		mRightIcon.setOnClickListener(this);
		
		mTitleTextView = (TextView) findViewById(R.id.tv_title);
	}
	
	private void initData(){
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//super.onActivityResult(requestCode, resultCode, data);
		fragmentModel.mFragment.onActivityResult(requestCode, resultCode, data);
        
	}

	public void switchContent(final FragmentModel fragment) {
		mTitle = fragment.mTitle;
		mContent = fragment.mFragment;

		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mContent)
		.commit();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
		
		mTitleTextView.setText(mTitle);
	}



	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.iv_left_icon:
			toggle();
			break;
		case R.id.iv_right_icon:
			showSecondaryMenu();
			break;
		}
	}	
	
	public String getwbKey()
	{
		return KeyString;
	}
	public void setwbKey(String keyString)
	{
		KeyString=keyString;
	}
}

