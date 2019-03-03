package com.example.fablix;



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class moviePage extends AppCompatActivity {
    String query="";
    String msg="";
    JSONArray table = new JSONArray();
    Integer page=0;
    Integer limit=2;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
         msg = bundle.getString("search");
         if(bundle.getString("move")==null){page=0;}
         else{page = bundle.getInt("move");}
        }
        setContentView(R.layout.moviepage);
        query = msg;
       query = query.replace(" ", "+");
        tomCat();
        //String[] c = new String[2];


    }


public void tomCat(){
    final RequestQueue queue = NetworkManager.sharedManager(this).queue;
    String URL;
    if(!query.equals("")){
    URL ="https://ec2-13-58-114-64.us-east-2.compute.amazonaws.com:8443/project1/auto?query=".concat(query);}
    else{
        URL ="https://ec2-13-58-114-64.us-east-2.compute.amazonaws.com:8443/project1/auto?";
}
    ((TextView) findViewById(R.id.textView)).setText("searching for: "+msg);
    final StringRequest afterLoginRequest = new StringRequest(Request.Method.GET, URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d("username.reponse", response);
                    try {
                        JSONObject reader = new JSONObject(response);
                        JSONArray ar = reader.getJSONArray("list");
                        Log.d("jsonarrystuff",ar.toString());
                        table= ar;
                        ArrayList<JSONObject> c = new ArrayList<JSONObject>();

                        for(int i =page;i<(limit+page);i++){
                            try {
                                if(i>table.length()){break;}
                                Log.d("index",Integer.toString(i));
                               // Log.d("index",table.getJSONObject(0).toString());
                                c.add(ar.getJSONObject(i));
                            }
                            catch(JSONException e){
                                Log.d("error in getting json",e.getMessage());

                            }
                        }

                        viewer adp = new viewer(c,moviePage.this);
                        ListView listView = (ListView) findViewById(R.id.list);
                        listView.setAdapter(adp);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String clicked = parent.getItemAtPosition(position).toString();
                               try {
                                   JSONObject send = new JSONObject(clicked);
                                 //  Toast.makeText(getApplicationContext(),send.toString() , Toast.LENGTH_SHORT).show();
                                   Intent goToIntent = new Intent(moviePage.this, movieStuff.class);
                                   goToIntent.putExtra("page", send.toString());
                                   goToIntent.putExtra("search", msg);
                                   startActivity(goToIntent);

                               }catch(JSONException e){Log.d("sending error",e.getMessage());}
                            }
                        });





                    }
                    catch(JSONException e){
                        Log.d("username.error", e.toString());
                        ((TextView) findViewById(R.id.textView)).setText("ERROR in parse");
                    }
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



    public void nextPage(View view) {

        page+=limit;
        if(page>table.length()){page=table.length()-limit;}
        if(page<0){page=0;}
        tomCat();



    }
    public void prevPage(View view) {

        page-=limit;
        if(page<0){page=0;}
        tomCat();



    }


}

