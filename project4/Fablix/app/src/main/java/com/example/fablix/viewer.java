package com.example.fablix;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class viewer extends ArrayAdapter<JSONObject> {
    private ArrayList<JSONObject> j;

    public viewer(ArrayList<JSONObject> movies, Context context) {
        super(context, R.layout.list_v, movies);
        j = movies;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.list_v, parent, false);

        JSONObject movie = j.get(position);

        TextView titleView = (TextView)view.findViewById(R.id.mTitle);
        try {
            titleView.setText(movie.getString("title"));
        }
        catch (JSONException e){
            Log.d("viewer error",e.getMessage());}
        return view;
    }
}
