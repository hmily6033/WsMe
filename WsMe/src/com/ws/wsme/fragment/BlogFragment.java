package com.ws.wsme.fragment;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.ws.wsme.AccessTokenKeeper;
import com.ws.wsme.Constants;
import com.ws.wsme.R;
import com.ws.wsme.WsMainActivity;
import com.ws.wsme.AsynLoader.SyncImageLoader;
import com.ws.wsme.http.common.C;
import com.ws.wsme.http.http.AnsynHttpRequest;
import com.ws.wsme.http.http.ObserverCallBack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BlogFragment extends ListFragment {

	private Oauth2AccessToken mAccessToken;
	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;
    public Map<String, String> map;
 // 判断是否是初始化
    private boolean isInit = false;
    private ListView list;
    private int start_index, end_index;
    private BlogFragmentAdapter adapter = null; 
    //微博json
    private JSONObject mjJsonObject;
    public HashMap<String,Bitmap> hashMap = new HashMap<String,Bitmap>();
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//授权按钮
		
		Button btnAuthButton=(Button)getActivity().findViewById(R.id.obtain_token_via_sso);
		if(btnAuthButton==null)
			return;
		btnAuthButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//授权
				AuthWBUser();
			}
		});
	}
	
	
    
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);
        WsMainActivity wsMainActivity=(WsMainActivity)getActivity();
        
        if (!wsMainActivity.getwbKey().isEmpty()) {
        	getExistwbJson();  
		}
         
    }  
  
    public void initList() {
        list = (ListView) getActivity().findViewById(android.R.id.list);
        list.setAdapter(adapter);
        list.setOnScrollListener(new OnScrollListener() {
 
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                isInit = true;
                switch (scrollState) {
                case OnScrollListener.SCROLL_STATE_IDLE:// 滑动停止
                    for (int i = 0; i < list.getChildCount(); i++) {
							View viewitem=list.getChildAt(i);
							ImageView iv_profile_image = (ImageView) viewitem.findViewById(R.id.iv_profile_image); 
							if (iv_profile_image!=null) {
						    	setViewImage(iv_profile_image,iv_profile_image.getTag().toString());
							}
                    }
                    isInit=false;
                    break;
 
                default:
                    break;
                }
            }


			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
                // 设置当前屏幕显示的起始index和结束index
                start_index = firstVisibleItem;
                end_index = firstVisibleItem + visibleItemCount;
			}

        });
 
    }
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {
    	WsMainActivity wsMainActivity=(WsMainActivity)getActivity();
    	 View v=null;
        if (!wsMainActivity.getwbKey().isEmpty()) {
	        v = inflater.inflate(R.layout.fragment_blog_list, container, false);  
        }
        else {
			v=inflater.inflate(R.layout.fragment_blog_list_view, container, false);
		}
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
		WeiboAuth mWeiboAuth= new WeiboAuth(getActivity(), Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
		mSsoHandler = new SsoHandler(getActivity(), mWeiboAuth);
        mSsoHandler.authorize(new AuthListener());
		
	}
	private void getExistwbJson()
    {
    	map = new HashMap<String, String>();
		String url ="https://api.weibo.com/2/statuses/home_timeline.json";
		map.put("access_token", mAccessToken.getToken());
		AnsynHttpRequest.requestByGet(getActivity(),url,callbackData,C.http.http_area, map, false, true);
    }
    /**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callbackData = new ObserverCallBack(){
		public void back(String data, int url) {
			switch (url) {
			case C.http.http_area: // 进行数据解析
				if(data == null) {
					return;
				}
				Message msg = new Message();
				msg.what = 1;
				msg.obj=data;
				mHandler.sendMessage(msg);
				break;
			default:
				msg = new Message();
				msg.what = url;
				msg.obj=data;
				mHandler.sendMessage(msg);
				break;
			}
		}
	};
	
	/**
	 *  处理UI线程中数据
	 */
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				try {
					mjJsonObject=new JSONObject(msg.obj.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				adapter = new BlogFragmentAdapter (getActivity(),msg.obj.toString());  
	            initList();
				Toast.makeText(getActivity(), "测试数据 数据编号："+msg.what, Toast.LENGTH_SHORT).show();
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
				Toast.makeText(getActivity(), "测试数据 数据编号："+msg.what, Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};
	
    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     *    该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {
        
        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(getActivity(), mAccessToken);
                WsMainActivity wsMainActivity=(WsMainActivity)getActivity();
                
                Toast.makeText(getActivity(), 
                        R.string.weibosdk_demo_toast_auth_success, Toast.LENGTH_LONG).show();
                if (wsMainActivity.getwbKey().isEmpty()) {
                	getExistwbJson();
                }
                wsMainActivity.setwbKey(AccessTokenKeeper.readAccessToken(getActivity()).getToken());
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
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }

        
        @Override
        public void onCancel() {
            Toast.makeText(getActivity(), 
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(getActivity(), 
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void setViewImage(ImageView v, String value) {
		if (hashMap.containsKey(value)) {
			v.setImageBitmap(hashMap.get(value)); 
			return;
		}
	    new ImageDownloadTask().execute(value, v);  
	}  
	
	private class ImageDownloadTask extends AsyncTask<Object, Object, Bitmap>{  
	        private ImageView imageView = null;  
	        private String urlString;
	        @Override  
	        protected Bitmap doInBackground(Object... params) {  
	            // TODO Auto-generated method stub  
	            Bitmap bmp = null;  
	            urlString=params[0].toString();
	            imageView = (ImageView) params[1];  
	            try {  
	                bmp = BitmapFactory.decodeStream(new URL((String)params[0]).openStream());  
	            } catch (MalformedURLException e) {  
	                // TODO Auto-generated catch block  
	                e.printStackTrace();  
	            } catch (IOException e) {  
	                // TODO Auto-generated catch block  
	                e.printStackTrace();  
	            }  
	            return bmp;  
	        }  
	          
	        protected void onPostExecute(Bitmap result){  
	            imageView.setImageBitmap(result); 
	            hashMap.put(urlString, result);
	        }  
	    }  
   class BlogFragmentAdapter extends BaseAdapter {
    	private LayoutInflater mInflater = null; 
    	private Context mContext=null;
    	private JSONArray mData;
    	
    	
    	SyncImageLoader syncImageLoader;

        public BlogFragmentAdapter(Context context,String data){  
            super();  
            mInflater = (LayoutInflater) context  
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
            mContext=context;
            try {
    			mData=new JSONObject(data).getJSONArray("statuses");
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }  
        
    	@Override
    	public int getCount() {
    		// TODO Auto-generated method stub
    		return mData.length()-1;
    	}

    	@Override
    	public Object getItem(int arg0) {
    		// TODO Auto-generated method stub
    		return null;
    	}

    	@Override
    	public long getItemId(int arg0) {
    		// TODO Auto-generated method stub
    		return arg0;
    	}

    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		
    		ViewHolder holder = null;    
            if (convertView == null) {    
                holder = new ViewHolder();    
                convertView = mInflater.inflate(R.layout.sina_weibo_list_item, null);  
                holder.iv_profile_image = (ImageView) convertView.findViewById(R.id.iv_profile_image);    
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);    
                holder.tv_created_at = (TextView) convertView.findViewById(R.id.tv_created_at); 
                holder.tv_text = (TextView) convertView.findViewById(R.id.tv_text); 
                holder.tv_source = (TextView) convertView.findViewById(R.id.tv_source); 
                holder.btn_retweet = (Button) convertView.findViewById(R.id.btn_retweet);    
      
                holder.btn_comment = (Button) convertView.findViewById(R.id.btn_comment);    
                holder.btn_good = (Button) convertView.findViewById(R.id.btn_good);    
                convertView.setTag(holder);    
            } else {   
                holder = (ViewHolder) convertView.getTag();    
            }  
      
            
            JSONObject jObject = null;
            try {
            	jObject=mData.getJSONObject(position);
            	JSONObject jUserObject=jObject.getJSONObject("user");
            	String mIdString=jObject.getString("id");
            	String mUrl=jUserObject.getString("profile_image_url");
            	holder.iv_profile_image.setTag(mUrl);
            	if (!isInit) {
            		
                	setViewImage(holder.iv_profile_image,mUrl);
                }
            	else {
            		holder.iv_profile_image.setImageResource(R.drawable.switchuser);
				}
            	

            	holder.tv_name.setText(jUserObject.getString("name"));
            	//时间Start
            	String datestring=jObject.getString("created_at");
            	SimpleDateFormat dateformat = new SimpleDateFormat("EEE MMM dd HH:mm:ss +0800 yyyy",Locale.ENGLISH);
            	Date date = dateformat.parse(datestring);
                SimpleDateFormat dateformatnew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date datenew=dateformatnew.parse(dateformatnew.format(date));
            	Date curDate = new Date(System.currentTimeMillis());
            	long diff = datenew.getTime() - curDate.getTime();
            	long days = diff / (1000 * 60 * 60 * 24);
            	long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
            	long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
            	if(minutes<=0)
            	{
            		holder.tv_created_at.setText(Math.abs(minutes)+"分钟前");
            	}
            	else
            		holder.tv_created_at.setText(Math.abs(minutes)+"分钟");
            	
            	//时间end
            	
                holder.tv_text.setText(jObject.getString("text").toString()); 
                
                holder.tv_source.setText(jObject.getString("source").toString());
                
                holder.btn_retweet.setText("转发："+jObject.getString("reposts_count").toString());
                holder.btn_comment.setText("评论："+jObject.getString("comments_count").toString());
                holder.btn_good.setText("点赞："+jObject.getString("attitudes_count").toString());
                
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (ParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            return convertView;  
        } 
    	
    	
    	

    	class ViewHolder {
    		/**
    		 * 头像
    		 */
    	    public ImageView iv_profile_image;
    	    /**
    		 * 名字
    		 */
    	    public TextView tv_name;
    	    
    	    /**
    		 * 内容
    		 */
    	    public TextView tv_text;
    	    /**
    		 * 来源
    		 */
    	    public TextView tv_source;
    	    
    	    /**
    		 *创建时间
    		 */
    	    public TextView tv_created_at;
    	    
    	    /**
    		 * 转发
    		 */
    	    public Button btn_retweet;
    	    
    	    /**
    		 * 评论
    		 */
    	    public Button btn_comment;
    	    
    	    /**
    		 * 赞
    		 */
    	    public Button btn_good; 
    	}  
    	
    }
}
