package com.example.android.rescueandroidapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    private EditText username;
    private Button updatebtn;
    private String curUserId;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initializefields();

        auth=FirebaseAuth.getInstance();
        curUserId=auth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String susername = username.getText().toString();
                if(TextUtils.isEmpty(susername))
                {
                    Toast.makeText(SettingsActivity.this,"Username needed",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    HashMap<String,String> profile =new HashMap<>();
                    profile.put("userId",curUserId);
                    profile.put("name",susername);
                    databaseReference.child("Users").child(curUserId).setValue(profile)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {

                                    if(task.isSuccessful())
                                    {
                                        SendToMainactivity();
                                        Toast.makeText(SettingsActivity.this,"Profile Updated",Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        String message = task.getException().toString();
                                        Toast.makeText(SettingsActivity.this,"Error "+message,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
    }

    private void initializefields() {
        username=(EditText)findViewById(R.id.username);
        updatebtn=(Button)findViewById(R.id.upadatebtn);
    }
    private void SendToMainactivity() {
        Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
