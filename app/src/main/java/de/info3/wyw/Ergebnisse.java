package de.info3.wyw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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

public class Ergebnisse extends AppCompatActivity implements OnMapReadyCallback {

    boolean isPermissiongranted;
    MapView mapView;
    GoogleMap map;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    int i =0;
    int j = 0;
    int k = 0;

    JSONObject antwortCar = null;
    JSONObject antwortBike = null;
    JSONObject antwortFoot = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ergebnisse);



        Bundle mapViewBundle = null;
        if(savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView = (MapView) findViewById(R.id.map_ergebnisse);
        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);
        checkPermission();

        try {
            antwortCar = new JSONObject(getIntent().getStringExtra("uebergeben1"));
            antwortBike = new JSONObject(getIntent().getStringExtra("uebergeben2"));
            antwortFoot = new JSONObject(getIntent().getStringExtra("uebergeben3"));
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

        TextView autoLaenge = (TextView) findViewById(R.id.txt_laenge_auto);
        autoLaenge.setText(Entfernung+" m");

        TextView autoZeit = (TextView) findViewById(R.id.txt_zeit_auto);
        autoZeit.setText(Zeit+" sec");

        JSONArray featuresBike = null;

        String EntfernungBike = null;
        String ZeitBike = null;
        try {
            featuresBike = antwortBike.getJSONArray("features");
            JSONObject featureBike = featuresBike.getJSONObject(0);
            JSONObject propertiesBike = featureBike.getJSONObject("properties");
            JSONObject summaryBike = propertiesBike.getJSONObject("summary");
            EntfernungBike = String.valueOf(summaryBike.getDouble("distance"));
            ZeitBike = String.valueOf(summaryBike.getDouble("duration"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView fahrradLaenge = (TextView) findViewById(R.id.txt_laenge_fahrrad);
        fahrradLaenge.setText(EntfernungBike);

        TextView fahrradZeit = (TextView) findViewById(R.id.txt_zeit_fahrrad);
        fahrradZeit.setText(ZeitBike);

/**

        TextView autoCo2 = (TextView) findViewById(R.id.co2_auto);
        autoCo2.setText(CO2);



        TextView fahrradCo2 = (TextView) findViewById(R.id.co2_fahrrad);
        fahrradCo2.setText(CO2);

        TextView fussLaenge = (TextView) findViewById(R.id.txt_laenge_fuss);
        fussLaenge.setText(Entfernung);

        TextView fussZeit = (TextView) findViewById(R.id.txt_zeit_fuss);
        fussLaenge.setText(Zeit);

        TextView fussCo2 = (TextView) findViewById(R.id.co2_fuss);
        fussLaenge.setText(CO2);


        Button aendernAuto = (Button) findViewById(R.id.btn_aendern_auto);
        aendernAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            Abfrage ORS

            solange ORS Abfrage:

            ProgressBar progressBarAuto = (ProgressBar) findViewById(R.id.progress_bar_auto);
            progressBarAuto.setVisibility(View.VISIBLE);

            wenn Ergebniss von server
             progressBarAuto.setVisibility(View.GONE);



                Intent intent = new Intent(Ergebnisse.this,tbd.class);
                startActivity(intent);
            }
        });

        Button aendernFahrrad = (Button) findViewById(R.id.btn_aendern_fahrrad);
        aendernFahrrad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Ergebnisse.this,tbd.class);
                startActivity(intent)
            }
        });

        Button aendernFuss = (Button) findViewById(R.id.btn_aendern_fuss);
        aendernFuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Ergebnisse.this,tbd.class);
                startActivity(intent)
            }
        });

*/

    }

    private void checkPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPermissiongranted = true;
                Toast.makeText(Ergebnisse.this,"Permission granted",Toast.LENGTH_SHORT).show();

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
            antwortBike = new JSONObject(getIntent().getStringExtra("uebergeben2"));
            antwortFoot = new JSONObject(getIntent().getStringExtra("uebergeben3"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GeoJsonLayer layerCar = new GeoJsonLayer(googleMap, antwortCar);
        GeoJsonLayer layerBike = new GeoJsonLayer(googleMap, antwortBike);
        GeoJsonLayer layerFoot = new GeoJsonLayer(googleMap, antwortFoot);

        layerCar.addLayerToMap();
        layerBike.addLayerToMap();
        layerFoot.addLayerToMap();

        /**for (GeoJsonFeature feature : layerCar.getFeatures()) {
            if (i>0){
            layerCar.removeFeature(feature);}
            i++;

        }

        for (GeoJsonFeature feature : layerBike.getFeatures()) {
            if (j>0){
                layerBike.removeFeature(feature);}
            j++;

        }

        for (GeoJsonFeature feature : layerFoot.getFeatures()) {
            if (k > 0) {
                layerFoot.removeFeature(feature);
            }
            k++;
        }*/

        GeoJsonLineStringStyle lineStringStyle = layerCar.getDefaultLineStringStyle();
        lineStringStyle.setColor(getResources().getColor(R.color.auto1));

        GeoJsonLineStringStyle lineStringStyleBike = layerBike.getDefaultLineStringStyle();
        lineStringStyleBike.setColor(getResources().getColor(R.color.fahrrad1));

        GeoJsonLineStringStyle lineStringStyleFoot = layerFoot.getDefaultLineStringStyle();
        lineStringStyleFoot.setColor(getResources().getColor(R.color.fuss1));

        LatLng start = new LatLng(49.41461,8.681495);
        googleMap.addMarker(new MarkerOptions().position(start));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(start));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start,15));


    }
}