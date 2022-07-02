package com.example.write;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.write.activity.CollectionActivity;
import com.example.write.activity.EditActivity;
import com.example.write.activity.IconActivity;
import com.example.write.activity.LoginActivity;
import com.example.write.activity.MyArticleActivity;
import com.example.write.activity.SettingActivity;
import com.example.write.adapter.BmobArticleAdapter;
import com.example.write.bean.BbArticle;
import com.example.write.bean.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;

    private View nav_header;
    private TextView username;//用户名
    private ImageView user_icon;//用户头像
    private FloatingActionButton fab;//编辑按钮

    private  Intent intent;
    private static final int LOGIN_CODE = 1;//判断登录状态码

    private List<BbArticle> articleList = new ArrayList<>();
    private BmobArticleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //默认初始化
        Bmob.initialize(this,"4411744e07f52d9f72363fdba661efbc");
        //设置toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //将导航栏显示出来
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        //滑动页面
        navigationView =(NavigationView) findViewById(R.id.nav_view);
        nav_header=navigationView.getHeaderView(0);//获取Header
        username = nav_header.findViewById(R.id.username);
        user_icon = nav_header.findViewById(R.id.icon_image);//获取Header控件--用户头像控件
        /*判断用户是否是登录状态*/
        if (BmobUser.isLogin()) {
            User user = BmobUser.getCurrentUser(User.class);
            String name = user.getUsername();
            username.setText(name);
//            downloadIcon(user);
            //user_icon.setClickable(false);
            user_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, IconActivity.class);
                    startActivity(intent);
                }
            });
        }else{
            username.setText("未登录");
            user_icon.setOnClickListener(this);
        }

        initMenu();
        initArticle();
        fab = (FloatingActionButton)findViewById(R.id.edit);
        fab.setOnClickListener(this);
    }

    /*下载用户头像并显示出来*/
    private void downloadIcon(User user)
    {
        BmobFile bmobFile = user.getUserIcon();
        if(bmobFile.getFileUrl() != null){
        bmobFile.download(new DownloadFileListener() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    String imagePath = bmobFile.getFileUrl();
                    if(imagePath != null)
                    {
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                        user_icon.setImageBitmap(bitmap);
                    }
                    Log.d("Test","UserIcon download success:"+bmobFile.getFileUrl());
                }
                else{
                    Log.d("Test","UserIcon download fail:"+e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer integer, long l) {

            }
        });}
    }


    /*初始化articleList，从Bmob云数据库取数据*/
    private void initArticle() {
        BmobQuery<BbArticle> articleBmobQuery = new BmobQuery<>();
        /*按更新的时间顺序排序查询*/
        articleBmobQuery.order("-updatedAt");//排序
        articleBmobQuery.findObjects(new FindListener<BbArticle>() {
            @Override
            public void done(List<BbArticle> list, BmobException e) {
                if(e == null)
                {
                    articleList = list;
                    /*显示出来*/
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(staggeredGridLayoutManager);
                    adapter = new BmobArticleAdapter(articleList);
                    recyclerView.setAdapter(adapter);
                    Log.d("Test","查询成功");
                }
                else{
                    Log.d("Test","查询数据失败");
                }
            }
        });
    }

    /*设置Navigation-Menu的点击事件*/
    private void initMenu() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_collection:
                        if(BmobUser.isLogin()){
                            Intent intent = new Intent(MainActivity.this, CollectionActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.nav_directory:
                        if(BmobUser.isLogin())
                        {
                            Intent intent = new Intent(MainActivity.this, MyArticleActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.nav_settings:
                        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.icon_image:
                Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent1);
                break;
            case R.id.edit:
                /*判断当前是否是登录状态，是登录状态则跳转到EditActivity，否则输出提示信息*/
                if(BmobUser.isLogin())
                {
                    User user = BmobUser.getCurrentUser(User.class);
                    Log.d("Test","Main_User"+user.getUsername());
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
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
    public static void actionStart(Context context, String username, int code){
        Intent intent = new Intent(context,MainActivity.class);
        intent.putExtra("code",code);
        intent.putExtra("username",username);
        context.startActivity(intent);
    }
}