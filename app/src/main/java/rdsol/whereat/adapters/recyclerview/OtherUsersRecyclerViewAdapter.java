package rdsol.whereat.adapters.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rdsol.whereat.R;
import rdsol.whereat.database.room.entities.AccountChannel;

public class OtherUsersRecyclerViewAdapter extends  RecyclerView.Adapter<OtherUsersRecyclerViewAdapter.CustomeViewHolder>  {


    private List<AccountChannel> items ;
    private Context context;

    public OtherUsersRecyclerViewAdapter ( List<AccountChannel> items, Context context ) {
        this.items = items;
        this.context = context;
    }


    @Override
    public void onBindViewHolder ( CustomeViewHolder holder, int position) {
        AccountChannel feeditem = items.get( position );

        holder.NameOfPersonChat.setText( feeditem.getUsername() );
        Glide.with( context ).load(feeditem.getImage_url()).placeholder(R.drawable.icon_dark ).into( holder.circleImageView );
    }

    @Override
    public int getItemCount ( ) {
        return items.size();
    }

    @NonNull
    @Override
    public CustomeViewHolder onCreateViewHolder ( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.share_to_names, null);
        return new CustomeViewHolder( view );
    }

    static class CustomeViewHolder extends RecyclerView.ViewHolder{
         private TextView NameOfPersonChat; ImageView tick ;
        private CircleImageView circleImageView;
        CustomeViewHolder ( @NonNull View itemView ) {
            super( itemView );

            this.tick = itemView.findViewById( R.id.tick);
            this.circleImageView = itemView.findViewById( R.id.circleImageView);
            this.NameOfPersonChat = itemView.findViewById( R.id.primary_text);
        }
    }
}
