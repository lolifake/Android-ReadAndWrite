package com.example.notepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EditText useraccount = (EditText) findViewById(R.id.useraccount);
        EditText userpwd = (EditText) findViewById(R.id.userpwd);
        EditText conuserpwd = (EditText) findViewById(R.id.conuserpwd);
        Button submit = (Button) findViewById(R.id.submit);

        MySQL mysql = new MySQL(this,"Userinfo",null,1); //建数据库
        SQLiteDatabase db = mysql.getReadableDatabase();
        SharedPreferences sp = this.getSharedPreferences("userinfo",this.MODE_PRIVATE); //将注册数据保存在userifo中

        submit.setOnClickListener(new View.OnClickListener() {
            boolean flag = true; //判断用户账号是否已存在的标志位
            @Override
            public void onClick(View view) {
                String account = useraccount.getText().toString();
                String pwd = userpwd.getText().toString();
                String conpwd = conuserpwd.getText().toString();

                if(account.equals("") || pwd.equals("") || conpwd.equals("")){
                    Toast.makeText(RegisterActivity.this,"账户和密码不能为空",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    Cursor cursor = db.query("logins",new String[]{"useraccount"},null,
                            null,null,null,null);
                    while(cursor.moveToNext()){
                        if(cursor.getString(0).equals(account)){
                            flag = false;
                            break;
                        }
                    }
                    if(flag){//判断用户是否已存在
                        if(pwd.equals(conpwd)){//判断两次输入的密码是否一致
                            //将数据插入数据表中
                            ContentValues cv = new ContentValues();
                            cv.put("useraccount",account);
                            cv.put("userpwd",pwd);
                            db.insert("logins",null,cv);
                            //将数据存储到SharedPreferences中
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("useraccount",account);
                            editor.putString("userpwd",pwd);
                            editor.apply();
                            //页面之间的跳转
                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(intent);
                            Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(RegisterActivity.this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this,"该账号已存在",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}