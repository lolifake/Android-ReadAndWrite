package com.example.network_weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button sendRequest;
    TextView ReponseTxt;
    EditText EditTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendRequest = (Button) findViewById(R.id.send_Request);
        ReponseTxt = (TextView) findViewById(R.id.response_txt);
        EditTxt = (EditText) findViewById(R.id.edit_txt);
        sendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.send_Request){
            sendRequestwithOkHttp();
        }
    }

    private void sendRequestwithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String cityname = EditTxt.getText().toString();
                try{
                    OkHttpClient client = new OkHttpClient(); //创建OkHttpClient实例
                    Request request = new Request.Builder()
                            .url("https://free-api.heweather.com/s6/weather/now?key=5ceb2bcd85bc4ce48fd538b47e8ade0b&location="+cityname)//请求接口
                            .build();//创建Request对象
                    Response response = client.newCall(request).execute();//发送请求获得服务器返回的数据
                    String responseData = response.body().string(); //得到返回数据的具体内容

                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
                    JsonRootBean jsondata = gson.fromJson(responseData,JsonRootBean.class);
                    showResponse(jsondata);
                    Log.d("kwwl", "地点==" + responseData);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showResponse(final JsonRootBean data){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //在这里进行UI操作，将结果显示在界面上
                ReponseTxt.setText("更新时间："+data.getHeWeather6().get(0).getUpdate().getLoc()+"\n当地温度："
                +data.getHeWeather6().get(0).getNow().getTmp()+"\n天气状况："+
                        data.getHeWeather6().get(0).getNow().getCond_txt()+"\n风向："+
                        data.getHeWeather6().get(0).getNow().getWind_dir()+"\n风力等级："+
                        data.getHeWeather6().get(0).getNow().getWind_sc());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_item:
                Toast.makeText(this, "Add sth", Toast.LENGTH_SHORT).show();
                break;
            case R.id.remove_item:
                Toast.makeText(this, "Remove sth", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}