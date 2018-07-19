package com.example.harsmeet.harsmeet_firebase_auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edit_email,edit_pass;
    ProgressBar progressBar_login;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity.this.getSupportActionBar().setTitle("Firebase Authentication");

        mAuth = FirebaseAuth.getInstance();

        edit_email = (EditText)findViewById(R.id.editText_email);
        edit_pass = (EditText)findViewById(R.id.editText_pass);
        progressBar_login = (ProgressBar)findViewById(R.id.progressBar2);

        findViewById(R.id.textview_SignUp).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);

    }
    private void userLogin() {
        String email = edit_email.getText().toString().trim();
        String password = edit_pass.getText().toString().trim();

        if (email.isEmpty()) {
            edit_email.setError("Email is required");
            edit_email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edit_email.setError("Please enter a valid email");
            edit_email.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            edit_pass.setError("Password is required");
            edit_pass.requestFocus();
            return;
        }

        if (password.length() < 6) {
            edit_pass.setError("Minimum lenght of password should be 6");
            edit_pass.requestFocus();
            return;
        }
        progressBar_login.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar_login.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    if(mAuth.getCurrentUser().isEmailVerified())
                    {
                        Intent intent = new Intent(MainActivity.this, Welcome_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(),"heyyyyyy u login",Toast.LENGTH_LONG).show();
                    }
//                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    @Override


        public void onClick (View view){
        switch (view.getId()) {

                case R.id.textview_SignUp:
                    finish();
                    startActivity(new Intent(MainActivity.this, SignUp_Activity.class));
                    break;

                case R.id.login:
                    userLogin();
                    break;

            }
        }
    }




