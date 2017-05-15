package com.example.ilgarrasulov.watertime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ilgarrasulov on 15.05.2017.
 */

public class DrinksListActivity extends AppCompatActivity {

   public static Intent newInstance(List<DrinkGlass> drinkGlasses, Context context){
       Intent i=new Intent(context,DrinksListActivity.class);
       i.putExtra("details",(Serializable)drinkGlasses);
       return i;
   }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.fragment_container);

        if(fragment==null){
            fragment=new DrinksListFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container,fragment)
                    .commit();
        }
    }
}
