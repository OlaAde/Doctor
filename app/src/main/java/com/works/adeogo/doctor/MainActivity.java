package com.works.adeogo.doctor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.works.adeogo.doctor.model.DoctorProfile;
import com.works.adeogo.doctor.model.Notification;
import com.works.adeogo.doctor.utils.FirebaseUtils;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private String userId;
    public static final String ANONYMOUS = "anonymous";
    private String mUsername, mEmail, mPassword;
    private CoordinatorLayout mMainLayout;

    public static final int RC_SIGN_IN = 1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDeleteDatabaseReference, mAllDatabaseReference;

    private List<String> mChatList = new ArrayList<>();
    private ChildEventListener mChildEventListener;
    private DoctorProfile mDoctorProfile;
    private boolean mStart = false;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


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

        setContentView(R.layout.activity_main);

        mMainLayout = findViewById(R.id.main_content);
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
                    mAllDatabaseReference = mFirebaseDatabase.getReference().child("new_doctors/" + "all_profiles/" + userId );
                    mDeleteDatabaseReference = mFirebaseDatabase.getReference().child("deleted").child("doctors").child(userId);
                    onSignedInInitialize(user.getDisplayName());
                } else {
                    // User is signed out
                    Intent intent = new Intent(MainActivity.this, ActivityRegister.class);
                    startActivity(intent);
                }
            }
        };

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        Intent intent = getIntent();
        mStart = intent.getBooleanExtra("start", false);

        if (mStart){
            mViewPager.setCurrentItem(1);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_support) {
            Intent intent = new Intent(MainActivity.this, SupportActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_logout) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("LOGOUT");
            alertDialog.setMessage("Want to Logout?");
            alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
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
                    final android.app.AlertDialog waitingDialog = new SpotsDialog(MainActivity.this);
                    waitingDialog.show();

                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    ref.child("new_doctors").child(userId).removeValue();
                    ref.child("new_doctors").child("all_profiles").child(userId).removeValue();
                    mDeleteDatabaseReference.push().setValue(userId);
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(mEmail ,mPassword);

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
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(intent);                                                            }
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
                    mFirebaseAuth.signOut();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
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
                    if (TextUtils.equals(dataSnapshot.getKey(), "email")){
                        mEmail = dataSnapshot.getValue(String.class);
                    }
                    if (TextUtils.equals(dataSnapshot.getKey(), "password")){
                        mPassword = dataSnapshot.getValue().toString();
                    }
                }
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return "Messages";
            }else if (position == 1){
                return "Schedule";
            }else return "Profile";
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position == 0){
                MessagesFragment messagesFragment = new MessagesFragment();
                return messagesFragment;
            }else if (position == 1){
                AppointmentFragment appointmentFragment = new AppointmentFragment();
                return appointmentFragment;
            }else {
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}