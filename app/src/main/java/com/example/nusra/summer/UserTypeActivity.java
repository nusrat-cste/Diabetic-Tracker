package com.example.nusra.summer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class UserTypeActivity extends AppCompatActivity {
    private Button btnAdmin, btnDoctor, btnPatient, btnObserver;
    private String _UserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        btnAdmin = (Button)findViewById(R.id.button_admin);
        btnDoctor = (Button)findViewById(R.id.button_doctor);
        btnPatient = (Button)findViewById(R.id.button_patient);
        btnObserver = (Button)findViewById(R.id.button_observer);

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _UserType = btnAdmin.getText().toString();
                Intent i = new Intent (UserTypeActivity.this, MainActivity.class);
                //Log.d("Utype",_UserType);
                i = i.putExtra("userType",_UserType);
                startActivity(i);
            }
        });

        btnDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _UserType = btnDoctor.getText().toString();
                Intent i = new Intent (UserTypeActivity.this, MainActivity.class);
                Log.d("Utype",_UserType);
                i = i.putExtra("userType",_UserType);
                startActivity(i);
            }
        });

        btnPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _UserType = btnPatient.getText().toString();
                Intent i = new Intent (UserTypeActivity.this, MainActivity.class);
                Log.d("Utype",_UserType);
                i = i.putExtra("userType",_UserType);
                startActivity(i);
            }
        });

        btnObserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _UserType = btnObserver.getText().toString();
                Intent i = new Intent (UserTypeActivity.this, MainActivity.class);
                Log.d("Utype",_UserType);
                i = i.putExtra("userType",_UserType);
                startActivity(i);
            }
        });



    }

}
