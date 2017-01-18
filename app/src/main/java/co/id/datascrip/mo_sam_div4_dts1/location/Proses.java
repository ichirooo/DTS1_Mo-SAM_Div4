package co.id.datascrip.mo_sam_div4_dts1.location;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import co.id.datascrip.mo_sam_div4_dts1.callback.CallbackLocation;
import co.id.datascrip.mo_sam_div4_dts1.custom.TimeOutDialog;

public class Proses {

    private static String getJSONdata(String strUrl) throws IOException {
        String data = null;
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Proses->getJSONdata", e.toString());
        } finally {
            if (iStream != null)
                iStream.close();
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return data;
    }

    public static class getLocation extends AsyncTask<String, Integer, HashMap<String, String>> {

        CallbackLocation.CBLocation cb;
        PlaceDetailsJSONParser parser = new PlaceDetailsJSONParser();
        Context context;
        int r;
        boolean rto;
        String data = "";
        ProgressDialog d;

        public getLocation(Context context, CallbackLocation.CBLocation cb) {
            this.cb = cb;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            d = new ProgressDialog(context);
            d.setMessage("Loading...");
            d.setIndeterminate(true);
            d.setCancelable(false);
            d.show();
        }

        @Override
        protected HashMap<String, String> doInBackground(String... params) {
            // TODO Auto-generated method stub
            String address = null;
            JSONObject jo = null;

            try {
                address = URLEncoder.encode(params[0], "utf-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                    address + "&sensor=false";
            try {
                data = getJSONdata(url);
                if (data != null) {
                    jo = new JSONObject(data);
                    r = jo.getJSONArray("results").length();
                } else return null;
            } catch (SocketTimeoutException e) {
                rto = true;
                return null;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                data = e.toString();
                return null;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                data = e.toString();
                return null;
            } catch (Exception e) {
                data = e.toString();
                return null;
            }

            if (r > 0) {
                return parser.parse(jo);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(HashMap<String, String> locations) {
            // TODO Auto-generated method stub
            if (rto) new TimeOutDialog(context).show();
            else cb.onCB(locations);
            d.dismiss();
        }
    }
}
