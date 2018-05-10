package com.example.lykia.roommate;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lykia.roommate.DAOs.RehomingDAO;
import com.example.lykia.roommate.DTOs.AnimalDTO;
import com.example.lykia.roommate.DTOs.RaceDTO;
import com.example.lykia.roommate.DTOs.RehomingDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> messageList;
    private FirebaseAuth auth;
    private RehomingDTO rehoming;
    private String code = "";

    public MessageAdapter(List<Messages> messageList) {
        this.messageList = messageList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout ,parent, false);

        return new MessageViewHolder(v);

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView profileImage;
        public TextView displayName;
        public TextView messageText;


        public MessageViewHolder(View view) {
            super(view);

            profileImage = (CircleImageView) view.findViewById(R.id.messageImageLayout);
            displayName = (TextView) view.findViewById(R.id.messageDisplayName);
            messageText = (TextView) view.findViewById(R.id.messageTextLayout);
        }
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, final int i) {

        auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser().getUid();
        final Messages message = messageList.get(i);
        String fromUser = message.getFrom();

        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUser);

        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("thumbImage").getValue().toString();

                viewHolder.displayName.setText(name);
                Picasso.get().load(image).placeholder(R.drawable.person).into(viewHolder.profileImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        String first = "", colorful = "", last = "";
        boolean check = true;

        for (int k = 0; k < message.getMessage().length(); k++) {
            if (message.getMessage().charAt(k) == '#') {
                code = "";
                for (int a = k; a < k + 9; a++) {
                    code += message.getMessage().charAt(a);
                }

                k += 8;

                check = false;
                colorful = "<font color='#0000FF'>" + code + "</font>";
            } else if (check) {
                first += message.getMessage().charAt(k);
            } else {
                last += message.getMessage().charAt(k);
            }
        }

        viewHolder.messageText.setText(Html.fromHtml(first + colorful + last));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int k = 0; k < message.getMessage().length(); k++) {
                    if (message.getMessage().charAt(k) == '#') {
                        code = "";
                        for (int i = k + 1; i < k + 9; i++) {
                            code += message.getMessage().charAt(i);
                        }

                        new Background().execute(view.getContext());
                    }
                }
            }
        });
    }

    public class Background extends AsyncTask<Context, Void, Context> {
        @Override
        protected Context doInBackground(Context... contexts) {
            rehoming = RehomingDAO.getRehomingPetByCode(code);

            return contexts[0];
        }

        @Override
        protected void onPostExecute(Context context) {
            super.onPostExecute(context);

            if (rehoming == null) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom2);
                dialog.setTitle("Bu hayvan sistemde yoktur.");

                dialog.show();
            } else {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom);
                dialog.setTitle(code);

                TextView animalText = (TextView) dialog.findViewById(R.id.animalName);
                animalText.setText(rehoming.getRace().getAnimal().getAnimalName());
                TextView raceText = (TextView) dialog.findViewById(R.id.raceText);
                raceText.setText(rehoming.getRace().getRaceName());
                TextView monthText = (TextView) dialog.findViewById(R.id.month);
                monthText.setText(Integer.toString(rehoming.getMonthOld()));
                TextView genderText = (TextView) dialog.findViewById(R.id.gender);
                genderText.setText(rehoming.getGender());
                TextView information = (TextView) dialog.findViewById(R.id.additional);
                information.setText(rehoming.getInformation());
                CircleImageView image1 = (CircleImageView) dialog.findViewById(R.id.image);
                Picasso.get().load(rehoming.getImagePath()).into(image1);
                Button angryButton = (Button) dialog.findViewById(R.id.angry_btn);
                angryButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });

                dialog.show();
            }
        }
    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
