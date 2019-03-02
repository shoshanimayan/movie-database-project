package com.example.fablix;



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    Integer page=null;
    Integer limit=1;
    String[] finalTable= new String[limit];


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
         msg = bundle.getString("search");
        if(bundle.getString("page")==null){page=0;}
        setContentView(R.layout.moviepage);
        query = msg;
       query = query.replace(" ", "+");
        tomCat();
        //String[] c = new String[2];
        ArrayList<JSONObject> c = new ArrayList<JSONObject>();

        for(int i =0;i<limit;i++){
            try {
                c.add(table.getJSONObject(i));
            }
            catch(JSONException e){
                Log.d("error in getting json",e.getMessage());

            }
        }
        //c[0]="birdadkjalfdsfgsfdsgfffffg\nfasdsdfadslksdjf;asdjdf\nflasdjfklajfkdjfalk\naskhflkadhklahfkalsd\nhiasdfliajfajf";
       // c[1]= "cat";
        viewer itemsAdapter = new viewer(c,this);
      //  ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,c );
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);



    }


public void tomCat(){
    final RequestQueue queue = NetworkManager.sharedManager(this).queue;
    String URL ="http://10.0.2.2:8080/project1/auto?query=".concat(query);//.concat("&limit=1&page=").concat(Integer.toString(page));

    Log.d("url",URL);
    ((TextView) findViewById(R.id.textView)).setText("searching for: "+msg);
    final StringRequest afterLoginRequest = new StringRequest(Request.Method.GET, URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d("username.reponse", response);
                    try {
                        JSONObject reader = new JSONObject(response);
                        JSONArray ar = reader.getJSONArray("list");
                        ((TextView) findViewById(R.id.textView)).setText(ar.toString());
                        table= ar;

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













}

