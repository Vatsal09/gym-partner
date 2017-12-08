package com.example.janmatthewmiranda.testlogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateProfile extends AppCompatActivity implements View.OnClickListener{

    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        saveBtn = (Button) findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(this);
    }

    private void saveUser() {
        /**
           Implement database save stuff here
         **/

        //Moves to Homepage
        finish();
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
    }

    @Override
    public void onClick(View v) {
        if (v == saveBtn) {
            saveUser();
        }
    }
}
