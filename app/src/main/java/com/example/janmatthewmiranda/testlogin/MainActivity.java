package com.example.janmatthewmiranda.testlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText emailIn;
    private EditText passwordIn;
    private Button loginBtn;
    private Button createBtn;

    private FirebaseAuth mAuth;
    private ProgressDialog pBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        pBar = new ProgressDialog(this);

        emailIn = (EditText) findViewById(R.id.user);
        passwordIn = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.loginBTN);
        createBtn = (Button) findViewById(R.id.createBTN);

        loginBtn.setOnClickListener(this);
        createBtn.setOnClickListener(this);
    }

    private  void registerUser(){
        String email = emailIn.getText().toString().trim();
        String password = passwordIn.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        pBar.setMessage("Registering Please Wait...");
        pBar.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pBar.dismiss();
                        if(task.isSuccessful()) {
                            Log.d("message", "createUserWithEmail:success");
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        } else {
                            Log.w("message", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void userLogin() {
        String email = emailIn.getText().toString().trim();
        String password = passwordIn.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }
        pBar.setMessage("Logging in, Please wait...");
        pBar.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pBar.dismiss();
                        if(task.isSuccessful()) {
                            Log.d("message", "signInWithEmail:success");
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        } else {
                            finish();
                            // If sign in fails, display a message to the user.
                            Log.w("", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v == createBtn) {
            registerUser();
        }

        if(v == loginBtn) {
            userLogin();
        }
    }
}
