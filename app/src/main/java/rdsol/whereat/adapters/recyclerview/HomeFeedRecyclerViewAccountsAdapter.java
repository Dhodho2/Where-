package rdsol.whereat.adapters.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rishabhharit.roundedimageview.RoundedImageView;

import java.util.List;

import rdsol.whereat.R;
import rdsol.whereat.activities.AccountChannelProfile;
import rdsol.whereat.database.preferences.MyPreferences;
import rdsol.whereat.database.room.entities.AccountChannel;
import rdsol.whereat.utils.AndroidUtilities;

public class HomeFeedRecyclerViewAccountsAdapter extends RecyclerView.Adapter<HomeFeedRecyclerViewAccountsAdapter.CustomeViewHolder> {
    private List<AccountChannel> items;
    private Context context;
    private MyPreferences myPreferences;

    public HomeFeedRecyclerViewAccountsAdapter ( List<AccountChannel> items, Context context ) {
        this.items = items;
        this.context = context;
        myPreferences = new MyPreferences( context );
    }

    @NonNull
    @Override
    public CustomeViewHolder onCreateViewHolder ( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.accounts_feed_items, parent , false);
        return new CustomeViewHolder( view );
    }

    static class CustomeViewHolder extends RecyclerView.ViewHolder {
        private TextView title, dated, description;
        RoundedImageView avatar;

        CustomeViewHolder ( @NonNull View itemView ) {
            super( itemView );

            this.avatar = itemView.findViewById( R.id.avatar );
            this.title = itemView.findViewById( R.id.title );

        }
    }

    @Override
    public void onBindViewHolder ( CustomeViewHolder holder, int position ) {
        AccountChannel feeditem = items.get( position );
        Glide.with( context ).load( feeditem.getImage_url() ).diskCacheStrategy( DiskCacheStrategy.ALL )
                .placeholder( R.drawable.ic_account_circle_deep_purple_600_24dp ).into( holder.avatar );


        holder.title.setText( (  feeditem.getFull_name()  ));

        holder.avatar.setOnClickListener( ev -> {
            context.startActivity( new Intent( context, AccountChannelProfile.class )
                    .putExtra( "account", feeditem.getId_row() + "" ) );
        } );
        int imgWidht = holder.avatar.getWidth();
        int textViewWidth = holder.title.getWidth();
       /* if(textViewWidth > imgWidht){*/
            holder.title.setText( feeditem.getFull_name().length() > 7 ? AndroidUtilities.ellipseString(  feeditem.getFull_name()  ,9) :  feeditem.getFull_name());
        //}
    }

    @Override
    public int getItemCount ( ) {
        return items == null ? 0 : items.size();
    }

}
