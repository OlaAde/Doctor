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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adeogo on 11/20/2017.
 */

public class FragmentReg1 extends Fragment implements BlockingStep {

    private MaterialEditText mEmailEDT;
    private MaterialEditText mPasswordEDT;
    private MaterialEditText mNameEDT;
    private MaterialEditText mPhoneEDT;
    private LinearLayout mLayout;
    private SendMessage SM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_register, container, false);

        mEmailEDT = v.findViewById(R.id.edtEmail);
        mPasswordEDT = v.findViewById(R.id.edtPassword);
        mNameEDT = v.findViewById(R.id.edtName);
        mPhoneEDT = v.findViewById(R.id.edtPhone);

        mLayout = v.findViewById(R.id.reg_rootLayout);
        //initialize your UI

        return v;
    }

    public List<String> setEmailGuys(View v){
        if (TextUtils.isEmpty(mEmailEDT.getText().toString())){
//            Snackbar.make(v, "", Snackbar.LENGTH_SHORT)
//                    .show();
            Toast.makeText(getActivity(), "Please enter email address", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (TextUtils.isEmpty(mNameEDT.getText().toString())){
            Snackbar.make(v, "Please enter phone number", Snackbar.LENGTH_SHORT)
                    .show();
            return null;
        }

        if (TextUtils.isEmpty(mPasswordEDT.getText().toString())){
            Snackbar.make(v, "Please enter password", Snackbar.LENGTH_SHORT)
                    .show();
            return null;
        }

        if (mPasswordEDT.getText().toString().length()< 6){
            Snackbar.make(v, "Password too short !!!", Snackbar.LENGTH_SHORT)
                    .show();
            return null;
        }

        if (TextUtils.isEmpty(mPhoneEDT.getText().toString())){
            Snackbar.make(v, "Please enter email address", Snackbar.LENGTH_SHORT)
                    .show();
            return null;
        }

        List<String> retString = new ArrayList<>();
        retString.add(mEmailEDT.getText().toString());
        retString.add(mPasswordEDT.getText().toString());
        retString.add(mEmailEDT.getText().toString());
        retString.add(mPhoneEDT.getText().toString());
    return retString;
    }

    public boolean checkData(){
        if (TextUtils.isEmpty(mEmailEDT.getText().toString())){
//
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

        if (TextUtils.isEmpty(mPhoneEDT.getText().toString())){
            Toast.makeText(getActivity(), "Please enter phone number", Toast.LENGTH_SHORT).show();
            return false;
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
                mPhoneEDT.getText().toString().trim());
        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

    }
}
