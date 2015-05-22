package course.examples.helloworldtabwidget;

import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class HelloTabWidget extends TabActivity  {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       

        setContentView(R.layout.main);

      

        
        @SuppressWarnings("unused")
		Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Reusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
       
        intent = new Intent().setClass(this, GoogleMapActivity.class);
        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("mapview").setIndicator("Map View")
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, FavoriteActivity.class);
        spec = tabHost.newTabSpec("albums").setIndicator("Favorites")
                      .setContent(intent);
        tabHost.addTab(spec);


        tabHost.setCurrentTab(0);
    }
    
    public void onRestart() {
    	super.onRestart();
    	getTabHost().setCurrentTab(0);
    }
    
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater mif = getMenuInflater();
    	mif.inflate(R.menu.main_activity_actions, menu);
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.action_search:
            Intent go= new Intent(HelloTabWidget.this, ListViewActivity.class);
            
            startActivity(go);
            return true;
        case R.id.action_getall:
        	Intent goall= new Intent(HelloTabWidget.this, HelloTabWidget.class);
        	MyGlobal.setflag(0);
            
            startActivity(goall);finish();
            return true;
        case R.id.action_nearest:
        	Intent gonear= new Intent(HelloTabWidget.this, HelloTabWidget.class);
        	MyGlobal.setflag(2);
            
            startActivity(gonear);finish();
        
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    public void switchTab(int tab){
    	getTabHost().setCurrentTab(tab);
}
}