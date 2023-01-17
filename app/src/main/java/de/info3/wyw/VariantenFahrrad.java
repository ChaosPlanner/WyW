package de.info3.wyw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;

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

    JSONObject featureBike;
    JSONObject featureBike2;
    JSONObject featureBike3;
    LatLng ZielPosition = null;
    LatLng StartPosition = null;

    double startPositionLat;
    double startPositionlong;

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
            features = antwortBike.getJSONArray("features");
            featureBike = features.getJSONObject(0);
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
            featureBike2 = features2.getJSONObject(1);
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
            featureBike3 = features3.getJSONObject(2);
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
            antwortBike = new JSONObject(getIntent().getStringExtra("uebergeben2"));
            double ZielPositionLat = getIntent().getDoubleExtra("uebergeben4",0);
            double ZielPositionlong = getIntent().getDoubleExtra("uebergeben5",0);
            ZielPosition = new LatLng(ZielPositionLat,ZielPositionlong);
            startPositionLat = getIntent().getDoubleExtra("uebergeben6",0);
            startPositionlong = getIntent().getDoubleExtra("uebergeben7",0);
            StartPosition = new LatLng(startPositionLat,startPositionlong);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GeoJsonLayer layerCarRed = new GeoJsonLayer(googleMap, featureBike);
        GeoJsonLayer layerCarBlue = new GeoJsonLayer(googleMap, featureBike2);
        GeoJsonLayer layerCarGreen = new GeoJsonLayer(googleMap, featureBike3);


        Integer[] colours = {getResources().getColor(R.color.auto1),getResources().getColor(R.color.fahrrad1)
                ,getResources().getColor(R.color.fuss1)};

/*
        ArrayList<GeoJsonFeature> listCarRed = new ArrayList<GeoJsonFeature>();
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

*/

        Log.i("LayerFahrrad1", String.valueOf(layerCarRed));

        GeoJsonLineStringStyle lineStringStyleCar1 = layerCarRed.getDefaultLineStringStyle();
        GeoJsonLineStringStyle lineStringStyleCar2 = layerCarBlue.getDefaultLineStringStyle();
        GeoJsonLineStringStyle lineStringStyleCar3 = layerCarGreen.getDefaultLineStringStyle();

        lineStringStyleCar1.setColor(colours[0]);
        lineStringStyleCar2.setColor(colours[1]);
        lineStringStyleCar3.setColor(colours[2]);

        layerCarRed.addLayerToMap();
        layerCarBlue.addLayerToMap();
        layerCarGreen.addLayerToMap();

        googleMap.addMarker(new MarkerOptions().position(StartPosition));
        googleMap.addMarker(new MarkerOptions().position(ZielPosition));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(StartPosition));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(StartPosition,15));


        Button route1Car = (Button) findViewById(R.id.btn_aendern_fahrrad_route1);
        route1Car.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VariantenFahrrad.this,Ergebnisse.class);
                intent.putExtra("uebergeben1",String.valueOf(featureBike));
                startActivity(intent);
            }
        });

        Button route2Car = (Button) findViewById(R.id.btn_aendern_fahrrad_route2);
        route1Car.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VariantenFahrrad.this,Ergebnisse.class);
                intent.putExtra("uebergeben1",String.valueOf(featureBike2));
                startActivity(intent);
            }
        });

        Button route3Car = (Button) findViewById(R.id.btn_aendern_fahrrad_route3);
        route1Car.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VariantenFahrrad.this,Ergebnisse.class);
                intent.putExtra("uebergeben1",String.valueOf(featureBike3));
                startActivity(intent);
            }
        });

    }
}