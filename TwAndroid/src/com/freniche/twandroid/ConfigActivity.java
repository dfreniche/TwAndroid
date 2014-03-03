package com.freniche.twandroid;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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
		(new ConnectTwitter()).execute(Globals.MODE_RECONNECT);

		
		buttonLogin = (Button) findViewById(R.id.twitterLogin);
		buttonLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Globals.getSharedTwitterHelper(getBaseContext()).isConnected()) {
					Globals.getSharedTwitterHelper(getBaseContext()).disconnectTwitter();
					buttonLogin.setText(R.string.label_connect);
				} else {
					(new ConnectTwitter()).execute(Globals.MODE_CONNECT_FIRST_TIME);
				}				
			}
		});
	}

	private void checkConnected() {
		if (Globals.getSharedTwitterHelper(this).isConnected()) {

			Globals.twitterStream = Globals.getSharedTwitterHelper(this).createNewTwitterStream();

		} 
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.config, menu);
		return true;
	}

	public class ConnectTwitter extends AsyncTask<Integer, Void, Void> {
		@Override

		protected Void doInBackground(Integer... mode) {
			if (mode[0] == Globals.MODE_CONNECT_FIRST_TIME) {
				Globals.getSharedTwitterHelper(getBaseContext()).askOAuth(ConfigActivity.this);
			} else if (mode[0] == Globals.MODE_RECONNECT) {
				Globals.getSharedTwitterHelper(getBaseContext()).handleOauthCallback(mUri);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			checkConnected();

		}
	}
	
}
