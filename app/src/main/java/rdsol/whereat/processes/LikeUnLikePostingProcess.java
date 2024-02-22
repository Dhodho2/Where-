package rdsol.whereat.processes;

import android.os.AsyncTask;

import static rdsol.whereat.netwox.HandleRequests.likeMedeaItem;
import static rdsol.whereat.netwox.HandleRequests.unLikeMedeaItem;

public class LikeUnLikePostingProcess extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground ( String... strings ) {
        String media_id = strings[0];
        String isLike = strings[1];

        return isLike.equals( "true" ) ? likeMedeaItem(media_id):
                unLikeMedeaItem(media_id);
    }


}
