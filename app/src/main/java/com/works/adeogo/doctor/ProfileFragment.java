package com.works.adeogo.doctor;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.works.adeogo.doctor.model.DoctorProfile;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private ImageView mProfileImageView;
    private LinearLayout mAboutLayout;
    private TextView mNameEditText,  mSpecialTextView, mSpecialityTextView, mStartTimeTextVIew, mEndTimeTextView, mOnlineTextView, mHomeTextView, mOfficeTextView, mClinicTextView;
    private TextView mPhoneNumberEditText, mSundayDateTextView, mMondayDateTextView, mTuesdayDateTextView, mWednesdayDateTextView, mThursdayDateTextView, mFridayDateTextView, mSaturdayDateTextView;


    private String urlFemale = "https://firebasestorage.googleapis.com/v0/b/dokita-c3d87.appspot.com/o/generic_photos%2Ficon_female.png?alt=media&token=eaecb6c4-5f7c-4bd6-ae8f-6a7a73e27a80";
    private String urlMale = "https://firebasestorage.googleapis.com/v0/b/dokita-c3d87.appspot.com/o/generic_photos%2Ficon_male.png?alt=media&token=e4530bf8-4f41-495d-885e-e2c73007b395";


    public static final int RC_SIGN_IN = 1;
    private static final int RC_PHOTO_PICKER = 2;

    private String userId, mUsername, mAbout;

    public static final String ANONYMOUS = "anonymous";

    private String mPictureUrl = urlMale, mPhoneNumber = "", mDoctorName = "", mEmail = "", mPassword = "", mCountry = "", mCity = "", mSpeciality = "", mDoctorId = "", mSpecial = "";
    private int sunday = 0, monday = 0, tuesday = 0, wednesday = 0, thursday = 0, friday = 0, saturday = 0, startHour, startMinute, endHour, endMinute, online = 0, home = 0, office = 0, clinic = 0, sex = 0 ;


    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private StorageReference mProfilePhotosStorageReference;
    private DatabaseReference mDoctorProfileDatabaseReference, mSelfPhotoReference;
    private DoctorProfile mReturnedDoctorProfile;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        mStartTimeTextVIew = rootView.findViewById(R.id.startTime);
        mEndTimeTextView = rootView.findViewById(R.id.endTime);

        mSundayDateTextView = rootView.findViewById(R.id.sunday);
        mMondayDateTextView = rootView.findViewById(R.id.monday);
        mTuesdayDateTextView = rootView.findViewById(R.id.tuesday);
        mWednesdayDateTextView = rootView.findViewById(R.id.wednesday);
        mThursdayDateTextView = rootView.findViewById(R.id.thursday);
        mFridayDateTextView = rootView.findViewById(R.id.friday);
        mSaturdayDateTextView = rootView.findViewById(R.id.saturday);

        mOnlineTextView = rootView.findViewById(R.id.online);
        mHomeTextView = rootView.findViewById(R.id.home);
        mOfficeTextView = rootView.findViewById(R.id.office);
        mClinicTextView = rootView.findViewById(R.id.clinic);

        mAboutLayout = rootView.findViewById(R.id.aboutMeLayout);

        mSpecialTextView = rootView.findViewById(R.id.tVSpecialCost);
        mSpecialityTextView = rootView.findViewById(R.id.profileSpecialityTextView);

        mProfileImageView = rootView.findViewById(R.id.profilePictureImageView);

        mNameEditText = rootView.findViewById(R.id.profileNameTextView);
        mPhoneNumberEditText = rootView.findViewById(R.id.profilePhoneNumberTextView);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    userId = user.getUid();
                    mDoctorProfileDatabaseReference = mFirebaseDatabase.getReference().child("new_doctors").child(userId).child("profile").child("profile");
                    onSignedInInitialize(user.getDisplayName());
                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        };

        onClicksOnCreate();

        mProfilePhotosStorageReference = mFirebaseStorage.getReference().child("profile_photos");



        return rootView;
    }

    private void onSignedInInitialize(String username) {
        mUsername = username;
        attachDatabaseReadListener();
    }

    private void onClicksOnCreate(){

        Picasso.with(getContext())
                .load(mPictureUrl)
                .resize(500, 500)
                .centerCrop()
                .into(mProfileImageView);


        mAboutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);


                intent.putExtra("doctorId", mReturnedDoctorProfile.getDoctorId());
                intent.putExtra("name",mReturnedDoctorProfile.getName());
                intent.putExtra("phoneNUmber", mReturnedDoctorProfile.getDoctorPhoneNumber());
                intent.putExtra("pictureUrl", mReturnedDoctorProfile.getPictureUrl());
                intent.putExtra("email", mReturnedDoctorProfile.getEmail());
                intent.putExtra("password", mReturnedDoctorProfile.getPassword());
                intent.putExtra("country", mReturnedDoctorProfile.getCountry());
                intent.putExtra("city", mReturnedDoctorProfile.getCity());
                intent.putExtra("speciality", mReturnedDoctorProfile.getSpeciality());
                intent.putExtra("consultationFee", mReturnedDoctorProfile.getConsultationFee());
                intent.putExtra("about", mReturnedDoctorProfile.getAbout());

                intent.putExtra("sunday", mReturnedDoctorProfile.getSunday());
                intent.putExtra("monday", mReturnedDoctorProfile.getMonday());
                intent.putExtra("tuesday", mReturnedDoctorProfile.getTuesday());
                intent.putExtra("wednesday", mReturnedDoctorProfile.getWednesday());
                intent.putExtra("thursday", mReturnedDoctorProfile.getThursday());
                intent.putExtra("friday", mReturnedDoctorProfile.getFirday());
                intent.putExtra("saturday", mReturnedDoctorProfile.getSaturday());
                intent.putExtra("startHour", mReturnedDoctorProfile.getStartHour());
                intent.putExtra("startMinute", mReturnedDoctorProfile.getStartMinute());
                intent.putExtra("endHour", mReturnedDoctorProfile.getEndHour());
                intent.putExtra("endMinute", mReturnedDoctorProfile.getEndMinute());
                intent.putExtra("online", mReturnedDoctorProfile.getOnlineConsult());
                intent.putExtra("home", mReturnedDoctorProfile.getHomeVisit());
                intent.putExtra("office", mReturnedDoctorProfile.getOfficeVisit());
                intent.putExtra("clinic", mReturnedDoctorProfile.getClinic());
                intent.putExtra("sex", mReturnedDoctorProfile.getSex());

                startActivity(intent);
            }
        });
    }
    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    DoctorProfile doctorProfile = dataSnapshot.getValue(DoctorProfile.class);
                    mReturnedDoctorProfile = doctorProfile;

                    if (TextUtils.equals(doctorProfile.getName(), "") ){
                        mNameEditText.setText("");
                    }else {
                        mDoctorName = doctorProfile.getName();
                        mNameEditText.setText(doctorProfile.getName());
                    }

                    if (TextUtils.equals(doctorProfile.getDoctorPhoneNumber(), "")){
                        mPhoneNumberEditText.setText("");
                    }else {
                        mPhoneNumber = doctorProfile.getDoctorPhoneNumber();
                        mPhoneNumberEditText.setText(doctorProfile.getDoctorPhoneNumber());
                    }

                    mSpeciality = doctorProfile.getSpeciality();
                    sunday = doctorProfile.getSunday();
                    monday = doctorProfile.getMonday();
                    tuesday = doctorProfile.getTuesday();
                    wednesday = doctorProfile.getWednesday();
                    thursday = doctorProfile.getThursday();
                    friday = doctorProfile.getFirday();
                    saturday = doctorProfile.getSaturday();
                    startHour = doctorProfile.getStartHour();
                    startMinute = doctorProfile.getStartMinute();
                    endHour = doctorProfile.getEndHour();
                    endMinute = doctorProfile.getEndMinute();

                    online = doctorProfile.getOnlineConsult();
                    home = doctorProfile.getHomeVisit();
                    office = doctorProfile.getOfficeVisit();
                    clinic = doctorProfile.getClinic();

                    sex = doctorProfile.getSex();

                    mAbout = doctorProfile.getAbout();

                    mSpecial = doctorProfile.getConsultationFee();

                    mSpecialityTextView.setText(mSpeciality);
                    mSpecialTextView.setText(mSpecial);

                    if (startMinute < 10){
                        mStartTimeTextVIew.setText(startHour+":0"+startMinute + "  -  " );
                    }else mStartTimeTextVIew.setText(startHour+":"+startMinute + "  -  " );

                    if (endMinute < 10){
                        mEndTimeTextView.setText(endHour+":0"+endMinute);
                    }else
                    mEndTimeTextView.setText(endHour+":"+endMinute);

                    mPictureUrl = doctorProfile.getPictureUrl();
                    if (TextUtils.isEmpty(mPictureUrl)){
                        Picasso.with(getContext())
                                .load(urlMale)
                                .resize(500, 500)
                                .centerCrop()
                                .into(mProfileImageView);
                    }else {
                        mPictureUrl = doctorProfile.getPictureUrl();
                        Picasso.with(getContext())
                                .load(mPictureUrl)
                                .resize(500, 500)
                                .centerCrop()
                                .into(mProfileImageView);
                    }

                    updateDateColor();
                    updateVenueColor();

                }
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mDoctorProfileDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mDoctorProfileDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabaseReadListener();
    }

    private void updateDateColor(){

        if (sunday == 1){
            mSundayDateTextView.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (sunday == 0){
            mSundayDateTextView.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_button_background));
        }

        if (monday == 1){
            mMondayDateTextView.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (monday == 0){
            mMondayDateTextView.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_button_background));
        }

        if (tuesday == 1){
            mTuesdayDateTextView.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (tuesday == 0){
            mTuesdayDateTextView.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_button_background));
        }

        if (wednesday == 1){
            mWednesdayDateTextView.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (wednesday == 0){
            mWednesdayDateTextView.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_button_background));
        }

        if (thursday == 1){
            mThursdayDateTextView.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (thursday == 0){
            mThursdayDateTextView.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_button_background));
        }

        if (friday == 1){
            mFridayDateTextView.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (friday == 0){
            mFridayDateTextView.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_button_background));
        }

        if (saturday == 1){
            mSaturdayDateTextView.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (saturday == 0){
            mSaturdayDateTextView.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_button_background));
        }
    }

    private void updateVenueColor(){

        if (online == 1){
            mOnlineTextView.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (online == 0){
            mOnlineTextView.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_button_background));
        }

        if (home == 1){
            mHomeTextView.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (home == 0){
            mHomeTextView.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_button_background));
        }

        if (office == 1){
            mOfficeTextView.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (office == 0){
            mOfficeTextView.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_button_background));
        }

        if (clinic == 1){
            mClinicTextView.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (clinic == 0){
            mClinicTextView.setBackground(getActivity().getResources().getDrawable(R.drawable.curved_button_background));
        }


    }

}
