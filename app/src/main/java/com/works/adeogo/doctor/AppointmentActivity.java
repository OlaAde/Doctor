package com.works.adeogo.doctor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.works.adeogo.doctor.model.Appointment;
import com.works.adeogo.doctor.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppointmentActivity extends AppCompatActivity {


    private String mDoctorPhone, mClientPhone, mDoctorName, mClientName, mLocation, mTime, mDoctorId, mUserId, mMessage;
    private int mStatus, mYear, mMonth, mDay;
    private DatabaseReference mAppointmentDatabaseReference, mClientAppointReference, mProfileDatabaseReference;
    private ChildEventListener mProfileListener;

    private String mKey, mPhoneNumber = "2392895175215", mProfileImageUrl;

    private CircleImageView mCircleImageView;
    private ImageView mDialImageView;
    private TextView mClientNameTextView, mClientPhoneNumber, mAppointmentMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mClientNameTextView = findViewById(R.id.clientNameTextView);
        mClientPhoneNumber = findViewById(R.id.clientPhoneNumberTextView);
        mAppointmentMessage = findViewById(R.id.appointmentMessage);
        mCircleImageView = findViewById(R.id.appointmentIv);
        mDialImageView = findViewById(R.id.dialImageView);

        Intent intent = getIntent();

        mKey = intent.getStringExtra("key");
        mUserId = intent.getStringExtra("userId");
        mDoctorId = intent.getStringExtra("doctorId");
        mTime = intent.getStringExtra("time");
        mDay = intent.getIntExtra("day", 0);
        mMonth = intent.getIntExtra("month", 0);
        mYear = intent.getIntExtra("year", 0);
        mDoctorPhone = intent.getStringExtra("doctor_phone");
        mClientPhone = intent.getStringExtra("client_phone");
        mDoctorName = intent.getStringExtra("doctorName");
        mClientName = intent.getStringExtra("clientName");
        mLocation = intent.getStringExtra("location");
        mStatus = intent.getIntExtra("status", 0);
        mMessage = intent.getStringExtra("message");

        mAppointmentDatabaseReference = FirebaseDatabase.getInstance().getReference("new_doctors").child(mDoctorId).child("appointments").child("appointments").child(mKey);
        mProfileDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users/" + mUserId);
        mClientAppointReference = FirebaseDatabase.getInstance().getReference().child("users").child("appointments");

        mClientNameTextView.setText(mClientName);
        mClientPhoneNumber.setText(mPhoneNumber);
        mAppointmentMessage.setText(mMessage);



        mDialImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mClientPhone));
                startActivity(intent);
            }
        });

        attachDatabaseReadListener();

    }

    private void attachDatabaseReadListener() {
        if (mProfileListener == null) {
            mProfileListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    if (TextUtils.equals(dataSnapshot.getKey(), "photoUrl")){
                        mProfileImageUrl = dataSnapshot.getValue().toString();
                    }else if (TextUtils.equals(dataSnapshot.getKey(), "phone")){
                        mPhoneNumber = dataSnapshot.getValue().toString();
                    }

                    mClientPhoneNumber.setText(mPhoneNumber);

                    Picasso.with(AppointmentActivity.this)
                            .load(mProfileImageUrl)
                            .resize(500, 500)
                            .centerCrop()
                            .into(mCircleImageView);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mProfileDatabaseReference.addChildEventListener(mProfileListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_appointment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_accept) {
            final Intent intent = new Intent(AppointmentActivity.this, MainActivity.class);
            intent.putExtra("start", true);

            Appointment appointment = new Appointment(mUserId, mDoctorId, mTime, mYear, mMonth, mDay, mDoctorPhone, mClientPhone, mDoctorName, mClientName, mLocation, 1, mMessage);


            mAppointmentDatabaseReference.setValue(appointment).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    startActivity(intent);
                }
            });

            return true;
        }

        if (id == R.id.action_decline) {
            final Intent intent = new Intent(AppointmentActivity.this, MainActivity.class);
            intent.putExtra("start", true);
            Appointment appointment = new Appointment(mUserId, mDoctorId, mTime, mYear, mMonth, mDay, mDoctorPhone, mClientPhone, mDoctorName, mClientName, mLocation, 2, mMessage);


            mClientAppointReference.setValue(appointment);
            mAppointmentDatabaseReference.setValue(appointment).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    startActivity(intent);
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}