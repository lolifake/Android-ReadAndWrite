package com.example.notepad;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class EditActivity extends Activity implements View.OnClickListener {

    EditText content;
    EditText title;
    Button submit;
    Button delete;
    sql mysql;
    String id;
    TextView noteName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mysql = new sql(this);
        content = (EditText) findViewById(R.id.edit_content);
        title = (EditText)  findViewById(R.id.edit_title);
        noteName = (TextView) findViewById(R.id.note_name);
        submit = (Button) findViewById(R.id.submit);
        delete = (Button) findViewById(R.id.delete);

        delete.setOnClickListener(this);
        submit.setOnClickListener(this);
        initData();
    }

    private void initData() {
        mysql = new sql(this);
        noteName.setText("添加记录");
        Intent intent = getIntent();
        if(intent!= null){
            id = intent.getStringExtra("id");
            if (id != null){
                noteName.setText("修改记录");
                content.setText(intent.getStringExtra("content"));
                title.setText(intent.getStringExtra("title"));
            }
        }
    }

    public void showToast(String message){
        Toast.makeText(EditActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.delete:
                content.setText("");
                break;
            case R.id.submit:
                String Title = title.getText().toString().trim();
                String Content = content.getText().toString().trim();
                if (id != null){//修改操作
                    if (Title.length()>0){
                        if (mysql.updateData(id, Title, Content)){
                            showToast("修改成功");
                            setResult(2);
                            finish();
                        }else {
                            showToast("修改失败");
                        }
                    }else {
                        showToast("标题不能为空!");
                    }
                }
                else{
                    if(Title.length()>0){
                        if(mysql.insertData(Title,Content)){
                            showToast("保存成功");
                            setResult(2);//向NotePadActivity返回数据
                            finish();
                        }else{
                            showToast("保存失败");
                        }
                    }else{
                        showToast("标题不能为空");
                    }
                }
                break;
        }
    }
}