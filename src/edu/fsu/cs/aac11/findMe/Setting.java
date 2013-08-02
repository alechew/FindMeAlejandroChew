package edu.fsu.cs.aac11.findMe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Setting extends Activity {

	ImageView defaultButton, saveButton;
	EditText messageBox, intervalTime;
	String userMessage, preferences;
	String defaultMessage = "Hi, ";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.settingslayout);
	    
	    defaultButton = (ImageView) findViewById(R.id.dImage);
	    saveButton = (ImageView) findViewById(R.id.saveC);
	    messageBox = (EditText) findViewById(R.id.theMessage);
	    intervalTime = (EditText) findViewById(R.id.intervalTime);
	    
	    SharedPreferences uMessage = getSharedPreferences(preferences, 0);
		userMessage = uMessage.getString("message", "Hi, ");
		SharedPreferences.Editor editor = uMessage.edit();
		editor.putString("message", userMessage);
		editor.commit();
		messageBox.setText(userMessage);
		intervalTime.setText(String.valueOf(uMessage.getLong("time", 15)));
	      
	}

	
	public void toMenu(View v)
	{
		long time;
		String timeInterval = intervalTime.getText().toString();
		if (timeInterval.equals("") != true)
		{
			time = Long.parseLong(timeInterval);
			
			if (time >= 0)
			{ 
				String finalMessage = messageBox.getText().toString();
				SharedPreferences uMessage = getSharedPreferences(preferences, 0);
				SharedPreferences.Editor editor = uMessage.edit();
				editor.putString("message", finalMessage);
				editor.putLong("time", time);
				editor.commit();
				time = 60000 * time;		// convert minutes to milisconds
			
			
				Intent passNumbers = new Intent();
				Bundle b = new Bundle();
				b.putString("userMessage", userMessage);
				b.putLong("interval", time);
				passNumbers.putExtras(b);
				setResult(Activity.RESULT_OK, passNumbers);
				finish();
			}
			else
				Toast.makeText(getApplicationContext(), "The Interval must be greater or equal to 15 minutes, Default is 15 minutes", Toast.LENGTH_LONG).show();
		}
		else
			Toast.makeText(getApplicationContext(), "TimeInterval is not set...", Toast.LENGTH_LONG).show();
	}
	
	public void toDefault(View v)
	{
		long time = 6000 * 15;
		String defaultMessage = "Hi, ";
		intervalTime.setText("15");
		messageBox.setText(defaultMessage);
		
		SharedPreferences uMessage = getSharedPreferences(preferences, 0);
		SharedPreferences.Editor editor = uMessage.edit();
		editor.putString("message", defaultMessage);
		editor.putLong("time", time);
		editor.commit();
		
	}
	
}
