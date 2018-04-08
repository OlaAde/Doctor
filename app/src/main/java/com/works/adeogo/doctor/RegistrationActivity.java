package com.works.adeogo.doctor;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.works.adeogo.doctor.model.DoctorProfile;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class RegistrationActivity extends AppCompatActivity {

    private int pageNum;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference, mAllDatabaseReference;


    private final static String FRAGMENTA_TAG = "FRAGMENTA_TAG", FRAGMENTB_TAG = "FRAGMENTB_TAG", FRAGMENTC_TAG = "FRAGMENTC_TAG";

    private NavigationTabStrip mTopNavigationTabStrip;
    private ImageView mBackImageView, mForwardImageview;
    private FragmentManager mFragmentManager;

    private int state = 0;

    private  android.app.AlertDialog waitingDialog;
    private String mEmail, mPassword, mPhone, mName, mSpecial;

    private String mPhotoUrl, mCountry, mSpeciality, mCity;

    private int sunday = 0, monday = 0, tuesday = 0, wednesday = 0, thursday = 0, friday = 0, saturday = 0,
            online = 0, home = 0, office = 0, clinic = 0, sex = 0, startHour, startMinute, endHour, endMinute , finisher = 0;

    private  NavigationTabStrip navigationTabStrip;


    FragmentReg1 fragmentReg1;

    ProfileRegistrationFragment profileRegistrationFragment;


    GigRegisterFragment gigRegisterFragment;

    private TextView mPreviousTextView, mNextTextView, mContentTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mBackImageView = findViewById(R.id.backImageView);
        mForwardImageview = findViewById(R.id.forwardImageView);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mFragmentManager = getSupportFragmentManager();

        fragmentReg1 = new FragmentReg1();
        profileRegistrationFragment = new ProfileRegistrationFragment();
        gigRegisterFragment = new GigRegisterFragment();

        mFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragmentReg1, FRAGMENTA_TAG)
                .commit();

        mForwardImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (getData()){
                   state += 1;
                   setState();
               }
            }
        });

        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state -= 1;
                setState();
            }
        });

        navigationTabStrip = (NavigationTabStrip) findViewById(R.id.nav_view);
//        navigationTabStrip.setClickable(false);
        navigationTabStrip.setTabIndex(0, true);
//        navigationTabStrip.setTitleSize(0);
        navigationTabStrip.setTitles("", "", "");
        navigationTabStrip.setTitleSize( 3.0f);
        navigationTabStrip.setFocusable(false);
        navigationTabStrip.setFocusableInTouchMode(false);
        navigationTabStrip.setClickable(false);
        navigationTabStrip.setOnTabStripSelectedIndexListener(null);
        navigationTabStrip.setEnabled(false );
//        navigationTabStrip.setStripColor(Color.RED);
//        navigationTabStrip.setStripWeight(10);
//        navigationTabStrip.setStripFactor(6);
//        navigationTabStrip.setStripType(NavigationTabStrip.StripType.POINT);
//        navigationTabStrip.setStripGravity(NavigationTabStrip.StripGravity.TOP);
//        navigationTabStrip.setTypeface("fonts/typeface.ttf");
//        navigationTabStrip.setCornersRadius(3);
//        navigationTabStrip.setAnimationDuration(300);
//        navigationTabStrip.setInactiveColor(Color.GRAY);
//        navigationTabStrip.setActiveColor(Color.WHITE);
    }


    private void completeReg() {
            waitingDialog = new SpotsDialog(this);
            waitingDialog.show();

            //Register new user
            mFirebaseAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            mFirebaseAuth.signInWithEmailAndPassword(mEmail, mPassword)
                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {

                                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                                            if(user!=null) {
                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(mName).build();
                                                user.updateProfile(profileUpdates);
                                            }
//
//                                            waitingDialog.dismiss();
//                                            Intent intent = new Intent(RegistrationActivity.this, FirstActivity.class);
//                                            startActivity(intent);
//                                            finish();

                                            String userId = mFirebaseAuth.getCurrentUser().getUid();

                                            DoctorProfile doctorProfile = new DoctorProfile();
                                            doctorProfile.setDoctorId(userId);
                                            doctorProfile.setEmail(mEmail);
                                            doctorProfile.setPassword(mPassword);
                                            doctorProfile.setName(mName);
                                            doctorProfile.setDoctorPhoneNumber(mPhone);
                                            doctorProfile.setCountry(mCountry);
                                            doctorProfile.setSpeciality(mSpeciality);
                                            doctorProfile.setCity(mCity);
                                            doctorProfile.setPictureUrl(mPhotoUrl);
                                            doctorProfile.setConsultationFee(mSpecial);
                                            doctorProfile.setSunday(sunday);
                                            doctorProfile.setMonday(monday);
                                            doctorProfile.setTuesday(tuesday);
                                            doctorProfile.setWednesday(wednesday);
                                            doctorProfile.setThursday(thursday);
                                            doctorProfile.setFirday(friday);
                                            doctorProfile.setSaturday(saturday);
                                            doctorProfile.setStartHour(startHour);
                                            doctorProfile.setStartMinute(startMinute);
                                            doctorProfile.setEndHour(endHour);
                                            doctorProfile.setEndMinute(endMinute);
                                            doctorProfile.setOnlineConsult(online);
                                            doctorProfile.setHomeVisit(home);
                                            doctorProfile.setOfficeVisit(office);
                                            doctorProfile.setClinic(clinic);
                                            doctorProfile.setSex(sex);

                                            mDatabaseReference = mFirebaseDatabase.getReference().child("new_doctors/"  +userId + "/profile/profile");
                                            mAllDatabaseReference = mFirebaseDatabase.getReference().child("new_doctors/" + "all_profiles/" + userId );


                                            FirebaseMessaging.getInstance().subscribeToTopic(userId);

                                            mDatabaseReference.push().setValue(doctorProfile)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            finisher +=1;
                                                            onFinisher();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            waitingDialog.dismiss();
                                                            Toast.makeText(RegistrationActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                            mAllDatabaseReference.setValue(doctorProfile)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            finisher+=1;
                                                            onFinisher();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            waitingDialog.dismiss();
                                                            Toast.makeText(RegistrationActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    waitingDialog.dismiss();
                                    Toast.makeText(RegistrationActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegistrationActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

    }

    private void onFinisher(){
        if (finisher == 2){
            Toast.makeText(RegistrationActivity.this, "Registered successfully !!!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegistrationActivity.this, FirstActivity.class);
            waitingDialog.dismiss();
            finish();
            startActivity(intent);
        }
    }

    private boolean getData(){
        List<String> returne = null;

        switch (state){
            case 0:
              returne = fragmentReg1.getData();

              if (returne!=null){
                  if (returne.size()!= 0  ){
                      mEmail = returne.get(0);
                      mPassword = returne.get(1);
                      mName = returne.get(2);
                      mPhone = returne.get(3);

                  }
              }

                break;
            case 1:

                returne = profileRegistrationFragment.getData();

                if (returne!=null) {
                    if (returne.size() != 0 ) {
                        mPhotoUrl = returne.get(0);
                        mCountry = returne.get(1);
                        mSpeciality = returne.get(2);
                        mCity = returne.get(3);
                        sex = Integer.valueOf(returne.get(4));
                    }
                }
                break;

            case 2:
                List<Integer> integers = gigRegisterFragment.getData();

                if (integers!= null){
                    if (integers.size() != 0){
                        sunday = integers.get(0);
                        monday = integers.get(1);
                        tuesday = integers.get(2);
                        wednesday = integers.get(3);
                        thursday = integers.get(4);
                        friday = integers.get(5);
                        saturday = integers.get(6);

                        online = integers.get(7);
                        home = integers.get(8);
                        office = integers.get(9);
                        clinic = integers.get(10);

                        startHour = integers.get(11);
                        startMinute = integers.get(12);
                        endHour = integers.get(13);
                        endMinute = integers.get(14);

                        returne = new ArrayList<>();
                        returne.add("");
                        requirePermission();
                    }
                }


                break;
        }
        return returne != null;
    }

    private void setState(){

        switch (state){
            case 0:
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragmentReg1, FRAGMENTA_TAG)
                        .commit();
                mBackImageView.setVisibility(View.INVISIBLE);

                navigationTabStrip.setTabIndex(0, true);
                break;

            case 1:
                profileRegistrationFragment.testerd(mPhotoUrl);
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, profileRegistrationFragment, FRAGMENTB_TAG)
                        .commit();

                mBackImageView.setVisibility(View.VISIBLE);
                mForwardImageview.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_forward_black_24dp));

                navigationTabStrip.setTabIndex(1, true);
                break;
            case 2:
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, gigRegisterFragment, FRAGMENTC_TAG)
                        .commit();

                mForwardImageview.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_black_24dp));

                navigationTabStrip.setTabIndex(2, true);
                break;

        }
    }

    private void requirePermission(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Disclaimer");

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView layout = (TextView) inflater.inflate( R.layout.ask_doc, null);
        layout.setText(getResources().getString(R.string.disclaimer));

        builder.setView(layout);

        final AlertDialog dialog = builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                completeReg();
            }
        }).setNeutralButton("Decline", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);

            }
        }).create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.blueLogin));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blueLogin));
            }
        });

        dialog.show();
    }

}
