package com.hackdroid.hospital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hackdroid.hospital.App.LocationAdapter;
import com.hackdroid.hospital.App.Server;
import com.hackdroid.hospital.Model.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LocationSelector extends AppCompatActivity {
    List<Location> list = new ArrayList<>();
    LocationAdapter locationAdapter ;
    RecyclerView recyclerView    ;
    RelativeLayout relativeLayout ;
    EditText locationEdt  ;
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
        setContentView(R.layout.activity_location_selector);
        recyclerView = findViewById(R.id.recycleView);
        relativeLayout = findViewById(R.id.loaderLayout) ;
        //recyclerView .
        locationEdt = findViewById(R.id.locationEdt) ;
        recyclerView.setHasFixedSize(true);
        locationEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                final  List<Location> filtermodelist=filter(list,editable.toString());
                try {
                    locationAdapter.filterList(filtermodelist);
                }catch (Exception e){

                }
            }
        });
        fetchLocation();
    }

    private void fetchLocation() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Server.CITY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Log.d("response" , response);
                try {
                    JSONObject jsonObject =  new JSONObject(response);
                    int count = jsonObject.getInt("count") ;
                    Log.d("count" , ""+count);
                    if (count > 0){
                        JSONArray array = jsonObject.getJSONArray("records") ;
                        list = new ArrayList<>();
                        for(int i=0; i<array.length() ; i++){
                            //location
                            relativeLayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            Location location =  new Location();
                            String id , city ;
                            JSONObject listObject = array.getJSONObject(i);
                            id = listObject.getString("id");
                            city = listObject.getString("city");
                            location.setId(id);
                            location.setLocation(city);
                            list.add(location) ;
                        }
                        setAdapter(list);
                    }else{
                        Toast.makeText(getApplicationContext() , "Sorry no city found" , Toast.LENGTH_SHORT).show();
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
        requestQueue.add(stringRequest) ;
    }
    private List<Location> filter(List<Location> pl,String query) {
        query=query.toLowerCase();
        final List<Location> filteredModeList=new ArrayList<>();
        for (Location model:pl)
        {
            final String text=model.getLocation().toLowerCase();
            if (text.contains(query))
            {
                filteredModeList.add(model);
            }
        }
        return filteredModeList;
    }
    private void setAdapter(List<Location> list) {
        locationAdapter = new LocationAdapter(this , list , LocationSelector.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(locationAdapter);

    }
}
