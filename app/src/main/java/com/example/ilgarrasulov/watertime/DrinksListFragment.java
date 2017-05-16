package com.example.ilgarrasulov.watertime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilgarrasulov on 15.05.2017.
 */

public class DrinksListFragment extends Fragment {

    private RecyclerView mDrinksListRecyclerView;
    private DrinksAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.drink_details_layout,container,false);

        mDrinksListRecyclerView=(RecyclerView) view.findViewById(R.id.drinks_list_recycler_view);
        mDrinksListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        mAdapter=new DrinksAdapter((List<DrinkGlass>) getActivity().getIntent().getSerializableExtra("details"));
        mDrinksListRecyclerView.setAdapter(mAdapter);
        return view;
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
        public DrinkHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater=LayoutInflater.from(getActivity());
            View view=inflater.inflate(R.layout.drink_details_single_item, parent,false);
            return new DrinkHolder(view);
        }

        @Override
        public void onBindViewHolder(DrinkHolder holder, int position) {
            DrinkGlass glass=mGlasses.get(position);
            holder.mTextView.setText( getResources().getString(R.string.at_str,DatabaseQuery.convertDateToString(glass.getGlass_time(),DatabaseQuery.sdf_today)));
        }

        @Override
        public int getItemCount() {
            return mGlasses.size();
        }
    }
}
