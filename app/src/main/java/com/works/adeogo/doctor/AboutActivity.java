package com.works.adeogo.doctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.works.adeogo.doctor.model.Appointment;
import com.works.adeogo.doctor.model.DoctorProfile;

public class AboutActivity extends AppCompatActivity {

    private MaterialEditText mMaterialEditText;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mAllDatabaseReference;
    private String mPictureUrl ="",  mPhoneNumber = "", mDoctorName = "", mEmail = "", mPassword = "", mCountry = "", mCity = "", mSpeciality = "", mDoctorId = "", mSpecial = "", mAbout, mConsultationFee;
    private int sunday = 0, monday = 0, tuesday = 0, wednesday = 0, thursday = 0, friday = 0, saturday = 0, startHour, startMinute, endHour, endMinute, online = 0, home = 0, office = 0, clinic = 0, sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("About");

        mMaterialEditText = findViewById(R.id.edtAbout);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        Intent intent = getIntent();
        mDoctorId = intent.getStringExtra("doctorId");
        mDoctorName = intent.getStringExtra("name");
        mPhoneNumber = intent.getStringExtra("phoneNUmber");
        mPictureUrl = intent.getStringExtra("pictureUrl");
        mEmail = intent.getStringExtra("email");
        mPassword = intent.getStringExtra("password");
        mCountry = intent.getStringExtra("country");
        mCity = intent.getStringExtra("city");
        mSpeciality = intent.getStringExtra("speciality");
        mConsultationFee = intent.getStringExtra("consultationFee");
        mAbout = intent.getStringExtra("about");

        sunday = intent.getIntExtra("sunday", 0);
        monday = intent.getIntExtra("monday", 0);
        tuesday = intent.getIntExtra("tuesday", 0);
        wednesday = intent.getIntExtra("wednesday", 0);
        thursday = intent.getIntExtra("thursday", 0);
        friday = intent.getIntExtra("friday", 0);
        saturday = intent.getIntExtra("saturday", 0);

        startHour = intent.getIntExtra("startHour", 0);
        startMinute = intent.getIntExtra("startMinute", 0);
        endHour = intent.getIntExtra("endHour", 0);
        endMinute = intent.getIntExtra("endMinute", 0);
        online = intent.getIntExtra("online", 0);
        home = intent.getIntExtra("home", 0);
        office = intent.getIntExtra("office", 0);
        clinic = intent.getIntExtra("clinic", 0);
        sex = intent.getIntExtra("sex", 0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.about, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {

            if (TextUtils.equals(mMaterialEditText.getText().toString(), mAbout) && TextUtils.equals(mMaterialEditText.getText().toString(), "")){
                Intent intent = new Intent(AboutActivity.this, NavigationStartActivity.class);
                startActivity(intent);
            }else {
                mAbout = mMaterialEditText.getText().toString();

                DoctorProfile doctorProfile = new DoctorProfile(mDoctorId, mDoctorName, mPhoneNumber, mPictureUrl, mEmail, mPassword, mCountry, mCity,
                        mSpeciality, mConsultationFee, mAbout, sunday, monday, tuesday, wednesday, thursday, friday, saturday, startHour, startMinute, endHour, endMinute,
                        online, home, office, clinic,sex );


                mDatabaseReference = mFirebaseDatabase.getReference("new_doctors").child(mDoctorId).child("profile").child("profile");

                mDatabaseReference.push().setValue(doctorProfile);
                mAllDatabaseReference = mFirebaseDatabase.getReference("new_doctors").child("all_profiles").child(mDoctorId);

                mAllDatabaseReference.setValue(doctorProfile).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AboutActivity.this, "About Updated!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AboutActivity.this, NavigationStartActivity.class);
                        startActivity(intent);
                    }
                });
            }

//            mClientAppointReference.setValue(appointment);
//            mAppointmentDatabaseReference.setValue(appointment).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    startActivity(intent);
//                }
//            });
//
//
//            final Intent intent = new Intent(AppointmentActivity.this, NavigationStartActivity.class);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
