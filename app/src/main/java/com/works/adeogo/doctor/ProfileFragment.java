package com.works.adeogo.doctor;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.works.adeogo.doctor.model.DoctorProfile;
import com.works.adeogo.doctor.model.Notification;
import com.works.adeogo.doctor.utils.FirebaseUtils;

import org.w3c.dom.Text;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private ImageView mProfileImageView;
    private TextView mNameEditText, mHighestTextView, mLowestTextView, mSpecialTextView, mSpecialityTextView;
    private TextView mPhoneNumberEditText;

    private String urlFemale = "https://firebasestorage.googleapis.com/v0/b/dokita-c3d87.appspot.com/o/generic_photos%2Ficon_female.png?alt=media&token=eaecb6c4-5f7c-4bd6-ae8f-6a7a73e27a80";
    private String urlMale = "https://firebasestorage.googleapis.com/v0/b/dokita-c3d87.appspot.com/o/generic_photos%2Ficon_male.png?alt=media&token=e4530bf8-4f41-495d-885e-e2c73007b395";


    public static final int RC_SIGN_IN = 1;
    private static final int RC_PHOTO_PICKER = 2;

    private String userId;
    public static final String ANONYMOUS = "anonymous";
    private String mUsername;

    private String mPictureUrl = urlMale;
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


    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private StorageReference mProfilePhotosStorageReference;
    private DatabaseReference mDoctorProfileDatabaseReference;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        mHighestTextView = rootView.findViewById(R.id.tVHighestCost);
        mLowestTextView = rootView.findViewById(R.id.tVLowestCost);
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

        Picasso.with(getContext())
                .load(mPictureUrl)
                .resize(500, 500)
                .centerCrop()
                .into(mProfileImageView);

        mProfilePhotosStorageReference = mFirebaseStorage.getReference().child("profile_photos");


        return rootView;
    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
//            Uri selectedImageUri = data.getData();
//
//            // Get a reference to store file at chat_photos/<FILENAME>
//            StorageReference photoRef = mProfilePhotosStorageReference.child(selectedImageUri.getLastPathSegment());
//
//            // Upload file to Firebase Storage
//            photoRef.putFile(selectedImageUri)
//                    .addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            // When the image has successfully uploaded, we get its download URL
//                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
//
//                            // Set the download URL to the message box, so that the user can send it to the database
//                            DoctorProfile doctorProfile = new DoctorProfile(mDoctorId, mEmail, mPassword, mDoctorName, mPhoneNumber, downloadUrl.toString(), mCountry, mCity, mSpeciality);
//                            mDoctorProfileDatabaseReference.push().setValue(doctorProfile);
//                        }
//                    });
//        }
//    }

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

                    mLowest = doctorProfile.getLowestCost();
                    mLowest = " $" + mLowest;
                    mHighest = doctorProfile.getHighestCost();
                    mHighest = " $" + mHighest;
                    mSpecial = doctorProfile.getSpecialCost();
                    mSpecial = " $" + mSpecial;

                    mSpecialityTextView.setText(mSpeciality);
                    mLowestTextView.setText(mLowest);
                    mHighestTextView.setText(mHighest);
                    mSpecialTextView.setText(mSpecial);


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


}
