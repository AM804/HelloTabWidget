package course.examples.helloworldtabwidget;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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



import android.os.StrictMode;
import android.app.ListActivity;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;






import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
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
	
public class GoogleMapActivity extends Activity implements LocationListener {

	protected static final String LOG_TAG = null;
	private GoogleMap mMap;
	private double gpslatitude;
	private double gpslongitude;
	private Button btnaddfav=null;
	private Button btnremfav=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (android.os.Build.VERSION.SDK_INT > 9) {

			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()

			.permitAll().build();

			StrictMode.setThreadPolicy(policy);

			}
		setContentView(R.layout.map);
		if(MyGlobal.getflag()==2){
			setUpMapIfNeeded(1);
		}
		else{
		setUpMapIfNeeded(0);
		}
		// The GoogleMap instance underlying the GoogleMapFragment defined in main.xml 
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		
		
		
		
		mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
	}
	
	
	@Override
    protected void onResume() {
        super.onResume();
        if(btnaddfav!=null){
			btnaddfav.setVisibility(View.INVISIBLE);
		}
		if(btnremfav!=null){
			btnremfav.setVisibility(View.INVISIBLE);
		}
        //mMap.clear();
        	if (MyGlobal.getflag()==1) {
        		
			
		
			
			mMap.clear();
			double x = Double.parseDouble(MyGlobal.getlat()); 
			double y = Double.parseDouble(MyGlobal.getlng());
			LatLng latLng = new LatLng(x, y);
			Marker marker = mMap.addMarker(new MarkerOptions()
				.title(MyGlobal.getname())
				.position(new LatLng(x,y)));
			mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
			mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
	       // Toast.makeText(getBaseContext(), "You selected :" + MyGlobal.getflag(), Toast.LENGTH_SHORT).show();

        
		}
        
    }
	
	protected void onRestart() {
		super.onRestart();
		if(btnaddfav!=null){
			btnaddfav.setVisibility(View.INVISIBLE);
		}
		if(btnremfav!=null){
			btnremfav.setVisibility(View.INVISIBLE);
		}
		
		
		
		if (MyGlobal.getflag()==1){
			
		
			
			mMap.clear();
			double x = Double.parseDouble(MyGlobal.getlat()); 
			double y = Double.parseDouble(MyGlobal.getlng());
			LatLng latLng = new LatLng(x, y);
			Marker marker = mMap.addMarker(new MarkerOptions()
				.title(MyGlobal.getname())
				.position(new LatLng(x,y)));
			mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
			mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
	       // Toast.makeText(getBaseContext(), "You selected :" + MyGlobal.getflag(), Toast.LENGTH_SHORT).show();

        
		}
		if (MyGlobal.getflag()==0){
			setUpMapIfNeeded(0);
		}
		
		if (MyGlobal.getflag()==2){
			//mMap.clear();
			setUpMapIfNeeded(1);
			
		}
	}
	
	private void setUpMapIfNeeded(int flag) {
        if (mMap == null) {
        	mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
    				.getMap();
            if (mMap != null) {
                setUpMap(flag);
            }
        }
    }
	
	
	
	private void setUpMap(final int flg) {

        new Thread(new Runnable() {
            public void run() {
                try {
                    retrieveAndAddPlaces(flg);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Cannot retrive cities", e);
                    return;
                }
            }
        }).start();
    }
	
	
	protected void retrieveAndAddPlaces(final int flg2) throws IOException {
        HttpURLConnection conn = null;
        final StringBuilder json = new StringBuilder();
        try {
            // Connect to the web service
            URL url = new URL("http://77.49.171.206:8080/MobileServer/Servlet.do");
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Read the JSON data into the StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                json.append(buff, 0, read);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to service", e);
            throw new IOException("Error connecting to service", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        // Must run this on the UI thread since it's a UI operation.
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    createMarkersFromJson(json.toString(),flg2);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error processing JSON", e);
                }
            }
        });
    }
	
	void createMarkersFromJson(String json, int flg3) throws JSONException {
		List<String> ListNames = new ArrayList<String>();
        JSONObject json1 = new JSONObject(json);
        System.out.print(json);
        JSONArray Array= json1.getJSONArray("places");
        List<Marker> markers = new ArrayList<Marker>();

        for (int i = 0; i < Array.length(); i++) {
        	
        	
        	

            JSONObject jsonObj = Array.getJSONObject(i);
            double x = Double.parseDouble(jsonObj.getString("lat")); 
            double y = Double.parseDouble(jsonObj.getString("lng")); 
            //System.out.print(jsonObj.getJSONArray("latlng"));
            
            if (flg3==1) {
            	Location loc1 = new Location("");
            	loc1.setLatitude(gpslatitude);
            	loc1.setLongitude(gpslongitude);

            	Location loc2 = new Location("");
            	loc2.setLatitude(x);
            	loc2.setLongitude(y);

            	float distanceInMeters = loc1.distanceTo(loc2);
            	if (distanceInMeters<1000) {
            		Marker marker = mMap.addMarker(new MarkerOptions()
                    .title(jsonObj.getString("name"))
                    .position(new LatLng(x,y)));
            		 mMap.setInfoWindowAdapter(new InfoWindowAdapter() {
                    	 
                         // Use default InfoWindow frame
                        

         				@Override
         				public View getInfoContents(Marker arg0) {
         					// TODO Auto-generated method stub
         					View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);
         					TextView tvLat = (TextView) v.findViewById(R.id.tv_name);
         					tvLat.setText( arg0.getTitle());
         					
         					return v;
         				}

         				@Override
         				public View getInfoWindow(Marker arg0) {
         					// TODO Auto-generated method stub
         					return null;
         				}
              
                         // Defines the contents of the InfoWindow
                        
                         
                     });
                     
                     
                     
                     
                         mMap.setOnMarkerClickListener(new OnMarkerClickListener()
                         {

                             //@Override
                             public boolean onMarkerClick(final Marker arg0) {
                             	arg0.showInfoWindow();
                             	String check=null;
         						try {
         							check = MyGlobal.CheckShared(arg0.getTitle());
         							//Toast.makeText(GoogleMapActivity.this, check, Toast.LENGTH_SHORT).show();
         						} catch (UnsupportedEncodingException e1) {
         							// TODO Auto-generated catch block
         							e1.printStackTrace();
         						}
         						btnaddfav=(Button)findViewById(R.id.AddFavButton);
                                 //btnaddfav.setVisibility(View.VISIBLE);
                                btnremfav=(Button)findViewById(R.id.RemFavButton);
                            	    //btnremfav.setVisibility(View.VISIBLE);
                                 if (check.equals("0")){
                                      //Toast.makeText(GoogleMapActivity.this, arg0.getTitle(), Toast.LENGTH_SHORT).show();// display toast
                                      //arg0.showInfoWindow();
                                      //String check=MyGlobal.CheckShared(arg0.getTitle());
                                 	 btnaddfav.setVisibility(View.VISIBLE);
                                      
                                      btnaddfav.setOnClickListener(new View.OnClickListener() {
                                     	    @Override
                                     	    public void onClick(View v) {
                                     	    	btnaddfav.setVisibility(View.INVISIBLE);
                                             	btnremfav.setVisibility(View.VISIBLE);

                                     	    	
                                     	    	//Toast.makeText(GoogleMapActivity.this, arg0.getTitle(), Toast.LENGTH_SHORT).show();
                                     	    	try {
         										String x=MyGlobal.AddFavorite2(arg0.getTitle(),arg0.getPosition());
         										Toast.makeText(GoogleMapActivity.this, x, Toast.LENGTH_SHORT).show();
         										} catch (UnsupportedEncodingException e) {
         											// TODO Auto-generated catch block
         											e.printStackTrace();
         										}
                                     	    }
                                     	});
                                      btnremfav.setVisibility(View.INVISIBLE);
                                 }
                                      else {
                                     	 
                                     	 btnremfav.setVisibility(View.VISIBLE);
                                     	 btnremfav.setOnClickListener(new View.OnClickListener() {
                                         	    @Override
                                         	    public void onClick(View v) {
                                         	    	btnremfav.setVisibility(View.INVISIBLE);
                                         	    	btnaddfav.setVisibility(View.VISIBLE);

                                         	    	//Toast.makeText(GoogleMapActivity.this, arg0.getTitle(), Toast.LENGTH_SHORT).show();
                                         	    	try {
             										MyGlobal.DeleteFav(arg0.getTitle());
             										Toast.makeText(GoogleMapActivity.this, "Place Deleted Successfully", Toast.LENGTH_SHORT).show();
             										} catch (UnsupportedEncodingException e) {
             											// TODO Auto-generated catch block
             											e.printStackTrace();
             										}
                                         	    }
                                         	});
                                     	 
                                     	 
                                     	 btnaddfav.setVisibility(View.INVISIBLE);
                                     	 
                                      }
                                      
                                 return true;
                             }
                             

                         });
                         
                         
                         mMap.setOnMapClickListener(new OnMapClickListener() {

                             @Override
                             public void onMapClick(LatLng point) {
                             	btnaddfav=(Button)findViewById(R.id.AddFavButton);
                             	btnaddfav.setVisibility(View.INVISIBLE);
                             	btnremfav=(Button)findViewById(R.id.RemFavButton);
                             	btnremfav.setVisibility(View.INVISIBLE);
                             }
                         });
            		
            		
                    
               ListNames.add(jsonObj.getString("name")); 
                markers.add(marker);
            	}
            	
            }
            
            else {
            Marker marker = mMap.addMarker(new MarkerOptions()
                .title(jsonObj.getString("name"))
                .position(new LatLng(x,y)));
            	
            mMap.setInfoWindowAdapter(new InfoWindowAdapter() {
            	 
                // Use default InfoWindow frame
               

				@Override
				public View getInfoContents(Marker arg0) {
					// TODO Auto-generated method stub
					View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);
					TextView tvLat = (TextView) v.findViewById(R.id.tv_name);
					tvLat.setText( arg0.getTitle());
					
					return v;
				}

				@Override
				public View getInfoWindow(Marker arg0) {
					// TODO Auto-generated method stub
					return null;
				}
     
                // Defines the contents of the InfoWindow
               
                
            });
            
            
            
            
                mMap.setOnMarkerClickListener(new OnMarkerClickListener()
                {

                    //@Override
                    public boolean onMarkerClick(final Marker arg0) {
                    	arg0.showInfoWindow();
                    	String check=null;
						try {
							check = MyGlobal.CheckShared(arg0.getTitle());
							//Toast.makeText(GoogleMapActivity.this, check, Toast.LENGTH_SHORT).show();
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						btnaddfav=(Button)findViewById(R.id.AddFavButton);
                        //btnaddfav.setVisibility(View.VISIBLE);
                        btnremfav=(Button)findViewById(R.id.RemFavButton);
                   	    //btnremfav.setVisibility(View.VISIBLE);
                        if (check.equals("0")){
                             //Toast.makeText(GoogleMapActivity.this, arg0.getTitle(), Toast.LENGTH_SHORT).show();// display toast
                             //arg0.showInfoWindow();
                             //String check=MyGlobal.CheckShared(arg0.getTitle());
                        	 btnaddfav.setVisibility(View.VISIBLE);
                             
                             btnaddfav.setOnClickListener(new View.OnClickListener() {
                            	    @Override
                            	    public void onClick(View v) {
                            	    	btnaddfav.setVisibility(View.INVISIBLE);
                                    	btnremfav.setVisibility(View.VISIBLE);

                            	    	
                            	    	//Toast.makeText(GoogleMapActivity.this, arg0.getTitle(), Toast.LENGTH_SHORT).show();
                            	    	try {
										String x=MyGlobal.AddFavorite2(arg0.getTitle(),arg0.getPosition());
										Toast.makeText(GoogleMapActivity.this, x, Toast.LENGTH_SHORT).show();
										} catch (UnsupportedEncodingException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
                            	    }
                            	});
                             btnremfav.setVisibility(View.INVISIBLE);
                        }
                             else {
                            	 
                            	 btnremfav.setVisibility(View.VISIBLE);
                            	 btnremfav.setOnClickListener(new View.OnClickListener() {
                                	    @Override
                                	    public void onClick(View v) {
                                	    	btnremfav.setVisibility(View.INVISIBLE);
                                	    	btnaddfav.setVisibility(View.VISIBLE);

                                	    	//Toast.makeText(GoogleMapActivity.this, arg0.getTitle(), Toast.LENGTH_SHORT).show();
                                	    	try {
    										MyGlobal.DeleteFav(arg0.getTitle());
    										Toast.makeText(GoogleMapActivity.this, "Place Deleted Successfully", Toast.LENGTH_SHORT).show();
    										} catch (UnsupportedEncodingException e) {
    											// TODO Auto-generated catch block
    											e.printStackTrace();
    										}
                                	    }
                                	});
                            	 
                            	 
                            	 btnaddfav.setVisibility(View.INVISIBLE);
                            	 
                             }
                             
                        return true;
                    }
                    

                });
                
                
                mMap.setOnMapClickListener(new OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng point) {
                    	btnaddfav=(Button)findViewById(R.id.AddFavButton);
                    	btnaddfav.setVisibility(View.INVISIBLE);
                    	btnremfav=(Button)findViewById(R.id.RemFavButton);
                    	btnremfav.setVisibility(View.INVISIBLE);
                    }
                });
                
                
                
                
                
                
                
                
                
                
                
           ListNames.add(jsonObj.getString("name")); 
            markers.add(marker);
            }
        }
        //Toast.makeText(getBaseContext(), "You selected :" + MyGlobal.getflag(), Toast.LENGTH_SHORT).show();
    }
	
	
	

	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		    //TextView locationTv = (TextView) findViewById(R.id.latlongLocation);
	        gpslatitude = location.getLatitude();
	        gpslongitude = location.getLongitude();
	        LatLng latLng = new LatLng(gpslatitude, gpslongitude);
	        //mMap.addMarker(new MarkerOptions().position(latLng));
	        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
	        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
	     //   locationTv.setText("Latitude:" + latitude + ", Longitude:" + longitude);
		
	}



	
	

	
	
	



	


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
}
