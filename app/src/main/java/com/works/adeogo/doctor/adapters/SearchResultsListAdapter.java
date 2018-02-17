package com.works.adeogo.doctor.adapters;

/**
 * Created by ademi on 29/01/2018.
 */

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.util.Util;
import com.works.adeogo.doctor.R;
import com.works.adeogo.doctor.model.DoctorProfile;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsListAdapter extends RecyclerView.Adapter<SearchResultsListAdapter.ViewHolder> {

    private List<DoctorProfile> mDataSet = new ArrayList<>();

    private int mLastAnimatedItemPosition = -1;

    public interface OnItemClickListener{
        void onClick(DoctorProfile colorWrapper);
    }

    private OnItemClickListener mItemsOnClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
//        public final TextView mColorName;
//        public final TextView mColorValue;
//        public final View mTextContainer;

        public ViewHolder(View view) {
            super(view);
//            mColorName = (TextView) view.findViewById(R.id.color_name);
//            mColorValue = (TextView) view.findViewById(R.id.color_value);
//            mTextContainer = view.findViewById(R.id.text_container);
        }
    }

    public void swapData(List<DoctorProfile> mNewDataSet) {
        mDataSet = mNewDataSet;
        notifyDataSetChanged();
    }

    public void setItemsOnClickListener(OnItemClickListener onClickListener){
        this.mItemsOnClickListener = onClickListener;
    }

    @Override
    public SearchResultsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_results_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultsListAdapter.ViewHolder holder, final int position) {

//        DoctorProfile doctorProfile = mDataSet.get(position);
//        holder.mColorName.setText(doctorProfile.getName());
//        holder.mColorValue.setText(colorSuggestion.getHex());
//
//        int color = Color.parseColor(colorSuggestion.getHex());
//        holder.mColorName.setTextColor(color);
//        holder.mColorValue.setTextColor(color);
//
//        if(mLastAnimatedItemPosition < position){
//            animateItem(holder.itemView);
//            mLastAnimatedItemPosition = position;
//        }
//
//        if(mItemsOnClickListener != null){
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mItemsOnClickListener.onClick(mDataSet.get(position));
//                }
//            });
//        }


    }

    @Override
    public int getItemCount() {
        return 10;
    }

    private void animateItem(View view) {
        view.setTranslationY(Util.getScreenHeight((Activity) view.getContext()));
        view.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .start();
    }
}