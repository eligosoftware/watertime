package com.example.ilgarrasulov.watertime;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by ilgarrasulov on 04.05.2017.
 */

public class DrinkFragment extends Fragment {
   private DatabaseQuery databaseQuery;
    private Calendar cal= Calendar.getInstance(Locale.ENGLISH);
    private TextView today_stats,next_drink;
    private CardView wellDone;
    private CardView details_group;
    private LinearLayout details_group_layout;
    private CountDownTimer countDownTimer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


//        DatabaseQuery q=new DatabaseQuery(getActivity());
//        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
//        Date d=new Date();
//       q.insertday(df.format(d));
////        q.delete();
//        q.getAllDrinks();

        // List<Date> dayValueInCells=new ArrayList<Date>();
//        Calendar mCal=(Calendar)cal.clone();
//        mCal.set(Calendar.DAY_OF_MONTH,1);

        databaseQuery=new DatabaseQuery(getActivity());

        View v =inflater.inflate(R.layout.drink_tab,container,false);

        today_stats=(TextView)v.findViewById(R.id.drink_tab_card_view_drank_today_text_view_today_stats);

        next_drink=(TextView)v.findViewById(R.id.drink_tab_card_view_drank_today_text_view_next_drink_value);

        wellDone=(CardView)v.findViewById(R.id.drink_tab_card_view_well_done);

        //updateTodayStats();


        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<DrinkGlass> drinkGlasses=databaseQuery.getDrinkGlasses(cal.getTime());
                if(drinkGlasses.size()>0) {
                    startActivity(DrinksListActivity.newInstance(drinkGlasses, getActivity()));
                }
                else {
                    Toast.makeText(getActivity(), R.string.no_drinks_today,Toast.LENGTH_SHORT).show();
                }

              //  Toast.makeText(getActivity(),"Wow!",Toast.LENGTH_SHORT).show();
            }
        };

        details_group=(CardView)v.findViewById(R.id.drink_tab_card_view_drank_today);
        details_group.setOnClickListener(listener);

        details_group_layout=(LinearLayout)v.findViewById(R.id.details_group);
        details_group_layout.setOnClickListener(listener);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Long id=databaseQuery.insertdayDrink();
                updateTodayStats();

                Snackbar.make(view, "1 glass added", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                   databaseQuery.undoInsertDayDrink(id);
                                   updateTodayStats();
                            }
                        }).show();
            }
        });
        return v ;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(countDownTimer!=null){
            countDownTimer.cancel();
        }
    }

    private void updateTodayStats(){

        Date today_start =DatabaseQuery.convertStringToDate(DatabaseQuery.convertDateToString(cal.getTime(),DatabaseQuery.sdf)+" 00:00:00",DatabaseQuery.sdf_full);
        Date today_end =DatabaseQuery.convertStringToDate(DatabaseQuery.convertDateToString(cal.getTime(),DatabaseQuery.sdf)+" 23:59:59",DatabaseQuery.sdf_full);

        List<CalendarDay> calendarDays =databaseQuery.getAllDrinks(today_start,today_end);

        if (calendarDays.size()>0){
            CalendarDay day= calendarDays.get(0);
            today_stats.setText(day.getDay_count() + " of "+ day.getSet_count()+" glasses");
            if(day.getDay_count()>=day.getSet_count()){
                wellDone.setVisibility(View.VISIBLE);
            }
            else
            {
                wellDone.setVisibility(View.GONE);
            }
        }

        if(countDownTimer!=null){
            countDownTimer.cancel();
        }

//        boolean isOn=WaterTimeService.isServiceAlarmOn(getActivity());
//        WaterTimeService.setServiceAlarm(getActivity(),false);
//        WaterTimeService.setServiceAlarm(getActivity(),isOn);

        SharedPreferences preferences=getActivity().getSharedPreferences(getActivity().getPackageName()+"_preferences", Context.MODE_PRIVATE);

        String next=preferences.getString("next_notif_time",null);



        if(next!=null && WaterTimeService.isServiceAlarmOn(getActivity())){
            Calendar cal2= (Calendar) cal.clone();

            cal2.setTime(DatabaseQuery.convertStringToDate(next,DatabaseQuery.sdf_full));

            long diff=cal2.getTime().getTime()-cal.getTime().getTime();

            countDownTimer=new CountDownTimer(diff,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    String hours= (int) (millisUntilFinished/(1000*60*60)) < 10 ? "0"+(int) (millisUntilFinished/(1000*60*60)) : String.valueOf((int) (millisUntilFinished/(1000*60*60)));
                    String minutes= (int) (millisUntilFinished/(1000*60))%60 < 10 ? "0"+(int) (millisUntilFinished/(1000*60))%60: String.valueOf((int) (millisUntilFinished/(1000*60))%60);
                    String seconds= (int) (millisUntilFinished/(1000))%60 <10 ? "0"+(int) (millisUntilFinished/(1000))%60: String.valueOf((int) (millisUntilFinished/(1000))%60);

                    next_drink.setText(hours+":"+minutes+":"+seconds);
                }

                @Override
                public void onFinish() {
                    ((MainActivity)getActivity()).instantiateUI();
                }
            };
            countDownTimer.start();
        }
        else {
            next_drink.setText("");
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        updateTodayStats();
    }
}
