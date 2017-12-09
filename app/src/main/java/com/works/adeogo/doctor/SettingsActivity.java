package com.works.adeogo.doctor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.rengwuxian.materialedittext.MaterialEditText;
import com.works.adeogo.doctor.model.DoctorProfile;
import com.works.adeogo.doctor.model.Notification;
import com.works.adeogo.doctor.utils.FirebaseUtils;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingsActivity extends AppCompatActivity {

    private MaterialEditText mNameMaterialEditText;
    private MaterialEditText mPhoneNumberMaterialEditText;
    private MaterialEditText mLowestMaterialEditText;
    private MaterialEditText mHighestMaterialEditText;
    private MaterialEditText mSpecialMaterialEditText;

    public static final String ANONYMOUS = "anonymous";
    private String mUsername;
    private String userId;

    private DoctorProfile mDoctorProfile;

    private String mPictureUrl = "";
    private String mPhoneNumber = "";
    private String mDoctorName = "";
    private String mEmail = "";
    private String mPassword = "";
    private String mCountry = "";
    private String mCity = "";
    private String mSpeciality = "";
    private String mDoctorId = "";
    private String mLowest = "";
    private String mHighest = "";
    private String mSpecial = "";


    private android.app.AlertDialog mWaitingDialog;

    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAuth mFirebaseAuth;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mProfileDatabaseReference;
    private ChildEventListener mChildEventListener;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/open_sans_semibold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_settings);

        mWaitingDialog = new SpotsDialog(this);
        mWaitingDialog.show();
        mNameMaterialEditText = findViewById(R.id.edtSettingsName);
        mPhoneNumberMaterialEditText = findViewById(R.id.edtSettingsNumber);
        mLowestMaterialEditText = findViewById(R.id.edtSettingsLow);
        mHighestMaterialEditText = findViewById(R.id.edtSettingsHighest);
        mSpecialMaterialEditText = findViewById(R.id.edtSettingsSpecial);

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
                    onSignedInInitialize(user.getDisplayName());
                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    Intent intent = new Intent(SettingsActivity.this, ActivityRegister.class);
                    startActivity(intent);
                }
            }
        };

    }

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
                    mLowest = doctorProfile.getLowestCost();
                    mHighest = doctorProfile.getHighestCost();
                    mSpecial = doctorProfile.getSpecialCost();

                    mNameMaterialEditText.setText(mDoctorName);
                    mPhoneNumberMaterialEditText.setText(mPhoneNumber);
                    mLowestMaterialEditText.setText(mLowest);
                    mHighestMaterialEditText.setText(mHighest);
                    mSpecialMaterialEditText.setText(mSpecial);

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
                    || TextUtils.isEmpty(mLowestMaterialEditText.getText().toString()) || TextUtils.isEmpty(mHighestMaterialEditText.getText().toString())
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

                if (TextUtils.isEmpty(mLowestMaterialEditText.getText().toString()))
                {
                    Toast.makeText(this, "Enter your updated lowest charge!", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(mHighestMaterialEditText.getText().toString()))
                {
                    Toast.makeText(this, "Enter your updated highest charge!", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(mSpecialMaterialEditText.getText().toString()))
                {
                    Toast.makeText(this, "Enter your updated special charge!", Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            mDoctorName = mNameMaterialEditText.getText().toString();
            mPhoneNumber = mPhoneNumberMaterialEditText.getText().toString();
            mLowest = mLowestMaterialEditText.getText().toString();
            mHighest = mHighestMaterialEditText.getText().toString();
            mSpecial = mSpecialMaterialEditText.getText().toString();

            doctorProfile.setName(mDoctorName);
            doctorProfile.setDoctorPhoneNumber(mPhoneNumber);
            doctorProfile.setLowestCost(mLowest);
            doctorProfile.setHighestCost(mHighest);
            doctorProfile.setSpecialCost(mSpecial);
            doctorProfile.setPictureUrl(mPictureUrl);
            doctorProfile.setPassword(mPassword);
            doctorProfile.setSpeciality(mSpeciality);
            doctorProfile.setDoctorId(mDoctorId);
            doctorProfile.setCity(mCity);
            doctorProfile.setCountry(mCountry);
            doctorProfile.setEmail(mEmail);

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