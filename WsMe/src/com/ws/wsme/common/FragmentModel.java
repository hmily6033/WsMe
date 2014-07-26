package com.ws.wsme.common;

import android.support.v4.app.Fragment;

public class FragmentModel {

	public int mTitle;
	public Fragment mFragment;
	public FragmentModel(int title, Fragment fg){
		mTitle = title;
		mFragment = fg;
	}
}
