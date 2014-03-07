package com.freniche.twandroid;

import java.io.InputStream;
import java.util.ArrayList;

import twitter4j.AsyncTwitter;
import twitter4j.DirectMessage;
import twitter4j.ResponseList;
import twitter4j.TwitterAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DirectMessagesActivity extends Activity {

	ListView mListView;
	DirectMessagesArrayAdapter mAdapter;
	ArrayList<DirectMessage> mDirectMessages;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_direct_messages);
		
		
		
		mListView = (ListView)findViewById(R.id.listview);
		mListView.setEmptyView(findViewById(R.id.emptyMessage));
		
		AsyncTwitter asyncTwitter = Globals.getSharedTwitterHelper(getApplicationContext()).getAsyncTwitter();
		asyncTwitter.addListener(new TwitterAdapter() {
			String s = "";
			
			@Override
			public void gotDirectMessages(ResponseList<DirectMessage> messages) {
				super.gotDirectMessages(messages);
				
				mDirectMessages = new ArrayList<DirectMessage>();
				for (DirectMessage dm: messages) {
					mDirectMessages.add(dm);
				}
				
				mListView.post(new Runnable() {

			        @Override
			        public void run() {
			        	mAdapter = new DirectMessagesArrayAdapter(DirectMessagesActivity.this, mDirectMessages);
						mListView.setAdapter(mAdapter);			        	
						mAdapter.notifyDataSetChanged();
					}
			    });
				
			}
			
			
		});
		asyncTwitter.getDirectMessages();
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.direct_messages, menu);
		return true;
	}

	public class DirectMessagesArrayAdapter extends ArrayAdapter<DirectMessage> {
		private final Context context;
		private ArrayList<DirectMessage> values;

		public DirectMessagesArrayAdapter(Context context, ArrayList<DirectMessage> values) {
			super(context, R.layout.tweet_row_layout, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.dm_row_layout, parent, false);
			TextView sender = (TextView) rowView.findViewById(R.id.labelSender);
			TextView dm = (TextView) rowView.findViewById(R.id.labelDM);

			ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
			new DownloadImageTask(imageView).execute(values.get(position).getSender().getMiniProfileImageURL());
			
			sender.setText(values.get(position).getSender().getName());
			dm.setText(values.get(position).getText());
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
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		  ImageView bmImage;

		  public DownloadImageTask(ImageView bmImage) {
		      this.bmImage = bmImage;
		  }

		  protected Bitmap doInBackground(String... urls) {
		      String urldisplay = urls[0];
		      Bitmap mIcon11 = null;
		      try {
		        InputStream in = new java.net.URL(urldisplay).openStream();
		        mIcon11 = BitmapFactory.decodeStream(in);
		      } catch (Exception e) {
		          Log.e("Error", e.getMessage());
		          e.printStackTrace();
		      }
		      return mIcon11;
		  }

		  protected void onPostExecute(Bitmap result) {
		      bmImage.setImageBitmap(result);
		  }
		}

}
