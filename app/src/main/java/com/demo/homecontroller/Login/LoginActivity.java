package com.demo.homecontroller.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.demo.homecontroller.R;
import com.demo.homecontroller.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {
    private String email,password;
    private EditText usernameEditText,passwordEditText;
    private Button loginButton;
    private ProgressBar loadingProgressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDB;
    private FirebaseDatabase database;
    private FirebaseUser mCurrentUser;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
//        database.setPersistenceEnabled(true); //persistence automatically handles offline behavior
        usersDB = database.getReference("Users");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                email = usernameEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();
                Log.d("login",email+" :: "+password);
                mAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    final FirebaseUser user = mAuth.getCurrentUser();
                                    final String uid = user.getUid();

                                    usersDB.child(uid).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                User user = dataSnapshot.getValue(User.class);
                                                final SharedPreferences sharedPreferences = getSharedPreferences("LocalUserPermaStore", MODE_PRIVATE);
                                                String string = new Gson().toJson(user);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("myUserDetails", string);
                                                editor.commit();
                                                sendUserToHome();
                                            } else {
                                                senduserToReg();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                                else {
                                    Log.d("login","creation on new user");
                                    mAuth.createUserWithEmailAndPassword(email,password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()){
                                                        final FirebaseUser user = mAuth.getCurrentUser();
                                                        final String uid = user.getUid();

                                                        usersDB.child(uid).addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.exists()) {
                                                                    User user = dataSnapshot.getValue(User.class);
                                                                    final SharedPreferences sharedPreferences = getSharedPreferences("LocalUserPermaStore", MODE_PRIVATE);
                                                                    String string = new Gson().toJson(user);
                                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                    editor.putString("myUserDetails", string);
                                                                    editor.commit();
                                                                    sendUserToHome();
                                                                } else {
                                                                    senduserToReg();
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                }

                            }
                        });

            }
        });
    }
    public void sendUserToHome() {
        Intent homeIntent = new Intent(LoginActivity.this, Onboarding.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }

    //Function to send the user to Registration Page
    private void senduserToReg() {
        Intent homeIntent = new Intent(LoginActivity.this, GatherDetails.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}