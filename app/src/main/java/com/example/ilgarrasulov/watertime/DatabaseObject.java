package com.example.ilgarrasulov.watertime;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ilgarrasulov on 10.05.2017.
 */

public class DatabaseObject {

    private static DBHelper db_helper;
    private SQLiteDatabase db;

    public DatabaseObject(Context context)
    {
        db_helper=new DBHelper(context);
        this.db_helper.getWritableDatabase();
        this.db=db_helper.getReadableDatabase();
    }

    public SQLiteDatabase getDBConnection(){
        return this.db;
    }
    public void closeDbConnection(){
        if(this.db!=null){
            this.db.close();
        }
    }
}
