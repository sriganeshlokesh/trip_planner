/*
Assignment #14
File Name: MainActivity.java
Group No : 26
Name: Sriganesh Lokesh, Rahul Sundaresan
 */
package com.example.ic014;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NestedPlacesAdapter.InteractWithTripsAdapterActivity,TripAdapter.InteractWithMainActivity {

    public static final String TAG = "MAIN";

    RecyclerView rv_trip_place;
    RecyclerView.Adapter rv_adapter;
    RecyclerView.LayoutManager rv_layoutManager;

    private ImageView img_add_trip;

    private FirebaseFirestore db;

    ArrayList<Trip> tripArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Trips");
        img_add_trip = findViewById(R.id.img_add_trip);
        rv_trip_place = findViewById(R.id.rv_trips);
        rv_layoutManager = new LinearLayoutManager(this);
        rv_trip_place.setLayoutManager(rv_layoutManager);

        db = FirebaseFirestore.getInstance();

        db.collection("Trips")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Log.d(TAG, "onSuccess: " + documentSnapshot);
                            final String description = documentSnapshot.getString("description");
                            final Double latitude = documentSnapshot.getDouble("latitude");
                            final Double longitude = documentSnapshot.getDouble("longitude");
                            final String place_id = documentSnapshot.getString("place_id");
                            final String tripName = documentSnapshot.getString("trip");
                            final ArrayList<Place> places = new ArrayList<>();


                            db.collection("Trips")
                                    .document(place_id)
                                    .collection("Places")
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                                String id = documentSnapshot.getString("id");
                                                String name = documentSnapshot.getString("name");
                                                String icon = documentSnapshot.getString("icon");
                                                Double latitude = documentSnapshot.getDouble("latitude");
                                                Double longitude = documentSnapshot.getDouble("longitude");
                                                Place place = new Place(id, name, latitude, longitude, icon);
                                                places.add(place);
                                            }
                                            Trip trip = new Trip(description, latitude,longitude,place_id,tripName,places);
                                            Log.d(TAG, "onSuccess: " + trip.toString());
                                            tripArrayList.add(trip);
                                            rv_adapter = new TripAdapter(tripArrayList, MainActivity.this);
                                            rv_trip_place.setAdapter(rv_adapter);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "onFailure:"+e.getMessage());
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        img_add_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTripActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void DeletePlace(final String selectedTripId, final String placeId) {

        db.collection("Trips").document(selectedTripId)
                .collection("Places")
                .document(placeId)
                .delete()
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        int tripPosition = -1, placePosition = -1;
                        for(int i = 0; i < tripArrayList.size(); i++){
                            Trip trip = tripArrayList.get(i);
                            if(trip.place_id.equals(selectedTripId)){
                                tripPosition = i;
                                for(int j = 0; j < trip.placeArrayList.size(); j++){
                                    Place place = trip.placeArrayList.get(j);
                                    if(place.place_id.equals(placeId)){
                                        placePosition = j;

                                        tripArrayList.get(tripPosition).placeArrayList.remove(placePosition);
                                        rv_adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    }
                });
    }
    @Override
    public void MapIntent(Trip trip) {
        Intent intent = new Intent(MainActivity.this, NearbyMapsActivity.class);
        intent.putExtra("trip_key", trip);
        startActivity(intent);
    }
}

