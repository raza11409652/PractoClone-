package com.hackdroid.hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import com.hackdroid.hospital.App.CustomLoader;
import com.hackdroid.hospital.App.Server;
import com.hackdroid.hospital.App.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Login extends AppCompatActivity {
Button login  , register  ;
String mobile , password ;
EditText mobileEdt , passwordEdt ;
CustomAlertDanger customAlertDanger ;
CustomLoader customLoader ;
SessionManager sessionManager ;
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


        setContentView(R.layout.activity_login);
        //TODO:://INIT view
        mobileEdt = findViewById(R.id.mobile);
        passwordEdt = findViewById(R.id.password);
        register = findViewById(R.id.registerBtn);
        login = findViewById(R.id.loginBtn);
        customAlertDanger = new CustomAlertDanger(this);
        customLoader = new CustomLoader(this);
        sessionManager = new SessionManager(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext() , Register.class);
                registerIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(registerIntent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobile = mobileEdt.getText().toString() ;
                password = passwordEdt.getText().toString() ;
                if(TextUtils.isEmpty(mobile)){
                    customAlertDanger.showAlertDanger("Mobile number is required");
                    return;
                }else if(TextUtils.isEmpty(password)){
                    customAlertDanger.showAlertDanger("Password is required");
                    return;
                }else{
                    startLogin(mobile , password);
                }
            }
        });
    }

    private void startLogin(final String mobile, final String password) {
        customLoader.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response" , response);
                customLoader.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean error = jsonObject.getBoolean("error");
                    if(error == false){
                        sessionManager.setLogin(true);
                        sessionManager.setLoginMobile(mobile);
                        Intent home = new Intent(getApplicationContext() , Dash.class);
                        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(home);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customLoader.dismiss();
                Log.d("error" , error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String>map = new HashMap<>();
                map.put("mobile" , mobile);
                map.put("password" , password);
                return map ;
            }
        } ;
        RequestQueue requestQueue = Volley.newRequestQueue(this );
        requestQueue.add(stringRequest);
    }
}
