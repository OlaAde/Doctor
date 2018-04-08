package com.works.adeogo.doctor.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.peekandpop.shalskar.peekandpop.PeekAndPop;
import com.squareup.picasso.Picasso;
import com.works.adeogo.doctor.R;
import com.works.adeogo.doctor.model.MedicalArticle;

import java.util.List;

/**
 * Created by Adeogo on 10/8/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    private List<MedicalArticle> mList = null;

    public NewsAdapter(Context context) {
        mContext = context;
    }


    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mNameTextView;
        public final TextView mDescTextView;
        public final ImageView mImageView;

        public final ImageView mArticleImageView;
        public final TextView mArticleTitle;
        public final TextView mArticleDesc;


        public NewsAdapterViewHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.title);
            mDescTextView = (TextView) itemView.findViewById(R.id.desc);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);

            PeekAndPop peekAndPop = new PeekAndPop.Builder((Activity) mContext)
                    .peekLayout(R.layout.pop_article)
                    .longClickViews(itemView)
                    .build();

            View peekView = peekAndPop.getPeekView();

            mArticleImageView = peekView.findViewById(R.id.imageView);
            mArticleTitle = peekView.findViewById(R.id.title);
            mArticleDesc = peekView.findViewById(R.id.desc);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_news, parent, false);
        return new NewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String Title = null, Desc = null, ImageUrl = null;

        if (mList != null) {
            MedicalArticle medicalArticle = mList.get(position);
            Title = medicalArticle.getTitle();
            Desc = medicalArticle.getDescription();
            ImageUrl = medicalArticle.getImageUrl();

            if (ImageUrl != null) {
                Picasso.with(mContext)
                        .load(ImageUrl)
                        .into(((NewsAdapter.NewsAdapterViewHolder) holder).mImageView);

                Picasso.with(mContext)
                        .load(ImageUrl)
                        .into(((NewsAdapterViewHolder) holder).mArticleImageView);
            }

            ((NewsAdapterViewHolder) holder).mNameTextView.setText(Title);
            ((NewsAdapterViewHolder) holder).mArticleTitle.setText(Title);

            ((NewsAdapterViewHolder) holder).mDescTextView.setText(Desc);
            ((NewsAdapterViewHolder) holder).mArticleDesc.setText(Desc);
        }

    }

    @Override
    public int getItemCount() {
        if (null == mList) {
            return 0;
        }

        if (mList.size() >= 5) {
            return 5;
        }
        return mList.size();
    }

    public List<MedicalArticle> swapData(List<MedicalArticle> list) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (list == mList) {
            return null; // bc nothing has changed
        }
        List<MedicalArticle> temp = mList;
        this.mList = list; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (list != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }
}
