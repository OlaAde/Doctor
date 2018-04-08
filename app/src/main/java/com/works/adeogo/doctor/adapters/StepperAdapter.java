package com.works.adeogo.doctor.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;
import com.works.adeogo.doctor.FragmentReg1;
import com.works.adeogo.doctor.GigRegisterFragment;
import com.works.adeogo.doctor.ProfileRegistrationFragment;
import com.works.adeogo.doctor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adeogo on 11/20/2017.
 */

public class StepperAdapter extends AbstractFragmentStepAdapter {

    public List<String> strings = new ArrayList<>();
    private static final String CURRENT_STEP_POSITION_KEY = "messageResourceId";

    public StepperAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
//        switch (position){
//            case 0:
//                final FragmentReg1 step1 = new FragmentReg1();
//                Bundle b1 = new Bundle();
//                b1.putInt(CURRENT_STEP_POSITION_KEY, position);
//                step1.setArguments(b1);
//                strings.add(0, step1.getTag());
//                return step1;
//
//            case 1:
//                final ProfileRegistrationFragment step2 = new ProfileRegistrationFragment();
//                Bundle b2 = new Bundle();
//                b2.putInt(CURRENT_STEP_POSITION_KEY, position);
//                step2.setArguments(b2);
//                strings.add(1, step2.getTag());
//                return step2;
//
//            case 2:
//                final GigRegisterFragment step3 = new GigRegisterFragment();
//                Bundle b3 = new Bundle();
//                b3.putInt(CURRENT_STEP_POSITION_KEY, position);
//                step3.setArguments(b3);
//                strings.add(2, step3.getTag());
//                return step3;
//        }

        return null;
    }

    public List<String> getTags(){
        return strings;
    }

    @Override
    public int getCount() {
        return 3;
    }


//    @NonNull
//    @Override
//    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
//        //Override this method to set Step title for the Tabs, not necessary for other stepper types
//        switch (position){
//            case 0:
//                return new StepViewModel.Builder(context)
//                        .setTitle("Sign In Register") //can be a CharSequence instead
//                        .create();
//            case 1:
//                return new StepViewModel.Builder(context)
//                        .setTitle("Tabs 2") //can be a CharSequence instead
//                        .create();
//        }
//        return null;
//    }
}