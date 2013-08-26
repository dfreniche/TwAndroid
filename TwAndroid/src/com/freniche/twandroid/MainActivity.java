package com.freniche.twandroid;

import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.TwitterStream;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.freniche.twitter.TwitterHelper;

public class MainActivity extends SherlockActivity implements OnClickListener {
	private static final String TAG = "T4JSample";
	private static final Integer MODE_CONNECT_FIRST_TIME = 1;
	private static final Integer MODE_RECONNECT = 2;

	private Uri mUri;
	private Button buttonLogin;
	private Button getTweetButton;
	private TextView tweetText;
	private ScrollView scrollView;

	private static TwitterStream twitterStream;
	private boolean running = false;

	private TwitterHelper mTwitterHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTwitterHelper = new TwitterHelper(this);


		scrollView = (ScrollView)findViewById(R.id.scrollView);
		tweetText =(TextView)findViewById(R.id.tweetText);
		getTweetButton = (Button)findViewById(R.id.getTweet);
		getTweetButton.setOnClickListener(this);
		buttonLogin = (Button) findViewById(R.id.twitterLogin);
		buttonLogin.setOnClickListener(this);

		System.out.println("On create");

		/**
		 * Handle OAuth Callback
		 */
		mUri = getIntent().getData();
		(new ConnectTwitter()).execute(MODE_RECONNECT);


	}

	protected void onResume() {
		super.onResume();
		System.out.println("On resume");

		checkConnected();
	}

	private void checkConnected() {
		if (mTwitterHelper.isConnected()) {

			twitterStream = mTwitterHelper.createNewTwitterStream();

			buttonLogin.setText(R.string.label_disconnect);
			getTweetButton.setEnabled(true);
		} else {
			buttonLogin.setText(R.string.label_connect);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		if (item.getItemId() == R.id.menu_home) {
			Log.d("", "Home");
			return true;
		}

		if (item.getItemId() == R.id.menu_tweet) {
			Log.d("", "Tweet");
			return true;
		}
		
		if (item.getItemId() == R.id.menu_mentions) {
			Log.d("", "Mentions");
			return true;
		}
		
		if (item.getItemId() == R.id.menu_dm) {
			Log.d("", "DMs");
			return true;
		}
		
		if (item.getItemId() == R.id.menu_config) {
			Log.d("", "Config");
			return true;
		}
		
		return true;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.twitterLogin:
			if (mTwitterHelper.isConnected()) {
				mTwitterHelper.disconnectTwitter();
				buttonLogin.setText(R.string.label_connect);
			} else {
				(new ConnectTwitter()).execute(MODE_CONNECT_FIRST_TIME);
			}
			break;
		case R.id.getTweet:
			if (running) {
				stopStreamingTimeline();
				running = false;
				getTweetButton.setText("start streaming");
			} else {
				startStreamingTimeline();
				running = true;
				getTweetButton.setText("stop streaming");
			}
			break;
		}
	}

	private void stopStreamingTimeline() {
		twitterStream.shutdown();
	}

	public void startStreamingTimeline() {
		UserStreamListener listener = new UserStreamListener() {

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
				System.out.println("deletionnotice");
			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
				System.out.println("scrubget");
			}

			@Override
			public void onStatus(Status status) {
				final String tweet = "@" + status.getUser().getScreenName() + " : " + status.getText() + "\n"; 
				System.out.println(tweet);
				tweetText.post(new Runnable() {
					@Override
					public void run() {
						tweetText.append(tweet);
						scrollView.fullScroll(View.FOCUS_DOWN);
					}
				});
			}

			@Override
			public void onTrackLimitationNotice(int arg0) {
				System.out.println("trackLimitation");
			}

			@Override
			public void onException(Exception arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onBlock(User arg0, User arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onDeletionNotice(long arg0, long arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onDirectMessage(DirectMessage arg0) {
				// TODO Auto-generated method stub				
			}

			@Override
			public void onFavorite(User arg0, User arg1, Status arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFollow(User arg0, User arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFriendList(long[] arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onUnblock(User arg0, User arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onUnfavorite(User arg0, User arg1, Status arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onUserListCreation(User arg0, UserList arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onUserListDeletion(User arg0, UserList arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onUserListMemberAddition(User arg0, User arg1, UserList arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onUserListMemberDeletion(User arg0, User arg1,  UserList arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onUserListSubscription(User arg0, User arg1, UserList arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onUserListUnsubscription(User arg0, User arg1, UserList arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onUserListUpdate(User arg0, UserList arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onUserProfileUpdate(User arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub

			}
		};
		twitterStream.addListener(listener);
		twitterStream.user();
	}

	private class ConnectTwitter extends AsyncTask<Integer, Void, Void> {
		@Override

		protected Void doInBackground(Integer... mode) {
			if (mode[0] == MODE_CONNECT_FIRST_TIME) {
				mTwitterHelper.askOAuth(MainActivity.this);
			} else if (mode[0] == MODE_RECONNECT) {
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
