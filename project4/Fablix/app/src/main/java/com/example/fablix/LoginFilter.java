package com.example.fablix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LoginFilter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String email = intent.getStringExtra(MainActivity.EXTRA_EMAIL);
        String pass = intent.getStringExtra(MainActivity.EXTRA_PASS);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView);
        textView.setText(email);

        TextView textView4 = findViewById(R.id.textView4);
        textView4.setText(pass);
    }
}
