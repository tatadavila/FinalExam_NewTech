package com.edu.uac.pickeep;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    Button logOutBtn, takePicBtn;
    TextView userEmailTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logOutBtn = findViewById(R.id.logOutBtn);
        takePicBtn = findViewById(R.id.takePicBtn);
        userEmailTV = findViewById(R.id.userEmailTV);

        initialize();
    }

    private void initialize() {
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    userEmailTV.setText("User Email: " + firebaseUser.getEmail());
                } else {
                    Log.d("FirebaseUser", "USER HAS FINISHED THE SESSION");
                }
            }
        };

        logOutBtn.setOnClickListener(this);
        takePicBtn.setOnClickListener(this);
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
            case R.id.logOutBtn:
                firebaseAuth.signOut();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.takePicBtn:
                Intent i2 = new Intent(MainActivity.this, TakePicActivity.class);
                startActivity(i2);
                break;
        }
    }
}