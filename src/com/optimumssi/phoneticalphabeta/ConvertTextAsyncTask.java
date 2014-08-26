package com.optimumssi.phoneticalphabeta;

import java.util.HashMap;
import java.util.Map;

import android.os.AsyncTask;
import android.util.Log;

public class ConvertTextAsyncTask extends AsyncTask<String, Void, String> {

	static Map<String, String> phoneticMap = new HashMap<String, String>();
	String stringToConvert;
	String convertedString;
	ResultListener resultListener;
	
	public void setOnResultListener(ResultListener listener) {
        this.resultListener = listener;
    }
	
	public ConvertTextAsyncTask() {
		super();
		phoneticMap.put("A", "Alfa");
		phoneticMap.put("B", "Bravo");
		phoneticMap.put("C", "Charlie");
		phoneticMap.put("D", "Delta");
		phoneticMap.put("E", "Echo");
		phoneticMap.put("F", "Foxtrot");
		phoneticMap.put("G", "Golf");
		phoneticMap.put("H", "Hotel");
		phoneticMap.put("I", "India");
		phoneticMap.put("J", "Juliett");
		phoneticMap.put("K", "Kilo");
		phoneticMap.put("L", "Lima");
		phoneticMap.put("M", "Mike");
		phoneticMap.put("N", "November");
		phoneticMap.put("O", "Oscar");
		phoneticMap.put("P", "Papa");
		phoneticMap.put("Q", "Quebec");
		phoneticMap.put("R", "Romeo");
		phoneticMap.put("S", "Sierra");
		phoneticMap.put("T", "Tango");
		phoneticMap.put("U", "Uniform");
		phoneticMap.put("V", "Victor");
		phoneticMap.put("W", "Whiskey");
		phoneticMap.put("X", "Xray");
		phoneticMap.put("Y", "Yankee");
		phoneticMap.put("Z", "Zulu");
		phoneticMap.put("1", "One");
		phoneticMap.put("2", "Two");
		phoneticMap.put("3", "Tree");
		phoneticMap.put("4", "Four");
		phoneticMap.put("5", "Five");
		phoneticMap.put("6", "Six");
		phoneticMap.put("7", "Seven");
		phoneticMap.put("8", "Eight");
		phoneticMap.put("9", "Nine");
		phoneticMap.put("0", "Zero");
		phoneticMap.put(" ", "SPACE");
		phoneticMap.put("-", "HYPHEN");
		phoneticMap.put("/", "SLASH");
		phoneticMap.put(".", "PERIOD");
		phoneticMap.put("@", "AT");
		phoneticMap.put("#", "HASH");
	}



	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		resultListener.onResultSucceeded(result);
	}



	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		stringToConvert = params[0];
		convertedString = "";
		String uprStringToConvert = stringToConvert.toUpperCase();
		char[] toBeConvertedCharArray = uprStringToConvert.toCharArray();
		for(char temp:toBeConvertedCharArray){
			convertedString += phoneticMap.get(String.valueOf(temp))+" ";
		}
		return convertedString;
	}

}
