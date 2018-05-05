package com.example.lykia.roommate;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hilal on 1.05.2018.
 */

public class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView animal_genus;
        public ImageView animal_img;
        public CardView card_view;


        public ViewHolder(View view) {
            super(view);

            card_view = (CardView) view.findViewById(R.id.card_view);
            animal_genus = (TextView) view.findViewById(R.id.animal_genus);
            animal_img = (ImageView) view.findViewById(R.id.photo);

        }
    }

    List<AnimalAttribute> list_animal;
    CustomItemClickListener listener;

    public SimpleRecyclerAdapter(List<AnimalAttribute> list_animal, CustomItemClickListener listener) {

        this.list_animal = list_animal;
        this.listener = listener;
    }


    @Override
    public SimpleRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout, parent, false);
        final ViewHolder view_holder = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, view_holder.getPosition());
            }
        });

        return view_holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.animal_genus.setText(list_animal.get(position).getGenus());

        holder.animal_img.setImageResource(list_animal.get(position).getPhoto_id());

    }

    @Override
    public int getItemCount() {
        return list_animal.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
