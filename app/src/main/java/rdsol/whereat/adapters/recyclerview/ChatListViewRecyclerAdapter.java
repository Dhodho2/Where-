package rdsol.whereat.adapters.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rdsol.whereat.R;
import rdsol.whereat.database.preferences.MyPreferences;
import rdsol.whereat.database.room.entities.DBMessages;
import rdsol.whereat.processes.NotifyDataAsReadAndSeenProcess;

public class ChatListViewRecyclerAdapter extends RecyclerView.Adapter<ChatListViewRecyclerAdapter.CustomeViewHolder> implements NotifyDataAsReadAndSeenProcess.HttpNotifyDataAsReadAndSeenCallBack {


    private List<DBMessages> items;
    private Context context;
    private MyPreferences myPreferences;

    public ChatListViewRecyclerAdapter ( List<DBMessages> items, Context context ) {
        //Set<DBMessages> data = new HashSet<>(  );
        /*data.addAll(items  );
        items.clear();
        items.addAll( data );*/
        this.items = items;
        this.context = context;
        myPreferences = new MyPreferences( context );
    }

    @NonNull
    @Override
    public CustomeViewHolder onCreateViewHolder ( ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_chat_message_item, parent, false );
        return new CustomeViewHolder( view );
    }

    @Override
    public void onBindViewHolder ( CustomeViewHolder holder, int position ) {
        DBMessages feeditem = items.get( position );


        //Log.e( "xxxx", "onBindViewHolder: " +feeditem.getImage_url() );
        Glide.with( context )
                .load( feeditem.getProfile_image() )
                .diskCacheStrategy( DiskCacheStrategy.ALL )
                .placeholder( R.drawable.ic_account_circle_deep_purple_600_24dp )
                .into( holder.image_message_profile );


        holder.comment_dated.setText( ( feeditem.getDated().split( " " )[ 1 ] ) );
        holder.messageText.setText( ( feeditem.getMessage() ) );
        holder.NameOfPersonChat.setText( ( feeditem.getFrom_full_name() ) );

        if ( myPreferences.getUserId().equals( feeditem.getFrom_id_user() + "" ) ) {
            holder.comment_dated.setVisibility( View.GONE );
            holder.image_message_profile.setVisibility( View.GONE );
            holder.messageText.setVisibility( View.GONE );
            holder.NameOfPersonChat.setVisibility( View.GONE );


            holder.comment_datedME.setText( ( feeditem.getDated().split( " " )[ 1 ] ) );
            holder.messageTextMe.setText( ( feeditem.getMessage() ) );

            holder.messageTextMe.setVisibility( View.VISIBLE );
            holder.comment_datedME.setVisibility( View.VISIBLE );
            if ( feeditem.getIs_seen_by_to_id_user() == 1 ) {
                holder.comment_Seen.setVisibility( View.VISIBLE );
            }
        }


    } //end if

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
                    if ( visiblePosition > -1 && items.size() > visiblePosition ) {
                        View v = llm.findViewByPosition( visiblePosition );
                        DBMessages feeditem = items.get( visiblePosition );
                        if ( !myPreferences.getUserId().equals( feeditem.getFrom_id_user() + "" ) ) {
                            if ( feeditem.getIs_seen_by_to_id_user() == 0 ) {

                                NotifyDataAsReadAndSeenProcess notifyDataAsReadAndSeenProcess = new NotifyDataAsReadAndSeenProcess( ChatListViewRecyclerAdapter.this );
                                notifyDataAsReadAndSeenProcess.execute( "dm", feeditem.getId_rows() + "" );
                            }
                        }
                        //if()
                        //  v.setBackgroundColor( DynamicColorUtils.getRandomColor() );
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

    @Override
    public int getItemCount ( ) {
        return items == null ? 0 : items.size();
    }

    @Override
    public void onDoneDelivery ( String response ) {
        //  RoomDB.roomConnect().getTableDirectMessages().setMessageAsSeen( feeditem.getId_rows() );
    }

    static class CustomeViewHolder extends RecyclerView.ViewHolder {
        private TextView NameOfPersonChat, messageText, comment_Seen, comment_dated, messageTextMe, comment_datedME;
        CircleImageView image_message_profile;


        CustomeViewHolder ( @NonNull View itemView ) {
            super( itemView );

            this.comment_datedME = itemView.findViewById( R.id.comment_datedME );
            this.NameOfPersonChat = itemView.findViewById( R.id.NameOfPersonChat );
            this.messageTextMe = itemView.findViewById( R.id.messageTextMe );

            this.messageText = itemView.findViewById( R.id.messageText );
            this.comment_Seen = itemView.findViewById( R.id.comment_Seen );
            this.comment_dated = itemView.findViewById( R.id.comment_dated );
            this.image_message_profile = itemView.findViewById( R.id.image_message_profile );


        }
    }

}
