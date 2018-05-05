package com.example.lykia.roommate;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {
        auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser().getUid();
        Messages message = messageList.get(i);
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

        viewHolder.messageText.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}