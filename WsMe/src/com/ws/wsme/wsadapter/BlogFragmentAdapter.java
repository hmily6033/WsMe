package com.ws.wsme.wsadapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ws.wsme.R;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BlogFragmentAdapter extends BaseAdapter {
	private LayoutInflater mInflater = null; 
	private Context mContext=null;
	private JSONArray mData;
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
        	holder.iv_profile_image.setImageURI(Uri.parse(jUserObject.getString("profile_image_url")));
        	holder.tv_name.setText(jUserObject.getString("name"));
        	//时间Start
        	String datestring=jObject.getString("created_at");
        	SimpleDateFormat dateformat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
        	Date date = dateformat.parse(datestring);
        	Date curDate = new Date(System.currentTimeMillis());
        	long diff = date.getTime() - curDate.getTime();
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
            holder.btn_comment.setText("评论：："+jObject.getString("comments_count").toString());
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
