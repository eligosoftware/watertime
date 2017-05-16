package com.example.ilgarrasulov.watertime;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by ilgarrasulov on 15.05.2017.
 */

public class DrinksListFragment extends Fragment {

    private Calendar cal= Calendar.getInstance(Locale.ENGLISH);
    private RecyclerView mDrinksListRecyclerView;
    private DrinksAdapter mAdapter;
    private AlertDialog.Builder builder;
    private DatabaseQuery dbquery;
    private TextView empty_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.drink_details_layout,container,false);

//        LinearLayoutManager manager = new LinearLayoutManager(getActivity()){
//            @Override
//            public boolean canScrollVertically() {
//                return false;
//            }
//        };

        dbquery=new DatabaseQuery(getActivity());

        mDrinksListRecyclerView=(RecyclerView) view.findViewById(R.id.drinks_list_recycler_view);
        mDrinksListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mDrinksListRecyclerView.setNestedScrollingEnabled(false);
        builder=new AlertDialog.Builder(getActivity());

        mAdapter=new DrinksAdapter((List<DrinkGlass>) getActivity().getIntent().getSerializableExtra("details"));
        mDrinksListRecyclerView.setAdapter(mAdapter);

        empty_view=(TextView)view.findViewById(R.id.empty_view);

        return view;
    }

    private void update(){
        List<DrinkGlass> glasses= dbquery.getDrinkGlasses(cal.getTime());
        mAdapter.setGlasses(glasses);

        if(glasses.size()>0){
            mDrinksListRecyclerView.setVisibility(View.VISIBLE);
            empty_view.setVisibility(View.GONE);
        }else {
            mDrinksListRecyclerView.setVisibility(View.GONE);
            empty_view.setVisibility(View.VISIBLE);
        }
        mAdapter.notifyDataSetChanged();
    }
    private class DrinkHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private long _id;
        private TextView mTextView;
        private ImageView mImageView;

        public DrinkHolder(View itemView) {
            super(itemView);
            mTextView= (TextView) itemView.findViewById(R.id.drink_details_single_item_text_view);
            mImageView= (ImageView) itemView.findViewById(R.id.drink_details_single_item_delete_link);
            mImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.drink_details_single_item_delete_link){
                builder.setMessage(R.string.delete_sure_question).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbquery.delete(DBSchema.DrinkTimes.NAME,"_id = "+_id);
                        update();
                    }
                }).setNegativeButton(getString(R.string.no),null);
                builder.show();
            }
        }
    }

    private class DrinksAdapter extends RecyclerView.Adapter<DrinkHolder>{

        private List<DrinkGlass> mGlasses;

        private void setGlasses(List<DrinkGlass> Glasses){
            mGlasses=Glasses;
        }

        public DrinksAdapter(List<DrinkGlass> glasses){
            mGlasses=glasses;
        }

        @Override
        public DrinkHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater=LayoutInflater.from(getActivity());
            View view=inflater.inflate(R.layout.drink_details_single_item, parent,false);
            return new DrinkHolder(view);
        }

        @Override
        public void onBindViewHolder(DrinkHolder holder, int position) {
            DrinkGlass glass=mGlasses.get(position);
            holder._id=glass.getId();
            holder.mTextView.setText( getResources().getString(R.string.at_str,DatabaseQuery.convertDateToString(glass.getGlass_time(),DatabaseQuery.sdf_today)));
        }

        @Override
        public int getItemCount() {
            return mGlasses.size();
        }
    }
}
