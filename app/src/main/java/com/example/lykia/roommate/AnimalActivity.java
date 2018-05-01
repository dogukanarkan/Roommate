package com.example.lykia.roommate;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnimalActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CircleImageView circleImageView;
    private TextView tw1;
    private TextView tw2;
    private Button msgButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);

        circleImageView = (CircleImageView) findViewById(R.id.pp_animal);
        tw1 = (TextView) findViewById(R.id.baseTextView);
        tw2 = (TextView) findViewById(R.id.detailTextView);
        msgButton = (Button) findViewById(R.id.messageButtonAnimal);


        toolbar = (Toolbar) findViewById(R.id.animalAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("#refkod");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == android.R.id.home) {
            finish();
            startActivity(new Intent(AnimalActivity.this, UserProfileActivity.class));
        }

        return true;
    }


}