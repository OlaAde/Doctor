package com.works.adeogo.doctor;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.works.adeogo.doctor.adapters.SliderAdapter;


public class FirstActivity extends AppCompatActivity {
    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private SliderAdapter mSlideAdapter;

    private Button mNextBtn, mPrevBtn;

    private TextView[] mDots;

    private int mCurrentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        mSlideViewPager = findViewById(R.id.slideViewPager);

        mNextBtn = findViewById(R.id.nextButton);
        mPrevBtn = findViewById(R.id.prevButton);

        mDotLayout = findViewById(R.id.dotsLayout);
        mSlideAdapter = new SliderAdapter(this);

        mSlideViewPager.setAdapter(mSlideAdapter);

        addDotsIndicator(0);
        setNextClickListener(true);

        mPrevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrentPage - 1);
            }
        });
    }

    private void setNextClickListener(boolean boo){
        if (boo){
            mNextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSlideViewPager.setCurrentItem(mCurrentPage + 1);
                }
            });
        }else {
            mNextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =  new Intent(FirstActivity.this, NavigationStartActivity.class);
                    finish();
                    ActivityOptions options =
                            ActivityOptions.makeCustomAnimation(FirstActivity.this, R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);
                    startActivity(intent, options.toBundle());
                }
            });
        }
    }

    public void addDotsIndicator(int position){
        mDots = new TextView[4];
        mDotLayout.removeAllViews();

        for (int i = 0; i< mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            mDotLayout.addView(mDots[i]);

            mSlideViewPager.addOnPageChangeListener(onPageChangeListener);
        }

        checkDotsIndicator(position);
    }

    public void checkDotsIndicator(int position){
        switch (position){
            case 0:
                mDots[0].setTextColor(getResources().getColor(R.color.colorWhite));
                mDots[1].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
                mDots[2].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
                mDots[3].setTextColor(getResources().getColor(R.color.colorTransparentWhite));


                mPrevBtn.setEnabled(false);
                mPrevBtn.setVisibility(View.INVISIBLE);
                mNextBtn.setEnabled(true);

                mPrevBtn.setText("");
                mNextBtn.setText("Next");

                setNextClickListener(true);
                break;
            case 1:
                mDots[0].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
                mDots[1].setTextColor(getResources().getColor(R.color.colorWhite));
                mDots[2].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
                mDots[3].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

                mPrevBtn.setEnabled(true);
                mPrevBtn.setVisibility(View.VISIBLE);
                mNextBtn.setEnabled(true);

                mPrevBtn.setText("Back");
                mNextBtn.setText("Next");
                setNextClickListener(true);
                break;

            case 2:
                mDots[0].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
                mDots[1].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
                mDots[2].setTextColor(getResources().getColor(R.color.colorWhite));
                mDots[3].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

                mPrevBtn.setEnabled(true);
                mPrevBtn.setVisibility(View.VISIBLE);
                mNextBtn.setEnabled(true);

                mPrevBtn.setText("Back");
                mNextBtn.setText("Next");
                setNextClickListener(true);
                break;

            case 3:
                mDots[0].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
                mDots[1].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
                mDots[2].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
                mDots[3].setTextColor(getResources().getColor(R.color.colorWhite));

                mPrevBtn.setEnabled(true);
                mPrevBtn.setVisibility(View.VISIBLE);
                mNextBtn.setEnabled(true);

                mPrevBtn.setText("Back");
                mNextBtn.setText("Let's Go");
                setNextClickListener(false);
                break;
        }
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            mCurrentPage = position;
            checkDotsIndicator(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
