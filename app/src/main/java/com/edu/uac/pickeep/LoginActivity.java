package com.edu.uac.pickeep;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private Button signInBtn, registerBtn;
    EditText emailEt, passwordET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEt = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        signInBtn = findViewById(R.id.signInBtn);
        registerBtn = findViewById(R.id.registerBtn);

        initialize();

        signInBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);

    }

    private void initialize() {
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    Log.d("FirebaseUser", "USER LOGGED!! " + firebaseUser.getEmail());
                } else {
                    Log.d("FirebaseUser", "USER HAS FINISHED THE SESSION");
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signInBtn:
                signIn(emailEt.getText().toString(), passwordET.getText().toString());
                break;
            case R.id.registerBtn:
                createAccount(emailEt.getText().toString(), passwordET.getText().toString());
                break;
        }
    }

    private void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Error during login process", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private void createAccount(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Error during registration process", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
}