package com.works.adeogo.doctor;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.works.adeogo.doctor.adapters.AppointmentAdapter;
import com.works.adeogo.doctor.model.Appointment;

import java.util.ArrayList;
import java.util.List;

import static com.works.adeogo.doctor.MessagesFragment.ANONYMOUS;


/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends Fragment implements AppointmentAdapter.AppointmentAdapterOnclickHandler {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mManager;
    private AppointmentAdapter mAdapter;

    private TextView mNoResults;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mAppointmentDatabaseReference;
    private ChildEventListener mChildEventListener;

    private List<Appointment> mList = new ArrayList<>();
    private String userId;
    private String mUsername;

    public AppointmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_appointment, container, false);

        mNoResults = rootView.findViewById(R.id.tvNoResultMessages);

        mRecyclerView = rootView.findViewById(R.id.appointment_recycler);

        mManager = new LinearLayoutManager(getActivity());
        mAdapter = new AppointmentAdapter(getActivity(), this);
        mAdapter.swapData(mList);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mManager);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    userId = user.getUid();
                    mAppointmentDatabaseReference = mFirebaseDatabase.getReference().child("new_doctors").child(userId).child("appointments").child("appointments");
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
                    mNoResults.setVisibility(View.GONE);
                    Appointment appointment = dataSnapshot.getValue(Appointment.class);

                    mList.add(appointment);
                    if (appointment != null){
                        mAdapter.swapData(null);
                        mAdapter.swapData(mList);
                        mAdapter.notifyDataSetChanged();
                    }
                    mAdapter.swapData(mList);
                }
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mAppointmentDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mAppointmentDatabaseReference.removeEventListener(mChildEventListener);
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
        mList.clear();
    }
    @Override
    public void voidMethod(List<Appointment> list, int adapterPosition) {

    }
}
