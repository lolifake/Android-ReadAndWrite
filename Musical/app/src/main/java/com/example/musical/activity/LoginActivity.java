package com.example.musical.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musical.MainActivity;
import com.example.musical.R;
import com.example.musical.enity.User;

import org.litepal.LitePal;

import java.util.List;

public class LoginActivity extends AppCompatActivity{

    private EditText user_name,user_pwd;
    private  int FLAG = 0;//判断用户输入是否正确标志位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        Button login = (Button) findViewById(R.id.login);
        user_name=(EditText) findViewById(R.id.login_name);
        user_pwd=(EditText) findViewById(R.id.login_pwd);

        TextView register = (TextView) findViewById(R.id.register);
        user_pwd.setInputType(129); //隐藏密码

       // login.setOnClickListener(this);
        /*登录按钮*/
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*得到用户名和密码*/
                String name = user_name.getText().toString();
                String pwd = user_pwd.getText().toString();
                Log.d("Test","email:"+name);
                Log.d("Test","pwd:"+pwd);
                /*利用LitePal从数据库中查到数据表User的所有数据*/
                List<User> userList = LitePal.findAll(User.class);
                /*遍历userList查询该用户是否在数据库中*/
                for(User user:userList){
                    if(user.getNiki_name().equals(name) && user.getPassword().equals(pwd))
                    {
                        MainActivity.actionStart(LoginActivity.this,name,1);//向MainActivity传递登录状态码和登录用户名
                        /*使用SharedPreferences保存登录用户名*/
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                        editor.putString("username",name);
                        editor.apply();
                        FLAG=1;
                        finish();/*销毁当前活动*/
                        break;
                    }
                }
                if(FLAG == 0){
                    Toast.makeText(LoginActivity.this, "昵称或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*注册按钮*/
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

}