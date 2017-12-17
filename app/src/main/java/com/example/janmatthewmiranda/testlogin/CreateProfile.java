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


import io.apptik.widget.multiselectspinner.MultiSelectSpinner;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;


public class CreateProfile extends AppCompatActivity implements View.OnClickListener{

    public static final int GALLERY_REQUEST = 95;
    private Button saveBtn;
    private EditText name;
    private EditText age;
    private EditText phoneNumber;
    private Spinner spinner1;
    private Button uploadBtn;
    private ImageView imageDisplay;
    private String genderSelected;

    private int progress1 = 0;
    private int progress2 = 0;
    private int progress3 = 0;
    private int progress4 = 0;
    private int progress5 = 0;

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

        mDatabase = database.getReference();

        User user = new User(email, nName, aAge, pphoneNumber);
        mDatabase.child("users").child(nName).setValue(user);

        //Moves to Homepage
        finish();
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
    }

    @IgnoreExtraProperties
    public static class User {

        public String email;
        public String name;
        public String age;
        public String phoneNumber;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String email, String name, String age, String phoneNumber) {
            this.email = email;
            this.name = name;
            this.age = age;
            this.phoneNumber = phoneNumber;
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

        seekBar1.setProgress(progress1);
        seekBar2.setProgress(progress2);
        seekBar3.setProgress(progress3);
        seekBar4.setProgress(progress4);
        seekBar5.setProgress(progress5);


        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress1 = i;
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
                progress2 = i;
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
                progress3 = i;
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
                progress4 = i;
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
                progress5 = i;
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
                for(int i=1; i<25; i++){
                    System.out.println(String.valueOf(selected[i-1]));
                }
            }
        }).setAllCheckedText("Available at All Hours").setAllUncheckedText("Not Free to Workout").setSelectAll(false).setTitle(getResources().getString(R.string.title)).setMinSelectedItems(0);
        tueTimes.setListAdapter(adapter2).setListener(new MultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                for(int i=1; i<25; i++){
                    System.out.println(String.valueOf(selected[i-1]));
                }
            }
        }).setAllCheckedText("Available at All Hours").setAllUncheckedText("Not Free to Workout").setSelectAll(false).setTitle(getResources().getString(R.string.title)).setMinSelectedItems(0);
        wedTimes.setListAdapter(adapter3).setListener(new MultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                for(int i=1; i<25; i++){
                    System.out.println(String.valueOf(selected[i-1]));
                }
            }
        }).setAllCheckedText("Available at All Hours").setAllUncheckedText("Not Free to Workout").setSelectAll(false).setTitle(getResources().getString(R.string.title)).setMinSelectedItems(0);
        thuTimes.setListAdapter(adapter4).setListener(new MultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                for(int i=1; i<25; i++){
                    System.out.println(String.valueOf(selected[i-1]));
                }
            }
        }).setAllCheckedText("Available at All Hours").setAllUncheckedText("Not Free to Workout").setSelectAll(false).setTitle(getResources().getString(R.string.title)).setMinSelectedItems(0);
        friTimes.setListAdapter(adapter5).setListener(new MultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                for(int i=1; i<25; i++){
                    System.out.println(String.valueOf(selected[i-1]));
                }
            }
        }).setAllCheckedText("Available at All Hours").setAllUncheckedText("Not Free to Workout").setSelectAll(false).setTitle(getResources().getString(R.string.title)).setMinSelectedItems(0);
        satTimes.setListAdapter(adapter6).setListener(new MultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                for(int i=1; i<25; i++){
                    System.out.println(String.valueOf(selected[i-1]));
                }
            }
        }).setAllCheckedText("Available at All Hours").setAllUncheckedText("Not Free to Workout").setSelectAll(false).setTitle(getResources().getString(R.string.title)).setMinSelectedItems(0);
        sunTimes.setListAdapter(adapter7).setListener(new MultiSelectSpinner.MultiSpinnerListener() {
            @Override
            public void onItemsSelected(boolean[] selected) {
                for(int i=1; i<25; i++){
                    System.out.println(String.valueOf(selected[i-1]));
                }
            }
        }).setAllCheckedText("Available at All Hours").setAllUncheckedText("Not Free to Workout").setSelectAll(false).setTitle(getResources().getString(R.string.title)).setMinSelectedItems(0);
    }
}
