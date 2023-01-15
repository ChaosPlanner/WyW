package de.info3.wyw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonLineStringStyle;
import com.google.maps.android.data.geojson.GeoJsonMultiLineString;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

public class VariantenAuto extends AppCompatActivity implements OnMapReadyCallback {

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

    LatLng ZielPosition = null;
    LatLng StartPosition = null;

    double startPositionLat;
    double startPositionlong;

    ArrayList<GeoJsonFeature> listCarRed = new ArrayList<GeoJsonFeature>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_varianten_auto);

        Bundle mapViewBundle = null;
        if(savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView = (MapView) findViewById(R.id.map_varianten_auto);
        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);
        checkPermission();

        try {
            antwortCar = new JSONObject(getIntent().getStringExtra("uebergeben1"));
            double ZielPositionLat = getIntent().getDoubleExtra("uebergeben4",0);
            double ZielPositionlong = getIntent().getDoubleExtra("uebergeben5",0);
            ZielPosition = new LatLng(ZielPositionLat,ZielPositionlong);
            startPositionLat = getIntent().getDoubleExtra("uebergeben6",0);
            startPositionlong = getIntent().getDoubleExtra("uebergeben7",0);
            StartPosition = new LatLng(startPositionLat,startPositionlong);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray features = null;

        String Entfernung = null;
        String Zeit = null;
        try {
            features = antwortCar.getJSONArray("features");
            JSONObject featureCar = features.getJSONObject(0);
            JSONObject propertiesCar = featureCar.getJSONObject("properties");
            JSONObject summaryCar = propertiesCar.getJSONObject("summary");
            Entfernung = String.valueOf(summaryCar.getDouble("distance"));
            Zeit = String.valueOf(summaryCar.getDouble("duration"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView autoLaenge = (TextView) findViewById(R.id.txt_laenge_auto_route1);
        autoLaenge.setText(Entfernung+" m");

        TextView autoZeit = (TextView) findViewById(R.id.txt_zeit_auto_route1);
        autoZeit.setText(Zeit+" sec");

        JSONArray features2 = null;

        String Entfernung2 = null;
        String Zeit2 = null;
        try {
            features2 = antwortCar.getJSONArray("features");
            JSONObject featureCar2 = features2.getJSONObject(1);
            JSONObject propertiesCar2 = featureCar2.getJSONObject("properties");
            JSONObject summaryCar2 = propertiesCar2.getJSONObject("summary");
            Entfernung2 = String.valueOf(summaryCar2.getDouble("distance"));
            Zeit2 = String.valueOf(summaryCar2.getDouble("duration"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView autoLaenge2 = (TextView) findViewById(R.id.txt_laenge_auto_route2);
        autoLaenge2.setText(Entfernung2+" m");

        TextView autoZeit2 = (TextView) findViewById(R.id.txt_zeit_auto_route2);
        autoZeit2.setText(Zeit2+" sec");

        JSONArray features3 = null;

        String Entfernung3 = null;
        String Zeit3 = null;
        try {
            features3 = antwortCar.getJSONArray("features");
            JSONObject featureCar3 = features3.getJSONObject(2);
            JSONObject propertiesCar3 = featureCar3.getJSONObject("properties");
            JSONObject summaryCar3 = propertiesCar3.getJSONObject("summary");
            Entfernung3 = String.valueOf(summaryCar3.getDouble("distance"));
            Zeit3 = String.valueOf(summaryCar3.getDouble("duration"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        TextView autoLaenge3 = (TextView) findViewById(R.id.txt_laenge_auto_route3);
        autoLaenge3.setText(Entfernung3+" m");

        TextView autoZeit3 = (TextView) findViewById(R.id.txt_zeit_auto_route3);
        autoZeit3.setText(Zeit3+" sec");
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
                Toast.makeText(VariantenAuto.this,"Permission granted",Toast.LENGTH_SHORT).show();

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
            antwortCar = new JSONObject(getIntent().getStringExtra("uebergeben1"));
            double ZielPositionLat = getIntent().getDoubleExtra("uebergeben4",0);
            double ZielPositionlong = getIntent().getDoubleExtra("uebergeben5",0);
            ZielPosition = new LatLng(ZielPositionLat,ZielPositionlong);
            startPositionLat = getIntent().getDoubleExtra("uebergeben6",0);
            startPositionlong = getIntent().getDoubleExtra("uebergeben7",0);
            StartPosition = new LatLng(startPositionLat,startPositionlong);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GeoJsonLayer layerCarRed = new GeoJsonLayer(googleMap, antwortCar);
        GeoJsonLayer layerCarBlue = new GeoJsonLayer(googleMap, antwortCar);
        GeoJsonLayer layerCarGreen = new GeoJsonLayer(googleMap, antwortCar);
        layerCarRed.addLayerToMap();
        layerCarBlue.addLayerToMap();
        layerCarGreen.addLayerToMap();


        //GeoJsonFeature lineStringFeatureCar1 = (GeoJsonFeature) layerCar.getFeature(1);

        /**GeoJsonLineStringStyle lineStringStyle = layerCar.getDefaultLineStringStyle();
        lineStringStyle.setColor(getResources().getColor(R.color.auto1));*/


        //lineStringStyleCar1.setColor(getResources().getColor(R.color.fahrrad1));
        //lineStringFeatureCar1.setLineStringStyle(lineStringStyleCar1);

        Integer[] colours = {getResources().getColor(R.color.auto1),getResources().getColor(R.color.fahrrad1)
                ,getResources().getColor(R.color.fuss1)};


        ArrayList <GeoJsonFeature> listCarRed = new ArrayList<GeoJsonFeature>();
        for (GeoJsonFeature feature : layerCarRed.getFeatures()) {
            listCarRed.add(feature);
        }


        ArrayList <GeoJsonFeature> listCarBlue = new ArrayList<GeoJsonFeature>();
        for (GeoJsonFeature feature : layerCarBlue.getFeatures()) {
            listCarBlue.add(feature);
        }

        ArrayList <GeoJsonFeature> listCarGreen = new ArrayList<GeoJsonFeature>();
        for (GeoJsonFeature feature : layerCarGreen.getFeatures()) {
            listCarGreen.add(feature);
        }

        if (listCarRed.size()>2){
            layerCarRed.removeFeature(listCarRed.get(2));}

        if (listCarRed.size()>1){
            layerCarRed.removeFeature(listCarRed.get(1));}

        if (listCarBlue.size()>2){
            layerCarBlue.removeFeature(listCarBlue.get(2));}

        if (listCarBlue.size()>1){
            layerCarBlue.removeFeature(listCarBlue.get(0));}

        if (listCarGreen.size()>2){
            layerCarGreen.removeFeature(listCarGreen.get(1));}

        if (listCarGreen.size()>1){
            layerCarGreen.removeFeature(listCarGreen.get(0));}

        GeoJsonLineStringStyle lineStringStyleCar1 = layerCarRed.getDefaultLineStringStyle();
        GeoJsonLineStringStyle lineStringStyleCar2 = layerCarBlue.getDefaultLineStringStyle();
        GeoJsonLineStringStyle lineStringStyleCar3 = layerCarGreen.getDefaultLineStringStyle();

        lineStringStyleCar1.setColor(colours[0]);
        lineStringStyleCar2.setColor(colours[1]);
        lineStringStyleCar3.setColor(colours[2]);

        googleMap.addMarker(new MarkerOptions().position(StartPosition));
        googleMap.addMarker(new MarkerOptions().position(ZielPosition));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(StartPosition));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(StartPosition,15));

    }
}

