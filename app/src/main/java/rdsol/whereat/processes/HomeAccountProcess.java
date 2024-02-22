package rdsol.whereat.processes;

import android.os.AsyncTask;

import java.util.List;

import rdsol.whereat.callbacks.HttpGetAccountHomeFeedData;
import rdsol.whereat.database.room.entities.AccountChannel;

import static rdsol.whereat.netwox.HandleRequests.getFollowingChannelAcccounts;

public class HomeAccountProcess extends AsyncTask<Void,Void, List<AccountChannel>> {
    private HttpGetAccountHomeFeedData httpGetAccountData;

    public HomeAccountProcess ( HttpGetAccountHomeFeedData httpGetAccountData ) {
        this.httpGetAccountData = httpGetAccountData;
    }

    @Override
    protected List<AccountChannel> doInBackground ( Void... voids ) {
        return getFollowingChannelAcccounts();
    }

    @Override
    protected void onPostExecute ( List<AccountChannel> dbAccountChannelData ) {
        super.onPostExecute( dbAccountChannelData );
        httpGetAccountData.onCompleteResponse( dbAccountChannelData );
    }
}
