package com.example.weather;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentViewHolder extends RecyclerView.ViewHolder {
     CircleImageView img_user_comment;
     TextView tv_username,tv_comment;
    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        img_user_comment=itemView.findViewById(R.id.img_user_comment);
        tv_username = itemView.findViewById(R.id.tv_username);
        tv_comment = itemView.findViewById(R.id.tv_comment);
    }
}
