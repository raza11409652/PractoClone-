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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hackdroid.hospital.App.AllTypesAdapter;
import com.hackdroid.hospital.App.CustomLoader;
import com.hackdroid.hospital.App.Server;
import com.hackdroid.hospital.Model.DrTypesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AllDrTypes extends AppCompatActivity {
    Toolbar toolbar ;
    CustomLoader customLoader  ;
    RecyclerView recycleView ;
    List<DrTypesModel> list =new ArrayList<>();
    AllTypesAdapter allTypesAdapter  ;
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
        setContentView(R.layout.activity_all_dr_types);
        toolbar = findViewById(R.id.toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.left_arrow);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        if (getSupportActionBar() !=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // toolbar.setTitle("Manage Booking");
        }
        setTitle("Specialities");
        customLoader = new CustomLoader(this);
        customLoader.show();
        recycleView = findViewById(R.id.recycleView) ;
        recycleView.setHasFixedSize(true);
        fetchAll();
    }

    private void fetchAll() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Server.TYPES_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            customLoader.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean error = jsonObject.getBoolean("error") ;
                    if (error == false){
                        JSONArray data = jsonObject.getJSONArray("records") ;
                        for (int i=0;i<data.length() ; i++){
                            JSONObject result = data.getJSONObject(i);
                            DrTypesModel drTypesModel = new DrTypesModel();
                            String image , id , title  ;
                            image = result.getString("image") ;
                            title = result.getString("title") ;
                            id = result.getString("id");
                            drTypesModel.setType(title);
                            drTypesModel.setImage(image);
                            drTypesModel.setId(id);
                            list.add(drTypesModel) ;
                        }
                        setAdapter(list);
                    }else{

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) ;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setAdapter(List<DrTypesModel> list) {
        allTypesAdapter = new AllTypesAdapter(list , this  , AllDrTypes.this) ;
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleView.setAdapter(allTypesAdapter);
    }
}
