package com.ws.wsme.wsadapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ws.wsme.R;
import com.ws.wsme.common.AsyncTaskImageDownload;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * @author LCL
 *
 */
public class BlogFragmentAdapter extends BaseAdapter {
	private LayoutInflater mInflater = null; 
	private JSONArray mData;
	private boolean isInit;
	private AsyncTaskImageDownload mAsyncTaskImageDownload;
	private ViewHolder holder = null;    
    public BlogFragmentAdapter(Context context,String data,boolean isinit,AsyncTaskImageDownload asyncTaskImageDownload){  
        super();  
        mInflater = (LayoutInflater) context  
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        isInit=isinit;
        mAsyncTaskImageDownload=asyncTaskImageDownload;
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
		return mData.length();
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
		
		
        if (convertView == null) {    
            holder = new ViewHolder();    
            convertView = mInflater.inflate(R.layout.sina_weibo_list_item, null);  
            holder.iv_profile_image = (ImageView) convertView.findViewById(R.id.iv_profile_image);    
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);    
            holder.tv_created_at = (TextView) convertView.findViewById(R.id.tv_created_at); 
            holder.tv_text = (TextView) convertView.findViewById(R.id.tv_text); 
            holder.tv_source = (TextView) convertView.findViewById(R.id.tv_source); 
            holder.tv_retweet = (TextView) convertView.findViewById(R.id.tv_retweet);    
            holder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);    
            holder.tv_good = (TextView) convertView.findViewById(R.id.tv_good);    
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
        		//mHandler.postDelayed(mRunnable, 2000);
        		mAsyncTaskImageDownload.setViewImage(holder.iv_profile_image,holder.iv_profile_image.getTag().toString());
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
        	if(minutes<0)
        	{
        		holder.tv_created_at.setText(Math.abs(minutes)+"分钟前");
        	}
        	else if(minutes>=0 || minutes<1)
        		holder.tv_created_at.setText("刚刚");
        	else
        		holder.tv_created_at.setText(Math.abs(minutes)+"分钟");
        	
        	//时间end
        	//微博正文内容
            holder.tv_text.setText(jObject.getString("text").toString()); 
            
            
            String s_sourceString=jObject.getString("source").toString();
            String s_leftString = s_sourceString.substring(0,s_sourceString.indexOf("</a>"));
            String s_content = s_leftString.substring(s_leftString.indexOf(">") + 1);
            holder.tv_source.setText("来自:"+s_content);
            
            holder.tv_retweet.setText(" "+jObject.getString("reposts_count").toString());
            holder.tv_comment.setText(" "+jObject.getString("comments_count").toString());
            holder.tv_good.setText(" "+jObject.getString("attitudes_count").toString());
            
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return convertView;  
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
			  mAsyncTaskImageDownload.setViewImage(holder.iv_profile_image,holder.iv_profile_image.getTag().toString());
		  }
		 };
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
	    public TextView tv_retweet;
	    
	    /**
		 * 评论
		 */
	    public TextView tv_comment;
	    
	    /**
		 * 赞
		 */
	    public TextView tv_good; 
	}  

}
