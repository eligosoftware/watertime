package com.example.ilgarrasulov.watertime;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        DatabaseQuery dq = new DatabaseQuery(this);
        dq.registerForToday();

    }
//        String[] permissions = new String[]{
//        Manifest.permission.READ_CALENDAR,
//                Manifest.permission.WRITE_CALENDAR};
//        ArrayList<String> askPermissions=new ArrayList<String>();
//
//        for(String p : permissions) {
//            if (ActivityCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
//                askPermissions.add(p);
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//            }
//        }
//
//        if(askPermissions.size()>0){
//            ActivityCompat.requestPermissions(this
//                    , askPermissions.toArray(new String[0]), READ_CALENDAR_PERMISIION);
//
//        }
//        else {
//            checkIt();
//        }


//    private void checkIt(){
//        Cursor cur = null;
//
//        ContentResolver cr = getContentResolver();
//        Uri uri = CalendarContract.Calendars.CONTENT_URI;
//
//        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
//                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
//                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
//
//        String[] selectionArgs = new String[]{"hera@example.com", "com.example",
//                "hera@example.com"};
//
//
//        cur=cr.query(uri, EVENT_PROJECTION, null, null, null);
//
//        while (cur.moveToNext()) {
//            long callId = 0;
//            String displayName = null;
//            String accountName = null;
//            String ownerName = null;
//
//            callId = cur.getLong(PROJECTION_ID_INDEX);
//            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
//            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
//            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
//            Log.i("dfcdcd",callId+"-"+displayName+"-"+accountName+"-"+ownerName);
//        }
//
//        long calId=1;
//        ContentValues cv=new ContentValues();
//        cv.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,"My calendar");
//
//        Uri updateUri= ContentUris.withAppendedId(uri,calId);
////        int rows=cr.update(updateUri,cv,null,null);
////        Log.i("dfcdcd","Rows updated: "+rows);
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode){
//            case READ_CALENDAR_PERMISIION:
//                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED) {
//                    checkIt();
//                }
//                break;
//            default:
//                super.onRequestPermissionsResult(requestCode,permissions,grantResults);
//        }
//    }

//    public static final String[] EVENT_PROJECTION=new String[]{
//        CalendarContract.Calendars._ID,
//        CalendarContract.Calendars.ACCOUNT_NAME,
//        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
//        CalendarContract.Calendars.OWNER_ACCOUNT
//};
//
//    // The indices for the projection array above.
//    private static final int PROJECTION_ID_INDEX = 0;
//    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
//    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
//    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
//






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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
