package com.example.lykia.roommate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class AdminPanelActivity extends AppCompatActivity {
    Button animal;
    Button article;
    int adminId;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        animal=(Button)findViewById(R.id.animalButton);
        article=(Button)findViewById(R.id.articleButton);

        adminId = getIntent().getExtras().getInt("adminId");

        toolbar = (Toolbar) findViewById(R.id.adminPanelAppBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin Paneli");

        animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminPanelActivity.this,AdoptedActivity.class));
            }
        });

        article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminPanelActivity.this,AddArticleActivity.class).putExtra("adminId", adminId));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_app_bar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out:
                startActivity(new Intent(AdminPanelActivity.this, MainActivity.class));
                finish();
                break;
        }

        return false;
    }
}