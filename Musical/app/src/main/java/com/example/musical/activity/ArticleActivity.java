package com.example.musical.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.PerformanceHintManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musical.R;
import com.example.musical.enity.Article;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ArticleActivity extends AppCompatActivity {

    public static final String TXT_Title = "Title";
    public static final String TXT_Author = "Author";
    public static final String Txt_Content = "Content";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        //得到数据
        Intent intent = getIntent();
        String txt_title = intent.getStringExtra(TXT_Title);
        String txt_content = intent.getStringExtra(Txt_Content);
        String txt_author = intent.getStringExtra(TXT_Author);
        /*得到登录用户名*/
        String collect_name = PreferenceManager.getDefaultSharedPreferences(this).getString("username","");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        TextView text_content = (TextView) findViewById(R.id.text_content);
        ImageView default_image = (ImageView) findViewById(R.id.text_background);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        /*设置文章标题名*/
        collapsingToolbarLayout.setTitle(txt_title);
        text_content.setText(txt_content);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.collect);
        /*编辑按钮*/
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Test","A_Collect_name:"+collect_name);
                /*更新Article表中的有关数据*/
                Article article = new Article();
                article.setCollect_user(collect_name);
                article.updateAll("txt_title = ? and txt_author = ?",txt_title,txt_author);
                Toast.makeText(ArticleActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                Log.d("Test","Litepal_Collectname:"+article.getCollect_user());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}