package com.works.adeogo.doctor.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.works.adeogo.doctor.R;
import com.works.adeogo.doctor.model.Question;

import java.util.List;

/**
 * Created by Adeogo on 10/8/2017.
 */

public class QuestionAdapter extends ArrayAdapter<Question> {

    Context mContext;
    public QuestionAdapter(Context context, int resource, List<Question> objects) {
        super(context, resource, objects);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_question, parent, false);
        }

        CardView youCardView = convertView.findViewById(R.id.mYouMessage);
        CardView notYouCardView = convertView.findViewById(R.id.mNotYouMessage);

        ImageView photoImageViewNotYou = (ImageView) convertView.findViewById(R.id.photoNotYou);
        ImageView photoImageViewYou = (ImageView) convertView.findViewById(R.id.photoYou);

        TextView youMessageTextView = (TextView) convertView.findViewById(R.id.youQuestionMessageTextView);
        TextView messageTextView = (TextView) convertView.findViewById(R.id.questionMessageTextView);

        Question question = getItem(position);

        int check = question.getYou();

        boolean isPhoto = question.getPhotoUrl() != null;

        if (check == 0){
            notYouCardView.setVisibility(View.GONE);
            youCardView.setVisibility(View.VISIBLE);
            if (isPhoto){
                youMessageTextView.setVisibility(View.GONE);
                photoImageViewYou.setVisibility(View.VISIBLE);
                Picasso.with(mContext)
                        .load(question.getPhotoUrl())
                        .into(photoImageViewYou);
            }else {
                photoImageViewYou.setVisibility(View.GONE);
                youMessageTextView.setVisibility(View.VISIBLE);
                youMessageTextView.setText(question.getText());
            }
        }else {
            youCardView.setVisibility(View.GONE);
            notYouCardView.setVisibility(View.VISIBLE);

            if (isPhoto) {
                messageTextView.setVisibility(View.GONE);
                photoImageViewNotYou.setVisibility(View.VISIBLE);
                Picasso.with(mContext)
                        .load(question.getPhotoUrl())
                        .into(photoImageViewNotYou);
            }else {
                messageTextView.setVisibility(View.VISIBLE);
                photoImageViewNotYou.setVisibility(View.GONE);
                messageTextView.setText(question.getText());
            }
        }
        return convertView;
    }
}
