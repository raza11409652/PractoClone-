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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hackdroid.hospital.FetchDr;
import com.hackdroid.hospital.Model.DrTypesModel;
import com.hackdroid.hospital.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DrtypesAdapter extends RecyclerView.Adapter<DrtypesAdapter.ViewHolder> {
    List<DrTypesModel> list ;
    Context context  ;
    Activity activity ;
    RequestOptions options ;
    public DrtypesAdapter(List<DrTypesModel> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;
        //options = new RequestOptions();
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.icon)
                .error(R.drawable.icon);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_dr_type , parent , false) ;
        return new DrtypesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(list.get(position).getType());
        //        Glide.with(mContext).load(mData.get(position).getUserprofile()).into(holder.imageView);
        String urlImage =  Server.ROOT_IMAGE + list.get(position).getImage();
       // Glide.with(context).load(urlImage).apply(options).into(holder.imageView);
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
        return list.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView ;
        TextView title ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title) ;
        }
    }
}
