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

public class PatientMealActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mDatabase;
    String CurrentUserName;
    String CurrentUserEmail;
    List<String> calorie_measure = new ArrayList<String>();
    private String Selected = " ";
    String breakfast_cal, lunch_cal, dinner_cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_meal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //getSetUserProfile();

        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String time = df.format(Calendar.getInstance().getTime());
        df = new SimpleDateFormat("MM/dd/yyyy");
        final String date = df.format(Calendar.getInstance().getTime());

        calorie_measure.add("SELECT");
        calorie_measure.add("CALORIE");

        Spinner _spinner1 = (Spinner) findViewById(R.id.spinner_breakfast);
        Spinner _spinner2 = (Spinner) findViewById(R.id.spinner_lunch);
        Spinner _spinner3 = (Spinner) findViewById(R.id.spinner_dinner);


        _spinner1.setOnItemSelectedListener(this);
        _spinner2.setOnItemSelectedListener(this);
        _spinner3.setOnItemSelectedListener(this);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, calorie_measure);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        _spinner1.setAdapter(dataAdapter);
        _spinner2.setAdapter(dataAdapter);
        _spinner3.setAdapter(dataAdapter);

        final EditText t_breakfast = (EditText)findViewById(R.id.editText_walk);
        final EditText t_lunch = (EditText)findViewById(R.id.editText_run);
        final EditText t_dinner = (EditText)findViewById(R.id.editText_workout);

        Button btn_ex_submit = (Button)findViewById(R.id.button_exercise);

        btn_ex_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                String breakfast = t_breakfast.getText().toString();

                String lunch = t_lunch.getText().toString();

                String dinner = t_dinner.getText().toString();
                //User current_user = new User(user.getUid(),new PatientRecord(walk,run,workout,aerobics));
                DatabaseReference q = mDatabase.child("patients").child("PatientData").child(user.getUid()).child("MealRecord").push();
                q.child("BreakfastCalorieIntake").setValue(breakfast);
                q.child("LunchCalorieIntake").setValue(lunch);
                q.child("DinnerCalorieIntake").setValue(dinner);
                q.child("RecordDate").setValue(date);
                q.child("RecordTime").setValue(time);

                finish();
                //startActivity(new Intent(getApplicationContext(), PatientExerciseActivity.class));
                Toast.makeText(getApplicationContext(),"Meal Information Added Successfully",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        int _id = parent.getId();
        if(_id == R.id.spinner_breakfast){
            breakfast_cal = parent.getItemAtPosition(position).toString();
        }
        else if(_id == R.id.spinner_lunch){
            lunch_cal = parent.getItemAtPosition(position).toString();
        }
        else if(_id == R.id.spinner_dinner){
            dinner_cal = parent.getItemAtPosition(position).toString();
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
        getMenuInflater().inflate(R.menu.patient_meal, menu);
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
