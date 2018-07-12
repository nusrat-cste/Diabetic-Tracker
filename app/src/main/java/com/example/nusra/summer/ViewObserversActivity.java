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

public class ViewObserversActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ListView lv;
    private ArrayList<String> Observers=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_observers);

        lv = (ListView) findViewById(R.id.lv_view_observers);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,Observers);

        lv.setAdapter(arrayAdapter);
        QueryAllObservers("Observer");
    }

    private void QueryAllObservers(String observers) {
        mAuth = FirebaseAuth.getInstance();

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        //final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Query query =   mDatabase.child("users").orderByChild("UserType").equalTo(observers);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null) {
                    if(dataSnapshot.getValue()!=null) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // String userName = snapshot.child("Name").getValue(String.class); works
                            User userName = snapshot.getValue(User.class);
                            Log.e("Arraylist", userName.Name);
                            Observers.add(userName.Name);
                            //Toast.makeText(getApplicationContext(), Pt.get(i++), Toast.LENGTH_SHORT).show();
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                      else{
                        Log.e("Arraylist", "No Observers Exist");
                        Toast.makeText(getApplicationContext(), "No Observers Exist",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Datasnapshot is null", Toast.LENGTH_SHORT).show();
                    Log.e("Arraylist", "Datasnapshot is null");
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
