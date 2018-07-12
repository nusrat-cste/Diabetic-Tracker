package com.example.nusra.summer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nusra.summer.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private String userType;
    private EditText inputEmail, inputPassword;
    private Button  btnSignUp,btnSignIn;
    private ProgressBar progressBar;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = this.getIntent();
        userType = intent.getExtras().getString("userType");

        Log.d("UserType",userType);
        inputEmail =  (EditText)findViewById(R.id.field_email);
        inputPassword = (EditText)findViewById(R.id.field_password);
        btnSignUp = (Button) findViewById(R.id.email_create_account_button);
        progressBar = (ProgressBar)findViewById(R.id.progressbar_Sign_inup) ;
        progressBar.setVisibility(View.INVISIBLE);

        btnSignUp.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {
               progressBar.setVisibility(View.VISIBLE);
               RegisterUser();
           }
        });

        btnSignIn = (Button) findViewById(R.id.email_sign_in_button);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               progressBar.setVisibility(View.VISIBLE);
               LoginUser();
           }
        });
    }

    private void LoginUser() {
        final String Email = inputEmail.getText().toString().trim();
        final String Password = inputPassword.getText().toString().trim();

        try {
            mAuth.signInWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                progressBar.setVisibility(View.INVISIBLE);
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(MainActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                if(userType.equals("Admin"))
                                    startActivity(new Intent(getApplicationContext(), AdminPageActivity.class));
                                else if(userType.equals("Patient"))
                                    startActivity(new Intent(getApplicationContext(), PatientDataInputActivity.class));
                                else if(userType.equals("Doctor"))
                                    startActivity(new Intent(getApplicationContext(), DoctorPageActivity.class));
                                else if(userType.equals("Observer"))
                                    startActivity(new Intent(getApplicationContext(), ObserverPageActivity.class));

                            } else{
                                progressBar.setVisibility(View.INVISIBLE);
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();                                }

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void RegisterUser() {

        final String Email = inputEmail.getText().toString().trim();
        final String Password = inputPassword.getText().toString().trim();
        if (TextUtils.isEmpty(Email)){
            Toast.makeText(this, "Email Cannot Be Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(Password)){
            Toast.makeText(this, "Password Cannot Be Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {
                            //check if successful
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                //User is successfully registered and logged in
                                //start Profile Activity here
                                Toast.makeText(MainActivity.this, "registration successful",
                                        Toast.LENGTH_SHORT).show();


                                FirebaseUser fuser = mAuth.getCurrentUser();
                                User user = new User(Email);
                                mDatabase.child("users").child(fuser.getUid()).child("Email").setValue(Email);

                                finish();

                                Intent i = new Intent (getApplicationContext(), UserInfoActivity.class);
                                i = i.putExtra("userType", userType);

                                startActivity(i);
                            }
                            else{
                                progressBar.setVisibility(View.INVISIBLE);
                                String error = task.getException().getMessage();
                                Toast.makeText(MainActivity.this, error,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }
}
