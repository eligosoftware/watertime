package com.example.ilgarrasulov.watertime;

import android.app.Application;

/**
 * Created by mragl on 27.05.2017.
 */

public class MainApplication extends Application {

    public void rescheduleTime() {
        boolean isOn= WaterTimeService.isServiceAlarmOn(this);
        WaterTimeService.setServiceAlarm(this,false);
        WaterTimeService.setServiceAlarm(this,isOn);
    }
}
