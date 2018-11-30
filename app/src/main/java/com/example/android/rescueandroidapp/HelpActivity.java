package com.example.android.rescueandroidapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HelpActivity extends AppCompatActivity {


    Spinner spinner;
    Spinner spinner2;
    Spinner spinner3;

    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<CharSequence> adapter2;
    ArrayAdapter<CharSequence> adapter3;

    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity);
        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.incidents, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.helplines, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        adapter3 = ArrayAdapter.createFromResource(this, R.array.medicalhelp, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);




        btn = (Button) findViewById(R.id.exitBt);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToMainactivity();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String situation = (String) spinner.getItemAtPosition(position);
                String url = "help";
                String type = "Emergency";
                BackgroundWorker backgroundWorker = new BackgroundWorker(HelpActivity.this);
                backgroundWorker.execute(type,situation,url);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String situation = (String) spinner2.getItemAtPosition(position);
                String url = "help";
                String type = "helplines";
                String city="";
                BackgroundWorker2 backgroundWorker = new BackgroundWorker2(HelpActivity.this);


                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},1000);
                }else{
                    LocationManager locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try{
                        city= hereLocation(location.getLatitude(), location.getLongitude());

                    } catch (Exception e){
                        city="Not Found1";
                    }

                }

                backgroundWorker.execute(type,situation,city);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String situation = (String) spinner3.getItemAtPosition(position);
                String type = "medicalhelp";
                String city="";
                BackgroundWorker3 backgroundWorker = new BackgroundWorker3(HelpActivity.this);


                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},1000);
                }else{
                    LocationManager locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try{
                        city= hereLocation(location.getLatitude(), location.getLongitude());

                    } catch (Exception e){
                        city="Not Found1";
                    }

                }

                backgroundWorker.execute(type,situation,city);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });












    }


    private void SendToMainactivity() {
        Intent intent = new Intent(HelpActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){


        switch(requestCode){
            case 1000:{
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    LocationManager locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try{
                        String city= hereLocation(location.getLatitude(), location.getLongitude());

                    } catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(this, "Not Found2", Toast.LENGTH_SHORT).show();
                    }
                }else
                {
                    Toast.makeText(this, "Permission not granted!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private String hereLocation(double lat, double lon){
        String cityName = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try{
            addresses = geocoder.getFromLocation(lat,lon,10);
            if(addresses.size() > 0){
                for(Address adr:addresses){
                    if(adr.getLocality() != null && adr.getLocality().length()>0){
                        cityName=adr.getLocality();
                        break;
                    }
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return cityName;
    }




}


