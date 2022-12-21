package de.info3.wyw;



import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.BreakIterator;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Datenabruf  {

    String url = "https://api.openrouteservice.org/v2/directions/driving-car/geojson";

    private JSONObject antwort;

    private JSONObject requestData;

    final String api_key = "5b3ce3597851110001cf624816705a0e98e34a77b89558cd6145883c";

    private JsonObjectRequest jsonObjectRequest;

    public Datenabruf(String startLaenge, String startBreite, String zielLaenge, String zielBreite) {

        try {
            requestData = new JSONObject("{\"coordinates\":[[" + startLaenge + "," + startBreite + "],[" + zielLaenge + "," + zielBreite + "]],\"alternative_routes\":{\"target_count\":3,\"weight_factor\":1.6}}\")");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setRequestData(requestData);

        jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, getRequestData(), new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {

                        setAntwort(response);

                        //BreakIterator textView = null;

                        Log.i("JSONAntwort:", String.valueOf(response));

                        //textView.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Log.e("JSONFehler", new String(error.networkResponse.data, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        // TODO: Handle error

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> superHeaders = new HashMap<>(super.getHeaders());
                superHeaders.put("Authorization", api_key);
                return Collections.unmodifiableMap(superHeaders);
            }
        };
    }

    public JSONObject getAntwort() {
        return antwort;
    }

    public void setAntwort(JSONObject antwort) {
        this.antwort = antwort;
    }

    public void setRequestData(JSONObject requestData) {

        this.requestData = requestData;
    }

    public JSONObject getRequestData() {
        return requestData;
    }

    public JsonObjectRequest getJsonObjectRequest() {
        return jsonObjectRequest;
    }

    /**RequestQueue requestQueue;

    // Instantiate the cache
    Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

    private DiskBasedCache.FileSupplier getCacheDir() {
        return null;
    }

    // Set up the network to use HttpURLConnection as the HTTP client.
    Network network = new BasicNetwork(new HurlStack());

    // Instantiate the RequestQueue with the cache and network.
    requestQueue = new RequestQueue(cache, network);

    // Start the queue
    requestQueue.start();

    String url = "http://www.example.com";

    // Formulate the request and handle the response.
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Do something with the response
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Handle error
                }
            });

    // Add the request to the RequestQueue.
    requestQueue.add(stringRequest);

    // Get a RequestQueue
    RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).
            getRequestQueue();


    // Add a request (in this example, called stringRequest) to your RequestQueue.
    MySingleton.getInstance(this).addToRequestQueue(stringRequest);**/

}
