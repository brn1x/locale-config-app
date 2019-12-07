package com.example.testemapproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ConfigActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_activity);

        Intent intent = getIntent();

        Double longitude = intent.getDoubleExtra("longitude", 0);
        Double latitude = intent.getDoubleExtra("latitude", 0);

    }
}
