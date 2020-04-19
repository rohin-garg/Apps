package com.example.coronavirusmapapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;

public class HasCorona extends AppCompatActivity {

    Button coronaB;
    String username;
    RequestQueue queue;
    Button backB;
    DatePickerDialog picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_has_corona);

        getSupportActionBar().setTitle("CovidAlert");

        coronaB = findViewById(R.id.confirm);
        //backB = findViewById(R.id.backbuttoncoronascreen);

        username = getIntent().getStringExtra("username");
        queue = Volley.newRequestQueue(this);
        coronaB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(HasCorona.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Intent intent = new Intent(HasCorona.this, MainActivity.class);
                                intent.putExtra("username", username);
                                startActivity(intent);
                            }
                        }, year, month, day);
                picker.show();
                //hasCoronaB();

            }
        });


    }

    public void hasCoronaB () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to continue? This action cannot be undone.");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                postUser();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void postUser () {
        String url = "http://192.168.1.24:5000/hascorona";
        HashMap<String, String> params = new HashMap<>();
        if (username == null){return;}
        Log.d("username", username);
        params.put("username", username);
        final Internet it = new Internet(this);
        JsonObjectRequest request = new JsonObjectRequest(url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("onResponse", "hascorona");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Here", "onErrorResponse");
                if (error instanceof TimeoutError) {
                    Log.d("Timeout:", "on response");
                } else if (error instanceof NoConnectionError){
                    Log.d("No connection", "on response");
                    Boolean bo = it.Check();
                    Log.d("Internet Connection", bo.toString());
                    error.printStackTrace();
                } else if (error instanceof AuthFailureError) {
                    error.printStackTrace();
                } else if (error instanceof ServerError) {
                    error.printStackTrace();
                } else if (error instanceof NetworkError) {
                    error.printStackTrace();
                } else if (error instanceof ParseError) {
                    //TODO
                }
            }
        });
        queue.add(request);
    }
}
