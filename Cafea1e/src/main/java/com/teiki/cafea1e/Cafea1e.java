package com.teiki.cafea1e;

import android.app.Application;

import com.google.android.gms.location.LocationClient;
import com.teiki.cafea1e.business.Fields;
import com.teiki.cafea1e.business.Record;

import java.util.ArrayList;
import java.util.HashMap;

public class Cafea1e extends Application {

    private static HashMap<String,Fields> records_map = new HashMap<String, Fields>();

    private static ArrayList<Record> records_list;

    private static ArrayList<Record> records_list_near;
	
	@Override
	public void onCreate() {
		
		super.onCreate();
	}

    public static HashMap<String, Fields> getRecords_map() {
        return records_map;
    }

    public static void setRecords_map(HashMap<String, Fields> records_map) {
        Cafea1e.records_map = records_map;
    }

    public static void setRecords_list(ArrayList<Record> list){
        if (records_map.isEmpty()) {
            for (Record record : list) {
                records_map.put(record.getRecordid(), record.getFields());
            }
        }
        Cafea1e.records_list = list;
    }

    public static ArrayList<Record> getRecords_list() {
        return records_list;
    }

    public static ArrayList<Record> getRecords_list_near() {
        return records_list_near;
    }

    public static void setRecords_list_near(ArrayList<Record> records_list_near) {
        Cafea1e.records_list_near = records_list_near;
    }
}
