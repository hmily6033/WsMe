package  com.ws.wsme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * 
 * @author LCL
 * 
 */
public class HomeMainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				
				Intent intent = new Intent(HomeMainActivity.this, WsMainActivity.class);
				startActivity(intent);
				HomeMainActivity.this.finish();
			}
		}, 3000);
	}
}
