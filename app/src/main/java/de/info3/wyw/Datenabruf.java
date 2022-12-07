package de.info3.wyw;



import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

public class Datenabruf {

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
