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
    public static final SimpleDateFormat sdf_today=new SimpleDateFormat("kk:mm");

    private Calendar calendar=Calendar.getInstance(Locale.ENGLISH);


    public List<DrinkGlass> getDrinkGlasses(Date date){
        List<DrinkGlass> drinkGlasses=new ArrayList<>();
        String dateString=sdf.format(date);

        String query="select "+DBSchema.DrinkTimes.DATE_TIME +", _id"+

                " from "+DBSchema.DrinkTimes.NAME
                +" where date("+DBSchema.DrinkTimes.DATE_TIME+") = '"+dateString+"'"
                ;
        Cursor cursor= getDBConnection().rawQuery(query,null);
        if (cursor.moveToFirst()){
            do{
                drinkGlasses.add(new DrinkGlass(cursor.getLong(cursor.getColumnIndex("_id")),convertStringToDate(cursor.getString(cursor.getColumnIndex(DBSchema.DrinkTimes.DATE_TIME)),sdf_full)));
            } while (cursor.moveToNext());
        }
        return drinkGlasses;
    }


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

    public int getStreak(String type){

//        delete(DBSchema.DrinkTimes.NAME,null);
//        delete(DBSchema.Drinks.NAME,null);
//        test_data();
        this.getDBConnection().execSQL("drop table if exists test");
        String temp_db_query="create temporary table test as select "+DBSchema.Drinks.DAY+" as dt from (select "+DBSchema.Drinks.DAY +", "+DBSchema.Drinks.SET_COUNT +", day_count, "+
                "case when "+DBSchema.Drinks.SET_COUNT+" <= day_count then 'success' else 'failed' end as result"
                +
                " from "+DBSchema.Drinks.NAME
                +
                " left outer join (" +
                "select count(*) as day_count, date("+ DBSchema.DrinkTimes.DATE_TIME+") as "+DBSchema.DrinkTimes.DATE_TIME +
                " from "+DBSchema.DrinkTimes.NAME+
                " group by date("+DBSchema.DrinkTimes.DATE_TIME+")"+
                " ) on "+ DBSchema.Drinks.DAY +" = "+DBSchema.DrinkTimes.DATE_TIME+") where result ='success' order by "+DBSchema.Drinks.DAY
                +"";

//        String query=
//                "select * from test";
        String query="select max(dt),count(*) as streak from(" +
                " select dt," +
                " date(dt, -(select count(*) from test t2 where t2.dt<=t1.dt) ||' day') as grp" +
                " from test AS t1)" +
                " group by grp";
//
        this.getDBConnection().execSQL(temp_db_query);

        switch (type){
            case "max":
                query+=" order by streak DESC";

                break;
            case "cur":
                Calendar calendar1= (Calendar) calendar.clone();
                calendar1.add(Calendar.DAY_OF_MONTH,-1);

                query+=" having max(dt)= '"+convertDateToString(calendar1.getTime(),sdf)+"' or '"+ convertDateToString(calendar.getTime(),sdf) +"' order by streak DESC";
                break;
            case "month":

            default:
                break;

        }

        Cursor c= this.getDBConnection().rawQuery(query,null);
        if(c.moveToFirst()){
            return c.getInt(1);
        }

        return 0;
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

    private long insertdayDrink_test(String date){
        ContentValues cv= new ContentValues();
        cv.put(DBSchema.DrinkTimes.DATE_TIME,date);

        return this.getDBConnection().insert(DBSchema.DrinkTimes.NAME,null,cv);

    }

    private void test_data(){
        String day1="2017-01-03";
        insertday(day1);

        String day11="2017-01-03 10:05:16";
        insertdayDrink_test(day11);
        String day12="2017-01-03 10:25:16";
        insertdayDrink_test(day12);
        String day13="2017-01-03 10:35:16";
        insertdayDrink_test(day13);
        String day14="2017-01-03 10:45:16";
        insertdayDrink_test(day14);
        String day15="2017-01-03 10:55:16";
        insertdayDrink_test(day15);
        String day16="2017-01-03 11:05:16";
        insertdayDrink_test(day16);
        String day17="2017-01-03 11:15:16";
        insertdayDrink_test(day17);
        String day18="2017-01-03 12:05:16";
        insertdayDrink_test(day18);


        String day2="2017-01-04";
        insertday(day2);

        String day21="2017-01-04 10:05:16";
        insertdayDrink_test(day21);
        String day22="2017-01-04 10:25:16";
        insertdayDrink_test(day22);
        String day23="2017-01-04 10:35:16";
        insertdayDrink_test(day23);
        String day24="2017-01-04 10:45:16";
        insertdayDrink_test(day24);
        String day25="2017-01-04 10:55:16";
        insertdayDrink_test(day25);
        String day26="2017-01-04 11:05:16";
        insertdayDrink_test(day26);
        String day27="2017-01-04 11:15:16";
        insertdayDrink_test(day27);
        String day28="2017-01-04 12:05:16";
        insertdayDrink_test(day28);

        String day3="2017-01-05";
        insertday(day3);

        String day31="2017-01-05 10:05:16";
        insertdayDrink_test(day31);
        String day32="2017-01-05 10:25:16";
        insertdayDrink_test(day32);
        String day33="2017-01-05 10:35:16";
        insertdayDrink_test(day33);
        String day34="2017-01-05 10:45:16";
        insertdayDrink_test(day34);
        String day35="2017-01-05 10:55:16";
        insertdayDrink_test(day35);
        String day36="2017-01-05 11:05:16";
        insertdayDrink_test(day36);
        String day37="2017-01-05 11:15:16";
        insertdayDrink_test(day37);
        String day38="2017-01-05 12:05:16";
        insertdayDrink_test(day38);

        String day4="2017-01-06";  // 4 days
        insertday(day4);

        String day41="2017-01-06 10:05:16";
        insertdayDrink_test(day41);
        String day42="2017-01-06 10:25:16";
        insertdayDrink_test(day42);
        String day43="2017-01-06 10:35:16";
        insertdayDrink_test(day43);
        String day44="2017-01-06 10:45:16";
        insertdayDrink_test(day44);
        String day45="2017-01-06 10:55:16";
        insertdayDrink_test(day45);
        String day46="2017-01-06 11:05:16";
        insertdayDrink_test(day46);
        String day47="2017-01-06 11:15:16";
        insertdayDrink_test(day47);
        String day48="2017-01-06 12:05:16";
        insertdayDrink_test(day48);

        String day5="2017-01-09";
        insertday(day5);

        String day51="2017-01-09 10:05:16";
        insertdayDrink_test(day51);
        String day52="2017-01-09 10:25:16";
        insertdayDrink_test(day52);
        String day53="2017-01-09 10:35:16";
        insertdayDrink_test(day53);
        String day54="2017-01-09 10:45:16";
        insertdayDrink_test(day54);
        String day55="2017-01-09 10:55:16";
        insertdayDrink_test(day55);
        String day56="2017-01-09 11:05:16";
        insertdayDrink_test(day56);
        String day57="2017-01-09 11:15:16";
        insertdayDrink_test(day57);
        String day58="2017-01-09 12:05:16";
        insertdayDrink_test(day58);

        String day6="2017-01-10"; //2 days
        insertday(day6);

        String day61="2017-01-10 10:05:16";
        insertdayDrink_test(day61);
        String day62="2017-01-10 10:25:16";
        insertdayDrink_test(day62);
        String day63="2017-01-10 10:35:16";
        insertdayDrink_test(day63);
        String day64="2017-01-10 10:45:16";
        insertdayDrink_test(day64);
        String day65="2017-01-10 10:55:16";
        insertdayDrink_test(day65);
        String day66="2017-01-10 11:05:16";
        insertdayDrink_test(day66);
        String day67="2017-01-10 11:15:16";
        insertdayDrink_test(day67);
        String day68="2017-01-10 12:05:16";
        insertdayDrink_test(day68);

        String day7="2017-01-20"; //1 day
        insertday(day7);

        String day71="2017-01-20 10:05:16";
        insertdayDrink_test(day71);
        String day72="2017-01-20 10:25:16";
        insertdayDrink_test(day72);
        String day73="2017-01-20 10:35:16";
        insertdayDrink_test(day73);
        String day74="2017-01-20 10:45:16";
        insertdayDrink_test(day74);
        String day75="2017-01-20 10:55:16";
        insertdayDrink_test(day75);
        String day76="2017-01-20 11:05:16";
        insertdayDrink_test(day76);
        String day77="2017-01-20 11:15:16";
        insertdayDrink_test(day77);
        String day78="2017-01-20 12:05:16";
        insertdayDrink_test(day78);

        String day8="2017-05-17";
        insertday(day8);

        String day81="2017-05-17 10:05:16";
        insertdayDrink_test(day81);
        String day82="2017-05-17 10:25:16";
        insertdayDrink_test(day82);
        String day83="2017-05-17 10:35:16";
        insertdayDrink_test(day83);
        String day84="2017-05-17 10:45:16";
        insertdayDrink_test(day84);
        String day85="2017-05-17 10:55:16";
        insertdayDrink_test(day85);
        String day86="2017-05-17 11:05:16";
        insertdayDrink_test(day86);
        String day87="2017-05-17 11:15:16";
        insertdayDrink_test(day87);
        String day88="2017-05-17 12:05:16";
        insertdayDrink_test(day88);

        String day9="2017-05-18";
        insertday(day9);

        String day91="2017-05-18 10:05:16";
        insertdayDrink_test(day91);
        String day92="2017-05-18 10:25:16";
        insertdayDrink_test(day92);
        String day93="2017-05-18 10:35:16";
        insertdayDrink_test(day93);
        String day94="2017-05-18 10:45:16";
        insertdayDrink_test(day94);
        String day95="2017-05-18 10:55:16";
        insertdayDrink_test(day95);
        String day96="2017-05-18 11:05:16";
        insertdayDrink_test(day96);
        String day97="2017-05-18 11:15:16";
        insertdayDrink_test(day97);
        String day98="2017-05-18 12:05:16";
        insertdayDrink_test(day98);

        String day10="2017-05-19";
        insertday(day10);

        String day101="2017-05-19 10:05:16";
        insertdayDrink_test(day101);
        String day102="2017-05-19 10:25:16";
        insertdayDrink_test(day102);
        String day103="2017-05-19 10:35:16";
        insertdayDrink_test(day103);
        String day104="2017-05-19 10:45:16";
        insertdayDrink_test(day104);
        String day105="2017-05-19 10:55:16";
        insertdayDrink_test(day105);
        String day106="2017-05-19 11:05:16";
        insertdayDrink_test(day106);
        String day107="2017-05-19 11:15:16";
        insertdayDrink_test(day107);
        String day108="2017-05-19 12:05:16";
        insertdayDrink_test(day108);

        String day11_="2017-05-20";
        insertday(day11_);

        String day11_1="2017-05-20 10:05:16";
        insertdayDrink_test(day11_1);
        String day11_2="2017-05-20 10:25:16";
        insertdayDrink_test(day11_2);
        String day11_3="2017-05-20 10:35:16";
        insertdayDrink_test(day11_3);
        String day11_4="2017-05-20 10:45:16";
        insertdayDrink_test(day11_4);
        String day11_5="2017-05-20 10:55:16";
        insertdayDrink_test(day11_5);
        String day11_6="2017-05-20 11:05:16";
        insertdayDrink_test(day11_6);
        String day11_7="2017-05-20 11:15:16";
        insertdayDrink_test(day11_7);
        String day11_8="2017-05-20 12:05:16";
        insertdayDrink_test(day11_8);

    }

    public void undoInsertDayDrink(Long id){

                delete(DBSchema.DrinkTimes.NAME,"_id = "+id);
            }
}
