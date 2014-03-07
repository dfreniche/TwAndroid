package com.freniche.twandroid;

import java.util.Map;

import twitter4j.AccountSettings;
import twitter4j.AsyncTwitter;
import twitter4j.Category;
import twitter4j.DirectMessage;
import twitter4j.Friendship;
import twitter4j.IDs;
import twitter4j.Location;
import twitter4j.OEmbed;
import twitter4j.PagableResponseList;
import twitter4j.Place;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.Relationship;
import twitter4j.ResponseList;
import twitter4j.SavedSearch;
import twitter4j.SimilarPlaces;
import twitter4j.Status;
import twitter4j.Trends;
import twitter4j.TwitterAPIConfiguration;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterListener;
import twitter4j.TwitterMethod;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.api.HelpResources.Language;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.freniche.twitter.ConnectTwitter;
import com.freniche.twitter.TwitterHelper;

public class ConfigActivity extends Activity {

	private Button buttonLogin;
	private Uri mUri;
	private TwitterHelper mTwitterHelper;

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
		
		mTwitterHelper = new TwitterHelper(this);

		
		AsyncTwitter asyncTwitter = mTwitterHelper.getAsyncTwitter();
		asyncTwitter.addListener(new TwitterAdapter() {
			String s = "";
			
			@Override
			public void gotAccountSettings(AccountSettings settings) {
				super.gotAccountSettings(settings);
				s += "ScreenName:" + settings.getScreenName();
				setTitle(s);
			}
			
			@Override
			public void gotRetweetsOfMe(ResponseList<Status> statuses) {
				super.gotRetweetsOfMe(statuses);
				s += "Statuses: "+ statuses.size();
				setTitle(s);
			}
			
		});
		asyncTwitter.getRetweetsOfMe();
		asyncTwitter.getAccountSettings();
		

	}
	
}
