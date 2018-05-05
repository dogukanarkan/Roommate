package com.example.lykia.roommate;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lykia.roommate.DAOs.ArticleDAO;
import com.example.lykia.roommate.DTOs.ArticleDTO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailArticleActivity extends AppCompatActivity {


    private ArticleDTO article;
    private Toolbar toolbar;
    private ImageView image;
    private TextView name;
    private TextView detailArticle;
    private TextView dateText;
    private int articleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article);

        toolbar = (Toolbar) findViewById(R.id.detailArticleAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        articleId = getIntent().getExtras().getInt("articleId");


        image = (ImageView)findViewById(R.id.photo);
        name = (TextView)findViewById(R.id.title);
        detailArticle=(TextView)findViewById(R.id.detailText);
        dateText=(TextView)findViewById(R.id.dateText);

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
    public class Background extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            article = ArticleDAO.getArticleById(articleId);

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            Picasso.get().load(article.getImagePath()).into(image);
            name.setText(article.getHeader());
            detailArticle.setText(article.getText());
            dateText.setText(article.getAdditionDate().toString());
        }
    }
}
