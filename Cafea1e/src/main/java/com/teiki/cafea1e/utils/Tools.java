package com.teiki.cafea1e.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.maps.model.LatLng;
import com.teiki.cafea1e.R;

import java.io.IOException;
import java.util.List;

public class Tools {
	
	// remplace les points par des balise html de saut ?? la ligne
	public static String format (String text) {
		StringBuilder result = new StringBuilder();
		int i = 0;
		int indexof;
		
		while (i<text.length()-1 && (indexof = text.indexOf(46, i)) != -1) {
			result.append(text.substring(i, indexof+1));
			if ( indexof +1 < text.length()-1) {
				
			if (text.charAt(indexof + 1) == (char) 32) {
				result.append(" <br /> ");
			}
			i = indexof+1;
			}
		}
		
		if (i<text.length()-1)
			result.append(text.substring(i));
		
		return result.toString();
	}
	
	public static boolean isOnline(Context ctx) {
	    ConnectivityManager cm =
	        (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected()) {
	        return true;
	    }
	    return false;
	}
	
	public static void internet_error(Context ctx) {
		final AlertDialog alertDialog = new  AlertDialog.Builder(ctx).create();
		alertDialog.setMessage(ctx.getString(R.string.Network_ConnectionError_Message));
		alertDialog.setTitle(ctx.getString(R.string.Network_ConnectionError_Title));
		alertDialog.setCancelable(false);
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.cancel();
			}
		});
		alertDialog.show ();
	}
	
	public static String formatString(String string) {
		char[] charsData = new char[string.length()];
		string.getChars(0, charsData.length, charsData, 0);

		char c;
		for (int i = 0; i < charsData.length; i++) {
			if ((c = charsData[i]) >= 'A' && c <= 'Z') {
				charsData[i] = (char) (c - 'A' + 'a');
			} else {
				switch (c) {
				case '\u00e0':
				case '\u00e2':
				case '\u00e4':
					charsData[i] = 'a';
					break;
				case '\u00e7':
					charsData[i] = 'c';
					break;
				case '\u00e8':
				case '\u00e9':
				case '\u00ea':
				case '\u00eb':
					charsData[i] = 'e';
					break;
				case '\u00ee':
				case '\u00ce':
				case '\u00ef':
					charsData[i] = 'i';
					break;
				case '\u00f4':
				case '\u00f6':
					charsData[i] = 'o';
					break;
				case '\u00f9':
				case '\u00fb':
				case '\u00fc':
					charsData[i] = 'u';
					break;
				}
			}
		}

		return new String(charsData);
	}

    public static LatLng getLatLngFromAdress(Context ctx, String strAddress) {
        Geocoder coder = new Geocoder(ctx);
        List<Address> address = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (address == null || address.isEmpty()) {
            return null;
        }
        Address location = address.get(0);

        if (location.getLatitude() != 0 && location.getLongitude() != 0) {
            LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
            return pos;
        }
        return null;
    }

}
