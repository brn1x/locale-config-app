package com.example.testemapproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ConfigActivity extends AppCompatActivity {

    //private static final String TAG = "ConfigActivity";

    DatabaseHelper dbHelper;

    private EditText configName;
    private Switch wifiSwitch;
    private SeekBar sbMedia, sbRing, sbAlarm;
    private Button btnSave;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_activity);
        dbHelper = new DatabaseHelper(this);
        configName = (EditText) findViewById(R.id.config_name);
        wifiSwitch = (Switch)findViewById(R.id.wifi_switch);
        sbMedia = (SeekBar)findViewById(R.id.sb_media);
        sbRing = (SeekBar) findViewById(R.id.sb_ring);
        sbAlarm = (SeekBar) findViewById(R.id.sb_alarm);
        btnSave = (Button) findViewById(R.id.btnSave);

        getSupportActionBar().setTitle(R.string.new_config);

        /*Intent intent = getIntent();

        Double longitude = intent.getDoubleExtra("longitude", 0);
        Double latitude = intent.getDoubleExtra("latitude", 0);*/

        final Double longitude = Double.valueOf(0);
        final Double latitude  = Double.valueOf(0);
        final int zoom = 0;


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cName = configName.getText().toString();
                if(cName.length() != 0){
                    int wifi = wifiSwitch.isChecked()? 1 : 0;
                    int media = sbMedia.getProgress();
                    int ring = sbRing.getProgress();
                    int alarm = sbAlarm.getProgress();
                    addData(cName, latitude, longitude, zoom, wifi, media, ring, alarm);
                    Intent intentBack = new Intent(ConfigActivity.this, MainActivity.class);
                    startActivity(intentBack);
                }else{
                    configName.setError("Insira um nome para configuração!");
                }
            }
        });

    }

    public void addData(String configName, Double lat, Double longi,
                        int zoom, int wifi, int media, int ring, int alarm){
        boolean insertData = dbHelper.addData(configName, lat, longi,
                zoom, wifi, media, ring, alarm);

        if(insertData){
            toastMessage("Data Successfully Inserted!");
        }else{
            toastMessage("Something went wrong");
        }
    }

    /*
        customizable toast
        @param message
    */
    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /*public void clearData(){
        configName.setText("");
        wifiSwitch.setChecked(false);
        sbMedia.setProgress(0);
        sbRing.setProgress(0);
        sbAlarm.setProgress(0);
    }*/
}
