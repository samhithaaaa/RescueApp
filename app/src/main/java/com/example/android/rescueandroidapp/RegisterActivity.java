package com.example.android.rescueandroidapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private Button CreateAccBtn;
    private EditText email,password;
    private TextView alreadyhaveacc;
    private FirebaseAuth mauth;
    private ProgressDialog loadingbar;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mauth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        Initializefields();

        alreadyhaveacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lintent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(lintent);
            }
        });

        CreateAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String semail = email.getText().toString();
        String spassword = password.getText().toString();
        if(TextUtils.isEmpty(semail))
        {
            Toast.makeText(this,"Enter email",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(spassword))
        {
            Toast.makeText(this,"Enter password",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingbar.setTitle("Creating account...");
            loadingbar.setMessage("Please wait");
            loadingbar.setCanceledOnTouchOutside(true);
            loadingbar.show();

            mauth.createUserWithEmailAndPassword(semail,spassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                String currUserId = mauth.getCurrentUser().getUid();
                                databaseReference.child("Users").child(currUserId).setValue("");
                                success();
                            }
                            else {
                                String message =task.getException().toString();
                                failure(message);
                            }
                        }
                    });

        }
    }

    private void failure(String message) {
        Toast.makeText(this,"Error: "+message,Toast.LENGTH_SHORT).show();
        loadingbar.dismiss();
    }

    private void success() {
        Intent mintent = new Intent(RegisterActivity.this,MainActivity.class);
        mintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mintent);
        finish();
        Toast.makeText(this,"Account created Successfully",Toast.LENGTH_SHORT).show();
        loadingbar.dismiss();
    }

    private void Initializefields() {
        CreateAccBtn = (Button)findViewById(R.id.register_button);
        password=(EditText)findViewById(R.id.register_password);
        email=(EditText)findViewById(R.id.register_email);
        alreadyhaveacc=(TextView)findViewById(R.id.already_have_account);
        loadingbar= new ProgressDialog(this);

    }
}
