package com.works.adeogo.doctor;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;
import com.works.adeogo.doctor.adapters.NewsAdapter;
import com.works.adeogo.doctor.model.Appointment;
import com.works.adeogo.doctor.model.MedicalArticle;
import com.works.adeogo.doctor.utils.JSONFormat;
import com.works.adeogo.doctor.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment{

    private NestedScrollView mMainLayout;
    private RecyclerView mNewsRecyclerView;
    private LinearLayoutManager mNewsLayoutManager;
    private List<MedicalArticle> medicalArticles = null;
    private NewsAdapter newsAdapter;
    private ProgressBar progressBar;
    private TextView mPoweredTextView, mDescTextView, mTitleTextView;
    private ImageView mImageView;
    private FragmentManager mFragmentManager;
    private RelativeLayout mRelativeLayout;
    private View popLayout;
    private Context mContext;

    private CarouselView carouselView;

    int[] sampleImages = {R.drawable.image_1, R.drawable.image_2, R.drawable.image_3};
    private ImageListener imageListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mContext = getContext();
        mNewsRecyclerView = rootView.findViewById(R.id.newsRecyclerView);
        mMainLayout = rootView.findViewById(R.id.rootLayout);

        progressBar = rootView.findViewById(R.id.progressBar);
        mPoweredTextView = rootView.findViewById(R.id.poweredClick);

        newsAdapter = new NewsAdapter(mContext);
        mNewsLayoutManager = new LinearLayoutManager(mContext);
        mNewsRecyclerView.setLayoutManager(mNewsLayoutManager);
        mNewsRecyclerView.setAdapter(newsAdapter);
        mFragmentManager = getFragmentManager();

        LayoutInflater newInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popLayout = newInflater.inflate( R.layout.pop_article, null);
        mTitleTextView = popLayout.findViewById(R.id.title);
        mDescTextView = popLayout.findViewById(R.id.desc);
        mImageView = popLayout.findViewById(R.id.imageView);

        updateLayout();

        carouselView = (CarouselView) rootView.findViewById(R.id.carouselView);
        imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
            }
        };

        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);
        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                Toasty.success(mContext, "Clicked item: " + position, Toast.LENGTH_SHORT, false).show();

            }
        });


        mPoweredTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return rootView;
    }

    public void updateLayout() {
        new LoadNewsTask().execute();
    }

    public class LoadNewsTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            URL url = NetworkUtils.buildUrl();
            String reply = null;
            try {
                reply = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return reply;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                medicalArticles = JSONFormat.getObjectArray(s);
                progressBar.setVisibility(View.GONE);
                newsAdapter.swapData(medicalArticles);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
