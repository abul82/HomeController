package com.demo.homecontroller.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.homecontroller.R;
import com.demo.homecontroller.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class GatherDetails extends AppCompatActivity {

    private String TAG = GatherDetails.class.getSimpleName();
    private String fName, lName, contact,userId;
    private FirebaseAuth firebaseAuth;
    SharedPreferences pref;
    private User user;
    private FirebaseDatabase database;
    private DatabaseReference usersDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gather_details);
        firebaseAuth = FirebaseAuth.getInstance();
        final EditText firstname = findViewById(R.id.fname);
        final EditText lastname = findViewById(R.id.lname);
        final EditText phn_num = findViewById(R.id.contactadmin);
        Button register = findViewById(R.id.registerButton);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        userId = firebaseAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        usersDB = database.getReference().child("Users");



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fName = firstname.getText().toString().trim();
                lName = lastname.getText().toString().trim();
                contact = phn_num.getText().toString().trim();
//                Toast.makeText(getApplicationContext(), "Button CLicked", Toast.LENGTH_LONG).show();
                if (!TextUtils.isEmpty(fName) && !TextUtils.isEmpty(lName) && !TextUtils.isEmpty(contact)) {
                    addUserToCollection();

//                    Toast.makeText(getApplicationContext(), "Non empty string", Toast.LENGTH_LONG).show();
//
                }
            }
        });
    }

    private void addUserToCollection() {
        user= new User(fName,lName,contact,userId);
        usersDB.child(userId)
                .setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

        Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_LONG).show();
                    }
                });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        firebaseAuth.signOut();
    }
}
