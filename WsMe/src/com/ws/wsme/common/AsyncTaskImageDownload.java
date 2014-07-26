package com.ws.wsme.common;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;


public  class AsyncTaskImageDownload {

	public HashMap<String,Bitmap> hashMap=new HashMap<String, Bitmap>();
	
	
	public  void setViewImage(ImageView v, String value) {
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
}

