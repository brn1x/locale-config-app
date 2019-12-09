package com.example.testemapproject;

        import android.app.Application;
        import android.content.Intent;

public class ApplicationService extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(this, KeepAliveService.class));
    }
}
