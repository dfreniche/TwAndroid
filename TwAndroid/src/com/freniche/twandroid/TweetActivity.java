package com.freniche.twandroid;

import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.freniche.twitter.TwitterHelper;

public class TweetActivity extends SherlockActivity {

	private Button mSendTweetButton;
	private EditText mTweetText;
	private Twitter mTwitter;
	private TwitterHelper mTwitterHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet);
		mTwitterHelper = new TwitterHelper(this);

		if (mTwitterHelper.isConnected()) {
			mTwitter = mTwitterHelper.getTwitter();
		} else {
			finish();
			Toast.makeText(getApplicationContext(), R.string.twitter_not_connected, Toast.LENGTH_LONG).show();
			
		}
		mTweetText = (EditText)findViewById(R.id.text_tweet);

		mSendTweetButton = (Button)findViewById(R.id.button_send_tweet);
		mSendTweetButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				/*
				try {
					Globals.getSharedTwitterHelper(getApplicationContext()).getTwitter().updateStatus(mTweetText.getText().toString());
					Toast.makeText(getApplicationContext(), "Successfully updated the status to [" +
							mTweetText.getText().toString() + "].", Toast.LENGTH_SHORT).show();

				} catch (TwitterException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
							*/	
				
				TwitterListener listener = new TwitterAdapter() {
					@Override public void updatedStatus(final Status status) {
						finish();
						mSendTweetButton.post(new Runnable() {
							
							@Override
							public void run() {
								Toast.makeText(getApplicationContext(), "Successfully updated the status to [" +
										status.getText() + "].", Toast.LENGTH_SHORT).show();
								finish();
							}
						});
						
					}

					public void onException(TwitterException e, int method) {

					}
				};
				// The factory instance is re-useable and thread safe.
				
				AsyncTwitter asyncTwitter = mTwitterHelper.getAsyncTwitter();
				asyncTwitter.addListener(listener);
				asyncTwitter.updateStatus(mTweetText.getText().toString());
			

			}
		});
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
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
}
