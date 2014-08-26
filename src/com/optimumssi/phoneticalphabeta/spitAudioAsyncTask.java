package com.optimumssi.phoneticalphabeta;

import android.app.Activity;
import android.os.AsyncTask;

public class spitAudioAsyncTask extends AsyncTask<String, Void, Boolean> {

	private Activity activity;
	
	public spitAudioAsyncTask(Activity p_activity) {
		super();
		setActivity(p_activity);
	}

	@Override
	protected Boolean doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

}
