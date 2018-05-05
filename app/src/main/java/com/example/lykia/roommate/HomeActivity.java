package com.example.lykia.roommate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private Spinner spinnerHayvan;
    private Spinner spinnerCins;
    private Spinner spinnerCinsiyet;
    private ListView listViewAnimal;
    private BottomNavigationView navButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navButton=(BottomNavigationView)findViewById(R.id.bottomBar);
        navButton.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home_nav:
                        Intent intent=new Intent(HomeActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.search_nav:
                        Intent intent1=new Intent(HomeActivity.this,SearchActivity.class);
                        startActivity(intent1);
                        finish();
                        break;
                    case R.id.profile_nav:
                        Intent intent2=new Intent(HomeActivity.this,OwnProfileActivity.class);
                        startActivity(intent2);
                        finish();
                        break;
                }
                return false;
            }
        });
        spinnerHayvan=(Spinner)findViewById((R.id.animalSpinner));
        spinnerCins=(Spinner)findViewById((R.id.raceSpinner));
        spinnerCinsiyet=(Spinner)findViewById((R.id.genderSpinner));
        listViewAnimal=(ListView)findViewById(R.id.LVshowAnimal);

        ArrayAdapter adapterHayvan = ArrayAdapter.createFromResource(this,R.array.cinsiyet, android.R.layout.simple_spinner_item);
        adapterHayvan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHayvan.setAdapter(adapterHayvan);
        spinnerHayvan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), parent.getItemIdAtPosition(position)+"seçilen ", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter adapterCins = ArrayAdapter.createFromResource(this,R.array.cinsiyet, android.R.layout.simple_spinner_item);
        adapterCins.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCins.setAdapter(adapterCins);
        spinnerHayvan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), parent.getItemIdAtPosition(position)+"seçilen ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter adapterCinsiyet = ArrayAdapter.createFromResource(this,R.array.cinsiyet, android.R.layout.simple_spinner_item);
        adapterCinsiyet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCinsiyet.setAdapter(adapterCinsiyet);
        spinnerHayvan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getBaseContext(), parent.getItemIdAtPosition(position)+"seçilen ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



//        startActivity(new Intent(this, AnimalActivity.class));
    }
}
