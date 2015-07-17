package com.teiki.cafea1e.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import com.teiki.cafea1e.activity.MainActivity;
import com.teiki.cafea1e.business.Record;
import com.teiki.cafea1e.data.MyCustomSuggestionProvider;
import com.teiki.cafea1e.utils.Tools;

public class GsonRecordsList extends AsyncTask<Void, Integer, Boolean> {
	
	private InputStream is;

	private static final String TAG_NBHITS = "nhits";
	private static final String TAG_RECORDS = "records";
	

	private WeakReference<FragmentActivity> mActivity = null;
    private WeakReference<MyCustomSuggestionProvider> mProvider = null;

	private String url;
	private int records_total_number;
    private ArrayList<Record> records_list;

	public GsonRecordsList(FragmentActivity fragmentActivity, String url, LatLng pos) {
		mActivity = new WeakReference<FragmentActivity>(fragmentActivity);
        records_list = new ArrayList<Record>();
		this.url = url;
		if (url == null) {
			this.cancel(true);
		}
	}

    public GsonRecordsList(FragmentActivity fragmentActivity, String url) {
        mActivity = new WeakReference<FragmentActivity>(fragmentActivity);
        records_list = new ArrayList<Record>();
        this.url = url;
        if (url == null) {
            this.cancel(true);
        }
    }

    public GsonRecordsList(MyCustomSuggestionProvider provider, String url) {
        mProvider = new WeakReference<MyCustomSuggestionProvider>(provider);
        records_list = new ArrayList<Record>();
        this.url = url;
        if (url == null) {
            this.cancel(true);
        }
    }
	

	@Override
	protected void onPostExecute(Boolean result) {

			if (result) {
                if (mActivity != null) {
                    if (records_list != null)
                        ((MainActivity) mActivity.get()).addRecordList(records_list);
                    ((MainActivity) mActivity.get()).NB_BAR_TOTAL = records_total_number;
                }
                else if (mProvider != null) {
                    mProvider.get().setRecordsList(records_list);
                }
			} else {
		    	Tools.internet_error(mActivity.get());
			}
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		if (url != null) {
			downloader(url);
			
			InputStreamReader inputStreamReader = null;
		    BufferedReader bufferedReader = null;
		    JsonReader jreader = null;
		    
		    if (is == null) {
		    	return false;
		    }
		    
		    inputStreamReader = new InputStreamReader(is);
		    bufferedReader = new BufferedReader(inputStreamReader);
		    jreader = new JsonReader(bufferedReader);
		    
		    try {
		    	createfromjson(jreader);
				jreader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	
			return true;
		}
		return false;
	}
		
	private void createfromjson(JsonReader jreader) {
		try {
			jreader.beginObject();
			Gson gson = new Gson();
			
			while(jreader.hasNext()) {
				final String name = jreader.nextName();
				if (jreader.peek() != JsonToken.NULL && name.equals(TAG_NBHITS)) {
					records_total_number = jreader.nextInt();
				}
				else{
					if (jreader.peek() != JsonToken.NULL && name.equals(TAG_RECORDS)) {
						jreader.beginArray();
						int i = 0;
						while (jreader.hasNext() && !isCancelled()) {
							final Record record = gson.fromJson(jreader, Record.class);
                            if (records_list != null)
							    records_list.add(record);
							i++;
						}
						jreader.endArray();
					}
					else {
						jreader.skipValue();
					}
				}
				
			}
			jreader.endObject();
			publishProgress(-1);
			
		} catch (MalformedJsonException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	private void downloader(String url) {
		try {
			final int timeoutConnection = 3000;
		    final int timeoutSocket = 5000;
			
			// defaultHttpClient
			HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpGet httpGet = new HttpGet(url);
            //HttpPost httpPost = new HttpPost(url);
 
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();        

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}