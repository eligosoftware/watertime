package com.example.ilgarrasulov.watertime;

import java.util.Date;

/**
 * Created by ilgarrasulov on 12.05.2017.
 */

public class CalendarDay {

    public CalendarDay(Date day,String result,int day_count,int set_count){
        this.date=day;
        this.success= result=="success"? true: false;
        this.day_count=day_count;
        this.set_count=set_count;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    private Date date;
    private boolean success;

    public int getDay_count() {
        return day_count;
    }

    public void setDay_count(int day_count) {
        this.day_count = day_count;
    }

    public int getSet_count() {
        return set_count;
    }

    public void setSet_count(int set_count) {
        this.set_count = set_count;
    }

    private int day_count,set_count;


}
