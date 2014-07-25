package com.ws.wsme;

import java.util.HashMap;
import java.util.Map;

import com.ws.wsme.fragment.BlogFragment;
import com.ws.wsme.fragment.TechFragment;
import com.ws.wsme.fragment.TouTiaoFragment;
import com.ws.wsme.fragment.YuleFragment;

import android.content.Context;

public class FragmentControlCenter {
	private static  FragmentControlCenter instance;
	private Map<String, FragmentModel> mFragmentModelMaps = new HashMap<String, FragmentModel>();

	private FragmentControlCenter(Context context) {
	}
	
	public static synchronized FragmentControlCenter getInstance(Context context) {
		if (instance == null){
			instance  = new FragmentControlCenter(context);
		}
		return instance;
	}

	
	public FragmentModel getTouTiaoFragmentModel(){
		FragmentModel fragmentModel = mFragmentModelMaps.get(FragmentBuilder.TOU_TIAO_FRAGMENT);
		if (fragmentModel == null){
			fragmentModel = FragmentBuilder.getTouTiaoFragmentModel();
			mFragmentModelMaps.put(FragmentBuilder.TOU_TIAO_FRAGMENT, fragmentModel);
		}
		return fragmentModel;
	}
	
	public FragmentModel getYuLeFragmentModel(){
		FragmentModel fragmentModel = mFragmentModelMaps.get(FragmentBuilder.YU_LE_FRAGMENT);
		if (fragmentModel == null){
			fragmentModel = FragmentBuilder.getYuLeFragmentModel();
			mFragmentModelMaps.put(FragmentBuilder.YU_LE_FRAGMENT, fragmentModel);
		}
		return fragmentModel;
	}
	
	public FragmentModel getTechFragmentModel(){
		FragmentModel fragmentModel = mFragmentModelMaps.get(FragmentBuilder.TECH_FRAGMENT);
		if (fragmentModel == null){
			fragmentModel = FragmentBuilder.getTechFragmentModel();
			mFragmentModelMaps.put(FragmentBuilder.TECH_FRAGMENT, fragmentModel);
		}
		return fragmentModel;
	}
	
	public FragmentModel getBlogFragmentModel(){
		FragmentModel fragmentModel = mFragmentModelMaps.get(FragmentBuilder.BLOG_FRAGMENT);
		if (fragmentModel == null){
			fragmentModel = FragmentBuilder.getBlogFragmentModel();
			mFragmentModelMaps.put(FragmentBuilder.BLOG_FRAGMENT, fragmentModel);
		}
		return fragmentModel;
	}
	
	
	public FragmentModel getFragmentModel(String name){
		return mFragmentModelMaps.get(name);
	}

	public void addFragmentModel(String name,FragmentModel fragment){
		mFragmentModelMaps.put(name, fragment);
	}
	
	
	
	
	
	public static class FragmentBuilder{
		public static final String TOU_TIAO_FRAGMENT = "TOU_TIAO_FRAGMENT";
		public static final String YU_LE_FRAGMENT = "YU_LE_FRAGMENT";
		public static final String TECH_FRAGMENT = "TECH_FRAGMENT";
		public static final String BLOG_FRAGMENT = "BLOG_FRAGMENT";
		
		
		public static FragmentModel  getTouTiaoFragmentModel(){
			TouTiaoFragment fragment = new TouTiaoFragment();
			FragmentModel fragmentModel = new FragmentModel(R.string.left_nav_ttnews_button_text, fragment);
			return fragmentModel;
		}
		
		public static FragmentModel getYuLeFragmentModel(){
			YuleFragment fragment = new YuleFragment();
			FragmentModel fragmentModel = new FragmentModel(R.string.left_nav_yule_button_text, fragment);
			return fragmentModel;
		}
		
		public static FragmentModel getTechFragmentModel(){
			TechFragment fragment = new TechFragment();
			FragmentModel fragmentModel = new FragmentModel(R.string.left_nav_kj_button_text, fragment);
			return fragmentModel;
		}
		
		public static FragmentModel getBlogFragmentModel(){
			BlogFragment fragment = new BlogFragment();
			FragmentModel fragmentModel = new FragmentModel(R.string.left_nav_weibo_button_text, fragment);
			return fragmentModel;
		}
	}
}
