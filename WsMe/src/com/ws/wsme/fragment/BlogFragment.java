package com.ws.wsme.fragment;


import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.ws.wsme.R;
import com.ws.wsme.WsMainActivity;
import com.ws.wsme.common.AccessTokenKeeper;
import com.ws.wsme.common.AsyncTaskImageDownload;
import com.ws.wsme.common.Constants;
import com.ws.wsme.http.common.C;
import com.ws.wsme.http.http.AnsynHttpRequest;
import com.ws.wsme.http.http.ObserverCallBack;
import com.ws.wsme.view.XListView;
import com.ws.wsme.view.XListView.IXListViewListener;
import com.ws.wsme.wsadapter.BlogFragmentAdapter;

public class BlogFragment extends Fragment implements IXListViewListener{

	private Oauth2AccessToken mAccessToken;
	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
	private SsoHandler mSsoHandler;
	public Map<String, String> map;
	// 判断是否是初始化
	private boolean isInit = false;
	private BlogFragmentAdapter adapter = null;
	private XListView mListView;
	// 异步加载图片
	private AsyncTaskImageDownload asyncTaskImageDownload = new AsyncTaskImageDownload();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mListView = (XListView) getActivity().findViewById(R.id.xListView);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		WsMainActivity wsMainActivity = (WsMainActivity) getActivity();

		if (!wsMainActivity.getwbKey().isEmpty()) {
			getExistwbJson();
		}

	}

	public void initList() {
		mListView.setAdapter(adapter);
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				isInit = true;
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:// 滑动停止
					mHandler.postDelayed(mRunnable, 1000); // 在Handler中执行子线程并延迟3s。
					break;

				default:
					break;
				}
			}
		
			private Runnable mRunnable = new Runnable() {
			  @Override
			  public void run() {
			   mHandler.sendEmptyMessage(1);
			  }
			 };
			 
			Handler mHandler = new Handler() {
			  @Override
			  public void handleMessage(Message msg) {
				  super.handleMessage(msg);
				  for (int i = 0; i < mListView.getChildCount(); i++) {
						View viewitem = mListView.getChildAt(i);
						ImageView iv_profile_image = (ImageView) viewitem
								.findViewById(R.id.iv_profile_image);
						if (iv_profile_image != null) {
							asyncTaskImageDownload.setViewImage(
									iv_profile_image, iv_profile_image.getTag()
											.toString());
						}
					}
					isInit = false;
			  }
			 };

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

		});

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		WsMainActivity wsMainActivity = (WsMainActivity) getActivity();
		View v =inflater.inflate(R.layout.fragment_blog_list, container, false);
		//开始认证
		if (wsMainActivity.getwbKey().isEmpty()) {
			// 授权
			AuthWBUser();
		}
//		if (!wsMainActivity.getwbKey().isEmpty()) {
//			v = 
//		} else {
//			v = inflater.inflate(R.layout.fragment_blog_list_view, container,
//					false);
//		}
		return v;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// SSO 授权回调
		// 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	private void AuthWBUser() {
		// 创建微博实例
		WeiboAuth mWeiboAuth = new WeiboAuth(getActivity(), Constants.APP_KEY,
				Constants.REDIRECT_URL, Constants.SCOPE);
		mSsoHandler = new SsoHandler(getActivity(), mWeiboAuth);
		mSsoHandler.authorize(new AuthListener());

	}

	private void getExistwbJson() {
		map = new HashMap<String, String>();
		String url = "https://api.weibo.com/2/statuses/home_timeline.json";
		map.put("access_token", mAccessToken.getToken());
		AnsynHttpRequest.requestByGet(getActivity(), url, callbackData,
				C.http.http_area, map, false, false);
	}

	/**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int url) {
			switch (url) {
			case C.http.http_area: // 进行数据解析
				if (data == null) {
					return;
				}
				Message msg = new Message();
				msg.what = 1;
				msg.obj = data;
				mHandler.sendMessage(msg);
				break;
			default:
				msg = new Message();
				msg.what = url;
				msg.obj = data;
				mHandler.sendMessage(msg);
				break;
			}
		}
	};

	/**
	 * 处理UI线程中数据
	 */
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				adapter = new BlogFragmentAdapter(getActivity(),
						msg.obj.toString(), isInit, asyncTaskImageDownload);
				initList();
				Toast.makeText(getActivity(), "刷新成功！",
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				break;
			default:
				Toast.makeText(getActivity(), "测试数据 数据编号：" + msg.what,
						Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};

	
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("1233456689");
	}
	
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				//start = ++refreshCnt;
				WsMainActivity wsMainActivity = (WsMainActivity) getActivity();
				if (!wsMainActivity.getwbKey().isEmpty()) {
					getExistwbJson();
				}
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				adapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}
	
	/**
	 * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
	 * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
	 * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
	 * SharedPreferences 中。
	 */
	class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			// 从 Bundle 中解析 Token
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				AccessTokenKeeper.writeAccessToken(getActivity(), mAccessToken);
				WsMainActivity wsMainActivity = (WsMainActivity) getActivity();

				Toast.makeText(getActivity(),
						R.string.weibosdk_demo_toast_auth_success,
						Toast.LENGTH_LONG).show();
				if (wsMainActivity.getwbKey().isEmpty()) {
					getExistwbJson();
				}
				wsMainActivity.setwbKey(AccessTokenKeeper.readAccessToken(
						getActivity()).getToken());
			} else {
				// 以下几种情况，您会收到 Code：
				// 1. 当您未在平台上注册的应用程序的包名与签名时；
				// 2. 当您注册的应用程序包名与签名不正确时；
				// 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
				String code = values.getString("code");
				String message = getString(R.string.weibosdk_demo_toast_auth_failed);
				if (!TextUtils.isEmpty(code)) {
					message = message + "\nObtained the code: " + code;
				}
				Toast.makeText(getActivity(), message, Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void onCancel() {
			Toast.makeText(getActivity(),
					R.string.weibosdk_demo_toast_auth_canceled,
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(getActivity(), "Auth exception : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}

	


}
