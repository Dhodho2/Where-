package rdsol.whereat.adapters.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skybase.humanizer.DateHumanizer;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rdsol.whereat.R;
import rdsol.whereat.activities.ViewWatchRead;
import rdsol.whereat.database.preferences.MyPreferences;
import rdsol.whereat.database.room.entities.DBNotificationsItems;
import rdsol.whereat.processes.UpdateNotificationAsSeen;

public class NotificationsRecyclerViewAdapter extends RecyclerView.Adapter<NotificationsRecyclerViewAdapter.CustomeViewHolder> {


    private List<DBNotificationsItems> items;
    private Context context;
    private MyPreferences myPreferences;

    public NotificationsRecyclerViewAdapter ( List<DBNotificationsItems> items, Context context ) {


        this.items = items;
        this.context = context;
        myPreferences = new MyPreferences( context );

    }

    @Override
    public int getItemCount ( ) {
        return items == null ? 0 : items.size();
    }

    static class CustomeViewHolder extends RecyclerView.ViewHolder {
        private TextView title_sub_text, title_text;
        CircleImageView imageView;

        ConstraintLayout llout;

        CustomeViewHolder ( @NonNull View itemView ) {
            super( itemView );

            this.imageView = itemView.findViewById( R.id.imageView );
            this.llout = itemView.findViewById( R.id.llout );
            this.title_sub_text = itemView.findViewById( R.id.title_sub_text );
            this.title_text = itemView.findViewById( R.id.title_text );


        }
    }

    @Override
    public void onBindViewHolder ( CustomeViewHolder holder, int position ) {
        DBNotificationsItems feeditem = items.get( position );
        holder.title_text.setText( feeditem.getDescription() + " ".concat( feeditem.getPost_title() ) );
        String f = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
            Date date = format.parse( feeditem.getOndate() );
            f = ( DateHumanizer.humanize( date, DateHumanizer.TYPE_PRETTY_EVERYTHING ) );


        } catch ( ParseException e ) {
            e.printStackTrace();
            f = feeditem.getOndate();
        }
        if ( feeditem.getIs_seen() == 1 ) {
            holder.title_sub_text.setText( f + " *(Seen)" );
        } else {
            holder.title_sub_text.setText( f );
        }
       /* Log.e( "xxxx", "onBindViewHolder:||------| " +  feeditem.getIs_image() );
        Log.e( "xxxx", "onBindViewHolder:||| " +  feeditem.getMedia() );*/
        holder.llout.setOnClickListener( ev -> {
            if ( feeditem.getMedia().endsWith( ".mp4" ) ) {
                context.startActivity( new Intent( context, ViewWatchRead.class )
                        .putExtra( "id_media", feeditem.getMedia_id() + "" )
                        .putExtra( "video_file", feeditem.getMedia() )
                        .putExtra( "file", feeditem.getMedia() )
                        .putExtra( "stream", "no" )
                        .putExtra( "dated", feeditem.getOndate() )
                        .putExtra( "liked", 0 )
                        .putExtra( "comments", 0 )
                );
            }
        } );
        Picasso.get().load( feeditem.getProfile_image() ).placeholder( R.drawable.ic_account_circle_deep_purple_600_24dp ).into( holder.imageView );
    }

    @NonNull
    @Override
    public CustomeViewHolder onCreateViewHolder ( ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.notifications_item, parent, false );
        return new CustomeViewHolder( view );
    }

    @Override
    public void onAttachedToRecyclerView ( @NonNull RecyclerView recyclerView ) {
        super.onAttachedToRecyclerView( recyclerView );
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if ( manager instanceof LinearLayoutManager && getItemCount() > 0 ) {
            LinearLayoutManager llm = ( LinearLayoutManager ) manager;
            recyclerView.addOnScrollListener( new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged ( @NonNull RecyclerView recyclerView, int newState ) {
                    super.onScrollStateChanged( recyclerView, newState );
                }

                void updateSeenMessages ( int visiblePosition ) {
                    DBNotificationsItems feeditem = items.get( visiblePosition );
                    if ( feeditem.getIs_seen() == 0 && visiblePosition > -1 && items.size() > visiblePosition ) {
                        View v = llm.findViewByPosition( visiblePosition );


                        UpdateNotificationAsSeen notification = new UpdateNotificationAsSeen();
                        notification.execute( feeditem.getId_rows() + "" );
                    }
                }

                @Override
                public void onScrolled ( @NonNull RecyclerView recyclerView, int dx, int dy ) {
                    super.onScrolled( recyclerView, dx, dy );

                    int visiblePosition = llm.findFirstCompletelyVisibleItemPosition();
                    updateSeenMessages( visiblePosition );


                    visiblePosition = llm.findFirstVisibleItemPosition();
                    updateSeenMessages( visiblePosition );

                    visiblePosition = llm.findLastCompletelyVisibleItemPosition();
                    updateSeenMessages( visiblePosition );


                }
            } );
        }
    }

}
