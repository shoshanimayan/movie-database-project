package com.example.fablix;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
    }

    public void verifyLogin(View view) {

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        EditText editText = (EditText) findViewById(R.id.editText);
        String email = editText.getText().toString();

        editText = (EditText) findViewById(R.id.editText2);
        String password = editText.getText().toString();

        String query = email.concat("_");
        query = query.concat(password);

        String URL ="https://ec2-13-58-114-64.us-east-2.compute.amazonaws.com:8443/project1/AndroidLogin?query=".concat(query);

        final StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject j = new JSONObject(response);
                            String msg = j.getString("msg");

                            if (msg.equals("Success")) {
                                Intent goToSearch = new Intent(context, search.class);
                                startActivity(goToSearch);
                            }

                            else {
                                ((TextView) findViewById(R.id.textView4)).setText(msg);
                            }

                        }
                        catch(JSONException e){
                            ((TextView) findViewById(R.id.textView4)).setText("ERROR");

                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((TextView) findViewById(R.id.textView4)).setText("ERROR");

                    }
                }
        );
        queue.add(request);

    }

}
