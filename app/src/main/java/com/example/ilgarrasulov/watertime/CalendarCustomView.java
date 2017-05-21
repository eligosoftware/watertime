package com.example.ilgarrasulov.watertime;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ilgarrasulov on 08.05.2017.
 */

public class CalendarCustomView extends LinearLayout{
    private static final String TAG= CalendarCustomView.class.getSimpleName();
    private ImageView previousButton,nextButton;
    private TextView currentDate;
    private GridView calendarGridView;
    private static final int MAX_CALENDAR_COUNT = 42;
    private Context context;
    private Calendar cal= Calendar.getInstance(Locale.ENGLISH);
    private DatabaseQuery mQuery;
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private GridAdapter mAdapter;

    public void setCallback(CalendarClick callback) {
        mCallback = callback;
    }

    private CalendarClick mCallback;



    public CalendarCustomView(Context context) {
        super(context);
    }

    public CalendarCustomView(Context context, @Nullable AttributeSet attrs) {


        super(context, attrs);
        this.context=context;
        initializeUILayout();
        setUpCalendarAdapter(null);
        setPreviousButtonClickEvent();
        setNextButtonClickEvent();
        setGridCellClickEvents();
    }


    public CalendarCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void initializeUILayout(){
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.calendar_layout,this);
        previousButton=(ImageView)view.findViewById(R.id.previous_month);
        nextButton=(ImageView)view.findViewById(R.id.next_month);
        currentDate=(TextView)view.findViewById(R.id.display_current_date);
        calendarGridView=(GridView)view.findViewById(R.id.calendar_grid);
    }


    private void setPreviousButtonClickEvent(){
        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH,-1);
                setUpCalendarAdapter(null);
            }
        });
    }
    private void setNextButtonClickEvent(){
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH,1);
                setUpCalendarAdapter(null);
            }
        });
    }

    private void setUpCalendarAdapter(Calendar tapped_date){
        List<Date> dayValueInCells=new ArrayList<Date>();
        Calendar mCal=(Calendar)cal.clone();
        mCal.set(Calendar.DAY_OF_MONTH,1);

        int firstDayOfTheMonth=mCal.get(Calendar.DAY_OF_WEEK)-2;
        if(firstDayOfTheMonth==-1)
        {
            firstDayOfTheMonth+=7;
        }
        mCal.add(Calendar.DAY_OF_MONTH,-firstDayOfTheMonth);
        Date first=mCal.getTime();

        while (dayValueInCells.size()<MAX_CALENDAR_COUNT){
            dayValueInCells.add(mCal.getTime());
            mCal.add(Calendar.DAY_OF_MONTH,1);
        }
        mQuery=new DatabaseQuery(context);
        List<CalendarDay> calendarDays=mQuery.getAllDrinks(first,dayValueInCells.get(dayValueInCells.size()-1));

        String sDate = formatter.format(cal.getTime());
        currentDate.setText(sDate);
        mAdapter=new GridAdapter(context,dayValueInCells,cal,calendarDays);

        if(tapped_date!=null){
            mAdapter.setTapped_position(tapped_date,false);
        }

        calendarGridView.setAdapter(mAdapter);




    }
    private void setGridCellClickEvents(){
        calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Calendar cal1= Calendar.getInstance();
                cal1.setTime((Date)calendarGridView.getAdapter().getItem(position));


                if (cal.get(Calendar.YEAR) == cal1.get(Calendar.YEAR)) {
                    if (cal.get(Calendar.MONTH) < cal1.get(Calendar.MONTH)) {
                        cal.add(Calendar.MONTH, 1);
                        setUpCalendarAdapter(cal1);
                    } else if (cal.get(Calendar.MONTH) > cal1.get(Calendar.MONTH)) {
                        cal.add(Calendar.MONTH, -1);
                        setUpCalendarAdapter(cal1);
                    }

                    else {
                        ((GridAdapter) calendarGridView.getAdapter()).setTapped_position(cal1,true);
                        calendarGridView.invalidate();
                        //Toast.makeText(context,"Clicked "+calendarGridView.getAdapter().getItem(position).toString(),Toast.LENGTH_LONG).show();
                    }

                }
                    else if (cal.get(Calendar.YEAR) < cal1.get(Calendar.YEAR)) {
                        cal.add(Calendar.MONTH, 1);
                        setUpCalendarAdapter(cal1);
                    } else {
                        cal.add(Calendar.MONTH, -1);
                        setUpCalendarAdapter(cal1);
                    }

                    mCallback.onDayClicked(cal1);

                }


        });

    }
    public void updateData(){
        setUpCalendarAdapter(null);
    }
}
