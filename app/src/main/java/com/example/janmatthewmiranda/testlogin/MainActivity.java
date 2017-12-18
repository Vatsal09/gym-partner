package com.example.janmatthewmiranda.testlogin;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, HomeFragment.OnFragmentInteractionListener, EditProfileFragment.OnFragmentInteractionListener, MatchesFragment.OnFragmentInteractionListener {

//    private TextView emailText;
    private Button profileBtn;
    private Button homeBtn;
    private Button matchesBtn;
    private List<CreateProfileActivity.User> matchesList;
    private int counter;
    private double userExperienceAvg;
    private String userID;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String userGymCoordinates;
    FragmentTransaction fragmentTransaction;

    private FragmentManager fm;
//    HomeFragment fhome = new HomeFragment();
//    EditProfileFragment fprofile = new EditProfileFragment();
//    MatchesFragment fmatches = new MatchesFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

//        databaseReference = FirebaseDatabase.getInstance().getReference();
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseUser = firebaseAuth.getCurrentUser();
//
//        userID = firebaseUser.getUid();
//        counter = 0;
//
//        matchesList = new ArrayList<>();
//
//
//        // Get current user's gym
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference userRef = ref.child("users").child(userID).child("gym_location");
//
//        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                userGymCoordinates = dataSnapshot.getValue(String.class);
//                Log.d("String", userGymCoordinates);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        // Get current user's experience level
//        userRef = ref.child("users").child(userID).child("experience_avg");
//        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                userExperienceAvg = dataSnapshot.getValue(Double.class);
////                String temp = dataSnapshot.getValue(String.class);
////                userExperienceAvg = Double.parseDouble(temp);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
//        // Get all users with same gym as user selected gym
//        DatabaseReference findSameGymUsers = FirebaseDatabase.getInstance().getReference("users");
//        findSameGymUsers.orderByChild("gym_location").equalTo(userGymCoordinates).addChildEventListener(new ChildEventListener() {
//            @Override
//            // Collect users with the same gym and put it into arraylist.
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Map<String, CreateProfileActivity.User> user = (Map<String, CreateProfileActivity.User>) dataSnapshot.getValue();
//                for (Map.Entry<String, CreateProfileActivity.User> entry : user.entrySet())
//                {
////                    Log.d("String",  entry.getKey() + " : " + entry.getValue());
////                    Log.d("String", "" + user.get("name"));
////                    System.out.println(entry.getKey() + "/" + entry.getValue());
//                }
////                Log.d("String", "" + user.get("name"));
//
////                Log.d("String", "" + dataSnapshot.getKey() + " -- Entry value" + dataSnapshot.getValue());
//
//                CreateProfileActivity.User tempUser = new CreateProfileActivity.User(""+ user.get("userID"), ""+ user.get("email"), ""+ user.get("name"), ""+ user.get("age"), ""+ user.get("phoneNumber"),
//                        ""+ user.get("gender"), ""+ user.get("imageLink"),""+ user.get("gymName"), ""+ user.get("gym_location"), Double.parseDouble(""+ user.get("experience_avg")), Double.parseDouble(""+ user.get("experience_flexibility")),
//                        Double.parseDouble(""+ user.get("experience_dynamic_strength")),Double.parseDouble(""+ user.get("experience_static_strength")),Double.parseDouble(""+ user.get("experience_aerobic")),Double.parseDouble(""+ user.get("experience_circuit")),
//                        (List<String>)(List<?>) user.get("schedule_mon"),(List<String>)(List<?>) user.get("schedule_tue"),(List<String>)(List<?>) user.get("schedule_wed"),(List<String>)(List<?>) user.get("schedule_thu"),(List<String>)(List<?>) user.get("schedule_fri"),(List<String>)(List<?>) user.get("schedule_sat"),
//                        (List<String>)(List<?>) user.get("schedule_sun"), (List<String>)(List<?>) user.get("matchList"));
//                matchesList.add(tempUser);
//                Log.d("arraylist", matchesList.toString());
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
        FirebaseUser user = firebaseAuth.getCurrentUser();

        profileBtn = (Button) findViewById(R.id.profileBtn);
        homeBtn = (Button) findViewById(R.id.homeBtn);
        matchesBtn = (Button) findViewById(R.id.matchesBtn);
//        logoutBtn = (Button) findViewById(R.id.logoutBtn);

        Toast.makeText(getBaseContext(), "Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();

//        logoutBtn.setOnClickListener(this);
        profileBtn.setOnClickListener(this);
        homeBtn.setOnClickListener(this);
        matchesBtn.setOnClickListener(this);


//        fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("Matches List",(Serializable) matchesList);
//        view_fragment.setArguments(bundle);
//        fragmentTransaction.replace(R.id.fragment_container, view_fragment);

//        ft.add(R.id.frameLayout, fhome);
//        ft.commit();

    }

    public class newUser {
        public String name;
        public String experience_avg;
        public String gymName;
        public String phoneNumber;
        public List<HashMap> matchList;

        public  newUser(String name, String experience_avg, String gymName, String phoneNumber) {
            this.name = name;
            this.experience_avg = experience_avg;
            this.gymName = gymName;
            this.phoneNumber = phoneNumber;
           // this.matchList = matchList;
        }
        //newUser(String name, String experience_avg, String gymName, String phoneNumber, List<HashMap> matchList)  {

    }

    newUser user1 = new newUser("test", "31.2", "Sonny Werblin Recreation Center", "8378");
    newUser user2 = new newUser("ashgdfak", "44.4", "Sonny Werblin Recreation Center", "1236985");
    newUser user3 = new newUser("Gao Pan", "97.8", "Sonny Werblin Recreation Center", "6466428972");
    newUser user4 = new newUser("asfafa", "48.4", "Sonny Werblin Recreation Center", "12414121");
    newUser user5 = new newUser("fsdgsag", "39.6", "Sonny Werblin Recreation Center", "123456");


    public void logoutUser() {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onClick(View v) {
        if(v == profileBtn) {
            //set frameLayout accordingly

            EditProfileFragment edit_profile_fragment = new EditProfileFragment();
            fm = getFragmentManager();
            fragmentTransaction = fm.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putSerializable("Match List",(Serializable) matchesList);
            edit_profile_fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.frameLayout, edit_profile_fragment);
            fragmentTransaction.commit();
        }
        if(v == homeBtn) {
            HomeFragment home_fragment = new HomeFragment();
            fm = getFragmentManager();
            fragmentTransaction = fm.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putSerializable("Match List",(Serializable) matchesList);
            home_fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.frameLayout, home_fragment);
            fragmentTransaction.commit();
        }
        if(v == matchesBtn) {
            MatchesFragment matches_fragment = new MatchesFragment();
            fm = getFragmentManager();
            fragmentTransaction = fm.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putSerializable("Match List",(Serializable) matchesList);
            matches_fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.frameLayout, matches_fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("onFragmentInteraction", "Interface initialized");
        logoutUser();

    }

}
