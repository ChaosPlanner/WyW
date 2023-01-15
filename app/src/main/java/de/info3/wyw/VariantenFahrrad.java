package de.info3.wyw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonLineStringStyle;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VariantenFahrrad extends AppCompatActivity implements OnMapReadyCallback {
    boolean isPermissiongranted;
    MapView mapView;
    GoogleMap map;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    int i =0;
    int j = 0;
    int k = 0;

    //Antworten die aus der Ergebnisse-Activity übergeben werden:
    JSONObject antwortCar = null;
    JSONObject antwortBike = null;
    JSONObject antwortFoot = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_varianten_fahrrad);
        Bundle mapViewBundle = null;
        if(savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView = (MapView) findViewById(R.id.map_varianten_fahrrad);
        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);
        checkPermission();

        try {
            antwortBike = new JSONObject(getIntent().getStringExtra("uebergeben2"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray features = null;

        String Entfernung = null;
        String Zeit = null;
        try {
            features = antwortBike.getJSONArray("features");
            JSONObject featureBike = features.getJSONObject(0);
            JSONObject propertiesBike = featureBike.getJSONObject("properties");
            JSONObject summaryBike = propertiesBike.getJSONObject("summary");
            Entfernung = String.valueOf(summaryBike.getDouble("distance"));
            Zeit = String.valueOf(summaryBike.getDouble("duration"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView fahrradLaenge = (TextView) findViewById(R.id.txt_laenge_fahrrad_route1);
        fahrradLaenge.setText(Entfernung+" m");

        TextView fahrradZeit = (TextView) findViewById(R.id.txt_zeit_fahrrad_route1);
        fahrradZeit.setText(Zeit+" sec");

        JSONArray features2 = null;

        String Entfernung2 = null;
        String Zeit2 = null;
        try {
            features2 = antwortBike.getJSONArray("features");
            JSONObject featureBike2 = features2.getJSONObject(1);
            JSONObject propertiesBike2 = featureBike2.getJSONObject("properties");
            JSONObject summaryBike2 = propertiesBike2.getJSONObject("summary");
            Entfernung2 = String.valueOf(summaryBike2.getDouble("distance"));
            Zeit2 = String.valueOf(summaryBike2.getDouble("duration"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView fahrradLaenge2 = (TextView) findViewById(R.id.txt_laenge_fahrrad_route2);
        fahrradLaenge2.setText(Entfernung2+" m");

        TextView fahrradZeit2 = (TextView) findViewById(R.id.txt_zeit_fahrrad_route2);
        fahrradZeit2.setText(Zeit2+" sec");

        JSONArray features3 = null;

        String Entfernung3 = null;
        String Zeit3 = null;
        try {
            features3 = antwortBike.getJSONArray("features");
            JSONObject featureBike3 = features3.getJSONObject(2);
            JSONObject propertiesBike3 = featureBike3.getJSONObject("properties");
            JSONObject summaryBike3 = propertiesBike3.getJSONObject("summary");
            Entfernung3 = String.valueOf(summaryBike3.getDouble("distance"));
            Zeit3 = String.valueOf(summaryBike3.getDouble("duration"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView fahrradLaenge3 = (TextView) findViewById(R.id.txt_laenge_fahrrad_route3);
        fahrradLaenge3.setText(Entfernung3+" m");

        TextView fahrradZeit3 = (TextView) findViewById(R.id.txt_zeit_fahrrad_route3);
        fahrradZeit3.setText(Zeit3+" sec");
/*
        TextView auto1Laenge = (TextView) findViewById(R.id.txt_laenge_auto_route1);
        auto1Laenge.setText(Entfernung);

        TextView auto1Zeit = (TextView) findViewById(R.id.txt_zeit_auto_route1);
        auto1Zeit.setText(Zeit);

        TextView auto1Co2 = (TextView) findViewById(R.id.co2_auto_route1);
        auto1Co2.setText(CO2);

        TextView auto2Laenge = (TextView) findViewById(R.id.txt_laenge_auto_route2);
        auto2Laenge.setText(Entfernung);

        TextView auto2Zeit = (TextView) findViewById(R.id.txt_zeit_auto_route2);
        auto2Zeit.setText(Zeit);

        TextView auto2Co2 = (TextView) findViewById(R.id.co2_auto_route2);
        auto2Co2.setText(CO2);

        TextView auto3Laenge = (TextView) findViewById(R.id.txt_laenge_auto_route3);
        auto3Laenge.setText(Entfernung);

        TextView auto3Zeit = (TextView) findViewById(R.id.txt_zeit_auto_route3);
        auto3Zeit.setText(Zeit);

        TextView auto3Co2 = (TextView) findViewById(R.id.co2_auto_route3);
        auto3Co2.setText(CO2);


 */




    }

    private void checkPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPermissiongranted = true;
                Toast.makeText(VariantenFahrrad.this,"Permission granted",Toast.LENGTH_SHORT).show();

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

        try {
            antwortBike = new JSONObject(getIntent().getStringExtra("uebergeben1"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GeoJsonLayer layerBike = new GeoJsonLayer(googleMap, antwortBike);

        layerBike.addLayerToMap();

        //GeoJsonFeature lineStringFeatureCar1 = (GeoJsonFeature) layerCar.getFeature(1);

        /**GeoJsonLineStringStyle lineStringStyle = layerCar.getDefaultLineStringStyle();
         lineStringStyle.setColor(getResources().getColor(R.color.auto1));*/

        GeoJsonLineStringStyle lineStringStyleBike1 = layerBike.getDefaultLineStringStyle();
        //lineStringStyleCar1.setColor(getResources().getColor(R.color.fahrrad1));
        //lineStringFeatureCar1.setLineStringStyle(lineStringStyleCar1);

        Integer[] colours = {getResources().getColor(R.color.auto1),getResources().getColor(R.color.fahrrad1)
                ,getResources().getColor(R.color.fuss1)};

        /**for (GeoJsonFeature feature : layerBike.getFeatures()) {
         // Do something to the feature

         lineStringStyleBike1.setColor(colours[i]);
         feature.setLineStringStyle(lineStringStyleBike1);
         i++;
         }*/


        LatLng start = new LatLng(49.41461,8.681495);
        googleMap.addMarker(new MarkerOptions().position(start));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(start));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start,15));

    }
}