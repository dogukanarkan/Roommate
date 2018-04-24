package com.example.lykia.roommate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


public class HomeActivity extends AppCompatActivity {

    Spinner spinnerHayvan;
    Spinner spinnerCins;
    Spinner spinnerCinsiyet;
    ListView listViewAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        spinnerHayvan=(Spinner)findViewById((R.id.animalSpinner));
        spinnerCins=(Spinner)findViewById((R.id.raceSpinner));
        spinnerCinsiyet=(Spinner)findViewById((R.id.genderSpinner));
        listViewAnimal=(ListView)findViewById(R.id.LVshowAnimal);

        ArrayAdapter adapterHayvan = ArrayAdapter.createFromResource(this,R.array.Animals, android.R.layout.simple_spinner_item);
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

        ArrayAdapter adapterCins = ArrayAdapter.createFromResource(this,R.array.Races, android.R.layout.simple_spinner_item);
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

        ArrayAdapter adapterCinsiyet = ArrayAdapter.createFromResource(this,R.array.Genders, android.R.layout.simple_spinner_item);
        adapterCinsiyet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCinsiyet.setAdapter(adapterCinsiyet);
        spinnerHayvan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), parent.getItemIdAtPosition(position)+"seçilen ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

}
