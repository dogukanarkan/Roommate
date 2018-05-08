package com.example.lykia.roommate;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.lykia.roommate.DAOs.RehomingDAO;
import com.example.lykia.roommate.DAOs.UserDAO;
import com.example.lykia.roommate.DTOs.RehomingDTO;
import com.example.lykia.roommate.DTOs.UserDTO;
import com.squareup.picasso.Picasso;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;


public class UserProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyPetsAdapter adapter;
    private List<RehomingDTO> pets;
    private UserDTO user;
    private Toolbar toolbar;
    private String userId;
    private CircleImageView userImage;
    private TextView userName;
    private TextView userLocation;
    private Button userMessageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar = (Toolbar) findViewById(R.id.userProfileAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userId = getIntent().getStringExtra("userId");

        userImage = (CircleImageView) findViewById(R.id.userImage);
        userName = (TextView) findViewById(R.id.userName);
        userLocation = (TextView) findViewById(R.id.userLocation);
        userMessageButton = (Button) findViewById(R.id.userMessageButton);
        recyclerView =findViewById(R.id.recycler_view_UserProfile);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new Background().execute();

        userMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfileActivity.this, MessageActivity.class).putExtra("messageUserMail", user.getMail()));
            }
        });
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


    public class MyPetsAdapter extends RecyclerView.Adapter<MyPetsHolder> {

        List<RehomingDTO> pets;

        public MyPetsAdapter(List<RehomingDTO> pets) {
            this.pets = pets;
        }

        @Override
        public MyPetsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pets_single_layout, parent, false);
            MyPetsHolder holder = new MyPetsHolder(v);

            return holder;
        }

        @Override
        public void onBindViewHolder(MyPetsHolder holder, final int position) {
            holder.petAnimalRaceName.setText(pets.get(position).getRace().getAnimal().getAnimalName() + " / " + pets.get(position).getRace().getRaceName());
            holder.petGenderName.setText(pets.get(position).getGender());
            holder.petAgeMonthOld.setText(setProperAge(pets.get(position).getMonthOld()));
            Picasso.get().load(pets.get(position).getImagePath()).placeholder(R.drawable.default_rehoming_icon).into(holder.petImage);

            holder.petMessageButton.setOnClickListener(new View.OnClickListener() {
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

    public String setProperAge(int monthOld) {
        String age, month, result;

        age = Integer.toString(monthOld / 12);
        month = Integer.toString(monthOld % 12);

        if (age.equals("0")) {
            result = month + " ay";
        } else {
            result = age + " yıl " + month + " ay";
        }


        return result;
    }

    public class Background extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            user = UserDAO.getUserById(Integer.parseInt(userId));
            pets = RehomingDAO.getRehomingPetsByOwnerId(Integer.parseInt(userId));

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            userName.setText(user.getFirstName() + " " + user.getLastName());
            userLocation.setText(user.getLocation());
            Picasso.get().load(user.getImagePath()).placeholder(R.drawable.person).into(userImage);

            adapter = new MyPetsAdapter(pets);
            recyclerView.setAdapter(adapter);
        }
    }
}

class MyPetsHolder extends RecyclerView.ViewHolder {

    CircleImageView petImage;
    TextView petAnimalRaceName;
    TextView petGenderName;
    TextView petAgeMonthOld;
    Button petMessageButton;

    public MyPetsHolder(View itemView) {
        super(itemView);
        petImage = itemView.findViewById(R.id.petImage);
        petAnimalRaceName = itemView.findViewById(R.id.petAnimalRaceName);
        petGenderName = itemView.findViewById(R.id.petGenderName);
        petAgeMonthOld = itemView.findViewById(R.id.petMonthOld);
        petMessageButton = itemView.findViewById(R.id.petMessageButton);
    }
}