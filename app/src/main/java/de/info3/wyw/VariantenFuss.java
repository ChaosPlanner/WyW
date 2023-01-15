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

public class VariantenFuss extends AppCompatActivity implements OnMapReadyCallback {

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
        setContentView(R.layout.activity_varianten_fuss);

        Bundle mapViewBundle = null;
        if(savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView = (MapView) findViewById(R.id.map_varianten_fuss);
        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);
        checkPermission();

        try {
            antwortFoot = new JSONObject(getIntent().getStringExtra("uebergeben3"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray features = null;

        String Entfernung = null;
        String Zeit = null;
        try {
            features = antwortFoot.getJSONArray("features");
            JSONObject featureFoot = features.getJSONObject(0);
            JSONObject propertiesFoot = featureFoot.getJSONObject("properties");
            JSONObject summaryFoot = propertiesFoot.getJSONObject("summary");
            Entfernung = String.valueOf(summaryFoot.getDouble("distance"));
            Zeit = String.valueOf(summaryFoot.getDouble("duration"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView fussLaenge = (TextView) findViewById(R.id.txt_laenge_fuss_route1);
        fussLaenge.setText(Entfernung+" m");

        TextView fussZeit = (TextView) findViewById(R.id.txt_zeit_fuss_route1);
        fussZeit.setText(Zeit+" sec");

        JSONArray features2 = null;

        String Entfernung2 = null;
        String Zeit2 = null;
        try {
            features2 = antwortFoot.getJSONArray("features");
            JSONObject featureFoot2 = features2.getJSONObject(1);
            JSONObject propertiesFoot2 = featureFoot2.getJSONObject("properties");
            JSONObject summaryFoot2 = propertiesFoot2.getJSONObject("summary");
            Entfernung2 = String.valueOf(summaryFoot2.getDouble("distance"));
            Zeit2 = String.valueOf(summaryFoot2.getDouble("duration"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView fussLaenge2 = (TextView) findViewById(R.id.txt_laenge_fuss_route2);
        fussLaenge2.setText(Entfernung2+" m");

        TextView fussZeit2 = (TextView) findViewById(R.id.txt_zeit_fuss_route2);
        fussZeit2.setText(Zeit2+" sec");

        JSONArray features3 = null;

        String Entfernung3 = null;
        String Zeit3 = null;
        try {
            features3 = antwortFoot.getJSONArray("features");
            JSONObject featureFoot3 = features3.getJSONObject(2);
            JSONObject propertiesFoot3 = featureFoot3.getJSONObject("properties");
            JSONObject summaryFoot3 = propertiesFoot3.getJSONObject("summary");
            Entfernung3 = String.valueOf(summaryFoot3.getDouble("distance"));
            Zeit3 = String.valueOf(summaryFoot3.getDouble("duration"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView fussLaenge3 = (TextView) findViewById(R.id.txt_laenge_fuss_route3);
        fussLaenge3.setText(Entfernung3+" m");

        TextView fussZeit3 = (TextView) findViewById(R.id.txt_zeit_fuss_route3);
        fussZeit3.setText(Zeit3+" sec");
/*
        TextView fuss1Laenge = (TextView) findViewById(R.id.txt_laenge_fuss_route1);
        fuss1Laenge.setText(Entfernung);

        TextView fuss1Zeit = (TextView) findViewById(R.id.txt_zeit_fuss_route1);
        fuss1Zeit.setText(Zeit);

        TextView fuss1Co2 = (TextView) findViewById(R.id.co2_fuss_route1);
        fuss1Co2.setText(CO2);

        TextView fuss2Laenge = (TextView) findViewById(R.id.txt_laenge_fuss_route2);
        fuss2Laenge.setText(Entfernung);

        TextView fuss2Zeit = (TextView) findViewById(R.id.txt_zeit_fuss_route2);
        fuss2Zeit.setText(Zeit);

        TextView fuss2Co2 = (TextView) findViewById(R.id.co2_fuss_route2);
        fuss2Co2.setText(CO2);

        TextView fuss3Laenge = (TextView) findViewById(R.id.txt_laenge_fuss_route3);
        fuss3Laenge.setText(Entfernung);

        TextView fuss3Zeit = (TextView) findViewById(R.id.txt_zeit_fuss_route3);
        fuss3Zeit.setText(Zeit);

        TextView fuss3Co2 = (TextView) findViewById(R.id.co2_fuss_route3);
        fuss3Co2.setText(CO2);


 */




    }

    private void checkPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPermissiongranted = true;
                Toast.makeText(VariantenFuss.this,"Permission granted",Toast.LENGTH_SHORT).show();

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
            antwortFoot = new JSONObject(getIntent().getStringExtra("uebergeben3"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GeoJsonLayer layerFoot = new GeoJsonLayer(googleMap, antwortFoot);

        layerFoot.addLayerToMap();

        //GeoJsonFeature lineStringFeatureFoot1 = (GeoJsonFeature) layerFoot.getFeature(1);

        /**GeoJsonLineStringStyle lineStringStyle = layerFoot.getDefaultLineStringStyle();
         lineStringStyle.setColor(getResources().getColor(R.color.fuss1));*/

        GeoJsonLineStringStyle lineStringStyleFoot1 = layerFoot.getDefaultLineStringStyle();
        //lineStringStyleCar1.setColor(getResources().getColor(R.color.fahrrad1));
        //lineStringFeatureCar1.setLineStringStyle(lineStringStyleCar1);

        Integer[] colours = {getResources().getColor(R.color.auto1),getResources().getColor(R.color.fahrrad1)
                ,getResources().getColor(R.color.fuss1)};

        for (GeoJsonFeature feature : layerFoot.getFeatures()) {
            // Do something to the feature

            lineStringStyleFoot1.setColor(colours[i]);
            feature.setLineStringStyle(lineStringStyleFoot1);
            i++;
        }


        LatLng start = new LatLng(49.41461,8.681495);
        googleMap.addMarker(new MarkerOptions().position(start));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(start));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start,15));

    }
}