package com.example.lykia.roommate;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

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

        btnRegister = (Button) findViewById(R.id.registerBtn);
        textFirstName = (EditText) findViewById(R.id.loginEditTextFirstName);
        textLastName = (EditText) findViewById(R.id.loginEditTextLastName);
        textMail = (EditText) findViewById(R.id.loginEditTextMail);
        textPassword = (EditText) findViewById(R.id.loginEditTextPassword);
        textLocation = (EditText) findViewById(R.id.loginEditTextLocation);

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
        mail = textMail.getText().toString().toLowerCase(trLocale);
        password = textPassword.getText().toString();
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
        user.setPassword(toMD5(toSHA1(password)));
        user.setLocation(location);

        return UserDAO.insertUser(user);
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