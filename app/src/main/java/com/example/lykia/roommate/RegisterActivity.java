package com.example.lykia.roommate;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lykia.roommate.DAOs.UserDAO;
import com.example.lykia.roommate.DTOs.UserDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button btnRegister;
    private EditText textFirstName;
    private EditText textLastName;
    private EditText textMail;
    private EditText textPassword;
    private EditText textLocation;

    private String firstName;
    private String lastName;
    private String mail;
    private String password;
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        btnRegister = (Button) findViewById(R.id.registerBtn);
        textFirstName = (EditText) findViewById(R.id.registerEditTextFirstName);
        textLastName = (EditText) findViewById(R.id.registerEditTextLastName);
        textMail = (EditText) findViewById(R.id.registerEditTextMail);
        textPassword = (EditText) findViewById(R.id.registerEditTextPassword);
        textLocation = (EditText) findViewById(R.id.registerEditTextLocation);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnRegister.setEnabled(false);

                String text;

                setProperCase();

                if (isNullEditText()) {
                    text = "Tüm bilgileri doldurmak zorunludur.";

                    showToast(text);

                    btnRegister.setEnabled(true);
                } else {
                    new Background().execute("checkMail", "registerUser");

                    registerUserToFirebase(mail, password);
                }
            }
        });
    }
}
