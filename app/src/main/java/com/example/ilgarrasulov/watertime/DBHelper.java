package com.example.ilgarrasulov.watertime;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ilgarrasulov on 10.05.2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DATABASE_NAME = "drinksBase.db";


    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+DBSchema.Drinks.NAME+"("+
        " _id integer primary key autoincrement, "+
                    DBSchema.Drinks.DAY+" integer,"+
                        DBSchema.Drinks.SET_COUNT+" integer)"
        );
        db.execSQL("create table "+DBSchema.DrinkTimes.NAME+"("+
                " _id integer primary key autoincrement, "+
                DBSchema.DrinkTimes.DATE_TIME+" integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
