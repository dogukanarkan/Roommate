package com.example.lykia.roommate;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String messageUserId;
    private String currentUserId;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    private CircleImageView image;
    private TextView name;
    private ImageButton sendButton;
    private EditText sendText;
    private RecyclerView messageList;
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter adapter;

    private final List<Messages> messagesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        toolbar = (Toolbar) findViewById(R.id.messageAppBar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.chat_bar_layout, null);

        actionBar.setCustomView(actionBarView);

        image = (CircleImageView) findViewById(R.id.chatBarImage);
        name = (TextView) findViewById(R.id.chatBarName);
        sendButton = (ImageButton) findViewById(R.id.messageSendButton);
        sendText = (EditText) findViewById(R.id.messageSendText);
        messageList = (RecyclerView) findViewById(R.id.messageRecycler);
        linearLayoutManager = new LinearLayoutManager(this);
        adapter = new MessageAdapter(messagesList);

        messageList.setHasFixedSize(true);
        messageList.setLayoutManager(linearLayoutManager);
        messageList.setAdapter(adapter);

        messageUserId = getIntent().getStringExtra("messageUserId");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        String messageUserMail = getIntent().getStringExtra("messageUserMail");

        if (messageUserMail != null) {
            databaseReference.child("Mails").child(toMD5(messageUserMail)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    messageUserId = dataSnapshot.child("userId").getValue().toString();

                    task();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            task();
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void task() {
        loadMessage();

        databaseReference.child("Users").child(messageUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String chatImage = dataSnapshot.child("thumbImage").getValue().toString();
                String chatUsername = dataSnapshot.child("name").getValue().toString();

                name.setText(chatUsername);
                Picasso.get().load(chatImage).placeholder(R.drawable.person).into(image);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadMessage() {
        databaseReference.child("messages").child(currentUserId).child(messageUserId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages message = dataSnapshot.getValue(Messages.class);

                messagesList.add(message);
                adapter.notifyDataSetChanged();
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
    }

    private void sendMessage() {
        String message = sendText.getText().toString();

        if (!TextUtils.isEmpty(message)) {
            String currentUserRef = "messages/" + currentUserId + "/" + messageUserId;
            String messageUserRef = "messages/" + messageUserId + "/" + currentUserId;

            DatabaseReference userMessagePush = databaseReference.child("messages").child(currentUserId).child(messageUserId).push();

            String pushId = userMessagePush.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message", message);
            messageMap.put("time", ServerValue.TIMESTAMP);
            messageMap.put("from", currentUserId);

            Map messageUserMap = new HashMap();
            messageUserMap.put(currentUserRef + "/" + pushId, messageMap);
            messageUserMap.put(messageUserRef + "/" + pushId, messageMap);

            sendText.setText("");

            databaseReference.child("Chat").child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(messageUserId)) {
                        Map chatAddMap = new HashMap();
                        chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                        Map chatUserMap = new HashMap();
                        chatUserMap.put("Chat/" + currentUserId + "/" + messageUserId, chatAddMap);
                        chatUserMap.put("Chat/" + messageUserId + "/" + currentUserId, chatAddMap);

                        databaseReference.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.d("CHAT_LOG", databaseError.getMessage().toString());
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            databaseReference.child("Chat").child(currentUserId).child(messageUserId).child("timestamp").setValue(ServerValue.TIMESTAMP);
            databaseReference.child("Chat").child(messageUserId).child(currentUserId).child("timestamp").setValue(ServerValue.TIMESTAMP);

            databaseReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Log.d("CHAT_LOG", databaseError.getMessage().toString());
                    }
                }
            });
        }
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

    private String toMD5(String input) {
        String hashPassword = null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes(), 0, input.length());

            hashPassword = new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hashPassword;
    }
}
