package com.works.adeogo.doctor.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.works.adeogo.doctor.R;
import com.works.adeogo.doctor.model.Question;

import java.util.List;

/**
 * Created by Adeogo on 10/8/2017.
 */

public class QuestionAdapter extends ArrayAdapter<Question> {
    public QuestionAdapter(Context context, int resource, List<Question> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_question, parent, false);
        }

        CardView youCardView = convertView.findViewById(R.id.mYouMessage);
        CardView notYouCardView = convertView.findViewById(R.id.mNotYouMessage);

        TextView youMessageTextView = (TextView) convertView.findViewById(R.id.youQuestionMessageTextView);
        TextView youNameTextView = (TextView) convertView.findViewById(R.id.youQuestionNameTextView);

        TextView messageTextView = (TextView) convertView.findViewById(R.id.questionMessageTextView);
        TextView nameTextView = (TextView) convertView.findViewById(R.id.questionNameTextView);

        Question question = getItem(position);

        int check = question.getYou();

        if (check == 0){
            notYouCardView.setVisibility(View.GONE);
            youCardView.setVisibility(View.VISIBLE);
            youMessageTextView.setText(question.getText());
            youNameTextView.setText(question.getName());
        }else if (check == 1){
            youCardView.setVisibility(View.GONE);
            notYouCardView.setVisibility(View.VISIBLE);
            messageTextView.setText(question.getText());
            nameTextView.setText(question.getName());

        }

        return convertView;
    }
}
