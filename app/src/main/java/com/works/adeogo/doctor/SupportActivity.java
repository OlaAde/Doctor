package com.works.adeogo.doctor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.net.URLEncoder;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SupportActivity extends AppCompatActivity {

    private MaterialEditText mDetailsEditText, mSubjectEditText;
    private String mDetails, mSubject;
    private RelativeLayout mMainLayout;
    private ActionBar mActionBar;
    private TextView mThanksTextView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_support);

        mActionBar =  getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("Contact Us");

        mThanksTextView =findViewById(R.id.tvThanks);
        mDetailsEditText = findViewById(R.id.edtProblemDisc);
        mSubjectEditText = findViewById(R.id.edtSubject);
        mMainLayout = findViewById(R.id.support_main);

        mThanksTextView.setVisibility(View.GONE);
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

        if (id == R.id.action_contact_us) {
            mDetails = mDetailsEditText.getText().toString();
            mSubject = mSubjectEditText.getText().toString();

            if (TextUtils.isEmpty(mSubject) || TextUtils.equals(mSubject, "")){
                Toast.makeText(this, "Please enter a subject", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (TextUtils.isEmpty(mDetails) || TextUtils.equals(mDetails, "")){
                Toast.makeText(this, "Please describe your problem", Toast.LENGTH_SHORT).show();
            }else if (mDetails.length() < 10){
                Toast.makeText(this, "Please further describe your problem", Toast.LENGTH_SHORT).show();
            }else if (mDetails.length() > 10 && !TextUtils.isEmpty(mSubject) && !TextUtils.equals(mSubject, "")){

                String uriText = "mailto:hellodoktari@gmail.com" +
                        "?subject=" + URLEncoder.encode(mSubject) +
                        "&body=" + URLEncoder.encode(mDetails);
                Uri uri = Uri.parse(uriText);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                Intent intent1 = new Intent(SupportActivity.this, ThanksActivity.class);

                startActivity(Intent.createChooser(sendIntent, "Contact Us"));
                messageSent();

//                Intent intent = new Intent(Intent.ACTION_SENDTO);
//                intent.setType("text/plain");
//                intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { "hellodoktari@gmail.com" });
//                intent.putExtra(Intent.EXTRA_SUBJECT, mSubject);
//                intent.putExtra(Intent.EXTRA_TEXT, mDetails);
//
//                Toast.makeText(this, "Gone", Toast.LENGTH_SHORT).show();
//                startActivity(Intent.createChooser(intent, "Contact Us"));


//                Intent[] intents = {intent};
//                startActivities(intents);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

   private void messageSent(){
        mThanksTextView.setVisibility(View.VISIBLE);
        mDetailsEditText.setText("");
        mSubjectEditText.setText("");
   }
}
