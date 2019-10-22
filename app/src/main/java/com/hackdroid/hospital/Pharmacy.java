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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hackdroid.hospital.App.DrugsAdapter;
import com.hackdroid.hospital.App.Server;
import com.hackdroid.hospital.Model.Drugs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Pharmacy extends AppCompatActivity {
   Toolbar toolbar ;
   List<Drugs> list = new ArrayList<>() ;
   RecyclerView recyclerView  ;
   RelativeLayout pharmacyLoader ;
   DrugsAdapter drugsAdapter ;
   EditText searchbox ;
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
        setContentView(R.layout.activity_pharmacy);
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
        pharmacyLoader = findViewById(R.id.pharmacyLoader) ;
        searchbox = findViewById(R.id.search) ;

        setTitle("Pharmacy");
        fetchPharmacy();
    searchbox.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        final  List<Drugs> filtermodelist=filter(list,editable.toString());
        try {
            drugsAdapter.filterList(filtermodelist);
        }catch (Exception e){

        }
    }
});

    }
    private List<Drugs> filter(List<Drugs> pl,String query) {
        query=query.toLowerCase();
        final List<Drugs> filteredModeList=new ArrayList<>();
        for (Drugs model:pl)
        {
            final String text=model.getName().toLowerCase();
            if (text.contains(query))
            {
                filteredModeList.add(model);
            }
        }
        return filteredModeList;
    }

    private void fetchPharmacy() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Server.DRUGS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response" , response) ;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean error = jsonObject.getBoolean("error") ;
                    if(error==false)  {
                        int count = jsonObject.getInt("count") ;
                        if(count>0){
                            list = new ArrayList<>();
                            pharmacyLoader.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            JSONArray records = jsonObject.getJSONArray("records");
                            for(int i=0;i<records.length() ; i++){
                                Drugs drugs = new Drugs();
                                String id , name , ven ,remarks , grp ;
                                JSONObject singleData = records.getJSONObject(i);
                                id = singleData.getString("id") ;
                                name = singleData.getString("name") ;
                                ven = singleData.getString("ven") ;
                                remarks = singleData.getString("remarks");
                                grp = singleData.getString("grp") ;
                                drugs.setId(id);drugs.setGroup(grp);
                                drugs.setName(name); drugs.setVen(ven); drugs.setRemarks(remarks);
                                list.add(drugs);
                            }
                            setAdapter(list);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error" , error .toString());
            }
        }) ;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest) ;
    }

    private void setAdapter(List<Drugs> list) {
        drugsAdapter = new DrugsAdapter(Pharmacy.this , this , list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator( new DefaultItemAnimator());
        recyclerView.setAdapter(drugsAdapter);
    }
}
