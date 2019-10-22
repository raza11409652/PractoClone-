package com.hackdroid.hospital.App;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {
    private static String TAG = SessionManager.class.getSimpleName();
    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "FOD Vendor Mobile App";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String LOGGED_IN_MOBILE="loggedInMobile";
    private static final String LOGGED_IN_USER="loggedInUSer";
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }
    public void setLogout(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }
    public  void setLoginMobile(String mobile){
        editor.putString(LOGGED_IN_MOBILE , mobile);
        editor.commit();
        Log.d(TAG , "user logged in mobile modified");
    } public  void setLoginuser(String name){
        editor.putString(LOGGED_IN_USER , name);
        editor.commit();
        Log.d(TAG , "user logged in mobile modified");
    }
    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
    public String getLoggedInMobile(){
        return pref.getString(LOGGED_IN_MOBILE,null);
    }
    public String getLoggedInUser(){
        return pref.getString(LOGGED_IN_USER,null);
    }
}
