package com.example.nusra.summer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nusra.summer.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText inputName, inputNumber, inputDoB;
    private Button buttonSubmit;
    public String loginMessage = "";
    private RadioGroup inputSex;
    private RadioGroup inputUserType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        mAuth = FirebaseAuth.getInstance();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser fuser = mAuth.getCurrentUser();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        inputName =  (EditText)findViewById(R.id.name);
        inputNumber = (EditText)findViewById(R.id.contact);
        inputDoB = (EditText)findViewById(R.id.dob);
        buttonSubmit= (Button) findViewById(R.id.Btn_submit);

        mDatabase.child("users").child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                String CurrentUserName = dataSnapshot.child("Name").getValue().toString();
                inputName.setText(CurrentUserName);
                String CurrentUserPhoneNo = dataSnapshot.child("PhoneNo").getValue().toString();
                inputNumber.setText(CurrentUserPhoneNo);
                String CurrentUserDOB = dataSnapshot.child("DateOfBirth").getValue().toString();
                inputDoB.setText(CurrentUserDOB);
                String CurrentUserSex = dataSnapshot.child("Sex").getValue().toString();

                if(CurrentUserSex.equalsIgnoreCase("Male")){
                    RadioButton mGenderMan=(RadioButton) findViewById(R.id.radioMale);
                    mGenderMan.setChecked(true);
                }else if(CurrentUserSex.equalsIgnoreCase("Female")){
                    RadioButton mGenderWoman=(RadioButton) findViewById(R.id.radioFemale);
                    mGenderWoman.setChecked(true);
                }

                String CurrentUserType = dataSnapshot.child("UserType").getValue().toString();
                if(CurrentUserType.equalsIgnoreCase("Doctor")){
                    RadioButton mTypeDoctor=(RadioButton) findViewById(R.id.radioDoctor);
                    mTypeDoctor.setChecked(true);
                }else if(CurrentUserType.equalsIgnoreCase("Patient")){
                    RadioButton mTypePatient=(RadioButton) findViewById(R.id.radioPatient);
                    mTypePatient.setChecked(true);
                }
                else if(CurrentUserType.equalsIgnoreCase("Observer")){
                    RadioButton mTypeObserver=(RadioButton) findViewById(R.id.radioObserver);
                    mTypeObserver.setChecked(true);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSex = (RadioGroup) findViewById(R.id.radioSex);
                inputUserType = (RadioGroup) findViewById(R.id.radioUserType);
                int selectedSex = inputSex.getCheckedRadioButtonId();
                int selectedUserType = inputUserType.getCheckedRadioButtonId();
                RadioButton inputSexBtn = (RadioButton) findViewById(selectedSex);
                RadioButton inputUserTypeBtn = (RadioButton) findViewById(selectedUserType);

                String name = inputName.getText().toString();
                String phoneNumber = inputNumber.getText().toString();
                String dateOfBirth = inputDoB.getText().toString();
                String Sex = inputSexBtn.getText().toString();
                String UserType = inputUserTypeBtn.getText().toString();
                // get selected radio button from radioGroup

                User user = new User(name,phoneNumber,dateOfBirth);
                mDatabase.child("users").child(fuser.getUid()).child("Name").setValue(name);
                mDatabase.child("users").child(fuser.getUid()).child("PhoneNo").setValue(phoneNumber);
                mDatabase.child("users").child(fuser.getUid()).child("DateOfBirth").setValue(dateOfBirth);
                mDatabase.child("users").child(fuser.getUid()).child("Sex").setValue(Sex);
                mDatabase.child("users").child(fuser.getUid()).child("UserType").setValue(UserType);

                finish();
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            loginMessage = "Logged in as "+ currentUser.getEmail();
            TextView tv = (TextView) findViewById(R.id.login_message);
            tv.setText(loginMessage);
        }
        else
        {
            Toast.makeText(this, "no user found", Toast.LENGTH_SHORT).show();
        }
    }
}
