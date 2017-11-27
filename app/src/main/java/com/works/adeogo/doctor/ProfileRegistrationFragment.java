package com.works.adeogo.doctor;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jackandphantom.circularprogressbar.CircleProgressbar;
import com.squareup.picasso.Picasso;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileRegistrationFragment extends Fragment implements BlockingStep {

    private String mEmail, mPassword, mPhone, mName;

    private String mPhotoUrl ;
    private String mCountry;
    private String mSpeciality;
    private String mCity;

    private SendMessage1 SM;

    private static final int RC_PHOTO_PICKER = 12;

    private int country_chooser_int = 0;
    private LinearLayout mLinearlayout;

    private ImageView mPickImageV;
    private ImageView mResultImageV;
    private CircleProgressbar mProgressBar;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mProfilePhotosStorageReference;
    private Spinner mCountrySpinner;
    private Spinner mSpecialitySpinner;
    private Spinner mCitySpinner;
    private ArrayAdapter<CharSequence> mCityAdapter = null;
    private LinearLayout mCityLinearLayout;

    public ProfileRegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_registration, container, false);
        mLinearlayout = view.findViewById(R.id.second_fragment_ll);

        mCountrySpinner = view.findViewById(R.id.imvPickCountry);
        mSpecialitySpinner = view.findViewById(R.id.imvSelectSpeciality);
        mCitySpinner = view.findViewById(R.id.imvPickCity);
        mCityLinearLayout = view.findViewById(R.id.llPickCity);

        mPickImageV = view.findViewById(R.id.imvPickImage);
        mProgressBar = view.findViewById(R.id.progressBarImageView);
        mProgressBar.setVisibility(View.GONE);
        mPickImageV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//                mProgressBar.setVisibility(View.VISIBLE);
                mPickImageV.setVisibility(View.INVISIBLE);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });
        mResultImageV = view.findViewById(R.id.imvFinalImage);
        mFirebaseStorage = FirebaseStorage.getInstance();
        mProfilePhotosStorageReference = mFirebaseStorage.getReference().child("profile_photos");

        ArrayAdapter<CharSequence> mCountryAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.countries, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> mSpecialityAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.specialities, android.R.layout.simple_spinner_item);

        mCityAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.countries, android.R.layout.simple_spinner_item);
        mCountryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCitySpinner.setAdapter(mCountryAdapter);

        mSpecialityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpecialitySpinner.setAdapter(mSpecialityAdapter);

        mCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCityAdapter = null;
        mCitySpinner.setAdapter(mCityAdapter);
        mCountrySpinner.setAdapter(mCountryAdapter);



        mCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                String Text = item.toString();
                mCountry = Text;
                if (TextUtils.equals(Text, "Uganda")){
                    country_chooser_int = 1;
                    setCitySpinners(mCityLinearLayout,mCitySpinner );
                }else if (TextUtils.equals(Text, "Nigeria")){
                    country_chooser_int = 2;
                    setCitySpinners(mCityLinearLayout, mCitySpinner);
                }else if (TextUtils.equals(Text, "Russia")){
                    country_chooser_int = 3;
                    setCitySpinners(mCityLinearLayout, mCitySpinner);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        mSpecialitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                String Text = item.toString();
                mSpeciality = Text;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }

        });


        mCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                String Text = item.toString();
                mCity = Text;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            // Get a reference to store file at chat_photos/<FILENAME>
            StorageReference photoRef = mProfilePhotosStorageReference.child(selectedImageUri.getLastPathSegment());


            // Upload file to Firebase Storage
            photoRef.putFile(selectedImageUri)
                   .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                           mProgressBar.setVisibility(View.VISIBLE);
                           float upload = taskSnapshot.getBytesTransferred();
                           float total = taskSnapshot.getTotalByteCount();
                           float currentProgress = upload * 100/total;

                           mProgressBar.setProgress(currentProgress);
                       }
                   }) .addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {

                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // When the image has successfully uploaded, we get its download URL
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    mPhotoUrl = downloadUrl.toString();

//                    mProgressBar.setVisibility(View.VISIBLE);
                    mPickImageV.setVisibility(View.INVISIBLE);
                    mResultImageV.setVisibility(View.VISIBLE);
                    Picasso.with(getContext())
                            .load(mPhotoUrl)
                            .into(mResultImageV);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mPickImageV.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public List<String> setProfileDetails(View v){
        if (TextUtils.isEmpty(mPhotoUrl)){
            Snackbar.make(v, "Please pick a picture", Snackbar.LENGTH_SHORT)
                    .show();
            return null;
        }

        if (TextUtils.isEmpty(mCountry)){
            Snackbar.make(v, "Please pick your resident country", Snackbar.LENGTH_SHORT)
                    .show();
            return null;
        }

        if (TextUtils.isEmpty(mSpeciality)){
            Snackbar.make(v, "Please select your speciality", Snackbar.LENGTH_SHORT)
                    .show();
            return null;
        }

        if (TextUtils.isEmpty(mCity)){
            Snackbar.make(v, "Please pick your resident city", Snackbar.LENGTH_SHORT)
                    .show();
            return null;
        }

        List<String> retString = new ArrayList<>();
        retString.add(mPhotoUrl);
        retString.add(mCountry);
        retString.add(mSpeciality);
        retString.add(mCity);
        return retString;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    public void setCitySpinners(LinearLayout CityLinearLayout, Spinner spinner) {
        CityLinearLayout.setVisibility(View.VISIBLE);

        if(country_chooser_int  == 1 ){
            mCityAdapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.ugandan_cities, android.R.layout.simple_spinner_item);
        }
        else if(country_chooser_int == 2){
            mCityAdapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.nigerian_cities, android.R.layout.simple_spinner_item);
        }
        else if(country_chooser_int == 3){
            mCityAdapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.russian_cities, android.R.layout.simple_spinner_item);
        }

        spinner.setAdapter(mCityAdapter);

    }

    @Override
    public void onNextClicked(final StepperLayout.OnNextClickedCallback callback) {
        if (!check())
            return;
        SM.sendData(mEmail, mPassword, mName, mPhone, mPhotoUrl, mCountry, mSpeciality, mCity);
        callback.goToNextStep();
    }

    protected void displayReceivedData(String email, String password, String name, String phone)
    {
        mEmail = email;
        mPassword = password;
        mName = name;
        mPhone = phone;
    }

    interface SendMessage1 {
        void sendData(String email, String password, String name, String phone, String photoUrl, String country, String speciality, String city );
    }

    private boolean check() {

        if (TextUtils.isEmpty(mPhotoUrl)){
            Toast.makeText(getActivity(), "Please pick a picture", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mCountry)){
            Toast.makeText(getActivity(), "Please pick your resident country", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mSpeciality)){
            Toast.makeText(getActivity(), "Please select your speciality", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mCity)){
            Toast.makeText(getActivity(), "Please pick your resident city", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            SM = (ProfileRegistrationFragment.SendMessage1) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
//        callback.goToPrevStep();
    }
}
