package com.works.adeogo.doctor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.works.adeogo.doctor.adapters.QuestionAdapter;
import com.works.adeogo.doctor.model.Notification;
import com.works.adeogo.doctor.model.Question;
import com.works.adeogo.doctor.utils.FirebaseUtils;
import com.works.adeogo.doctor.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class QuestionActivity extends AppCompatActivity {

    //    private RecyclerView mRecyclerView;
    private QuestionAdapter mAdapter;
    private LinearLayoutManager mManager;
    //    private List<Question> mQuestionList ;
    private ListView mQuestionListView;

    private EditText mQuestionEditText;
    private Button mSendButton;
    private TextView mNoInternetTextView;
    private TextView mNoQuestionTextView;
    private LinearLayout mSendLinearLayout;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mClientDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    public static final int RC_SIGN_IN = 1;
    private String mUsername;
    private String mClientName;
    private String userId;
    private String mClientId;


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

        setContentView(R.layout.activity_question);

        getSupportActionBar().setTitle("Question");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mClientId = intent.getStringExtra("client_id");
        mClientName = intent.getStringExtra("client_name");

        mSendLinearLayout = (LinearLayout) findViewById(R.id.questionLinearLayout);
        mNoInternetTextView = (TextView) findViewById(R.id.questionNoInternetTextView);
        mNoQuestionTextView = (TextView) findViewById(R.id.questionNoQuestionsTextView);
        mQuestionListView = (ListView) findViewById(R.id.questionListView);

        mQuestionEditText = (EditText) findViewById(R.id.questionMessageEditText);
        mSendButton = (Button) findViewById(R.id.questionSendButton);

        List<Question> questions = new ArrayList<>();

        mAdapter = new QuestionAdapter(this, R.layout.item_question, questions);
        mQuestionListView.setAdapter(mAdapter);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    userId = user.getUid();
                    mUsername = user.getDisplayName();

                    mClientDatabaseReference = mFirebaseDatabase.getReference().child("users").child(mClientId).child("questions").child(userId);

                    mDatabaseReference = mFirebaseDatabase.getReference().child("new_doctors/" +userId + "/questions/" + mClientId);
                    onSignedInInitialize(user.getDisplayName());
                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    Intent intent = new Intent(QuestionActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };

        mQuestionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        int position = mQuestionListView.getFirstVisiblePosition();
        mQuestionListView.smoothScrollToPosition(position);

        boolean isConnect = NetworkUtils.isOnline(this);
        if (!isConnect){
            mNoInternetTextView.setVisibility(View.VISIBLE);
            noInternet();
        }

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mQuestionEditText.getText().toString())){
                    Question question = new Question(mQuestionEditText.getText().toString().trim(), mUsername, 0);
                    mDatabaseReference.push().setValue(question);
                    mClientDatabaseReference.push().setValue(question);

                    Notification notification = new Notification();
                    notification.setText(mQuestionEditText.getText().toString().trim());
                    notification.setTopic(mClientId);
                    notification.setUid(userId);
                    notification.setUsername(mUsername);

                    FirebaseDatabase.getInstance().getReference(mClientId).push().setValue(notification);

                    FirebaseMessaging.getInstance().subscribeToTopic(userId);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(mClientId);
                    // Clear input box
                    mQuestionEditText.setText("");
                }else {
                    Toast.makeText(QuestionActivity.this, "Enter a question", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onSignedInInitialize(String username) {
        mUsername = username;
        attachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Question question = dataSnapshot.getValue(Question.class);
                    if (TextUtils.equals(question.getName(), mUsername)){
                        question.setYou(0);
                        question.setName("You");
                    }else {
                        question.setYou(1);
                    }
                    mAdapter.add(question);
                }
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabaseReadListener();
    }


    private void noInternet(){
        mNoQuestionTextView.setVisibility(View.GONE);
        mNoInternetTextView.setVisibility(View.VISIBLE);
        mSendLinearLayout.setVisibility(View.GONE);
    }


}
