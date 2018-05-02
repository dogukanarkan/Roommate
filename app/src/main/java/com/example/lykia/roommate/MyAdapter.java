package com.example.lykia.roommate;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lykia.roommate.DTOs.UserDTO;
import com.squareup.picasso.Picasso;

import java.util.List;

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

        holder.name.setText(fullName);
        Picasso.get().load(users.get(position).getImagePath()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
