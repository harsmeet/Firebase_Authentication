package com.example.harsmeet.harsmeet_firebase_auth;

import android.content.Intent;
import android.database.DatabaseUtils;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp_Activity extends AppCompatActivity implements View.OnClickListener {

    EditText SignUpEmail,SignUpPass,SignUpName;
    ProgressBar progressBar_signup;
    private FirebaseAuth mAuth;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mDatabase;
    String id;
    //UserSessionManager session;
    FirebaseDatabase firebaseDatabase;
    String name2;
    DatabaseReference databaseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        SignUp_Activity.this.getSupportActionBar().setTitle("Firebase Authentication");

        try {
            SignUpEmail = (EditText) findViewById(R.id.signUp_email);
            SignUpPass = (EditText) findViewById(R.id.signUp_pass);
            SignUpName = (EditText) findViewById(R.id.editTextName);
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference("User");
            progressBar_signup = (ProgressBar) findViewById(R.id.progressBar);
            findViewById(R.id.ButtonSignUp).setOnClickListener(this);
            findViewById(R.id.textView_login).setOnClickListener(this);


        }catch (Exception e){
         e.printStackTrace();
        }


        }
        public void registerUser() {
        String email = SignUpEmail.getText().toString().trim();
        String password = SignUpPass.getText().toString().trim();
        String name = SignUpName.getText().toString().trim();
        name2=name;

        if (!TextUtils.isEmpty(name)) {

            Toast.makeText(getApplication(),"Name added",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getApplicationContext(),"Name is required",Toast.LENGTH_SHORT).show();
        }

        if (email.isEmpty()) {
            SignUpEmail.setError("Email is required");
            SignUpEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            SignUpEmail.setError("Please enter a valid email");
            SignUpEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            SignUpPass.setError("Password is required");
            SignUpPass.requestFocus();
            return;
        }

        if (password.length() < 6) {
            SignUpPass.setError("Minimum lenght of password should be 6");
            SignUpPass.requestFocus();
            return;
        }
        progressBar_signup.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar_signup.setVisibility(View.GONE);

                try {
                    if (task.isSuccessful()) {

                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        id = user.getUid();
                        // Write a users to the database
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        mDatabase = firebaseDatabase.getReference().child("user");
                        Name user1 = new Name(name2);
                        mDatabase.child(id).setValue(user1);
                        firebaseAuth.getCurrentUser().sendEmailVerification();
                        Toast.makeText(SignUp_Activity.this, "verification email sent", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SignUp_Activity.this, MainActivity.class));
                    } else {

                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getApplicationContext(), "You are already registered !", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }



            }
        });

    }

         @Override
         public void onClick(View view) {
         switch (view.getId()) {
            case R.id.ButtonSignUp:
                registerUser();
                break;

            case R.id.textView_login:
                finish();
                startActivity(new Intent(SignUp_Activity.this, MainActivity.class));
                break;
        }
    }
}
