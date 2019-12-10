package com.example.testemapproject;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;

import com.example.testemapproject.Model.LocaleConfig;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


public class KeepAliveService extends Service {

    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "Channel_Id";


    /* Database Helper*/
    DatabaseHelper dbHelper;

    /* System Managers */
    private WifiManager mWifiManager;
    private AudioManager mAudioManager;

    /* Data List */
    private List<LocaleConfig> listData;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Test tst = new Test(startId);
        tst.start();

        /* Database Helper*/
        dbHelper = new DatabaseHelper(this);
        /* System Managers */
        mWifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

       listData = dbHelper.getData();
        /*final List<LocaleConfig> listConfigs = new ArrayList<>();
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
           }*/

        int media = 5, ring = 5, alarm = 5;


        startForeground();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startForeground() {
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        startForeground(NOTIF_ID, new NotificationCompat.Builder(this,
                NOTIF_CHANNEL_ID) // don't forget create a notification channel first
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("App rodando em plano de fundo!")
                .setContentIntent(pendingIntent)
                .build());
    }

    class Test extends Thread {
        public int startId;
        public boolean status = true;

        public Test(int startId){
            this.startId = startId;
        }

        public void run(){
            while(status){
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                listData = dbHelper.getData();
                Location lctn = MapsActivity.getCurrentLocation();
                if(lctn != null){
                    System.out.println("TESTE");
                    System.out.println("Location"+lctn.getLatitude());
                    if (listData.size() != 0){
                        System.out.println(listData.size());
                        for (LocaleConfig lc : listData) {
                            CircleOptions circleOptions = new CircleOptions()
                                    .center(new LatLng(lc.getLat(), lc.getLongi()))
                                    .radius(50)
                                    .strokeWidth(3f);
                            float[] distance = new float[2];
                            Location.distanceBetween(lctn.getLatitude(), lctn.getLongitude(), circleOptions.getCenter().latitude, circleOptions.getCenter().longitude, distance);
                            if(distance[0] < circleOptions.getRadius()){
                                mAudioManager.setStreamVolume(mAudioManager.STREAM_MUSIC, (int) lc.getMedia(), mAudioManager.FLAG_VIBRATE);
                                mAudioManager.setStreamVolume(mAudioManager.STREAM_ALARM, (int) lc.getAlarm(), mAudioManager.FLAG_VIBRATE);
                                mAudioManager.setStreamVolume(mAudioManager.STREAM_RING, (int) lc.getRing(), mAudioManager.FLAG_VIBRATE);
                                mWifiManager.setWifiEnabled(lc.getWifi() == 1 ? true : false);
                            }
                        }
                    }
                }
            }
        }
    }
}