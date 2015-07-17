package com.teiki.cafea1e.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.teiki.cafea1e.Cafea1e;
import com.teiki.cafea1e.R;
import com.teiki.cafea1e.activity.MainActivity;
import com.teiki.cafea1e.business.Fields;
import com.teiki.cafea1e.business.Record;
import com.teiki.cafea1e.lazylist.LazyAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ListFragment extends android.support.v4.app.ListFragment {
	
	ListView list;
	
	ArrayList<Record> records_list;

    private TextView name;
    private TextView adress;
    private TextView distance;
	
	LazyAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list, container, false);

        //list.setEmptyView();

		return view;
	}

    @Override
    public void onResume() {
        super.onResume();
        list = getListView();

        setList(Cafea1e.getRecords_list_near());
    }

    private void setList(final ArrayList<Record> records_list){
		this.records_list = records_list;



        if (adapter == null)
		    adapter = new LazyAdapter(getActivity(), records_list);

		list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity)getActivity()).openRecordDetail(records_list.get(position).getFields(),records_list.get(position).getRecordid());
            }
        });
	}

}
