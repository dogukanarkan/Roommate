package com.example.lykia.roommate;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lykia.roommate.DAOs.UserDAO;
import com.example.lykia.roommate.DTOs.UserDTO;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchActivity extends AppCompatActivity {
    private BottomNavigationView navButton;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<UserDTO> list;
    private EditText SearchEditText;
    private TextView emptySearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchEditText = (EditText) findViewById(R.id.editText);
        emptySearch = (TextView) findViewById(R.id.emptySearch);
        recyclerView = (RecyclerView) findViewById(R.id.searchRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        navButton = (BottomNavigationView) findViewById(R.id.bottomBar);

        navButton.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_nav:
                        Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.search_nav:
                        Intent inten1=new Intent(SearchActivity.this,SearchActivity.class);
                        startActivity(inten1);
                        break;
                    case R.id.profile_nav:
                        Intent intent2=new Intent(SearchActivity.this,OwnProfileActivity.class);
                        startActivity(intent2);
                        break;

                }
                return false;
            }
        });

        new Background().execute();

        SearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<UserDTO> tempList = new ArrayList<>();
                String fullName;

                for(int k=0; k<list.size();k++) {
                    fullName = list.get(k).getFirstName() + " " + list.get(k).getLastName();

                    if(fullName.toUpperCase().contains(charSequence.toString().toUpperCase())) {
                        tempList.add(list.get(k));
                    }
                }

                if (tempList.size() == list.size()) {
                    tempList.clear();
                }

                if (tempList.size() > 0 || SearchEditText.length() == 0) {
                    emptySearch.setVisibility(View.GONE);
                } else {
                    emptySearch.setVisibility(View.VISIBLE);
                }

                adapter = new MyAdapter(tempList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

        List<UserDTO> users;

        public MyAdapter(List<UserDTO> users) {
            this.users = users;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_single_layout, parent, false);
            MyHolder holder = new MyHolder(v);

            return holder;
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            String fullName = users.get(position).getFirstName() + " " + users.get(position).getLastName();
            final String userId = Integer.toString(users.get(position).getUserId());

            holder.name.setText(fullName);
            Picasso.get().load(users.get(position).getImagePath()).placeholder(R.drawable.person).into(holder.image);
            holder.location.setText(users.get(position).getLocation());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(SearchActivity.this, UserProfileActivity.class).putExtra("userId", userId));
                }
            });
        }

        @Override
        public int getItemCount() {
            return users.size();
        }
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
        }
    }
}

class MyHolder extends RecyclerView.ViewHolder {

    CircleImageView image;
    TextView name;
    TextView location;

    public MyHolder(View itemView) {
        super(itemView);

        image = (CircleImageView) itemView.findViewById(R.id.userSingleImage);
        name = (TextView) itemView.findViewById(R.id.userSingleName);
        location = (TextView) itemView.findViewById(R.id.userSingleVariable);
    }
}