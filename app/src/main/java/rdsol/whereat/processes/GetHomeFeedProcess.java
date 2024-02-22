package rdsol.whereat.processes;

import android.os.AsyncTask;

import java.util.List;

import rdsol.whereat.callbacks.HttpReqCallBackData;
import rdsol.whereat.database.room.entities.DBHomeFeed;

import static rdsol.whereat.netwox.HandleRequests.getHomeFeedData;

public class GetHomeFeedProcess extends AsyncTask<Void,Void,List<DBHomeFeed > > {
    private HttpReqCallBackData httpReqCallBack;

    public GetHomeFeedProcess ( HttpReqCallBackData httpReqCallBack ) {
        this.httpReqCallBack = httpReqCallBack;
    }

    @Override
    protected List<DBHomeFeed> doInBackground ( Void... voids ) {
        return getHomeFeedData();
    }

    @Override
    protected void onPostExecute ( List<DBHomeFeed> dbHomeFeeds ) {
        super.onPostExecute( dbHomeFeeds );
        httpReqCallBack.onCompleteResponse( dbHomeFeeds );
    }
}
