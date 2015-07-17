package com.teiki.cafea1e.server;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.teiki.cafea1e.R;
import com.teiki.cafea1e.business.Fields;
import com.teiki.cafea1e.utils.Tools;
import com.teiki.cafea1e.yelp.YelpV2API;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by antoinegaltier on 01/05/2014.
 */
public class YelpParser extends AsyncTask<Void, Integer, Boolean> {

    private InputStream is;
    private String rawData;

    private static final String TAG_NBHITS = "nhits";
    private static final String TAG_RECORDS = "records";


    private WeakReference<FragmentActivity> mActivity = null;

    private Fields fields;


    public YelpParser(FragmentActivity fragmentActivity, Fields fields) {
        mActivity = new WeakReference<FragmentActivity>(fragmentActivity);
        this.fields = fields;
    }




    @Override
    protected void onPostExecute(Boolean result) {
        if (mActivity != null) {
            if (result) {
                System.out.println(rawData);
            } else {
                Tools.internet_error(mActivity.get());
            }
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        OAuthService service = new ServiceBuilder()
                .provider(YelpV2API.class)
                .apiKey(mActivity.get().getString(R.string.yelp_consumer_key))
                .apiSecret(mActivity.get().getString(R.string.yelp_consumer_secret))
                .build();
        Token accessToken = new Token(mActivity.get().getString(R.string.yelp_token), mActivity.get().getString(R.string.yelp_token_secret));

        // We want to perform a search.
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
        request.addQuerystringParameter("location", fields.getAdresse());
        // Based on a GPS coordinate latitude / longitude
        String geo_pos=String.valueOf(fields.getPos_gps().latitude) + "," + String.valueOf(fields.getPos_gps().longitude);
        request.addQuerystringParameter("cll", geo_pos);
        // Looking for any restaurants
        //request.addQuerystringParameter("term", "food");
        service.signRequest(accessToken, request);
        Response response = request.send();
        rawData = response.getBody();

        return true;


/*        if (url != null) {
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
        return false;*/
    }

/*    private void createfromjson(JsonReader jreader) {
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
                            records_tab[i] = record;
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
    }*/
}