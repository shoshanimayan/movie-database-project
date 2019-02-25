package com.example.fablix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_EMAIL = "com.example.fablix.EMAIL";
    public static final String EXTRA_PASS = "com.example.fablix.PASS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the Login button */
    public void sendLogin(View view) {
        Intent intent = new Intent(this, LoginFilter.class);

        EditText editText = findViewById(R.id.editText);
        String email = editText.getText().toString();
        intent.putExtra(EXTRA_EMAIL, email);

        EditText editText2 = findViewById(R.id.editText2);
        String pass = editText2.getText().toString();
        intent.putExtra(EXTRA_PASS, pass);

        startActivity(intent);

    }
}
