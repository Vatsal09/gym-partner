package com.example.janmatthewmiranda.testlogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
//    private TextView emailText;
    private Button logoutBtn;
    private Button profileBtn;
    private Button homeBtn;
    private Button matchesBtn;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        profileBtn = (Button) findViewById(R.id.profileBtn);
        homeBtn = (Button) findViewById(R.id.homeBtn);
        matchesBtn = (Button) findViewById(R.id.matchesBtn);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        logoutBtn = (Button) findViewById(R.id.logoutBtn);

//        emailText = (TextView) findViewById(R.id.emailText);
        Toast.makeText(getBaseContext(), "Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();

//        emailText.setText("Welcome " + user.getEmail());
        logoutBtn.setOnClickListener(this);
        profileBtn.setOnClickListener(this);
        homeBtn.setOnClickListener(this);
        matchesBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v == profileBtn) {
            //set frameLayout accordingly
        }
        if(v == homeBtn) {
            //set frameLayout accordingly
        }
        if(v == matchesBtn) {
            //set frameLayout accordingly
        }
        if(v == logoutBtn) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
      }
    }
}
