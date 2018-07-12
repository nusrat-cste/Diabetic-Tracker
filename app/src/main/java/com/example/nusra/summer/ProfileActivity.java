package com.example.nusra.summer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mDatabase;
    String CurrentUserName;
    String CurrentUserEmail;
    String CurrentUserDOB;
    String CurrentUserPhoneNo;
    String CurrentUserSex;
    String CurrentUserType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").child(user.getUid()).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CurrentUserName = dataSnapshot.getValue(String.class);
                TextView tv1 = (TextView) findViewById(R.id.current_user_name);
                tv1.setText(CurrentUserName);
                //Toast.makeText(ProfileActivity.this,CurrentUserName+" ",Toast.LENGTH_SHORT).show();
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase.child("users").child(user.getUid()).child("DateOfBirth").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CurrentUserDOB = dataSnapshot.getValue(String.class);
                TextView tv2 = (TextView) findViewById(R.id.current_user_dob);
                tv2.setText(CurrentUserDOB);
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase.child("users").child(user.getUid()).child("Email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CurrentUserEmail = dataSnapshot.getValue(String.class);
                TextView tv3 = (TextView) findViewById(R.id.current_user_email);
                tv3.setText(CurrentUserEmail);
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase.child("users").child(user.getUid()).child("PhoneNo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CurrentUserPhoneNo = dataSnapshot.getValue(String.class);
                TextView tv4 = (TextView) findViewById(R.id.current_user_phone_no);
                tv4.setText(CurrentUserPhoneNo);
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mDatabase.child("users").child(user.getUid()).child("Sex").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CurrentUserSex = dataSnapshot.getValue(String.class);
                TextView tv5 = (TextView) findViewById(R.id.current_user_sex);
                tv5.setText(CurrentUserSex);
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mDatabase.child("users").child(user.getUid()).child("UserType").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CurrentUserType = dataSnapshot.getValue(String.class);
                TextView tv6 = (TextView) findViewById(R.id.current_user_Type);
                tv6.setText(CurrentUserType);
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}

