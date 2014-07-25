package com.ws.wsme.wsadapter;

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

import com.ws.wsme.R;
import com.ws.wsme.AsynLoader.SyncImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BlogFragmentAdapter extends BaseAdapter {
	private LayoutInflater mInflater = null; 
	private Context mContext=null;
	private JSONArray mData;
	public HashMap<String,Bitmap> hashMap = new HashMap<String,Bitmap>();
	
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
        	
        	//得到可用的图片
        	String mUrl=jUserObject.getString("profile_image_url");
        	setViewImage(holder.iv_profile_image,mUrl);

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
	
	/**
     * 获取网落图片资源 
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url){
    	URL myFileURL;
    	Bitmap bitmap=null;
    	try{
    		myFileURL = new URL(url);
    		//获得连接
    		HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
    		//设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
    		conn.setConnectTimeout(6000);
    		//连接设置获得数据流
    		conn.setDoInput(true);
    		//不使用缓存
    		conn.setUseCaches(false);
    		//这句可有可无，没有影响
    		//conn.connect();
    		//得到数据流
    		InputStream is = conn.getInputStream();
    		//解析得到图片
    		bitmap = BitmapFactory.decodeStream(is);
    		//关闭数据流
    		is.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
		return bitmap;
    	
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
