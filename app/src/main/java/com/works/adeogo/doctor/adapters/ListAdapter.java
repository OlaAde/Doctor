package com.works.adeogo.doctor.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.works.adeogo.doctor.R;
import com.works.adeogo.doctor.model.ChatHead;
import com.works.adeogo.doctor.model.Question;

import java.util.List;

/**
 * Created by Adeogo on 11/16/2017.
 */

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    private List<ChatHead> mList = null;

    private final ListAdapterOnclickHandler mClickHandler;


    public interface ListAdapterOnclickHandler{
        void voidMethod(List<ChatHead> list, int adapterPosition);
    }

    public ListAdapter(Context context, ListAdapterOnclickHandler listAdapterOnclickHandler){
        mContext = context;
        mClickHandler = listAdapterOnclickHandler;
    }


    public class ListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mNameTextView;

        public ListAdapterViewHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.title_tv);

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
        View view  = layoutInflater.inflate(R.layout.layout_list, parent, false);
        return new ListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String Title = null;

        if(mList != null){
            ChatHead chatHead = mList.get(position);
            Title = chatHead.getUserName();
            if (TextUtils.isEmpty(Title) || TextUtils.equals("", Title)){
                Title = "Anonymous";
            }
        }

        ((ListAdapterViewHolder) holder).mNameTextView.setText(Title);
    }


    @Override
    public int getItemCount() {
        if(null == mList){
            return 0;
        }
        return mList.size();
    }


    public List<ChatHead> swapData(List<ChatHead> list) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (list == mList) {
            return null; // bc nothing has changed
        }
        List<ChatHead> temp = mList;
        this.mList = list; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (list != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }


}
