package com.example.ilgarrasulov.watertime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ilgarrasulov on 10.05.2017.
 */

public class DatabaseQuery extends DatabaseObject {
    public DatabaseQuery(Context context) {
        super(context);
    }
    public static final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat sdf_full=new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

    private Calendar calendar=Calendar.getInstance(Locale.ENGLISH);


    public List<CalendarDay> getAllDrinks(Date dateBegin, Date dateEnd) {
        List<CalendarDay> calendarDays=new ArrayList<>();

        String whereString;

        if(dateBegin!=null || dateEnd!=null){


            whereString=new String(" where "+DBSchema.Drinks.DAY+" BETWEEN \""+sdf.format(dateBegin)+"\" AND \""+sdf.format(dateEnd)+"\" ");
        }
        else{
            whereString="";
        }

        String query="select "+DBSchema.Drinks.DAY +", "+DBSchema.Drinks.SET_COUNT +", day_count, "+
            "case when "+DBSchema.Drinks.SET_COUNT+" <= day_count then 'success' else 'failed' end as result"
                +
                " from "+DBSchema.Drinks.NAME
                +
                " left outer join (" +
                "select count(*) as day_count, date("+ DBSchema.DrinkTimes.DATE_TIME+") as "+DBSchema.DrinkTimes.DATE_TIME +
                " from "+DBSchema.DrinkTimes.NAME+
                " group by date("+DBSchema.DrinkTimes.DATE_TIME+")"+
                " ) on "+ DBSchema.Drinks.DAY +" = "+DBSchema.DrinkTimes.DATE_TIME
                +whereString;
        Cursor cursor= getDBConnection().rawQuery(query,null);
        if (cursor.moveToFirst()){
         do{

             calendarDays.add(new CalendarDay(convertStringToDate(cursor.getString(cursor.getColumnIndex(DBSchema.Drinks.DAY)),sdf),cursor.getString(cursor.getColumnIndex("result")),cursor.getInt(cursor.getColumnIndex("day_count")),cursor.getInt(cursor.getColumnIndex(DBSchema.Drinks.SET_COUNT))));
         } while (cursor.moveToNext());
        }
        return calendarDays;
    }

    public static Date convertStringToDate(String dateS,SimpleDateFormat format) {

        Date d = null;
        try {
            d = format.parse(dateS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }
    public static String convertDateToString(Date date,SimpleDateFormat format){
        String dateString= format.format(date);
        return dateString;
    }

    public void registerForToday(){

        Cursor c=this.getDBConnection().query(DBSchema.Drinks.NAME,null,"date("+ DBSchema.Drinks.DAY+") = ?",new String[]{convertDateToString(calendar.getTime(),sdf)},null,null,null);

        if(!c.moveToFirst()){
           insertday(convertDateToString(calendar.getTime(),sdf));
        }

    }

    public void delete(String tableName,String whereClause){
        this.getDBConnection().delete(tableName,whereClause,null);
    }

    public void insertday(String date){
        ContentValues cv= new ContentValues();
        cv.put(DBSchema.Drinks.DAY,date);
        cv.put(DBSchema.Drinks.SET_COUNT,8);

        this.getDBConnection().insert(DBSchema.Drinks.NAME,null,cv);
    }

    public long insertdayDrink(){
        ContentValues cv= new ContentValues();
        cv.put(DBSchema.DrinkTimes.DATE_TIME,convertDateToString(calendar.getTime(),sdf_full));

        return this.getDBConnection().insert(DBSchema.DrinkTimes.NAME,null,cv);

    }
    public void undoInsertDayDrink(Long id){

                delete(DBSchema.DrinkTimes.NAME,"_id = "+id);
            }
}
