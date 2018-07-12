package com.example.nusra.summer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.nusra.summer.Models.PatientBGRecord;
import com.example.nusra.summer.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PatientBGActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;
    String CurrentUserName;
    String CurrentUserEmail;
    Button BGsubmit;
    EditText CurrentDate,CurrentTime,GlucoseReading;
    RadioGroup inputMealBefore;
    RadioButton inputMealBtn;
    int selectedMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_bg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        CurrentTime = (EditText) findViewById(R.id.current_time);
        GlucoseReading = (EditText) findViewById(R.id.bg_reading);

        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        String time = df.format(Calendar.getInstance().getTime());
        CurrentTime.setText(time);

        CurrentDate = (EditText) findViewById(R.id.current_date);
        df = new SimpleDateFormat("MM/dd/yyyy");

        final String date = df.format(Calendar.getInstance().getTime());
        CurrentDate.setText(date);
        final String currentDate = CurrentDate.getText().toString();
        final String currentTime = CurrentTime.getText().toString();

        BGsubmit = (Button) findViewById(R.id.btn_submit_bg);


        getSetUserProfile();

        BGsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inputMealBefore = (RadioGroup)findViewById(R.id.radioMealBefore);
                selectedMeal = inputMealBefore.getCheckedRadioButtonId();
                inputMealBtn = (RadioButton) findViewById(selectedMeal);
                String MealBefore = inputMealBtn.getText().toString();
                String glucose_reading = GlucoseReading.getText().toString();

                RadioGroup inputBgMetric = (RadioGroup)findViewById(R.id.radio_bg_metric);
                int selectedMeal = inputBgMetric.getCheckedRadioButtonId();
                RadioButton inputbgMetricBtn = (RadioButton) findViewById(selectedMeal);
                String BGMetric = inputbgMetricBtn.getText().toString();


                mDatabase2 = FirebaseDatabase.getInstance().getReference();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                User current_user = new User(user.getUid(),new PatientBGRecord(glucose_reading,currentTime,MealBefore,currentDate,currentTime));
                DatabaseReference q = mDatabase2.child("patients").child("PatientData").child(user.getUid()).child("GlucoseRecord").push();
                q.child("RecordOfDate").setValue(currentDate);
                q.child("RecordOfTime").setValue(currentTime);
                q.child("RecordOfMealBefore").setValue(MealBefore);
                q.child("GlucoseLevel").setValue(glucose_reading);
                q.child("GlucoseLevelMetric").setValue(BGMetric);

                finish();
                startActivity(new Intent(getApplicationContext(), PatientDataInputActivity.class));
            }
        });
    }
    private void getSetUserProfile() {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CurrentUserName = dataSnapshot.child("Name").getValue(String.class);
                TextView tv1 = (TextView) findViewById(R.id.tv_user_name);
                tv1.setText(CurrentUserName);
                CurrentUserEmail = dataSnapshot.child("Email").getValue(String.class);
                TextView tv2 = (TextView) findViewById(R.id.tv_user_email);
                tv2.setText(CurrentUserEmail);
            }
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.patient_bg, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
