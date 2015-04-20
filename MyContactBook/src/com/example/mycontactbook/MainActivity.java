package com.example.mycontactbook;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity implements OnRefreshListener {
   public final static String EXTRA_MESSAGE = "com.example.AddressBook.MESSAGE";

   private PullToRefreshListView obj;	
   private ArrayAdapter arrayAdapter ;
   private ArrayList array_list;
   DBHelper mydb;
   private int limit = 5;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      mydb = new DBHelper(this);
      array_list = mydb.getAllCotacts(limit);

      arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);

      //adding it to the list view.
      obj = (PullToRefreshListView)findViewById(R.id.listView1);
      obj.setOnRefreshListener(this);
      obj.setAdapter(arrayAdapter);

      obj.setOnItemClickListener(new OnItemClickListener(){

     @Override
     public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
     long arg3) {
         // TODO Auto-generated method stub
         int id_To_Search = arg2 + 1;
         Bundle dataBundle = new Bundle();
         dataBundle.putInt("id", id_To_Search - 1 );
         Intent intent = new Intent(getApplicationContext(),DisplayContact.class);
         intent.putExtras(dataBundle);
         startActivity(intent);
     }
     });
   }
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
      }
   @Override 
   public boolean onOptionsItemSelected(MenuItem item) 
   { 
      super.onOptionsItemSelected(item); 
      switch(item.getItemId()) 
      { 
         case R.id.item1: 
            Bundle dataBundle = new Bundle();
            dataBundle.putInt("id", 0);
            Intent intent = new Intent(getApplicationContext(),DisplayContact.class);
            intent.putExtras(dataBundle);
            startActivity(intent);
            return true; 
         default: 
            return super.onOptionsItemSelected(item); 

       } 

   } 
   public boolean onKeyDown(int keycode, KeyEvent event) {
      if (keycode == KeyEvent.KEYCODE_BACK) {
         moveTaskToBack(true);
      }
      return super.onKeyDown(keycode, event);
   }
   
	@Override
	public void onRefresh(PullToRefreshBase refreshView) {
		 	     new GetDataTask().execute();

	}
	
	private class GetDataTask extends AsyncTask<Void, Void, ArrayList<String>> {

		@Override
		protected ArrayList<String> doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			limit = limit+5;
			array_list = mydb.getAllCotacts(limit);
			return array_list;
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			//arrayAdapter.notifyDataSetChanged();
			arrayAdapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1, array_list);
			obj.setAdapter(arrayAdapter);
			obj.onRefreshComplete();
			super.onPostExecute(result);
		}
	}
	
	

}
