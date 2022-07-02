package com.example.musical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musical.activity.CollectionActivity;
import com.example.musical.activity.EditArticleActivity;
import com.example.musical.activity.LoginActivity;
import com.example.musical.activity.MyArticleActivity;
import com.example.musical.activity.SettingActivity;
import com.example.musical.adapter.ArticleAdapter;
import com.example.musical.enity.Article;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import org.litepal.tablemanager.Connector;

import java.security.KeyPairGenerator;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;

    private List<Article> articleList = new ArrayList<>();
    private ArticleAdapter adapter;//适配器

    private View nav_header;
    private TextView username;//用户名
    private ImageView user_icon;//用户头像
    private FloatingActionButton fab;//编辑按钮

    private  Intent intent;

    private static final int LOGIN_CODE = 1;//判断登录状态码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //显示toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //将导航栏显示出来
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        //滑动屏幕
        navigationView =(NavigationView) findViewById(R.id.nav_view);
        nav_header=navigationView.getHeaderView(0);//获取Header
        username = nav_header.findViewById(R.id.username);
        user_icon = nav_header.findViewById(R.id.icon_image);//获取Header控件--用户头像控件
        initMenu();
        initHeader();

        initArticle();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter = new ArticleAdapter(articleList);
        recyclerView.setAdapter(adapter);

        //编辑功能
        fab = (FloatingActionButton) findViewById(R.id.edit);
        initFab();

    }

    /*编辑按钮的点击事件*/
    private void initFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().getIntExtra("code",0) == LOGIN_CODE)
                {
                    /*跳转到编辑按钮并传递user_name*/
                    String name = getIntent().getStringExtra("username");
                    Intent intent = new Intent(MainActivity.this,EditArticleActivity.class);
                    intent.putExtra("author",name);
                    startActivity(intent);
                }
            }
        });
    }

    /*得到articleList数据*/
    private void initArticle() {
        articleList = LitePal.findAll(Article.class);
    }

    /*设置Navigation-Menu的点击事件*/
    private void initMenu() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //各菜单点击事件
                Intent intent2 = null;
                switch (item.getItemId()){
                    case R.id.nav_collection:
                        if(getIntent().getIntExtra("code",0) == LOGIN_CODE)
                        {
                            /*登录后可以查看收藏页面，向CollectionActivity传递数据*/
                            String name = getIntent().getStringExtra("username");
                            intent2 = new Intent(MainActivity.this, CollectionActivity.class);
                            intent2.putExtra("user",name);
                            startActivity(intent2);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.nav_directory:
                        if(getIntent().getIntExtra("code",0)==LOGIN_CODE)
                        {
                            /*登录后可以查看我的文章页面，向MyArticleActivity传递数据*/
                            String name = getIntent().getStringExtra("username");
                            intent2 = new Intent(MainActivity.this, MyArticleActivity.class);
                            intent2.putExtra("txt_author",name);
                            Log.d("Test","nav_name"+name);
                            startActivity(intent2);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.nav_settings:
                        /*未登录可以查看，但要修改必须登录*/
                        intent2 = new Intent(MainActivity.this, SettingActivity.class);
                        String name = getIntent().getStringExtra("username");
                        intent2.putExtra("username",name);
                        startActivity(intent2);
                        break;
                    default:
                }
                return true;
            }
        });
    }

    /*设置NavigationView-Header的点击事件*/
    private void initHeader() {
        user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent1);
            }
        });
        /*得到从Activity传出的数据*/
        intent = getIntent();
        String name = intent.getStringExtra("username");
        int code = intent.getIntExtra("code",0);
        /*判断是否为登录状态*/
        if(code == LOGIN_CODE && name != null){
            username.setText(name);
            user_icon.setClickable(false);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    /*从Activity向MainActivity传递数据*/
    public static void actionStart(Context context, String username,int code){
        Intent intent = new Intent(context,MainActivity.class);
        intent.putExtra("code",code);
        intent.putExtra("username",username);
        context.startActivity(intent);
    }

}
