package com.example.musical.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.musical.MainActivity;
import com.example.musical.R;
import com.example.musical.enity.Article;

public class EditArticleActivity extends AppCompatActivity {

    private EditText edit_title,edit_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);

        /*设置toolbar*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*设置导航按钮*/
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);//设置home按钮图片
        }

        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_content = (EditText) findViewById(R.id.edit_content);

    }
    /*显示menu按钮*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }
    /*设置按钮功能*/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.send:
                /*得到当前用户的用户名，即文章的作者*/
                String author = getIntent().getStringExtra("author");
                /*从编辑框中得到文章标题和文章内容*/
                String title = edit_title.getText().toString();
                String content = edit_content.getText().toString();
                if(title.equals("")){/*设置标题不能为空*/
                    Toast.makeText(this, "标题不能为空", Toast.LENGTH_SHORT).show();
                }
                else{
                    /*数据插入Article表中*/
                    Article article = new Article();
                    article.setTxt_content(content);
                    article.setTxt_title(title);
                    article.setTxt_author(author);
                    article.save();
                    /*将登录用户名和登录状态码传递给MainActivity*/
                    MainActivity.actionStart(EditArticleActivity.this,author,1);
                }
                break;
            default:
        }
        return true;
    }
}