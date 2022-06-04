package com.gic.weathermaps;

import androidx.fragment.app.FragmentActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    //GLOBAL VARIABLES
    EditText CityName;
    TextView main;
    TextView description;
    TextView temperature;
    private GoogleMap mMap;
    public void GetDetails(View view){
        DownloadTask task= new DownloadTask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + CityName.getText().toString() + "&appid=ab0b77c0a4cacf977b8c312773f4dec6");


    }


    public class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);
                String se = jsonObject.getString("weather");
                Log.i("weather", se);
                JSONArray arr = new JSONArray(se);
                for (int i=0; i < arr.length(); i++) {
                    JSONObject jsonPart = arr.getJSONObject(i);
                    description.setText(jsonPart.getString("description"));
                    main.setText(jsonPart.getString("main"));
                }
              
                //String cor = jsonObject.getString("coord");
               // Log.i("weather", cor);
                JSONArray arr1 = new JSONArray(s);

               // org.json.JSONObject jsonArray = obj.getJSONObject(cor);
               Log.i("wow", String.valueOf(arr1));
                for (int k=0; k < arr1.length(); k++) {
                    JSONObject jsonPart1 = arr1.getJSONObject(k);
                   temperature.setText(arr1.getJSONObject(0).getString("lon".toString()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        CityName = (EditText) findViewById(R.id.CityName);
        temperature = (TextView) findViewById(R.id.Temperature);
        description = (TextView) findViewById(R.id.Description);
        main = (TextView) findViewById(R.id.Main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}