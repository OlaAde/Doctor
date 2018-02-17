package com.works.adeogo.doctor;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.philliphsu.numberpadtimepicker.NumberPadTimePickerDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.works.adeogo.doctor.model.DoctorProfile;
import com.works.adeogo.doctor.model.Notification;
import com.works.adeogo.doctor.utils.FirebaseUtils;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingsActivity extends AppCompatActivity {

    private MaterialEditText mNameMaterialEditText, mPhoneNumberMaterialEditText, mSpecialMaterialEditText;

    private TextView mSundayDateTextView, mMondayDateTextView, mTuesdayDateTextView, mWednesdayDateTextView, mThursdayDateTextView, mFridayDateTextView, mSaturdayDateTextView, mStartTimeTextView, mEndTimeTextView,
            mOnlineTextView, mHomeTextView, mOfficeTextView, mClinicTextView;

    public static final String ANONYMOUS = "anonymous";
    private String mUsername, userId;

    private DoctorProfile mDoctorProfile;

    private String mPictureUrl = "",  mPhoneNumber = "", mDoctorName = "", mEmail = "", mPassword = "", mCountry = "", mCity = "", mSpeciality = "", mDoctorId = "", mSpecial = "";
    private int sunday = 0, monday = 0, tuesday = 0, wednesday = 0, thursday = 0, friday = 0, saturday = 0, startHour, startMinute, endHour, endMinute, online = 0, home = 0, office = 0, clinic = 0;

    private android.app.AlertDialog mWaitingDialog;

    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAuth mFirebaseAuth;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mProfileDatabaseReference, mAllProfileReference;
    private ChildEventListener mChildEventListener;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_settings);

        mWaitingDialog = new SpotsDialog(this);
        mWaitingDialog.show();
        mNameMaterialEditText = findViewById(R.id.edtSettingsName);
        mPhoneNumberMaterialEditText = findViewById(R.id.edtSettingsNumber);

        mSpecialMaterialEditText = findViewById(R.id.edtSettingsSpecial);

        mSundayDateTextView = findViewById(R.id.sunday);
        mMondayDateTextView = findViewById(R.id.monday);
        mTuesdayDateTextView = findViewById(R.id.tuesday);
        mWednesdayDateTextView = findViewById(R.id.wednesday);
        mThursdayDateTextView = findViewById(R.id.thursday);
        mFridayDateTextView = findViewById(R.id.friday);
        mSaturdayDateTextView = findViewById(R.id.saturday);

        mOnlineTextView = findViewById(R.id.online);
        mHomeTextView = findViewById(R.id.home);
        mOfficeTextView = findViewById(R.id.office);
        mClinicTextView = findViewById(R.id.clinic);

        mStartTimeTextView = findViewById(R.id.startTime);
        mEndTimeTextView = findViewById(R.id.endTime);

        mStartTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NumberPadTimePickerDialog timePicker = new NumberPadTimePickerDialog(SettingsActivity.this, mStartTimeListener, true);
                timePicker.show();
            }
        });

        mEndTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NumberPadTimePickerDialog timePicker = new NumberPadTimePickerDialog(SettingsActivity.this, mStartTimeListener, true);
                timePicker.show();
            }
        });

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    userId = user.getUid();
                    FirebaseMessaging.getInstance().subscribeToTopic(userId);
                    mProfileDatabaseReference = mFirebaseDatabase.getReference().child("new_doctors").child(userId).child("profile").child("profile");
                    mAllProfileReference = mFirebaseDatabase.getReference().child("new_doctors/" + "all_profiles/" + userId );

                    onSignedInInitialize(user.getDisplayName());
                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    Intent intent = new Intent(SettingsActivity.this, ActivityRegister.class);
                    startActivity(intent);
                }
            }
        };

        setDayClickListener();
        setVenueClickListener();
    }

    private TimePickerDialog.OnTimeSetListener mStartTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            startHour = hourOfDay;
            startMinute = minute;
            mStartTimeTextView.setText(startHour+":"+startMinute + "  -  ");
        }
    };

    private TimePickerDialog.OnTimeSetListener mEndTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            endHour = hourOfDay;
            endMinute = minute;
            mEndTimeTextView.setText(endHour+":"+endMinute);
        }
    };


    private void onSignedInInitialize(String username) {
        mUsername = username;
        attachDatabaseReadListener();
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
                    mDoctorProfile = doctorProfile;

                    mWaitingDialog.dismiss();

                    mDoctorName = doctorProfile.getName();
                    mPictureUrl = doctorProfile.getPictureUrl();
                    mPhoneNumber = doctorProfile.getDoctorPhoneNumber();
                    mEmail = doctorProfile.getEmail();
                    mPassword = doctorProfile.getPassword();
                    mCountry = doctorProfile.getCountry();
                    mCity = doctorProfile.getCity();
                    mSpeciality = doctorProfile.getSpeciality();
                    mDoctorId = doctorProfile.getDoctorId();
                    mSpecial = doctorProfile.getConsultationFee();

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


                    mNameMaterialEditText.setText(mDoctorName);
                    mPhoneNumberMaterialEditText.setText(mPhoneNumber);
                    mSpecialMaterialEditText.setText(mSpecial);

                    mStartTimeTextView.setText(startHour+":"+startMinute  + "  -  ");
                    mEndTimeTextView.setText(endHour+":"+endMinute);

                    updateDateColor();
                    updateVenueColor();


                    if (startMinute < 10){
                        mStartTimeTextView.setText(startHour+":0"+startMinute  + "  -  ");
                    }else mEndTimeTextView.setText(startHour+":"+startMinute  + "  -  ");

                    if (endMinute < 10){
                        mEndTimeTextView.setText(endHour+":0"+endMinute);
                    }else
                        mEndTimeTextView.setText(endHour+":"+endMinute);

                }
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mProfileDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mProfileDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    private void setDayClickListener(){
        mSundayDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sunday == 0){
                    sunday = 1;
                }else if (sunday == 1){
                    sunday = 0;
                }

                updateDateColor();
            }
        });

        mMondayDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (monday == 0){
                    monday = 1;
                }else if (monday == 1){
                    monday = 0;
                }
                updateDateColor();
            }
        });

        mTuesdayDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tuesday == 0){
                    tuesday = 1;
                }else if (tuesday == 1){
                    tuesday = 0;
                }

                updateDateColor();
            }
        });

        mWednesdayDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wednesday == 0){
                    wednesday = 1;
                }else if (wednesday == 1){
                    wednesday = 0;
                }

                updateDateColor();
            }
        });

        mThursdayDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (thursday == 0){
                    thursday = 1;
                }else if (thursday == 1){
                    thursday = 0;
                }

                updateDateColor();
            }
        });

        mFridayDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (friday == 0){
                    friday = 1;
                }else if (friday == 1){
                    friday = 0;
                }

                updateDateColor();
            }
        });

        mSaturdayDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saturday == 0){
                    saturday = 1;
                }else if (saturday == 1){
                    saturday = 0;
                }

                updateDateColor();
            }
        });
    }

    private void updateDateColor(){

                if (sunday == 1){
                    mSundayDateTextView.setBackground(SettingsActivity.this.getResources().getDrawable(R.drawable.curved_button_background_clicked));
                }else if (sunday == 0){
                    mSundayDateTextView.setBackground(SettingsActivity.this.getResources().getDrawable(R.drawable.curved_button_background));
                }

                if (monday == 1){
                    mMondayDateTextView.setBackground(SettingsActivity.this.getResources().getDrawable(R.drawable.curved_button_background_clicked));
                }else if (monday == 0){
                    mMondayDateTextView.setBackground(SettingsActivity.this.getResources().getDrawable(R.drawable.curved_button_background));
                }


                if (tuesday == 1){
                    mTuesdayDateTextView.setBackground(SettingsActivity.this.getResources().getDrawable(R.drawable.curved_button_background_clicked));
                }else if (tuesday == 0){
                    mTuesdayDateTextView.setBackground(SettingsActivity.this.getResources().getDrawable(R.drawable.curved_button_background));
                }

                if (wednesday == 1){
                    mWednesdayDateTextView.setBackground(SettingsActivity.this.getResources().getDrawable(R.drawable.curved_button_background_clicked));
                }else if (wednesday == 0){
                    mWednesdayDateTextView.setBackground(SettingsActivity.this.getResources().getDrawable(R.drawable.curved_button_background));
                }


                if (thursday == 1){
                    mThursdayDateTextView.setBackground(SettingsActivity.this.getResources().getDrawable(R.drawable.curved_button_background_clicked));
                }else if (thursday == 0){
                    mThursdayDateTextView.setBackground(SettingsActivity.this.getResources().getDrawable(R.drawable.curved_button_background));
                }




                if (friday == 1){
                    mFridayDateTextView.setBackground(SettingsActivity.this.getResources().getDrawable(R.drawable.curved_button_background_clicked));
                }else if (friday == 0){
                    mFridayDateTextView.setBackground(SettingsActivity.this.getResources().getDrawable(R.drawable.curved_button_background));
                }

                if (saturday == 1){
                    mSaturdayDateTextView.setBackground(SettingsActivity.this.getResources().getDrawable(R.drawable.curved_button_background_clicked));
                }else if (saturday == 0){
                    mSaturdayDateTextView.setBackground(SettingsActivity.this.getResources().getDrawable(R.drawable.curved_button_background));
                }
    }

    private void updateVenueColor(){

        if (online == 1){
            mOnlineTextView.setBackground(SettingsActivity.this.getResources().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (online == 0){
            mOnlineTextView.setBackground(SettingsActivity.this.getResources().getDrawable(R.drawable.curved_button_background));
        }

        if (home == 1){
            mHomeTextView.setBackground(SettingsActivity.this.getResources().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (home == 0){
            mHomeTextView.setBackground(SettingsActivity.this.getResources().getDrawable(R.drawable.curved_button_background));
        }


        if (office == 1){
            mOfficeTextView.setBackground(SettingsActivity.this.getResources().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (office == 0){
            mOfficeTextView.setBackground(SettingsActivity.this.getResources().getDrawable(R.drawable.curved_button_background));
        }

        if (clinic == 1){
            mClinicTextView.setBackground(SettingsActivity.this.getResources().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (clinic == 0){
            mClinicTextView.setBackground(SettingsActivity.this.getResources().getDrawable(R.drawable.curved_button_background));
        }


    }

    private void setVenueClickListener(){
        mOnlineTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (online == 0){
                    online = 1;
                }else if (online == 1){
                    online = 0;
                }

                updateVenueColor();
            }
        });

        mHomeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (home == 0){
                    home = 1;
                }else if (home == 1){
                    home = 0;
                }
                updateVenueColor();
            }
        });

        mOfficeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (office == 0){
                    office = 1;
                }else if (office == 1){
                    office = 0;
                }

                updateVenueColor();
            }
        });

        mClinicTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clinic == 0){
                    clinic = 1;
                }else if (clinic == 1){
                    clinic = 0;
                }

                updateVenueColor();
            }
        });

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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            mWaitingDialog.show();
            DoctorProfile doctorProfile = new DoctorProfile();
            if (TextUtils.isEmpty(mNameMaterialEditText.getText().toString()) || TextUtils.isEmpty(mPhoneNumberMaterialEditText.getText().toString())
                    || TextUtils.isEmpty(mSpecialMaterialEditText.getText().toString()))
            {
                if (TextUtils.isEmpty(mNameMaterialEditText.getText().toString()))
                {
                    Toast.makeText(this, "Enter your updated name!", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(mPhoneNumberMaterialEditText.getText().toString()))
                {
                    Toast.makeText(this, "Enter your updated phone number!", Toast.LENGTH_SHORT).show();
                }


                if (TextUtils.isEmpty(mSpecialMaterialEditText.getText().toString()))
                {
                    Toast.makeText(this, "Enter your Consultation Fee!", Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            if (sunday == 0 & monday == 0 & tuesday == 0 & wednesday == 0 & thursday == 0 & friday == 0 & saturday == 0 ){
                Toast.makeText(this, "You have to pick at least a day", Toast.LENGTH_SHORT).show();

                return false;
            }

            mDoctorName = mNameMaterialEditText.getText().toString();
            mPhoneNumber = mPhoneNumberMaterialEditText.getText().toString();
            mSpecial = mSpecialMaterialEditText.getText().toString();

            doctorProfile.setName(mDoctorName);
            doctorProfile.setDoctorPhoneNumber(mPhoneNumber);
            doctorProfile.setConsultationFee(mSpecial);
            doctorProfile.setPictureUrl(mPictureUrl);
            doctorProfile.setPassword(mPassword);
            doctorProfile.setSpeciality(mSpeciality);
            doctorProfile.setDoctorId(mDoctorId);
            doctorProfile.setCity(mCity);
            doctorProfile.setCountry(mCountry);
            doctorProfile.setEmail(mEmail);
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


            mAllProfileReference.setValue(doctorProfile);

            mProfileDatabaseReference.push().setValue(doctorProfile).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    if(user!=null) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(mDoctorName).build();
                        user.updateProfile(profileUpdates);
                    }
                    mWaitingDialog.dismiss();
                    finish();
                    Toast.makeText(SettingsActivity.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}