package com.example.musical.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.musical.R;
import com.example.musical.enity.User;

import org.litepal.LitePal;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private boolean FLAG=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button register = (Button) findViewById(R.id.register);
        /*EditText res_email = (EditText) findViewById(R.id.res_email);*/
        EditText res_pwd = (EditText) findViewById(R.id.res_pwd);
        EditText enres_pwd = (EditText) findViewById(R.id.enres_pwd);
        EditText res_name = (EditText) findViewById(R.id.res_name);

        res_pwd.setInputType(129);//设置密码不可见，如果editText.getInputType()的值为128则代表目前是明文显示密码，为129则是隐藏密码
        enres_pwd.setInputType(129);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  String email = res_email.getText().toString();*/
                /*得到编辑框数据*/
                String pwd = res_pwd.getText().toString();
                String en_pwd = enres_pwd.getText().toString();
                String name = res_name.getText().toString();
                /*判断昵称，密码是否为空*/
                if(name.equals("")||pwd.equals("")||en_pwd.equals("")){
                    Toast.makeText(RegisterActivity.this, "昵称或密码不能为空", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    /*从数据库得到数据*/
                    List<User> userList = LitePal.findAll(User.class);
                    for(User user:userList){
                        if(user.getNiki_name().equals(name)){//如果数据库中存在相同的数据，标志设为false
                            Log.d("Test",user.getNiki_name());
                            FLAG = false;
                            break;
                        }
                    }
                    if(FLAG)//数据库不存在使用该昵称的用户
                    {
                        if(pwd.equals(en_pwd))//两次输入的密码一致
                        {
                            /*将注册用户添加进数据表*/
                            User user = new User();
                            user.setNiki_name(name);
                            user.setPassword(pwd);
                            user.save();
                            /*跳转到LoginActivity*/
                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(intent);
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "昵称已存在", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }
}