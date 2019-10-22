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
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hackdroid.hospital.App.Constant;
import com.hackdroid.hospital.App.CustomLoader;
import com.hackdroid.hospital.App.HospitalAdapter;
import com.hackdroid.hospital.App.Server;
import com.hackdroid.hospital.Model.HospitalList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Hospitalfinder extends AppCompatActivity {
    Toolbar toolbar;
    CustomLoader customLoader;
    RelativeLayout notFound;
    RecyclerView recycleView;
    List<HospitalList> list = new ArrayList<>();
    HospitalAdapter     hospitalAdapter ;

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
        setContentView(R.layout.activity_hospitalfinder);
        toolbar = findViewById(R.id.toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.left_arrow);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // toolbar.setTitle("Manage Booking");
        }
        setTitle("Hospital in " + Constant.USER_LOCATION);
        customLoader = new CustomLoader(this);
        customLoader.show();
        fetchHospital(Constant.USER_LOCATION);
        notFound = findViewById(R.id.notFound);
        recycleView = findViewById(R.id.recycleView);
        recycleView.setHasFixedSize(true);

    }

    private void fetchHospital(final String userLocation) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.HOSPITAL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    customLoader.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean error = jsonObject.getBoolean("error");
                    if (error == false) {
                        int count = jsonObject.getInt("count");
                        Log.d("response", jsonObject.toString());
                        if (count > 0) {
                            recycleView.setVisibility(View.VISIBLE);
                            list = new ArrayList<>();
                            JSONArray data = jsonObject.getJSONArray("records");
                            for (int i = 0; i < data.length(); i++) {
                                HospitalList hospitalList = new HospitalList();
                                JSONObject singleResopnse = data.getJSONObject(i);
                                String name, id, phone, address, city;
                                name = singleResopnse.getString("name");
                                id = singleResopnse.getString("id");
                                phone = singleResopnse.getString("phone");
                                city = singleResopnse.getString("city");
                                address = singleResopnse.getString("address");
                                hospitalList.setAddress(address);
                                hospitalList.setCity(city);
                                hospitalList.setName(name);
                                hospitalList.setPhone(phone);
                                hospitalList.setId(id);
                                list.add(hospitalList);
                            }
                            setupAdapter(list);
                        } else {
                            notFound.setVisibility(View.VISIBLE);
                        }
                    } else {
                        notFound.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    customLoader.dismiss();
                    notFound.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customLoader.dismiss();
                notFound.setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("location", userLocation);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setupAdapter(List<HospitalList> list) {
        hospitalAdapter = new HospitalAdapter(this , Hospitalfinder.this , list);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleView.setAdapter(hospitalAdapter);
    }
}
