package com.example.lykia.roommate;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ListView lv;
    String[] names = {"Hilal Erisev", "Doğukan Arkan", "Büşra Akgüller", "Tuğçe Yıldırım", "Hasan Yılmaz","Hilal Erisev", "Doğukan Arkan", "Büşra Akgüller", "Tuğçe Yıldırım", "Hasan Yılmaz","Hilal Erisev", "Doğukan Arkan", "Büşra Akgüller", "Tuğçe Yıldırım", "Hasan Yılmaz"};
    int[] images = {R.drawable.hilal, R.drawable.dogukan1,R.drawable.busra,R.drawable.tugce,R.drawable.kisi,R.drawable.hilal, R.drawable.dogukan1,R.drawable.busra,R.drawable.tugce,R.drawable.kisi,R.drawable.hilal, R.drawable.dogukan1,R.drawable.busra,R.drawable.tugce,R.drawable.kisi};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        lv=(ListView)findViewById(R.id.liste);

        Person adapter=new Person(this, names, images);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int post, long l) {

                Toast.makeText(getApplicationContext(),names[post],Toast.LENGTH_SHORT).show();

            }
        });
    }
}
