package de.info3.wyw;


import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONObject;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    boolean isPermissiongranted;
    MapView mapView;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private LatLng StartPosition;
    private LatLng ZielPosition = null;
    TextView startKoordinaten;
    TextView zielKoordinaten;

    private ProgressBar ladeKreis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ladeKreis = (ProgressBar) findViewById(R.id.progressBar1);
        ladeKreis.setVisibility(GONE);

        Bundle mapViewBundle = null;
        if(savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView = (MapView) findViewById(R.id.map_start);
        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);
        checkPermission();


        startKoordinaten = (TextView) findViewById(R.id.textView_start_koordinaten);
        startKoordinaten.setText("15.74646 , 83.84747");


        zielKoordinaten = (TextView) findViewById(R.id.textView_ziel_koordinaten);

        Button sucheStarten = (Button) findViewById(R.id.suche_starten_btn);

        sucheStarten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abfrage starten

                //Nach Klicken des Buttons wird jetzt ein Loading-Spinner sichtbar.
                ladeKreis.setVisibility(View.VISIBLE);

                //Dann startet der Datenabruf...
                Datenabruf datenabruf1 = new Datenabruf("8.681495","49.41461","8.686507","49.41943", new DatenabrufInterface(){

                    //... und wenn der Datenabruf fertig ist,
                    // sorgt das DatenabrufInterface dafür, dass es weiter geht.
                    @Override
                    public void onSuccess(JSONObject response){
                        Log.i("Datenabruf2", String.valueOf(response));

                        //Jetzt wird auch die Lade-Animation wieder unsichtbar gemacht...
                        ladeKreis.setVisibility(GONE);

                        //...und die nächste Activity wird aufgerufen.
                        Intent intent = new Intent(MapsActivity.this,Ergebnisse.class);
                        startActivity(intent);

                    }
                }
                );
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(datenabruf1.getJsonObjectRequest());


                // expiziten intent uebergeben????

            }
        });


        //RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        // Access the RequestQueue through your singleton class.
        //Log.i("Datenabruf1", String.valueOf(datenabruf1.getAntwort()));


    }

    private void checkPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPermissiongranted = true;
                Toast.makeText(MapsActivity.this,"Permission granted",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",getPackageName(),"");
            intent.setData(uri);
            startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
            permissionToken.continuePermissionRequest();
            }
        }).check();
    }



    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if(mapViewBundle == null) {
             mapViewBundle = new Bundle();
             outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("you"));


    googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
        @Override
        public void onMapLongClick(@NonNull LatLng latLng) {
            googleMap.addMarker(new MarkerOptions().position(latLng));
            ZielPosition = latLng;
            zielKoordinaten.setText(latLng.latitude +" , " +latLng.longitude);
        }
    });

    }
}



