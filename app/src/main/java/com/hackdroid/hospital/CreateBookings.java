package com.hackdroid.hospital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hackdroid.hospital.Alert.CustomSuccessAlert;
import com.hackdroid.hospital.App.Constant;
import com.hackdroid.hospital.App.CustomLoader;
import com.hackdroid.hospital.App.Server;
import com.hackdroid.hospital.App.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CreateBookings extends AppCompatActivity {
    Toolbar toolbar  ;
    String name = Constant.NAME ;
    ImageView imageView ;
    TextView details ;
    BottomSheetDialog sheetDialog ;
    TextView date ;
    Button close  ;
    Date today =new Date();
    CalendarView calendarView  ;
    Calendar calendar  ;
    String dateSelected  ;
    Spinner time ;
    String[] timeArray = {"9Am-10Am"  , "10Am-11Am" , "11Am-11:30Am" , "12:30Pm-01Pm"};
    Button book ;
    SessionManager sessionManager ;
    CustomLoader customLoader  = new CustomLoader(this);
    CustomSuccessAlert customSuccessAlert = new CustomSuccessAlert(this , this);
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
        setContentView(R.layout.activity_create_bookings);
        toolbar = findViewById(R.id.toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.left_arrow);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        if (getSupportActionBar() !=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // toolbar.setTitle("Manage Booking");
        }
        setTitle(name);
        sessionManager = new SessionManager(this);
        imageView = findViewById(R.id.image);
        details = findViewById(R.id.name);
        details.setText(name+"\n"+Constant.ADDRESS);
        String imageUrl = Server.ROOT_IMAGE + Constant.IMAGE ;
        Picasso.get().load(imageUrl).into(imageView);
        date = findViewById(R.id.date);
        sheetDialog = new BottomSheetDialog(this , R.style.SheetDialog);
        sheetDialog.setContentView(R.layout.dateselector);
        close = sheetDialog.findViewById(R.id.close);
        calendarView = sheetDialog.findViewById(R.id.selectDate);
        calendarView.setMinDate(today.getTime());
        calendar =Calendar.getInstance();
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        time =findViewById(R.id.timeSelect);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,timeArray);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        time.setAdapter(aa);
        dateSelected = sdf.format(today).toString() ;

        date.setText(sdf.format(today).toString());
        book = findViewById(R.id.book);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int yr, int month, int day) {
                calendar.set(Calendar.YEAR , yr);
                calendar.set(Calendar.MONTH , month);
                calendar.set(Calendar.DAY_OF_MONTH , day);
                month = month+1;
                String Month = null , Day = null;
                if (month<10){
                    Month="0"+month;
                }else{
                    Month =String.valueOf( month);
                }
                if (day<10){
                    Day = "0"+day;
                }else{
                    Day = String.valueOf(day);
                }
                dateSelected = yr+"-"+Month+"-"+Day;
                date.setText(dateSelected);
                calendarView.setDate(calendar.getTimeInMillis());
                sheetDialog.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sheetDialog.isShowing()){
                    sheetDialog.dismiss();
                }
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.show();
            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedTime = time.getSelectedItem().toString();
               // Toast.makeText(getApplicationContext() , selectedTime , Toast.LENGTH_SHORT).show();
                if(TextUtils.isEmpty(selectedTime ) &&TextUtils.isEmpty(dateSelected)){
                    return;
                }else{
                    customLoader.show();
                    createBooking(sessionManager.getLoggedInMobile() , Constant.ID_FOR_BOOKING , selectedTime , dateSelected);
                }
            }
        });
    }

    private void createBooking(final String loggedInMobile, final String idForBooking, final String selectedTime, final String dateSelected) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.CREATE_BOOKING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("repone" , response);
                customLoader.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean error  =jsonObject.getBoolean("error");
                    if (error == false){
                        customSuccessAlert.showAlert("Your Booking has been scheduled \n " +
                                "You can manage your booking in Booking Tab");
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //return super.getParams();
                HashMap<String , String> map = new HashMap<>();
                map.put("user" , loggedInMobile) ;
                map.put("doctor" , idForBooking);
                map.put("time" , selectedTime) ;
                map.put("date" , dateSelected);
                return  map ;


            }
        } ;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest) ;
    }
}
