package com.freniche.twandroid;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import twitter4j.AsyncTwitter;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.freniche.twitter.TwitterHelper;

public class TweetActivity extends SherlockActivity {

	private Button mSendTweetButton;
	private Button mAttachImageButton;
	private Button mCameraButton;
	private EditText mTweetText;
	private Twitter mTwitter;
	private TwitterHelper mTwitterHelper;
	private final static int RESULT_LOAD_IMAGE = 10;
    private static final int CAMERA_REQUEST = 1888; 

	private ImageView mTweetImage;

	Uri mSelectedImageUri;
	String mPicturePath;	
	
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

		
		mCameraButton = (Button) this.findViewById(R.id.button_camera);
		mCameraButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
                startActivityForResult(cameraIntent, CAMERA_REQUEST); 
            }
        });
		
		mTweetImage = (ImageView)findViewById(R.id.tweetImage);
		
		
		mAttachImageButton = (Button)findViewById(R.id.button_attach_image);
		mAttachImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						 
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
		
		mSendTweetButton = (Button)findViewById(R.id.button_send_tweet);
		mSendTweetButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				TwitterListener listener = new TwitterAdapter() {
					@Override public void updatedStatus(final Status status) {
						mSendTweetButton.post(new Runnable() {
							
							@Override
							public void run() {
								Toast.makeText(getApplicationContext(), "Successfully updated the status to [" +
										status.getText() + "].", Toast.LENGTH_SHORT).show();
								finish();
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
				StatusUpdate status = new StatusUpdate(mTweetText.getText().toString());
				File imageFile = new File(mPicturePath);
				status.setMedia(imageFile);
				asyncTwitter.updateStatus(status);
			

			}
		});
	}


	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	     super.onActivityResult(requestCode, resultCode, data);
	      
	     if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
	         mSelectedImageUri = data.getData();
	         String[] filePathColumn = { MediaStore.Images.Media.DATA };
	 
	         Cursor cursor = getContentResolver().query(mSelectedImageUri, filePathColumn, null, null, null);
	         cursor.moveToFirst();
	 
	         int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	         mPicturePath = cursor.getString(columnIndex);
	         cursor.close();
	                      
	         // String picturePath contains the path of selected Image
	         mTweetImage.setImageURI(mSelectedImageUri);
	         
	     }
	     
	     if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
	            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
	            mTweetImage.setImageBitmap(photo);
	            
	            
	            String root = Environment.getExternalStorageDirectory().toString();
	            File myDir = new File(root + "/saved_images");    
	            myDir.mkdirs();
	            Random generator = new Random();
	            int n = 10000;
	            n = generator.nextInt(n);
	            String fname = "Image-"+ n +".jpg";
	            File file = new File (myDir, fname);
	            if (file.exists ()) file.delete (); 
	            try {
	                   FileOutputStream out = new FileOutputStream(file);
	                   photo.compress(Bitmap.CompressFormat.JPEG, 90, out);
	                   out.flush();
	                   out.close();
	                   mPicturePath = file.getAbsolutePath();
	            } catch (Exception e) {
	                   e.printStackTrace();
	            }
	        } 
	}

	
	public void uploadPic(File file, String message,Twitter twitter) throws Exception  {
	    try{
	        StatusUpdate status = new StatusUpdate(message);
	        status.setMedia(file);
	        twitter.updateStatus(status);}
	    catch(TwitterException e){
	        Log.d("TAG", "Pic Upload error" + e.getErrorMessage());
	        throw e;
	    }
	}
}
