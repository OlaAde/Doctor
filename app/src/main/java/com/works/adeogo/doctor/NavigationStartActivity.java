package com.works.adeogo.doctor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.works.adeogo.doctor.model.DoctorProfile;

import io.fabric.sdk.android.Fabric;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NavigationStartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView mNameTextView, mSpecialityTextview;
    private CircleImageView mProfileImageView;
    private NavigationView mNavigationView;
    public static final String ANONYMOUS = "anonymous";
    private String mUsername, mEmail, mPassword, userId, mPictureUrl, mSpeciality, mName;
    private CoordinatorLayout mMainLayout;

    public static final int RC_SIGN_IN = 1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDeleteDatabaseReference, mAllDatabaseReference;

    private List<String> mChatList = new ArrayList<>();
    private ChildEventListener mChildEventListener;
    private DoctorProfile mDoctorProfile;
    private boolean mStart = false;

    private FragmentManager mFragmentManager;
    private ActionBar mActionBar;

    private HomeFragment homeFragment;
    private MessagesFragment messagesFragment;
    private AppointmentFragment appointmentFragment;
    private CollaborateFragment collaborateFragment;
    private ProfileFragment profileFragment;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());

        setContentView(R.layout.activity_navigation_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActionBar = getSupportActionBar();
        mFragmentManager = getSupportFragmentManager();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        View headerLayout = mNavigationView.getHeaderView(0); // 0-index header

        mProfileImageView = headerLayout.findViewById(R.id.imageView);
        mNameTextView = headerLayout.findViewById(R.id.profile_name_textview);
        mSpecialityTextview = headerLayout.findViewById(R.id.profile_speciality_textview);


        mMainLayout = findViewById(R.id.main_content);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        CreateFragments();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    userId = user.getUid();
                    FirebaseMessaging.getInstance().subscribeToTopic(userId);
                    mAllDatabaseReference = mFirebaseDatabase.getReference().child("new_doctors/" + "all_profiles/" + userId);
                    mDeleteDatabaseReference = mFirebaseDatabase.getReference().child("deleted").child("doctors").child(userId);
                    onSignedInInitialize(user.getDisplayName());
                } else {
                    // User is signed out
                    Intent intent = new Intent(NavigationStartActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };

        Intent intent = getIntent();
        mStart = intent.getBooleanExtra("start", false);

        if (mStart) {
            mActionBar.setTitle(R.string.action_appointments);
            mNavigationView.getMenu().getItem(1).setChecked(true);
            AppointmentFragment appointmentFragment = new AppointmentFragment();
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_frame, appointmentFragment)
                    .commit();
        } else {
            mActionBar.setTitle(R.string.action_home);
            mNavigationView.getMenu().getItem(0).setChecked(true);
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_frame, homeFragment)
                    .commit();
        }

    }

    private void CreateFragments(){
        homeFragment = new HomeFragment();
        messagesFragment = new MessagesFragment();
        appointmentFragment = new AppointmentFragment();
        collaborateFragment = new CollaborateFragment();
        profileFragment = new ProfileFragment();
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
                    if (TextUtils.equals(dataSnapshot.getKey(), "email")) {
                        mEmail = dataSnapshot.getValue(String.class);
                    } else if (TextUtils.equals(dataSnapshot.getKey(), "password")) {
                        mPassword = dataSnapshot.getValue().toString();
                    } else if (TextUtils.equals(dataSnapshot.getKey(), "pictureUrl")) {
                        mPictureUrl = dataSnapshot.getValue().toString();
                        Picasso.with(NavigationStartActivity.this)
                                .load(mPictureUrl)
                                .resize(500, 500)
                                .centerCrop()
                                .into(mProfileImageView);
                    } else if (TextUtils.equals(dataSnapshot.getKey(), "name")) {
                        mName = dataSnapshot.getValue().toString();
                        mNameTextView.setText(mName);
                    } else if (TextUtils.equals(dataSnapshot.getKey(), "speciality")) {
                        mSpeciality = dataSnapshot.getValue().toString();
                        mSpecialityTextview.setText(mSpeciality);
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
            mAllDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mAllDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(NavigationStartActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_home) {
            mActionBar.setTitle(R.string.action_home);
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_frame, homeFragment)
                    .commit();

        } else if (id == R.id.action_messages) {
            mActionBar.setTitle(R.string.action_messages);
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_frame, messagesFragment)
                    .commit();

        } else if (id == R.id.action_appointments) {
            mActionBar.setTitle(R.string.action_appointments);
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_frame, appointmentFragment)
                    .commit();
        } else if (id == R.id.action_profile) {
            mActionBar.setTitle(R.string.action_profile);
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_frame, profileFragment)
                    .commit();
        } else if (id == R.id.action_collaborate) {
            mActionBar.setTitle(R.string.action_collaborate);
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_frame, collaborateFragment)
                    .commit();
        } else if (id == R.id.action_contact_us) {

            Intent intent = new Intent(NavigationStartActivity.this, SupportActivity.class);
            startActivity(intent);

        } else if (id == R.id.action_logout) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(NavigationStartActivity.this);
            alertDialog.setTitle("LOGOUT");
            alertDialog.setMessage("Want to Logout?");
            alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(NavigationStartActivity.this);
                    alertDialog.setTitle("DELETE ACCOUNT");
                    alertDialog.setMessage("Every reference to this account will be deleted!");

                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final android.app.AlertDialog waitingDialog = new SpotsDialog(NavigationStartActivity.this);
                            waitingDialog.show();
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(userId);
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            ref.child("new_doctors").child(userId).removeValue();
                            ref.child("new_doctors").child("all_profiles").child(userId).removeValue();
                            mDeleteDatabaseReference.push().setValue(userId);
                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(mEmail, mPassword);

                            // Prompt the user to re-provide their sign-in credentials
                            user.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            user.delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                                            ref.child("new_doctors").child(userId).removeValue();

                                                            if (task.isSuccessful()) {
                                                                waitingDialog.dismiss();
                                                                Snackbar.make(mMainLayout, "Account deleted!", Snackbar.LENGTH_LONG).show();
                                                                mFirebaseAuth.signOut();
                                                                Intent intent = new Intent(NavigationStartActivity.this, LoginActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });

                                        }
                                    });
                        }
                    });
                    alertDialog.show();
                }
            });

            alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            alertDialog.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    FirebaseMessaging.getInstance().unsubscribeFromTopic(userId);
                    mFirebaseAuth.signOut();
                    Intent intent = new Intent(NavigationStartActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            alertDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
