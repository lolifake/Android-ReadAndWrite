package com.example.musical.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musical.enity.Article;
import com.example.musical.activity.ArticleActivity;
import com.example.musical.R;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private Context mContext;
    private List<Article> mArticleList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView textTitle;
        TextView textAuthor;
        TextView textContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=(CardView) itemView;
            textTitle=(TextView) itemView.findViewById(R.id.text_title);
            textAuthor=(TextView) itemView.findViewById(R.id.text_author);
            textContent=(TextView) itemView.findViewById(R.id.text_content);
        }
    }

    public ArticleAdapter(List<Article> articleList){
        mArticleList = articleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.article_item,parent,false);
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
        /*设置长按功能*/
       /* holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(mContext,"test",Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = mArticleList.get(position);
        holder.textTitle.setText(article.getTxt_title());
        holder.textAuthor.setText(article.getTxt_author());
        holder.textContent.setText(article.getTxt_content());
    }

    @Override
    public int getItemCount() {
        return mArticleList.size();
    }

}
