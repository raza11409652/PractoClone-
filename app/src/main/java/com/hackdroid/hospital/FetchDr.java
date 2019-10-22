package com.hackdroid.hospital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hackdroid.hospital.App.AdapterDr;
import com.hackdroid.hospital.App.Constant;
import com.hackdroid.hospital.App.CustomLoader;
import com.hackdroid.hospital.App.Server;
import com.hackdroid.hospital.Model.Doctors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FetchDr extends AppCompatActivity {
    Toolbar toolbar ;
    String location = Constant.USER_LOCATION , type = Constant.DR_TYPE ;
    List<Doctors> list  = new ArrayList<>();
    AdapterDr adapterDr ;
    RelativeLayout notFound ;
    RecyclerView recyclerView ;
    CustomLoader customLoader ;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Poppins-Regular.ttf")
                .setFontAttrId(R.attr.fontPath).build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_dr);
        toolbar = findViewById(R.id.toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.left_arrow);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        if (getSupportActionBar() !=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // toolbar.setTitle("Manage Booking");
        }
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        notFound = findViewById(R.id.notFound);
        customLoader = new CustomLoader(this);
        customLoader.show();
        if(TextUtils.isEmpty(type)){
            //customLoader.dismiss();
            fetchDrByLocation(location);
        }else {
           // Toast.makeText(getApplicationContext() , ""+type , Toast.LENGTH_SHORT).show();
           // customLoader.dismiss();
            fetchDrByLocationAndType(location , type);
        }
    }

    private void fetchDrByLocationAndType(final String location, final String type) {
        StringRequest request = new StringRequest(Request.Method.POST, Server.FETCH_DR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response" , response);
                customLoader.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean error = jsonObject.getBoolean("error") ;
                    if(error==false){
                        recyclerView.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = jsonObject.getJSONArray("records") ;
                        if(jsonArray.length()>0){
                            for (int i=0;i<jsonArray.length() ; i++){
                                Doctors doctors = new Doctors();
                                String id ,name , city , type , image , address , state , phone ;
                                JSONObject result = jsonArray.getJSONObject(i);
                                id = result.getString("id");
                                name = result.getString("name") ;
                                city = result.getString("city") ;
                                type = result.getString("type");
                                image = result.getString("image") ;
                                address = result.getString("address") ;
                                state = result.getString("state") ;
                                phone = result.getString("phone");
                                doctors.setAddress(address);
                                doctors.setName(name);
                                doctors.setImage(image);
                                doctors.setPhone(phone);
                                doctors.setType(type);
                                doctors.setState(state);
                                doctors.setCity(city);
                                doctors.setId(id);
                                list.add(doctors);
                            }
                            setAdapter(list);
                        }else{
                            notFound.setVisibility(View.VISIBLE);
                        }
                    }else{
                        notFound.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            customLoader.dismiss();
            notFound.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String ,String> map = new HashMap<>();
                map.put("city" , location) ;
                map.put("type" , type);
                 return map ;
            }
        } ;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request) ;
    }

    private void setAdapter(List<Doctors> list) {
        adapterDr = new AdapterDr(list , this , FetchDr.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterDr);
    }

    private void fetchDrByLocation(final String location) {
        StringRequest request = new StringRequest(Request.Method.POST, Server.FETCH_DR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response" , response);
                customLoader.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean error = jsonObject.getBoolean("error") ;
                    if(error==false){
                        recyclerView.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = jsonObject.getJSONArray("records") ;
                        if(jsonArray.length()>0){
                            for (int i=0;i<jsonArray.length() ; i++){
                                Doctors doctors = new Doctors();
                                String id ,name , city , type , image , address , state , phone ;
                                JSONObject result = jsonArray.getJSONObject(i);
                                id = result.getString("id");
                                name = result.getString("name") ;
                                city = result.getString("city") ;
                                type = result.getString("type");
                                image = result.getString("image") ;
                                address = result.getString("address") ;
                                state = result.getString("state") ;
                                phone = result.getString("phone");
                                doctors.setAddress(address);
                                doctors.setName(name);
                                doctors.setImage(image);
                                doctors.setPhone(phone);
                                doctors.setType(type);
                                doctors.setState(state);
                                doctors.setCity(city);
                                doctors.setId(id);
                                list.add(doctors);
                            }
                            setAdapter(list);
                        }else{
                            notFound.setVisibility(View.VISIBLE);
                        }
                    }else{
                        notFound.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customLoader.dismiss();
                notFound.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String ,String> map = new HashMap<>();
                map.put("city" , location) ;
              //  map.put("type" , type);
                return map ;
            }
        } ;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request) ;
    }

}
