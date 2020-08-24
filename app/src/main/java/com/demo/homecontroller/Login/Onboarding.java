package com.demo.homecontroller.Login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.homecontroller.MainActivity;
import com.demo.homecontroller.R;
import com.demo.homecontroller.SessionStorage;
import com.demo.homecontroller.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Onboarding extends AppCompatActivity {
        CountDownTimer countDownTimer;
        private FirebaseAuth currentUser;
        private FirebaseDatabase database;
        private DatabaseReference usersDB;
        public static final String TAG = Onboarding.class.getSimpleName();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.WHITE);
            }
            requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
            getSupportActionBar().hide();
            setContentView(R.layout.activity_onboard);
            currentUser= FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();

                    if(FirebaseAuth.getInstance().getCurrentUser() != null){
                        usersDB = database.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        usersDB.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    User user = dataSnapshot.getValue(User.class);
                                    SessionStorage.saveUser(Onboarding.this, user);
                                    startActivity(new Intent(Onboarding.this, MainActivity.class));
                                    finish();
                                } else {
                                    startActivity(new Intent(Onboarding.this, LoginActivity.class));
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                startActivity(new Intent(Onboarding.this, LoginActivity.class));
    //                    startActivity(new Intent(MainActivity.this, EntryPage.class));
                                finish();
                            }
                        });
                    } else {
                        startActivity(new Intent(Onboarding.this, LoginActivity.class));
                        finish();
                    }
        }
        @Override
        protected void onStart() {
            super.onStart();

        }
    }

