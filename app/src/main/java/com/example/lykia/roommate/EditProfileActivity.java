package com.example.lykia.roommate;

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lykia.roommate.DAOs.ArticleDAO;
import com.example.lykia.roommate.DAOs.UserDAO;
import com.example.lykia.roommate.DTOs.UserDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class EditProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseUser currentUser;
    private FirebaseAuth auth;
    private DatabaseReference userDatabase;
    private StorageReference imageStorage;
    private String currentUserID;
    private CircleImageView imageView;
    private Button imageButton;
    private Button infoButton;
    private Button passwordButton;
    private EditText textFirstName;
    private EditText textLastName;
    private EditText textLocation;
    private EditText textPassword1;
    private EditText textPassword2;

    private String firstName;
    private String lastName;
    private String fullName;
    private String location;
    private String password;

    private int id;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        id = getIntent().getExtras().getInt("userId");

        textFirstName = (EditText) findViewById(R.id.editProfileFirstName);
        textLastName = (EditText) findViewById(R.id.editProfileLastName);
        textLocation = (EditText) findViewById(R.id.editProfileLocation);
        textPassword1 = (EditText) findViewById(R.id.editProfilePassword1);
        textPassword2 = (EditText) findViewById(R.id.editProfilePassword2);

        toolbar = (Toolbar) findViewById(R.id.editProfileAppBar);
        imageView = (CircleImageView) findViewById(R.id.editProfileImage);
        imageButton = (Button) findViewById(R.id.editProfileUpdateImageButton);
        infoButton = (Button) findViewById(R.id.editProfileUpdateInfoButton);
        passwordButton = (Button) findViewById(R.id.editProfileUpdatePasswordButton);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profili Düzenle");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserID = currentUser.getUid();
        imageStorage = FirebaseStorage.getInstance().getReference();
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String image = dataSnapshot.child("image").getValue().toString();

                if (!image.equals("default")) {
                    Picasso.get().load(image).placeholder(R.drawable.person).into(imageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        new GetInfoFromDatabase().execute();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "SELECT IMAGE"), 1);
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProperCase();

                if (isNullEditText()) {
                    text = "Lütfen tüm bilgileri doldurunuz.";

                    showToast(text);
                } else {
                    new UpdateInfo().execute();
                }
            }
        });

        passwordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProperCase();

                if (!checkPassword()) {
                    text = "Parolalar uyuşmamaktadır.";

                    showToast(text);
                } else {
                    new UpdatePassword().execute();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri).setAspectRatio(1, 1).start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                File thumbFile = new File(resultUri.getPath());
                Bitmap thumbImage = new Compressor(this).setMaxHeight(150).setMaxWidth(150).setQuality(60).compressToBitmap(thumbFile);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                final byte[] thumbByte = stream.toByteArray();

                StorageReference filePath = imageStorage.child("profileImages").child(currentUserID + ".jpg");
                final StorageReference thumbFilePath = imageStorage.child("profileImages").child("thumbs").child(currentUserID + ".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String downloadUrl = task.getResult().getDownloadUrl().toString();
                            UploadTask uploadTask = thumbFilePath.putBytes(thumbByte);

                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumbTask) {
                                    final String thumbDownloadUrl = thumbTask.getResult().getDownloadUrl().toString();

                                    if (thumbTask.isSuccessful()) {
                                        Map updateHashMap = new HashMap();

                                        updateHashMap.put("image", downloadUrl);
                                        updateHashMap.put("thumbImage", thumbDownloadUrl);

                                        userDatabase.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()) {
                                                    new UpdateImagePath().execute(thumbDownloadUrl);

                                                    text = "Fotoğraf başarıyla güncellenmiştir.";

                                                    showToast(text);
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public class GetInfoFromDatabase extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            getInfoFromDatabase();

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            fillEditText();
        }
    }

    public class UpdateInfo extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            updateInfo();

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            updateInfoToFirebase(fullName);
        }
    }

    public class UpdatePassword extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            updatePassword();

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            updatePasswordToFirebase(password);
        }
    }

    public class UpdateImagePath extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            updateImagePath(params[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);
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

    private void getInfoFromDatabase() {
        UserDTO user = UserDAO.getUserById(id);

        firstName = user.getFirstName();
        lastName = user.getLastName();
        location = user.getLocation();
    }

    private void fillEditText() {
        textFirstName.setText(firstName);
        textLastName.setText(lastName);
        textLocation.setText(location);
    }

    private boolean updateInfo() {
        UserDTO user = UserDAO.getUserById(id);

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setLocation(location);

        return UserDAO.updateUser(user);
    }

    private boolean updatePassword() {
        UserDTO user = UserDAO.getUserById(id);

        user.setPassword(password);

        return UserDAO.updateUser(user);
    }

    private boolean updateImagePath(String imagePath) {
        UserDTO user = UserDAO.getUserById(id);

        user.setImagePath(imagePath);

        return UserDAO.updateUser(user);
    }

    private void updateInfoToFirebase(String fullName) {
        userDatabase.child("name").setValue(fullName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    text = "Bilgileriniz başarıyla güncellenmiştir.";

                    showToast(text);
                } else {
                    text = "Bilgileriniz güncellenemedi.";

                    showToast(text);
                }
            }
        });
    }

    private void updatePasswordToFirebase(String password) {
        currentUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    text = "Parolanız başarıyla değiştirilmiştir.";

                    showToast(text);
                } else {
                    text = "Parolanız değiştirilemedi.";

                    showToast(text);
                }
            }
        });
    }

    private boolean isNullEditText() {
        return textFirstName.getText().length() == 0 ||
                textLastName.getText().length() == 0 ||
                textLocation.getText().length() == 0;
    }

    private boolean checkPassword() {
        return textPassword1.getText().length() != 0 &&
                textPassword1.getText().toString().equals(textPassword2.getText().toString());
    }

    private void setProperCase() {
        Locale trLocale = Locale.forLanguageTag("tr-TR");

        firstName = properCase(textFirstName.getText().toString(), trLocale);
        lastName = properCase(textLastName.getText().toString(), trLocale);
        fullName = firstName + " " + lastName;
        location = properCase(textLocation.getText().toString(), trLocale);
        password = toMD5(toSHA1(textPassword1.getText().toString()));
    }

    private String properCase(String input, Locale locale) {
        if (input.length() == 0) {
            return "";
        }

        if (input.length() == 1) {
            return input.toUpperCase(locale);
        }

        return input.substring(0, 1).toUpperCase(locale) + input.substring(1).toLowerCase(locale);
    }

    private String toMD5(String input) {
        String hashPassword = null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes(), 0, input.length());

            hashPassword = new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hashPassword;
    }

    private String toSHA1(String input) {
        String hashPassword = null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.reset();
            md.update(input.getBytes("UTF-8"));

            hashPassword = new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return hashPassword;
    }
}