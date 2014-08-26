package com.optimumssi.phoneticalphabeta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.app.Activity;
import android.content.ContentProvider;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements ResultListener{

	EditText textToConvert;
//	Button convertButton;
	Button toggleKeyBoardDescription;
	Button clearButton;
	Button audioButton;
	TextView convertedTextToDisplay;
	String stringToConvert;
	
	private SoundPool soundPool;
	private List<Integer> soundID;
	boolean loaded = false;
	
	AssetManager am;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Set the hardware buttons to control the music
	    this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		am = getAssets();
		textToConvert = (EditText) findViewById(R.id.textToConvert);
		toggleKeyBoardDescription = (Button) findViewById(R.id.toggleKeyBoard);
//		convertButton = (Button) findViewById(R.id.convertButton);
		clearButton = (Button) findViewById(R.id.clearButton);
		audioButton = (Button) findViewById(R.id.audioButton);
		convertedTextToDisplay = (TextView) findViewById(R.id.convertedText);
//		convertButton.setOnClickListener(convertText_OnClickListener);
		toggleKeyBoardDescription.setOnClickListener(toggleKeyBoard_OnClickListener);
		clearButton.setOnClickListener(clearText_OnClickListener);
		audioButton.setOnClickListener(listenAudio_OnClickListener);
		//************************TESTING**************************//
		textToConvert.addTextChangedListener(textToConvert_AddTextChangedListener);
// 		Load the sound
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		
	    soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
	      @Override
	      public void onLoadComplete(SoundPool soundPool, int sampleId,
	          int status) {
	        loaded = true;
	      }
	    });

	}
	
	TextWatcher textToConvert_AddTextChangedListener = new TextWatcher(){

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			stringToConvert = textToConvert.getText().toString();
			ConvertTextAsyncTask cTAT = new ConvertTextAsyncTask();
			cTAT.setOnResultListener(MainActivity.this);
			cTAT.execute(stringToConvert);
		}
		
	};

	OnClickListener toggleKeyBoard_OnClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			InputMethodManager inputMgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMgr.toggleSoftInput(0, 0);
		}
	};
	
	OnClickListener clearText_OnClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			textToConvert.setText("");
			convertedTextToDisplay.setText("");
		}
	};

	OnClickListener listenAudio_OnClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			String tempConvertedTextToDisplay = (String) convertedTextToDisplay.getText();
			String[] filesToPlay = tempConvertedTextToDisplay.split(" ");

//			AssetFileDescriptor descriptor;
//			try {
//				AssetFileDescriptor afd= new AssetFileDescriptor(ParcelFileDescriptor.open(new File(new URI("file:///assets/sounds/alfa.mp3")), ParcelFileDescriptor.MODE_READ_ONLY), 0, AssetFileDescriptor.UNKNOWN_LENGTH);
//				Log.i("Info-File Exists","|File Exits|");
//			} catch (FileNotFoundException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (URISyntaxException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			for(String tempFileToPlay: filesToPlay){
				Log.i("Info-File to play list","|"+tempFileToPlay+"|");

//					descriptor = getAssets().openFd(tempFileToPlay.toLowerCase()+".mp3");//"file:///assets/sounds/"+tempFileToPlay.toLowerCase()
//					BufferedReader f = new BufferedReader(new FileReader(descriptor.getFileDescriptor()));
				try {
				    AssetFileDescriptor descriptor = getAssets().openFd("alfa");
				    long start = descriptor.getStartOffset();
				    long end = descriptor.getLength();
				    MediaPlayer mediaPlayer=new MediaPlayer();
				    mediaPlayer.setDataSource(descriptor.getFileDescriptor(), start, end);
				    mediaPlayer.prepare();
				    mediaPlayer.start(); 
					String[] list = getAssets().list("assets");
					for(String temp: list){
						Log.i("Info-List Item","|"+temp+"| LIST ITEM");
					}
					File f = null;
					try {
						f = new File(new URI("file:///~/alfa"));
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ParcelFileDescriptor pfd = ParcelFileDescriptor.open(f, ParcelFileDescriptor.MODE_READ_ONLY);
					AssetFileDescriptor afd = new AssetFileDescriptor (pfd, 0, AssetFileDescriptor.UNKNOWN_LENGTH);
					soundID.add(soundPool.load(afd.getFileDescriptor(), 0, afd.getLength(), 1));
//					Uri uri = Uri.parse("file:///android_asset/sounds/"+tempFileToPlay.toLowerCase()+".mp3");
//					InputStream is = getAssets().open(uri.getPath());
//					soundID.add(soundPool.load(st.openAssetFile(uri, ""), 1));
					Log.i("Info-File to play list","|"+tempFileToPlay+"| is added");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
			// Getting the user sound settings
			AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
			float actualVolume = (float) audioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC);
			float maxVolume = (float) audioManager
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float volume = actualVolume / maxVolume;
			
			if (loaded) {
				Iterator iteratorSoundID = soundID.iterator();
				while(iteratorSoundID.hasNext()){
					int tempSoundID = (Integer) iteratorSoundID.next();
					Log.e("Test", "sound INT"+tempSoundID);
					soundPool.play(tempSoundID, volume, volume, 1, 0, 1f);
					Log.e("Test", "Played sound");
				}
			}
			
/*			
 * 			#########################JUNK CODE#########################
 * 			String[] listOfSounds = null;
			try {
				listOfSounds = am.list("sounds");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(listOfSounds!=null){
				for(String tempFileToPlay: listOfSounds){
					if (tempFileToPlay.endsWith(".mp3")) {
						Log.i("Info-File to play list","|"+tempFileToPlay+"|");
					}
				}
			}*/
//		    soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
//		    soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
//		      @Override
//		      public void onLoadComplete(SoundPool soundPool, int sampleId,
//		          int status) {
//		        loaded = true;
//		      }
//		    });
//		    soundID = soundPool.load(MainActivity.this, R.raw.bravo, 1);
//			
//			AssetManager am = getAssets();
//			try {
//
//				String[] files = am.list("res");
//				if(files!=null){
//					Log.e("Test", "files fetched");
//					Log.e("Test", "File size "+files.length);
//				}else{
//					Log.e("Test", "files not fetched");
//				}
//
//				for(int i=0; i<files.length; i++){
//					Log.i("INFO", " file= :"+i+"= name=> "+files[i]);
//				}
//
//			} catch (IOException e1) {
//
//				// TODO Auto-generated catch block
//
//				e1.printStackTrace();
//
//			}
//			//*********************************************************************//
//			String tempListenString = (String) convertedTextToDisplay.getText();
//			Log.i("Info", tempListenString);
//			// Getting the user sound settings
//			AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
//			float actualVolume = (float) audioManager
//					.getStreamVolume(AudioManager.STREAM_MUSIC);
//			float maxVolume = (float) audioManager
//					.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//			float volume = actualVolume / maxVolume;
//			// Is the sound loaded already?
//			if (loaded) {
//				soundPool.play(soundID, volume, volume, 1, 0, 1f);
//				Log.e("Test", "Played sound");
//			}
		}
	};
	
//	OnClickListener convertText_OnClickListener = new OnClickListener(){
//		@Override
//		public void onClick(View v) {
//			stringToConvert = textToConvert.getText().toString();
//			ConvertTextAsyncTask cTAT = new ConvertTextAsyncTask();
//			cTAT.setOnResultListener(MainActivity.this);
//			cTAT.execute(stringToConvert);
//		}
//	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onResultSucceeded(String result) {
		convertedTextToDisplay.setText(result);
	}

}
