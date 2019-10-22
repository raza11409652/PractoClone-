package com.hackdroid.hospital.App;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hackdroid.hospital.CreateBookings;
import com.hackdroid.hospital.Model.Doctors;
import com.hackdroid.hospital.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterDr extends RecyclerView.Adapter<AdapterDr.ViewHolder> {
    List<Doctors>list ;
    Context context ;
    Activity activity ;

    public AdapterDr(List<Doctors> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.single_layout_doctor , parent, false);

        return new AdapterDr.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.name.setText(list.get(position).getName());
        holder.type.setText(list.get(position).getType());
        holder.address.setText(list.get(position).getAddress());
        holder.state.setText(list.get(position).getCity()+" "+list.get(position).getState());
        holder.phone.setText(list.get(position).getPhone());
        String urlImage = Server.ROOT_IMAGE+list.get(position).getImage();
        Picasso.get()
                .load(urlImage)
                .placeholder(R.drawable.icon)
                .error(R.drawable.icon)
                .into(holder.imageView);
        holder.createBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.NAME = list.get(position).getName();
                Constant.ADDRESS = list.get(position).getAddress() ;
                Constant.IMAGE = list.get(position).getImage() ;
                Constant.ID_FOR_BOOKING = list.get(position).getId();
                Intent intent = new Intent(context , CreateBookings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView ;
        Button createBooking  ;
        TextView name , address , state , type ,phone ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            state = itemView.findViewById(R.id.state) ;
            type = itemView.findViewById(R.id.type);
            phone =itemView.findViewById(R.id.phone) ;
            createBooking  = itemView.findViewById(R.id.booking) ;
        }
    }
}
