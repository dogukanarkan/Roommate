package com.example.lykia.roommate;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lykia.roommate.DAOs.AnimalDAO;
import com.example.lykia.roommate.DAOs.ArticleDAO;
import com.example.lykia.roommate.DAOs.UserDAO;
import com.example.lykia.roommate.DTOs.AnimalDTO;
import com.example.lykia.roommate.DTOs.ArticleDTO;
import com.example.lykia.roommate.DTOs.UserDTO;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BriefArticleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ArticleDTO> articleList;
    private BriefArticleActivity.MyAdapter adapter;
    private boolean check = true;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brief_article);

        toolbar=(Toolbar)findViewById(R.id.briefArticleAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bilgilendirme Yazıları");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        recyclerView = (RecyclerView) findViewById(R.id.searchRecyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);

        new Background().execute();

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
    public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

        List<ArticleDTO> article;

        public MyAdapter(List<ArticleDTO> article) {
            this.article = article;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout, parent, false);
            MyHolder holder = new MyHolder(v);

            return holder;
        }

        @Override
        public void onBindViewHolder(MyHolder holder, final int position) {
            holder.name.setText(article.get(position).getHeader());
            Picasso.get().load(article.get(position).getImagePath()).into(holder.image);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(BriefArticleActivity.this, DetailArticleActivity.class).putExtra("articleId", article.get(position).getArticleId()));
                }
            });
        }



        @Override
        public int getItemCount() {
            return article.size();
        }
    }
    public class Background extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            articleList = ArticleDAO.getAllArticles();

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            adapter = new MyAdapter(articleList);
            recyclerView.setAdapter(adapter);
            check = false;


        }
    }
    class MyHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;

        public MyHolder(View itemView) {
            super(itemView);

            image = (ImageView)itemView.findViewById(R.id.photo);
            name = (TextView) itemView.findViewById(R.id.animal_genus);
        }
    }
}
