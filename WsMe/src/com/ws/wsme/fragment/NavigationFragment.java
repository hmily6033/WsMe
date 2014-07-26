package com.ws.wsme.fragment;

import com.ws.wsme.WsMainActivity;
import com.ws.wsme.R;
import com.ws.wsme.common.CommonLog;
import com.ws.wsme.common.FragmentControlCenter;
import com.ws.wsme.common.FragmentModel;
import com.ws.wsme.common.LogFactory;



import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

@SuppressLint("InflateParams")
public class NavigationFragment extends Fragment implements OnCheckedChangeListener, OnClickListener{

	private static final CommonLog log = LogFactory.createLog();
	
	private View mView;
	private RadioGroup  m_radioGroup;
	private ImageButton m_AddButton;
	
	private FragmentControlCenter mControlCenter;
	
	public NavigationFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		log.e("NavigationFragment onCreate");
		
		mControlCenter = FragmentControlCenter.getInstance(getActivity());
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		log.e("NavigationFragment onDestroy");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		log.e("NavigationFragment onCreateView");
		
		mView = inflater.inflate(R.layout.navitation_channel_layout, null);
		return mView;	
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		log.e("NavigationFragment onActivityCreated");
		
		setupViews();
	}
	
	
	private void setupViews(){
		m_radioGroup = (RadioGroup) mView.findViewById(R.id.nav_radiogroup);
		((RadioButton) m_radioGroup.getChildAt(0)).toggle();
		
		m_radioGroup.setOnCheckedChangeListener(this);
		
		m_AddButton = (ImageButton) mView.findViewById(R.id.ib_add_content);
		m_AddButton.setOnClickListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int id) {
		switch(id){
		case R.id.rb_toutiao:
			goTouTiaoFragment();
			break;
		case R.id.rb_yule:
			goYuLeFragment();
			break;
		case R.id.rb_tech:
			goTechFragment();
			break;
		case R.id.rb_blog:
			goBlogFragment();
			break;
		}
	}
	
	
	private void goTouTiaoFragment(){
		if (getActivity() == null)
			return;

		FragmentModel fragmentModel = mControlCenter.getTouTiaoFragmentModel();
		if (getActivity() instanceof WsMainActivity) {
			WsMainActivity ra = (WsMainActivity) getActivity();
			ra.switchContent(fragmentModel);
		}
	}
	
	private void goYuLeFragment(){
		if (getActivity() == null)
			return;

		
		FragmentModel fragmentModel = mControlCenter.getYuLeFragmentModel();
		if (getActivity() instanceof WsMainActivity) {
			WsMainActivity ra = (WsMainActivity) getActivity();
			ra.switchContent(fragmentModel);
		}
	}
	
	private void goTechFragment(){
		if (getActivity() == null)
			return;

		FragmentModel fragmentModel = mControlCenter.getTechFragmentModel();
		if (getActivity() instanceof WsMainActivity) {
			WsMainActivity ra = (WsMainActivity) getActivity();
			ra.switchContent(fragmentModel);
		}
	}
	
	private void goBlogFragment(){
		if (getActivity() == null)
			return;

		FragmentModel fragmentModel = mControlCenter.getBlogFragmentModel();
		if (getActivity() instanceof WsMainActivity) {
			WsMainActivity ra = (WsMainActivity) getActivity();
			ra.switchContent(fragmentModel);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.ib_add_content:
			Toast.makeText(getActivity(), "添加尼妹,现在没内容！！！", Toast.LENGTH_SHORT).show();
			break;
		}
	}
	
}
