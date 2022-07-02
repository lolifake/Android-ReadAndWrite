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
import com.example.write.bean.BbArticle;
import com.example.write.bean.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class CollectionActivity extends AppCompatActivity {

    private String objectId;
    private BmobUser user;
    private BmobArticleAdapter adapter;
    private List<BbArticle> articleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        /*设置toolbar*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*设置导航按钮*/
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        /*得到当前登录用户*/
        user = BmobUser.getCurrentUser(BmobUser.class);
        objectId = user.getObjectId();
        initArticle();
    }
    /*初始化文章数据*/
    private void initArticle() {
        /*被收藏的文章数，因此查询的是文章表*/
        BmobQuery<BbArticle> articleBmobQuery = new BmobQuery<>();
        User bmobUser = new User();
        bmobUser.setObjectId(objectId);
        /*collectArticle是User表字段，用来存储该用户收藏的所有文章*/
        articleBmobQuery.addWhereRelatedTo("collectArticle",new BmobPointer(bmobUser));
        articleBmobQuery.findObjects(new FindListener<BbArticle>() {
            @Override
            public void done(List<BbArticle> list, BmobException e) {
                if(e == null)
                {
                    articleList = list;
                    /*将recycleView显示出来*/
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(staggeredGridLayoutManager);
                    adapter = new BmobArticleAdapter(articleList);
                    recyclerView.setAdapter(adapter);
                    Log.d("Test","收藏个数:"+list.size());
                }
                else{
                    Log.d("Tset","查询失败:"+e.getMessage());
                }
            }
        });
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