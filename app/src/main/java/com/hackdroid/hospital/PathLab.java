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
import com.hackdroid.hospital.App.PathAdapter;
import com.hackdroid.hospital.App.Server;
import com.hackdroid.hospital.Model.Path;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PathLab extends AppCompatActivity {
    Toolbar toolbar;
    RelativeLayout loader , notFound;
    RecyclerView recyclerView;
    List<Path> list = new ArrayList<>();

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
        setContentView(R.layout.activity_path_lab);
        toolbar = findViewById(R.id.toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.left_arrow);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // toolbar.setTitle("Manage Booking");
        }
        setTitle("Labs in " + Constant.USER_LOCATION);
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        loader = findViewById(R.id.loader);
        notFound =findViewById(R.id.notFound) ;
        fetchLabs(Constant.USER_LOCATION);
    }

    private void fetchLabs(final String userLocation) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Server.LABS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                try {
                    JSONObject result = new JSONObject(response);
                    Boolean error = result.getBoolean("error");
                    if (error == true) {
                        loader.setVisibility(View.GONE);
                        notFound.setVisibility(View.VISIBLE);
                    } else if (error == false) {
                        int count = result.getInt("count");
                        if (count > 0) {
                            JSONArray jsonArray = result.getJSONArray("records");
                            loader.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            list = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Path path = new Path();
                                String name , id, type , address ;
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                name = jsonObject.getString("name") ;
                                address = jsonObject.getString("address") ;
                                type = jsonObject.getString("type");
                                id = jsonObject.getString("id");
                                path.setAddress(address);
                                path.setName(name);
                                path.setId(id);
                                path.setType(type);
                                list.add(path) ;
                            }
                            setAdapter(list);
                        }else{
                           loader.setVisibility(View.GONE);
                           notFound.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

    private void setAdapter(List<Path> list) {
        PathAdapter pathAdapter = new PathAdapter(list , this , PathLab.this) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pathAdapter);
    }
}
