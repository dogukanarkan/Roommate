package com.example.lykia.roommate;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lykia.roommate.R;

/**
 * Created by hilal on 24.04.2018.
 */

public class Person extends ArrayAdapter<String> {
    int [] imgs={};
    String [] names={};
    Context c;
    LayoutInflater inflater;
    public  Person(Context context,String[] names, int [] imgs)
    {
            super(context,R.layout.custom,names);
            this.c=context;
            this.names=names;
            this.imgs=imgs;
    }

    public  class ViewHolder
    {
        TextView nameTv;
        ImageView img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView==null)
        {
            inflater=(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.custom,null);

        }
        final  ViewHolder holder=new ViewHolder();
        holder.nameTv=(TextView)convertView.findViewById(R.id.nameTv);
        holder.img=(ImageView)convertView.findViewById(R.id.imageView1);
        holder.img.setImageResource(imgs[position]);
        holder.nameTv.setText(names[position]);


        return convertView;
    }
}
