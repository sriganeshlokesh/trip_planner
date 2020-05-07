/*
Assignment #14
File Name: AddTripActivity.java
Group No : 26
Name: Sriganesh Lokesh, Rahul Sundaresan
 */
package com.example.ic014;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class AddTripActivity extends AppCompatActivity implements CityAdapter.InteractWithAddTripActivity {

    private EditText et_trip_name, et_city_name;
    private Button bt_search, bt_add_trip;

    public static final String TAG= "TRIP";

    RecyclerView rv_city_recycler;
    RecyclerView.Adapter rv_adapter;
    RecyclerView.LayoutManager rv_layoutManager;

    ArrayList<City> cityArrayList = null;
    City city_select = null;

    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        setTitle("Add Trip");
        et_trip_name = findViewById(R.id.et_trip_name);
        et_city_name = findViewById(R.id.et_city_search);
        bt_search = findViewById(R.id.bt_search);
        bt_add_trip = findViewById(R.id.bt_add_trip);

        rv_city_recycler = findViewById(R.id.rv_city_search);
        rv_layoutManager = new LinearLayoutManager(this);
        rv_city_recycler.setLayoutManager(rv_layoutManager);

        db = FirebaseFirestore.getInstance();


        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_trip_name.getText().toString().equals("")){
                    et_trip_name.setError("Enter Valid Trip Name");
                }
                else if(et_city_name.getText().toString().equals("")){
                    et_city_name.setError("Enter Valid City Name");
                }
                else{
                    String url = null;
                    try {
                        url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?" +
                                "key=AIzaSyA9Cg5GFaXkZiv-h4sxWYepHvDKm2Q3S48" + "&" +
                                "types=(cities)" + "&" +
                                "input=" + URLEncoder.encode(et_city_name.getText().toString(),"UTF-8");
                        new getCities().execute(url);

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                }
            }
        });

        bt_add_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String url = "https://maps.googleapis.com/maps/api/place/details/json?" +
                            "key=AIzaSyA9Cg5GFaXkZiv-h4sxWYepHvDKm2Q3S48"+"&"+
                            "placeid="+URLEncoder.encode(city_select.place_id,"UTF-8");
                    new getGeoCoordinates().execute(url);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    class getGeoCoordinates extends AsyncTask<String, Void, double[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected double[] doInBackground(String... strings) {
            double[] geo = {0,0};
            HttpURLConnection connection = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    Log.d(TAG, "doInBackground: " + json);
                    JSONObject root = new JSONObject(json);
                    JSONObject result = root.getJSONObject("result");
                    JSONObject geometry = result.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    Double latitude = Double.valueOf(location.getString("lat"));
                    Double longitude = Double.valueOf(location.getString("lng"));
                    geo[0] = latitude;
                    geo[1] = longitude;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return geo;
        }

        @Override
        protected void onPostExecute(double[] d) {
            super.onPostExecute(d);
            Log.d(TAG, "onPostExecute: " + d);
            if(city_select.place_id.equals("") || city_select.place_address.equals("")){
                Toast.makeText(AddTripActivity.this, "Select a City", Toast.LENGTH_SHORT).show();
            }
            else{
                HashMap<String, Object> hmap = new HashMap<>();
                hmap.put("trip",et_trip_name.getText().toString());
                hmap.put("description", city_select.place_address);
                hmap.put("place_id",city_select.place_id);
                hmap.put("latitude",d[0]);
                hmap.put("longitude",d[1]);

                db.collection("Trips")
                        .document(city_select.place_id)
                        .set(hmap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent  = new Intent(AddTripActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "onFailure:" + e.getMessage());
                            }
                        });
            }

        }
    }

    @Override
    public void getCityDetails(int position) {
        city_select = cityArrayList.get(position);
        et_city_name.setText(city_select.place_address);
    }

    class getCities extends AsyncTask<String, Void, ArrayList<City>>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<City> doInBackground(String... strings) {
            cityArrayList = new ArrayList<>();

            HttpURLConnection connection = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    Log.d(TAG, "doInBackground: " + json);
                    JSONObject root = new JSONObject(json);
                    JSONArray predictions = root.getJSONArray("predictions");
                    for(int i = 0; i< predictions.length(); i++){
                        JSONObject object =predictions.getJSONObject(i);
                        String description = object.getString("description");
                        String place_id = object.getString("place_id");
                        City city = new City(description, place_id);
                        cityArrayList.add(city);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return cityArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<City> cities) {
            super.onPostExecute(cities);
            for (City c: cities) {
                Log.d(TAG, "onPostExecute: " + c.toString());
            }
            rv_adapter = new CityAdapter(cities, AddTripActivity.this);
            rv_city_recycler.setAdapter(rv_adapter);
        }
    }
}

