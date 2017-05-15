package com.example.ilgarrasulov.watertime;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ilgarrasulov on 12.05.2017.
 */
public class GridAdapter extends ArrayAdapter {
    public void setTapped_position(Integer tapped_position) {
        this.tapped_position = tapped_position;
        this.notifyDataSetChanged();
    }

    private Integer tapped_position;
    private List<Date> monthlyDates;
    private Calendar currentDate;
    private List<CalendarDay> calendarDays;
    private LayoutInflater mInflater;
    public GridAdapter(Context context, List<Date> monthlyDates, Calendar currentDate, List<CalendarDay> calendarDays) {
        super(context, R.layout.single_cell_layout);

        this.monthlyDates=monthlyDates;
        this.currentDate=currentDate;
        this.calendarDays=calendarDays;

        mInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return monthlyDates.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return monthlyDates.get(position);
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return monthlyDates.indexOf(item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Date mDate = monthlyDates.get(position);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(mDate);
        int dayValue = dateCal.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCal.get(Calendar.MONTH) + 1;
        int displayYear = dateCal.get(Calendar.YEAR);

        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);


        Calendar current =Calendar.getInstance(Locale.ENGLISH);

        int currentMonth1 = current.get(Calendar.MONTH) + 1;
        int currentYear1 = current.get(Calendar.YEAR);
        int currentDay1 = current.get(Calendar.DAY_OF_MONTH);


        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.single_cell_layout, parent, false);
        }

        if (displayMonth == currentMonth && displayYear == currentYear) {
            view.setBackground(getContext().getResources().getDrawable(R.drawable.in_month));
        } else {
            view.setBackground(getContext().getResources().getDrawable(R.drawable.outside_month));
        }
        if(currentDay1==dayValue && currentMonth1==displayMonth && currentYear1==displayYear) {
            view.setBackground(getContext().getResources().getDrawable(R.drawable.red_border));

        }

        if(tapped_position!=null && tapped_position==position){
            if (displayMonth == currentMonth && displayYear == currentYear) {
            view.setBackground(getContext().getResources().getDrawable(R.drawable.green_border_in_month));
            }else{
                view.setBackground(getContext().getResources().getDrawable(R.drawable.green_border_outside_month));
            }
        }

        TextView cellNumber=(TextView)view.findViewById(R.id.calendar_date_id);
        cellNumber.setText(String.valueOf(dayValue));

        ImageView eventIndicator=(ImageView)view.findViewById(R.id.event_id);

        Calendar eventCalendar=Calendar.getInstance();

        for(int i=0;i<calendarDays.size();i++){
            eventCalendar.setTime(calendarDays.get(i).getDate());
            if(dayValue==eventCalendar.get(Calendar.DAY_OF_MONTH) &&
                    displayMonth==eventCalendar.get(Calendar.MONTH)+1 &&
                    displayYear==eventCalendar.get(Calendar.YEAR)
                    &&(calendarDays.get(i).getDay_count()>=calendarDays.get(i).getSet_count())
                    ){
                eventIndicator.setVisibility(View.VISIBLE);//setBackgroundColor(Color.parseColor("#FF4081"));
            }
        }



        return view;
    }
}
