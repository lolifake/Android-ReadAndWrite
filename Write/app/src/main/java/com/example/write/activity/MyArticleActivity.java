package com.example.write.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.write.MainActivity;
import com.example.write.R;
import com.example.write.adapter.BmobArticleAdapter;
import com.example.write.adapter.MyArticleAdapter;
import com.example.write.bean.BbArticle;
import com.example.write.bean.User;
import com.example.write.until.LitePalArticle;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class MyArticleActivity extends AppCompatActivity {

    private MyArticleAdapter adapter;
    private BmobArticleAdapter bmobArticleAdapter;
    private String content;
    private List<LitePalArticle> articleList = new ArrayList<>();//数据库全部数据存放
    private List<LitePalArticle> my_articleList = new ArrayList<>();//存放当前登录用户所写的文章
    private List<BbArticle> bbArticleList = new ArrayList<>();
    private String name;//用户名
    private User user;
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
        /*得到当前登录用户名*/
        user = BmobUser.getCurrentUser(User.class);
        name = user.getUsername();
        /*初始化当前数据*/
        initList();
        initBmobArticle();
/*        initList();*/

    }
    /*从云数据库中得到数据*/
    private void initBmobArticle(){
        BmobQuery<BbArticle> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("author",name);
        bmobQuery.findObjects(new FindListener<BbArticle>() {
            @Override
            public void done(List<BbArticle> list, BmobException e) {
                if(e == null)
                {
                    bbArticleList = list;
                    /*显示RecycleView*/
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                   GridLayoutManager layoutManager = new GridLayoutManager(MyArticleActivity.this,1);
                    recyclerView.setLayoutManager(layoutManager);
                    adapter = new MyArticleAdapter(articleList,bbArticleList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    Log.d("MyArticleActivity","MyArticle 查询成功"+bbArticleList.size());
                }
                else{
                    Log.d("MyArticleActivity","查询失败"+e.getMessage());
                }
            }
        });
    }

    /*向articleList中插入数据*/
    private void initList() {
        /*将当前用户所写的文章全部显示出来,从本地数据库中得到数据*/
        articleList = LitePal.findAll(LitePalArticle.class);
        for(LitePalArticle article:articleList)
        {
            if(article.getAuthor().equals(name)){
                my_articleList.add(article);
                Log.d("Test","author"+article.getAuthor());
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                /*删除完毕后返回主页面*/
                Intent intent = new Intent(MyArticleActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }
}