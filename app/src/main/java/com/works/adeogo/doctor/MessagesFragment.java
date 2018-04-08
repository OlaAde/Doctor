package com.works.adeogo.doctor;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.works.adeogo.doctor.adapters.ListAdapter;
import com.works.adeogo.doctor.model.ChatHead;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment implements ListAdapter.ListAdapterOnclickHandler {

    private String userId;
    public static final String ANONYMOUS = "anonymous";
    private String mUsername;

    private ImageView mPatientsImageView, mDoctorsImageView;
    private TextView mNoResults, mLabelDoctor, mLabelPatients;
    private ListAdapter mPatientListAdapter, mDoctorListAdapter;
    private LinearLayoutManager mPatientManager, mDoctorManager;
    private RecyclerView mPatientRecyclerView, mDoctorRecyclerView;
    private ProgressBar mProgressBar;

    public static final int RC_SIGN_IN = 1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private ChildEventListener mPatientChildEventListener, mDoctorChildEventListener;
    private Query patientChatHeadQuery, doctorChatHeadQuery;
    private DatabaseReference mConnectedRef;
    private ValueEventListener mValueEventListener;
    private RelativeLayout mPatientsRelativeLayout, mDoctorsRelativeLayout;

    private boolean isPatientOpen = true;
    private boolean isDoctorOpen = true;

    private int mState = 0;

    private List<ChatHead> mPatientsChatList = new ArrayList<>(),  mDoctorsChatList = new ArrayList<>();
    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);

        mDoctorsRelativeLayout = rootView.findViewById(R.id.doctorsLabelRL );
        mPatientsRelativeLayout = rootView.findViewById(R.id.patientsLabelRL);

        mPatientsImageView = rootView.findViewById(R.id.droppishPatientsImageView);
        mDoctorsImageView = rootView.findViewById(R.id.droppishDoctorsImageView);

        mLabelPatients = rootView.findViewById(R.id.clientMessagesLabel);
        mLabelDoctor = rootView.findViewById(R.id.doctorsMessagesLabel);

        mDoctorRecyclerView = rootView.findViewById(R.id.doctors_recycler_view);
        mPatientRecyclerView = rootView.findViewById(R.id.patients_recycler_view);
        mNoResults = rootView.findViewById(R.id.tvNoResultMessages);
        mProgressBar = rootView.findViewById(R.id.progressBar);

        mPatientListAdapter = new ListAdapter(getContext(), this);
        mDoctorListAdapter = new ListAdapter(getContext(), this);

        mPatientManager = new LinearLayoutManager(getContext());
        mDoctorManager = new LinearLayoutManager(getContext());

        mPatientManager.setStackFromEnd(true);
        mPatientManager.setReverseLayout(true);

        mDoctorManager.setStackFromEnd(true);
        mDoctorManager.setReverseLayout(true);


        mPatientRecyclerView.setAdapter(mPatientListAdapter);
        mPatientRecyclerView.setLayoutManager(mPatientManager);

        mDoctorRecyclerView.setAdapter(mDoctorListAdapter);
        mDoctorRecyclerView.setLayoutManager(mDoctorManager);

        mPatientListAdapter.swapData(mPatientsChatList);

        mDoctorListAdapter.swapData(mDoctorsChatList);

        mDoctorsRelativeLayout.setVisibility(View.GONE);
        mPatientsRelativeLayout.setVisibility(View.GONE);

        //firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mPatientsRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPatientsRelativeLayout.getVisibility() == View.VISIBLE){
                    changeImageVisibility(0);
                }
            }
        });

        mDoctorsRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mDoctorsRelativeLayout.getVisibility() == View.VISIBLE){
                    changeImageVisibility(1);
                }
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    userId = user.getUid();

                    patientChatHeadQuery = mFirebaseDatabase.getReference().child("questions").child("doctors").child(userId).child("chat_head").child("clients").orderByChild("unixTime");
                    doctorChatHeadQuery = mFirebaseDatabase.getReference().child("questions").child("doctors").child(userId).child("chat_head").child("doctors").orderByChild("unixTime");

                    onSignedInInitialize(user.getDisplayName());
                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
        return rootView;
    }

    private void changeImageVisibility(int check){
        if (check == 0){
            if (isPatientOpen){
                isPatientOpen = false;
                mPatientsRelativeLayout.setBackground(getResources().getDrawable(R.drawable.patient_ripple_bkg));
                mPatientsImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
                mPatientRecyclerView.setVisibility(View.GONE);

                mLabelPatients.setTextColor(getResources().getColor(R.color.black));
            }else{
                isPatientOpen = true;
                mPatientsImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
                mPatientsRelativeLayout.setBackground(getResources().getDrawable(R.drawable.patient_ripple_bkg_before));
                mPatientRecyclerView.setVisibility(View.VISIBLE);

                mLabelPatients.setTextColor(getResources().getColor(R.color.white));
            }
        }else if(check == 1){
            if (isDoctorOpen){
                isDoctorOpen = false;
                mDoctorsImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
                mDoctorRecyclerView.setVisibility(View.GONE);
                mDoctorsRelativeLayout.setBackground(getResources().getDrawable(R.drawable.ripple_bkg));

                mLabelDoctor.setTextColor(getResources().getColor(R.color.black));

            }else {
                isDoctorOpen = true;
                mDoctorsImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
                mDoctorsRelativeLayout.setBackground(getResources().getDrawable(R.drawable.ripple_bkg_before));
                mDoctorRecyclerView.setVisibility(View.VISIBLE);

                mLabelDoctor.setTextColor(getResources().getColor(R.color.white));
            }
        }
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
        patientChatHeadQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                   mState = 2;
                   onStatusChange();
                }else {
                   onStatusChange();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        doctorChatHeadQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mState = 3;
                    onStatusChange();
                }else {
                   onStatusChange();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (mPatientChildEventListener == null) {
            mPatientChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    mNoResults.setVisibility(View.GONE);
                    ChatHead chatHead =  dataSnapshot.getValue(ChatHead.class);
                    if (chatHead!= null ){
                        boolean check = true;
                        for(int i = 0; i < mPatientsChatList.size(); i++){
                            if (TextUtils.equals(chatHead.getUserId(), mPatientsChatList.get(i).getUserId() )){
                                check = false;
                            }
                        }
                        if (check){
                            mPatientsChatList.add(chatHead);
                        }
                    }
                    if (chatHead != null){
                        mPatientListAdapter.swapData(null);
                        mPatientListAdapter.notifyDataSetChanged();
                        mPatientListAdapter.swapData(mPatientsChatList);
                        mPatientListAdapter.notifyDataSetChanged();
                    }
                    mPatientListAdapter.swapData(mPatientsChatList);
                }
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };

            patientChatHeadQuery.addChildEventListener(mPatientChildEventListener);
        }

        if (mDoctorChildEventListener == null) {
            mDoctorChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    mNoResults.setVisibility(View.GONE);
                    ChatHead chatHead =  dataSnapshot.getValue(ChatHead.class);
                    if (chatHead!= null ){
                        boolean check = true;
                        for(int i = 0; i < mDoctorsChatList.size(); i++){
                            if (TextUtils.equals(chatHead.getUserId(), mDoctorsChatList.get(i).getUserId() )){
                                check = false;
                            }
                        }
                        if (check){
                            mDoctorsChatList.add(chatHead);
                        }
                    }
                    if (chatHead != null){
                        mDoctorListAdapter.swapData(null);
                        mDoctorListAdapter.notifyDataSetChanged();
                        mDoctorListAdapter.swapData(mDoctorsChatList);
                        mDoctorListAdapter.notifyDataSetChanged();
                    }
                    mDoctorListAdapter.swapData(mDoctorsChatList);
                }
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };

            doctorChatHeadQuery.addChildEventListener(mDoctorChildEventListener);
        }


        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {

//                    Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();
//                    Log.e("Tagged", "Connected");
//                    DatabaseReference con = myConnectionsRef.push();
//
//                    // when this device disconnects, remove it
//                    con.onDisconnect().removeValue();
//
//                    // when I disconnect, update the last time I was seen online
//                    lastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP);
//
//                    // add this device to my connections list
//                    // this value could contain info about the device or a timestamp too
//                    con.setValue(Boolean.TRUE);
                }else {
//                    Toast.makeText(getActivity(), "Disconnected", Toast.LENGTH_SHORT).show();
//                    Log.e("Tagged", "Disconnected");
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled at .info/connected");
            }
        };
//        mConnectedRef.addValueEventListener(mValueEventListener);
    }

    private void detachDatabaseReadListener() {
        if (mPatientChildEventListener != null) {
            patientChatHeadQuery.removeEventListener(mPatientChildEventListener);
            mPatientChildEventListener = null;
        }

        if (mDoctorChildEventListener != null) {
            doctorChatHeadQuery.removeEventListener(mDoctorChildEventListener);
            mDoctorChildEventListener = null;
        }

    }

    private void onStatusChange(){
        // state == 0 -> No Data at all
        if (mState == 0){
            mProgressBar.setVisibility(View.GONE);
            mNoResults.setVisibility(View.VISIBLE);

            mPatientsRelativeLayout.setVisibility(View.GONE);
            mDoctorsRelativeLayout.setVisibility(View.GONE);

        }else if (mState == 1){

            mProgressBar.setVisibility(View.GONE);
            mNoResults.setVisibility(View.GONE);

            mPatientsRelativeLayout.setVisibility(View.VISIBLE);
            mDoctorsRelativeLayout.setVisibility(View.VISIBLE);

        }else if (mState == 2){
            mProgressBar.setVisibility(View.GONE);
            mNoResults.setVisibility(View.GONE);

            mPatientsRelativeLayout.setVisibility(View.VISIBLE);

        }else if (mState == 3){
            mProgressBar.setVisibility(View.GONE);
            mNoResults.setVisibility(View.GONE);

            mDoctorsRelativeLayout.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        detachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabaseReadListener();
        mDoctorsChatList.clear();
        mPatientsChatList.clear();
    }

    @Override
    public void voidMethod(List<ChatHead> list, int adapterPosition) {
        Intent intent = new Intent(getActivity(), TestChatActivity.class);
        ChatHead chatHead = list.get(adapterPosition);
        intent.putExtra("client_id", chatHead.getUserId());
        intent.putExtra("client_name", chatHead.getUserName());
        intent.putExtra("which", chatHead.getWhich());
        intent.putExtra("collaborate", false);
        intent.putExtra("client_picture", chatHead.getPictureUrl());
        startActivity(intent);
    }
}