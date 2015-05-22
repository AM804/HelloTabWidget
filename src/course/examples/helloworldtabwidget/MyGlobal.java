package course.examples.helloworldtabwidget;

import java.io.InputStream;

import org.apache.http.impl.client.DefaultHttpClient;

import com.google.android.gms.maps.model.LatLng;












import android.app.Application;
import android.location.Location;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;


public class MyGlobal extends Application {
	
	
	private static String Globallat;
	private static String Globallng;
	private static String Globalname;
	private static int flag=0;
	
	
	public static String getlat() {
		return Globallat;
	}
	
	
	public static String getlng() {
		return Globallng;
	}
	
	
	public static String getname() {
		return Globalname;
	}
	
	public static int getflag() {
		return flag;
	}
	
	
	public static void setlat(String lat) {
		Globallat=lat;
	}
	
	public static void setlng(String lng) {
		Globallng=lng;
	}
	
	public  static void setname(String name) {
		Globalname=name;
	}
	
	public static void setflag(int flg) {
		flag=flg;
	}
	
	public static void AddFavorite(String name,LatLng latlng){
		double lat = latlng.latitude;
		double lng = latlng.longitude;
		final String URL = "http://77.49.171.206:8080/MobileServer/Servlet.do";
		
		InputStream inputStream = null;
        String result = "";
        try {
 
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
 
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(URL);
 
           // String json = "";
            
 
            // 3. build jsonObject
           // JSONObject jsonObject = new JSONObject();
           // jsonObject.accumulate("name", name);
           // jsonObject.accumulate("latitude", String.valueOf(lat));
           // jsonObject.accumulate("longitude", String.valueOf(lng));
 
            // 4. convert JSONObject to JSON to String
            //json = jsonObject.toString();
 
            // ** Alternative way to convert Person object to JSON string usin Jackson Lib 
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person); 
 
            // 5. set json to StringEntity
           // StringEntity se = new StringEntity(json);
 
            // 6. set httpPost Entity
          //  httpPost.setEntity(se);
 
            // 7. Set some headers to inform server about the type of the content   
            httpPost.setHeader("Name",name);
            httpPost.setHeader("Lat",String.valueOf(lat));
            httpPost.setHeader("Lng",String.valueOf(lng));
            
            //httpPost.setHeader("Content-type", "application/json");
 
            // 8. Execute POST request to the given URL
            httpclient.execute(httpPost);
 
           
 
           
            
 
        } catch (Exception e) {
         //   Log.d("InputStream", e.getLocalizedMessage());
        }
 
     
       
		
		return;
	}
	
	public static String CheckShared(String Name) throws UnsupportedEncodingException {
		
		String i1=URLEncoder.encode(Name,"UTF-8");
		
		HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost("http://77.49.171.206:8080/MobileServer/Servlet4.do?Name="+i1); 
        HttpResponse response;
        String responseStr = null;
		try {
            response = client.execute(request);
            responseStr = EntityUtils.toString(response.getEntity());
            Log.d("Response of GET request", response.toString());
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
     return responseStr;
		
		
		
	}
	
	
	
	public static String AddFavorite2(String Name,LatLng pos) throws UnsupportedEncodingException  {
		int i=1;
		//double name=Name;
		String i1=URLEncoder.encode(Name,"UTF-8");
		String responseStr = null;
		double lat = pos.latitude;
		double lng =pos.longitude;
		String url="http://77.49.171.206:8080/MobileServer/Servlet2.do?";
		//String send=url+"Name="+Name;
		//String query = URLEncoder.encode("Name="+Name,);
		//String send=url+query;
		
		//Object nameValuePairs;
		//((Object) nameValuePairs).add(new BasicNameValuePair("Mode", Name));
		HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost("http://77.49.171.206:8080/MobileServer/Servlet2.do?Name="+i1+"&Lat="+lat+"&Lng="+lng); 
                                // replace with your url
 
        HttpResponse response;
        try {
            response = client.execute(request);
            responseStr = EntityUtils.toString(response.getEntity());
            Log.d("Response of GET request", response.toString());
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
     return responseStr;
    }
	
	
public static void DeleteFav(String Name) throws UnsupportedEncodingException {
		
		String i1=URLEncoder.encode(Name,"UTF-8");
		
		HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost("http://77.49.171.206:8080/MobileServer/Servlet3.do?Name="+i1); 
        HttpResponse response;
        String responseStr = null;
		try {
            response = client.execute(request);
            responseStr = EntityUtils.toString(response.getEntity());
            Log.d("Response of GET request", response.toString());
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
     
		
		
		
	}
		
	
	
	
	
		
}
