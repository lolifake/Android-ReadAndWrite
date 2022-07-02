package com.example.write.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.write.MainActivity;
import com.example.write.R;
import com.example.write.bean.BbArticle;
import com.example.write.bean.User;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class ArticleActivity extends AppCompatActivity {

    private List<User> userList = new ArrayList<>(); //收藏该帖子的用户
    private boolean flag = false;
    private FloatingActionButton fab;
    private String UserObjectId;//当前用户的ID
    private String activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        //得到数据
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String objectId= intent.getStringExtra("id");//当前文章的ID
        activity = intent.getStringExtra("activity");
        //设置toolbar和 CollapsingToolbarLayout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        TextView text_content = (TextView) findViewById(R.id.text_content);
        ImageView default_image = (ImageView) findViewById(R.id.text_background);
        setSupportActionBar(toolbar);
        /*设置文章标题名*/
        collapsingToolbarLayout.setTitle(title);
        text_content.setText(content);
        /*得到当前用户*/
        if(BmobUser.isLogin())
        {
            User currentUser = BmobUser.getCurrentUser(User.class);
            UserObjectId = currentUser.getObjectId();
        }
        /*设置导航栏*/
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        /*编辑收藏按钮*/
        fab = (FloatingActionButton) findViewById(R.id.collect);
        findCollectArticle(objectId);
        //Log.d("ArticleActivity","userLiset:"+userList.size());
    }


    private void findCollectArticle(String objectId)
    {
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        BbArticle article = new BbArticle();
        article.setObjectId(objectId);
        bmobQuery.addWhereRelatedTo("collects",new BmobPointer(article));
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e == null)
                {
                    userList = list;
                    User user = new User();
                    user.setObjectId(UserObjectId);
//                    Log.d("ArticleActivity", "flag:"+flag);
                    for(int i = 0;i < userList.size();i++)
                    {
//                        Log.d("ArticleActivity", "username"+userList.get(i).getUsername());
                        if(userList.get(i).getObjectId().equals(user.getObjectId()))
                        {
                            flag = true;
                            Log.d("ArticleActivity", "flag:"+flag);
                        }
                    }
                    if(flag)
                    {
                        fab.setImageResource(R.drawable.scucess_collect);
                        /*取消收藏*/
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deleteArticle(objectId);
                                deleteUser(objectId);
                                fab.setImageResource(R.drawable.nav_collection);
                                Toast.makeText(ArticleActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{/*收藏功能*/
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(BmobUser.isLogin()){
                                    updateArticle(objectId);
                                    updateUser(objectId);
                                    fab.setImageResource(R.drawable.scucess_collect);
                                }
                                else{
                                    Toast.makeText(ArticleActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    Log.d("ArticleActivity","查询收藏用户成功"+userList.size());
                }else{
                    Log.d("ArticleActivity","查询收藏用户失败"+e.getMessage());
                }
            }
        });
    }
    /*更新数据，即删除之前多对多关联，即取消对该帖子的收藏操作*/
    /*对BbArticle表进行操作*/
    private void deleteArticle(String objectId)
    {
        BbArticle article = new BbArticle();
        article.setObjectId(objectId);
        User user = BmobUser.getCurrentUser(User.class);
        BmobRelation relation = new BmobRelation();
        relation.remove(user);
        article.setCollects(relation);
        article.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Log.d("ArticleActivity","BbArticle关联关系删除成功");
                }
                else{
                    Log.d("ArticleActivity","BbArticle关联关系删除失败"+e.getMessage());
                }
            }
        });
    }

    /*对User表进行操作*/
    private void deleteUser(String objectId)
    {
        User user = new User();
        user.setObjectId(UserObjectId);
        BbArticle article = new BbArticle();
        article.setObjectId(objectId);

        BmobRelation relation = new BmobRelation();
        relation.remove(article);
        user.setCollectArticle(relation);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null)
                {
                    Log.d("ArticleActivity","User关联关系删除成功");
                }
                else{
                    Log.d("ArticleActivity","User关联关系删除失败"+e.getMessage());
                }
            }
        });
    }

    /*更新User表中的数剧*/
    private void updateUser(String objectId)
    {
        User user = BmobUser.getCurrentUser(User.class);
        BbArticle article = new BbArticle();
        article.setObjectId(objectId);
        /*将当前被收藏的帖子插入用户的collectArticle字段中,表明用户收藏了该帖子*/
        BmobRelation relation = new BmobRelation();
        /*将当前文章添加到多对多关联中*/
        relation.add(article);
        user.setCollectArticle(relation);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Log.d("Test","CollectArticle 插入成功");
                }
                else{
                    Log.d("Test","CollectArticle 插入失败："+e.getMessage());
                }
            }
        });
    }

    /*更新BbArticle表中的数据*/
    private void updateArticle(String objectId){
        /*更新Article表中的有关数据*/
        User user = BmobUser.getCurrentUser(User.class);
        BbArticle article = new BbArticle();
        article.setObjectId(objectId);
        /*将当前用户添加到BbArticle表中的collect字段值中，表明该帖子被当前用户收藏*/
        BmobRelation relation = new BmobRelation();
        /*将当前用户添加到多对多关联中*/
        relation.add(user);
        article.setCollects(relation);
        article.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Toast.makeText(ArticleActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("Tset","Collect Fail:"+e.getMessage());
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(ArticleActivity.this,MainActivity.class);
//                if(activity.indexOf("MainActivity") != -1)
//                {
//                    intent = new Intent(ArticleActivity.this, MainActivity.class);
//                }
//                else if(activity.indexOf("CollectionActivity") != -1)
//                {
//                    intent = new Intent(ArticleActivity.this,CollectionActivity.class);
//                }
//                else if(activity.indexOf("MyArticleActivity") != -1)
//                {
//                    intent = new Intent(ArticleActivity.this,MyArticleActivity.class);
//                }
//                Log.d("Test","Activity : "+activity);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }
}
