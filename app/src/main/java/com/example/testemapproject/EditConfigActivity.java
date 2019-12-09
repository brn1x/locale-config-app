package com.example.testemapproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditConfigActivity extends AppCompatActivity {

    //private static final String TAG = "EditConfigActivity";

    DatabaseHelper dbHelper;

    private EditText configName;
    private Switch wifiSwitch;
    private SeekBar sbMedia, sbRing, sbAlarm;
    private Button btnEditSave, btnEditLocation;

    private int iID;
    private String iCName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_edit_activity);
        dbHelper = new DatabaseHelper(this);
        configName = (EditText) findViewById(R.id.ed_config_name);
        wifiSwitch = (Switch) findViewById(R.id.ed_wifi_switch);
        sbMedia = (SeekBar) findViewById(R.id.ed_sb_media);
        sbRing = (SeekBar) findViewById(R.id.ed_sb_ring);
        sbAlarm = (SeekBar) findViewById(R.id.ed_sb_alarm);
        btnEditLocation = (Button) findViewById(R.id.ed_btnEditMap);
        btnEditSave = (Button) findViewById(R.id.ed_btnSaveChanges);

        Intent receivedIntent = getIntent();
        iID = receivedIntent.getIntExtra("id", 0);
        iCName = receivedIntent.getStringExtra("cName");
        final Double iLat = receivedIntent.getDoubleExtra("lat", 0);
        final Double iLongi = receivedIntent.getDoubleExtra("longi", 0);
        final int iZoom = receivedIntent.getIntExtra("zoom", 0);
        final int iWifi = receivedIntent.getIntExtra("wifi",0);
        final int iMedia = receivedIntent.getIntExtra("media",0);
        final int iRing = receivedIntent.getIntExtra("ring",0);
        final int iAlarm = receivedIntent.getIntExtra("alarm",0);

        configName.setText(iCName);
        wifiSwitch.setChecked(iWifi==1? true : false);
        sbMedia.setProgress(iMedia);
        sbRing.setProgress(iRing);
        sbAlarm.setProgress(iAlarm);

        getSupportActionBar().setTitle(R.string.edit_config);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnEditLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditConfigActivity.this, MapsActivity.class);
                intent.putExtra("lat", iLat);
                intent.putExtra("longi", iLongi);
                intent.putExtra("zoom", iZoom);
                intent.putExtra("wifi", iWifi);
                intent.putExtra("media", iMedia);
                intent.putExtra("ring", iRing);
                intent.putExtra("alarm", iAlarm);
                startActivity(intent);
            }
        });

        btnEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = configName.getText().toString();
                if(newName.length() != 0) {
                    int wifi = wifiSwitch.isChecked() ? 1 : 0;
                    int media = sbMedia.getProgress();
                    int ring = sbRing.getProgress();
                    int alarm = sbAlarm.getProgress();
                    dbHelper.updateConfig(iID,iCName,newName,iLat,iLongi,iZoom,wifi,media,ring,alarm);
                    toastMessage("Alterações salvas!");
                    Intent goHomeIntent = new Intent(EditConfigActivity.this, MainActivity.class);
                    startActivity(goHomeIntent);
                }else{
                    configName.setError("Insira um nome para a configuração!");
                }
            }
        });
    }

    /*public void updateData(String oldCName, String newCName, Double lat, Double longi,
                           int zoom, int wifi, int media, int ring, int alarm){



    }*/
    /*public void receiveData(){
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
      zoom.setText(String.valueOf(iZoom));
      wifi.setText(String.valueOf(iWifi));
      media.setText(String.valueOf(iMedia));
      ring.setText(String.valueOf(iRing));
      alarm.setText(String.valueOf(iAlarm));
  }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteConfig:
                dbHelper.deleteConfig(iID, iCName);
                toastMessage("Configuração excluida");
                Intent intent = new Intent(EditConfigActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /*
      customizable toast
      @param message
    */
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
