package com.example.nusra.summer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PatientBGActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;
    String CurrentUserName;
    String CurrentUserEmail;
    Button BGsubmit;
    EditText CurrentDate,CurrentTime,GlucoseReading;
    RadioGroup inputMealBefore;
    RadioButton inputMealBtn;
    String selectedMeal;
    List<String> Meals = new ArrayList<String>();
    List<String> Time = new ArrayList<String>();
    private String selectedTime;
    private String MealBefore;


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
        Meals.add("Breakfast");
        Meals.add("Lunch");
        Meals.add("Dinner");
        Time.add("Before");
        Time.add("After");

        final Spinner _spinner1 = (Spinner) findViewById(R.id.spinner_Meal);
        final Spinner _spinner2 = (Spinner) findViewById(R.id.spinner_time);

        _spinner1.setOnItemSelectedListener(this);
        _spinner2.setOnItemSelectedListener(this);

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Meals);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Time);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        _spinner1.setAdapter(dataAdapter1);
        _spinner2.setAdapter(dataAdapter2);

        BGsubmit = (Button) findViewById(R.id.btn_submit_bg);


        getSetUserProfile();
        
        BGsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                String glucose_reading = GlucoseReading.getText().toString();

                RadioGroup inputBgMetric = (RadioGroup)findViewById(R.id.radio_bg_metric);
                int selectedM = inputBgMetric.getCheckedRadioButtonId();
                RadioButton inputbgMetricBtn = (RadioButton) findViewById(selectedM);
                String BGMetric = inputbgMetricBtn.getText().toString();


                mDatabase2 = FirebaseDatabase.getInstance().getReference();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                DatabaseReference q = mDatabase2.child("patients").child("PatientData").child(user.getUid()).child("GlucoseRecord").push();
                q.child("RecordOfDate").setValue(currentDate);
                q.child("RecordOfTime").setValue(currentTime);

                if(selectedTime.equals("Before")){
                    Log.e("Meal",selectedMeal+selectedTime);
                q.child("RecordOfMealBefore").setValue(selectedMeal);
                q.child("RecordOfMealAfter").setValue("N/A");
                }
                else if(selectedTime.equals("After")){
                q.child("RecordOfMealAfter").setValue(selectedMeal);
                q.child("RecordOfMealBefore").setValue("N/A");
                }
                q.child("GlucoseLevel").setValue(glucose_reading);
                q.child("GlucoseLevelMetric").setValue(BGMetric);

                finish();
                Toast.makeText(getApplicationContext(),"Records added succesfully",Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int _id = parent.getId();
        if(_id == R.id.spinner_Meal){
            selectedMeal = parent.getItemAtPosition(position).toString();
            Log.e("Arraylist",selectedMeal);
        }
        else if(_id == R.id.spinner_time){
            selectedTime = parent.getItemAtPosition(position).toString();
            Log.e("Arraylist",selectedTime);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
