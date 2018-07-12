package com.example.nusra.summer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nusra.summer.Models.PatientBGRecord;
import com.example.nusra.summer.Models.PatientExerciseRecord;
import com.example.nusra.summer.Models.PatientMealRecord;
import com.example.nusra.summer.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class APatientRecordActivity extends AppCompatActivity {
  User _selectedPatient;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mDatabase;
    String CurrentUserName;
    String CurrentUserEmail;
    private FirebaseAuth mAuth;
    private ArrayList<PatientBGRecord> BG = new ArrayList<>();
    private ListView lv;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<PatientExerciseRecord> Ex = new ArrayList<>();
    private ArrayList<PatientMealRecord> Ml = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apatient_record);

        Intent intent = this.getIntent();
        _selectedPatient = (User) intent.getExtras().getSerializable("selectedPatient");
        Log.e("List","name :"+_selectedPatient.Name);

        Toast.makeText(getApplicationContext(),"You selected the patient "+_selectedPatient.Name,Toast.LENGTH_SHORT).show();

        Button viewBG = (Button)findViewById(R.id.button_view_bg);
        viewBG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();

                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                //final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //Log.e("Arraylist","patientUID "+ _selectedPatient.UID);
                getRecords("GlucoseRecord");
            }
        });

        Button viewEX = (Button)findViewById(R.id.button_view_ex);
        viewEX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();

                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                //final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //Log.e("Arraylist","patientUID "+ _selectedPatient.UID);
                getRecords("ExerciseRecord");
            }
        });

        Button viewMl = (Button)findViewById(R.id.button_view_meal);
        viewMl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("Arraylist","patientUID "+ _selectedPatient.UID);
                getRecords("MealRecord");
            }
        });
    }

    public void getRecords(final String recordtype){
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    Query query =   mDatabase.child("patients").child("PatientData").child(_selectedPatient.UID).child(recordtype).orderByKey();

                    query.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot!=null) {
            if(dataSnapshot.getValue()!=null) {
            //Log.e("Arraylist","BGlR "+dataSnapshot.getChildren());
            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
            //Log.e("Arraylist","BG"+snapshot.getValue().toString());
                if(recordtype.equals("GlucoseRecord")) {
                    PatientBGRecord UserRecord = snapshot.getValue(PatientBGRecord.class);
                    Log.e("Arraylist", "Bg =" + UserRecord.GlucoseLevel + " " + UserRecord.GlucoseLevelMetric + " date and time = " + UserRecord.RecordOfTime + " " + UserRecord.RecordOfDate + " record Taken Before = " + UserRecord.RecordOfMealBefore);
                    BG.add(UserRecord);
                }
                else if(recordtype.equals("ExerciseRecord")) {
                    PatientExerciseRecord UR = snapshot.getValue(PatientExerciseRecord.class);
                    Log.e("Arraylist","All exercise records");
                    Log.e("Arraylist", "Walking "+UR.WalkingDuration);
                    Log.e("Arraylist", "Running "+UR.RunningDuration);
                    Log.e("Arraylist", "Workout "+UR.WorkoutDuration);
                    Log.e("Arraylist", "Aerobics "+UR.AerobicsDuration);
                    Log.e("Arraylist", "Time and date "+UR.RecordTime +" "+UR.RecordDate);
                    Ex.add(UR);
                }
                else if(recordtype.equals("MealRecord")) {
                    PatientMealRecord UserRecord = snapshot.getValue(PatientMealRecord.class);
                    Log.e("Arraylist","All meal records");
                    Log.e("Arraylist", "Walking "+UserRecord.BreakfastCalorieIntake);
                    Log.e("Arraylist", "Running "+UserRecord.LunchCalorieIntake);
                    Log.e("Arraylist", "Workout "+UserRecord.DinnerCalorieIntake);
                    Log.e("Arraylist", "Time and date "+UserRecord.RecordTime +" "+UserRecord.RecordDate);
                    Ml.add(UserRecord);
                }
            }
            }
            else{
            Log.e("Arraylist", "No Data Exist");
            //Toast.makeText(getApplicationContext(), "No Observers Exist",Toast.LENGTH_SHORT).show();
            }

            }
            else
            Toast.makeText(getApplicationContext(), "Datasnapshot is null", Toast.LENGTH_SHORT).show();
            }

    @Override
    public void onCancelled(DatabaseError databaseError) {
            //toastMsg(databaseError.getMessage());
            Log.e("errorOfAddvalue", "onCancelled: " + databaseError.getMessage());

    }
                    });
    }
}