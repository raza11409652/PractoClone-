package com.hackdroid.hospital.App;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hackdroid.hospital.Model.Bookings;
import com.hackdroid.hospital.R;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
    Context context;
    List<Bookings> list;
    Activity activity;

    public BookingAdapter(Context context, List<Bookings> list, Activity activity) {
        this.context = context;
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.singlebooking, parent, false);
        return new BookingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.doctor.setText(list.get(position).getDoctor());
        holder.date.setText(list.get(position).getDate() + " at " + list.get(position).getTime());
        holder.address.setText(list.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, doctor, address;

        public ViewHolder(@NonNull View itemView) {


            super(itemView);
            date = itemView.findViewById(R.id.date);
            doctor = itemView.findViewById(R.id.doctor);
            address = itemView.findViewById(R.id.address);


        }
    }
}
