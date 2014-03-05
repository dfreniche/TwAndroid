package com.freniche.twandroid;

import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.freniche.twitter.ConnectTwitter;

public class MainActivity extends SherlockActivity {
	private static final String TAG = "T4JSample";

	private TextView tweetText;
	private ScrollView scrollView;
	private Uri mUri;

	private boolean running = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		scrollView = (ScrollView)findViewById(R.id.scrollView);
		tweetText =(TextView)findViewById(R.id.tweetText);
		

		Log.w("", "On create");

		/**
		 * Handle OAuth Callback
		 */
		mUri = getIntent().getData();
		(new ConnectTwitter(this, mUri)).execute(Globals.MODE_RECONNECT);

		
	}

	protected void onResume() {
		super.onResume();
		Log.w(getPackageName(), "On resume");

		startStreamingTimeline();

	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		if (item.getItemId() == R.id.menu_home) {
			Log.w("", "Home");
			return true;
		}

		if (item.getItemId() == R.id.menu_tweet) {
			Log.w("", "Tweet");
			
			Intent intent = new Intent(this, TweetActivity.class);
			startActivity(intent);
			
			return true;
		}
		
		if (item.getItemId() == R.id.menu_mentions) {
			Log.w("", "Mentions");
			return true;
		}
		
		if (item.getItemId() == R.id.menu_dm) {
			Log.w("", "DMs");
			return true;
		}
		
		if (item.getItemId() == R.id.menu_config) {
			Log.w("", "Config");
			
			Intent intent = new Intent(this, ConfigActivity.class);
			startActivity(intent);
			
			return true;
		}
		
		return true;
	}
	/*
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
*/
	private void stopStreamingTimeline() {
		Globals.twitterStream.shutdown();
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
				Log.w(getPackageName(), tweet);
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
		
		if (Globals.getSharedTwitterHelper(getApplicationContext()).isConnected()) {
			Globals.twitterStream.addListener(listener);
			Globals.twitterStream.user();
		} else {
			Toast.makeText(getApplicationContext(), getString(R.string.twitter_not_connected), Toast.LENGTH_LONG).show();
		}
		
	}

	

}
