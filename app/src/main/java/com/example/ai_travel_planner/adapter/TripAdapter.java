package com.example.ai_travel_planner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ai_travel_planner.R;
import com.example.ai_travel_planner.database.Trip;

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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_item,parent,false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Trip trip = tripList.get(position);

        holder.tvDestination.setText(trip.getDestination());

        holder.tvDate.setText(
                trip.getStartDate() + " - " + trip.getEndDate());

        holder.tvBudget.setText("Budget : ₹" + trip.getBudget());

        holder.tvTravelMode.setText("Travel Mode : " + trip.getTravelMode());

        holder.tvHotel.setText("Hotel : " + trip.getHotel());
    }


    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvDestination,tvDate,tvBudget,tvTravelMode,tvHotel;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvBudget = itemView.findViewById(R.id.tvBudget);
            tvTravelMode = itemView.findViewById(R.id.tvTravelMode);
            tvHotel = itemView.findViewById(R.id.tvHotel);

        }
    }
}