package rdsol.whereat.adapters.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.skybase.humanizer.DateHumanizer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import rdsol.whereat.R;
import rdsol.whereat.activities.GeneralFragmentHandlerActivity;
import rdsol.whereat.database.preferences.MyPreferences;
import rdsol.whereat.database.room.entities.DBMessages;

import static rdsol.whereat.activities.GeneralFragmentHandlerActivity.DIRECT_CHAT_MESSAGES;

public class DirectMessagesRecyclerViewAdapter  extends RecyclerView.Adapter<DirectMessagesRecyclerViewAdapter.CustomeViewHolder> {


    private List<DBMessages> items;
    private Context context;
    private MyPreferences myPreferences;

    public DirectMessagesRecyclerViewAdapter ( List<DBMessages> items, Context context ) {

        Set<DBMessages> data = new HashSet<>(  );
        Set<String> nams = new HashSet<>(  );

        for ( DBMessages item :
                items ) {
            if(!nams.contains( item.getFrom_full_name() )){
                data.add( item );
                nams.add( item.getFrom_full_name() );
            }
        }

        items.clear();
        items.addAll( data );
        this.items = items;
        this.context = context;
        myPreferences = new MyPreferences( context );
    }

    @NonNull
    @Override
    public CustomeViewHolder onCreateViewHolder ( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.item_chat_users, parent , false);
        return new CustomeViewHolder( view );
    }

    @Override
    public void onBindViewHolder ( CustomeViewHolder holder, int position ) {
        DBMessages feeditem = items.get( position );


        //Log.e( "xxxx", "onBindViewHolder: " +feeditem.getImage_url() );
        Glide.with( context ).load( feeditem.getProfile_image() ).diskCacheStrategy( DiskCacheStrategy.ALL )
                .placeholder( R.drawable.ic_account_circle_deep_purple_600_24dp )
                .into( holder.image_user_list_profile );
        String f ="";
        try {
             SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
            Date date = format.parse(feeditem.getDated() );
             f = (DateHumanizer.humanize(date,DateHumanizer.TYPE_PRETTY_EVERYTHING));


        } catch ( ParseException e ) {
            e.printStackTrace();
        }

        holder.text_view_blocked.setText( (  f ));
        holder.text_user_list_nickname.setText( (  feeditem.getFrom_full_name()  ));

        holder.llout.setOnClickListener( ev -> {
            context.startActivity( new Intent( context, GeneralFragmentHandlerActivity.class ).putExtra( "fragment_name",DIRECT_CHAT_MESSAGES  )
                    .putExtra( "user_chats", feeditem.getFrom_id_user() + "" )
                    .putExtra( "user_username", feeditem.getFrom_full_name() + "" )
            );
        } );

    }

    @Override
    public int getItemCount ( ) {
        return items == null ? 0 : items.size();
    }
    static class CustomeViewHolder extends RecyclerView.ViewHolder {
        private TextView text_user_list_nickname ,text_view_blocked;
        CircleImageView image_user_list_profile;

        LinearLayout llout ;

        CustomeViewHolder ( @NonNull View itemView ) {
            super( itemView );

            this.text_view_blocked = itemView.findViewById( R.id.text_view_blocked );
            this.llout = itemView.findViewById( R.id.llout );
            this.image_user_list_profile = itemView.findViewById( R.id.image_user_list_profile );
            this.text_user_list_nickname = itemView.findViewById( R.id.text_user_list_nickname );


        }
    }

}
