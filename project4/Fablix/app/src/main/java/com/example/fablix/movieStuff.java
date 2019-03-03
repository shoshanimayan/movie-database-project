package com.example.fablix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class movieStuff extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_stuff);

        Bundle bundle = getIntent().getExtras();
        String msg = bundle.getString("page");

        try {
            JSONObject movie = new JSONObject(msg);
            String title = movie.getString("value");
            String year = movie.getString("year");
            String director = movie.getString("Dir");
            String rating = movie.getString("rate");
            String genres = movie.getString("genres");
            String stars = movie.getString("stars");

            ((TextView) findViewById(R.id.textView2)).setText(title);
            ((TextView) findViewById(R.id.textView13)).setText(year);
            ((TextView) findViewById(R.id.textView14)).setText(director);
            ((TextView) findViewById(R.id.textView15)).setText(rating);
            ((TextView) findViewById(R.id.textView16)).setText(genres);
            ((TextView) findViewById(R.id.textView17)).setText(stars);
        }

        catch(JSONException e){
            Log.d("error in getting json",e.getMessage());
        }


    }

}
