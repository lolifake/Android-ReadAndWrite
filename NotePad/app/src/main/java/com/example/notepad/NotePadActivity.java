package com.example.notepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.ListView;

import android.widget.Toast;

import java.util.List;

public class NotePadActivity extends Activity {

    private List<Note> list;
    private NoteAdapter adapter;
    private ListView listView;
    private sql mysql;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        listView = (ListView) findViewById(R.id.list_view);
        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotePadActivity.this,EditActivity.class);
                startActivityForResult(intent,1);//打开EditActivity
            }
        });
        initData();
    }

    private void initData() {
        mysql = new sql(this);
        showQueryData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                Note notepad = list.get(position);
                Intent intent = new Intent(NotePadActivity.this, EditActivity.class);
                intent.putExtra("id", notepad.getId());
                intent.putExtra("title", notepad.getTitle()); //记录的标题
                intent.putExtra("content", notepad.getContent()); //记录的内容
                NotePadActivity.this.startActivityForResult(intent, 1);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int
                    position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder( NotePadActivity.this);
                        dialog.setMessage("是否删除此事件？");
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Note notepad = list.get(position);
                                if(mysql.deleteData(notepad.getId())){
                                    list.remove(position);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(NotePadActivity.this,"删除成功",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog.show();
                return true;
            }
        });
    }

    private void showQueryData() {
        if(list != null){
            list.clear();
        }

        list = mysql.query();
        adapter = new NoteAdapter(this,list);
        listView.setAdapter(adapter);
    }
    /**
     * requestCode和startActivityForResult中的requestCode相对应
     * resultCode是由子Activity通过其setResult()方法返回
     */
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){//EditActivity返回数据给NotePadActivity处理，重写函数
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==2){
            showQueryData();
        }
    }
}
