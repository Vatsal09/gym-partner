package com.example.janmatthewmiranda.testlogin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


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

    private TextView matchText, gymText, experienceText;
    private ImageButton matchButton, passButton;
    private ImageView matchImage;

    private DatabaseReference databaseReference;
    private DatabaseReference logReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String userGymCoordinates;
    private ArrayList<CreateProfileActivity.User> matchesArrayList;
    private int counter;
    private double userExperienceAvg;
    private String userID;
    private boolean checkMatch;
    private String state;

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


        // Get current user's gym
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = ref.child("users").child(userID).child("gym");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userGymCoordinates = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Get current user's experience level
        userRef = ref.child("users").child(userID).child("experience_avg");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userExperienceAvg = dataSnapshot.getValue(Double.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // Get all users with same gym as user selected gym
        databaseReference.child("users").orderByChild("gym").equalTo(userGymCoordinates).addChildEventListener(new ChildEventListener() {
            @Override
            // Collect users with the same gym and put it into arraylist.
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CreateProfileActivity.User user = dataSnapshot.getValue(CreateProfileActivity.User.class);
                matchesArrayList.add(user);
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

        matchText = (TextView) view.findViewById(R.id.home_match_name);
        gymText = (TextView) view.findViewById(R.id.home_gym_name);
        experienceText = (TextView) view.findViewById(R.id.home_experience_match);

        matchButton = (ImageButton) view.findViewById(R.id.match_button);
        passButton = (ImageButton) view.findViewById(R.id.pass_button);

        matchImage = (ImageView) view.findViewById(R.id.match_picture);

        // Initialize first matches view
        matchText.setText(matchesArrayList.get(counter).name);
        gymText.setText(matchesArrayList.get(counter).gymName);

        Double experienceDifference = findExperienceDiff(userExperienceAvg, matchesArrayList.get(counter).experience_avg);
        experienceText.setText(experienceDifference + "% Experience Match");

        matchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set values of card to the next potential partner
                counter++;
                matchText.setText(matchesArrayList.get(counter).name);
                gymText.setText(matchesArrayList.get(counter).gymName);

                Double experienceDifference = findExperienceDiff(userExperienceAvg, matchesArrayList.get(counter).experience_avg);
                experienceText.setText(experienceDifference + "% Experience Match");

                // Add match to current users matchlist into the database
                databaseReference.child("users").child(userID).child("matchList").child(matchesArrayList.get(counter).userID).child("state").setValue("Pending");


                // Now check if the other person has matched with you or not

                if(checkIfMatched((matchesArrayList.get(counter).userID))) {


                }


                databaseReference.child("users").child(matchesArrayList.get(counter).userID).child("matchList").child(userID).child("isAccepted").setValue(true);

                // Now check if the other person has matched with you or not

                if(checkIfMatched((matchesArrayList.get(counter).userID))) {
                    databaseReference.child("users").child(matchesArrayList.get(counter).userID).child("matchList").child(userID).child("isAccepted").setValue(true);
                }

                databaseReference.child("users").child(userID).child("matchList").child("userID").child("isAccepted").setValue(true);



                databaseReference.child("users").child(matchesArrayList.get(counter).userID).child("matchList").child(userID).child("check").setValue(true);

//                        child("check").setValue(true);
//                databaseReference.child("users").child(userID).child("matchList").child("hasReceived").setValue(true);
            }
        });

        passButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set values of card to the next potential partner
                counter++;
                matchText.setText(matchesArrayList.get(counter).name);
                gymText.setText(matchesArrayList.get(counter).gymName);

                Double experienceDifference = findExperienceDiff(userExperienceAvg, matchesArrayList.get(counter).experience_avg);
                experienceText.setText(experienceDifference + "% Experience Match");
            }
        });

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
                Double checkID = dataSnapshot.getValue(Double.class);
                if (checkID.equals(userID)) {
                    checkMatch = true;
//                    databaseReference.child("users").child(userID).child("matchList").child("userID").child("isAccepted").setValue(true);
//                    databaseReference.child("users").child(matchesArrayList.get(counter).userID).child("matchList").child(userID).child("isAccepted").setValue(true);
                    // TODO: ADD NOTIFICATION FUNCTIONALLITY



                }
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
                if (checkID.equals(userID)) {
                    checkMatch = true;
//                    databaseReference.child("users").child(userID).child("matchList").child("userID").child("isAccepted").setValue(true);
//                    databaseReference.child("users").child(matchesArrayList.get(counter).userID).child("matchList").child(userID).child("isAccepted").setValue(true);
                    // TODO: ADD NOTIFICATION FUNCTIONALLITY



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
