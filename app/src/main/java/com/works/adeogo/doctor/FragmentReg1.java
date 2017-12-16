package com.works.adeogo.doctor;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import net.rimoto.intlphoneinput.IntlPhoneInput;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adeogo on 11/20/2017.
 */

public class FragmentReg1 extends Fragment implements BlockingStep {

    private MaterialEditText mEmailEDT, mPasswordEDT, mNameEDT;
    private IntlPhoneInput mIntlPhoneInput;
    private LinearLayout mLayout;
    private SendMessage SM;
    private String phoneNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_register, container, false);

        mEmailEDT = v.findViewById(R.id.edtEmail);
        mPasswordEDT = v.findViewById(R.id.edtPassword);
        mNameEDT = v.findViewById(R.id.edtName);
        mIntlPhoneInput = v.findViewById(R.id.phoneInput);

        mLayout = v.findViewById(R.id.reg_rootLayout);
        //initialize your UI

        return v;
    }

    public boolean checkData(){

        if (TextUtils.isEmpty(mEmailEDT.getText().toString())){
            Toast.makeText(getActivity(), "Please enter email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mNameEDT.getText().toString())){
            Toast.makeText(getActivity(), "Please enter phone number", Toast.LENGTH_SHORT).show();

            return false;
        }

        if (TextUtils.isEmpty(mPasswordEDT.getText().toString())){
            Toast.makeText(getActivity(), "Please enter password", Toast.LENGTH_SHORT).show();

            return false;
        }

        if (mPasswordEDT.getText().toString().length()< 6){

            Toast.makeText(getActivity(), "Password too short !!!", Toast.LENGTH_SHORT).show();
            return false;
        }

        phoneNumber = mIntlPhoneInput.getNumber();

        if (TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(getActivity(), "Please enter phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!mIntlPhoneInput.isValid()){
            Toast.makeText(getActivity(), "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
        }


        return true;
    }

    @Override
    public VerificationError verifyStep() {
        //return null if the user can go to the next step, create a new VerificationError instance otherwise
        return null;
    }

    @Override
    public void onSelected() {
        //update UI when selected
    }



    @Override
    public void onError(@NonNull VerificationError error) {
        //handle error inside of the fragment, e.g. show error on EditText
    }

    interface SendMessage {
        void sendData(String email, String password, String name, String phone);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            SM = (SendMessage) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }

    @Override
    public void onNextClicked(final StepperLayout.OnNextClickedCallback callback) {
        if (!checkData())
            return;
        SM.sendData(mEmailEDT.getText().toString().trim(), mPasswordEDT.getText().toString().trim(), mNameEDT.getText().toString().trim(),
                phoneNumber);
        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

    }
}
