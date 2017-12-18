package com.example.janmatthewmiranda.testlogin;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import io.apptik.widget.multiselectspinner.MultiSelectSpinner;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

    public static final int GALLERY_REQUEST = 94;
    long startTime = System.currentTimeMillis();

    FirebaseUser currentUser;
    private Button logoutBtnF;
    private Button saveBtn;
    private Button uploadBtn;

    public EditText nameEditText;
    public EditText numberEditText;
    public EditText ageEditText;

    private SeekBar flexibilityBar;
    private SeekBar dynamicStrengthBar;
    private SeekBar staticStrengthBar;
    private SeekBar aerobicBar;
    private SeekBar circuitBar;

    public ImageView imageDisplay;
    private FirebaseStorage storage;
    private Uri uriOfImage;
    private String imageLink;
    private StorageReference mStorage;

    private double progress1,
            progress2,
            progress3,
            progress4,
            progress5;

    List mon, tue, wed, thu, fri, sat, sun;

    private Spinner spinner1;

    private MultiSelectSpinner monTimes;
    private MultiSelectSpinner tueTimes;
    private MultiSelectSpinner wedTimes;
    private MultiSelectSpinner thuTimes;
    private MultiSelectSpinner friTimes;
    private MultiSelectSpinner satTimes;
    private MultiSelectSpinner sunTimes;

    private String [] workout_sch_mon = new String[0];
    private String [] workout_sch_tue = new String[0];
    private String [] workout_sch_wed = new String[0];
    private String [] workout_sch_thu = new String[0];
    private String [] workout_sch_fri = new String[0];
    private String [] workout_sch_sat = new String[0];
    private String [] workout_sch_sun = new String[0];

    private OnFragmentInteractionListener mListener;

    private String genderSelected;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        startTime = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        nameEditText = view.findViewById(R.id.name);
        numberEditText = view.findViewById(R.id.phoneNumber);
        ageEditText = view.findViewById(R.id.age);

        flexibilityBar = view.findViewById(R.id.seekBar_1);
        dynamicStrengthBar = view.findViewById(R.id.seekBar_2);
        staticStrengthBar = view.findViewById(R.id.seekBar_3);
        aerobicBar = view.findViewById(R.id.seekBar_4);
        circuitBar = view.findViewById(R.id.seekBar_5);

        setSeekBars();

        imageDisplay = view.findViewById(R.id.seeImage);
        storage = FirebaseStorage.getInstance();

        spinner1 = (Spinner) view.findViewById(R.id.genderSpinner);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                genderSelected = adapterView.getItemAtPosition(i).toString();
                System.out.println(genderSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        monTimes = (MultiSelectSpinner) view.findViewById(R.id.multiselectSpinnerMon);
        tueTimes = (MultiSelectSpinner) view.findViewById(R.id.multiselectSpinnerTue);
        wedTimes = (MultiSelectSpinner) view.findViewById(R.id.multiselectSpinnerWed);
        thuTimes = (MultiSelectSpinner) view.findViewById(R.id.multiselectSpinnerThu);
        friTimes = (MultiSelectSpinner) view.findViewById(R.id.multiselectSpinnerFri);
        satTimes = (MultiSelectSpinner) view.findViewById(R.id.multiselectSpinnerSat);
        sunTimes = (MultiSelectSpinner) view.findViewById(R.id.multiselectSpinnerSun);
        mon = new ArrayList<String>();
        tue = new ArrayList<String>();
        wed = new ArrayList<String>();
        thu = new ArrayList<String>();
        fri = new ArrayList<String>();
        sat = new ArrayList<String>();
        sun = new ArrayList<String>();
        setMultiSelect();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String userID = "";
        if (currentUser != null){
            userID = currentUser.getUid();
        }
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference usersRef = database.getReference("users");
        usersRef.orderByKey().equalTo(userID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Iterable<DataSnapshot> s = dataSnapshot.getChildren();
                Iterator<DataSnapshot> i = s.iterator();
                while (i.hasNext()) {
                    DataSnapshot child = i.next();
                    String value = child.getValue().toString();
                    if (child.getKey().contains("schedule")) {
                        Iterable<DataSnapshot> day = child.getChildren();
                        Iterator<DataSnapshot> iterator = day.iterator();
                        while (iterator.hasNext()) {
                            DataSnapshot times = iterator.next();
                            int item;
                            switch (times.getValue().toString()) {
                                case "12 AM" :
                                    item = 0;
                                    break;
                                case "1 AM" :
                                    item = 1;
                                    break;
                                case "2 AM" :
                                    item = 2;
                                    break;
                                case "3 AM" :
                                    item = 3;
                                    break;
                                case "4 AM" :
                                    item = 4;
                                    break;
                                case "5 AM" :
                                    item = 5;
                                    break;
                                case "6 AM" :
                                    item = 6;
                                    break;
                                case "7 AM" :
                                    item = 7;
                                    break;
                                case "8 AM" :
                                    item = 8;
                                    break;
                                case "9 AM" :
                                    item = 9;
                                    break;
                                case "10 AM" :
                                    item = 10;
                                    break;
                                case "11 AM" :
                                    item = 11;
                                    break;
                                case "12 PM" :
                                    item = 12;
                                    break;
                                case "1 PM" :
                                    item = 13;
                                    break;
                                case "2 PM" :
                                    item = 14;
                                    break;
                                case "3 PM" :
                                    item = 15;
                                    break;
                                case "4 PM" :
                                    item = 16;
                                    break;
                                case "5 PM" :
                                    item = 17;
                                    break;
                                case "6 PM" :
                                    item = 18;
                                    break;
                                case "7 PM" :
                                    item = 19;
                                    break;
                                case "8 PM" :
                                    item = 20;
                                    break;
                                case "9 PM" :
                                    item = 21;
                                    break;
                                case "10 PM" :
                                    item = 22;
                                    break;
                                case "11 PM" :
                                    item = 23;
                                    break;
                                default :
                                    item = -1;
                                    break;
                            }

                            switch (child.getKey()) {
                                case "schedule_mon":
                                    mon.add(times.getValue().toString());
                                    monTimes.selectItem(item, true);
                                    break;
                                case "schedule_tue" :
                                    tue.add(times.getValue().toString());
                                    tueTimes.selectItem(item, true);
                                    break;
                                case "schedule_wed" :
                                    wed.add(times.getValue().toString());
                                    wedTimes.selectItem(item, true);
                                    break;
                                case "schedule_thu" :
                                    thu.add(times.getValue().toString());
                                    thuTimes.selectItem(item, true);
                                    break;
                                case "schedule_fri" :
                                    fri.add(times.getValue().toString());
                                    friTimes.selectItem(item, true);
                                    break;
                                case "schedule_sat" :
                                    sat.add(times.getValue().toString());
                                    satTimes.selectItem(item, true);
                                    break;
                                case "schedule_sun" :
                                    sun.add(times.getValue().toString());
                                    sunTimes.selectItem(item, true);
                                    break;
                                default:
                                    break;
                            }
                        }
                    } else {
                        switch (child.getKey()) {
                            case "name":
                                nameEditText.setText(value);
                                break;
                            case "age":
                                ageEditText.setText(value);
                                break;
                            case "phoneNumber":
                                numberEditText.setText(value);
                                break;
                            case "experience_aerobic":
                                aerobicBar.setProgress(Integer.parseInt(value));
                                break;
                            case "experience_circuit":
                                circuitBar.setProgress(Integer.parseInt(value));
                                break;
                            case "experience_dynamic_strength":
                                dynamicStrengthBar.setProgress(Integer.parseInt(value));
                                break;
                            case "experience_flexibility":
                                flexibilityBar.setProgress(Integer.parseInt(value));
                                break;
                            case "experience_static_strength":
                                staticStrengthBar.setProgress(Integer.parseInt(value));
                                break;
                            case "imageLink" :
                                
                            default:
                                break;
                        }
                    }
                }
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

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        Log.d("time", totalTime + "");
        logoutBtnF = (Button) view.findViewById(R.id.logoutBTNF);
        logoutBtnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("logout", "Logout successful");
                //mListener.onLogoutSelected();
                mListener.onFragmentInteraction(null);
            }
        });
        Log.d("button view", logoutBtnF.toString());
        saveBtn = (Button) view.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUser();
            }
        });
        uploadBtn = (Button) view.findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            Log.d("this runs","right here");
            mListener = (OnFragmentInteractionListener) context;
            Log.d("context", mListener.toString());
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener");
        }
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
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
        void onFragmentInteraction(Uri uri);
    }
    public void setMultiSelect(){
        ArrayList<String> times = new ArrayList<>();
        times.add("12 AM");
        for(int i=1; i<12;i++){
            times.add(Integer.toString(i) + " AM");
        }
        times.add("12 PM");
        for(int i=1; i<12;i++){
            times.add(Integer.toString(i) + " PM");
        }

        ArrayAdapter<String> adapter1 = new ArrayAdapter <String>(getActivity(), android.R.layout.simple_list_item_activated_1, times);
        ArrayAdapter<String> adapter2 = new ArrayAdapter <String>(getActivity(), android.R.layout.simple_list_item_activated_1, times);
        ArrayAdapter<String> adapter3 = new ArrayAdapter <String>(getActivity(), android.R.layout.simple_list_item_activated_1, times);
        ArrayAdapter<String> adapter4 = new ArrayAdapter <String>(getActivity(), android.R.layout.simple_list_item_activated_1, times);
        ArrayAdapter<String> adapter5 = new ArrayAdapter <String>(getActivity(), android.R.layout.simple_list_item_activated_1, times);
        ArrayAdapter<String> adapter6 = new ArrayAdapter <String>(getActivity(), android.R.layout.simple_list_item_activated_1, times);
        ArrayAdapter<String> adapter7 = new ArrayAdapter <String>(getActivity(), android.R.layout.simple_list_item_activated_1, times);

        monTimes.setListAdapter(adapter1).setListener(new MultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                int count = 0;
                for(int i=1; i<25; i++){
                    if(selected[i-1] == true){
                        count++;
                    }

                    System.out.println(String.valueOf(selected[i-1]));
                }
                workout_sch_mon = new String[count];
                for(int i=0; i<24; i++){
                    if(selected[i] == true){
                        String tmp = "";
                        if (i == 0) tmp = "12 AM";
                        else if (i < 12) tmp = Integer.toString(i) + " AM";

                        else if (i == 12) tmp = "12 PM";
                        else if (i>12) tmp = Integer.toString(i-12) + " PM";
                        else{
                            System.out.println("Error at the Multiselect Outputs to List");
                        }
                        // String.valueOf(selected[i-1]
                        workout_sch_mon[count-1] = tmp;
                        count--;
                    }
                }
            }
        }).setAllCheckedText("Available at All Hours").setAllUncheckedText("Not Free to Workout").setSelectAll(false).setTitle(getResources().getString(R.string.title)).setMinSelectedItems(0);
        tueTimes.setListAdapter(adapter2).setListener(new MultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                int count = 0;
                for(int i=1; i<25; i++){
                    if(selected[i-1] == true){
                        count++;
                    }

                    System.out.println(String.valueOf(selected[i-1]));
                }
                workout_sch_tue = new String[count];
                for(int i=0; i<24; i++){
                    if(selected[i] == true){
                        String tmp = "";
                        if (i == 0) tmp = "12 AM";
                        else if (i < 12) tmp = Integer.toString(i) + " AM";

                        else if (i == 12) tmp = "12 PM";
                        else if (i>12) tmp = Integer.toString(i-12) + " PM";
                        else{
                            System.out.println("Error at the Multiselect Outputs to List");
                        }
                        // String.valueOf(selected[i-1]
                        workout_sch_tue[count-1] = tmp;
                        count--;
                    }
                }
            }
        }).setAllCheckedText("Available at All Hours").setAllUncheckedText("Not Free to Workout").setSelectAll(false).setTitle(getResources().getString(R.string.title)).setMinSelectedItems(0);
        wedTimes.setListAdapter(adapter3).setListener(new MultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                int count = 0;
                for(int i=1; i<25; i++){
                    if(selected[i-1] == true){
                        count++;
                    }

                    System.out.println(String.valueOf(selected[i-1]));
                }
                workout_sch_wed = new String[count];
                for(int i=0; i<24; i++){
                    if(selected[i] == true){
                        String tmp = "";
                        if (i == 0) tmp = "12 AM";
                        else if (i < 12) tmp = Integer.toString(i) + " AM";

                        else if (i == 12) tmp = "12 PM";
                        else if (i>12) tmp = Integer.toString(i-12) + " PM";
                        else{
                            System.out.println("Error at the Multiselect Outputs to List");
                        }
                        // String.valueOf(selected[i-1]
                        workout_sch_wed[count-1] = tmp;
                        count--;
                    }
                }
            }
        }).setAllCheckedText("Available at All Hours").setAllUncheckedText("Not Free to Workout").setSelectAll(false).setTitle(getResources().getString(R.string.title)).setMinSelectedItems(0);
        thuTimes.setListAdapter(adapter4).setListener(new MultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                int count = 0;
                for(int i=1; i<25; i++){
                    if(selected[i-1] == true){
                        count++;
                    }

                    System.out.println(String.valueOf(selected[i-1]));
                }
                workout_sch_thu = new String[count];
                for(int i=0; i<24; i++){
                    if(selected[i] == true){
                        String tmp = "";
                        if (i == 0) tmp = "12 AM";
                        else if (i < 12) tmp = Integer.toString(i) + " AM";

                        else if (i == 12) tmp = "12 PM";
                        else if (i>12) tmp = Integer.toString(i-12) + " PM";
                        else{
                            System.out.println("Error at the Multiselect Outputs to List");
                        }
                        // String.valueOf(selected[i-1]
                        workout_sch_thu[count-1] = tmp;
                        count--;
                    }
                }
            }
        }).setAllCheckedText("Available at All Hours").setAllUncheckedText("Not Free to Workout").setSelectAll(false).setTitle(getResources().getString(R.string.title)).setMinSelectedItems(0);
        friTimes.setListAdapter(adapter5).setListener(new MultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                int count = 0;
                for(int i=1; i<25; i++){
                    if(selected[i-1] == true){
                        count++;
                    }

                    System.out.println(String.valueOf(selected[i-1]));
                }
                workout_sch_fri = new String[count];
                for(int i=0; i<24; i++){
                    if(selected[i] == true){
                        String tmp = "";
                        if (i == 0) tmp = "12 AM";
                        else if (i < 12) tmp = Integer.toString(i) + " AM";

                        else if (i == 12) tmp = "12 PM";
                        else if (i>12) tmp = Integer.toString(i-12) + " PM";
                        else{
                            System.out.println("Error at the Multiselect Outputs to List");
                        }
                        // String.valueOf(selected[i-1]
                        workout_sch_fri[count-1] = tmp;
                        count--;
                    }
                }
            }
        }).setAllCheckedText("Available at All Hours").setAllUncheckedText("Not Free to Workout").setSelectAll(false).setTitle(getResources().getString(R.string.title)).setMinSelectedItems(0);
        satTimes.setListAdapter(adapter6).setListener(new MultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                int count = 0;
                for(int i=1; i<25; i++){
                    if(selected[i-1] == true){
                        count++;
                    }

                    System.out.println(String.valueOf(selected[i-1]));
                }
                workout_sch_sat = new String[count];
                for(int i=0; i<24; i++){
                    if(selected[i] == true){
                        String tmp = "";
                        if (i == 0) tmp = "12 AM";
                        else if (i < 12) tmp = Integer.toString(i) + " AM";

                        else if (i == 12) tmp = "12 PM";
                        else if (i>12) tmp = Integer.toString(i-12) + " PM";
                        else{
                            System.out.println("Error at the Multiselect Outputs to List");
                        }
                        // String.valueOf(selected[i-1]
                        workout_sch_sat[count-1] = tmp;
                        count--;
                    }
                }
            }
        }).setAllCheckedText("Available at All Hours").setAllUncheckedText("Not Free to Workout").setSelectAll(false).setTitle(getResources().getString(R.string.title)).setMinSelectedItems(0);
        sunTimes.setListAdapter(adapter7).setListener(new MultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                int count = 0;
                for(int i=1; i<25; i++){
                    if(selected[i-1] == true){
                        count++;
                    }

                    System.out.println(String.valueOf(selected[i-1]));
                }
                workout_sch_sun = new String[count];
                for(int i=0; i<24; i++){
                    if(selected[i] == true){
                        String tmp = "";
                        if (i == 0) tmp = "12 AM";
                        else if (i < 12) tmp = Integer.toString(i) + " AM";

                        else if (i == 12) tmp = "12 PM";
                        else if (i>12) tmp = Integer.toString(i-12) + " PM";
                        else{
                            System.out.println("Error at the Multiselect Outputs to List");
                        }
                        // String.valueOf(selected[i-1]
                        workout_sch_sun[count-1] = tmp;
                        count--;
                    }
                }
            }
        }).setAllCheckedText("Available at All Hours").setAllUncheckedText("Not Free to Workout").setSelectAll(false).setTitle(getResources().getString(R.string.title)).setMinSelectedItems(0);
    }
    // Opens the gallery using implicit intent
    public void openGallery(){
        Intent getImagefromGallery = new Intent(Intent.ACTION_PICK);
        File galleryDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String path = galleryDir.getPath();
        Uri url = Uri.parse(path);
        getImagefromGallery.setDataAndType(url, "image/*");
        startActivityForResult(getImagefromGallery, GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == GALLERY_REQUEST){
                uriOfImage = data.getData();
                InputStream inputStream;
                try {
                    inputStream = getActivity().getContentResolver().openInputStream(uriOfImage);
                    Bitmap imageToUpload = BitmapFactory.decodeStream(inputStream);
                    imageDisplay.setImageBitmap(imageToUpload);
                    String userID = currentUser.getUid();
                    mStorage = storage.getReference();

                    StorageReference profileImageReference = mStorage.child("users/" + userID + "/" + uriOfImage.getLastPathSegment());
                    UploadTask uploadTask = profileImageReference.putFile(uriOfImage);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            System.out.println("IMAGE URL NOT FOUND");
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            imageLink = downloadUrl.toString();
                        }
                    });
                }catch (FileNotFoundException exc){
                    exc.printStackTrace();
                    Toast.makeText(getActivity(), "Could not open image. Please choose another image",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private void saveUser() {
        /**
         Implement database save stuff here
         **/
        String userID = currentUser.getUid();

        String name = nameEditText.getText().toString();
        String age = ageEditText.getText().toString();
        String number = numberEditText.getText().toString();
        String email = currentUser.getEmail().toString();

        double experience_avg = (progress1 + progress2 + progress3 + progress4 + progress5)/5.0;
        if (workout_sch_mon.length != 0) mon = new ArrayList<String>(Arrays.asList(workout_sch_mon));
        if (workout_sch_tue.length != 0) tue = new ArrayList<String>(Arrays.asList(workout_sch_tue));
        if (workout_sch_wed.length != 0) wed = new ArrayList<String>(Arrays.asList(workout_sch_wed));
        if (workout_sch_thu.length != 0) thu = new ArrayList<String>(Arrays.asList(workout_sch_thu));
        if (workout_sch_fri.length != 0) fri = new ArrayList<String>(Arrays.asList(workout_sch_fri));
        if (workout_sch_sat.length != 0) sat = new ArrayList<String>(Arrays.asList(workout_sch_sat));
        if (workout_sch_sun.length != 0) sun = new ArrayList<String>(Arrays.asList(workout_sch_sun));
        List coordinates = new ArrayList<Double>();
        coordinates.add(0);
        coordinates.add(0);
        String destination = "";
        //List coordinates = new ArrayList<Double>(Arrays.asList(LatLang));
        //String coords = ("lat: " + LatLang[0] + ", long: " + LatLang[1]);
        List match_list = new ArrayList<String>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase = database.getReference();


        EditProfileFragment.User user = new EditProfileFragment.User(userID, email, name, age, number, genderSelected, imageLink, destination, coordinates, experience_avg, progress1, progress2, progress3, progress4, progress5, mon, tue, wed, thu, fri, sat, sun, match_list);
        mDatabase.child("users").child(userID).setValue(user);

        //Moves to Homepage
        FragmentManager fm = getFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        HomeFragment fhome = new HomeFragment();
        ft.replace(R.id.frameLayout, fhome);
        ft.commit();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    @IgnoreExtraProperties
    public static class User {

        public String userID;
        public String email;
        public String name;
        public String age;
        public String phoneNumber;
        public String gender;
        public String imageLink;
        public List gym_location;
        public String gymName;
        public Double experience_avg;
        public Double experience_flexibility;
        public Double experience_dynamic_strength;
        public Double experience_static_strength;
        public Double experience_aerobic;
        public Double experience_circuit;
        public List schedule_mon;
        public List schedule_tue;
        public List schedule_wed;
        public List schedule_thu;
        public List schedule_fri;
        public List schedule_sat;
        public List schedule_sun;
        public List matchList;



        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String userID, String email, String name, String age, String phoneNumber, String gender, String imageLink, String gymName, List gym_location, Double experience_avg, Double experience_flexibility, Double experience_dynamic_strength, Double experience_static_strength,
                    Double experience_aerobic, Double experience_circuit, List schedule_mon, List schedule_tue, List schedule_wed, List schedule_thu, List schedule_fri, List schedule_sat, List schedule_sun, List matchList) {
            this.userID = userID;
            this.email = email;
            this.name = name;
            this.age = age;
            this.phoneNumber = phoneNumber;
            this.gender = gender;
            this.imageLink = imageLink;
            this.gymName = gymName;
            this.gym_location = gym_location;
            this.experience_avg = experience_avg;
            this.experience_flexibility = experience_flexibility;
            this.experience_dynamic_strength = experience_dynamic_strength;
            this.experience_static_strength = experience_static_strength;
            this.experience_aerobic = experience_aerobic;
            this.experience_circuit = experience_circuit;
            this.schedule_mon = schedule_mon;
            this.schedule_tue = schedule_tue;
            this.schedule_wed = schedule_wed;
            this.schedule_thu = schedule_thu;
            this.schedule_fri = schedule_fri;
            this.schedule_sat = schedule_sat;
            this.schedule_sun = schedule_sun;
            this.matchList = matchList;
        }

    }
    public void setSeekBars(){
        flexibilityBar.setMax(100);
        dynamicStrengthBar.setMax(100);
        staticStrengthBar.setMax(100);
        aerobicBar.setMax(100);
        circuitBar.setMax(100);

        flexibilityBar.setProgress(0);
        dynamicStrengthBar.setProgress(0);
        staticStrengthBar.setProgress(0);
        aerobicBar.setProgress(0);
        circuitBar.setProgress(0);


        flexibilityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress1 = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        dynamicStrengthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress2 = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        staticStrengthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress3 = i;;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        aerobicBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress4 = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        circuitBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress5 = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
