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
import rdsol.whereat.pojos.PojoBillBoardData;
import rdsol.whereat.utils.AndroidUtilities;

public class ReyclerViewBillboard extends RecyclerView.Adapter<ReyclerViewBillboard.CustomeViewHolder>  {

    private List<PojoBillBoardData> items;
    private Context context;
    private MyPreferences myPreferences;

    public ReyclerViewBillboard ( List<PojoBillBoardData> items, Context context ) {
        this.items = items;
        this.context = context;
        myPreferences = new MyPreferences( context );
    }

    @Override
    public void onBindViewHolder (CustomeViewHolder holder, int position ) {
        PojoBillBoardData feeditem = items.get( position );

        holder.artistName.setText( (  feeditem.getArtistName()  ));
        holder.noOfShows.setText( (  feeditem.getNumberOfShows()  ));
        holder.ViewsCounter.setText( (  feeditem.getNumberOfViews()  ));

    }
    @NonNull
    @Override
    public CustomeViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.item_billboard, parent , false);
        return new CustomeViewHolder( view );
    }
    static class CustomeViewHolder extends RecyclerView.ViewHolder {
        private TextView artistName, noOfShows, ViewsCounter;

        CustomeViewHolder ( @NonNull View itemView ) {
            super( itemView );

            this.artistName = itemView.findViewById( R.id.artistName );
            this.noOfShows = itemView.findViewById( R.id.noOfShows );
            this.ViewsCounter = itemView.findViewById( R.id.ViewsCounter );

        }
    }


    @Override
    public int getItemCount ( ) {
        return items == null ? 0 : items.size();
    }
}
