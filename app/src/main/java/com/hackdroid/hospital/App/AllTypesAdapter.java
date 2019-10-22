package com.hackdroid.hospital.App;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hackdroid.hospital.FetchDr;
import com.hackdroid.hospital.Model.DrTypesModel;
import com.hackdroid.hospital.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AllTypesAdapter extends RecyclerView.Adapter<AllTypesAdapter.ViewHolder> {
    List<DrTypesModel> list ;
    Context context ;
    Activity activity ;

    public AllTypesAdapter(List<DrTypesModel> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_layout_type , parent,false);
        return new AllTypesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(list.get(position).getType());
        String urlImage =  Server.ROOT_IMAGE + list.get(position).getImage();
        Picasso.get()
                .load(urlImage)
                .placeholder(R.drawable.icon)
                .error(R.drawable.icon)
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(context , ""+list.get(position).getId() , Toast.LENGTH_SHORT).show();
                Constant.DR_TYPE = list.get(position).getId();
                Intent intent = new Intent(context , FetchDr.class);
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
        TextView title ;
        ImageView imageView ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text) ;
            imageView = itemView.findViewById(R.id.image) ;
        }
    }
}
