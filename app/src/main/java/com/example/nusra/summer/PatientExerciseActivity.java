package com.example.nusra.summer;

import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class PatientExerciseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mDatabase;
    String CurrentUserName;
    String CurrentUserEmail;
    List<String> durationMetric = new ArrayList<String>();
    private String Selected = " ";
    String walk_T, run_T, work_T, aero_T;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_exercise);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSetUserProfile();

        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String time = df.format(Calendar.getInstance().getTime());
        df = new SimpleDateFormat("MM/dd/yyyy");
        final String date = df.format(Calendar.getInstance().getTime());

        durationMetric.add("SELECT");
        durationMetric.add("Minutes");
        durationMetric.add("Hours");
        Spinner _spinner1 = (Spinner) findViewById(R.id.spinner_walk);
        Spinner _spinner2 = (Spinner) findViewById(R.id.spinner_run);
        Spinner _spinner3 = (Spinner) findViewById(R.id.spinner_workout);
        Spinner _spinner4 = (Spinner) findViewById(R.id.spinner_aerobics);

        _spinner1.setOnItemSelectedListener(this);
        _spinner2.setOnItemSelectedListener(this);
        _spinner3.setOnItemSelectedListener(this);
        _spinner4.setOnItemSelectedListener(this);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, durationMetric);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        _spinner1.setAdapter(dataAdapter);
        _spinner2.setAdapter(dataAdapter);
        _spinner3.setAdapter(dataAdapter);
        _spinner4.setAdapter(dataAdapter);

        final EditText t_walk = (EditText)findViewById(R.id.editText_walk);
        final EditText t_run = (EditText)findViewById(R.id.editText_run);
        final EditText t_workout = (EditText)findViewById(R.id.editText_workout);
        final EditText t_aerobics = (EditText)findViewById(R.id.editText_aerobics);

        Button btn_ex_submit = (Button)findViewById(R.id.button_exercise);

        btn_ex_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                String walk = t_walk.getText().toString();

                if (walk_T.equals("Hours")){
                    int walk_time = Integer.parseInt(walk)*60;
                    walk = String.valueOf(walk_time);
                }

                String run = t_run.getText().toString();
                if (run_T.equals("Hours")){
                    int run_time = Integer.parseInt(run)*60;
                    run = String.valueOf(run_time);
                }
                String workout = t_workout.getText().toString();
                if (work_T.equals("Hours")){
                    int work_time = (Integer.parseInt(workout))*60;
                    workout = String.valueOf(work_time);
                }
                String aerobics = t_aerobics.getText().toString();
                if (aero_T.equals("Hours")){
                    int aero_time = Integer.parseInt(aerobics)*60;
                    aerobics = String.valueOf(aero_time);
                }
                //User current_user = new User(user.getUid(),new PatientRecord(walk,run,workout,aerobics));
                DatabaseReference q = mDatabase.child("patients").child("PatientData").child(user.getUid()).child("ExerciseRecord").push();
                q.child("WalkingDuration").setValue(walk);
                q.child("RunningDuration").setValue(run);
                q.child("WorkoutDuration").setValue(workout);
                q.child("AerobicsDuration").setValue(aerobics);
                q.child("RecordDate").setValue(date);
                q.child("RecordTime").setValue(time);

                finish();
                //startActivity(new Intent(getApplicationContext(), PatientExerciseActivity.class));
                Toast.makeText(getApplicationContext(),"Exercise Information Successfully added",Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        int _id = parent.getId();
        if(_id == R.id.spinner_walk){
            walk_T = parent.getItemAtPosition(position).toString();
        }
        else if(_id == R.id.spinner_run){
            run_T = parent.getItemAtPosition(position).toString();
        }
        else if(_id == R.id.spinner_workout){
            work_T = parent.getItemAtPosition(position).toString();
        }
        else if(_id == R.id.spinner_aerobics){
            aero_T = parent.getItemAtPosition(position).toString();
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {

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
        getMenuInflater().inflate(R.menu.patient_exercise, menu);
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
