package com.example.janmatthewmiranda.testlogin;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, homeFragment.OnFragmentInteractionListener, editProfileFragment.OnFragmentInteractionListener, matchesFragment.OnFragmentInteractionListener {

    private FirebaseAuth firebaseAuth;
//    private TextView emailText;
    private Button logoutBtn;
    private Button profileBtn;
    private Button homeBtn;
    private Button matchesBtn;

    private FragmentManager fm;
    homeFragment fhome = new homeFragment();
    editProfileFragment fprofile = new editProfileFragment();
    matchesFragment fmatches = new matchesFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        profileBtn = (Button) findViewById(R.id.profileBtn);
        homeBtn = (Button) findViewById(R.id.homeBtn);
        matchesBtn = (Button) findViewById(R.id.matchesBtn);
        logoutBtn = (Button) findViewById(R.id.logoutBtn);

        Toast.makeText(getBaseContext(), "Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();

        logoutBtn.setOnClickListener(this);
        profileBtn.setOnClickListener(this);
        homeBtn.setOnClickListener(this);
        matchesBtn.setOnClickListener(this);

        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.frameLayout, fhome);
        ft.commit();

    }

    public void logoutUser() {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onClick(View v) {
        if(v == profileBtn) {
            //set frameLayout accordingly
            fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frameLayout, fprofile);
            ft.commit();
        }
        if(v == homeBtn) {
            //set frameLayout accordingly
            fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frameLayout, fhome);
            ft.commit();
        }
        if(v == matchesBtn) {
            //set frameLayout accordingly
            fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frameLayout, fmatches);
            ft.commit();
        }
        if(v == logoutBtn) {
            logoutUser();
      }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
