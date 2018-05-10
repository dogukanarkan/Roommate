package com.example.lykia.roommate;

import android.app.Dialog;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lykia.roommate.DAOs.AdoptedDAO;
import com.example.lykia.roommate.DAOs.UserDAO;
import com.example.lykia.roommate.DTOs.AdoptedDTO;
import com.example.lykia.roommate.DTOs.UserDTO;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdoptedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<AdoptedDTO> adoptedList;
    private MyAdoptedAdapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopted);

        toolbar = (Toolbar) findViewById(R.id.adoptedAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sahiplendirilen Hayvanlar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view2);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);

        new Test().execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return false;
    }
    public class MyAdoptedAdapter extends RecyclerView.Adapter<MyAdoptedHolder> {

        List<AdoptedDTO> adopted;

        public MyAdoptedAdapter(List<AdoptedDTO> adopted) {
            this.adopted = adopted;
        }

        @Override
        public MyAdoptedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_card_view, parent, false);
            MyAdoptedHolder holder = new MyAdoptedHolder(v);

            return holder;
        }

        @Override
        public void onBindViewHolder(MyAdoptedHolder holder, final int position) {

            holder.toUser.setText(adopted.get(position).getToUser().getFirstName() + " " + adopted.get(position).getToUser().getLastName());
            holder.adoptedDate.setText(DateTimeConverter(adopted.get(position).getAdoptedDate()));
            Picasso.get().load(adopted.get(position).getImagePath()).placeholder(R.drawable.default_rehoming_icon).into(holder.animalImage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(AdoptedActivity.this);
                    dialog.setContentView(R.layout.custom);
                    dialog.setTitle(" ");

                    TextView animalText = (TextView) dialog.findViewById(R.id.animalName);
                    animalText.setText(adopted.get(position).getRace().getAnimal().getAnimalName());
                    TextView raceText = (TextView) dialog.findViewById(R.id.raceText);
                    raceText.setText(adopted.get(position).getRace().getRaceName());
                    TextView monthText = (TextView) dialog.findViewById(R.id.month);
                    monthText.setText(Integer.toString(adopted.get(position).getMonthOld()));
                    TextView genderText = (TextView) dialog.findViewById(R.id.gender);
                    genderText.setText(adopted.get(position).getGender());
                    TextView information = (TextView) dialog.findViewById(R.id.additional);
                    information.setText(adopted.get(position).getToUser().getFirstName());
                    CircleImageView image1 = (CircleImageView) dialog.findViewById(R.id.image);
                    Picasso.get().load(adopted.get(position).getImagePath()).into(image1);
                    Button angryButton = (Button) dialog.findViewById(R.id.angry_btn);
                    angryButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            dialog.dismiss();

                        }
                    });

                    dialog.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return adopted.size();
        }
    }

    private String DateTimeConverter (Timestamp timestamp) {
        java.text.SimpleDateFormat sample = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");

        return sample.format(timestamp);
    }

    public class Test extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            adoptedList = AdoptedDAO.getAllAdoptedPets();

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            adapter = new MyAdoptedAdapter(adoptedList);
            recyclerView.setAdapter(adapter);

        }
    }

    class MyAdoptedHolder extends RecyclerView.ViewHolder {

        ImageView animalImage;
        TextView toUser;
        TextView adoptedDate;

        public MyAdoptedHolder(View itemView) {
            super(itemView);

            animalImage = (ImageView)itemView.findViewById(R.id.animalPhoto);
            toUser = (TextView) itemView.findViewById(R.id.toUser);
            adoptedDate=(TextView)itemView.findViewById(R.id.adoptedDate);

        }
    }
}