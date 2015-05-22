package course.examples.helloworldtabwidget;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FavoriteActivity extends Activity {
	private ListView lv;
    private List<String>coord= new ArrayList<String>() ;
    private final List<String> result = new ArrayList<String>();
    private String lat,lng;
    // Listview Adapter
    ArrayAdapter<String> adapter;
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	       // lv = (ListView) findViewById(R.id.list_view);
	        
	    
	 
	 
     
}
	 protected void onResume() {
		 setContentView(R.layout.fav);
		 super.onResume();
		// lv = (ListView) findViewById(R.id.list_view);
		 if (adapter!=null){
			 adapter.clear();
			 adapter.notifyDataSetChanged();
		 }
		 new HttpGetTask().execute();
		 
		 
	 }
	 private class HttpGetTask extends AsyncTask<Void, Void, List<String>> {

			// Get your own user name at http://www.geonames.org/login
			//private static final String USER_NAME = "aporter";

			private static final String URL = "http://77.49.171.206:8080/MobileServer/GetFav.do";

			AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

			@Override
			protected List<String> doInBackground(Void... params) {
				HttpGet request = new HttpGet(URL);
				JSONResponseHandler responseHandler = new JSONResponseHandler();
				try {
					return mClient.execute(request, responseHandler);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(final List<String> result) {
				if (null != mClient)
					mClient.close();
				  
		        lv = (ListView) findViewById(R.id.list_view);
		       
		       // inputSearch = (EditText) findViewById(R.id.inputSearch);
		         
		        // Adding items to listview
		        
		        adapter = new ArrayAdapter<String>(FavoriteActivity.this,R.layout.list_item,result);
		        
		        lv.setAdapter(adapter);
		       // adapter.notifyDataSetChanged();
		        //((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();
		       
		       
		       
		        
		        
		        OnItemClickListener itemClickListener = new OnItemClickListener() {
		            
		           

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						
						
						for (int i = 0; i<result.size(); i++) {
							
							if (result.get(i)==adapter.getItem(position)) {
								lat=coord.get(2*i);
								lng=coord.get(2*i+1);
							}
							
						}
						
						MyGlobal.setname(adapter.getItem(position));
						MyGlobal.setlat(lat);
						MyGlobal.setlng(lng);
						MyGlobal.setflag(1);
						
						
						Toast.makeText(getBaseContext(), "You selected :" + MyGlobal.getname(), Toast.LENGTH_SHORT).show();
						((HelloTabWidget) FavoriteActivity.this.getParent()).switchTab(0);					//finish();
						 
						// TODO Auto-generated method stub
						
					}
		        };
		 
		        // Setting the item click listener for listView
		        lv.setOnItemClickListener(itemClickListener);
		         
		        /**
		         * Enabling Search Filter
		         * */
		    
			}
		}

		private class JSONResponseHandler implements ResponseHandler<List<String>> {

			
			private static final String NAME_TAG = "name";
			private static final String LIST_TAG = "places";

			@Override
			public List<String> handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				//List<String> result = new ArrayList<String>();
				//List<String> coord=new ArrayList<String>();
				String JSONResponse = new BasicResponseHandler()
						.handleResponse(response);
				try {

					// Get top-level JSON Object - a Map
					JSONObject responseObject = (JSONObject) new JSONTokener(
							JSONResponse).nextValue();

					// Extract value of "earthquakes" key -- a List
					JSONArray earthquakes = responseObject
							.getJSONArray(LIST_TAG);

					// Iterate over earthquakes list
					for (int idx = 0; idx < earthquakes.length(); idx++) {

						// Get single earthquake data - a Map
						JSONObject earthquake = (JSONObject) earthquakes.get(idx);

						// Summarize earthquake data as a string and add it to
						// result
						result.add(earthquake.getString(NAME_TAG));
						coord.add(earthquake.getString("lat"));
						coord.add(earthquake.getString("lng"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return result;
			}
		}
	 
	 
	 

}

