package itp341.barbosa.marlon.musicoftroy.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

public class JSONParser {
	static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    static JSONArray jArr = null;
    static String TAG = JSONParser.class.getSimpleName();
    
    
    public static JSONArray getJSONArrayFromUrl(String url) throws UnsupportedEncodingException, ClientProtocolException, IOException, JSONException   {

        // Making HTTP request
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();


            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();


        // try parse the string to a JSON object
            jArr = (JSONArray) new JSONTokener(json).nextValue();

        // return JSON String
        return jArr;
    }
    
    public static JSONObject getJSONObjectFromUrl(String url) throws UnsupportedEncodingException, ClientProtocolException, IOException, JSONException {

        // Making HTTP request
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();


        // try parse the string to a JSON object
       
            jObj = new JSONObject(json);

        // return JSON String
        return jObj;
    }
    

    public static String getJSONStringFromUrl(String url) throws UnsupportedEncodingException, ClientProtocolException, IOException {

        // Making HTTP request
            // defaultHttpClient
        	Log.d(TAG, "Making HTTP request");
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();


            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            json = sb.toString();
    

   

        // return JSON String
        Log.d(TAG, "response is: " + json);
        return json;

    }
}
