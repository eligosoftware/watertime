package com.example.ilgarrasulov.watertime;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static Intent newIntent(Context context){
        return new Intent(context,MainActivity.class);
    }



    public void instantiateUI(){
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("DRINK"));
        tabLayout.addTab(tabLayout.newTab().setText("CALENDAR"));
        //  tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);


        final ViewPager viewPager = (ViewPager) findViewById(R.id.tab_pager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());


        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1) {
                    ((CalendarFragment)getSupportFragmentManager().getFragments().get(2)).onDayClicked(Calendar.getInstance(Locale.ENGLISH));
                    ((CalendarCustomView)getSupportFragmentManager().getFragments().get(2).getView().findViewById(R.id.custom_calendar)).updateData();
                    ((CalendarFragment)getSupportFragmentManager().getFragments().get(2)).updateMaxRecords();

                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        instantiateUI();

        DatabaseQuery dq = new DatabaseQuery(this);


        SharedPreferences prefs= getSharedPreferences(getPackageName()+"_preferences",MODE_PRIVATE);

        if(!dq.registeredForToday(this)) {
            dq.registerForToday(this);
            WaterTimeService.setServiceAlarm(this, prefs.getBoolean("enable_notifications", false));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(),PrefActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
