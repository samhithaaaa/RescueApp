package com.example.android.rescueandroidapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class CommunityActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private Button locButton;
    private ScrollView mScrollview;
    private static final int Request_User_Location_Code = 99;
    private TextView displaymessages;
    private String groupName;
    private FirebaseAuth auth;
    private String curuserID, curusername,currDate,currTime;
    private DatabaseReference usersref, groupnameRef, groupMessageKeyRef;
    double lat,longit;

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                 lat=location.getLatitude();
                 longit=location.getLongitude();

                Log.i("Latitude", "latitude: "+lat);
                Log.i("Longitude", "longitude: "+longit);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }

        };

        // If device is running SDK < 23

        if (Build.VERSION.SDK_INT < 23) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // ask for permission

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


            } else {

                // we have permission!

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }

        }




        groupName=getIntent().getExtras().get("Groupname").toString();
        Toast.makeText(CommunityActivity.this,"GroupName "+groupName,Toast.LENGTH_SHORT).show();

        auth=FirebaseAuth.getInstance();
        curuserID=auth.getCurrentUser().getUid();
        usersref= FirebaseDatabase.getInstance().getReference().child("Users");
        groupnameRef= FirebaseDatabase.getInstance().getReference().child("UserGroup").child(groupName);

        InitialiazeFields();

        GetUserInfo();

        locButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String address=getAddress(lat,longit);
                saveMessgetoDatabase(address);

            }
        });

    }

    private String getAddress(double lat, double longit) {
        String cityname="";
        String address="";
        if(lat!=0 && longit!=0)
        {
            Geocoder geocoder = new Geocoder(CommunityActivity.this, Locale.getDefault());
            try {
                List<Address> addressList=geocoder.getFromLocation(lat,longit,1);
                address= addressList.get(0).getAddressLine(0);
                cityname=addressList.get(0).getLocality();
                Log.i("address","address in method: "+address);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return address;
    }


    private void saveMessgetoDatabase(String address)
    {
        Log.i("address","address in saveMessage to Dtabase : "+address);
        String messageKey = groupnameRef.push().getKey();
        Calendar calenforDate = Calendar.getInstance();
        SimpleDateFormat curDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        currDate=curDateFormat.format(calenforDate.getTime());

        Calendar calforTime = Calendar.getInstance();
        SimpleDateFormat curTimeFormat = new SimpleDateFormat("hh:mm a");
        currTime=curTimeFormat.format(calforTime.getTime());

        HashMap<String,Object> groupmessageKey= new HashMap<>();
        groupnameRef.updateChildren(groupmessageKey);
        String slat=Double.toString(lat);
        String slongit=Double.toString(longit);

        groupMessageKeyRef= groupnameRef.child(messageKey);
        HashMap<String,Object> messageinfomap= new HashMap<>();
        messageinfomap.put("name",curusername);
        messageinfomap.put("Location",address);
        messageinfomap.put("message","Help Required Immediately");
        messageinfomap.put("lat",slat);
        messageinfomap.put("lng",slongit);
        messageinfomap.put("Date",currDate);
        messageinfomap.put("time",currTime);
        groupMessageKeyRef.updateChildren(messageinfomap);

        Log.i("lat","lat: "+lat);
        Log.i("lng","lng: "+longit);

    }

    private void GetUserInfo()
    {
        usersref.child(curuserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    curusername=dataSnapshot.child("name").getValue().toString();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        groupnameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                if(dataSnapshot.exists())
                {
                    displayAllMessages(dataSnapshot);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
                if(dataSnapshot.exists())
                {
                    displayAllMessages(dataSnapshot);
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void displayAllMessages(DataSnapshot dataSnapshot)
    {
        Iterator iterator =dataSnapshot.getChildren().iterator();
        while(iterator.hasNext())
        {
            String msgDate = (String) ((DataSnapshot)iterator.next()).getValue();
            String msgLocation = (String) ((DataSnapshot)iterator.next()).getValue();
            String msglat = (String) (((DataSnapshot)iterator.next()).getValue());
            String msglng = (String) ((DataSnapshot)iterator.next()).getValue();
            String msg = (String) ((DataSnapshot)iterator.next()).getValue();
            String msgName = (String) ((DataSnapshot)iterator.next()).getValue();
            String msgTime = (String) ((DataSnapshot)iterator.next()).getValue();

            displaymessages.append(msgName + ":\n"+ msg + "\n" +"Help Location: "+ msgLocation + "\n" +"Latitude: "+ msglat + "\n" + "Longitude: "+ msglng + "\n" + "Date: " + msgDate + "  " + "Time: "+msgTime + "\n\n\n");

        }
    }

    private void InitialiazeFields()
    {
        toolbar=(Toolbar)findViewById(R.id.community_loc_exchange);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(groupName);
        locButton=(Button)findViewById(R.id.loc_button);
        displaymessages=(TextView)findViewById(R.id.chat_display);
        mScrollview=(ScrollView)findViewById(R.id.my_scroll_view);

    }
}
