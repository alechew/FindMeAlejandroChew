package edu.fsu.cs.aac11.findMe;


import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

public class ContactList extends ListActivity {


    //ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
	
	Cursor mCursor;
    CursorAdapter mCursorAdapter;
    String[] mProjection;
    String[] mListColumns;
    String selectionClause = ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1";
    int[] mListItems;

    String sortOrder = ContactsContract.Contacts.DISPLAY_NAME+ " COLLATE LOCALIZED ASC";
	final int contacts = 3;
	int contactSelected = 0;
	ListView theList;
	
	int numberOfContacts;
	String[] phoneNumbers;
	String[] phoneChosen = new String[contacts];
	ListView listview;
	CheckBox[] boxes = new CheckBox[3];
	boolean full = false;
    
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    /*mCursor = getContentResolver().query(
	    		ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
	    */
	    mCursor = getContacts();
	    numberOfContacts = mCursor.getCount();
	    
	    
	    //phoneNumbers = new String[numberOfContacts];
	  
	    mListColumns = new String[] { 
	    ContactsContract.Contacts.DISPLAY_NAME };
	    mListItems = new int[] { R.id.namebox };
	    mCursorAdapter = new SimpleCursorAdapter(this, 
	    R.layout.listitem, mCursor, mListColumns, mListItems);
	    		
	    setListAdapter(mCursorAdapter);
	    setContentView(R.layout.contactlist);
	    getContactandPhones();
	}

	private Cursor getContacts() 
    {
        // Run query
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[] { ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME };
        //String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" + ("1") + "'";
        
        String[] selectionArgs = null;
        

        return managedQuery(uri, projection, selectionClause, selectionArgs,sortOrder);
    }
	
	
	
	public void getContactandPhones()
	{
	    int counter = 0;
	    int counter1 = 0;
		ContentResolver cr = getContentResolver();
	    Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null, selectionClause, null, sortOrder);
	    numberOfContacts = cur.getCount();
	    phoneNumbers = new String[numberOfContacts];
	    if (cur.getCount() > 0) 
	    {
	        while (cur.moveToNext()) 
	        {
		        String ids = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
		        //String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		        //list.add(new BasicNameValuePair("name",name.toString())); 
		        //String number = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.NUMBER));
		        if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)  
	            {
	            //Query phone here.  
		        	Cursor pCur = cr.query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, 
	                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", new String[]{ids}, null);
		        	while(pCur.moveToNext())
		        	{
		        		String number = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	                    phoneNumbers[counter] = number.toString();
		        		//list.add(new BasicNameValuePair("num",number.toString()));
	                    Log.i("numbers in contact from query", phoneNumbers[counter]);
		        		counter++;
		        		break;
	                }
	
		        	pCur.close();
	            }//if
		        else 
		        {
		        	// put dummy value in array of numbers
		        	
		        	
		        }
		
	        }//while
	        //Log.i("array items", "" +list);
	    }//if
//	    for (int i = 0 ;  i < numberOfContacts; i++)
//	    {
//	    	Log.i("PhoneNumbers in Array", phoneNumbers[i].toString());
//	    	
//	    }
	}
	
	
	@Override
	protected void onListItemClick (ListView l, View v, int position, long id)
	{
		CheckBox box;
		View view;
		// contactSelected is the counter to populate array
		box = (CheckBox) v.findViewById(R.id.checkBox);
		
		if (contactSelected <= 3)
		{
		
			if (box.isChecked() == true)
			{
					contactSelected--;
					box.setChecked(false);
					Log.i("Chek if checked", String.valueOf(contactSelected));
					
			}
			else if (contactSelected != 3)
			{
				phoneChosen[contactSelected] = phoneNumbers[position];
				contactSelected++;
				box = (CheckBox) v.findViewById(R.id.checkBox);
				box.setChecked(true);
			}
			
//			if(contactSelected == 3)
//			{
//				finishMe(v);
//			}
			if (contactSelected == 3)
				full = true;
			else
				full = false;
			
		}
		if (full == true)
			Toast.makeText(getApplicationContext(), "Maximum amount of contacts selected, Press Save Button", Toast.LENGTH_LONG).show();
		
	}
	
	
	public void finishMe(View v)
	{
		
		if (contactSelected != 3)
		{
			String[] tempPhones = new String[contactSelected];
			for (int i = 0 ; i < contactSelected ; i++)
			{
				tempPhones[i] = phoneChosen[i];
				Log.i("numbers in contactList phoneCHosen", phoneChosen[i]);
				Log.i("numbers in contact list tempohoens", tempPhones[i]);
			}
			
			phoneChosen = new String[tempPhones.length];
			phoneChosen = tempPhones;
		}
		
		Intent passNumbers = new Intent();
		Bundle b = new Bundle();
		b.putStringArray("numbers", phoneChosen); 
		passNumbers.putExtras(b);
		setResult(Activity.RESULT_OK, passNumbers);
		finish();
	}
	
	@Override
	public void onBackPressed()
	{
		View v =  null;
		finishMe(v);
		
	}
	
	
}
