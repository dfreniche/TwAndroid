package com.freniche.twandroid;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DirectMessagesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_direct_messages);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.direct_messages, menu);
		return true;
	}

}