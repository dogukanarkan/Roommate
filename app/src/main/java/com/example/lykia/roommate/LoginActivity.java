package com.example.lykia.roommate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lykia.roommate.DAOs.AdminDAO;
import com.example.lykia.roommate.DAOs.UserDAO;
import com.example.lykia.roommate.DTOs.AdminDTO;
import com.example.lykia.roommate.DTOs.UserDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private ProgressDialog loginProgress;
    private Button loginButton;
    private EditText textMail;
    private EditText textPassword;

    private String mail;
    private String password;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginProgress = new ProgressDialog(this);
        loginButton = (Button) findViewById(R.id.loginButton);
        textMail = (EditText) findViewById(R.id.loginEditTextMail);
        textPassword = (EditText) findViewById(R.id.loginEditTextPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setEnabled(false);

                String text;

                if (isNullEditText()) {
                    text = "Lütfen bilgilerinizi eksiksiz giriniz.";

                    showToast(text);

                    loginButton.setEnabled(true);
                } else {
                    showProgress();

                    mail = textMail.getText().toString();
                    password = toMD5(toSHA1(textPassword.getText().toString()));

                    new Background().execute();
                }
            }
        });
    }

    public class Background extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            if (loginUser()) {
                return "user";
            } else if (loginAdmin()) {
                return "admin";
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String text;

            if (result == null) {
                loginProgress.dismiss();

                text = "Giriş yapılamadı.\nBilgilerinizi kontrol ediniz.";

                showToast(text);

                loginButton.setEnabled(true);
            } else if (result.equals("user")) {
                loginProgress.dismiss();

                text = "Başarıyla giriş yapıldı.";

                showToast(text);

                loginButton.setEnabled(true);

                loginUserToFirebase(mail, password);

                startActivity(new Intent(LoginActivity.this, HomeActivity.class).putExtra("userId", id));
                finish();
            } else if (result.equals("admin")) {
                loginProgress.dismiss();

                text = "Başarıyla giriş yapıldı.";

                showToast(text);

                loginButton.setEnabled(true);

                startActivity(new Intent(LoginActivity.this, AdminPanelActivity.class).putExtra("adminId", id));
                finish();
            }
        }
    }

    private boolean isNullEditText() {
        return textMail.getText().length() == 0 || textPassword.getText().length() == 0;
    }

    private boolean loginUser() {
        UserDTO user = UserDAO.getUserForLogin(mail, password);

        if (user != null) {
            id = user.getUserId();

            return true;
        }

        return false;
    }

    private boolean loginAdmin() {
        AdminDTO admin = AdminDAO.getAdminForLogin(mail, password);

        if (admin != null) {
            id = admin.getAccountId();
            AdminDAO.updateAdmin(admin);

            return true;
        }

        return false;
    }

    private void loginUserToFirebase(String mail, String password) {
        mAuth.signInWithEmailAndPassword(mail, password);
    }

    private void showToast(String text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);

        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setGravity(Gravity.CENTER);

        toast.show();
    }

    private void showProgress() {
        loginProgress.setTitle("Giriş Yapılıyor");
        loginProgress.setMessage("Lütfen bekleyiniz.");
        loginProgress.setCanceledOnTouchOutside(false);

        loginProgress.show();
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
