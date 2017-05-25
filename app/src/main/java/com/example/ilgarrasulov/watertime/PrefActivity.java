package com.example.ilgarrasulov.watertime;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.app.FragmentManager;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;

/**
 * Created by mragl on 22.05.2017.
 */

public class PrefActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.setDefaultValues(PrefActivity.this,R.xml.preferences,false);
        initSummary(getPreferenceScreen());
    }

    private void initSummary(Preference p) {
        if (p instanceof PreferenceGroup) {
            PreferenceGroup pGrp = (PreferenceGroup) p;
            for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
                initSummary(pGrp.getPreference(i));
            }
        } else {
            updatePrefSummary(p);
        }
    }

    private void updatePrefSummary(Preference p) {
        if (p instanceof ListPreference) {
            ListPreference listPref = (ListPreference) p;
            p.setSummary(listPref.getEntry());
        }

        if (p instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p;
            if (p.getTitle().toString().toLowerCase().contains("password"))
            {
                p.setSummary("******");
            } else {
                p.setSummary(getString(R.string.drinks_per_day_description,editTextPref.getText()));
            }
            if(p.getKey().equals("drinks_per_day")) {
                DatabaseQuery dbquery = new DatabaseQuery(getApplicationContext());
                dbquery.registerForToday(getApplicationContext());

                SharedPreferences preferences = getSharedPreferences(getPackageName()+"_preferences",MODE_PRIVATE);
                WaterTimeService.setServiceAlarm(getApplicationContext(),false);
                WaterTimeService.setServiceAlarm(getApplicationContext(),preferences.getBoolean("enable_notifications",false));
            }
        }
        if (p instanceof MultiSelectListPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p;
            p.setSummary(editTextPref.getText());
        }
        if(p instanceof SwitchPreference){
            WaterTimeService.setServiceAlarm(getApplicationContext(),((SwitchPreference) p).isChecked());
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePrefSummary(findPreference(key));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
