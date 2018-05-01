package com.example.lykia.roommate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BriefArticleActivity extends AppCompatActivity {

    private RecyclerView recycler_view;
    private List<AnimalAttribute> animal_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brief_article);
        recycler_view = (RecyclerView)findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        recycler_view.setLayoutManager(layoutManager);

        animal_list = new ArrayList<AnimalAttribute>();

        animal_list.add(new AnimalAttribute("Tekir Kedi",R.drawable.busra));
        animal_list.add(new AnimalAttribute("Chow Chow",R.drawable.busra));
        animal_list.add(new AnimalAttribute("MalteseTerier",R.drawable.busra));
        animal_list.add(new AnimalAttribute("British Long Hair",R.drawable.busra));



        SimpleRecyclerAdapter adapter_items = new SimpleRecyclerAdapter(animal_list, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
//                Log.d("position", "Tıklanan Pozisyon:" + position);
//                AnimalAttribute animal = animal_list.get(position);
//                Toast.makeText(getApplicationContext(),"pozisyon:"+" "+position+" "+"Ad:"+animal.getGenus(),Toast.LENGTH_SHORT).show();
                startActivity(new Intent(BriefArticleActivity.this, DetailArticleActivity.class));
                finish();
            }
        });
        recycler_view.setHasFixedSize(true);

        recycler_view.setAdapter(adapter_items);

        recycler_view.setItemAnimator(new DefaultItemAnimator());


    }

}
