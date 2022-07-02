package com.example.musical.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musical.MainActivity;
import com.example.musical.activity.MyArticleActivity;
import com.example.musical.enity.Article;
import com.example.musical.R;
import com.example.musical.activity.ArticleActivity;

import org.litepal.LitePal;

import java.util.List;

public class MyArticleAdapter extends RecyclerView.Adapter<MyArticleAdapter.ViewHolder> {

    private Context mContext;
    private List<Article> mArticleList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView textTitle;
        TextView textAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=(CardView) itemView;
            textTitle=(TextView) itemView.findViewById(R.id.text_title);
            textAuthor=(TextView) itemView.findViewById(R.id.text_author);
        }
    }
    public MyArticleAdapter(List<Article> articleList){
        mArticleList = articleList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.myarticle,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        /*点击卡片，实现跳转*/
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Article article = mArticleList.get(position);
                Intent intent = new Intent(mContext, ArticleActivity.class);
                intent.putExtra(ArticleActivity.TXT_Title,article.getTxt_title());
                intent.putExtra(ArticleActivity.Txt_Content,article.getTxt_content());
                mContext.startActivity(intent);
            }
        });
        /*长按删除*/
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                /*使用dialog进行提示*/
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setMessage("是否删除此文章");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int position = holder.getAdapterPosition();
                        /*得到想删除的文章*/
                        Article article = mArticleList.get(position);
                        String name = article.getTxt_author();

                        /*从数据库删除该文章，按id删除*/
                        LitePal.delete(Article.class,article.id);
                        /*删除后跳转到MyArticleActivity*/
                        MyArticleActivity.onAction(mContext,name);
                        dialogInterface.dismiss();

                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();
                return true;
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyArticleAdapter.ViewHolder holder, int position) {
        Article article = mArticleList.get(position);
        holder.textTitle.setText(article.getTxt_title());
        holder.textAuthor.setText(article.getTxt_content());
//        holder.textAuthor.setText(article.getTxt_author());
    }

    @Override
    public int getItemCount() {
        return mArticleList.size();
    }
}
