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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserInfoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText inputName, inputNumber, inputDoB;
    private Button buttonSubmit;
    public String loginMessage = "";
    private RadioGroup inputSex;
    private String userType;
    private String leftDevice;
    private String rightDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        inputName =  (EditText)findViewById(R.id.name);
        inputNumber = (EditText)findViewById(R.id.contact);
        inputDoB = (EditText)findViewById(R.id.dob);
        buttonSubmit= (Button) findViewById(R.id.Btn_submit);

        Intent intent = getIntent();
        userType = intent.getExtras().getString("userType");

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSex = (RadioGroup) findViewById(R.id.radioSex);
                int selectedSex = inputSex.getCheckedRadioButtonId();
                RadioButton inputSexBtn = (RadioButton) findViewById(selectedSex);

                String name = inputName.getText().toString();
                String phoneNumber = inputNumber.getText().toString();
                String dateOfBirth = inputDoB.getText().toString();
                String Sex = inputSexBtn.getText().toString();
                //String UserType = inputUserTypeBtn.getText().toString();
                // get selected radio button from radioGroup


                FirebaseUser fuser = mAuth.getCurrentUser();

                User user = new User(name,phoneNumber,dateOfBirth);
                mDatabase.child("users").child(fuser.getUid()).child("Name").setValue(name);
                mDatabase.child("users").child(fuser.getUid()).child("PhoneNo").setValue(phoneNumber);
                mDatabase.child("users").child(fuser.getUid()).child("DateOfBirth").setValue(dateOfBirth);
                mDatabase.child("users").child(fuser.getUid()).child("Sex").setValue(Sex);
                mDatabase.child("users").child(fuser.getUid()).child("UserType").setValue(userType);
                mDatabase.child("users").child(fuser.getUid()).child("Device").child("Leftsole").setValue(Sex);
                mDatabase.child("users").child(fuser.getUid()).child("Device").child("Rightsole").setValue(userType);
                if(userType.equals("Patient"))
                    mDatabase.child("users").child(fuser.getUid()).child("AssignedDoctor").setValue("");
                finish();
                if(userType.equals("Patient"))

                    startActivity(new Intent(getApplicationContext(), PatientDataInputActivity.class));
                else if(userType.equals("Admin"))
                    startActivity(new Intent(getApplicationContext(), AdminPageActivity.class));
                if(userType.equals("Doctor"))
                    startActivity(new Intent(getApplicationContext(), DoctorPageActivity.class));
                else if(userType.equals("Observer"))
                    startActivity(new Intent(getApplicationContext(), ObserverPageActivity.class));
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
