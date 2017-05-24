package com.example.ilgarrasulov.watertime;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

/**
 * Created by ilgarrasulov on 24.05.2017.
 */

public class WaterTimeService extends IntentService {
    private static final String TAG = "WaterTimeService";


    public WaterTimeService() {
        super(TAG);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, WaterTimeService.class);
    }

    public static void setServiceAlarm(Context context, boolean isOn){

        Intent i = WaterTimeService.newIntent(context);
        PendingIntent pi=PendingIntent.getService(context,0,i,0);
        AlarmManager alarmManager=(AlarmManager)context.getSystemService(ALARM_SERVICE);

        if(isOn){
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime()+1*60*1000,1*60*1000,pi);
        } else{
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }
    public static boolean isServiceAlarmOn(Context context){
        Intent i =WaterTimeService.newIntent(context);
        PendingIntent pi=PendingIntent.getService(context,0,i,PendingIntent.FLAG_NO_CREATE);
        return pi!=null;
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Intent i=MainActivity.newIntent(this);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);


        Uri alarm_sount = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification=new NotificationCompat.Builder(this)
                .setTicker("Time for water!")
                .setSmallIcon(R.mipmap.ic_glass_of_water)
                .setContentTitle("Time for water!")
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setLights(Color.GREEN,500,500)
                .build();

        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(0,notification);



    }
}
