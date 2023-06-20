package com.example.weather.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.Model.CommentQualityModel;
import com.example.weather.R;

import java.util.List;

public class QualityWeatherAdapter extends RecyclerView.Adapter<QualityWeatherAdapter.CommentQuality> {
    TextView tv_username, tv_comment, tv_date_comment;

    private List<CommentQualityModel> mCommentQuality;

    public QualityWeatherAdapter(List<CommentQualityModel> mCommentQuality) {
        this.mCommentQuality = mCommentQuality;
    }

    @NonNull
    @Override
    public CommentQuality onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentQuality(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QualityWeatherAdapter.CommentQuality holder, int position) {
        holder.bindData(mCommentQuality.get(position));
    }


    @Override
    public int getItemCount() {
        return mCommentQuality != null ? mCommentQuality.size() : 0;
    }

    class CommentQuality extends RecyclerView.ViewHolder {
        public CommentQuality(View itemView) {
            super(itemView);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_comment = itemView.findViewById(R.id.tv_comment);
            tv_date_comment = itemView.findViewById(R.id.tv_date_comment);
        }

        public void bindData(CommentQualityModel commentQuality) {
            tv_username.setText("" + commentQuality.getTv_username());
            tv_comment.setText("" + commentQuality.getTv_comment());
            tv_date_comment.setText("" + commentQuality.getTv_date_comment());

        }
    }
}

