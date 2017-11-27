package com.works.adeogo.doctor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.works.adeogo.doctor.R;
import com.works.adeogo.doctor.model.Appointment;

import java.util.List;

/**
 * Created by Adeogo on 11/19/2017.
 */

public class AppointmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    private List<Appointment> mList = null;

    private final AppointmentAdapter.AppointmentAdapterOnclickHandler mClickHandler;


    public interface AppointmentAdapterOnclickHandler {
        void voidMethod(List<Appointment> list, int adapterPosition);
    }

    public AppointmentAdapter(Context context, AppointmentAdapter.AppointmentAdapterOnclickHandler appointmentAdapterOnclickHandler) {
        mContext = context;
        mClickHandler = appointmentAdapterOnclickHandler;
    }


    public class AppointmentAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mClientNameTextView;
        public final TextView mLocationTextView;
        public final TextView mDateTextView;

        public AppointmentAdapterViewHolder(View itemView) {
            super(itemView);
            mClientNameTextView = (TextView) itemView.findViewById(R.id.itemClientName);
            mLocationTextView = (TextView) itemView.findViewById(R.id.itemLocation);
            mDateTextView = (TextView) itemView.findViewById(R.id.itemDate);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();

            mClickHandler.voidMethod(mList, adapterPosition);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_appointment, parent, false);
        return new AppointmentAdapter.AppointmentAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String ClientName = null;
        String Location = null;
        String Date = null;

        if (mList != null) {
            Appointment appointment = mList.get(position);
            ClientName = appointment.getClientName();
            Location = appointment.getLocation();
            int day = appointment.getDay();
            int month = appointment.getMonth();
            int year = appointment.getYear();
            Date = day + "/" + month + "/" + year;
        }

        ((AppointmentAdapterViewHolder) holder).mClientNameTextView.setText(ClientName);
        ((AppointmentAdapterViewHolder) holder).mLocationTextView.setText(Location);
        ((AppointmentAdapterViewHolder) holder).mDateTextView.setText(Date);
    }


    @Override
    public int getItemCount() {
        if (null == mList) {
            return 0;
        }
        return mList.size();
    }


    public List<Appointment> swapData(List<Appointment> list) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (list == mList) {
            return null; // bc nothing has changed
        }
        List<Appointment> temp = mList;
        this.mList = list; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (list != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }
}
