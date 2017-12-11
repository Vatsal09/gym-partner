package com.example.janmatthewmiranda.testlogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

public class CreateProfile extends AppCompatActivity implements View.OnClickListener{

    private Button saveBtn;
    private EditText name;
    private EditText age;
    private EditText phoneNumber;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        saveBtn = (Button) findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(this);
    }

    private void saveUser() {
        /**
           Implement database save stuff here
         **/
        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        user = firebaseAuth.getCurrentUser();

        String nName = name.getText().toString();
        String aAge = age.getText().toString();
        String pphoneNumber = phoneNumber.getText().toString();
        String email = user.getEmail().toString();

        mDatabase = database.getReference();

        User user = new User(email, nName, aAge, pphoneNumber);
        mDatabase.child("users").child(nName).setValue(user);

        //Moves to Homepage
        finish();
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
    }

    @IgnoreExtraProperties
    public static class User {

        public String email;
        public String name;
        public String age;
        public String phoneNumber;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String email, String name, String age, String phoneNumber) {
            this.email = email;
            this.name = name;
            this.age = age;
            this.phoneNumber = phoneNumber;
        }

    }

    @Override
    public void onClick(View v) {
        if (v == saveBtn) {
            saveUser();
        }
    }
}
