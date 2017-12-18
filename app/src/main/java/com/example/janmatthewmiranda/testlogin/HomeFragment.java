package com.example.janmatthewmiranda.testlogin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.Serializable;


import static java.lang.Thread.sleep;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView matchText, gymText, experienceText, sorryText;
    private ImageButton matchButton, passButton;
    private ImageView matchImage;

    private DatabaseReference databaseReference;
    private DatabaseReference logReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String userGymCoordinates;
    private List<CreateProfileActivity.User> matchesList;
    private int counter;
    private double userExperienceAvg;
    private String userID;
    private boolean checkMatch;
    private String state;
    private StorageReference mStorage;
    private Uri uriOfImage;



    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        userID = firebaseUser.getUid();
        counter = 0;

        matchesList = (ArrayList<CreateProfileActivity.User>) getArguments().getSerializable("Matches List");
        if(matchesList == null) {
            Log.d("bundle has failed", matchesList.toString());
        }



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


//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        Query query = reference.child("users").orderByChild("gym_location").equalTo(userGymCoordinates);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot data : dataSnapshot.getChildren()) {
//                        matchesList.add( (CreateProfileActivity.User) data.getValue());
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        Log.d("arraylist", matchesList.toString());

        matchText = (TextView) view.findViewById(R.id.home_match_name);
        gymText = (TextView) view.findViewById(R.id.home_gym_name);
        experienceText = (TextView) view.findViewById(R.id.home_experience_match);
        sorryText = (TextView) view.findViewById(R.id.sorry_message);

        matchButton = (ImageButton) view.findViewById(R.id.match_button);
        passButton = (ImageButton) view.findViewById(R.id.pass_button);

        matchImage = (ImageView) view.findViewById(R.id.match_picture);



        if (matchesList.size() == 0) {
            matchText.setVisibility(View.INVISIBLE);
            gymText.setVisibility(View.INVISIBLE);
            experienceText.setVisibility(View.INVISIBLE);
            sorryText.setVisibility(View.VISIBLE);

            matchButton.setVisibility(View.INVISIBLE);
            passButton.setVisibility(View.INVISIBLE);
            Log.d("String", "You reach inside the if statement");

        }

        else {
            // Initialize first matches view
            Log.d("String", "You reach after else statement");

            matchText.setVisibility(View.VISIBLE);
            gymText.setVisibility(View.VISIBLE);
            experienceText.setVisibility(View.VISIBLE);
            sorryText.setVisibility(View.INVISIBLE);

            matchButton.setVisibility(View.VISIBLE);
            passButton.setVisibility(View.VISIBLE);

            matchText.setText(matchesList.get(counter).name);
            Log.d("name", matchesList.get(counter).name);
            gymText.setText(matchesList.get(counter).gymName);
            Log.d("gymName", matchesList.get(counter).gymName);

            Double experienceDifference = findExperienceDiff(userExperienceAvg, matchesList.get(counter).experience_avg);
            experienceText.setText(experienceDifference + "% Experience Match");

//            StorageReference profileImageReference = mStorage.child("users/" + matchesList.get(counter).userID + "/" + uriOfImage.getLastPathSegment());
//
//            final long ONE_MEGABYTE = 1024 * 1024;
//
//            //
//            profileImageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                @Override
//                public void onSuccess(byte[] bytes) {
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                    matchImage.setImageBitmap(bitmap);
//                }
//            });

            matchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Set values of card to the next potential partner
                    counter++;
                    matchText.setText(matchesList.get(counter).name);
                    gymText.setText(matchesList.get(counter).gymName);

                    Double experienceDifference = findExperienceDiff(userExperienceAvg, matchesList.get(counter).experience_avg);
                    experienceText.setText(experienceDifference + "% Experience Match");
                    // Add match to current users matchlist into the database
                    databaseReference.child("users").child(userID).child("matchList").child(matchesList.get(counter).userID).child("state").setValue("Pending");


                    // Now check if the other person has matched with you or not

                    if (checkIfMatched(matchesList.get(counter).userID)) {

                        if (checkState(matchesList.get(counter).userID).equals("Passed")) {
                            databaseReference.child("users").child(userID).child("matchList").child(matchesList.get(counter).userID).child("state").setValue("Failed");
                        }
                        else if (checkState(matchesList.get(counter).userID).equals("Pending")) {
                            databaseReference.child("users").child(userID).child("matchList").child(matchesList.get(counter).userID).child("state").setValue("Accepted");
                            databaseReference.child("users").child(matchesList.get(counter).userID).child("matchList").child(userID).child("state").setValue("Accepted");

                            // TODO: Add Toast Notifications that you have found a match
                        }
                        else if (checkState(matchesList.get(counter).userID).equals("Accepted")) {
                            databaseReference.child("users").child(userID).child("matchList").child(matchesList.get(counter).userID).child("state").setValue("Accepted");
                        }
                    }
                }
            });

            passButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Set values of card to the next potential partner
                    counter++;
                    matchText.setText(matchesList.get(counter).name);
                    gymText.setText(matchesList.get(counter).gymName);

                    Double experienceDifference = findExperienceDiff(userExperienceAvg, matchesList.get(counter).experience_avg);
                    experienceText.setText(experienceDifference + "% Experience Match");
                }
            });
        }
        // Inflate the layout for this fragment
        return view;
    }

    public String checkState(String matchID) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = ref.child("users").child(matchID).child("matchList").child(userID);
        state = "";
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String stateCheck = dataSnapshot.getValue(String.class);
                state = stateCheck;
                Log.d("InCheckState", "State = " + state);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return state;
    }

    public boolean checkIfMatched(String matchID) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = ref.child("users").child(matchID).child("matchList");
        checkMatch = false;
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double checkID = dataSnapshot.getValue(Double.class);
                Log.d("InCheckState", "State = " + state);
                if (checkID.equals(userID)) {
                    checkMatch = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return checkMatch;
    }


    Double findExperienceDiff(double A, double B) {
        double diff = Math.abs(A - B);
        return 100 - (diff * 2);
    }

    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked
        switch (v.getId()) {
//            case R.id.textView_help:
//                switchFragment(HelpFragment.TAG);
//                break;
//            case R.id.textView_settings:
//                switchFragment(SettingsFragment.TAG);
//                break;
        }
    }



        // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
