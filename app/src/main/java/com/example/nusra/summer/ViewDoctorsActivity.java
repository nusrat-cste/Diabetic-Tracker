package com.example.nusra.summer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nusra.summer.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewDoctorsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private  ListView lv;
    private ArrayList<String> Doctors=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_doctors);
        lv = (ListView) findViewById(R.id.lv_view_doctors);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,Doctors);
        lv.setAdapter(arrayAdapter);

        QueryAllDoctors("Doctor");
    }

    private void QueryAllDoctors(String doctor) {
        mAuth = FirebaseAuth.getInstance();

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query =   mDatabase.child("users").orderByChild("UserType").equalTo(doctor);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                if(dataSnapshot.getValue()!=null) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        User userName = snapshot.getValue(User.class);
                        Log.e("Arraylist", userName.Name);
                        Doctors.add(userName.Name) ;
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
                else{
                    Log.e("Arraylist", "No Doctors Exist");
                    Toast.makeText(getApplicationContext(), "No Doctors Exist",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                    Log.e("Arraylist", "Datasnapshot does not exist");
                    Toast.makeText(getApplicationContext(), "Datasnapshot does not exist",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //toastMsg(databaseError.getMessage());
                Log.e("errorOfAddvalue", "onCancelled: " + databaseError.getMessage());
            }
        });
    }
}
