package com.example.ai_travel_planner.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ai_travel_planner.R;
import com.example.ai_travel_planner.model.Place;

import java.util.List;

public class PlaceAdapter
        extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private final List<Place> placeList;

    public PlaceAdapter(List<Place> placeList) {
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        Place place = placeList.get(position);
        if (place.displayName != null
                && place.displayName.text != null) {

            holder.tvPlaceName.setText(
                    place.displayName.text
            );

        } else {

            holder.tvPlaceName.setText(
                    "Unknown Place"
            );
        }
        holder.tvPlaceAddress.setText(
                place.formattedAddress != null
                        ? place.formattedAddress
                        : "Address unavailable"
        );

        if (place.rating != null) {
            holder.tvPlaceRating.setText(
                    "⭐ " + place.rating
            );
        } else {
            holder.tvPlaceRating.setText(
                    "⭐ Rating unavailable"
            );
        }

        holder.btnViewPlace.setOnClickListener(v -> {

            if (place.googleMapsUri != null
                    && !place.googleMapsUri.isEmpty()) {

                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(place.googleMapsUri)
                );

                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvPlaceName;
        TextView tvPlaceAddress;
        TextView tvPlaceRating;
        Button btnViewPlace;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPlaceName =
                    itemView.findViewById(R.id.tvPlaceName);

            tvPlaceAddress =
                    itemView.findViewById(R.id.tvPlaceAddress);

            tvPlaceRating =
                    itemView.findViewById(R.id.tvPlaceRating);

            btnViewPlace =
                    itemView.findViewById(R.id.btnViewPlace);
        }
    }
}