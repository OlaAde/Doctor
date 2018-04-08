package com.works.adeogo.doctor;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Adeogo on 11/20/2017.
 */

public class FragmentReg1 extends Fragment  {

    private MaterialEditText mEmailEDT, mPasswordEDT, mRePassword, mNameEDT;
    private IntlPhoneInput mIntlPhoneInput;
    private LinearLayout mLayout;
//    private SendMessage SM;
    private String phoneNumber;
    private String mPasswordEntered;
    private boolean mConfirmed = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_register, container, false);

        mEmailEDT = v.findViewById(R.id.edtEmail);
        mPasswordEDT = v.findViewById(R.id.edtPassword);
        mRePassword = v.findViewById(R.id.edtRePassword);
        mNameEDT = v.findViewById(R.id.edtName);
        mIntlPhoneInput = v.findViewById(R.id.phoneInput);

        mLayout = v.findViewById(R.id.reg_rootLayout);
        //initialize your UI

        mRePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPasswordEntered = mPasswordEDT.getText().toString();
            }
        });


        mRePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPasswordEntered = mPasswordEDT.getText().toString();
                if (charSequence.toString().trim().length() > 0) {
                    if (!TextUtils.equals(mPasswordEntered, charSequence.toString())){
                        mConfirmed = false;
                        mRePassword.setTextColor(getActivity().getResources().getColor(R.color.colorRed));
                    }else {
                        mConfirmed = true;
                        mRePassword.setTextColor(getActivity().getResources().getColor(R.color.black));
                    }
                } else {

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mEmailEDT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email = mEmailEDT.getText().toString();
                if (charSequence.toString().trim().length() > 0) {
                    if (!isEmailValid(email)){
                        mEmailEDT.setTextColor(getActivity().getResources().getColor(R.color.colorRed));
                    }else {
                        mEmailEDT.setTextColor(getActivity().getResources().getColor(R.color.black));
                    }
                } else {

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return v;
    }

    public boolean checkData(){

        if (TextUtils.isEmpty(mEmailEDT.getText().toString())){
            Toast.makeText(getActivity(), "Please enter email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isEmailValid(mEmailEDT.getText().toString())){
            Toast.makeText(getActivity(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
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

        if (!mConfirmed){
            Toast.makeText(getActivity(), "Confirm Password!", Toast.LENGTH_SHORT).show();
            return false;
        }

        phoneNumber = mIntlPhoneInput.getNumber();

        if (TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(getActivity(), "Please enter phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!mIntlPhoneInput.isValid()){
            Toast.makeText(getActivity(), "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }



    public List<String> getData(){

        String mEmail, mPassword, mName, mPhone;

        if (!checkData()){
            return null;
        }else {
            mEmail = mEmailEDT.getText().toString().trim();
            mPassword = mPasswordEDT.getText().toString().trim();
            mName = mNameEDT.getText().toString().trim();
            mPhone = phoneNumber;

            List result = new ArrayList<String>();

            result.add(mEmail);
            result.add(mPassword);
            result.add(mName);
            result.add(mPhone);

            return result;
        }
    }

//    interface SendMessage {
//        void sendData(String email, String password, String name, String phone);
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
////        try {
////            SM = (SendMessage) getActivity();
////        } catch (ClassCastException e) {
////            throw new ClassCastException("Error in retrieving data. Please try again");
////        }
//
//    }
//
//    @Override
//    public void onNextClicked(final StepperLayout.OnNextClickedCallback callback) {
//        if (!checkData())
//            return;
//        SM.sendData(St);
//        callback.goToNextStep();
//    }



    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
