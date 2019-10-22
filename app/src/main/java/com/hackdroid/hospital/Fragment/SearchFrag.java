package com.hackdroid.hospital.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hackdroid.hospital.App.BookingAdapter;
import com.hackdroid.hospital.App.CustomLoader;
import com.hackdroid.hospital.App.Server;
import com.hackdroid.hospital.App.SessionManager;
import com.hackdroid.hospital.Model.Bookings;
import com.hackdroid.hospital.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchFrag extends Fragment {
    View view ;
    RelativeLayout notFound ;
    Animation animation ;
    SessionManager sessionManager  ;
    CustomLoader customLoader ;
    RecyclerView recyclerView ;
    List<Bookings> list = new ArrayList<>();
    public SearchFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_search, container, false);
        animation = AnimationUtils.loadAnimation(getContext() , R.anim.fedup);

        notFound = view.findViewById(R.id.notFound);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        //notFound.setAnimation(animation);
        sessionManager = new SessionManager(getContext()) ;
        loadBookings(sessionManager.getLoggedInMobile());
        customLoader =new CustomLoader(getContext());
        customLoader.show();
        return  view ;
    }

    private void loadBookings(final String user) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.GET_BOOKING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response" , response);
                customLoader.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean error = jsonObject.getBoolean("error");
                    if (error){
                        notFound.setAnimation(animation);
                        notFound.setVisibility(View.VISIBLE);

                    }else{

                        recyclerView.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = jsonObject.getJSONArray("records");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject r = jsonArray.getJSONObject(i);
                            Bookings bookings = new Bookings();
                            String date ,time , doctor , id , add ;
                            date = r.getString("date");
                            time = r.getString("time");
                            doctor = r.getString("doctor");
                            id = r.getString("id");
                            add = r.getString("address");
                            bookings.setAddress(add);
                            bookings.setDate(date);
                            bookings.setId(id);
                            bookings.setDoctor(doctor);
                            bookings.setTime(time);
                            list.add(bookings);
                        }
                        setAdapter(list);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                customLoader.dismiss();
                notFound.setAnimation(animation);
                notFound.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> map = new HashMap<>();
                map.put("mobile" , user);
                return map  ;
            }
        } ;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void setAdapter(List<Bookings> list) {
        BookingAdapter adapter = new BookingAdapter(getContext() ,list , getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

}
