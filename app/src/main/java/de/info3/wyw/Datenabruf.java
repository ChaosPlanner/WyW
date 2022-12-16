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
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.text.BreakIterator;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Datenabruf {

    String url = "https://httpbin.org/delay/2";

    private JSONObject antwort;

    final String api_key = "5b3ce3597851110001cf624816705a0e98e34a77b89558cd6145883c";

    public Datenabruf() {}

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> superHeaders = new HashMap<>(super.getHeaders());
                        superHeaders.put("Authorization", api_key);
                        return Collections.unmodifiableMap(superHeaders);
                    }


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
                        // TODO: Handle error

                    }
                });

    public JSONObject getAntwort() {
        return antwort;
    }

    public void setAntwort(JSONObject antwort) {
        this.antwort = antwort;
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
