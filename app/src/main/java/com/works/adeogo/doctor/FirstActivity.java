package com.works.adeogo.doctor;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.works.adeogo.doctor.adapters.CustomPagerAdapter;

public class FirstActivity extends AppCompatActivity {


    private ViewPager mViewPager;
    private int pageNum;

    private NavigationTabStrip mTopNavigationTabStrip;

    private TextView mPreviousTextView, mNextTextView, mContentTextView;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        initUI();
        setUI();
    }

    private void initUI() {
        mViewPager = (ViewPager) findViewById(R.id.first_view_pager);
        mViewPager.setCurrentItem(0);
        mTopNavigationTabStrip = (NavigationTabStrip) findViewById(R.id.nav_strip);

    }

    private void setUI() {
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                super.setPrimaryItem(container, position, object);

                checkers(position);
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                final View view = new View(getBaseContext());
                container.addView(view);
                return view;
            }
        });

        mTopNavigationTabStrip.setTabIndex(0, true);
        mTopNavigationTabStrip.setViewPager(mViewPager);


        mContentTextView = findViewById(R.id.content);
        mPreviousTextView = findViewById(R.id.previous);
        mNextTextView = findViewById(R.id.next);
        final NavigationTabStrip navigationTabStrip = (NavigationTabStrip) findViewById(R.id.nav_strip);
        navigationTabStrip.setTitles("", "", "");
        navigationTabStrip.setTabIndex(0, true);
        navigationTabStrip.setTitleSize(0);
        navigationTabStrip.setStripColor(Color.RED);
        navigationTabStrip.setStripWeight(10);
        navigationTabStrip.setStripFactor(6);
        navigationTabStrip.setStripType(NavigationTabStrip.StripType.LINE);
        navigationTabStrip.setStripGravity(NavigationTabStrip.StripGravity.BOTTOM);
        navigationTabStrip.setTypeface("fonts/typeface.ttf");
        navigationTabStrip.setCornersRadius(3);
        navigationTabStrip.setAnimationDuration(300);
        navigationTabStrip.setInactiveColor(Color.GRAY);
        navigationTabStrip.setActiveColor(Color.WHITE);

    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_first);
////        handler = new Handler();
////        viewPager = (ViewPager)findViewById(R.id.hot_deal_view_pager);
////        mAdapter = new CommonViewPagerAdapter(FirstActivity.this, getTestData());
////        viewPager.setAdapter(mAdapter);
////        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
////            @Override
////            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
////            }
////            @Override
////            public void onPageSelected(int position) {
////                page = position;
////                switch (position){
////                    case 0:
////                        radioGroup.check(R.id.radioButton);
////                        break;
////                    case 1:
////                        radioGroup.check(R.id.radioButton2);
////                        break;
////                    case 2:
////                        radioGroup.check(R.id.radioButton3);
////                        break;
////                }
////            }
////            @Override
////            public void onPageScrollStateChanged(int state) {
////            }
////        });
//    }

//    public List<HotDealObject> getTestData() {
//        List<HotDealObject> mTestData = new ArrayList<HotDealObject>();
//        mTestData.add(new HotDealObject("$42.00", "", "Fried Fish with Sauce", "It is a long established fact that a reader will be distracted by the when looking at its layout.It is a long established fact that a reader will be distracted by the when looking at its layout"));
//        mTestData.add(new HotDealObject("$30.00", "", "Rice and Bean with Sauce", "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC It is a long established fact that a reader will be distracted by the when looking at its layout"));
//        mTestData.add(new HotDealObject("$23.00", "", "Baked Potato with Salad", "There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour It is a long established fact that a reader will be distracted by the when looking at its layout"));
//        return mTestData;
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        handler.postDelayed(runnable, delay);
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        handler.removeCallbacks(runnable);
//    }

    private void checkers(final int PageNum)
    {
        switch (PageNum){
            case 0:
                mPreviousTextView.setVisibility(View.GONE);
                mNextTextView.setVisibility(View.VISIBLE);
                mNextTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mViewPager.setCurrentItem(1);
                        pageNum = 1;
                    }
                });

                mContentTextView.setText("Hello,\nWelcome to the Dokitari app");
                break;
            case 1:
                mPreviousTextView.setVisibility(View.VISIBLE);
                mNextTextView.setVisibility(View.VISIBLE);
                mNextTextView.setText("Next");

                mContentTextView.setText("Here you can,\nConsult with patients");

                mPreviousTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mViewPager.setCurrentItem(0);
                        pageNum = 0;
                    }
                });

                mNextTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mViewPager.setCurrentItem(2);
                        pageNum = 2;
                    }
                });
                break;
            case 2:
                mPreviousTextView.setVisibility(View.VISIBLE);
                mNextTextView.setVisibility(View.VISIBLE);
                mNextTextView.setText("Let's Go");

                mContentTextView.setText("and\nManage appointments");

                mPreviousTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mViewPager.setCurrentItem(1);
                        pageNum = 1;
                    }
                });

                mNextTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
        }
    }
}







