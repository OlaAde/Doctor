package com.works.adeogo.doctor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
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
import com.works.adeogo.doctor.adapters.QuestionAdapter;
import com.works.adeogo.doctor.model.ChatHead;
import com.works.adeogo.doctor.model.Notification;
import com.works.adeogo.doctor.model.Question;
import com.works.adeogo.doctor.utils.Constants;
import com.works.adeogo.doctor.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class QuestionActivity extends AppCompatActivity {

    //    private RecyclerView mRecyclerView;
    private QuestionAdapter mAdapter;
    private LinearLayoutManager mManager;
    //    private List<Question> mQuestionList ;
    private ListView mQuestionListView;

    private ImageView mLinkImageView, mSendImage;
    private EditText mQuestionEditText;
    private TextView mNoInternetTextView, mNoQuestionTextView;
    private LinearLayout mSendLinearLayout;

    private StorageReference mChatPhotosStorageReference;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mClientDatabaseReference, mSelfDatabaseReference, mSelfChatHeadDatabaseReference, mClientChatHeadDatabaseReference, mPictureRef;
    private ChildEventListener mChildEventListener, mPhotoChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private ActionBar mActionBar;

    public static final int RC_SIGN_IN = 1, RC_PHOTO_PICKER = 2;
    private String mUsername, mClientName, userId, mClientId, mClientPhotoUrl, mSelfPictureUrl;
    private int which;
    private boolean isFromCollaborate = false;

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

        setContentView(R.layout.activity_question);

        mActionBar = getSupportActionBar();
        mActionBar.setTitle("Question");
        mActionBar.setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        mClientId = intent.getStringExtra("client_id");
        isFromCollaborate = intent.getBooleanExtra("collaborate", false);
        mClientName = intent.getStringExtra("client_name");
        mClientPhotoUrl = intent.getStringExtra("client_picture");
        which = intent.getIntExtra("which", 0);

        mActionBar.setTitle(mClientName);
        mLinkImageView = findViewById(R.id.linkPhoto);
        mSendLinearLayout = (LinearLayout) findViewById(R.id.questionLinearLayout);
        mNoInternetTextView = (TextView) findViewById(R.id.questionNoInternetTextView);
        mNoQuestionTextView = (TextView) findViewById(R.id.questionNoQuestionsTextView);
        mQuestionListView = (ListView) findViewById(R.id.questionListView);

        mQuestionEditText = (EditText) findViewById(R.id.questionMessageEditText);
        mSendImage = (ImageView) findViewById(R.id.questionSendButton);

        List<Question> questions = new ArrayList<>();

        mAdapter = new QuestionAdapter(this, R.layout.item_question, questions);
        mQuestionListView.setAdapter(mAdapter);
        mQuestionListView.setClickable(false);

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
                    mUsername = user.getDisplayName();

                    if (which == 0) {
                        mClientDatabaseReference = mFirebaseDatabase.getReference().child("users").child(mClientId).child("questions").child("doctors").child(userId);
                        mClientChatHeadDatabaseReference = mFirebaseDatabase.getReference().child("users").child(mClientId).child("questions").child("chat_head").child("doctors").child(userId);
                        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("clients").child("doctors").child("chat_photos");

                        mSelfDatabaseReference = mFirebaseDatabase.getReference().child("new_doctors").child(userId).child("questions").child("clients").child(mClientId);
                        mSelfChatHeadDatabaseReference = mFirebaseDatabase.getReference().child("new_doctors").child(userId).child("questions").child("chat_head").child("clients").child(mClientId);

                    } else if (which == 1) {
                        mClientDatabaseReference = mFirebaseDatabase.getReference().child("new_doctors").child(mClientId).child("questions").child("doctors").child(userId);
                        mClientChatHeadDatabaseReference = mFirebaseDatabase.getReference().child("new_doctors").child(mClientId).child("questions").child("chat_head").child("doctors").child(userId);
                        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("doctors").child("doctors").child("chat_photos");

                        mSelfDatabaseReference = mFirebaseDatabase.getReference().child("new_doctors").child(userId).child("questions").child("doctors").child(mClientId);
                        mSelfChatHeadDatabaseReference = mFirebaseDatabase.getReference().child("new_doctors").child(userId).child("questions").child("chat_head").child("doctors").child(mClientId);

                    }

                    mPictureRef = mFirebaseDatabase.getReference().child("new_doctors").child("all_profiles").child(userId);


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
                    mSendImage.setEnabled(true);
                } else {
                    mSendImage.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        int position = mQuestionListView.getFirstVisiblePosition();
        mQuestionListView.smoothScrollToPosition(position);

        boolean isConnect = NetworkUtils.isOnline(this);
        if (!isConnect) {
            mNoInternetTextView.setVisibility(View.VISIBLE);
            noInternet();
        }

        // Send button sends a message and clears the EditText
        mSendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(mQuestionEditText.getText().toString())) {
                    if (isFromCollaborate || which == 1) {
                        Question question = new Question(mQuestionEditText.getText().toString().trim(), mUsername, 0, null);
                        mSelfDatabaseReference.push().setValue(question);
                        mClientDatabaseReference.push().setValue(question);

                        long unixTimeStamp = NetworkUtils.getUnixTime();
                        ChatHead selfChatHead = new ChatHead(mClientId, mClientName, mClientPhotoUrl, unixTimeStamp, 1);
                        mSelfChatHeadDatabaseReference.setValue(selfChatHead);

                        ChatHead doctorChatHead = new ChatHead(userId, mUsername, mSelfPictureUrl, unixTimeStamp, 1);


                        mClientChatHeadDatabaseReference.setValue(doctorChatHead);

                        Notification notification = new Notification();
                        notification.setText(mQuestionEditText.getText().toString().trim());
                        notification.setTopic(mClientId);
                        notification.setUid(userId);
                        notification.setUsername(mUsername);
                        notification.setType("0");
                        notification.setWhich(Integer.toString(which));
                        if (mClientPhotoUrl != null) {
                            notification.setImageUrl(mClientPhotoUrl);
                        } else {
                            mClientPhotoUrl = Constants.mPlaceHolder;
                            notification.setImageUrl(mClientPhotoUrl);
                        }


                        FirebaseDatabase.getInstance().getReference("notifications").child(mClientId).push().setValue(notification);

                        FirebaseMessaging.getInstance().subscribeToTopic(userId);

                        // Clear input box
                        mQuestionEditText.setText("");
                    } else {
                        Question question = new Question(mQuestionEditText.getText().toString().trim(), mUsername, 0, null);
                        mSelfDatabaseReference.push().setValue(question);
                        mClientDatabaseReference.push().setValue(question);

                        long unixTimeStamp = NetworkUtils.getUnixTime();

                        ChatHead selfChatHead = new ChatHead(mClientId, mClientName, mClientPhotoUrl, unixTimeStamp, 0);
                        mSelfChatHeadDatabaseReference.setValue(selfChatHead);

                        ChatHead clientChatHead = new ChatHead(userId, mUsername, mSelfPictureUrl, unixTimeStamp, 0);
                        mClientChatHeadDatabaseReference.setValue(clientChatHead);


                        Notification notification = new Notification();
                        notification.setText(mQuestionEditText.getText().toString().trim());
                        notification.setTopic(mClientId);
                        notification.setUid(userId);
                        notification.setUsername(mUsername);
                        notification.setType("0");
                        notification.setWhich(Integer.toString(which));
                        if (mClientPhotoUrl != null) {
                            notification.setImageUrl(mClientPhotoUrl);
                        } else {
                            mClientPhotoUrl = Constants.mPlaceHolder;
                            notification.setImageUrl(mClientPhotoUrl);
                        }
                        FirebaseDatabase.getInstance().getReference("notifications").child(mClientId).push().setValue(notification);

                        FirebaseMessaging.getInstance().subscribeToTopic(userId);
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(mClientId);
                        // Clear input box
                        mQuestionEditText.setText("");
                    }

                } else {
                    Toast.makeText(QuestionActivity.this, "Enter a question", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mLinkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
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
                    if (TextUtils.equals(question.getName(), mUsername)) {
                        question.setYou(0);
                        question.setName("You");
                    } else {
                        question.setYou(1);
                    }
                    mAdapter.add(question);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mSelfDatabaseReference.addChildEventListener(mChildEventListener);
        }

        if (mPhotoChildEventListener == null) {
            mPhotoChildEventListener = new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String key = dataSnapshot.getKey().toString();
                    if (TextUtils.equals(key, "pictureUrl")) {
                        mSelfPictureUrl = dataSnapshot.getValue().toString();
                    }
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mPictureRef.addChildEventListener(mPhotoChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mSelfDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }

        if (mPhotoChildEventListener != null) {
            mPictureRef.removeEventListener(mPhotoChildEventListener);
            mPhotoChildEventListener = null;
        }

        mAdapter.clear();

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
        } else if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            // Get a reference to store file at chat_photos/<FILENAME>
            StorageReference photoRef = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());

            // Upload file to Firebase Storage
            photoRef.putFile(selectedImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // When the image has successfully uploaded, we get its download URL
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();


                            Question question = new Question(null, mUsername, 0, downloadUrl.toString());
                            ChatHead selfChatHead;
                            ChatHead clientChatHead;
                            if (isFromCollaborate || which == 1) {

                                long unixTimeStamp = NetworkUtils.getUnixTime();

                                selfChatHead = new ChatHead(mClientId, mClientName, mClientPhotoUrl, unixTimeStamp, 1);

                                clientChatHead = new ChatHead(userId, mUsername, mSelfPictureUrl, unixTimeStamp, 1);


                            } else {

                                long unixTimeStamp = NetworkUtils.getUnixTime();

                                selfChatHead = new ChatHead(mClientId, mClientName, mClientPhotoUrl, unixTimeStamp, 0);

                                clientChatHead = new ChatHead(userId, mUsername, mSelfPictureUrl, unixTimeStamp, 0);
                            }

                            mSelfChatHeadDatabaseReference.setValue(selfChatHead);
                            mClientChatHeadDatabaseReference.setValue(clientChatHead);

                            mSelfDatabaseReference.push().setValue(question);
                            mClientDatabaseReference.push().setValue(question);

                            Notification notification = new Notification();
                            notification.setText(mQuestionEditText.getText().toString().trim());
                            notification.setTopic(mClientId);
                            notification.setUid(userId);
                            notification.setUsername(mUsername);
                            notification.setType("0");
                            notification.setWhich(Integer.toString(which));
                            if (mClientPhotoUrl != null) {
                                notification.setImageUrl(mClientPhotoUrl);
                            } else {
                                mClientPhotoUrl = Constants.mPlaceHolder;
                                notification.setImageUrl(mClientPhotoUrl);
                            }

                            FirebaseDatabase.getInstance().getReference("notifications").child(mClientId).push().setValue(notification);

                            FirebaseMessaging.getInstance().subscribeToTopic(userId);
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(mClientId);


//                            // Set the download URL to the message box, so that the user can send it to the database
//                            FriendlyMessage friendlyMessage = new FriendlyMessage(null, mUsername, downloadUrl.toString());
//                            mMessagesDatabaseReference.push().setValue(friendlyMessage);
                            System.out.println("On Success!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("OnFailureListener: " + e);
                            e.printStackTrace();
                        }
                    });
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

    private void noInternet() {
        mNoQuestionTextView.setVisibility(View.GONE);
        mNoInternetTextView.setVisibility(View.VISIBLE);
        mSendLinearLayout.setVisibility(View.GONE);
    }
}
