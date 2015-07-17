package com.teiki.cafea1e.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teiki.cafea1e.Cafea1e;
import com.teiki.cafea1e.activity.MainActivity;
import com.teiki.cafea1e.R;
import com.teiki.cafea1e.adapter.InfoWindowsMarker;
import com.teiki.cafea1e.business.Fields;
import com.teiki.cafea1e.business.Record;
import com.teiki.cafea1e.utils.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class GeoLocalisationMap extends Fragment {
	
	private SupportMapFragment fragment;
	private GoogleMap mMap;

    //private ArrayList<Record> records_list = new ArrayList<Record>();
    private LinkedHashMap<String,Fields> records_map = new LinkedHashMap<String, Fields>();
    private LinkedHashMap<String,Marker> markers_map = new LinkedHashMap<String, Marker>();

    private Boolean everybody_is_here=false;

    private float initial_zoom;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.map, container, false);
		
		FragmentManager fm = getChildFragmentManager();
		fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
		if (fragment == null) {
			fragment = SupportMapFragment.newInstance();
			fm.beginTransaction().replace(R.id.map, fragment).commit();
		}
		
		mMap = fragment.getMap();

		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setMap();
	}
	
	private void setMap(){
		if (mMap == null) {
			mMap = fragment.getMap();
		}
	}
	
	@SuppressLint("NewApi")
	public void setUpMap() {
		//setMap();
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setMyLocationEnabled(true);


		Location mCurrentLocation;
	    mCurrentLocation = ((MainActivity)getActivity()).mLocationClient.getLastLocation();
        if (mCurrentLocation != null) {
            LatLng pos = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
//	    Marker current_pos_marker = mMap.addMarker(
//	    		new MarkerOptions()
//	    		.position(pos));
            // .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));

            // Move the camera instantly to hamburg with a zoom of 15.
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
            showInMap(pos);

        }
        else{
            showParisInMap();
        }
        initial_zoom = mMap.getCameraPosition().zoom;

        // Zoom in, animating the camera.
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                ((MainActivity)getActivity()).openRecordDetail(records_map.get(marker.getTitle()),marker.getTitle());
            }
        });



    }

    public void showParisInMap(){
        LatLng pos = new LatLng(48.853, 2.35);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 12));
    }

    public void showInMap(LatLng pos){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
    }

    public void addRecordList(ArrayList<Record> record_list) {
        mMap.clear();
        records_map.clear();
        markers_map.clear();
        int i=0;
        for(Record record : record_list){
            addRecord(record);
            i++;
        }

        //Cafea1e.setRecords_map(records_map);

        InfoWindowsMarker infoadapter = new InfoWindowsMarker(getActivity(),getActivity().getLayoutInflater(),records_map);
        mMap.setInfoWindowAdapter(infoadapter);

        setFavoriteFlag();
    }


	
	public void addRecord(Record record){
		if (record != null) {
            Marker new_marker = null;
            if (record.getFields().getPos_gps() != null) {
                new_marker = mMap.addMarker(
                        new MarkerOptions()
                                .position(record.getFields().getPos_gps())
                                .title(record.getRecordid())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_record))
                );
            }
            else if (record.getFields().getAdresse() != null) {
                LatLng gps_pos = Tools.getLatLngFromAdress(getActivity(), record.getFields().getAdresse());
                if (gps_pos != null){
                    new_marker = mMap.addMarker(
                            new MarkerOptions()
                                    .position(Tools.getLatLngFromAdress(getActivity(), record.getFields().getAdresse()))
                                    .title(record.getRecordid())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_record))
                    );
                }

            }
            else
            {
                //Toast.makeText(getActivity(),"hey",Toast.LENGTH_SHORT).show();
            }
            if (new_marker != null) {
                records_map.put(record.getRecordid(), record.getFields());
                markers_map.put(record.getRecordid(), new_marker);
            }
            //records_list.add(record);
		}

//        // Move the camera instantly to hamburg with a zoom of 15.
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
//
//        // Zoom in, animating the camera.
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
	}

    private void setFavoriteFlag(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Map<String,Boolean> favorite_marker_map = null;
        try {
            favorite_marker_map = (Map<String, Boolean>) preferences.getAll();
        } catch (ClassCastException e) {
        }
        if (favorite_marker_map != null && !favorite_marker_map.isEmpty()){
            Iterator it = favorite_marker_map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry)it.next();
                changeMarkerFlagToFavoriteFlag((String) pairs.getKey());
                it.remove(); // avoids a ConcurrentModificationException
            }
        }
    }

    public void changeMarkerFlagToFavoriteFlag(String record_id){
        Marker favorite_marker = markers_map.get(record_id);
        if (favorite_marker != null){
            favorite_marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_record_fav));
        }
    }

}
