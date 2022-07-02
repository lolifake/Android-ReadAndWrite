package com.example.musical.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.musical.R;
import com.example.musical.enity.Article;
import com.example.musical.enity.User;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
/*设置页面的具体设置页面*/
public class ItemActivity extends AppCompatActivity {

    private String text_info;
    private String username;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alter_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView info_view = (TextView) findViewById(R.id.text_info);
        editText = (EditText) findViewById(R.id.edit_info);
        Intent intent = getIntent();
        /*从SettingActivity得到选择的设置并显示出来*/
        text_info = intent.getStringExtra("info");
        username = intent.getStringExtra("username");/*得到登录用户的用户名*/

        info_view.setText(text_info);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(ItemActivity.this,SettingActivity.class);
        String con = editText.getText().toString();
        User user = new User();
        Article article = new Article();
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.submit:
                /*在此处将编辑好的内容传入数据库*/
                if(text_info.equals("昵称")){
                    /*修改用户名，需要修改用户表和文章表中的有关数据*/
                    user.setNiki_name(con);
                    user.updateAll("niki_name = ?",username);
                    article.setTxt_author(con);
                    article.updateAll("txt_author = ?",username);
                    /*传递修改后的用户名给别的活动*/
                    intent.putExtra("username",con);
                    Toast.makeText(this, "昵称修改成功", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }else if(text_info.equals("性别")){
                    /*更新User表中的数据*/
                    user.setSexual(con);
                    user.updateAll("niki_name = ?",username);
                    /*只修改性别，用户名并未修改，传递得到的用户名即可*/
                    Toast.makeText(this, "性别修改成功", Toast.LENGTH_SHORT).show();
                    intent.putExtra("username",username);
                    startActivity(intent);
                }else if(text_info.equals("个性签名")){
                    /*更新User表中的数据*/
                    user.setSexual(con);
                    user.updateAll("niki_name = ?",username);
                    Toast.makeText(this, "个性签名修改成功", Toast.LENGTH_SHORT).show();
                    /*只修改个性签名，用户名并未修改，传递得到的用户名即可*/
                    intent.putExtra("username",username);
                    startActivity(intent);
                }
                finish();/*销毁活动*/
                break;
            default:
        }
        return true;
    }

    public static void onAction(Context context, String info,String username){
        Intent intent = new Intent(context,ItemActivity.class);
        intent.putExtra("info",info);
        intent.putExtra("username",username);
        context.startActivity(intent);
    }
}