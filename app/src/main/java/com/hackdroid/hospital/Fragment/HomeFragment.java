package com.hackdroid.hospital.Fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hackdroid.hospital.AllDrTypes;
import com.hackdroid.hospital.App.Constant;
import com.hackdroid.hospital.App.DrtypesAdapter;
import com.hackdroid.hospital.App.Server;
import com.hackdroid.hospital.FetchDr;
import com.hackdroid.hospital.Hospitalfinder;
import com.hackdroid.hospital.LocationSelector;
import com.hackdroid.hospital.Model.DrTypesModel;
import com.hackdroid.hospital.PathLab;
import com.hackdroid.hospital.Pharmacy;
import com.hackdroid.hospital.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    View view ;
    TextView location ;
    public  static  final  int LOCATION_REQ =111 ;
    CardView pharmacyView , pathLabCard  , hospitalFinder ,drFinder;
    LinearLayout dr_types_holder ;
    ProgressBar loader ;
    RecyclerView recyclerView ;
    DrtypesAdapter adapter ;
    List<DrTypesModel> list = new ArrayList<>() ;
    Button viewAll  ;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_home, container, false);
        location = view.findViewById(R.id.location) ;
        location.setText(Constant.USER_LOCATION);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent locationSearch = new Intent(getContext() , LocationSelector.class) ;
                startActivityForResult(locationSearch , LOCATION_REQ);
            }
        });
        pharmacyView = view.findViewById(R.id.pharmacyCard);
        pathLabCard = view.findViewById(R.id.pathLabCard) ;
        hospitalFinder = view.findViewById(R.id.hospitalFinder) ;
        loader = view.findViewById(R.id.loader);
        dr_types_holder =view.findViewById(R.id.dr_types_holder) ;
        recyclerView = view.findViewById(R.id.dr_types);
        viewAll = view.findViewById(R.id.viewAll) ;
        drFinder =view.findViewById(R.id.drFinder) ;
        recyclerView.setHasFixedSize(true);
        pharmacyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pharamcySearch = new Intent(getContext() , Pharmacy.class) ;
                pharamcySearch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(pharamcySearch);
            }
        });
        pathLabCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pathlab = new Intent(getContext() , PathLab.class);
                pathlab.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(pathlab);
            }
        });
        hospitalFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hospital = new Intent(getContext() , Hospitalfinder.class) ;
                hospital.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(hospital);
            }
        });
        fetchDashDr();
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewallDr = new Intent(getContext() , AllDrTypes.class);
                viewallDr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(viewallDr);
            }
        });
        drFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.DR_TYPE = null ;
                Intent intent = new Intent(getContext() , FetchDr.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        return  view ;
    }

    private void fetchDashDr() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Server.TYPES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response"  ,response);
                loader.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean error = jsonObject.getBoolean("error") ;
                    if(error ==false){
                        dr_types_holder.setVisibility(View.VISIBLE);
                        JSONArray data = jsonObject.getJSONArray("records");
                        for(int i=0;i<data.length() ;i++){
                            DrTypesModel drTypesModel = new DrTypesModel();
                            JSONObject responseSingle = data.getJSONObject(i);
                            String title , id , image ;
                            title = responseSingle.getString("title");
                            id = responseSingle.getString("id");
                            image = responseSingle.getString("image");
                            drTypesModel.setId(id);
                            drTypesModel.setImage(image);
                            drTypesModel.setType(title);
                            list.add(drTypesModel);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void setAdapter(List<DrTypesModel> list) {
        adapter = new DrtypesAdapter(list , getContext() , getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL , false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOCATION_REQ && resultCode== Activity.RESULT_OK){
            //String location  = Constant.USER_LOCATION ;
            location.setText(Constant.USER_LOCATION);
            //Toast.makeText(getContext() , ""+location , Toast.LENGTH_SHORT).show();
        }
    }
}
