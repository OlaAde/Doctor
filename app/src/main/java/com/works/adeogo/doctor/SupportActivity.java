package com.works.adeogo.doctor;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.works.adeogo.doctor.R;

import dmax.dialog.SpotsDialog;

public class SupportActivity extends AppCompatActivity {

    private MaterialEditText mDetailsEditText, mSubjectEditText;
    private String mDetails, mSubject;
    private LinearLayout mMainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDetailsEditText = findViewById(R.id.edtProblemDisc);
        mSubjectEditText = findViewById(R.id.edtSubject);
        mMainLayout = findViewById(R.id.support_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_us_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_contact_us) {
            mDetails = mDetailsEditText.getText().toString();
            mSubject = mSubjectEditText.getText().toString();

            if (TextUtils.isEmpty(mSubject) || TextUtils.equals(mSubject, "")){
                Snackbar.make(mMainLayout, "Please enter a subject", Snackbar.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(mDetails) || TextUtils.equals(mDetails, "")){
                Snackbar.make(mMainLayout, "Please describe your problem", Snackbar.LENGTH_SHORT).show();
            }else if (mDetails.length() < 50){
                Snackbar.make(mMainLayout, "Please further describe your problem", Snackbar.LENGTH_SHORT).show();
            }else if (mDetails.length() > 50 && !TextUtils.isEmpty(mSubject) && !TextUtils.equals(mSubject, "")){
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, "adeogo.oladipo@gmail.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, mSubject);
                intent.putExtra(Intent.EXTRA_TEXT, mDetails);

                startActivity(Intent.createChooser(intent, "Contact Us"));
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
