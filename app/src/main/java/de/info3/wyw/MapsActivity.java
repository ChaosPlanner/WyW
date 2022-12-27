package de.info3.wyw;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.internal.maps.zzx;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapView mapView;

    //LÄNGEN und BREITENGRAD

    private Marker destinationMarker;

    boolean isPermissiongranted;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    TextView zielKoordinaten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView = (MapView) findViewById(R.id.map_start);
        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);
        checkPermission();

        zielKoordinaten = (TextView) findViewById(R.id.textView_ziel_koordinaten);

        Button sucheStarten = (Button) findViewById(R.id.suche_starten_btn);

        sucheStarten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abfrage starten

                Intent intent = new Intent(MapsActivity.this, Ergebnisse.class);
                startActivity(intent);
                // expiziten intent uebergeben????

            }
        });

        Datenabruf datenabruf1 = new Datenabruf("8.681495","49.41461","8.686507","49.41943");

        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();


        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(datenabruf1.getJsonObjectRequest());


        Log.i("Datenabruf1", String.valueOf(datenabruf1.getAntwort()));


    }

    private void checkPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPermissiongranted = true;
                Toast.makeText(MapsActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
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
        if (mapViewBundle == null) {
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

    //NICHT MEHR VERÄNDERN!!! NUR ZAHLEN
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {


        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            double startlatitude = location.getLatitude();
                            double startlongitude = location.getLongitude();

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
});}}



