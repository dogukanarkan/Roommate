package com.example.lykia.roommate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


public class ForgotPasswordActivity extends AppCompatActivity {
private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        toolbar=(Toolbar) findViewById(R.id.forgot_password_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Parolamı Unuttum");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}
