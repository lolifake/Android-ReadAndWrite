package com.example.musical.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsets;

import com.example.musical.MainActivity;
import com.example.musical.enity.Article;
import com.example.musical.R;
import com.example.musical.adapter.MyArticleAdapter;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class MyArticleActivity extends AppCompatActivity {

    private MyArticleAdapter adapter;
    private String content;
    private List<Article> articleList = new ArrayList<>();//数据库全部数据存放
    private List<Article> my_articleList = new ArrayList<>();//存放当前登录用户所写的文章
    private String name;//用户名
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_article);
        /*显示toolbar*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*ActionBar显示出来*/
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        name = getIntent().getStringExtra("txt_author");
        Log.d("Test","myName"+name);
        initList();
        /*显示RecycleView*/
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyArticleAdapter(my_articleList);
        recyclerView.setAdapter(adapter);

    }
    /*向articleList中插入数据*/
    private void initList() {
        /*将当前用户所写的文章全部显示出来*/
        articleList = LitePal.findAll(Article.class);
        for(Article article:articleList)
        {
            if(article.getTxt_author().equals(name)){
                my_articleList.add(article);
                Log.d("Test","author"+article.getTxt_author());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                /*删除完毕后返回主页面*/
                String name = getIntent().getStringExtra("txt_author");
                MainActivity.actionStart(MyArticleActivity.this,name,1);
                finish();
                break;
        }
        return true;
    }

   /*设置传递数据*/
    public static void onAction(Context context, String name){
        Intent intent = new Intent(context,MyArticleActivity.class);
        intent.putExtra("txt_author",name);
        context.startActivity(intent);
    }
}