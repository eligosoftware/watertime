package com.example.ilgarrasulov.watertime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by ilgarrasulov on 04.05.2017.
 */

public class CalendarFragment extends Fragment implements CalendarClick {



    private RecyclerView mDrinksListRecyclerView;
    private DrinksAdapter mAdapter;

    private CardView detailsCardView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.calendar_tab,container,false);

        CalendarCustomView calendarCustomView=(CalendarCustomView)view.findViewById(R.id.custom_calendar);
        calendarCustomView.setCallback(this);

        mDrinksListRecyclerView=(RecyclerView) view.findViewById(R.id.calendar_tab_day_details);

        mDrinksListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDrinksListRecyclerView.setNestedScrollingEnabled(false);

        Calendar cal=Calendar.getInstance(Locale.ENGLISH);
        mAdapter=new CalendarFragment.DrinksAdapter(new DatabaseQuery(getActivity()).getDrinkGlasses(cal.getTime()));
        mDrinksListRecyclerView.setAdapter(mAdapter);


        detailsCardView=(CardView)view.findViewById(R.id.calendar_tab_day_details_card_view);

        return view;
    }

    @Override
    public void onDayClicked(Calendar calendar) {
        mAdapter=new CalendarFragment.DrinksAdapter(new DatabaseQuery(getActivity()).getDrinkGlasses(calendar.getTime()));
        mDrinksListRecyclerView.setAdapter(mAdapter);
        resizeDetailsCardView();
    }


    private void resizeDetailsCardView(){
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) detailsCardView.getLayoutParams();
                params.height= 70+mAdapter.getItemCount()*40;
        detailsCardView.setLayoutParams(params);
    }

    private class DrinkHolder extends RecyclerView.ViewHolder{

        private TextView mTextView;
        private ImageView mImageView;

        public DrinkHolder(View itemView) {
            super(itemView);
            mTextView= (TextView) itemView.findViewById(R.id.drink_details_single_item_text_view);
            mImageView= (ImageView) itemView.findViewById(R.id.drink_details_single_item_delete_link);
        }

    }

    private class DrinksAdapter extends RecyclerView.Adapter<DrinkHolder>{

        private List<DrinkGlass> mGlasses;

        public DrinksAdapter(List<DrinkGlass> glasses){
            mGlasses=glasses;
        }

        @Override
        public CalendarFragment.DrinkHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater=LayoutInflater.from(getActivity());
            View view=inflater.inflate(R.layout.drink_details_single_item_no_delete, parent,false);
            return new CalendarFragment.DrinkHolder(view);
        }

        @Override
        public void onBindViewHolder(CalendarFragment.DrinkHolder holder, int position) {
            DrinkGlass glass=mGlasses.get(position);
            holder.mTextView.setText( getResources().getString(R.string.at_str,DatabaseQuery.convertDateToString(glass.getGlass_time(),DatabaseQuery.sdf_today)));
        }

        @Override
        public int getItemCount() {
            return mGlasses.size();
        }
    }

}
