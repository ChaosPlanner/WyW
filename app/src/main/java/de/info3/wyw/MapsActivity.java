package de.info3.wyw;


import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.internal.maps.zzx;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.android.volley.RequestQueue;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
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

    private Marker destinationMarker;
    double startlatitude;
    double startlongitude;

    private ProgressBar ladeKreis;
    public JSONObject carAntwort;
    JSONObject bikeAntwort;
    JSONObject footAntwort;

    String urlcar = "https://api.openrouteservice.org/v2/directions/driving-car/geojson";
    String urlbike = "https://api.openrouteservice.org/v2/directions/cycling-regular/geojson";
    String urlfoot = "https://api.openrouteservice.org/v2/directions/foot-walking/geojson";

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


        /**startKoordinaten = (TextView) findViewById(R.id.textView_start_koordinaten);
        startKoordinaten.setText("15.74646 , 83.84747");*/


        zielKoordinaten = (TextView) findViewById(R.id.textView_ziel_koordinaten);

        Button sucheStarten = (Button) findViewById(R.id.suche_starten_btn);

        sucheStarten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abfrage starten

                if (ZielPosition != null) {
                    //Nach Klicken des Buttons wird jetzt ein Loading-Spinner sichtbar.
                    ladeKreis.setVisibility(View.VISIBLE);

                    //Dann startet der Datenabruf...
                    //später String.valueOf(ZielPosition.longitude),String.valueOf(ZielPosition.latitude)
                    Datenabruf datenabruf1 = new Datenabruf(String.valueOf(startlongitude), String.valueOf(startlatitude),
                            String.valueOf(ZielPosition.longitude), String.valueOf(ZielPosition.latitude), urlcar, new DatenabrufInterface() {

                        //... und wenn der Datenabruf fertig ist,
                        // sorgt das DatenabrufInterface dafür, dass es weiter geht.
                        @Override
                        public void onSuccess(JSONObject response) {
                            Log.i("DatenabrufCar", String.valueOf(response));
                            //Log.i("BikeAntwort", String.valueOf(bikeResponse));

                            carAntwort = response;


                            //...und die nächste Activity wird aufgerufen.

                            if (carAntwort != null && bikeAntwort != null && footAntwort != null) {
                                //Jetzt wird auch die Lade-Animation wieder unsichtbar gemacht...
                                ladeKreis.setVisibility(GONE);
                                Intent intent = new Intent(MapsActivity.this, Ergebnisse.class);
                                intent.putExtra("uebergeben1", String.valueOf(carAntwort));
                                intent.putExtra("uebergeben2", String.valueOf(bikeAntwort));
                                intent.putExtra("uebergeben3", String.valueOf(footAntwort));
                                intent.putExtra("uebergeben4", ZielPosition.latitude);
                                intent.putExtra("uebergeben5", ZielPosition.longitude);
                                intent.putExtra("uebergeben6", startlatitude);
                                intent.putExtra("uebergeben7", startlongitude);
                                startActivity(intent);
                            }


                        }
                    }
                    );

                    Datenabruf datenabrufbike = new Datenabruf(String.valueOf(startlongitude), String.valueOf(startlatitude),
                            String.valueOf(ZielPosition.longitude), String.valueOf(ZielPosition.latitude), urlbike, new DatenabrufInterface() {

                        //... und wenn der Datenabruf fertig ist,
                        // sorgt das DatenabrufInterface dafür, dass es weiter geht.
                        @Override
                        public void onSuccess(JSONObject response) {
                            Log.i("DatenabrufBike", String.valueOf(response));
                            //Log.i("BikeAntwort", String.valueOf(bikeResponse));

                            bikeAntwort = response;


                            //...und die nächste Activity wird aufgerufen.

                            if (carAntwort != null && bikeAntwort != null && footAntwort != null) {
                                //Jetzt wird auch die Lade-Animation wieder unsichtbar gemacht...
                                ladeKreis.setVisibility(GONE);
                                Intent intent = new Intent(MapsActivity.this, Ergebnisse.class);
                                intent.putExtra("uebergeben1", String.valueOf(carAntwort));
                                intent.putExtra("uebergeben2", String.valueOf(bikeAntwort));
                                intent.putExtra("uebergeben3", String.valueOf(footAntwort));
                                intent.putExtra("uebergeben4", ZielPosition.latitude);
                                intent.putExtra("uebergeben5", ZielPosition.longitude);
                                intent.putExtra("uebergeben6", startlatitude);
                                intent.putExtra("uebergeben7", startlongitude);
                                startActivity(intent);
                            }


                        }
                    }
                    );

                    Datenabruf datenabruffoot = new Datenabruf(String.valueOf(startlongitude), String.valueOf(startlatitude),
                            String.valueOf(ZielPosition.longitude), String.valueOf(ZielPosition.latitude), urlfoot, new DatenabrufInterface() {

                        //... und wenn der Datenabruf fertig ist,
                        // sorgt das DatenabrufInterface dafür, dass es weiter geht.
                        @Override
                        public void onSuccess(JSONObject response) {
                            Log.i("DatenabrufFoot", String.valueOf(response));
                            //Log.i("BikeAntwort", String.valueOf(bikeResponse));

                            footAntwort = response;


                            //...und die nächste Activity wird aufgerufen.

                            if (carAntwort != null && bikeAntwort != null && footAntwort != null) {
                                //Jetzt wird auch die Lade-Animation wieder unsichtbar gemacht...
                                ladeKreis.setVisibility(GONE);
                                Intent intent = new Intent(MapsActivity.this, Ergebnisse.class);
                                intent.putExtra("uebergeben1", String.valueOf(carAntwort));
                                intent.putExtra("uebergeben2", String.valueOf(bikeAntwort));
                                intent.putExtra("uebergeben3", String.valueOf(footAntwort));
                                intent.putExtra("uebergeben4", ZielPosition.latitude);
                                intent.putExtra("uebergeben5", ZielPosition.longitude);
                                intent.putExtra("uebergeben6", startlatitude);
                                intent.putExtra("uebergeben7", startlongitude);
                                startActivity(intent);
                            }


                        }
                    }
                    );

                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(datenabruf1.getJsonObjectRequest());
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(datenabrufbike.getJsonObjectRequest());
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(datenabruffoot.getJsonObjectRequest());

                }
                else {
                    Toast.makeText(MapsActivity.this,"Bitte geben Sie ein Ziel an.",Toast.LENGTH_LONG).show();
                }
            }
        });


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
    /**public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("you"));



    googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
        @Override
        public void onMapLongClick(@NonNull LatLng latLng) {
            googleMap.addMarker(new MarkerOptions().position(latLng));
            ZielPosition = latLng;
            zielKoordinaten.setText(latLng.latitude +" , " +latLng.longitude);
        }
    });

    }*/
    public void onMapReady(@NonNull GoogleMap googleMap) {


        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            startlatitude = location.getLatitude();
                            startlongitude = location.getLongitude();

                            TextView startlatitudeTextView = (TextView) findViewById(R.id.startlatitude_textview);
                            startlatitudeTextView.setText(String.format("%.6f", startlatitude));

                            TextView startlongitudeTextView = (TextView) findViewById(R.id.startlongitude_textview);
                            startlongitudeTextView.setText(String.format("%.6f", startlongitude));


                            // Zunächst müssen Sie den aktuellen Standort des Geräts ermitteln.
                            // Dies kann mit Hilfe von GPS oder von Netzwerkorten erfolgen.
                            // In diesem Beispiel verwenden wir eine statische Latitude und Longitude, um den Standort zu simulieren.
                            LatLng currentLocation = new LatLng(startlatitude, startlongitude);

                            // Fügen Sie einen Marker für den aktuellen Standort auf der Karte hinzu.
                            googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));

                            // Zoomen Sie auf die Karte, um den aktuellen Standort anzuzeigen.
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                            //Neu!!!


                            // Setze einen OnMapClickListener, um einen Marker als Zielort hinzuzufügen, wenn auf die Karte geklickt wird
                            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {
                                    // Entferne den vorherigen Marker, falls vorhanden
                                    if (destinationMarker != null) {
                                        destinationMarker.remove();
                                    }

                                    // Füge einen Marker hinzu und speichere die Koordinaten des Markers als Zielort

                                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Zielort");
                                    destinationMarker = googleMap.addMarker(markerOptions);
                                    LatLng destination = destinationMarker.getPosition();


                                    // Hole die aktuelle Position des Markers
                                    LatLng markerPosition = destinationMarker.getPosition();
                                    MarkerOptions newmarkerOptions = new MarkerOptions().position(markerPosition);
                                    destinationMarker = googleMap.addMarker(newmarkerOptions);
                                    LatLng currentPosition = destinationMarker.getPosition();

                                    // Aktualisiere die TextViews mit den Längen- und Breitengradkoordinaten der Position
                                    updateCoordinateTextViews(currentPosition);
                                }
                                private void updateCoordinateTextViews(LatLng position) {
                                    // Aktualisiere die TextViews mit den Längen- und Breitengradkoordinaten der Position

                                    TextView ziellatitudeTextView = (TextView) findViewById(R.id.textView_ziel_koordinaten);
                                    ziellatitudeTextView.setText(String.format("%.6f", position.latitude));

                                    TextView ziellongitudeTextView = (TextView) findViewById(R.id.textView_ziel_koordinaten2);
                                    ziellongitudeTextView.setText(String.format("%.6f", position.longitude));
                                }
                            });
                            // !!!Berechne die Route von dem Startpunkt zum Zielort!!!
                            // !!!Dazu könntest du zum Beispiel die Directions API von Google Maps verwenden!!!



                            /*@Override
                            public void onMapLongClick(@NonNull LatLng latLng;
                            ) {
                                googleMap.addMarker(new MarkerOptions().position(latLng));
                                ZielPosition = latLng;
                                zielKoordinaten.setText(latLng.latitude + " , " + latLng.longitude);
                            }*/
                        }




                    }
                });
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                if (destinationMarker != null) {
                    destinationMarker.remove();
                }

                destinationMarker = googleMap.addMarker(new MarkerOptions().position(latLng));
                ZielPosition = latLng;
                zielKoordinaten.setText(latLng.latitude +" , " +latLng.longitude);
            }
        });
    }
}



