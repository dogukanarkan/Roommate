package com.example.lykia.roommate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lykia.roommate.DAOs.AnimalDAO;
import com.example.lykia.roommate.DAOs.RaceDAO;
import com.example.lykia.roommate.DAOs.RehomingDAO;
import com.example.lykia.roommate.DAOs.UserDAO;
import com.example.lykia.roommate.DTOs.AnimalDTO;
import com.example.lykia.roommate.DTOs.RaceDTO;
import com.example.lykia.roommate.DTOs.RehomingDTO;
import com.example.lykia.roommate.DTOs.UserDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class RehomingActivity extends AppCompatActivity {

    private CircleImageView mDisplayImage;
    private Button mImageBtn;
    private Button mRehomingBtn;
    private static  final int GALLERY_PICK=1;

    private boolean resultCheck = true;
    private String imagePath;

    private Toolbar toolbar;
    private Spinner animal;
    private Spinner race;
    private Spinner gender;
    private EditText month;
    private EditText additional;
    private StorageReference imageStorage;
    private int rehomingId;
    private int userId;

    private List<AnimalDTO> animals;
    private List<RaceDTO> races;
    private Uri resultUri;
    private byte[] thumbByte;

    Spinner animalSpinner;
    Spinner raceSpinner;
    Spinner genderSpinner;
    ArrayAdapter<CharSequence>adapter3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rehoming);

        toolbar = (Toolbar) findViewById(R.id.rehomingAppBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sahiplendir");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userId = getIntent().getExtras().getInt("userId");
        imageStorage = FirebaseStorage.getInstance().getReference();

        month = (EditText) findViewById(R.id.ay);
        additional = (EditText) findViewById(R.id.ek);

        mImageBtn=(Button)findViewById(R.id.addPhotoBtn);
        mRehomingBtn=(Button)findViewById(R.id.rehomingBtn);
        mDisplayImage=(CircleImageView)findViewById(R.id.userSingleImage);
        animalSpinner=(Spinner)findViewById(R.id.spinner);
        raceSpinner=(Spinner)findViewById(R.id.spinner2);
        genderSpinner=(Spinner)findViewById(R.id.spinner3);
        adapter3=ArrayAdapter.createFromResource(this,R.array.cinsiyet,android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter3);

        new GetSpinnersFromDatabase().execute();

        animalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, final long id) {
                new GetRaceSpinnerFromDatabase().execute((int) id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),GALLERY_PICK);
            }
        });

        mRehomingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File thumbFile = new File(resultUri.getPath());
                Bitmap thumbImage = new Compressor(RehomingActivity.this).setMaxHeight(100).setMaxWidth(100).setQuality(30).compressToBitmap(thumbFile);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                thumbByte = stream.toByteArray();

                new Background().execute();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return true;
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==GALLERY_PICK&&resultCode==RESULT_OK){
            Uri imageUri=data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                File thumbFile = new File(resultUri.getPath());

                Picasso.get().load(thumbFile).placeholder(R.drawable.default_rehoming_icon).into(mDisplayImage);
            }
        }
    }

    public class Background extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            RehomingDTO rehoming = new RehomingDTO();

            int animalId = (int) animalSpinner.getSelectedItemId();
            AnimalDTO animal = AnimalDAO.getAnimalById(animalId);

            int raceId = (int) raceSpinner.getSelectedItemId();
            RaceDTO race = RaceDAO.getRaceById(raceId);

            UserDTO user = UserDAO.getUserById(userId);

            rehoming.setRace(race);
            rehoming.setUser(user);

            rehoming.setGender(genderSpinner.getSelectedItem().toString());
            rehoming.setMonthOld(Integer.parseInt(month.getText().toString()));
            rehoming.setInformation(additional.getText().toString());
            rehoming.setCode(randomCode());

            boolean result = RehomingDAO.insertRehomingPet(rehoming);

            if (!result) {
                String text = "Sistemde bir sorun yaşandı.\nLütfen tekrar deneyiniz.";

                showToast(text);

                resultCheck = false;
            }

            rehomingId = RehomingDAO.getRehomingCount();

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            StorageReference filePath = imageStorage.child("animalImages").child(rehomingId + ".jpg");

            UploadTask uploadTask = filePath.putBytes(thumbByte);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        imagePath = task.getResult().getDownloadUrl().toString();

                        new UpdateImagePath().execute();

                        if (resultCheck) {
                            startActivity(new Intent(RehomingActivity.this, OwnProfileActivity.class).putExtra("userId", userId));
                            finish();
                        }
                    }
                }
            });
        }
    }

    private void showToast(String text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);

        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setGravity(Gravity.CENTER);

        toast.show();
    }

    public class UpdateImagePath extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            RehomingDTO rehoming = RehomingDAO.getRehomingPetById(rehomingId);
            rehoming.setImagePath(imagePath);
            RehomingDAO.updateRehomingPet(rehoming);

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);


        }
    }

    public class GetSpinnersFromDatabase extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            animals = AnimalDAO.getAllAnimals();
            races = RaceDAO.getAllRaces();

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            populateAnimalSpinner();
            populateRaceSpinner(races);
        }
    }

    public class GetRaceSpinnerFromDatabase extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... ints) {
            races = RaceDAO.getRacesByAnimalId(ints[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            populateRaceSpinner(races);
        }
    }

    private void populateAnimalSpinner() {
        List<String> list = new ArrayList<>();

        list.add("Hayvan");
        for (int i = 0; i < animals.size(); i++) {
            list.add(animals.get(i).getAnimalName());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        animalSpinner.setAdapter(spinnerAdapter);
    }

    private void populateRaceSpinner(List<RaceDTO> races) {
        List<String> list = new ArrayList<>();

        list.add("Cins");
        for (int i = 0; i < races.size(); i++) {
            list.add(races.get(i).getRaceName());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        raceSpinner.setAdapter(spinnerAdapter);
    }

    private String randomCode() {
        Random random = new Random();
        int max = 99999999;
        int min = 10000000;
        String rand = Integer.toString(random.nextInt(max) + min);

        return rand;
    }
}

