package com.example.notepad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class NoteAdapter extends BaseAdapter {

    private List<Note> data;
    private Context context;

    public NoteAdapter(Context context,List<Note> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.noteinfo,null);
            holder = new ViewHolder();
            holder.txt_title=(TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Note noteInfo = (Note) getItem(position);
        holder.txt_title.setText(noteInfo.getTitle());
        return convertView;
    }

    static class ViewHolder{
        TextView txt_title;
    }
}