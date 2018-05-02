package com.example.lykia.roommate;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyHolder extends RecyclerView.ViewHolder {

    CircleImageView image;
    TextView name;

    public MyHolder(View itemView) {
        super(itemView);

        image = (CircleImageView) itemView.findViewById(R.id.userSingleImage);
        name = (TextView) itemView.findViewById(R.id.userSingleName);
    }
}