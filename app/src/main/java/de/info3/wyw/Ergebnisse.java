package de.info3.wyw;

import static android.view.View.GONE;

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

import java.util.ArrayList;

public class Ergebnisse extends AppCompatActivity implements OnMapReadyCallback {

    boolean isPermissiongranted;
    MapView mapView;
    GoogleMap map;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private ProgressBar ladeKreis;

    int i =0;
    int j = 0;
    int k = 0;

    //Antworten die aus der MapsActivity übergeben werden:
    JSONObject antwortCar = null;
    JSONObject antwortBike = null;
    JSONObject antwortFoot = null;

    //Antworten die an die Detailansicht weiter gegeben werden:
    JSONObject carAntwort;
    JSONObject bikeAntwort;
    JSONObject footAntwort;

    String urlcar = "https://api.openrouteservice.org/v2/directions/driving-car/geojson";
    String urlbike = "https://api.openrouteservice.org/v2/directions/cycling-regular/geojson";
    String urlfoot = "https://api.openrouteservice.org/v2/directions/foot-walking/geojson";

    LatLng ZielPosition = null;
    LatLng StartPosition = null;

    double startPositionLat;
    double startPositionlong;

    ArrayList<GeoJsonFeature> listCarRed = new ArrayList<GeoJsonFeature>();
    ArrayList<GeoJsonFeature> listBikeBlue = new ArrayList<GeoJsonFeature>();
    ArrayList<GeoJsonFeature> listFootGreen = new ArrayList<GeoJsonFeature>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ergebnisse);
        ladeKreis = (ProgressBar) findViewById(R.id.progressBar1);
        ladeKreis.setVisibility(GONE);



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

        JSONArray featuresFoot = null;

        String EntfernungFoot = null;
        String ZeitFoot = null;
        try {
            featuresFoot = antwortFoot.getJSONArray("features");
            JSONObject featureFoot = featuresFoot.getJSONObject(0);
            JSONObject propertiesFoot = featureFoot.getJSONObject("properties");
            JSONObject summaryFoot = propertiesFoot.getJSONObject("summary");
            EntfernungFoot = String.valueOf(summaryFoot.getDouble("distance"));
            ZeitFoot = String.valueOf(summaryFoot.getDouble("duration"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView fussLaenge = (TextView) findViewById(R.id.txt_laenge_fuss);
        fussLaenge.setText(EntfernungFoot);

        TextView fussZeit = (TextView) findViewById(R.id.txt_zeit_fuss);
        fussZeit.setText(ZeitFoot);

        Button aendernAuto = (Button) findViewById(R.id.btn_aendern_auto);

        aendernAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ladeKreis.setVisibility(View.VISIBLE);

                Datenabruf datenabrufcar = new Datenabruf(String.valueOf(startPositionlong),String.valueOf(startPositionLat),
                        String.valueOf(ZielPosition.longitude),String.valueOf(ZielPosition.latitude), urlcar, new DatenabrufInterface(){

                    //... und wenn der Datenabruf fertig ist,
                    // sorgt das DatenabrufInterface dafür, dass es weiter geht.
                    @Override
                    public void onSuccess(JSONObject response){
                        Log.i("DatenabrufCarErgebnisse", String.valueOf(response));
                        //Log.i("BikeAntwort", String.valueOf(bikeResponse));

                        carAntwort=response;



                        //...und die nächste Activity wird aufgerufen.

                            //Jetzt wird auch die Lade-Animation wieder unsichtbar gemacht...
                            ladeKreis.setVisibility(GONE);
                            Intent intent = new Intent(Ergebnisse.this,VariantenAuto.class);
                            intent.putExtra("uebergeben1",String.valueOf(carAntwort));
                            intent.putExtra("uebergeben4",ZielPosition.latitude);
                            intent.putExtra("uebergeben5",ZielPosition.longitude);
                            intent.putExtra("uebergeben6",startPositionLat);
                            intent.putExtra("uebergeben7",startPositionlong);
                            startActivity(intent);


                    }
                }
                );

                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(datenabrufcar.getJsonObjectRequest());


            }
        });

        Button aendernRad = (Button) findViewById(R.id.btn_aendern_fahrrad);

        aendernRad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ladeKreis.setVisibility(View.VISIBLE);

                Datenabruf datenabrufBike = new Datenabruf(String.valueOf(startPositionlong),String.valueOf(startPositionLat),
                        String.valueOf(ZielPosition.longitude),String.valueOf(ZielPosition.latitude), urlbike, new DatenabrufInterface(){

                    //... und wenn der Datenabruf fertig ist,
                    // sorgt das DatenabrufInterface dafür, dass es weiter geht.
                    @Override
                    public void onSuccess(JSONObject response){
                        Log.i("DatenabrufBikeErgebnisse", String.valueOf(response));
                        //Log.i("BikeAntwort", String.valueOf(bikeResponse));

                        bikeAntwort=response;



                        //...und die nächste Activity wird aufgerufen.

                        //Jetzt wird auch die Lade-Animation wieder unsichtbar gemacht...
                        ladeKreis.setVisibility(GONE);
                        Intent intentFahrrad = new Intent(Ergebnisse.this,VariantenFahrrad.class);
                        intentFahrrad.putExtra("uebergeben2",String.valueOf(bikeAntwort));
                        intentFahrrad.putExtra("uebergeben4",ZielPosition.latitude);
                        intentFahrrad.putExtra("uebergeben5",ZielPosition.longitude);
                        intentFahrrad.putExtra("uebergeben6",startPositionLat);
                        intentFahrrad.putExtra("uebergeben7",startPositionlong);
                        startActivity(intentFahrrad);


                    }
                }
                );

                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(datenabrufBike.getJsonObjectRequest());


            }
        });

        Button aendernFuss = (Button) findViewById(R.id.btn_aendern_fuss);

        aendernFuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ladeKreis.setVisibility(View.VISIBLE);

                Datenabruf datenabruffuss = new Datenabruf(String.valueOf(startPositionlong),String.valueOf(startPositionLat),
                        String.valueOf(ZielPosition.longitude),String.valueOf(ZielPosition.latitude), urlfoot, new DatenabrufInterface(){

                    //... und wenn der Datenabruf fertig ist,
                    // sorgt das DatenabrufInterface dafür, dass es weiter geht.
                    @Override
                    public void onSuccess(JSONObject response){
                        Log.i("DatenabrufFussErgebnisse", String.valueOf(response));
                        //Log.i("BikeAntwort", String.valueOf(bikeResponse));

                        footAntwort=response;



                        //...und die nächste Activity wird aufgerufen.

                        //Jetzt wird auch die Lade-Animation wieder unsichtbar gemacht...
                        ladeKreis.setVisibility(GONE);
                        Intent intent = new Intent(Ergebnisse.this,VariantenFuss.class);
                        intent.putExtra("uebergeben3",String.valueOf(footAntwort));
                        intent.putExtra("uebergeben4",ZielPosition.latitude);
                        intent.putExtra("uebergeben5",ZielPosition.longitude);
                        intent.putExtra("uebergeben6",startPositionLat);
                        intent.putExtra("uebergeben7",startPositionlong);
                        startActivity(intent);


                    }
                });

                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(datenabruffuss.getJsonObjectRequest());

            }
        });

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
            double ZielPositionLat = getIntent().getDoubleExtra("uebergeben4",0);
            double ZielPositionlong = getIntent().getDoubleExtra("uebergeben5",0);
            ZielPosition = new LatLng(ZielPositionLat,ZielPositionlong);
            startPositionLat = getIntent().getDoubleExtra("uebergeben6",0);
            startPositionlong = getIntent().getDoubleExtra("uebergeben7",0);
            StartPosition = new LatLng(startPositionLat,startPositionlong);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GeoJsonLayer layerCar = new GeoJsonLayer(googleMap, antwortCar);
        GeoJsonLayer layerBike = new GeoJsonLayer(googleMap, antwortBike);
        GeoJsonLayer layerFoot = new GeoJsonLayer(googleMap, antwortFoot);


        for (GeoJsonFeature feature : layerCar.getFeatures()) {
            listCarRed.add(feature);
        }

        if (listCarRed.size()>2){
            layerCar.removeFeature(listCarRed.get(2));}

        if (listCarRed.size()>1){
            layerCar.removeFeature(listCarRed.get(1));}


        layerCar.addLayerToMap();


        for (GeoJsonFeature feature : layerBike.getFeatures()) {
            listBikeBlue.add(feature);
        }

        if (listBikeBlue.size()>2){
            layerBike.removeFeature(listBikeBlue.get(2));}

        if (listBikeBlue.size()>1){
            layerBike.removeFeature(listBikeBlue.get(1));
        }

        layerBike.addLayerToMap();


        for (GeoJsonFeature feature : layerFoot.getFeatures()) {
            listFootGreen.add(feature);
        }

        if (listFootGreen.size()>2){
            layerFoot.removeFeature(listFootGreen.get(2));}

        if (listFootGreen.size()>1){
            layerFoot.removeFeature(listFootGreen.get(1));}


        layerFoot.addLayerToMap();

        GeoJsonLineStringStyle lineStringStyle = layerCar.getDefaultLineStringStyle();
        lineStringStyle.setColor(getResources().getColor(R.color.auto1));

        GeoJsonLineStringStyle lineStringStyleBike = layerBike.getDefaultLineStringStyle();
        lineStringStyleBike.setColor(getResources().getColor(R.color.fahrrad1));

        GeoJsonLineStringStyle lineStringStyleFoot = layerFoot.getDefaultLineStringStyle();
        lineStringStyleFoot.setColor(getResources().getColor(R.color.fuss1));


        googleMap.addMarker(new MarkerOptions().position(StartPosition));
        googleMap.addMarker(new MarkerOptions().position(ZielPosition));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(StartPosition));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(StartPosition,15));


    }
}