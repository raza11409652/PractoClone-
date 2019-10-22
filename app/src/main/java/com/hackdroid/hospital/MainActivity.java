package com.hackdroid.hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.hackdroid.hospital.App.SessionManager;

/*
* TODO :: This is Splash screen first screen to be attempt
* */
public class MainActivity extends AppCompatActivity {
SessionManager sessionManager  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()){
            Intent home  = new Intent(getApplicationContext() , Dash.class);
            startActivity(home);
            finish();
        }else{
            Intent login  = new Intent(getApplicationContext() , Login.class);
            startActivity(login);
            finish();
        }

    }
}
