package com.example.notepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);//加载布局
        //获取在布局文件中定义的元素
        EditText accountEdit = (EditText) findViewById(R.id.account);
        EditText passwordEdit = (EditText)  findViewById(R.id.password);
        Button login = (Button) findViewById(R.id.login);
        Button reg = (Button) findViewById(R.id.reg);
        //获取数据
        MySQL mysql = new MySQL(this,"Userinfo",null,1);
        SQLiteDatabase db = mysql.getReadableDatabase();
        SharedPreferences sp1 = this.getSharedPreferences("userinfo",this.MODE_PRIVATE);

        accountEdit.setText(sp1.getString("useraccount",null));
        passwordEdit.setText(sp1.getString("userpwd",null));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();

                //查询用户名和密码相同的数据
               Cursor cursor = db.query("logins",new String[]{"useraccount","userpwd"}," useraccount=? and userpwd=?",
                       new String[]{account,password},null,null,null);

                int flag = cursor.getCount(); //查询出的记录条数，若不为0，则进行跳转
                if(flag!=0){
                    Intent intent = new Intent(LoginActivity.this,NotePadActivity.class);
                    startActivity(intent);

                }
                else{
                    Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                }
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}