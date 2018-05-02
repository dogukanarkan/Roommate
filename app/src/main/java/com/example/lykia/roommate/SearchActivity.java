package com.example.lykia.roommate;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.lykia.roommate.DAOs.UserDAO;
import com.example.lykia.roommate.DTOs.UserDTO;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<UserDTO> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = (RecyclerView) findViewById(R.id.searchRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new Background().execute();
    }

    public class Background extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            list = UserDAO.getAllUsers();

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            adapter = new MyAdapter(list);
            recyclerView.setAdapter(adapter);
        }
    }
}
