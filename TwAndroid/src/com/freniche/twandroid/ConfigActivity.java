package com.freniche.twandroid;

import twitter4j.AccountSettings;
import twitter4j.AsyncTwitter;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.freniche.twitter.ConnectTwitter;
import com.freniche.twitter.TwitterHelper;

public class ConfigActivity extends Activity {

	private Button buttonLogin;
	private Uri mUri;
	private TwitterHelper mTwitterHelper;
	TextView mLabelInfo;
	RadioGroup mThemeSelector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(Globals.defaultTheme);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);
		
		/**
		 * Handle OAuth Callback
		 */
		mUri = getIntent().getData();
		if (mUri != null) {
			(new ConnectTwitter(this, mUri)).execute(Globals.MODE_RECONNECT);
		}
		
		mLabelInfo = (TextView)findViewById(R.id.labelInfo);
		
		buttonLogin = (Button) findViewById(R.id.twitterLogin);
		buttonLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Globals.getSharedTwitterHelper(getBaseContext()).isConnected()) {
					Globals.getSharedTwitterHelper(getBaseContext()).disconnectTwitter();
					buttonLogin.setText(R.string.label_connect);
				} else {
					(new ConnectTwitter(ConfigActivity.this, mUri)).execute(Globals.MODE_CONNECT_FIRST_TIME);
					buttonLogin.setText(R.string.label_disconnect);

					Log.w("","firstTime");

				}				
			}
		});
		
		mTwitterHelper = new TwitterHelper(this);

		
		if (Globals.getSharedTwitterHelper(this).isConnected()) {
			buttonLogin.setText(R.string.label_disconnect);
			
			
			AsyncTwitter asyncTwitter = mTwitterHelper.getAsyncTwitter();
			asyncTwitter.addListener(new TwitterAdapter() {
				String s = "";
				
				@Override
				public void gotAccountSettings(AccountSettings settings) {
					super.gotAccountSettings(settings);
					s += "ScreenName:" + settings.getScreenName();
					mLabelInfo.post(new Runnable() {
				        @Override
				        public void run() {
							mLabelInfo.setText(s);
						}
				    });
				}
				
				@Override
				public void gotRetweetsOfMe(ResponseList<Status> statuses) {
					super.gotRetweetsOfMe(statuses);
					s += "Statuses: "+ statuses.size();
					mLabelInfo.post(new Runnable() {
				        @Override
				        public void run() {
							mLabelInfo.setText(s);
						}
				    });
				}
				
			});
			asyncTwitter.getRetweetsOfMe();
			asyncTwitter.getAccountSettings();
			
		} else {
			buttonLogin.setText(R.string.label_connect);

		}
		
		

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		mThemeSelector = (RadioGroup)findViewById(R.id.themeGroup);
		mThemeSelector.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@TargetApi(19)
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				Log.d("", "Theme changed");
				if (checkedId == R.id.themeLigth) {
				    getApplication().setTheme(R.style.LightTheme);
				    Globals.defaultTheme = R.style.LightTheme;
				} else if (checkedId == R.id.themeDark)  {
				    getApplication().setTheme(R.style.BlackTheme);
					Globals.defaultTheme = R.style.BlackTheme;

				} else {
				    getApplication().setTheme(R.style.AppTheme);
					Globals.defaultTheme = R.style.AppTheme;				
				}
				if (android.os.Build.VERSION.SDK_INT > 11) {
					recreate();
				}
			}
		});
	}
	
}
