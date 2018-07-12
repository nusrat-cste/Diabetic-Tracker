package com.example.nusra.summer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.ListView;
import android.widget.Toast;

import com.example.nusra.summer.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class DoctorPageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        private DatabaseReference mDatabase;
        String CurrentUserName;
        String CurrentUserEmail;
        private FirebaseAuth mAuth;
        private ArrayList<String> Pt = new ArrayList<>();
        private ArrayList<User> assignedPatients = new ArrayList<>();
        private ListView lv;
        private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_page);
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

        //lv_view_assigned_patients;
        lv = (ListView) findViewById(R.id.lv_view_assigned_patients);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,Pt);
        lv.setAdapter(arrayAdapter);

        //getSetUserProfile();
        QueryAllUserType();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                User aPatient = assignedPatients.get((int) id);
                Intent intent = new Intent(getApplicationContext(), APatientRecordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedPatient", aPatient);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private void QueryAllUserType() {
        mAuth = FirebaseAuth.getInstance();

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Query query =   mDatabase.child("users").orderByChild("AssignedDoctor").equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null) {
                    if(dataSnapshot.getValue()!=null) {
                        int i = 0;
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){ ;
                            // String userName = snapshot.child("Name").getValue(String.class); works
                            User userName = snapshot.getValue(User.class);
                            userName.UID = snapshot.getKey(); //assigns key to UID, because without assigning so , I was getting UID as null
                            Log.e("Arraylist", userName.Name+" "+userName.UID);
                            Pt.add(userName.Name) ;
                            assignedPatients.add(userName);
                            arrayAdapter.notifyDataSetChanged();
                            Log.e("Patient", Pt.get(i++));
                        }
                    }
                    else{
                        Log.e("Arraylist", "No Such Patients Exist");
                        Toast.makeText(getApplicationContext(), "No Patients Assigned",Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.doctor_page, menu);
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
