package com.hackdroid.hospital.App;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hackdroid.hospital.Model.Location;
import com.hackdroid.hospital.R;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    Context context;
    List<Location> list;
    Activity activity ;

    public LocationAdapter(Context context, List<Location> list , Activity activity) {
        this.context = context;
        this.list = list;
        this.activity = activity ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater.from(contex).inflate(R.layout.single_booking_active, parent , false);
        View view = LayoutInflater.from(context).inflate(R.layout.single_city, parent,
                false);
        return new LocationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
         holder.city.setText(list.get(position).getLocation());
         holder.city.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Constant.USER_LOCATION = list.get(position).getLocation() ;
                 Intent intent = new Intent() ;
                 intent.putExtra("location" , Constant.USER_LOCATION);
                 activity.setResult(Activity.RESULT_OK , intent);
                 activity.finish();
             }
         });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void filterList(List<Location> filtermodelist) {
        list= new ArrayList<>();
        list.addAll(filtermodelist);
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView city  ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.city);
        }
    }
}
