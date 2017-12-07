package com.example.janmatthewmiranda.testlogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private TextView emailText;
    private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        logoutBtn = (Button) findViewById(R.id.logoutBtn);
        emailText = (TextView) findViewById(R.id.emailText);

        emailText.setText("Welcome " + user.getEmail());
        logoutBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == logoutBtn) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
