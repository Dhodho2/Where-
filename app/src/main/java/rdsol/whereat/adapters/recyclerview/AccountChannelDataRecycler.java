package rdsol.whereat.adapters.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import rdsol.whereat.R;
import rdsol.whereat.activities.ViewWatchRead;
import rdsol.whereat.database.room.entities.DBAccountChannelData;

public class AccountChannelDataRecycler extends  RecyclerView.Adapter<AccountChannelDataRecycler.CustomeViewHolder> {

    private List<DBAccountChannelData> items ;
    private Context context;


    public AccountChannelDataRecycler ( List<DBAccountChannelData> items, Context context ) {
        this.items = items;
        this.context = context;
    }

    @Override
    public void onBindViewHolder (CustomeViewHolder holder, int position) {
        DBAccountChannelData feeditem = items.get( position );
        Glide.with( context ).load(/*R.drawable.icon_dark */feeditem.getImage_url()).placeholder(R.drawable.icon_dark ).into( holder.image_thumbnail );


        /*holder.NameOfPersonChat.setText(feeditem.getNameOfPersonChat());
        holder.commentText.setText(feeditem.getMessageText() );

        holder.comment_dated.setText(feeditem.getDated() == null? "":feeditem.getDated().split( " " )[1]);


*/

        holder.image_thumbnail.setOnClickListener( ev -> {
            context.startActivity( new Intent( context, ViewWatchRead.class )
                    .putExtra( "id_media", feeditem.getId_row() + "" )
                    .putExtra( "video_file", feeditem.getImage_url() )
                    .putExtra( "file", feeditem.getId_row() )


                    .putExtra( "dated", feeditem.getDated() )
                    .putExtra( "liked", feeditem.getLiked() )
                    .putExtra( "comments", feeditem.getComments_count() )
            );
        } );
    }

    @Override
    public int getItemCount ( ) {
        return items.size();
    }

    @NonNull
    @Override
    public CustomeViewHolder onCreateViewHolder ( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.profile_items, null);
        return new CustomeViewHolder( view );
    }
    static class CustomeViewHolder extends RecyclerView.ViewHolder{
      //  private TextView NameOfPersonChat,commentText,comment_dated;
        private ImageView image_thumbnail;
        CustomeViewHolder ( @NonNull View itemView ) {
            super( itemView );
           /* this.NameOfPersonChat = itemView.findViewById( R.id.NameOfPersonChat);
            this.commentText = itemView.findViewById( R.id.commentText);*/
            this.image_thumbnail = itemView.findViewById( R.id.image_thumbnail);
          //  this.comment_dated = itemView.findViewById( R.id.comment_dated);
           // image_thumbnail.setVisibility( View.INVISIBLE );
        }
    }
}
