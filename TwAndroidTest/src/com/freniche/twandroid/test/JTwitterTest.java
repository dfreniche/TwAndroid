package com.freniche.twandroid.test;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.test.AndroidTestCase;

public class JTwitterTest extends AndroidTestCase {
	private static final String user ="dfrenichetester";
    private static final String pw ="t3st4cc0unt";
	
	public void testTwitter() {
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("dQxGqAiuZ26E838alHRUTQ")
		  .setOAuthConsumerSecret("***REMOVED***")
		  .setOAuthAccessToken("***REMOVED***")
		  .setOAuthAccessTokenSecret("***REMOVED***");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		
		// Make a Twitter object
	    Query query = new Query("source:twitter4j sevilla");
	    QueryResult result = null;
		try {
			result = twitter.search(query);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    for (Status status : result.getTweets()) {
	        System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
	    }
	}

}
