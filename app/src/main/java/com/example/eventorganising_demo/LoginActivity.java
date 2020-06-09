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

public class LoginActivity extends AppCompatActivity {
    TextInputLayout emailTextInputLayout, passwordTextInputLayout;
    String email, password;
    FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";
    MaterialButton button;
    TextView textViewLogin;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        textViewLogin = findViewById(R.id.tvLoginRegister);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, Signup_Activity.class);
                startActivity(intent);
            }
        });
        button = (MaterialButton) findViewById(R.id.loginButton);
        emailTextInputLayout = findViewById(R.id.textInputLayoutEmail);
        passwordTextInputLayout = findViewById(R.id.textInputLayoutPassword);
        mAuth = FirebaseAuth.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    progressDialog.show();
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                        // ...
                                    }

                                    // ...
                                }
                            });
                }
            }
        });
    }

    public boolean validate() {

        email = emailTextInputLayout.getEditText().getText().toString();
        password = passwordTextInputLayout.getEditText().getText().toString();
        password = passwordTextInputLayout.getEditText().getText().toString();

        if (email.isEmpty()) {
            emailTextInputLayout.setError("Required");
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTextInputLayout.setError("Wrong Email");
            return false;
        }

        if (password.isEmpty()) {
            passwordTextInputLayout.setError("Required");
            return false;
        }

        return true;

    }
}