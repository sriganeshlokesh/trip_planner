/*
Assignment #14
File Name: AddPlacesActivity.java
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

public class AddPlacesActivity extends AppCompatActivity implements PlaceAdapter.InteractWithAddPlacesActivity {

    private static final String TAG = "PLACE";
    RecyclerView rv_place;
    RecyclerView.Adapter rv_adapter;
    RecyclerView.LayoutManager rv_layoutManager;
    ArrayList<Place> placeArrayList = null;
    private FirebaseFirestore db;
    String geoLocation = "";
    String place_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_places);
        setTitle("Add Places");
        db = FirebaseFirestore.getInstance();
        rv_place = findViewById(R.id.rv_places);
        rv_layoutManager = new LinearLayoutManager(this);
        rv_place.setLayoutManager(rv_layoutManager);
        if (getIntent() != null && getIntent().getExtras() != null) {
            geoLocation = getIntent().getExtras().getString("geolocation");
            place_id = getIntent().getExtras().getString("place_id");
            try {
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                        "key=AIzaSyA9Cg5GFaXkZiv-h4sxWYepHvDKm2Q3S48" + "&" +
                        "location=" + URLEncoder.encode(geoLocation,"UTF-8")+"&radius=1000";
                new getPlaces().execute(url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addPlace(int position) {
        Place place = placeArrayList.get(position);
        HashMap<String, Object> hmap = new HashMap<>();
        hmap.put("id", place.place_id);
        hmap.put("name",place.place_name);
        hmap.put("icon", place.place_icon);
        hmap.put("latitude", place.place_lat);
        hmap.put("longitude", place.place_lng);
        db.collection("Trips")
                .document(place_id)
                .collection("Places")
                .document(place.place_id)
                .set(hmap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(AddPlacesActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "onFailure:" + e.getMessage() );
                    }
                });
    }

    class getPlaces extends AsyncTask<String, Void, ArrayList<Place>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Place> doInBackground(String... strings) {
            HttpURLConnection connection = null;
            placeArrayList = new ArrayList<>();
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    Log.d(TAG, "doInBackground: " + json);
                    JSONObject root = new JSONObject(json);
                    JSONArray results = root.getJSONArray("results");
                    for(int i = 0; i<results.length(); i++){
                        JSONObject object = results.getJSONObject(i);
                        String id = object.getString("id");
                        String name = object.getString("name");
                        String icon = object.getString("icon");
                        JSONObject geometry = object.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");
                        double latitude = location.getDouble("lat");
                        double longitude = location.getDouble("lng");
                        Place place = new Place(id, name, latitude, longitude, icon);
                        placeArrayList.add(place);
                    }

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return placeArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Place> places) {
            super.onPostExecute(places);
            rv_adapter = new PlaceAdapter(places, AddPlacesActivity.this);
            rv_place.setAdapter(rv_adapter);
        }
    }
}


