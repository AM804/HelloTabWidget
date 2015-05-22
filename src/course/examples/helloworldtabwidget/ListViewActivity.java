package course.examples.helloworldtabwidget;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;



import android.app.ListActivity;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;






import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



//import course.examples.Networking.AndroidHttpClientJSON.NetworkingAndroidHttpClientJSONActivity;
//import course.examples.Networking.AndroidHttpClientJSON.R;



// This applications requires several set up steps. 
// See https://developers.google.com/maps/documentation/android/start for more information
	
// Requires a device that supports OpenGL ES version 2
// I've run this application successfully on an emulated Nexus5
	
public class ListViewActivity extends Activity  {
	
	// List view
    private ListView lv;
    private List<String>coord= new ArrayList<String>() ;
    private final List<String> result = new ArrayList<String>();
    private String lat,lng;
    // Listview Adapter
    ArrayAdapter<String> adapter;
    
    private List<String>ListNames = new ArrayList<String>();
    String products[] = {"Dell Inspiron", "HTC One X", "HTC Wildfire S", "HTC Sense", "HTC Sensation XE",
            "iPhone 4S", "Samsung Galaxy Note 800",
            "Samsung Galaxy S3", "MacBook Air", "Mac Mini", "MacBook Pro"};
    String[] Meri= new String[100];
     
    // Search EditText
    EditText inputSearch;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.search);
		super.onCreate(savedInstanceState);
		new HttpGetTask().execute();
	}

	private class HttpGetTask extends AsyncTask<Void, Void, List<String>> {

		// Get your own user name at http://www.geonames.org/login
		//private static final String USER_NAME = "aporter";

		private static final String URL = "http://77.49.171.206:8080/MobileServer/Servlet.do";

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
	        inputSearch = (EditText) findViewById(R.id.inputSearch);
	         
	        // Adding items to listview
	        adapter = new ArrayAdapter<String>(ListViewActivity.this,R.layout.list_item,result);
	        lv.setAdapter(adapter);
	        
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
					 
					finish();
					 
					// TODO Auto-generated method stub
					
				}
	        };
	 
	        // Setting the item click listener for listView
	        lv.setOnItemClickListener(itemClickListener);
	         
	        /**
	         * Enabling Search Filter
	         * */
	        inputSearch.addTextChangedListener(new TextWatcher() {
	             
	            @Override
	            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
	                // When user changed the Text
	            	ListViewActivity.this.adapter.getFilter().filter(cs);   
	            }
	             
	            @Override
	            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
	                    int arg3) {
	                // TODO Auto-generated method stub
	                 
	            }
	             
	            @Override
	            public void afterTextChanged(Editable arg0) {
	                // TODO Auto-generated method stub                          
	            }
	        });
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

