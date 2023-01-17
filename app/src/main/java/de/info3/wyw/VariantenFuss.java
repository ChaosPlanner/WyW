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

    LatLng ZielPosition = null;
    LatLng StartPosition = null;

    double startPositionLat;
    double startPositionlong;

    JSONObject featureFoot;
    JSONObject featureFoot2;
    JSONObject featureFoot3;

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
            features = antwortFoot.getJSONArray("features");
            featureFoot = features.getJSONObject(0);
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
            featureFoot2 = features2.getJSONObject(1);
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
            featureFoot3 = features3.getJSONObject(2);
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
            double ZielPositionLat = getIntent().getDoubleExtra("uebergeben4",0);
            double ZielPositionlong = getIntent().getDoubleExtra("uebergeben5",0);
            ZielPosition = new LatLng(ZielPositionLat,ZielPositionlong);
            startPositionLat = getIntent().getDoubleExtra("uebergeben6",0);
            startPositionlong = getIntent().getDoubleExtra("uebergeben7",0);
            StartPosition = new LatLng(startPositionLat,startPositionlong);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GeoJsonLayer layerCarRed = new GeoJsonLayer(googleMap, featureFoot);
        GeoJsonLayer layerCarBlue = new GeoJsonLayer(googleMap, featureFoot2);
        GeoJsonLayer layerCarGreen = new GeoJsonLayer(googleMap, featureFoot3);


        Integer[] colours = {getResources().getColor(R.color.auto1),getResources().getColor(R.color.fahrrad1)
                ,getResources().getColor(R.color.fuss1)};


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

        Button route1Bike = (Button) findViewById(R.id.btn_aendern_fuss_route1);
        route1Bike.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VariantenFuss.this,Ergebnisse.class);
                intent.putExtra("uebergeben3",String.valueOf(featureFoot));
                startActivity(intent);
            }
        });

        Button route2Bike = (Button) findViewById(R.id.btn_aendern_fuss_route2);
        route2Bike.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VariantenFuss.this,Ergebnisse.class);
                intent.putExtra("uebergeben3",String.valueOf(featureFoot2));
                startActivity(intent);
            }
        });

        Button route3Bike = (Button) findViewById(R.id.btn_aendern_fuss_route3);
        route3Bike.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VariantenFuss.this,Ergebnisse.class);
                intent.putExtra("uebergeben3",String.valueOf(featureFoot3));
                startActivity(intent);
            }
        });

    }
}