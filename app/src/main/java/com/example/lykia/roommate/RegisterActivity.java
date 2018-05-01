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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lykia.roommate.DAOs.UserDAO;
import com.example.lykia.roommate.DTOs.UserDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private Button btnRegister;
    private EditText textFirstName;
    private EditText textLastName;
    private EditText textMail;
    private EditText textPassword;
    private EditText textLocation;

    private String firstName;
    private String lastName;
    private String fullName;
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

                    registerUserToFirebase(fullName, mail, password);
                }
            }
        });
    }

    public class Background extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            boolean methodResult = false;
            String result = null;
            methodResult = checkMail();
            result = "checkMail" + methodResult;

            if (!methodResult) {
                methodResult = registerUser();
                result = "registerUser" + methodResult;
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String text;

            switch (result) {
                case "checkMailtrue":
                    text = "Bu mail adresi zaten sistemi kayıtlıdır.";

                    showToast(text);

                    btnRegister.setEnabled(true);
                    break;
                case "registerUsertrue":
                    text = "Başarıyla üye olundu.\nGiriş sayfasına yönlendiriliyorsunuz...";

                    showToast(text);

                    btnRegister.setEnabled(true);

                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                    break;
                default:
                    break;
            }
        }
    }

    private boolean isNullEditText() {
        return textFirstName.getText().length() == 0 || textLastName.getText().length() == 0 || textMail.getText().length() == 0 ||
                textPassword.getText().length() == 0 || textLocation.getText().length() == 0;
    }

    private void setProperCase() {
        Locale trLocale = Locale.forLanguageTag("tr-TR");

        firstName = properCase(textFirstName.getText().toString(), trLocale);
        lastName = properCase(textLastName.getText().toString(), trLocale);
        fullName = firstName + " " + lastName;
        mail = textMail.getText().toString().toLowerCase(trLocale);
        password = toMD5(toSHA1(textPassword.getText().toString()));
        location = properCase(textLocation.getText().toString(), trLocale);
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

    private boolean registerUser() {
        UserDTO user = new UserDTO();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMail(mail);
        user.setPassword(password);
        user.setLocation(location);

        return UserDAO.insertUser(user);
    }

    private void registerUserToFirebase(final String fullName, String mail, String password) {
        mAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = currentUser.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", fullName);
                    userMap.put("image", "default");
                    userMap.put("thumbImage", "default");

                    mDatabase.setValue(userMap);
                } else {
                    String text = "Kayıt gerçekleşemedi.";

                    showToast(text);
                }
            }
        });
    }

    private boolean checkMail() {
        UserDTO user = UserDAO.getUserByMail(textMail.getText().toString().toLowerCase());

        return user != null;
    }

    private void showToast(String text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);

        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setGravity(Gravity.CENTER);

        toast.show();
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
