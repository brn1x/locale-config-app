package com.example.testemapproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


import com.example.testemapproject.Model.LocaleConfig;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class EditConfigActivity extends AppCompatActivity {

    TextView cName, lat, longi, zoom, wifi, media, ring, alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list_activity);
        cName = (TextView) findViewById(R.id.ed_config_name);
        lat = (TextView) findViewById(R.id.ed_lat);
        longi = (TextView) findViewById(R.id.ed_longi);
        zoom = (TextView) findViewById(R.id.ed_zoom);
        wifi = (TextView) findViewById(R.id.ed_wifi);
        media = (TextView) findViewById(R.id.ed_media);
        ring = (TextView) findViewById(R.id.ed_ring);
        alarm = (TextView) findViewById(R.id.ed_alarm);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String iCName = intent.getStringExtra("cName");
        Double iLat = intent.getDoubleExtra("lat", 0);
        Double iLongi = intent.getDoubleExtra("longi", 0);
        int iZoom = intent.getIntExtra("zoom", 0);
        int iWifi = intent.getIntExtra("wifi",0);
        int iMedia = intent.getIntExtra("media",0);
        int iRing = intent.getIntExtra("ring",0);
        int iAlarm = intent.getIntExtra("alarm",0);

        cName.setText(iCName);
        lat.setText(iLat.toString());
        longi.setText(iLongi.toString());
        zoom.setText(iZoom);
        wifi.setText(iWifi);
        media.setText(iMedia);
        ring.setText(iRing);
        alarm.setText(iAlarm);

        //receiveData();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
    }

    public void receiveData(){
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String iCName = intent.getStringExtra("cName");
        Double iLat = intent.getDoubleExtra("lat", 0);
        Double iLongi = intent.getDoubleExtra("longi", 0);
        int iZoom = intent.getIntExtra("zoom", 0);
        int iWifi = intent.getIntExtra("wifi",0);
        int iMedia = intent.getIntExtra("media",0);
        int iRing = intent.getIntExtra("ring",0);
        int iAlarm = intent.getIntExtra("alarm",0);

        /*cName.setText(iCName);
        lat.setText(iLat.toString());
        longi.setText(iLongi.toString());
        zoom.setText(iZoom);
        wifi.setText(iWifi);
        media.setText(iMedia);
        ring.setText(iRing);
        alarm.setText(iAlarm);*/
    }

}
