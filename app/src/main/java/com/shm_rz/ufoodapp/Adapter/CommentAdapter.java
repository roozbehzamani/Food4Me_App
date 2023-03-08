package com.shm_rz.ufoodapp.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.shm_rz.ufoodapp.Model.Comment;
import com.shm_rz.ufoodapp.R;

import java.util.ArrayList;

/**
 * Created by Shabnam Moazam on 15/05/2018.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {



    private ArrayList<Comment> cmList;
    CommentAdapter.customAdapterInterface customAdapterInterface;

    public CommentAdapter(ArrayList<Comment> cmList, CommentAdapter.customAdapterInterface customAdapterInterface){
        this.customAdapterInterface=customAdapterInterface;
        this.cmList=cmList;
    }

    public interface customAdapterInterface {
        void onCustomListItemClick(int position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.show_comment_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder , final int position) {
        Comment comment = cmList.get(position);

        holder.txtComment.setText(comment.getText());
        holder.txtUserPhone.setText(comment.getName());
        holder.ratingBar.setRating(comment.getStars());

        holder.resCartView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return cmList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView
                txtUserPhone , txtComment;
        private RatingBar ratingBar;
        private CardView
                resCartView;




        public MyViewHolder(final View itemView) {
            super(itemView);

            txtComment = (TextView)itemView.findViewById(R.id.txtComment);
            txtUserPhone = (TextView)itemView.findViewById(R.id.txtUserPhone);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            resCartView=(CardView)itemView.findViewById(R.id.cmCardView);

            resCartView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=(int)v.getTag();
                    customAdapterInterface.onCustomListItemClick(pos);
                }
            });
        }
    }
}
