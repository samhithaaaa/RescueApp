package com.example.android.rescueandroidapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    //LocationManager locationManager;
    //LocationListener locationListener;

    //double latit,lng;

    private Toolbar maintoolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Adapter adapter;
    private FirebaseUser curruser;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth=FirebaseAuth.getInstance();

        curruser=auth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        maintoolbar = (Toolbar)findViewById(R.id.mainpage_toolbar);
        setSupportActionBar(maintoolbar);
        getSupportActionBar().setTitle("Rescue App");

        viewPager =(ViewPager)findViewById(R.id.main_tabs_pager);
        adapter = new Adapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout =(TabLayout)findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(viewPager);


    }


    @Override
    protected void onStart() {
        super.onStart();
        if(curruser==null)
        {
            Sendtologinactivity();
        }
        else {
            verifyUserExist();

        }
    }
    /* public double getLat()
    {
        Log.i("lat in","lat in getlat method: "+latit);
        return latit;
    }

    public double getLongit()
    {
        return lng;
    } */

    private void verifyUserExist() {
        String curUserId=auth.getCurrentUser().getUid();
        databaseReference.child("Users").child(curUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if((dataSnapshot.child("name").exists()))
                {
                    Toast.makeText(MainActivity.this,"Welcome",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent sintent=new Intent(MainActivity.this,SettingsActivity.class);
                    sintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(sintent);
                    finish();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.settings)
        {
            Intent sintent=new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(sintent);
        }
        if(item.getItemId()==R.id.help_guide)
        {
            Sendtohelpactivity();
        }
        if(item.getItemId()==R.id.Usersgroup)
        {
            Usersgroup();
        }
        if(item.getItemId()==R.id.logout)
        {
            auth.signOut();
            Sendtologinactivity();

        }
        return true;
    }

    private void Usersgroup() {
        databaseReference.child("UserGroup").child("Community").setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this,"Community group activated",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void Sendtologinactivity() {
        Intent intent =new Intent(MainActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void Sendtohelpactivity() {
        Intent intent1 =new Intent(MainActivity.this,HelpActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent1);
        finish();
    }

}
