package com.teiki.cafea1e.activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.teiki.cafea1e.Cafea1e;
import com.teiki.cafea1e.R;
import com.teiki.cafea1e.business.Fields;
import com.teiki.cafea1e.business.Record;
import com.teiki.cafea1e.data.DatabaseTable;
import com.teiki.cafea1e.data.MyCustomSuggestionProvider;
import com.teiki.cafea1e.fragment.GeoLocalisationMap;
import com.teiki.cafea1e.fragment.ListFragment;
import com.teiki.cafea1e.fragment.NavigationDrawerFragment;
import com.teiki.cafea1e.fragment.RecordDetail;
import com.teiki.cafea1e.server.GsonRecordsList;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends LocationActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	private NavigationDrawerFragment mNavigationDrawerFragment;

	private CharSequence mTitle;
	
	private final static String tag_map = "tag_map";
	private final static String tag_list = "tag_list";
	private final static String tag_favorite = "tag_favorite";
    private final static String tag_record_detail = "tag_record_detail";
	
	//private Record[] record_tab;
    private ArrayList<Record> records_list;
	
	private AsyncTask<Void, Integer, Boolean> asynctask;
	
	private final int DISTANCE_RAYON_URL = 3000;

    public int NB_BAR_TOTAL;

    protected DatabaseTable database;
    private SearchView searchview;
    private MenuItem search_item;
    private String[] menuTitle;

    private Boolean doubleBackToExitPressedOnce = false;
	

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();
        menuTitle = getResources().getStringArray(R.array.menu_array);

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
		
		selectfragment(0);
    }

    @Override
    protected void onResume() {
//        if (mLocationClient != null && mLocationClient.isConnected()) {
//            Location mCurrentLocation = mLocationClient.getLastLocation();
//            if (mCurrentLocation != null) {
//                LatLng pos = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
//
//                final FragmentManager fm = getSupportFragmentManager();
//
//                GeoLocalisationMap fragment = (GeoLocalisationMap) fm.findFragmentByTag(tag_map);
//
//
//                if (fragment.isVisible()) {
//                    fragment.showInMap(pos);
//                }
//            }
//        }

        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            showResults(query);
        }
        else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // handles a click on a search suggestion; launches activity to show word
//            Intent wordIntent = new Intent(this, R.class);
//            wordIntent.setData(intent.getData());
//            startActivity(wordIntent);
            openRecordDetail(intent.getData());
        }

    }

    private void showResults(String query) {

        Cursor cursor = database.getWordMatches(query, null);

        if (cursor == null) {
            Toast.makeText(this,R.string.no_result,Toast.LENGTH_SHORT).show();
        } else {

            final int count = cursor.getCount();
            if (count == 1) {
                //TO DO
            }
        }
    }


    @SuppressLint("NewApi")
	public void parsejson(){
        if (NB_BAR_TOTAL != 0)
            asynctask = new GsonRecordsList(this, getString(R.string.url_json_bar)+"&rows=200");//+NB_BAR_TOTAL
        else
		    asynctask = new GsonRecordsList(this, getString(R.string.url_json_bar)+"&rows=200");
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB_MR1) {
			asynctask.execute();
		} else {
			asynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
	}
	
	@SuppressLint("NewApi")
	private void parsejson(LatLng pos){
		asynctask = new GsonRecordsList(this, 
				getString(R.string.url_json_bar)
				+"&"+getString(R.string.url_json_bar_geofilter)+"="+pos.latitude+","+pos.longitude+","+DISTANCE_RAYON_URL +"&rows=50", pos);
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB_MR1) {
			asynctask.execute();
		} else {
			asynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		selectfragment(position);
	}
	
	private void selectfragment(int num) {
		switch (num) {
		case 0:
			openMap();
			break;
		case 1:
			openList();
			break;
		}
	}

	public void onSectionAttached(int number) {
        mTitle = menuTitle[number];
//		switch (number) {
//		case 1:
//			mTitle = getString(R.string.title_section1);
//			break;
//		case 2:
//			mTitle = getString(R.string.title_section2);
//			break;
//		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                searchview = (SearchView) menu.findItem(R.id.search).getActionView();
                searchview.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                searchview.setIconifiedByDefault(false);
            }
            search_item = menu.findItem(R.id.search);
		}



        return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                onSearchRequested();
                return true;
            default:
                return false;
        }
        //return super.onOptionsItemSelected(item);
	}




	private void openMap() {
		Fragment frag = setupFragments(tag_map);
		showFragment(frag, tag_map);
	}
	
	private void openList() {
		Fragment frag = setupFragments(tag_list);
		showFragment(frag, tag_list);
	}

    public void openRecordDetail(Fields fields, String recordid) {
        Fragment frag = setupFragments(tag_record_detail, recordid);
        Bundle data = new Bundle();
        data.putParcelable(RecordDetail.TAG_DATA_FIELDS,fields);
        data.putString(RecordDetail.TAG_RECORD_ID,recordid);
        frag.setArguments(data);
        showFragment(frag, recordid);
    }

    @SuppressLint("NewApi")
    public void openRecordDetail(Uri uri) {
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);

        if (cursor == null)
            return;
        cursor.moveToFirst();

        int index_name = cursor.getColumnIndexOrThrow(DatabaseTable.KEY_WORD);
        int index_adress = cursor.getColumnIndexOrThrow(DatabaseTable.KEY_DEFINITION);
        String name  = cursor.getString(index_name);
        String adress  = cursor.getString(index_adress);

        if (search_item!= null){
            search_item.collapseActionView();
        }

        for(Map.Entry<String, Fields> fields : Cafea1e.getRecords_map().entrySet()) {
            if (fields.getValue().getNom().equals(name)){
                openRecordDetail(fields.getValue(),fields.getKey());
            }
        }

//        Fragment frag = setupFragments(tag_record_detail, recordid);
//        Bundle data = new Bundle();
//        data.putParcelable(RecordDetail.TAG_DATA_FIELDS,fields);
//        frag.setArguments(data);
//        showFragment(frag, recordid);
    }

    public Fragment setupFragments(String name) {
        return setupFragments(name,null);
    }
	public Fragment setupFragments(String name, String tag) {
		final FragmentManager fm = getSupportFragmentManager();
		
		Fragment fragment = null;

		if (name.equals(tag_map)) {
			fragment = (GeoLocalisationMap) fm.findFragmentByTag(tag_map);
			if (fragment == null)
				fragment = new GeoLocalisationMap();
		}
        else if(name.equals(tag_record_detail)) {
			fragment = (RecordDetail) fm.findFragmentByTag(tag);
			if (fragment == null) {
				fragment = new RecordDetail();
			}
		}
		else if(name.equals(tag_list)) {
			fragment = (ListFragment) fm.findFragmentByTag(tag_list);
			if (fragment == null) {
				fragment = new ListFragment();
			}
		}
//		else if(name.equals(tag_favorite)) {
//			fragment = (SearchResult) fm.findFragmentByTag(tag_favorite);
//			if (fragment == null) {
//				fragment = new SearchResult();
//			}
//		}
		return fragment;
	}
	
    
    public void showFragment(final Fragment fragment, String tag) {
		if (fragment == null)
			return;

		final FragmentManager fm = getSupportFragmentManager();
		final FragmentTransaction ft = fm.beginTransaction();

		
		if (tag != null)
			ft.replace(R.id.container, fragment, tag);
		else
			ft.replace(R.id.container, fragment);
		
		ft.addToBackStack(tag);
		
		ft.commit();
	}
    
    
    @Override
    public void onConnected(Bundle dataBundle) {
    	super.onConnected(dataBundle);
    	
    	Location mCurrentLocation = mLocationClient.getLastLocation();
        if (mCurrentLocation != null) {
            LatLng pos = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

            parsejson(pos);
        }
        else{
            Toast.makeText(this,"Veuillez activer votre géo localisation",Toast.LENGTH_SHORT).show();
            Cafea1e.setRecords_list_near(new ArrayList<Record>());
            parsejson();
        }
    	
    	final FragmentManager fm = getSupportFragmentManager();
    	
    	GeoLocalisationMap fragment = (GeoLocalisationMap) fm.findFragmentByTag(tag_map);
    	
    	
    	if (fragment.isVisible()){
    		fragment.setUpMap();
    	}
    }

//	public Record[] getRecord_tab() {
//		if (record_tab == null){
//			parsejson();
//		}
//		return record_tab;
//	}
//
//	public void setRecord_tab(Record[] record_tab) {
//		this.record_tab = record_tab;
//
//		final FragmentManager fm = getSupportFragmentManager();
//
//    	GeoLocalisationMap fragment = (GeoLocalisationMap) fm.findFragmentByTag(tag_map);
//
//
//    	if (fragment != null && fragment.isVisible()){
//    		fragment.addRecordTab(record_tab);
//    	}
//
//        //parsejson();
//	}

    public void addRecordList(ArrayList<Record> records_list){
        this.records_list = records_list;
        final FragmentManager fm = getSupportFragmentManager();

        GeoLocalisationMap fragment = (GeoLocalisationMap) fm.findFragmentByTag(tag_map);


        if (fragment != null && fragment.isVisible()){
            fragment.addRecordList(records_list);
            if (records_list.isEmpty()){
                fragment.showParisInMap();
            }
        }

        if (Cafea1e.getRecords_list_near() != null) {
            for (Record record : records_list) {
                insertRecordIntoContentProvider(record.getFields().getNom(), record.getFields().getAdresse() + " - " + record.getFields().getArrondissement());
            }

            //database = new DatabaseTable(this, records_list);
            Cafea1e.setRecords_list(records_list);
        }
        else{
            Cafea1e.setRecords_list_near(records_list);
            parsejson();
        }
    }

    private void insertRecordIntoContentProvider(String name, String adresse){
        ContentValues values = new ContentValues();
        values.put(DatabaseTable.KEY_WORD, name);
        values.put(DatabaseTable.KEY_DEFINITION, adresse);
        getContentResolver().insert(MyCustomSuggestionProvider.CONTENT_URI, values);
        values.clear();
    }

    public void startIntentToGoogleMap(String adress)
    {
        String uri = String.format(Locale.FRENCH, "http://maps.google.com/maps?&daddr=%s", adress);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        try
        {
            startActivity(intent);
        }
        catch(ActivityNotFoundException ex)
        {
            try
            {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            }
            catch(ActivityNotFoundException innerEx)
            {
                Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void changeFavoriteFlag(String record_id){
        final FragmentManager fm = getSupportFragmentManager();

        GeoLocalisationMap fragment = null;

        fragment = (GeoLocalisationMap) fm.findFragmentByTag(tag_map);

        if (fragment != null){
            fragment.changeMarkerFlagToFavoriteFlag(record_id);
        }
    }

//    @Override
//    public void onBackPressed() {
//        final FragmentManager fm = getSupportFragmentManager();
//
//        GeoLocalisationMap fragment = (GeoLocalisationMap) fm.findFragmentByTag(tag_map);
//
//
//        if (fragment.isVisible()){
//            if (!doubleBackToExitPressedOnce) {
//                this.doubleBackToExitPressedOnce = true;
//                Toast.makeText(this, getString(R.string.mess_back_again), Toast.LENGTH_SHORT).show();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        doubleBackToExitPressedOnce = false;
//                    }
//                }, 2000);
//            }
//            else {
////                while(getSupportFragmentManager().getBackStackEntryCount() != 0){
////                    super.onBackPressed();
////                }
//                finish();
//            }
//        }
//        else {
//            if (doubleBackToExitPressedOnce || getSupportFragmentManager().getBackStackEntryCount() != 0) {
//                super.onBackPressed();
//                return;
//            }
//            this.doubleBackToExitPressedOnce = true;
//            Toast.makeText(this, getString(R.string.mess_back_again), Toast.LENGTH_SHORT).show();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    doubleBackToExitPressedOnce = false;
//                }
//            }, 2000);
//        }
//    }
}
