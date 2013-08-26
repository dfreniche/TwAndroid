package com.freniche.twitter;

import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.util.Log;



public class TwitterHelper {
	private static Twitter twitter;
	private static RequestToken requestToken;
	private static SharedPreferences mSharedPreferences;

	public TwitterHelper(Context context) {
		mSharedPreferences = context.getSharedPreferences(TwitterConsts.PREFERENCE_NAME, Context.MODE_PRIVATE);
	}


	/**
	 * Handle OAuth Callback
	 */

	public void handleOauthCallback(Uri uri) {
		if (uri != null && uri.toString().startsWith(TwitterConsts.CALLBACK_URL)) {
			String verifier = uri.getQueryParameter(TwitterConsts.IEXTRA_OAUTH_VERIFIER);
			try { 
				AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier); 
				Editor ed = mSharedPreferences.edit();
				ed.putString(TwitterConsts.PREF_KEY_TOKEN, accessToken.getToken()); 
				ed.putString(TwitterConsts.PREF_KEY_SECRET, accessToken.getTokenSecret()); 
				ed.commit();
			} catch (Exception e) { 
				Log.e("TwitterHelper", e.getMessage()); 
			}
		}
	}

	/**
	 * check if the account is authorized
	 * @return
	 */
	public boolean isConnected() {
		return mSharedPreferences.getString(TwitterConsts.PREF_KEY_TOKEN, null) != null;
	}

	public void askOAuth(Context context) {
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setOAuthConsumerKey(TwitterConsts.CONSUMER_KEY);
		configurationBuilder.setOAuthConsumerSecret(TwitterConsts.CONSUMER_SECRET);
		Configuration configuration = configurationBuilder.build();
		twitter = new TwitterFactory(configuration).getInstance();

		try {
			requestToken = twitter.getOAuthRequestToken(TwitterConsts.CALLBACK_URL);
			// Toast.makeText(this, "Please authorize this app!", Toast.LENGTH_LONG).show();
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Remove Token, Secret from preferences
	 */
	public void disconnectTwitter() {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.remove(TwitterConsts.PREF_KEY_TOKEN);
		editor.remove(TwitterConsts.PREF_KEY_SECRET);

		editor.commit();
	}

	public TwitterStream createNewTwitterStream() {
		TwitterStream twitterStream;

		String oauthAccessToken = mSharedPreferences.getString(TwitterConsts.PREF_KEY_TOKEN, "");
		String oAuthAccessTokenSecret = mSharedPreferences.getString(TwitterConsts.PREF_KEY_SECRET, "");

		ConfigurationBuilder confbuilder = new ConfigurationBuilder();
		Configuration conf = confbuilder
				.setOAuthConsumerKey(TwitterConsts.CONSUMER_KEY)
				.setOAuthConsumerSecret(TwitterConsts.CONSUMER_SECRET)
				.setOAuthAccessToken(oauthAccessToken)
				.setOAuthAccessTokenSecret(oAuthAccessTokenSecret)
				.build();
		twitterStream = new TwitterStreamFactory(conf).getInstance();

		return twitterStream;

	}
	
	public Twitter getTwitter() {
		twitter = (new TwitterFactory()).getInstance();
		twitter.setOAuthConsumer(TwitterConsts.CONSUMER_KEY, TwitterConsts.CONSUMER_SECRET);

		twitter.setOAuthAccessToken(loadAccessToken());
		
		return twitter;
	}
	
	private AccessToken loadAccessToken() {
		String oauthAccessToken = mSharedPreferences.getString(TwitterConsts.PREF_KEY_TOKEN, "");
		String oAuthAccessTokenSecret = mSharedPreferences.getString(TwitterConsts.PREF_KEY_SECRET, "");

		return new AccessToken(oauthAccessToken, oAuthAccessTokenSecret);
	}

	public AsyncTwitter getAsyncTwitter() {
		AsyncTwitterFactory factory = new AsyncTwitterFactory();
		AsyncTwitter asyncTwitter = factory.getInstance();
		
		asyncTwitter.setOAuthConsumer(TwitterConsts.CONSUMER_KEY, TwitterConsts.CONSUMER_SECRET);
		asyncTwitter.setOAuthAccessToken(loadAccessToken());
		
		return asyncTwitter;
	}
	
}
