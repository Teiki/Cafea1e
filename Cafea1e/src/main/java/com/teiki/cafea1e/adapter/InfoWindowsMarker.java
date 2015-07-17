package com.teiki.cafea1e.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.teiki.cafea1e.R;
import com.teiki.cafea1e.business.Fields;

import java.util.HashMap;

public class InfoWindowsMarker implements InfoWindowAdapter {

	private View mWindow;
    private HashMap<String,Fields> records_map;
    private Context ctx;

     public InfoWindowsMarker(Context ctx, LayoutInflater layoutinflater, HashMap<String,Fields> records_map) {
    	 mWindow = layoutinflater.inflate(R.layout.custom_info_window, null);
         this.records_map = records_map;
         this.ctx = ctx;
     }

     @Override
     public View getInfoWindow(Marker marker) {
    	 
         render(marker, mWindow);
         return mWindow;
     }

     @Override
     public View getInfoContents(Marker marker) {
//         if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_contents) {
//             // This means that the default info contents will be used.
//             return null;
//         }
//         render(marker, mContents);
//         return mContents;
    	 return null;
     }

     private void render(Marker marker, View view) {
         int badge = R.drawable.ic_launcher;
         ((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

         String title = records_map.get(marker.getTitle()).getNom();
         TextView titleUi = ((TextView) view.findViewById(R.id.title));
         if (title != null) {
             // Spannable string allows us to edit the formatting of the text.
             SpannableString titleText = new SpannableString(title);
             titleText.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, titleText.length(), 0);
             titleUi.setText(titleText);
         } else {
             titleUi.setText("");
         }

         String snippet = records_map.get(marker.getTitle()).getAdresse();
         TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
         if (snippet != null) {
             snippetUi.setText(snippet);
         } else {
             snippetUi.setText("");
         }

         TextView coffee_price = ((TextView) view.findViewById(R.id.coffee_price));
         double record_price = records_map.get(marker.getTitle()).getPrix_comptoir();
         if (record_price != 0 )
            coffee_price.setText(ctx.getString(R.string.coffee_price)+ " " + String.valueOf(record_price));
         else
             coffee_price.setVisibility(View.GONE);
     }

}
