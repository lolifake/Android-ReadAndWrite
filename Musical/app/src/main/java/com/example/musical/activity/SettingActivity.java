package com.example.musical.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musical.MainActivity;
import com.example.musical.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView nick_name,sexual,note;
    private Button login_out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nick_name = (TextView) findViewById(R.id.niki_name);
        sexual = (TextView) findViewById(R.id.sexual);
        note = (TextView) findViewById(R.id.per_note);
        login_out = (Button) findViewById(R.id.login_out);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        nick_name.setOnClickListener(this);
        sexual.setOnClickListener(this);
        note.setOnClickListener(this);
        login_out.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                /*修改完毕后返回主页面*/
                String name = getIntent().getStringExtra("username");
                MainActivity.actionStart(SettingActivity.this,name,1);
                finish();
                break;
            default:
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        //从主页面拿到用户名
        String name = getIntent().getStringExtra("username");
        /*进行昵称等设置时需要先登录*/
        if(name != null){
            switch (view.getId()){
                case R.id.niki_name:
                    ItemActivity.onAction(SettingActivity.this,nick_name.getText().toString(),name);
                    break;
                case R.id.sexual:
                    ItemActivity.onAction(SettingActivity.this,sexual.getText().toString(),name);
                    break;
                case R.id.per_note:
                    ItemActivity.onAction(SettingActivity.this,note.getText().toString(),name);
                    break;
                case R.id.login_out:
                    MainActivity.actionStart(SettingActivity.this,"未登录",0);
                    break;
                default:
            }
            finish();
        }
        else{
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
        }

    }



}