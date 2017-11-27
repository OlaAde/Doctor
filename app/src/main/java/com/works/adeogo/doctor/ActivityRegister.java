package com.works.adeogo.doctor;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.works.adeogo.doctor.adapters.StepperAdapter;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityRegister extends AppCompatActivity implements StepperLayout.StepperListener, Step, FragmentReg1.SendMessage, ProfileRegistrationFragment.SendMessage1{


    private StepperLayout mStepperLayout;
    private StepperAdapter mStepperAdapter;

    private String mPhotoUrl ;
    private String mCountry;
    private String mSpeciality;
    private String mCity;
    private String mEmail ;
    private String mPassword;
    private String mName;
    private String mPhone;
    private int clicked = 0;

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

        setContentView(R.layout.activity_register2);

        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperAdapter = new StepperAdapter(getSupportFragmentManager(), this);
        mStepperLayout.setAdapter(mStepperAdapter);
        mStepperLayout.setListener(this);
    }



    @Override
    public void onCompleted(View completeButton) {
        Toast.makeText(this, "onCompleted!", Toast.LENGTH_SHORT).show();
    }



    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    private static String makeFragmentName(int viewPagerId, int index) {

        return "android:switcher:" + R.id.ms_stepPager + ":" + index;
    }
    @Override
    public void onError(VerificationError verificationError) {
        Toast.makeText(this, "onError! -> " + verificationError.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {
        finish();
    }

    @Override
    public void sendData(String email, String password, String name, String phone) {


        ProfileRegistrationFragment f = (ProfileRegistrationFragment) getSupportFragmentManager().findFragmentByTag(makeFragmentName(com.stepstone.stepper.R.id.ms_stepPager, 1));

        f.displayReceivedData(email, password, name, phone);
    }

    @Override
    public void sendData(String email, String password, String name, String phone, String photoUrl, String country, String speciality, String city) {
        GigRegisterFragment gigRegisterFragment = (GigRegisterFragment) getSupportFragmentManager().findFragmentByTag(makeFragmentName(com.stepstone.stepper.R.id.ms_stepPager, 2));
        gigRegisterFragment.displayReceivedData1(email, password, name, phone, photoUrl, country, speciality, city, ActivityRegister.this);
    }
}
