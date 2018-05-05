package com.example.lykia.roommate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class OwnProfileActivity extends AppCompatActivity {
    private BottomNavigationView navButton;


    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_profile);

        navButton=(BottomNavigationView)findViewById(R.id.bottomBar);
        navButton.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home_nav:
                        Intent intent=new Intent(OwnProfileActivity.this,HomeActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.search_nav:
                        Intent intent1=new Intent(OwnProfileActivity.this,SearchActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.profile_nav:
                        Intent intent2=new Intent(OwnProfileActivity.this,OwnProfileActivity.class);
                        startActivity(intent2);
                        return true;
                }
                return false;
            }
        });

        toolbar = (Toolbar) findViewById(R.id.ownProfileAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout);
        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

}
