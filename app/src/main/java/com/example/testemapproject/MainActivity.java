package com.example.testemapproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.testemapproject.Model.LocaleConfig;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //private static final String TAG = "MainListActivity";

    DatabaseHelper dbHelper;
    private ListView configList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list_activity);
        configList = (ListView) findViewById(R.id.configListView);
        dbHelper = new DatabaseHelper(this);

        loadListData();
        getSupportActionBar().setTitle(R.string.app_name);
    }

    private void loadListData() {

        final Cursor listData = dbHelper.getData();
        final List<LocaleConfig> listConfigs = new ArrayList<>();
        while (listData.moveToNext()) {
            listConfigs.add(new LocaleConfig(
                    listData.getInt(0),
                    listData.getString(1),
                    listData.getDouble(2),
                    listData.getDouble(3),
                    listData.getInt(4),
                    listData.getInt(5),
                    listData.getInt(6),
                    listData.getInt(7),
                    listData.getInt(8)
            ));
        }
        final ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listConfigs);
        configList.setAdapter(adapter);

        configList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                callEditScreen(listConfigs.get(i));
            }
        });
    }

    private void callEditScreen(LocaleConfig lc) {
        Intent intent = new Intent(MainActivity.this, EditConfigActivity.class);
        intent.putExtra("id", lc.getId());
        intent.putExtra("cName", lc.getConfigName());
        intent.putExtra("lat", lc.getLat());
        intent.putExtra("longi", lc.getLongi());
        intent.putExtra("zoom", lc.getZoom());
        intent.putExtra("wifi", lc.getWifi());
        intent.putExtra("media", lc.getMedia());
        intent.putExtra("ring", lc.getRing());
        intent.putExtra("alarm", lc.getAlarm());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addConfig:
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
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