package com.works.adeogo.doctor;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

    private TextView mNoResults;
    private ListAdapter mListAdapter;
    private LinearLayoutManager mManager;
    private RecyclerView mRecyclerView;

    public static final int RC_SIGN_IN = 1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mChatDatabaseReference;
    private ChildEventListener mChildEventListener;

    private List<ChatHead> mChatList = new ArrayList<>();



    public MessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        mRecyclerView = rootView.findViewById(R.id.messages_recycler_view);
        mNoResults = rootView.findViewById(R.id.tvNoResultMessages);

        mListAdapter = new ListAdapter(getContext(), this);
        mManager = new LinearLayoutManager(getContext());
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.setLayoutManager(mManager);

        mListAdapter.swapData(mChatList);

        //firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    userId = user.getUid();
                    mChatDatabaseReference = mFirebaseDatabase.getReference().child("new_doctors").child(userId).child("questions").child("chat_head");
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
                    ChatHead chatHead =  dataSnapshot.getValue(ChatHead.class);
                    if (chatHead!= null ){
                        boolean check = true;
                        for(int i = 0; i < mChatList.size(); i++){
                            if (TextUtils.equals(chatHead.getUserId(), mChatList.get(i).getUserId() )){
                                check = false;
                            }
                        }
                        if (check){
                            mChatList.add(chatHead);
                        }
                    }
                    if (chatHead != null){
                        mListAdapter.swapData(null);
                        mListAdapter.notifyDataSetChanged();
                        mListAdapter.swapData(mChatList);
                        mListAdapter.notifyDataSetChanged();
                    }
                    mListAdapter.swapData(mChatList);
                }
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mChatDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mChatDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
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
        mChatList.clear();
    }

    @Override
    public void voidMethod(List<ChatHead> list, int adapterPosition) {
        Intent intent = new Intent(getActivity(), QuestionActivity.class);
        ChatHead chatHead = list.get(adapterPosition);
        intent.putExtra("client_id", chatHead.getUserId());
        intent.putExtra("client_name", chatHead.getUserName());
        startActivity(intent);
    }
}
