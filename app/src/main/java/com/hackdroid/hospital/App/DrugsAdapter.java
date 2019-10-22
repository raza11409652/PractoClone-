package com.hackdroid.hospital.App;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hackdroid.hospital.Model.Drugs;
import com.hackdroid.hospital.R;

import java.util.ArrayList;
import java.util.List;

public class DrugsAdapter extends RecyclerView.Adapter<DrugsAdapter.ViewHolder> {
    Activity activity ;
    Context context ;
    List<Drugs> list ;

    public DrugsAdapter(Activity activity, Context context, List<Drugs> list) {
        this.activity = activity;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.single_drugs  ,parent ,false) ;
        return new DrugsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.remrks.setText(list.get(position).getRemarks());
        holder.name.setText(list.get(position).getName());
        holder.ven.setText(list.get(position).getVen());
        holder.grp.setText(list.get(position).getGroup());

    }

    public void filterList(List<Drugs> filtermodelist) {
        list= new ArrayList<>();
        list.addAll(filtermodelist);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name , ven , grp , remrks ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameMed);
            ven = itemView.findViewById(R.id.venMed);
            grp = itemView.findViewById(R.id.grp) ;
            remrks = itemView.findViewById(R.id.remarks);
        }
    }
}
