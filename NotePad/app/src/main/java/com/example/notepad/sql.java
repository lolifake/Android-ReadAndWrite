package com.example.notepad;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.notepad.Note;

import java.util.ArrayList;
import java.util.List;

public class sql extends SQLiteOpenHelper {

    private SQLiteDatabase sq;
    private SQLiteDatabase db;

    public static final String CREATE_TABLE = "create table Note(" +
            "id integer primary key autoincrement," +
            "title text," +
            "content text)";

    public sql(Context context){
        super(context,"Note",null,1);
        sq=this.getWritableDatabase();
        db = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    //添加数据
    public boolean insertData(String userTitle,String userContent){
        ContentValues cv = new ContentValues();
        cv.put("title",userTitle);
        cv.put("content",userContent);
        return sq.insert("Note",null,cv) > 0;
    }
    //删除数据
    public boolean deleteData(String id){
        String sql="id"+"=?";
        String[] contentValuesArray=new String[]{String.valueOf(id)};
        return
                sq.delete("Note",sql,contentValuesArray)>0;
    }
    //修改数据
    public boolean updateData(String id,String title,String content){
        ContentValues contentValues=new ContentValues();
        contentValues.put("content",content);
        contentValues.put("title",title);
        String sql="id"+"=?";
        String[] strings=new String[]{id};
        return
                sq.update("Note",contentValues,sql,strings)>0;
    }
    //查询数据
    public List<Note> query(){
        List<Note> list = new ArrayList<Note>();

        Cursor cursor = db.query("Note",new String[]{"id","title","content"},
                null,null,null,null,null);
        if(cursor != null){
            while(cursor.moveToNext()){
                Note noteInfo = new Note();
                @SuppressLint("Range")
                String title = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range")
                String content = cursor.getString(cursor.getColumnIndex("content"));
                @SuppressLint("Range")
                String id = String.valueOf(cursor.getInt
                        (cursor.getColumnIndex("id")));
                noteInfo.setId(id);
                noteInfo.setTitle(title);
                noteInfo.setContent(content);
                list.add(noteInfo);
            }
            cursor.close();
        }
        return list;
    }
}
