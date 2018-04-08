package com.works.adeogo.doctor.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.works.adeogo.doctor.QuestionActivity;
import com.works.adeogo.doctor.R;
import com.works.adeogo.doctor.TestChatActivity;
import com.works.adeogo.doctor.model.ChatHead;
import com.works.adeogo.doctor.model.DoctorProfile;
import com.works.adeogo.doctor.model.Status;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Adeogo on 11/16/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    Context mContext;
    private List<DoctorProfile> mList = null;
    private List<DoctorProfile> mFilteredList = null;

    private final SearchAdapterOnclickHandler mClickHandler;

    public interface SearchAdapterOnclickHandler {
        void voidMethod(List<DoctorProfile> list, int adapterPosition);
    }

    public SearchAdapter(Context context, SearchAdapterOnclickHandler searchAdapterOnclickHandler) {
        mContext = context;
        mClickHandler = searchAdapterOnclickHandler;
    }


    public class SearchAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mNameTextView, mSpecialityTextView, mAboutTextView, mMessage;
        public final ImageView mImageView;

        public SearchAdapterViewHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.doctorName);
            mMessage = (TextView) itemView.findViewById(R.id.textViewMessage);
            mSpecialityTextView = (TextView) itemView.findViewById(R.id.doctorSpeciality);
            mAboutTextView = (TextView) itemView.findViewById(R.id.aboutDoctor);
            mImageView = (ImageView) itemView.findViewById(R.id.profileImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();

            mClickHandler.voidMethod(mFilteredList, adapterPosition);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_search, parent, false);
        return new SearchAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String Name = null, Speciality = null, About = null, PictureUrl = null;
        Status status = null;
        long unixTime = 0;

        if (mFilteredList != null) {

            final DoctorProfile doctorProfile = mFilteredList.get(position);
            Name = doctorProfile.getName();
            Speciality = doctorProfile.getSpeciality();
            About = doctorProfile.getAbout();
            PictureUrl = doctorProfile.getPictureUrl();
            status = doctorProfile.getStatus();


            ((SearchAdapterViewHolder) holder).mSpecialityTextView.setText(Speciality);

            ((SearchAdapterViewHolder) holder).mNameTextView.setText(Name);

            ((SearchAdapterViewHolder) holder).mMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, TestChatActivity.class);

                    intent.putExtra("client_id", doctorProfile.getDoctorId());
                    intent.putExtra("client_name", doctorProfile.getName());
                    intent.putExtra("client_picture", doctorProfile.getPictureUrl());
                    intent.putExtra("collaborate", true);
                    intent.putExtra("which", 1);
                    mContext.startActivity(intent);
                }
            });

            if (status == null) {
                ((SearchAdapterViewHolder) holder).mAboutTextView.setText(About);
                if (PictureUrl != null) {
                    Picasso.with(mContext)
                            .load(PictureUrl)
                            .into(((SearchAdapterViewHolder) holder).mImageView);
                }
            } else {
                String statusText = status.getText();
                String imageUrl = status.getImageUrl();

                ((SearchAdapterViewHolder) holder).mAboutTextView.setText(statusText);
                Picasso.with(mContext)
                        .load(imageUrl)
                        .into(((SearchAdapterViewHolder) holder).mImageView);
            }
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = mList;
                } else {
                    List<DoctorProfile> filteredList = new ArrayList<>();
                    for (DoctorProfile row : mList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getSpeciality().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    mFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<DoctorProfile>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        if (null == mFilteredList) {
            return 0;
        }
        return mFilteredList.size();
    }

    public List<DoctorProfile> swapData(List<DoctorProfile> list) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (list == mList) {
            return null; // bc nothing has changed
        }
        List<DoctorProfile> temp = mList;
        this.mList = list; // new cursor value assigned

        mFilteredList = mList;

        //check if this is a valid cursor, then update the cursor
        if (list != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

}
