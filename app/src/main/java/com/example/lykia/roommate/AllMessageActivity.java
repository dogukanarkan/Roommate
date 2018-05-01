package com.example.lykia.roommate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.lykia.roommate.DAOs.AnimalDAO;
import com.example.lykia.roommate.DAOs.UserDAO;
import com.example.lykia.roommate.DTOs.AnimalDTO;
import com.example.lykia.roommate.DTOs.RehomingDTO;
import com.example.lykia.roommate.DTOs.UserDTO;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AllMessageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView usersList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_message);

        toolbar = (Toolbar) findViewById(R.id.allMessageAppBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mesajlar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        usersList = (RecyclerView) findViewById(R.id.allMessagesList);
        usersList.setHasFixedSize(true);
        usersList.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<FirebaseUsers, UsersViewHolder> adapter = new FirebaseRecyclerAdapter<FirebaseUsers, UsersViewHolder>(
                FirebaseUsers.class, R.layout.users_single_layout, UsersViewHolder.class, mDatabase
        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, FirebaseUsers model, int position) {
                viewHolder.setName(model.getName());
            }
        };

        usersList.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return true;
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        View view;

        public UsersViewHolder(View itemView) {
            super(itemView);

            view = itemView;
        }

        public void setName(String name) {
            TextView userNameView = (TextView) view.findViewById(R.id.userSingleName);
            userNameView.setText(name);
        }
    }
}
