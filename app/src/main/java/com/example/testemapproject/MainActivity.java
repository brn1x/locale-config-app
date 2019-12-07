package com.example.testemapproject;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.testemapproject.Model.LocaleConfig;

import java.lang.reflect.Array;
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
        configList = (ListView)findViewById(R.id.configListView);
        dbHelper = new DatabaseHelper(this);

        loadListData();
    }

    private void loadListData(){

        Cursor listData = dbHelper.getData();
        List<LocaleConfig> listConfigs = new ArrayList<>();
        while (listData.moveToNext()){
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
        final ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listConfigs)
        configList.setAdapter(adapter);

        configList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //String name = adapterView.get
            }
        });
    }
}