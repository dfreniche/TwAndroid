package com.freniche.twandroid;

import com.freniche.twandroid.MainActivity.ConnectTwitter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ConfigActivity extends Activity {

	private Button buttonLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);
		
		buttonLogin = (Button) findViewById(R.id.twitterLogin);
		buttonLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Globals.twitterHelper.isConnected()) {
					Globals.twitterHelper.disconnectTwitter();
					buttonLogin.setText(R.string.label_connect);
				} else {
					(new ConnectTwitter()).execute(Globals.MODE_CONNECT_FIRST_TIME);
				}				
			}
		});
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
				mTwitterHelper.askOAuth(MainActivity.this);
			} else if (mode[0] == Globals.MODE_RECONNECT) {
				mTwitterHelper.handleOauthCallback(mUri);
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
