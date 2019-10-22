package com.hackdroid.hospital.App;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hackdroid.hospital.Hospitalfinder;
import com.hackdroid.hospital.Model.HospitalList;
import com.hackdroid.hospital.R;

import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.ViewHolder> {
    Context context;
    Activity activity ;
    List<HospitalList> list ;

    public HospitalAdapter(Context context, Activity activity, List<HospitalList> list) {
        this.context = context;
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_hospital , parent , false) ;
            return new HospitalAdapter.ViewHolder(view) ;
        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        holder.address.setText(list.get(position).getAddress());
        holder.city.setText(list.get(position).getCity());
        holder.phone.setText(list.get(position).getPhone());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView name  ,        address , city , phone  ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            city     =itemView.findViewById(R.id.city);
            phone = itemView.findViewById(R.id.phone);
        }
    }
}
