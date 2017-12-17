package com.example.janmatthewmiranda.testlogin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;

import io.apptik.widget.multiselectspinner.MultiSelectSpinner;

import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CreateProfileActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int GALLERY_REQUEST = 95;
    private Button saveBtn;
    private EditText name;
    private EditText age;
    private EditText phoneNumber;
    private Spinner spinner1;
    private Button uploadBtn;
    private ImageView imageDisplay;
    private String genderSelected;
    private Double [] LatLang;
    private Double progress1 = 0.0;
    private Double progress2 = 0.0;
    private Double progress3 = 0.0;
    private Double  progress4 = 0.0;
    private Double progress5 = 0.0;

    private SeekBar seekBar1;
    private SeekBar seekBar2;
    private SeekBar seekBar3;
    private SeekBar seekBar4;
    private SeekBar seekBar5;

    private MultiSelectSpinner monTimes;
    private MultiSelectSpinner tueTimes;
    private MultiSelectSpinner wedTimes;
    private MultiSelectSpinner thuTimes;
    private MultiSelectSpinner friTimes;
    private MultiSelectSpinner satTimes;
    private MultiSelectSpinner sunTimes;

    private String [] workout_sch_mon;
    private String [] workout_sch_tue;
    private String [] workout_sch_wed;
    private String [] workout_sch_thu;
    private String [] workout_sch_fri;
    private String [] workout_sch_sat;
    private String [] workout_sch_sun;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        spinner1 = (Spinner) findViewById(R.id.genderSpinner);
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
        uploadBtn = (Button) findViewById(R.id.uploadBtn);
        imageDisplay = (ImageView) findViewById(R.id.seeImage);

        seekBar1 = (SeekBar) findViewById(R.id.seekBar_1);
        seekBar2 = (SeekBar) findViewById(R.id.seekBar_2);
        seekBar3 = (SeekBar) findViewById(R.id.seekBar_3);
        seekBar4 = (SeekBar) findViewById(R.id.seekBar_4);
        seekBar5 = (SeekBar) findViewById(R.id.seekBar_5);
        setSeekBars();
        monTimes = (MultiSelectSpinner) findViewById(R.id.multiselectSpinnerMon);
        tueTimes = (MultiSelectSpinner) findViewById(R.id.multiselectSpinnerTue);
        wedTimes = (MultiSelectSpinner) findViewById(R.id.multiselectSpinnerWed);
        thuTimes = (MultiSelectSpinner) findViewById(R.id.multiselectSpinnerThu);
        friTimes = (MultiSelectSpinner) findViewById(R.id.multiselectSpinnerFri);
        satTimes = (MultiSelectSpinner) findViewById(R.id.multiselectSpinnerSat);
        sunTimes = (MultiSelectSpinner) findViewById(R.id.multiselectSpinnerSun);
        setMultiSelect();


        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                String destination = place.getName().toString();
                System.out.println(destination);
                LatLng destinationLatLng = place.getLatLng();
                LatLang = new Double[2];
                LatLang[0] = destinationLatLng.latitude;
                LatLang[1] = destinationLatLng.longitude;
            }
            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
            }
        });
        saveBtn = (Button) findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(this);
    }

    private void saveUser() {
        /**
           Implement database save stuff here
         **/
        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);


        user = firebaseAuth.getCurrentUser();

        String nName = name.getText().toString();
        String aAge = age.getText().toString();
        String pphoneNumber = phoneNumber.getText().toString();
        String email = user.getEmail().toString();
        String userID = user.getUid();
        Double eExperience_avg = (progress1 + progress2 + progress3 + progress4 + progress5)/5.0;
        List mon = new ArrayList<String>(Arrays.asList(workout_sch_mon));
        List tue = new ArrayList<String>(Arrays.asList(workout_sch_tue));
        List wed = new ArrayList<String>(Arrays.asList(workout_sch_wed));
        List thu = new ArrayList<String>(Arrays.asList(workout_sch_thu));
        List fri = new ArrayList<String>(Arrays.asList(workout_sch_fri));
        List sat = new ArrayList<String>(Arrays.asList(workout_sch_sat));
        List sun = new ArrayList<String>(Arrays.asList(workout_sch_sun));
        List coordinates = new ArrayList<Double>(Arrays.asList(LatLang));

        mDatabase = database.getReference();

        User user = new User(email, nName, aAge, pphoneNumber, genderSelected, coordinates, eExperience_avg, progress1, progress2, progress3, progress4, progress5, mon, tue, wed, thu, fri, sat, sun);
        mDatabase.child("users").child(userID).setValue(user);

        //Moves to Homepage
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @IgnoreExtraProperties
    public static class User {

        public String email;
        public String name;
        public String age;
        public String phoneNumber;
        public String gender;
        public List gym_location;
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



        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String email, String name, String age, String phoneNumber, String gender, List gym_location, Double experience_avg, Double experience_flexibility, Double experience_dynamic_strength, Double experience_static_strength,
                    Double experience_aerobic, Double experience_circuit, List schedule_mon, List schedule_tue, List schedule_wed, List schedule_thu, List schedule_fri, List schedule_sat, List schedule_sun) {
            this.email = email;
            this.name = name;
            this.age = age;
            this.phoneNumber = phoneNumber;
            this.gender = gender;
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
        }

    }

    @Override
    public void onClick(View v) {
        if (v == saveBtn) {
            saveUser();
        }
    }

    // Opens the gallery using implicit intent
    public void openGallery(View v){
        Intent getImagefromGallery = new Intent(Intent.ACTION_PICK);
        File galleryDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String path = galleryDir.getPath();
        Uri url = Uri.parse(path);
        getImagefromGallery.setDataAndType(url, "image/*");
        startActivityForResult(getImagefromGallery, GALLERY_REQUEST);


    }

    public void setSeekBars(){
        seekBar1.setMax(100);
        seekBar2.setMax(100);
        seekBar3.setMax(100);
        seekBar4.setMax(100);
        seekBar5.setMax(100);

        seekBar1.setProgress(0);
        seekBar2.setProgress(0);
        seekBar3.setProgress(0);
        seekBar4.setProgress(0);
        seekBar5.setProgress(0);


        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress1 = (double) i;
                System.out.println(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress2 = (double) i;
                System.out.println(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress3 = (double) i;
                System.out.println(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress4 = (double) i;
                System.out.println(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress5 = (double) i;
                System.out.println(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == GALLERY_REQUEST){
                Uri uriOfImage = data.getData();
                InputStream inputStream;
                try {
                    inputStream=getContentResolver().openInputStream(uriOfImage);
                    Bitmap imageToUpload = BitmapFactory.decodeStream(inputStream);
                    imageDisplay.setImageBitmap(imageToUpload);
                }catch (FileNotFoundException exc){
                    exc.printStackTrace();
                    Toast.makeText(this, "Could not open image. Please choose another image",Toast.LENGTH_LONG).show();
                }
            }
        }

    }
    public void setMultiSelect(){
        ArrayList<String> times = new ArrayList<>();
        for(int i=1; i<13;i++){
           times.add(Integer.toString(i) + " AM");
        }
        for(int i=1; i<13;i++){
            times.add(Integer.toString(i) + " PM");
        }

        ArrayAdapter<String> adapter1 = new ArrayAdapter <String>(this, android.R.layout.simple_list_item_activated_1, times);
        ArrayAdapter<String> adapter2 = new ArrayAdapter <String>(this, android.R.layout.simple_list_item_activated_1, times);
        ArrayAdapter<String> adapter3 = new ArrayAdapter <String>(this, android.R.layout.simple_list_item_activated_1, times);
        ArrayAdapter<String> adapter4 = new ArrayAdapter <String>(this, android.R.layout.simple_list_item_activated_1, times);
        ArrayAdapter<String> adapter5 = new ArrayAdapter <String>(this, android.R.layout.simple_list_item_activated_1, times);
        ArrayAdapter<String> adapter6 = new ArrayAdapter <String>(this, android.R.layout.simple_list_item_activated_1, times);
        ArrayAdapter<String> adapter7 = new ArrayAdapter <String>(this, android.R.layout.simple_list_item_activated_1, times);

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
                for(int i=1; i<25; i++){
                    if(selected[i-1] == true){
                        String tmp = "";
                        if(i < 12){
                            tmp = Integer.toString(i) +" AM";
                        }
                        else if(i == 12){
                            tmp = Integer.toString(i-1) +" AM";
                        }
                        else if(i>12){
                            tmp = Integer.toString(i-12) +" PM";
                        }
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
                for(int i=1; i<25; i++){
                    if(selected[i-1] == true){
                        String tmp = "";
                        if(i < 12){
                            tmp = Integer.toString(i) +" AM";
                        }
                        else if(i == 12){
                            tmp = Integer.toString(i-1) +" AM";
                        }
                        else if(i>12){
                            tmp = Integer.toString(i-12) +" PM";
                        }
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
                for(int i=1; i<25; i++){
                    if(selected[i-1] == true){
                        String tmp = "";
                        if(i < 12){
                            tmp = Integer.toString(i) +" AM";
                        }
                        else if(i == 12){
                            tmp = Integer.toString(i-1) +" AM";
                        }
                        else if(i>12){
                            tmp = Integer.toString(i-12) +" PM";
                        }
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
                for(int i=1; i<25; i++){
                    if(selected[i-1] == true){
                        String tmp = "";
                        if(i < 12){
                            tmp = Integer.toString(i) +" AM";
                        }
                        else if(i == 12){
                            tmp = Integer.toString(i-1) +" AM";
                        }
                        else if(i>12){
                            tmp = Integer.toString(i-12) +" PM";
                        }
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
                for(int i=1; i<25; i++){
                    if(selected[i-1] == true){
                        String tmp = "";
                        if(i < 12){
                            tmp = Integer.toString(i) +" AM";
                        }
                        else if(i == 12){
                            tmp = Integer.toString(i-1) +" AM";
                        }
                        else if(i>12){
                            tmp = Integer.toString(i-12) +" PM";
                        }
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
                for(int i=1; i<25; i++){
                    if(selected[i-1] == true){
                        String tmp = "";
                        if(i < 12){
                            tmp = Integer.toString(i) +" AM";
                        }
                        else if(i == 12){
                            tmp = Integer.toString(i-1) +" AM";
                        }
                        else if(i>12){
                            tmp = Integer.toString(i-12) +" PM";
                        }
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
                for(int i=1; i<25; i++){
                    if(selected[i-1] == true){
                        String tmp = "";
                        if(i < 12){
                            tmp = Integer.toString(i) +" AM";
                        }
                        else if(i == 12){
                            tmp = Integer.toString(i-1) +" AM";
                        }
                        else if(i>12){
                            tmp = Integer.toString(i-12) +" PM";
                        }
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
}
