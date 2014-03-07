package com.freniche.twitter;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.freniche.twandroid.Globals;

public class ConnectTwitter extends AsyncTask<Integer, Void, Void> {
	private Activity context;
	private Uri mUri;
	
	public ConnectTwitter(Activity context, Uri uri) {
		this.context = context;
		this.mUri = uri;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected Void doInBackground(Integer... mode) {
		if (mode[0] == Globals.MODE_CONNECT_FIRST_TIME) {
			this.publishProgress(null);
			Globals.getSharedTwitterHelper(context).askOAuth(context);
		} else if (mode[0] == Globals.MODE_RECONNECT) {
			Globals.getSharedTwitterHelper(context).handleOauthCallback(mUri);
		}

		return null;
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
		Toast.makeText(context, "Please authorize this app!", Toast.LENGTH_LONG).show();

	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		checkConnected();

	}
	
	private void checkConnected() {
		Log.w("","checkConnected");
		if (Globals.getSharedTwitterHelper(this.context).isConnected()) {

			//Globals.twitterStream = Globals.getSharedTwitterHelper(this.context).createNewTwitterStream();
			Log.w("","checkConnected IS connected");

		} 
	}
}
