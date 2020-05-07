/*
Assignment #14
File Name: NestedPlacesAdapter.java
Group No : 26
Name: Sriganesh Lokesh, Rahul Sundaresan
 */
package com.example.ic014;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NestedPlacesAdapter extends RecyclerView.Adapter<NestedPlacesAdapter.ViewHolder> {
    public static InteractWithTripsAdapterActivity interact;

    ArrayList<Place> places = null;
    String selectedTripId;
    Context ctx;

    public NestedPlacesAdapter(ArrayList<Place> places, String selectedTripId, Context ctx) {
        this.places = places;
        this.selectedTripId = selectedTripId;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public NestedPlacesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout rv_layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.nested_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(rv_layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NestedPlacesAdapter.ViewHolder holder, final int position) {
        interact = (InteractWithTripsAdapterActivity) ctx;
        Picasso.get().load(places.get(position).place_icon).into(holder.img_place_icon);
        holder.tv_place_name.setText(places.get(position).place_name);
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interact.DeletePlace(selectedTripId, places.get(position).place_id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_place_icon;
        TextView tv_place_name;
        ImageView img_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_place_icon = itemView.findViewById(R.id.img_place_icon_main);
            tv_place_name = itemView.findViewById(R.id.tv_place_name_main);
            img_delete = itemView.findViewById(R.id.img_delete_place);
        }
    }
    public interface InteractWithTripsAdapterActivity{
        void DeletePlace(String selectedTripId,String placeId);
    }
}

