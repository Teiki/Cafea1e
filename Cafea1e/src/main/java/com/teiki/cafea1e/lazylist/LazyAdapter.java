 package com.teiki.cafea1e.lazylist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teiki.cafea1e.R;
import com.teiki.cafea1e.business.Record;

import java.util.ArrayList;

 public class LazyAdapter extends BaseAdapter {
    
	
    private Activity activity;
    private ArrayList<Record> records_list;
    private static LayoutInflater inflater;
    
	public LazyAdapter(Activity a, ArrayList<Record> records_list) {
		activity = a;
		this.records_list = records_list;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

    public int getCount() {
        return records_list.size();
    }

    public Record getItem(int position) {
        return records_list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View ligne=convertView;
        
        if(convertView==null) {
        	ligne = inflater.inflate(R.layout.list_row, null);
        }
        
        Record item = getItem(position);
        
        TextView name;
        TextView adress;
        TextView distance;

        name = (TextView) ligne.findViewById(R.id.name);
		adress = (TextView) ligne.findViewById(R.id.adress);
        distance = (TextView) ligne.findViewById(R.id.distance);

		
		name.setText(item.getFields().getNom());
		adress.setText(item.getFields().getAdresse()+ " - " + item.getFields().getArrondissement());
        distance.setText(item.getFields().getDistance() + " mètre");
		
        return ligne;
    }
}