package com.works.adeogo.doctor.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ademi on 04/04/2018.
 */

public abstract class ViewHolder<DATA> extends RecyclerView.ViewHolder {

    public abstract void onBind(DATA data);

    public ViewHolder(View itemView) {
        super(itemView);
    }

}