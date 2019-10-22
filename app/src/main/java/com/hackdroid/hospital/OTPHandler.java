package com.hackdroid.hospital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hackdroid.hospital.Alert.CustomAlertDanger;
import com.hackdroid.hospital.App.Constant;
import com.hackdroid.hospital.App.CustomLoader;
import com.hackdroid.hospital.App.Server;
import com.hackdroid.hospital.App.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OTPHandler extends AppCompatActivity {
    Toolbar toolbar;
    String mobile = Constant.USER_MOBILE   , otp;
    EditText OTP ;
    Button otpVerify ;
    CustomAlertDanger alertDanger ;
    CustomLoader loader ;
    SessionManager sessionManager  ;
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
        setContentView(R.layout.activity_otphandler);
        toolbar = findViewById(R.id.toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.left_arrow);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        if (getSupportActionBar() !=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // toolbar.setTitle("Manage Booking");
        }
        sessionManager = new SessionManager(this);
        setTitle("Verify ");
        alertDanger = new CustomAlertDanger(this);
        loader = new CustomLoader(this);
        OTP = findViewById(R.id.otpEdt);
        otpVerify = findViewById(R.id.otpVerify);
        otpVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp = OTP.getText().toString() ;
                if(TextUtils.isEmpty(otp)){
                    alertDanger.showAlertDanger("OTP is required");
                    return;
                }else if(otp.length()>5 || otp.length()<5){
                    alertDanger.showAlertDanger("Invalid OTP");
                    return;
                }
                else {
                    startVerify(mobile , otp);
                }
            }
        });

    }

    private void startVerify(final String mobile, final String otp) {
        loader.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.OTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loader.dismiss();
                Log.d("response" ,response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean error = jsonObject.getBoolean("error");
                    if(error == false){
                       // sessionManager = new SessionManager(this);
                        sessionManager.setLogin(true);
                        sessionManager.setLoginMobile(mobile);
                        Intent home = new Intent(getApplicationContext() , Dash.class);
                        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(home);
                        finish();
                    }else{
                        String msg = jsonObject.getString("msg");
                        //alertDanger(msg);
                        alertDanger.showAlertDanger("Error !! "+msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.dismiss();
                Log.d("error "  , error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String> map = new HashMap<>();
                map.put("token" , otp);
                map.put("mobile" , mobile);
                return  map ;
            }
        } ;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest)  ;
    }
}
