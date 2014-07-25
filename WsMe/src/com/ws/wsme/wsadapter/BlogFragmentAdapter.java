package com.ws.wsme.wsadapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ws.wsme.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
            convertView = mInflater.inflate(R.layout.fragment_blog_listitem, null);  
            holder.blog_title_left_pic = (ImageView) convertView.findViewById(R.id.blog_title_left_pic);    
            holder.blog_up_text = (TextView) convertView.findViewById(R.id.blog_up_text);    
            holder.blog_down_text = (TextView) convertView.findViewById(R.id.blog_down_text);    
            holder.blog_title_right_pic = (ImageView) convertView.findViewById(R.id.blog_title_right_pic);    
  
            convertView.setTag(holder);    
        } else {   
            holder = (ViewHolder) convertView.getTag();    
        }  
  
        holder.blog_title_left_pic.setImageResource(R.drawable.ic_launcher); 
        JSONObject jObject = null;
        try {
        	jObject=mData.getJSONObject(position);
        	holder.blog_up_text.setText(jObject.getString("id").toString());  
            holder.blog_down_text.setText(jObject.getString("reposts_count").toString()); 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
        holder.blog_title_right_pic.setImageResource(R.drawable.ic_launcher);  
        holder.blog_title_right_pic.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(mContext, "dd", Toast.LENGTH_LONG);
				//System.out.println("Click on the speaker image on ListItem ");  
			}
		});

        return convertView;  
    }    
	
	class ViewHolder {    
	    public ImageView blog_title_left_pic;  
	    public TextView blog_up_text;  
	    public TextView blog_down_text;  
	    public ImageView blog_title_right_pic;    
	}  
	
}
