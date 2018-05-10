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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lykia.roommate.DAOs.AdminDAO;
import com.example.lykia.roommate.DAOs.ArticleDAO;
import com.example.lykia.roommate.DTOs.AdminDTO;
import com.example.lykia.roommate.DTOs.ArticleDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class AddArticleActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CircleImageView image;
    private Button addImageButton;
    private Button addArticleButton;
    private EditText articleHeader;
    private EditText articleText;
    private Uri resultUri;
    private StorageReference imageStorage;

    private boolean resultCheck = true;
    private String imagePath;
    private int articleId;
    private int adminId;
    private byte[] thumbByte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);

        image = (CircleImageView) findViewById(R.id.articleImage);
        addImageButton = (Button) findViewById(R.id.addImageButton);
        addArticleButton = (Button) findViewById(R.id.addArticleButton);
        articleHeader = (EditText) findViewById(R.id.headerEditText);
        articleText = (EditText) findViewById(R.id.articleEditText);
        toolbar = (Toolbar) findViewById(R.id.addArticleAppBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Yazı Ekle");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adminId = getIntent().getExtras().getInt("adminId");
        imageStorage = FirebaseStorage.getInstance().getReference();

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleyIntent = new Intent();
                galleyIntent.setType("image/*");
                galleyIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleyIntent, "SELECT IMAGE"), 1);
            }
        });

        addArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File thumbFile = new File(resultUri.getPath());
                Bitmap thumbImage = new Compressor(AddArticleActivity.this).setMaxHeight(160).setMaxWidth(90).setQuality(50).compressToBitmap(thumbFile);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                thumbByte = stream.toByteArray();

                new Background().execute();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(16, 9)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                File thumbFile = new File(resultUri.getPath());

                Picasso.get().load(thumbFile).placeholder(R.drawable.default_rehoming_icon).into(image);
            }
        }
    }

    public class Background extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            ArticleDTO article = new ArticleDTO();

            AdminDTO admin = AdminDAO.getAdminById(adminId);

            article.setAdmin(admin);
            article.setHeader(articleHeader.getText().toString());
            article.setText(articleText.getText().toString());

            boolean result = ArticleDAO.insertArticle(article);

            if (!result) {
                String text = "Sistemde bir sorun yaşandı.\nLütfen tekrar deneyiniz.";

                showToast(text);

                resultCheck = false;
            }

            articleId = ArticleDAO.getArticleCount();

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            StorageReference filePath = imageStorage.child("articleImages").child(articleId + ".jpg");

            UploadTask uploadTask = filePath.putBytes(thumbByte);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        imagePath = task.getResult().getDownloadUrl().toString();

                        new UpdateImagePath().execute();

                        if (resultCheck) {
                            String text = "Başarıyla eklendi.";

                            showToast(text);

                            articleHeader.setText("");
                            articleText.setText("");
                            image.setImageResource(R.drawable.default_rehoming_icon);
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
            ArticleDTO article = ArticleDAO.getArticleById(articleId);
            article.setImagePath(imagePath);
            ArticleDAO.updateArticle(article);

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);


        }
    }
}
