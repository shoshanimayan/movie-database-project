package com.example.fablix;



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;


public class moviePage extends AppCompatActivity {
    String query="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moviepage);
        query="wonder bar";//test query
        // no user is logged in, so we must connect to the server

        // Use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        // 10.0.2.2 is the host machine when running the android emulator
        final StringRequest afterLoginRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2:8080/project1/auto?query=wonder",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("username.reponse", response);
                        ((TextView) findViewById(R.id.textView)).setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((TextView) findViewById(R.id.textView)).setText("error");

                        Log.d("username.error", error.toString());
                    }
                }
        );
        queue.add(afterLoginRequest);

        //((TextView) findViewById(R.id.textView)).setText("you are searching: "+query);


    }

    public void connectToTomcat() {
        Log.d("msg","in tomcat");

        // Use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;


        // 10.0.2.2 is the host machine when running the android emulator
        String URL = "http://10.0.2.2:8080/project1/auto?query="+query;
        RequestQueue que = Volley.newRequestQueue(this);
        JsonArrayRequest array = new JsonArrayRequest( URL, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response){
                Log.d("message",response.toString());
                ((TextView) findViewById(R.id.textView)).setText(response.toString());
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError e){
                Log.d("error",e.getMessage());
                ((TextView) findViewById(R.id.textView)).setText("error");

            }
        });



    }











}

