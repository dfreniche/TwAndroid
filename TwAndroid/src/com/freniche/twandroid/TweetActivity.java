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
		}
		mTweetText = (EditText)findViewById(R.id.text_tweet);

		mSendTweetButton = (Button)findViewById(R.id.button_send_tweet);
		mSendTweetButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				TwitterListener listener = new TwitterAdapter() {
					@Override public void updatedStatus(Status status) {
						System.out.println("Successfully updated the status to [" +
								status.getText() + "].");
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
