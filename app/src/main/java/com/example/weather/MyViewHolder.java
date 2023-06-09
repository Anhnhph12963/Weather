package com.example.weather;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    CircleImageView profile_image;
    ImageView img_like, img_send, img_comment, img_post;
    TextView tv_username, tv_timeAge, title_post, tv_like, tv_comment;
    EditText edt_comment;
    public static RecyclerView recyclerView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        profile_image = itemView.findViewById(R.id.profile_image);
        img_post = itemView.findViewById(R.id.img_post);
        img_like = itemView.findViewById(R.id.img_like);
        img_comment = itemView.findViewById(R.id.img_comment);
        tv_username = itemView.findViewById(R.id.tv_username);
        tv_timeAge = itemView.findViewById(R.id.timeAge);
        title_post = itemView.findViewById(R.id.title_post);
        tv_like = itemView.findViewById(R.id.tv_like);
        tv_comment = itemView.findViewById(R.id.tv_comment);
        img_send = itemView.findViewById(R.id.img_send);
        edt_comment = itemView.findViewById(R.id.edt_commnet);
        recyclerView = itemView.findViewById(R.id.ryc_comment);

    }

    public void countLike(String postKey, String uid, DatabaseReference databaseLike) {
        databaseLike.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int totaLikes = (int) snapshot.getChildrenCount();
                    tv_like.setText(totaLikes + "");
                } else {
                    tv_like.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseLike.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(uid).exists()) {
                    img_like.setColorFilter(Color.BLUE);
                } else {
                    img_like.setColorFilter(Color.GRAY);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
