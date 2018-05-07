package com.example.lykia.roommate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class OwnProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView navButton;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private int userId;

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

        userId = 1;
        setNavigationViewListener();


        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout);
        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (item.getItemId() == R.id.message_nav) {
            startActivity(new Intent(OwnProfileActivity.this, AllMessageActivity.class));
        }
        return super.onOptionsItemSelected(item);

    }
    public boolean onMenuItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.rehoming_nav: {
                Intent intent=new Intent(OwnProfileActivity.this,RehomingActivity.class);
                startActivity(intent);
                break;
            }
        }
        //close navigation drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rehoming_nav:
                startActivity(new Intent(OwnProfileActivity.this, RehomingActivity.class).putExtra("userId", userId));
                break;
            case R.id.article_nav:
                startActivity(new Intent(OwnProfileActivity.this,BriefArticleActivity.class));
                break;
            case R.id.edit_profile_nav:
                startActivity(new Intent(OwnProfileActivity.this, EditProfileActivity.class).putExtra("userId", userId));
                break;
            case R.id.logout_nav:
                startActivity(new Intent(OwnProfileActivity.this, MainActivity.class));
                break;

        }
        return false;
    }


}
