package com.hackdroid.hospital.Alert;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.hackdroid.hospital.R;

public class CustomAlertDanger {
    Context context ;
    AlertDialog alertDialog;

    public CustomAlertDanger(Context context) {
        this.context = context;
    }
    public  void showAlertDanger(String msg){

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.alert_danger , null);
        TextView text  = view.findViewById(R.id.text);
        ImageView imageView= view.findViewById(R.id.image);
       // imageView.setAnimation(animation);
        Button ok = view.findViewById(R.id.ok) ;

        text.setText(msg);
        alert.setView(view);
        alert.setCancelable(false) ;
//        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
        alertDialog  = alert.create() ;
        alertDialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (alertDialog.isShowing()){
                        dismiss();
                    }
            }
        });
    }
    public  void dismiss(){
        alertDialog.dismiss();
    }
}

