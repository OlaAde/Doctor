package com.works.adeogo.doctor;


import android.app.Activity;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.works.adeogo.doctor.model.DoctorProfile;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class GigRegisterFragment extends Fragment implements BlockingStep {


    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mAllDatabaseReference;

    private String mEmail, mPassword, mPhone, mName, mMax, mLow, mSpecial;

    private MaterialEditText mHighestMaterialEditText, mLowestMaterialEditText, mSpecialMaterialEditText;

    private String mPhotoUrl ;
    private String mCountry;
    private String mSpeciality;
    private String mCity;

    private Context mContext;

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

        mHighestMaterialEditText = rootView.findViewById(R.id.edtMax);
        mLowestMaterialEditText = rootView.findViewById(R.id.edtLow);
        mSpecialMaterialEditText = rootView.findViewById(R.id.edtSpecial);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mContext = getActivity();

        return rootView;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

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

    private void lastNext() {
        final android.app.AlertDialog waitingDialog = new SpotsDialog(getActivity());
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
                                        waitingDialog.dismiss();
                                        Intent intent = new Intent(activity, MainActivity.class);
                                        startActivity(intent);
                                        activity.finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                waitingDialog.dismiss();
                                Toast.makeText(activity, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

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
                        doctorProfile.setHighestCost(mMax);
                        doctorProfile.setLowestCost(mLow);
                        doctorProfile.setSpecialCost(mSpecial);


                        mDatabaseReference = mFirebaseDatabase.getReference().child("new_doctors/"  +userId + "/profile/profile");
                        mAllDatabaseReference = mFirebaseDatabase.getReference().child("new_doctors/" + "all_profiles/" + userId );
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notifications");
                        databaseReference.child("token").setValue(FirebaseInstanceId.getInstance().getToken());

                        mDatabaseReference.push().setValue(doctorProfile)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(mContext, "Registered successfully !!!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(activity, MainActivity.class);
                                        if (mContext != null){
                                            startActivity(intent);
                                        }else {
                                            Intent intent1 = new Intent(activity, MainActivity.class);
                                            startActivity(intent1);
                                        }

                                        waitingDialog.dismiss();
                                        activity.finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        waitingDialog.dismiss();
                                        Toast.makeText(activity, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                        mAllDatabaseReference.push().setValue(doctorProfile)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(mContext, "Registered successfully !!!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(activity, MainActivity.class);
                                        startActivity(intent);
                                        waitingDialog.dismiss();
                                        activity.finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        waitingDialog.dismiss();
                                        Toast.makeText(activity, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onNextClicked(final StepperLayout.OnNextClickedCallback callback) {

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        if (!check())
            return;
        lastNext();

    }


    private boolean check() {
        if (TextUtils.isEmpty(mHighestMaterialEditText.getText().toString())){
//
            Toast.makeText(getActivity(), "Please enter your highest fee", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mLowestMaterialEditText.getText().toString())){
            Toast.makeText(getActivity(), "Please enter your lowest fee", Toast.LENGTH_SHORT).show();

            return false;
        }

        if (TextUtils.isEmpty(mSpecialMaterialEditText.getText().toString())){
            Toast.makeText(getActivity(), "Please enter your special consultation fee", Toast.LENGTH_SHORT).show();

            return false;
        }

        mMax = mHighestMaterialEditText.getText().toString();
        mLow = mLowestMaterialEditText.getText().toString();
        mSpecial = mSpecialMaterialEditText.getText().toString();

        return true;
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

    }



}
