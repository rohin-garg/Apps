package com.example.coronavirusmapapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignIn extends AppCompatActivity {

    Button signinButton;
    EditText usernameIn;
    EditText passIn;
    RequestQueue queue;
    Button moveSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportActionBar().setTitle("CovidAlert");

        moveSignUp = findViewById(R.id.moveToSignUp);

        moveSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
            }
        });

        usernameIn = findViewById(R.id.usernameSignIn);
        passIn = findViewById(R.id.passwordSignIn);

        queue = Volley.newRequestQueue(this);
        signinButton = findViewById(R.id.loginButton);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postText(usernameIn.getText().toString(), passIn.getText().toString());
            }
        });
    }

    public void postText(String username, String pass){
        String url = "http://192.168.1.24:5000/signin";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(username, pass);
        final String usernameCopy = username;
        final String passwordCopy = pass;
        final Internet it = new Internet(this);
        JsonObjectRequest request = new JsonObjectRequest(url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String success = response.getString("success");
                    if (success.equals("fail")){
                        Toast.makeText(getApplicationContext(), "Wrong username or password, try again.", Toast.LENGTH_LONG).show();
                    } else if (success.equals("success")){
                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignIn.this, MainActivity.class);
                        intent.putExtra("username", usernameCopy);
                        startActivity(intent);
                    }
                } catch (JSONException e){
                    Log.d("Here", "IN CATCH");
                }
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
