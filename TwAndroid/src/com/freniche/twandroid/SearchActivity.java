package com.freniche.twandroid;

import java.io.File;
import java.util.ArrayList;

import com.freniche.twandroid.MainActivity.TweetsArrayAdapter;
import com.freniche.twitter.TwitterHelper;

import twitter4j.AsyncTwitter;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SearchActivity extends Activity {
	TweetsArrayAdapter mAdapter;
	ArrayList<Status> mTweets;

	EditText mSearchText;
	Button mSearchButton;
	ListView mListResult;
	private TwitterHelper mTwitterHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		mListResult = (ListView)findViewById(R.id.listViewSearchResults);
		mListResult.setEmptyView(findViewById(R.id.emptyMessage));
		
		mTwitterHelper = new TwitterHelper(this);

		mSearchText = (EditText)findViewById(R.id.txtSearch);
		mSearchButton = (Button)findViewById(R.id.btnSearch);
		mSearchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String searchString = mSearchText.getText().toString();
				TwitterListener listener = new TwitterAdapter() {
					@Override
					public void searched(final QueryResult queryResult) {
						super.searched(queryResult);
						mSearchButton.post(new Runnable() {

							@Override
							public void run() {
								mAdapter = new MainActivity().new TweetsArrayAdapter(SearchActivity.this, queryResult.getTweets());
								mListResult.setAdapter(mAdapter);

							}
						});
					}

					@Override 
					public void onException(TwitterException te, twitter4j.TwitterMethod method) {
						te.printStackTrace();
					};
				};
				// The factory instance is re-useable and thread safe.

				AsyncTwitter asyncTwitter = mTwitterHelper.getAsyncTwitter();
				asyncTwitter.addListener(listener);
				Query qry = new Query(searchString);
				asyncTwitter.search(qry);

			}
		});
	}

	

}
