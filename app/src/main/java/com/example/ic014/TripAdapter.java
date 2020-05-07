/*
Assignment #14
File Name: TripAdapter.java
Group No : 26
Name: Sriganesh Lokesh, Rahul Sundaresan
 */
package com.example.ic014;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {
    public static InteractWithMainActivity interact;
    ArrayList<Trip> trips;
    Context ctx;
    String selectedTripId;
    public TripAdapter(ArrayList<Trip> trips, Context ctx) {
        this.trips = trips;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public TripAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout rv_layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(rv_layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TripAdapter.ViewHolder holder, final int position) {

        selectedTripId = trips.get(position).place_id;
        interact = (InteractWithMainActivity) ctx;
        holder.textViewTrip.setText(trips.get(position).trip_name);
        holder.textViewDescription.setText(trips.get(position).place_address);

        holder.rv_adapter = new NestedPlacesAdapter(trips.get(position).placeArrayList, selectedTripId, ctx);
        holder.rv_place.setAdapter(holder.rv_adapter);

        holder.img_add_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, AddPlacesActivity.class);
                intent.putExtra("place_id", trips.get(position).place_id);
                String geoLocation = trips.get(position).trip_latitude + "," + trips.get(position).trip_longitude;
                intent.putExtra("geolocation", geoLocation);
                ctx.startActivity(intent);
            }
        });
        holder.img_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interact.MapIntent(trips.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return trips.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTrip, textViewDescription;
        ImageView img_location, img_add_place;
        RecyclerView rv_place;
        RecyclerView.Adapter rv_adapter;
        RecyclerView.LayoutManager layoutManager;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTrip = itemView.findViewById(R.id.tv_trip_name);
            textViewDescription = itemView.findViewById(R.id.tv_place_address);
            img_location = itemView.findViewById(R.id.img_location);
            img_add_place = itemView.findViewById(R.id.img_add_place);
            rv_place = itemView.findViewById(R.id.rv_place_trip);
            layoutManager = new LinearLayoutManager(ctx);
            rv_place.setLayoutManager(layoutManager);
        }
    }
    public interface InteractWithMainActivity{
        void MapIntent(Trip trip);
    }
}

