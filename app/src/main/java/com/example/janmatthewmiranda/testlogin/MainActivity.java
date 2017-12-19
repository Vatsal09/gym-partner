package com.example.janmatthewmiranda.testlogin;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
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
long startTime = System.currentTimeMillis();
    private Button profileBtn;
    private Button homeBtn;
    private Button matchesBtn;
    private List<newUser> matchesList;
    private int counter;
    private double userExperienceAvg;
    public String userID;
    private String gymName = "";
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String userGymCoordinates;
    FragmentTransaction fragmentTransaction;
    private ProgressDialog mProgressDialog;
    private FragmentManager fm;
    HomeFragment fhome = new HomeFragment();
//    EditProfileFragment fprofile = new EditProfileFragment();
//    MatchesFragment fmatches = new MatchesFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final long startTime = System.currentTimeMillis();
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        matchesList = new ArrayList<>();
        userID = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.orderByKey().equalTo(userID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                long endTime = System.currentTimeMillis();
                Log.d("time",endTime - startTime + "");
                gymName = ((HashMap<String, String>) dataSnapshot.getValue()).get("gymName");

                databaseReference = FirebaseDatabase.getInstance().getReference("users");

                databaseReference.orderByChild("gymName").equalTo(gymName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                            Log.d("child Count", childSnapshot.getChildrenCount() + "");
                            HashMap<String, String> n = (HashMap<String, String>) childSnapshot.getValue();
                            Log.d("data", childSnapshot.getValue().toString());
                            String UID = "";
                            String name = "";
                            String experience_avg = "";
                            String gymName = "";
                            String phoneNumber = "";
                            if (n.get("userID") != null) UID = n.get("userID");
                            if (n.get("name") != null) name = n.get("name");
                            if (n.get("experience_avg") != null) experience_avg = n.get("experience_avg");
                            if (n.get("gymName") != null) gymName = n.get("gymName");
                            if (n.get("phoneNumber") != null) phoneNumber = n.get("phoneNumber");
                            newUser user = new newUser(UID, name, experience_avg, gymName, phoneNumber);
                            if (!UID.equals(userID)) matchesList.add(user);
                        }
                        fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Match List",(Serializable) matchesList);
                        Log.d("print Bundle", bundle.toString());
                        fhome.setArguments(bundle);
                        ft.add(R.id.frameLayout, fhome);
                        ft.commit();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
/*
        newUser user1 = new newUser("20eAVa3KTDT1i9tuEyMSNt719p83","test", 31.2, "Sonny Werblin Recreation Center", "8378");
        newUser user2 = new newUser("DGawXRG28CcdPDD5V5C7tglrd7i2","ashgdfak", 44.4, "Sonny Werblin Recreation Center", "1236985");
        newUser user3 = new newUser("MJ2qgvbXnkaq3nBVcMPFvLbvDMo1","Gao Pan", 97.8, "Sonny Werblin Recreation Center", "6466428972");
        newUser user4 = new newUser("c3HHoIeOsEdu5t5U82weYravIrI2","asfafa", 48.4, "Sonny Werblin Recreation Center", "12414121");
        newUser user5 = new newUser("gYLeTKrun3OBGenNwIXQlFn26NB2","fsdgsag", 39.6, "Sonny Werblin Recreation Center", "123456");
        matchesList.add(user1);
        matchesList.add(user2);
        matchesList.add(user3);
        matchesList.add(user4);
        matchesList.add(user5);*/






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



    }

    public class newUser implements Serializable {
        public String userID;
        public String name;
        public String experience_avg;
        public String gymName;
        public String phoneNumber;

        public newUser() {
            this("", "", "0.0", "", "");
        }
        public  newUser(String userID, String name, String experience_avg, String gymName, String phoneNumber) {
            this.userID = userID;
            this.name = name;
            this.experience_avg = experience_avg;
            this.gymName = gymName;
            this.phoneNumber = phoneNumber;
           // this.matchList = matchList;
        }
        public String getUserID() {
            return userID;
        }
        public String getName() {
            return  name;
        }
        public String getExperience_avg() {
            return experience_avg;
        }
        public String getGymName() {
            return gymName;
        }
        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }
        public void setName(String name) {
            this.name = name;
        }
        public void setExperience_avg(String experience_avg) {
            this.experience_avg = experience_avg;
        }
        public void setGymName(String gymName) {
            this.gymName = gymName;
        }
        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
        //newUser(String name, String experience_avg, String gymName, String phoneNumber, List<HashMap> matchList)  {

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
            Log.d("print Bundle", bundle.toString());
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
