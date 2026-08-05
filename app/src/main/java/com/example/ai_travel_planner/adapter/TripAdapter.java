package com.example.ai_travel_planner.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ai_travel_planner.EditTripActivity;
import com.example.ai_travel_planner.R;
import com.example.ai_travel_planner.database.Trip;
import com.example.ai_travel_planner.database.TripDatabase;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    List<Trip> tripList;

    public interface OnTripClickListener {
        void onTripClick(Trip trip);
        void onTripLongClick(Trip trip);
    }

    private OnTripClickListener listener;

    public TripAdapter(List<Trip> tripList,
                       OnTripClickListener listener) {

        this.tripList = tripList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Trip trip = tripList.get(position);

        holder.tvDestination.setText(trip.getDestination());

        holder.tvDate.setText(
                trip.getStartDate() + " - " + trip.getEndDate());

        holder.tvBudget.setText(
                "Budget : ₹" + trip.getBudget());

        holder.tvTravelMode.setText(
                "Travel Mode : " + trip.getTravelMode());

        holder.tvHotel.setText(
                "Hotel : " + trip.getHotel());

        // View Trip
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTripClick(trip);
            }
        });

        // Long Click
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onTripLongClick(trip);
            }
            return true;
        });

        // Edit Trip
        holder.btnEditTrip.setOnClickListener(v -> {

            Intent intent = new Intent(
                    holder.itemView.getContext(),
                    EditTripActivity.class
            );

            intent.putExtra("id", trip.getId());
            intent.putExtra("destination", trip.getDestination());
            intent.putExtra("startDate", trip.getStartDate());
            intent.putExtra("endDate", trip.getEndDate());
            intent.putExtra("travelers", trip.getTravelers());
            intent.putExtra("budget", trip.getBudget());
            intent.putExtra("travelMode", trip.getTravelMode());
            intent.putExtra("hotel", trip.getHotel());
            intent.putExtra("aiTrip", trip.getAiTrip());

            holder.itemView.getContext().startActivity(intent);

        });

        // Delete Trip
        holder.btnDeleteTrip.setOnClickListener(v -> {

            int currentPosition = holder.getAdapterPosition();

            if (currentPosition == RecyclerView.NO_POSITION) {
                return;
            }

            TripDatabase.getInstance(
                            holder.itemView.getContext())
                    .tripDao()
                    .deleteTrip(trip);

            tripList.remove(currentPosition);

            notifyItemRemoved(currentPosition);

            notifyItemRangeChanged(
                    currentPosition,
                    tripList.size()
            );

            Toast.makeText(
                    holder.itemView.getContext(),
                    "Trip Deleted Successfully",
                    Toast.LENGTH_SHORT
            ).show();

        });

    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvDestination;
        TextView tvDate;
        TextView tvBudget;
        TextView tvTravelMode;
        TextView tvHotel;

        Button btnEditTrip;
        Button btnDeleteTrip;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvBudget = itemView.findViewById(R.id.tvBudget);
            tvTravelMode = itemView.findViewById(R.id.tvTravelMode);
            tvHotel = itemView.findViewById(R.id.tvHotel);

            btnEditTrip = itemView.findViewById(R.id.btnEditTrip);
            btnDeleteTrip = itemView.findViewById(R.id.btnDeleteTrip);

        }
    }
}