package com.freniche.twandroid;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.freniche.twitter.ConnectTwitter;

public class ConfigActivity extends Activity {

	private Button buttonLogin;
	private Uri mUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);
		
		/**
		 * Handle OAuth Callback
		 */
		mUri = getIntent().getData();
		(new ConnectTwitter(this, mUri)).execute(Globals.MODE_RECONNECT);

		
		buttonLogin = (Button) findViewById(R.id.twitterLogin);
		buttonLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Globals.getSharedTwitterHelper(getBaseContext()).isConnected()) {
					Globals.getSharedTwitterHelper(getBaseContext()).disconnectTwitter();
					buttonLogin.setText(R.string.label_connect);
				} else {
					(new ConnectTwitter(ConfigActivity.this, mUri)).execute(Globals.MODE_CONNECT_FIRST_TIME);
					Log.w("","firstTime");

				}				
			}
		});
	}
	
}
