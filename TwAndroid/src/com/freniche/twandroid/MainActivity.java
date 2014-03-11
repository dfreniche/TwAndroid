package com.freniche.twandroid;

import java.util.ArrayList;
import java.util.List;

import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.freniche.twitter.ConnectTwitter;

public class MainActivity extends SherlockActivity {
	private static final String TAG = "T4JSample";

	private Uri mUri;
	private boolean running = false;

	ListView mListView;
	TweetsArrayAdapter mAdapter;
	ArrayList<Status> mTweets;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTweets = new ArrayList<Status>();

		mListView = (ListView)findViewById(R.id.listview);
		mListView.setEmptyView(findViewById(R.id.emptyMessage));

		mAdapter = new TweetsArrayAdapter(this, mTweets);
		mListView.setAdapter(mAdapter);

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
		if (item.getItemId() == R.id.menu_search) {
			Log.w("", "Home");
			Intent intent = new Intent(this, SearchActivity.class);
			startActivity(intent);
			
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
			
			Intent intent = new Intent(this, MentionsActivity.class);
			startActivity(intent);
			
			return true;
		}

		if (item.getItemId() == R.id.menu_dm) {
			Log.w("", "DMs");
			
			Intent intent = new Intent(this, DirectMessagesActivity.class);
			startActivity(intent);
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
		Globals.getTwitterStream().shutdown();
	}

	public void startStreamingTimeline() {
		if (running) {
			return;
		}
		
		
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
			public void onStatus(final Status status) {
				final String tweet = "@" + status.getUser().getScreenName() + " : " + status.getText() + "\n"; 
				Log.w(getPackageName(), tweet);
				//mTweets.add(status);
				
				mListView.post(new Runnable() {

			        @Override
			        public void run() {
			        	mAdapter.add(status);
			        	mAdapter.notifyDataSetChanged();
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
			running = true;

			Globals.getTwitterStream().addListener(listener);
			Globals.getTwitterStream().user();
		} else {
			Toast.makeText(getApplicationContext(), getString(R.string.twitter_not_connected), Toast.LENGTH_LONG).show();
		}

	}


	public class TweetsArrayAdapter extends ArrayAdapter<Status> {
		private final Context context;
		private ArrayList<Status> values;

		public TweetsArrayAdapter(Context context, ArrayList<Status> values) {
			super(context, R.layout.tweet_row_layout, values);
			this.context = context;
			this.values = values;
		}
		
		public TweetsArrayAdapter(Context context, List<Status> values) {
			super(context, R.layout.tweet_row_layout, values);
			this.context = context;
			this.values = (ArrayList<Status>) values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.tweet_row_layout, parent, false);
			TextView textView = (TextView) rowView.findViewById(R.id.label);
			ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
			textView.setText(values.get(position).getUser().getName()+ " "+ values.get(position).getText());
			// change the icon for Windows and iPhone
			//imageView.setImageURI(new Uri());

			return rowView;
		}
		/*
		@Override
		public void add(Status newStatus) {
		    values.add(newStatus);
		    notifyDataSetChanged();
		}

		@Override
		public void addAll(Collection<? extends Status> newDailyDatas) {
		    values.addAll(newDailyDatas);
		    notifyDataSetChanged();
		}

		@Override
		public void insert(Status newDailyData, int index) {
			values.add(index, newDailyData);
		    notifyDataSetChanged();
		}

		@Override
		public void remove(Status newDailyData) {
			values.remove(newDailyData);
		    notifyDataSetChanged();
		}

		@Override
		public void clear() {
			values.clear();
		    notifyDataSetChanged();
		}
		*/
	} 




}
