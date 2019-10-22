package com.hackdroid.hospital.App;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.hackdroid.hospital.R;

public class CustomLoader {
    Context context ;
    AlertDialog alertDialog ;

    public CustomLoader(Context context) {
        this.context = context;
    }

    public  void show(){
        AlertDialog.Builder alert = new AlertDialog.Builder(context , R.style.customLoader);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_loader , null);
        alert.setView(view);
        alert.setCancelable(false) ;
        alertDialog  = alert.create() ;
        alertDialog.show();
    }
    public void dismiss(){
        if (alertDialog.isShowing()){
            alertDialog.dismiss();
        }
    }
}
