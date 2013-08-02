package edu.fsu.cs.aac11.findMe;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MenuActivity extends Activity {
 
	ImageView logoImage, parentMonitor, monitor, contact, information, setting, serviceStatus, messagepic, setBut;
	boolean parentClicked, monitorClicked, contactChosen, settingChosen, passwordSet, displayMessage;
	EditText password;
	String passwordPreferences, pin;					// password preference variables
	String[] phoneNumbers = new String[3];
	String userMessage = "Hi, ";
	Timer myTimer = new Timer();
	long timeInterval = 900000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_activity);
		//parentMonitor = (ImageView) findViewById(R.id.parentMonitor);
		//monitor  = (ImageView) findViewById(R.id.monitor);
		SharedPreferences message = getSharedPreferences(passwordPreferences, 0);
		password = (EditText) findViewById(R.id.password);
		
		timeInterval = message.getLong("intervalTimeF", timeInterval);
		pin = message.getString("pin", "");
		SharedPreferences.Editor editor = message.edit();
		editor.putString("pin", pin);
		editor.putBoolean("set", false);
		editor.putBoolean("status", false);
		editor.putBoolean("parentClicked", false);
		editor.putBoolean("monitorClicked", false);
		editor.putString("userMessage", userMessage);
		editor.commit();
		
		parentMonitor = (ImageView) findViewById(R.id.pStart);
		
		if (message.getBoolean("status", false) == false)
		{
			parentMonitor.setClickable(false);
			parentMonitor.setBackgroundResource(R.drawable.start);
		}
		else	// if status is active service
		{
			if(message.getBoolean("monitorClicked", true) == true)
				parentMonitor.setBackgroundResource(R.drawable.start);
			else
				parentMonitor.setBackgroundResource(R.drawable.stop);
			
//			myTimer = new Timer();
//		      myTimer.schedule(new TimerTask() {          
//		          public void run() {
//		        	  
//		        	  startMyService();
//		          }           
//		      }, 0, timeInterval); //30000 = 15 Seconds Interval.
		}
		
		monitor  = (ImageView) findViewById(R.id.mStart);
		if (message.getBoolean("status", false) == false)
		{
			monitor.setBackgroundResource(R.drawable.start);
		}
		else			// if status is active service
		{
			if(message.getBoolean("parentClicked", true) == true)
				monitor.setBackgroundResource(R.drawable.start);
			else
				monitor.setBackgroundResource(R.drawable.stop);
			
//			myTimer = new Timer();
//		      myTimer.schedule(new TimerTask() {          
//		          public void run() {
//		        	  
//		        	  startMyService();
//		          }           
//		      }, 0, timeInterval); //30000 = 15 Seconds Interval.
		}
		
		contact  =  (ImageView) findViewById(R.id.contact);
		information  = (ImageView) findViewById(R.id.information);
		messagepic = (ImageView) findViewById(R.id.informationText);
		messagepic.setVisibility(View.INVISIBLE);
		setting  = (ImageView) findViewById(R.id.setting);
		logoImage = (ImageView) findViewById(R.id.logoImage);
		serviceStatus = (ImageView) findViewById(R.id.servicestatus);
		if (message.getBoolean("status", false) == false)
			serviceStatus.setBackgroundResource(R.drawable.serviceoff);
		else
			serviceStatus.setBackgroundResource(R.drawable.serviceon);
		setBut = (ImageView) findViewById(R.id.set);
		setBut.setBackgroundResource(R.drawable.chk);
//		parentClicked = false;
//		monitorClicked = false;
		parentClicked = message.getBoolean("parentClicked", false);
		monitorClicked = message.getBoolean("monitorClicked", false);
		contactChosen = false;
		settingChosen = false;
		displayMessage = false;
		
		

		
	}
	
//	@Override
//	public void onResume()
//	{
//		super.onResume();
//		boolean service;
//		SharedPreferences message = getSharedPreferences(passwordPreferences, 0);
//		service = message.getBoolean("set", false);
//		displayMessage = false;
//		
//		if (service == false)
//			serviceStatus.setBackgroundResource(R.drawable.serviceoff);
//		else
//		{
//			//serviceStatus.setBackgroundResource(R.drawable.serviceon);
//			parentMonitor.setClickable(false);
//		}
//	}
	
	
	public void parentMonitor(View v)
	{
		if (parentClicked == true)
		{
			//stop the service
			myTimer.cancel();
			this.stopService(new Intent(this, GPSService2.class));
			parentClicked = false;
			monitor.setClickable(true);
			serviceStatus.setBackgroundResource(R.drawable.serviceoff);
			SharedPreferences message = getSharedPreferences(passwordPreferences, 0);
			SharedPreferences.Editor editor = message.edit();
			editor.putBoolean("status", false);
			editor.putBoolean("parentClicked", parentClicked);
			editor.commit();
			parentMonitor.setBackgroundResource(R.drawable.start);
			parentMonitor.setClickable(false);
		}
		else if(monitorClicked == false)
		{
			if (contactChosen == true)
			{
				parentClicked = true;
				monitor.setClickable(false);
				
				// it will change the button to set the service off however, must press set to validate the password and make it clickable to stop
				
				parentMonitor.setClickable(false);
				//start service
				
				  myTimer = new Timer();
			      myTimer.schedule(new TimerTask() {          
			          public void run() {
			        	  
			        	  startMyService();
			          }           
			      }, 0, timeInterval); //30000 = 15 Seconds Interval.
				
				
				//startMyService();
				serviceStatus.setBackgroundResource(R.drawable.serviceon);
				SharedPreferences message = getSharedPreferences(passwordPreferences, 0);
				SharedPreferences.Editor editor = message.edit();
				editor.putBoolean("status", true);
				editor.putBoolean("parentClicked", parentClicked);
				editor.commit();
				parentMonitor.setBackgroundResource(R.drawable.stop);
			}
			else
			{
				Toast.makeText(getApplicationContext(), "You Must First Select Your Contacts",Toast.LENGTH_LONG).show();
			}
		}
		
	}
	
	public void monitor (View v)
	{
		if (monitorClicked == true)
		{
			// stop the service
			myTimer.cancel();
			this.stopService(new Intent(this, GPSService2.class));
			parentMonitor.setClickable(true);
			monitorClicked = false;
			serviceStatus.setBackgroundResource(R.drawable.serviceoff);
			SharedPreferences message = getSharedPreferences(passwordPreferences, 0);
			SharedPreferences.Editor editor = message.edit();
			editor.putBoolean("status", false);
			editor.putBoolean("monitorClicked", monitorClicked);
			editor.commit();
			monitor.setBackgroundResource(R.drawable.start);
			
		}
		else if (parentClicked == false)
		{	
			if( contactChosen == true)
			{
				monitorClicked = true;
				parentMonitor.setClickable(false);
				
				// start the service
				myTimer = new Timer();
				myTimer.schedule(new TimerTask() {          
			          public void run() {
			        	  
			        	  startMyService();
			          }           
			      }, 0, timeInterval); //30000 = 15 Seconds Interval.
				
				//startMyService();
				Log.i("calling Service", "Service Called");
				serviceStatus.setBackgroundResource(R.drawable.serviceon);
				SharedPreferences message = getSharedPreferences(passwordPreferences, 0);
				SharedPreferences.Editor editor = message.edit();
				editor.putBoolean("status", true);
				editor.putBoolean("monitorClicked", monitorClicked);
				editor.commit();
				monitor.setBackgroundResource(R.drawable.stop);
			}
			else
			{
				Toast.makeText(getApplicationContext(), "You Must First Select Your Contacts",Toast.LENGTH_LONG).show();
			}
		}
		
	}
	
	public void selectContact(View v)
	{
		Intent toContact = new Intent(MenuActivity.this, ContactList.class);
		startActivityForResult(toContact, 0);
		
	}
	
	public void setting(View v)
	{
		
		Intent toSetting = new Intent(MenuActivity.this,Setting.class);	
		startActivityForResult(toSetting , 1);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// request code for contact is zero, for setting  is 1
		super.onActivityResult(requestCode, resultCode, data); 
		  switch(requestCode) { 
		    case (0) : { 
		      if (resultCode == Activity.RESULT_OK) { 
		    	  Bundle b = new Bundle();
		    	  b = data.getExtras();
		     String[] phoneNumbers1 = b.getStringArray("numbers");
		     phoneNumbers = new String[phoneNumbers1.length];
		     
		     for (int  i = 0 ; i < phoneNumbers1.length ; i++)
		     {
			    	phoneNumbers[i] = phoneNumbers1[i];					// copy the array of strings to a constant one.
			    	Log.i("numbers in Menu", phoneNumbers[i]);
		     }
		     contactChosen = true;
		      }// end if 
		      break; 
		    }
		    
		    case (1):{
		    	if (resultCode == Activity.RESULT_OK) 
		    	{ 
			    	String theMessage;  
		    		Bundle c = new Bundle();
			    	c = data.getExtras();
			    	theMessage = c.getString("userMessage");
			    	timeInterval = c.getLong("interval");
			    	SharedPreferences message = getSharedPreferences(passwordPreferences, 0);
			  		SharedPreferences.Editor editor = message.edit();
			  		editor.putString("userMessage", theMessage);
			  		editor.putLong("intervalTimeF", timeInterval);
			  		editor.commit();
			  		userMessage = theMessage;
		    	
		    	
		    	}
		    	 break;
		  }// end case 2
		   
		}// request code;
	}	// end function
	
	public void validatePassword(View v)
	{
		String tPass = password.getText().toString();
		boolean passwordSet; 
		SharedPreferences message = getSharedPreferences(passwordPreferences, 0);
		pin = message.getString("pin", "");
		passwordSet = message.getBoolean("set", false);
		if (passwordSet == true)
		{
			if( tPass.equals(pin))
			{
				
				SharedPreferences.Editor editor = message.edit();
				editor.putString("pin","");
				editor.putBoolean("set", false);
				editor.commit();
				parentMonitor.setClickable(true);
				password.setText("");
				Toast.makeText(getApplicationContext(), "Password has Been Disabled", Toast.LENGTH_LONG).show();
			}
			
		}
		else
		{
			if (tPass.equals("") == false)
			{
				SharedPreferences.Editor editor = message.edit();
				editor.putString("pin", tPass);
				editor.putBoolean("set", true);
				editor.commit();
				parentMonitor.setClickable(true);		// because password is set
				Toast.makeText(getApplicationContext(), "Password has been Enabled", Toast.LENGTH_LONG).show();
				password.setText("");
			}
			else
				Toast.makeText(getApplicationContext(), "Pin/Password cannot be empty...", Toast.LENGTH_LONG).show();
		}
		 
	}
	
	public void displayHelp(View v)
	{
		if (displayMessage == false)
		{
			displayMessage = true;
			messagepic.setVisibility(View.VISIBLE);
		}
		else
		{
			displayMessage = false;
			messagepic.setVisibility(View.INVISIBLE);
			
		}
	}
	
	
	public void startMyService()
	{
		Log.i("calling Service", "Inside SAtartMyService Function");
//		Intent myService = new Intent(this, MyGPSService.class);
		Intent myService = new Intent(this, GPSService2.class);
		Bundle b = new Bundle();
		b.putStringArray("phones", phoneNumbers);
		b.putString("finalMessage", userMessage);
		myService.putExtras(b);
		this.startService(myService);
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

}
