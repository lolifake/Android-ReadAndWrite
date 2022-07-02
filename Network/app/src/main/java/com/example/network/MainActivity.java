package com.example.network;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener{

    Button baidu;
    Button bing;
    Button weibo;
    Button bilibili;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        baidu = (Button)  findViewById(R.id.baidu);
        bing = (Button) findViewById(R.id.bing);
        weibo = (Button) findViewById(R.id.weibo);
        bilibili = (Button) findViewById(R.id.bilibili);

        baidu.setOnClickListener(this);
        bing.setOnClickListener(this);
        weibo.setOnClickListener(this);
        bilibili.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);;
        if(view.getId() == R.id.baidu)
        {
            intent.putExtra("web","https://www.baidu.com/");
            startActivity(intent);
        }
        else if(view.getId() == R.id.bing){
            intent.putExtra("web","https://cn.bing.com/");
            startActivity(intent);
        }
        else if(view.getId() == R.id.weibo){
            intent.putExtra("web","https://weibo.com/");
            startActivity(intent);
        }
        else{
            intent.putExtra("web","https://www.bilibili.com/");
            startActivity(intent);
        }
    }
}