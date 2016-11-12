package com.zok.art.notificationdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private NotificationManager mManager;
    private NotificationCompat.Builder mBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        findViewById(R.id.btn_show).setOnClickListener(this);
        findViewById(R.id.btn_hide).setOnClickListener(this);
        findViewById(R.id.btn_add).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show:
                createNotify();
                break;
            case R.id.btn_hide:
                hideNotify();
                break;
            case R.id.btn_add:
                updateProgress();
        }

    }

    private void createNotify() {
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Title")
                .setContentText("Text")
                .setProgress(100, 0, false)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        mManager.notify(0, mBuilder.build());
    }

    private void hideNotify() {
        mManager.cancel(0);
    }

    private int mProgress;

    private void updateProgress() {
        if(mProgress == 100) mProgress = 0;
        mBuilder.setProgress(100, mProgress+=10, false);
        mManager.notify(0, mBuilder.build());
    }
}
