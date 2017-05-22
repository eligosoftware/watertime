package com.example.ilgarrasulov.watertime;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.app.FragmentManager;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

/**
 * Created by mragl on 22.05.2017.
 */

public class PrefActivity extends PreferenceActivity {
    PrefFragment mPrefFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm=getFragmentManager();
        mPrefFragment= new PrefFragment();
        SharedPreferences sharedPreferences=getSharedPreferences("drinks_per_day",MODE_PRIVATE);
        mPrefFragment.setUpSummary( sharedPreferences.getString("drinks_per_day",""));

        fm.beginTransaction()
                .replace(android.R.id.content,mPrefFragment)
                .commit();

    }
//    SharedPreferences.OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener =new SharedPreferences.OnSharedPreferenceChangeListener() {
//        @Override
//        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
//            if (sharedPreferences==getSharedPreferences("drinks_per_day",MODE_PRIVATE)){
//                mPrefFragment.setUpSummary(s);
//            }
//        }
//    }


    public static class PrefFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

        }
        public void setUpSummary(String value){
            Preference preference= findPreference("drinks_per_day");
            preference.setSummary(" . Current = "+value);
        }
    }
}
