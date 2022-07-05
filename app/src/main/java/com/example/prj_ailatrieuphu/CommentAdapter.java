package com.example.prj_ailatrieuphu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import Model.Comment;
import myinterface.CommentInterface;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
    Context context;
    View view;
    CommentInterface commentInterface;
    private ArrayList<Comment> ds;
    public CommentAdapter(ArrayList<Comment> ds,CommentInterface commentInterface){
        this.ds = ds;
        this.commentInterface = commentInterface;
    }
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_comment,parent,false);
        context = parent.getContext();
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = ds.get(position);
        if(comment == null) {
            return;
        }
        holder.txtUserName.setText(comment.getName());
        holder.txtCommentContent.setText(comment.getContent());
        holder.txtLikeNumber.setText(comment.getLike() + "");
        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentInterface.onClickInterface(comment);
                int likenumber = Integer.parseInt(holder.txtLikeNumber.getText().toString());
               if(holder.isLiked==false){
                   holder.isLiked = true;
                   holder.btnLike.setImageResource(R.drawable.red_heart);
                   likenumber++;

               }
               else{
                   holder.isLiked = false;
                   holder.btnLike.setImageResource(R.drawable.black_heart);
                   likenumber--;
               }
               holder.txtLikeNumber.setText(String.valueOf(likenumber));
               comment.setLike(likenumber);
                BackgroundTask backgroundTask = new BackgroundTask(context);
               backgroundTask.updateComment(comment);
            }
        });

    }

    @Override
    public int getItemCount() {
            return  ds.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        boolean isLiked = false,isUnliked = false;
        TextView txtUserName,txtCommentContent,txtLikeNumber,txtDislikeNumber;
        ImageButton btnLike;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtCommentContent = itemView.findViewById(R.id.txtCommentContent);
            txtLikeNumber = itemView.findViewById(R.id.txtLikeNumber);
            btnLike = itemView.findViewById(R.id.btnLike);
        }
    }
    public void release(){
        context = null;
    }
}
