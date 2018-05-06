package com.example.lykia.roommate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllMessageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView usersList;
    private DatabaseReference userDatabase;
    private DatabaseReference messageDatabase;
    private DatabaseReference chatDatabase;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_message);

        toolbar = (Toolbar) findViewById(R.id.allMessageAppBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mesajlar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        messageDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(currentUserId);
        chatDatabase = FirebaseDatabase.getInstance().getReference().child("Chat").child(currentUserId);

        usersList = (RecyclerView) findViewById(R.id.allMessagesList);
        usersList.setHasFixedSize(true);
        usersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        Query chatQuery = chatDatabase.orderByChild("timestamp");

        FirebaseRecyclerAdapter<FirebaseUsers, UsersViewHolder> adapter = new FirebaseRecyclerAdapter<FirebaseUsers, UsersViewHolder>(
                FirebaseUsers.class, R.layout.users_single_layout, UsersViewHolder.class, chatQuery) {
            @Override
            protected void populateViewHolder(final UsersViewHolder viewHolder, FirebaseUsers model, final int position) {
                final String messageUserId = getRef(position).getKey();
                Query lastMessageQuery = messageDatabase.child(messageUserId).limitToLast(1);

                lastMessageQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String lastMessage = dataSnapshot.child("message").getValue().toString();
                        viewHolder.setLastMessage(lastMessage);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(AllMessageActivity.this, MessageActivity.class).putExtra("messageUserId", messageUserId));
                    }
                });

                userDatabase.child(messageUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String image = dataSnapshot.child("thumbImage").getValue().toString();

                        viewHolder.setName(name);
                        viewHolder.setImage(image);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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

        public void setImage(String thumbImage) {
            CircleImageView imageView = (CircleImageView) view.findViewById(R.id.userSingleImage);
            Picasso.get().load(thumbImage).placeholder(R.drawable.person).into(imageView);
        }

        public void setLastMessage(String lastMessage) {
            TextView lastMessageView = (TextView) view.findViewById(R.id.userSingleVariable);
            lastMessageView.setText(lastMessage);
        }
    }
}
