package com.teiki.cafea1e.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.teiki.cafea1e.R;
import com.teiki.cafea1e.activity.MainActivity;
import com.teiki.cafea1e.business.Fields;
import com.teiki.cafea1e.server.YelpParser;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by antoinegaltier on 29/04/2014.
 */

public class RecordDetail extends Fragment {

    public final static String TAG_DATA_FIELDS = "fields";
    public final static String TAG_RECORD_ID = "record_id";

    private TextView title;
    private TextView adress;
    private TextView price_coffee;
    private Button go_to_map;
    private RadioButton btn_favorite;

    private Fields fields;
    private String record_id;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.record_detail, container, false);

        Bundle data = getArguments();
        if (data != null) {
            this.fields = data.getParcelable(TAG_DATA_FIELDS);
            this.record_id = data.getString(TAG_RECORD_ID);
        }

        title = (TextView) view.findViewById(R.id.title);
        adress = (TextView) view.findViewById(R.id.record_adress);
        go_to_map = (Button) view.findViewById(R.id.go_to_map);
        price_coffee = (TextView) view.findViewById(R.id.price_coffee);
        btn_favorite = (RadioButton) view.findViewById(R.id.favorite);

        setInfoFields(fields);

        AsyncTask<Void, Integer, Boolean> asynctask = new YelpParser(getActivity(),fields);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB_MR1) {
            asynctask.execute();
        } else {
            asynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = preferences.edit();
                if (preferences.getBoolean(record_id, false))
                {
                    editor.remove(record_id);
                    btn_favorite.setChecked(false);
                }
                else
                {
                    editor.putBoolean(record_id, true);
                    btn_favorite.setChecked(true);
                    ((MainActivity)getActivity()).changeFavoriteFlag(record_id);
                }
                editor.apply();
            }
        });

        return view;
    }

    private void setInfoFields(final Fields fields) {
        title.setText(fields.getNom());
        adress.setText(fields.getAdresse());
        price_coffee.setText(getString(R.string.coffee_price) + String.valueOf(fields.getPrix_comptoir()) + " €");
        go_to_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).startIntentToGoogleMap(fields.getAdresse());
            }
        });
        setFavoriteFlag();
    }

    private void setFavoriteFlag(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Map<String,Boolean> favorite_record_map = null;
        try {
            favorite_record_map = (Map<String, Boolean>) preferences.getAll();
        } catch (ClassCastException e) {
        }
        if (favorite_record_map != null && !favorite_record_map.isEmpty()){
            btn_favorite.setChecked(favorite_record_map.get(record_id) != null);
        }
    }
}
