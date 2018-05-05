package com.example.lykia.roommate;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.lykia.roommate.DAOs.RehomingDAO;
import com.example.lykia.roommate.DTOs.RehomingDTO;
import com.squareup.picasso.Picasso;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;


public class UserProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyUserAdapter adapter;
    private List<RehomingDTO> pets;
    private Toolbar toolbar;
    private TextView test;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        test = (TextView) findViewById(R.id.textViewName);
        toolbar = (Toolbar) findViewById(R.id.userProfileAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mesajlar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userId = getIntent().getStringExtra("userId");
        test.setText(userId);

        recyclerView =findViewById(R.id.recycler_view_UserProfile);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new Background().execute();

    }

    public class MyUserAdapter extends RecyclerView.Adapter<MyUserHolder> {

        List<RehomingDTO> pets;

        public MyUserAdapter(List<RehomingDTO> pets) {
            this.pets = pets;
        }

        @Override
        public MyUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pets_single_layout, parent, false);
            MyUserHolder holder = new MyUserHolder(v);

            return holder;
        }

        @Override
        public void onBindViewHolder(MyUserHolder holder, final int position) {
            holder.textViewRace.setText(pets.get(position).getRace().getRaceName());
            holder.textViewMonth.setText(Integer.toString(pets.get(position).getMonthOld()));
            holder.textViewGender.setText(pets.get(position).getGender());
            Picasso.get().load(pets.get(position).getImagePath()).placeholder(R.drawable.default_rehoming_icon).into(holder.image);

            holder.messageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UserProfileActivity.this, MessageActivity.class)
                            .putExtra("messageUserMail", pets.get(position).getUser().getMail()));
                }
            });

        }

        @Override
        public int getItemCount() {
            return pets.size();
        }
    }

    public class Background extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            pets = RehomingDAO.getAllRehomingPets();

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            adapter = new MyUserAdapter(pets);
            recyclerView.setAdapter(adapter);


        }
    }
}

 class MyUserHolder extends RecyclerView.ViewHolder {

    CircleImageView image;
    TextView textViewRace;
    TextView textViewGender;
    TextView textViewMonth;
    Button messageButton;


    public MyUserHolder(View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.image);
        textViewRace = itemView.findViewById(R.id.textViewRace);
        textViewGender = itemView.findViewById(R.id.textViewGender);
        textViewMonth = itemView.findViewById(R.id.textViewAge);
        messageButton = itemView.findViewById(R.id.messageButton);

    }
}