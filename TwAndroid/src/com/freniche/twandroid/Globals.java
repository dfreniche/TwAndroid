package com.freniche.twandroid;

import twitter4j.TwitterStream;
import android.content.Context;

import com.freniche.twitter.TwitterHelper;

public class Globals {
	private static TwitterHelper twitterHelper;
	public static final Integer MODE_CONNECT_FIRST_TIME = 1;
	public static final Integer MODE_RECONNECT = 2;
	private static TwitterStream twitterStream;

	public static TwitterHelper getSharedTwitterHelper(Context ctx) {
		if (twitterHelper == null) {
			twitterHelper = new TwitterHelper(ctx);
		}
		return twitterHelper;
	}
	
	public static TwitterStream getTwitterStream() {
		if (twitterStream == null) {
			if (twitterHelper != null) {
				twitterStream = twitterHelper.createNewTwitterStream();
			}
		}
		return twitterStream;
	}
}
