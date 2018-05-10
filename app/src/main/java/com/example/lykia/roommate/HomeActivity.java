package com.example.lykia.roommate;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.lykia.roommate.DAOs.AnimalDAO;
import com.example.lykia.roommate.DAOs.RaceDAO;
import com.example.lykia.roommate.DAOs.RehomingDAO;
import com.example.lykia.roommate.DTOs.AnimalDTO;
import com.example.lykia.roommate.DTOs.RaceDTO;
import com.example.lykia.roommate.DTOs.RehomingDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView navButton;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Spinner animalSpinner;
    private Spinner raceSpinner;
    private Spinner genderSpinner;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private List<RehomingDTO> pets;
    private List<RehomingDTO> petsByGender;
    private List<AnimalDTO> animals;
    private List<RaceDTO> races;
    private MyHomeAdapter adapter;
    ArrayAdapter<CharSequence>adapterGender;

    private int userId;
    private int animalSpinnerId;
    private int raceCount = 0;
    private boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        animalSpinner= findViewById((R.id.animalSpinner));
        raceSpinner= findViewById((R.id.raceSpinner));
        genderSpinner= findViewById((R.id.genderSpinner));

        petsByGender = new ArrayList<>();
        userId = getIntent().getExtras().getInt("userId");

        toolbar = findViewById(R.id.homeAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Roommate");

        setNavigationViewListener();

        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout);
        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView= findViewById(R.id.recycler_viewHomePage);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);

        adapterGender=ArrayAdapter.createFromResource(this,R.array.cinsiyet,android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapterGender);

        navButton=(BottomNavigationView)findViewById(R.id.bottomBar);
        navButton.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home_nav:
                        Intent intent=new Intent(HomeActivity.this,HomeActivity.class);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                        return true;
                    case R.id.search_nav:
                        Intent intent1=new Intent(HomeActivity.this,SearchActivity.class);
                        intent1.putExtra("userId", userId);
                        startActivity(intent1);
                        return true;
                    case R.id.profile_nav:
                        Intent intent2=new Intent(HomeActivity.this,OwnProfileActivity.class);
                        intent2.putExtra("userId", userId);
                        startActivity(intent2);
                        return true;
                }
                return false;
            }
        });

        new GetSpinnersFromDatabase().execute();
        new Background().execute();

        animalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, final long id) {
                new GetRaceSpinnerFromDatabase().execute((int) id);
                animalSpinnerId = (int) id;

                if (check || id != 0) {
                    new GetPetsByAnimal().execute((int) id);
                }

                check = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        raceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id != 0) {
                    genderSpinner.setSelection(0);
                    new GetRaceCount().execute((int) id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id != 0) {
                    new GetPetsByGender().execute(genderSpinner.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_home);
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
            startActivity(new Intent(HomeActivity.this, AllMessageActivity.class));
        }
        return super.onOptionsItemSelected(item);

    }

    public boolean onMenuItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.rehoming_nav: {
                Intent intent=new Intent(HomeActivity.this,RehomingActivity.class);
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
                startActivity(new Intent(HomeActivity.this, RehomingActivity.class).putExtra("userId", userId));
                break;
            case R.id.article_nav:
                startActivity(new Intent(HomeActivity.this,BriefArticleActivity.class));
                break;
            case R.id.edit_profile_nav:
                startActivity(new Intent(HomeActivity.this, EditProfileActivity.class).putExtra("userId", userId));
                break;
            case R.id.logout_nav:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                break;

        }
        return false;
    }

    public class MyHomeAdapter extends RecyclerView.Adapter<MyHomeHolder> {

        List<RehomingDTO> pets;

        public MyHomeAdapter(List<RehomingDTO> pets) {
            this.pets = pets;
        }

        @Override
        public MyHomeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_animal, parent, false);
            MyHomeHolder holder = new MyHomeHolder(v);

            return holder;
        }

        @Override
        public void onBindViewHolder( MyHomeHolder holder,final int position) {

            Picasso.get().load(pets.get(position).getImagePath()).placeholder(R.drawable.default_rehoming_icon).into(holder.animalImage);
            holder.textViewAnimal.setText(pets.get(position).getRace().getAnimal().getAnimalName());
            holder.textViewRace.setText(pets.get(position).getRace().getRaceName());
            holder.textViewMonth.setText(setProperAge(pets.get(position).getMonthOld()));
            holder.textViewGender.setText(pets.get(position).getGender());
            holder.textViewRefCode.setText(pets.get(position).getCode());

            holder.messageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomeActivity.this, MessageActivity.class)
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

            adapter = new MyHomeAdapter(pets);
            recyclerView.setAdapter(adapter);
        }
    }

    public class GetRaceCount extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected Integer doInBackground(Integer... ints) {
            for (int i = 1; i < animalSpinnerId; i++) {
                raceCount += RaceDAO.getRaceCount(i);
            }

            return ints[0];
        }

        @Override
        protected void onPostExecute(Integer ints) {
            super.onPostExecute(ints);

            new GetPetsByRace().execute((int) raceCount + ints);
        }
    }



    public class GetPetsByAnimal extends AsyncTask<Integer, Void, Void>
    {
        @Override
        protected Void doInBackground(Integer... ints) {
            if (ints[0] == 0) {
                pets = RehomingDAO.getAllRehomingPets();
            } else {
                pets = RehomingDAO.getRehomingPetsByAnimalId(ints[0]);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            adapter = new MyHomeAdapter(pets);
            recyclerView.setAdapter(adapter);
        }
    }

    public class GetPetsByRace extends AsyncTask<Integer, Void, Void>
    {
        @Override
        protected Void doInBackground(Integer... ints) {
            pets = RehomingDAO.getRehomingPetsByRaceId(ints[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            adapter = new MyHomeAdapter(pets);
            recyclerView.setAdapter(adapter);
        }
    }

    public class GetPetsByGender extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            petsByGender.clear();

            for (int i = 0; i < pets.size(); i++) {
                if (pets.get(i).getGender().equals(strings[0])) {
                    petsByGender.add(pets.get(i));
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            adapter = new MyHomeAdapter(petsByGender);
            recyclerView.setAdapter(adapter);
        }
    }

    public class GetSpinnersFromDatabase extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            animals = AnimalDAO.getAllAnimals();
            races = RaceDAO.getAllRaces();

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            populateAnimalSpinner();
            populateRaceSpinner(races);
        }
    }

    public class GetRaceSpinnerFromDatabase extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... ints) {
            races = RaceDAO.getRacesByAnimalId(ints[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);


            populateRaceSpinner(races);
        }
    }

    private void populateAnimalSpinner() {
        List<String> list = new ArrayList<>();

        list.add("Hayvan");
        for (int i = 0; i < animals.size(); i++) {
            list.add(animals.get(i).getAnimalName());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        animalSpinner.setAdapter(spinnerAdapter);
    }

    private void populateRaceSpinner(List<RaceDTO> races) {
        List<String> list = new ArrayList<>();

        list.add("Cins");
        for (int i = 0; i < races.size(); i++) {
            list.add(races.get(i).getRaceName());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        raceSpinner.setAdapter(spinnerAdapter);
    }

}

class MyHomeHolder extends RecyclerView.ViewHolder {

    CircleImageView animalImage;
    TextView textViewAnimal;
    TextView textViewRace;
    TextView textViewGender;
    TextView textViewMonth;
    TextView textViewRefCode;
    Button messageButton;


    public MyHomeHolder(View itemView) {
        super(itemView);
        animalImage = itemView.findViewById(R.id.animalImage);
        textViewAnimal = itemView.findViewById(R.id.textAnimal);
        textViewRace = itemView.findViewById(R.id.textRace);
        textViewGender = itemView.findViewById(R.id.textGender);
        textViewMonth = itemView.findViewById(R.id.textMonth);
        textViewRefCode = itemView.findViewById(R.id.textRefCode);
        messageButton = itemView.findViewById(R.id.messageButton);

    }
}