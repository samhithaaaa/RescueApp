package com.example.android.rescueandroidapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private Button LoginBtn,Phoneloginbtn;
    private EditText email,password;
    private TextView signup, forgotpass;
    private FirebaseAuth auth;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth=FirebaseAuth.getInstance();


        Initializefields();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rintent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(rintent);

            }
        });
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserLogin();
            }
        });
    }

    private void UserLogin() {
        String semail = email.getText().toString();
        String spass=password.getText().toString();
        if(TextUtils.isEmpty(semail))
        {
            Toast.makeText(this,"Enter email",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(spass))
        {
            Toast.makeText(this,"Enter password",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingbar.setTitle("Login");
            loadingbar.setMessage("Please wait");
            loadingbar.setCanceledOnTouchOutside(true);
            loadingbar.show();

            auth.signInWithEmailAndPassword(semail,spass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                SendToMainactivity();
                                loadingbar.dismiss();
                            }
                            else {
                                String message =task.getException().toString();
                                failure(message);
                                loadingbar.dismiss();
                            }
                        }
                    });

        }
    }

    private void failure(String message) {
        Toast.makeText(this,"Login failed "+message,Toast.LENGTH_SHORT).show();
    }

    private void Initializefields() {
        LoginBtn = (Button)findViewById(R.id.login_button);
        email=(EditText)findViewById(R.id.login_email);
        password=(EditText)findViewById(R.id.login_password);
        signup=(TextView)findViewById(R.id.signup);
        forgotpass=(TextView)findViewById(R.id.forgot_password);
        loadingbar= new ProgressDialog(this);
    }


    private void SendToMainactivity() {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
