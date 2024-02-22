package rdsol.whereat.adapters.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import rdsol.whereat.R;
import rdsol.whereat.database.room.entities.DBCommentMessage;

public class CommentsRecyclerViewChats extends  RecyclerView.Adapter<CommentsRecyclerViewChats.CustomeViewHolder> {
    private List<DBCommentMessage> items ;
    private Context context;


    public CommentsRecyclerViewChats ( List<DBCommentMessage> items, Context context ) {
        this.items = items;
        this.context = context;
    }

    @Override
    public void onBindViewHolder (CustomeViewHolder holder, int position) {
        DBCommentMessage feeditem = items.get( position );

        holder.NameOfPersonChat.setText(feeditem.getNameOfPersonChat());
        holder.commentText.setText(feeditem.getMessageText() );

        holder.comment_dated.setText(feeditem.getDated() == null? "":feeditem.getDated().split( " " )[1]);

    }

    @Override
    public int getItemCount ( ) {
        return items.size();
    }

    @NonNull
    @Override
    public CustomeViewHolder onCreateViewHolder ( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.readview_comments_item, parent,false);
        return new CustomeViewHolder( view );
    }
    static class CustomeViewHolder extends RecyclerView.ViewHolder{
        private TextView NameOfPersonChat,commentText,comment_dated;
        private ImageView loading_status;
        CustomeViewHolder ( @NonNull View itemView ) {
            super( itemView );
            this.NameOfPersonChat = itemView.findViewById( R.id.NameOfPersonChat);
            this.commentText = itemView.findViewById( R.id.commentText);
            this.loading_status = itemView.findViewById( R.id.loading_status);
            this.comment_dated = itemView.findViewById( R.id.comment_dated);
            loading_status.setVisibility( View.INVISIBLE );
        }
    }
}
