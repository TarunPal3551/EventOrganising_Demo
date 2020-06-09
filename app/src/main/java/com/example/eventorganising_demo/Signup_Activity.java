package com.example.eventorganising_demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup_Activity extends AppCompatActivity {
    TextView textViewLogin;
    MaterialButton buttonSignup;
    TextInputLayout nameTextInputLayout, emailTextInputLayout, phoneTextInputLayout, passwordTextInputLayout;
    String name, email, moblie, password;
    FirebaseAuth mAuth;
    private static final String TAG = "Signup_Activity";
    ProgressDialog progressDialog;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        nameTextInputLayout = findViewById(R.id.textInputLayoutName);
        emailTextInputLayout = findViewById(R.id.textInputLayoutEmail);
        phoneTextInputLayout = findViewById(R.id.textInputLayoutPhone);
        passwordTextInputLayout = findViewById(R.id.textInputLayoutPassword);
        textViewLogin = findViewById(R.id.tvLogin);
        buttonSignup = findViewById(R.id.signupButton);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Signup_Activity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {

                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        User userModel = new User(name, email, moblie, "");
                                        mDatabase.child("users").child(user.getUid()).setValue(userModel);
                                        Toast.makeText(Signup_Activity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Signup_Activity.this, LoginActivity.class);
                                        startActivity(intent);
                                        //updateUI(user);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(Signup_Activity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                        // updateUI(null);
                                    }

                                    // ...
                                }
                            });

                }

            }
        });
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup_Activity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    public boolean validate() {
        name = nameTextInputLayout.getEditText().getText().toString();
        email = emailTextInputLayout.getEditText().getText().toString();
        moblie = phoneTextInputLayout.getEditText().getText().toString();
        password = passwordTextInputLayout.getEditText().getText().toString();
        if (name.isEmpty()) {
            nameTextInputLayout.setError("Required");
            return false;
        }
        if (email.isEmpty()) {
            emailTextInputLayout.setError("Required");
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTextInputLayout.setError("Wrong Email");
            return false;
        }
        if (moblie.isEmpty()) {
            phoneTextInputLayout.setError("Required");
            return false;
        }
        if (password.isEmpty()) {
            passwordTextInputLayout.setError("Required");
            return false;
        }

        return true;

    }

}