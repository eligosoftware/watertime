package com.example.ilgarrasulov.watertime;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by ilgarrasulov on 04.05.2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        mNumOfTabs=NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                DrinkFragment drinkFragment=new DrinkFragment();
                return drinkFragment;

            case 1:
                CalendarFragment calendarFragment = new CalendarFragment();
                return calendarFragment;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
