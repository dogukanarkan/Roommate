package com.example.lykia.roommate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity {
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister=(Button)findViewById(R.id.registerBtn);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intnt= new Intent(RegisterActivity.this,HomeActivity.class);
                startActivity(intnt);
            }
        });
    }
}
