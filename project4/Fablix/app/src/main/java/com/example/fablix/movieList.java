package com.example.fablix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


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


public class movieList extends AppCompatActivity {
    String query="";
    JSONArray table = new JSONArray();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent= getIntent();
        String msg = intent.getStringExtra("search");
        setContentView(R.layout.moviepage);
        query = "wonder+bar";
        query = query.replace(" ", "_");
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        String URL ="http://10.0.2.2:8080/project1/auto?query=".concat(query);

        Log.d("url",URL);
        ((TextView) findViewById(R.id.textView)).setText(msg);
        // 10.0.2.2 is the host machine when running the android emulator
        final StringRequest afterLoginRequest = new StringRequest(Request.Method.GET, URL,
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
                        ((TextView) findViewById(R.id.textView)).setText("ERROR");

                        Log.d("username.error", error.toString());
                    }
                }
        );
        queue.add(afterLoginRequest);



    }













}


