package com.example.lykia.roommate;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lykia.roommate.DAOs.AdoptedDAO;
import com.example.lykia.roommate.DAOs.RaceDAO;
import com.example.lykia.roommate.DAOs.RehomingDAO;
import com.example.lykia.roommate.DAOs.UserDAO;
import com.example.lykia.roommate.DTOs.AdoptedDTO;
import com.example.lykia.roommate.DTOs.RaceDTO;
import com.example.lykia.roommate.DTOs.RehomingDTO;
import com.example.lykia.roommate.DTOs.UserDTO;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OwnProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView navButton;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private RecyclerView recyclerView;
    private OwnProfileActivity.MyOwnAdapter adapter;
    private List<RehomingDTO> pets;
    private int userId;
    private EditText toUser;
    private Button dialogBtn;
    private String toUserMail;
    private int petId;
    private String text;
    private Button deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_profile);

        navButton = (BottomNavigationView) findViewById(R.id.bottomBar);
        navButton.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_nav:
                        Intent intent = new Intent(OwnProfileActivity.this, HomeActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.search_nav:
                        Intent intent1 = new Intent(OwnProfileActivity.this, SearchActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.profile_nav:
                        Intent intent2 = new Intent(OwnProfileActivity.this, OwnProfileActivity.class);
                        startActivity(intent2);
                        return true;
                }
                return false;
            }
        });

        toolbar = (Toolbar) findViewById(R.id.ownProfileAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        userId = getIntent().getExtras().getInt("userId");

        recyclerView = findViewById(R.id.recycler_view_OwnProfile);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        new OwnProfileActivity.Background().execute();

        setNavigationViewListener();


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        deleteBtn=(Button)findViewById(R.id.deleteBtn);

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
                Intent intent = new Intent(OwnProfileActivity.this, RehomingActivity.class);
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
                startActivity(new Intent(OwnProfileActivity.this, BriefArticleActivity.class));
                break;
            case R.id.edit_profile_nav:
                startActivity(new Intent(OwnProfileActivity.this, EditProfileActivity.class).putExtra("userId", userId));
                break;
            case R.id.logout_nav:
                startActivity(new Intent(OwnProfileActivity.this, LoginActivity.class));
                break;

        }
        return false;
    }

    public class MyOwnAdapter extends RecyclerView.Adapter<MyOwnHolder> {

        List<RehomingDTO> pets;

        public MyOwnAdapter(List<RehomingDTO> pets) {
            this.pets = pets;
        }

        @Override
        public MyOwnHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pets_own_single_layout, parent, false);
            MyOwnHolder holder = new MyOwnHolder(v);

            return holder;
        }

        @Override
        public void onBindViewHolder(MyOwnHolder holder, final int position) {
            holder.textViewRace.setText(pets.get(position).getRace().getRaceName());
            holder.textViewMonth.setText(Integer.toString(pets.get(position).getMonthOld()));
            holder.textViewGender.setText(pets.get(position).getGender());
            Picasso.get().load(pets.get(position).getImagePath()).placeholder(R.drawable.default_rehoming_icon).into(holder.image);

            petId = pets.get(position).getPetId();

            holder.rehomedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(OwnProfileActivity.this);
                    dialog.setContentView(R.layout.adopted_layout);
                    dialog.setTitle("Sahiplendir");

                    toUser = (EditText) dialog.findViewById(R.id.toUser);
                    dialogBtn = (Button) dialog.findViewById(R.id.dialogBtn);

                    dialog.show();

                    dialogBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toUserMail = toUser.getText().toString();

                            new AdoptedPet().execute();

                            dialog.dismiss();
                            text = "Hayvanınız Sahiplendirildi";
                            showToast(text);
                        }
                    });


                }

            });

            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AdoptedPetDelete().execute();
                    text = "Hayvanınız Sistemden Kaldırıldı";
                    showToast(text);

                }
            });

        }

        @Override
        public int getItemCount() {
            return pets.size();
        }
    }

    private void showToast(String text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);

        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setGravity(Gravity.CENTER);

        toast.show();
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

            adapter = new OwnProfileActivity.MyOwnAdapter(pets);
            recyclerView.setAdapter(adapter);
        }
    }

    public class AdoptedPet extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            AdoptedDTO pet = new AdoptedDTO();

            UserDTO fromUser = UserDAO.getUserById(userId);
            UserDTO toUser = UserDAO.getUserByMail(toUserMail);
            RehomingDTO rehomingPet = RehomingDAO.getRehomingPetById(petId);
            RaceDTO race = RaceDAO.getRaceById(rehomingPet.getRace().getRaceId());

            pet.setFromUser(fromUser);
            pet.setToUser(toUser);
            pet.setRace(race);
            pet.setImagePath(rehomingPet.getImagePath());
            pet.setGender(rehomingPet.getGender());
            pet.setMonthOld(rehomingPet.getMonthOld());

            AdoptedDAO.insertAdoptedPet(pet);

            RehomingDAO.deleteRehomingPetById(petId);
            startActivity(new Intent(OwnProfileActivity.this, OwnProfileActivity.class).putExtra("userId", userId));

            return null;
        }


        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);
        }
    }

    public class AdoptedPetDelete extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            RehomingDAO.deleteRehomingPetById(petId);

            startActivity(new Intent(OwnProfileActivity.this, OwnProfileActivity.class).putExtra("userId", userId));

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);
        }

    }


    class MyOwnHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView textViewRace;
        TextView textViewGender;
        TextView textViewMonth;
        Button rehomedBtn;
        Button deleteBtn;


        public MyOwnHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            textViewRace = itemView.findViewById(R.id.textViewRace);
            textViewGender = itemView.findViewById(R.id.textViewGender);
            textViewMonth = itemView.findViewById(R.id.textViewAge);
            rehomedBtn = itemView.findViewById(R.id.rehomedBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);

        }
    }
}