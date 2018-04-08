package com.works.adeogo.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;
import com.works.adeogo.doctor.model.Message;
import com.works.adeogo.doctor.model.MessagesFixtures;
import com.works.adeogo.doctor.model.TempMessage;
import com.works.adeogo.doctor.model.User;
import com.works.adeogo.doctor.utils.AppUtils;
import com.works.adeogo.doctor.utils.IdGenerator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class TestingActivity extends AppCompatActivity
        implements MessagesListAdapter.SelectionListener,
        MessagesListAdapter.OnLoadMoreListener,
        MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        DateFormatter.Formatter {

    private ImageLoader imageLoader;
    private MessagesListAdapter<Message> messagesAdapter;

    protected final String senderId = "0";
    private static final int TOTAL_MESSAGES_COUNT = 100;
    private Menu menu;
    private int selectionCount;
    private Date lastLoadedDate;

    private Query testChatterQuery;

    private ArrayList<Message> messages = new ArrayList<>();

    private StorageReference mChatPhotosStorageReference;
    private FirebaseStorage mFirebaseStorage;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private ChildEventListener mtestChildEventListener, mChildEventListener, mPhotoChildEventListener;
    private DatabaseReference testChatterReference, mClientDatabaseReference, mSelfDatabaseReference, mSelfChatHeadDatabaseReference, mClientChatHeadDatabaseReference, mPictureRef;

    private String mUsername, mClientName, userId, mClientId, mClientPhotoUrl, mSelfPictureUrl, messageId;
    private int which;
    private boolean isFromCollaborate = false;

    public static final int RC_SIGN_IN = 1, RC_PHOTO_PICKER = 2;
    private ActionBar mActionBar;


    public static void open(Context context) {
        context.startActivity(new Intent(context, TestingActivity.class));
    }

    private MessagesList messagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_chat);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.e("user things", user + " Here");
                if (user != null) {
                    // User is signed in
                    userId = user.getUid();

                    messagesList = (MessagesList) findViewById(R.id.messagesList);
                    initAdapter();

                    MessageInput input = (MessageInput) findViewById(R.id.input);
                    input.setInputListener(TestingActivity.this);
                    input.setAttachmentsListener(TestingActivity.this);

                    testChatterQuery = mFirebaseDatabase.getReference().child("testChatter");
                    testChatterQuery = mFirebaseDatabase.getReference().child("questions").child("MoMdHbx6IgVcEdKJ4GHSmN0ULLg1").child("clients").child("3xFvDJ4ufaM0zTCQHhDzBYfbnt73");
                    testChatterReference = mFirebaseDatabase.getReference().child("testChatter");
                    onSignedInInitialize(user.getDisplayName());
                } else {
                    // User is signed out

                    Intent intent = new Intent(TestingActivity.this, LoginActivity.class);
                    startActivity(intent);
                    onSignedOutCleanup();
                }
            }
        };

        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Picasso.with(TestingActivity.this).load(url).into(imageView);
            }
        };


    }

    @Override
    public boolean onSubmit(CharSequence input) {
        messageId = new IdGenerator(new Random()).nextId();
        testChatterReference = FirebaseDatabase.getInstance().getReference().child("testChatter");
        testChatterReference.push().setValue(new TempMessage(messageId, input.toString(), new Date(), "0",
                "Olamide", "https://chatasl.com/wp-content/themes/community-builder/assets/images/avatars/user-default-avatar.png",
                true, null, null));
        return true;
    }

    @Override
    public void onAddAttachments() {
        messagesAdapter.addToStart(MessagesFixtures.getImageMessage(), true);
    }

    @Override
    public String format(Date date) {
        if (DateFormatter.isToday(date)) {
            return getString(R.string.date_header_today);
        } else if (DateFormatter.isYesterday(date)) {
            return getString(R.string.date_header_yesterday);
        } else {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
        }
    }

    private void initAdapter() {
        messagesAdapter = new MessagesListAdapter<>(userId, imageLoader);
        messagesAdapter.enableSelectionMode(this);
        messagesAdapter.setLoadMoreListener(this);
        messagesAdapter.setDateHeadersFormatter(this);
        messagesList.setAdapter(messagesAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.chat_actions_menu, menu);
        onSelectionChanged(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                messagesAdapter.deleteSelectedMessages();
                break;
            case R.id.action_copy:
                messagesAdapter.copySelectedMessagesText(this, getMessageStringFormatter(), true);
                AppUtils.showToast(this, R.string.copied_message, true);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (selectionCount == 0) {
            super.onBackPressed();
        } else {
            messagesAdapter.unselectAllItems();
        }
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        if (totalItemsCount < TOTAL_MESSAGES_COUNT) {
            loadMessages();
        }
    }

    @Override
    public void onSelectionChanged(int count) {
        this.selectionCount = count;
        menu.findItem(R.id.action_delete).setVisible(count > 0);
        menu.findItem(R.id.action_copy).setVisible(count > 0);
    }

    protected void loadMessages() {
        attachDatabaseReadListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    private MessagesListAdapter.Formatter<Message> getMessageStringFormatter() {
        return new MessagesListAdapter.Formatter<Message>() {
            @Override
            public String format(Message message) {
                String createdAt = new SimpleDateFormat("MMM d, EEE 'at' h:mm a", Locale.getDefault())
                        .format(message.getCreatedAt());

                String text = message.getText();
                if (text == null) text = "[attachment]";

                return String.format(Locale.getDefault(), "%s: %s (%s)",
                        message.getUser().getName(), text, createdAt);
            }
        };
    }

    private void onSignedInInitialize(String username) {
        mUsername = username;
        attachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
        mUsername = "Anonymous";
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mtestChildEventListener == null) {
            mtestChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    TempMessage tempMessage = dataSnapshot.getValue(TempMessage.class);
                    if (tempMessage != null) {
                        Message message = new Message(tempMessage.getId(), tempMessage.getText(), tempMessage.getCreatedAt(), new User(tempMessage.getUserId(), tempMessage.getName(), tempMessage.getAvatar(), tempMessage.isOnline()), tempMessage.getImage(), tempMessage.getVoice());
                        List<Message> teMessages = new ArrayList<>();
                        teMessages.add(message);
                        messagesAdapter.addToStart(message, true);

                        messages.add(message);
                    }
//                    lastLoadedDate = messages.get(messages.size() - 1).getCreatedAt();
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
            testChatterReference.addChildEventListener(mtestChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mtestChildEventListener != null) {
            testChatterReference.removeEventListener(mtestChildEventListener);
            mtestChildEventListener = null;
        }
        messagesAdapter.clear();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabaseReadListener();
    }
}