package com.works.adeogo.doctor;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.support.v4.app.ActivityCompat;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.philliphsu.numberpadtimepicker.NumberPadTimePicker;
import com.philliphsu.numberpadtimepicker.NumberPadTimePickerDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.works.adeogo.doctor.model.DoctorProfile;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class  GigRegisterFragment extends Fragment{


    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mAllDatabaseReference;

    private String mEmail, mPassword, mPhone, mName, mSpecial;

    private MaterialEditText  mConsultationFeeEditText;

    private String mPhotoUrl, mCountry, mSpeciality, mCity;

    private int startHour, startMinute, endHour, endMinute;

    private int sunday = 0, monday = 0, tuesday = 0, wednesday = 0, thursday = 0, friday = 0, saturday = 0, online = 0, home = 0, office = 0, clinic = 0, sex = 0;

    private Context mContext;

    private TextView mStartTimeTextView, mEndTimeTextView;

    private TextView mSundayDateTextView, mMondayDateTextView, mTuesdayDateTextView, mWednesdayDateTextView, mThursdayDateTextView, mFridayDateTextView, mSaturdayDateTextView,
            mOnlineTextView, mHomeTextView, mOfficeTextView, mClinicTextView;

    private LinearLayout mRootLayout;

    Activity a;
    Activity activity;
    public GigRegisterFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            a = (Activity) context;
        }
        try {
            activity = getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gig_register, container, false);

        mRootLayout = rootView.findViewById(R.id.registerRoot);


        mConsultationFeeEditText = rootView.findViewById(R.id.edtSpecial);
        mStartTimeTextView = rootView.findViewById(R.id.startTime);
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

        mFirebaseAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mContext = getActivity();

        mStartTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NumberPadTimePickerDialog timePicker = new NumberPadTimePickerDialog(getActivity(), mStartTimeListener, true);
                timePicker.show();
            }
        });

        mEndTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NumberPadTimePickerDialog timePicker = new NumberPadTimePickerDialog(getActivity(), mEndTimeListener, true);
                timePicker.show();
            }
        });

        updateDateColor();
        updateVenueColor();
        setDayClickListener();
        setVenueClickListener();

        return rootView;
    }

//    @Nullable
//    @Override
//    public VerificationError verifyStep() {
//        return null;
//    }
//
//    @Override
//    public void onSelected() {
//
//    }

    @SuppressLint("NewApi")
    private void updateDateColor(){

        if (sunday == 1){
            mSundayDateTextView.setBackground(getActivity().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (sunday == 0){
            mSundayDateTextView.setBackground(getActivity().getDrawable(R.drawable.curved_button_background));
        }

        if (monday == 1){
            mMondayDateTextView.setBackground(getActivity().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (monday == 0){
            mMondayDateTextView.setBackground(getActivity().getDrawable(R.drawable.curved_button_background));
        }


        if (tuesday == 1){
            mTuesdayDateTextView.setBackground(getActivity().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (tuesday == 0){
            mTuesdayDateTextView.setBackground(getActivity().getDrawable(R.drawable.curved_button_background));
        }

        if (wednesday == 1){
            mWednesdayDateTextView.setBackground(getActivity().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (wednesday == 0){
            mWednesdayDateTextView.setBackground(getActivity().getDrawable(R.drawable.curved_button_background));
        }


        if (thursday == 1){
            mThursdayDateTextView.setBackground(getActivity().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (thursday == 0){
            mThursdayDateTextView.setBackground(getActivity().getDrawable(R.drawable.curved_button_background));
        }




        if (friday == 1){
            mFridayDateTextView.setBackground(getActivity().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (friday == 0){
            mFridayDateTextView.setBackground(getActivity().getDrawable(R.drawable.curved_button_background));
        }

        if (saturday == 1){
            mSaturdayDateTextView.setBackground(getActivity().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (saturday == 0){
            mSaturdayDateTextView.setBackground(getActivity().getDrawable(R.drawable.curved_button_background));
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

    @SuppressLint("NewApi")
    private void updateVenueColor(){

        if (online == 1){
            mOnlineTextView.setBackground(getActivity().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (online == 0){
            mOnlineTextView.setBackground(getActivity().getDrawable(R.drawable.curved_button_background));

        }

        if (home == 1){
            mHomeTextView.setBackground(getActivity().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (home == 0){
            mHomeTextView.setBackground(getActivity().getDrawable(R.drawable.curved_button_background));
        }


        if (office == 1){
            mOfficeTextView.setBackground(getActivity().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (office == 0){
            mOfficeTextView.setBackground(getActivity().getDrawable(R.drawable.curved_button_background));
        }

        if (clinic == 1){
            mClinicTextView.setBackground(getActivity().getDrawable(R.drawable.curved_button_background_clicked));
        }else if (clinic == 0){
            mClinicTextView.setBackground(getActivity().getDrawable(R.drawable.curved_button_background));
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


    private TimePickerDialog.OnTimeSetListener mStartTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            startHour = hourOfDay;
            startMinute = minute;
            mStartTimeTextView.setText(startHour+":"+startMinute);
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
//
//    @Override
//    public void onError(@NonNull VerificationError error) {
//
//    }

    protected void displayReceivedData1(String email, String password, String name, String phone, String photoUrl, String country, String speciality, String city, Context context )
    {
        mContext = context;
        mEmail = email;
        mPassword = password;
        mName = name;
        mPhone = phone;
        mPhotoUrl = photoUrl;
        mCountry = country;
        mSpeciality = speciality;
        mCity = city;
    }


    public List<Integer> getData(){

        if (!check()){
            return null;
        }else {

            List result = new ArrayList<String>();

            result.add(sunday);
            result.add(monday);
            result.add(tuesday);
            result.add(wednesday);
            result.add(thursday);
            result.add(friday);
            result.add(saturday);

            result.add(online);
            result.add(home);
            result.add(office);
            result.add(clinic);

            result.add(startHour);
            result.add(startMinute);
            result.add(endHour);
            result.add(endMinute);

            return result;

        }
    }


    private boolean check() {

        if (TextUtils.isEmpty(mConsultationFeeEditText.getText().toString())){

            Toast.makeText(getActivity(), "Please enter your consultation fee", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (sunday == 0 & monday == 0 & tuesday == 0 & wednesday == 0 & thursday == 0 & friday == 0 & saturday == 0 ){
            Toast.makeText(getActivity(), "You have to pick at least a day", Toast.LENGTH_SHORT).show();

            return false;
        }

        if (online == 0 & home == 0 & office == 0 & clinic == 0){
            Toast.makeText(getActivity(), "You have to pick at least a venue", Toast.LENGTH_SHORT).show();

            return false;
        }

        mSpecial = mConsultationFeeEditText.getText().toString();

        return true;
    }

}
