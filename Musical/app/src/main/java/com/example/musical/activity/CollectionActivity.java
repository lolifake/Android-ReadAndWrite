package com.example.musical.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.PerformanceHintManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import com.example.musical.enity.Article;
import com.example.musical.R;
import com.example.musical.adapter.ArticleAdapter;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity {

    private ArticleAdapter adapter;

    private List<Article> articleList = new ArrayList<>();//存放当前用户收藏的文章
    private List<Article> dataList = new ArrayList<>();//存放全部数据
    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        /*得到登录用户*/
        user = getIntent().getStringExtra("user");
        Log.d("Test", "collect_user:" + user);
        initArticle();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new ArticleAdapter(articleList);
        recyclerView.setAdapter(adapter);
    }

    /*初始化文章数据*/
    private void initArticle() {
        if (user == null) {
            user = PreferenceManager.getDefaultSharedPreferences(this).getString("username", "");
            Log.d("Test", "collect_name:" + user);
        }
        /*从数据库中取出数据*/
        dataList = LitePal.findAll(Article.class);
        for(Article article:dataList)
        {
            /*得到当前用户收藏的数据*/
            if(article.getCollect_user()==user){
                articleList.add(article);
                Log.d("Test","author"+article.getTxt_author());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}