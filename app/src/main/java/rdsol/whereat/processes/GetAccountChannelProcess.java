package rdsol.whereat.processes;

import android.os.AsyncTask;

import java.util.List;

import rdsol.whereat.callbacks.HttpGetAccountData;
import rdsol.whereat.database.room.entities.DBAccountChannelData;

import static rdsol.whereat.netwox.HandleRequests.getChannelAcccountData;

public class GetAccountChannelProcess extends AsyncTask<String,Void, List<DBAccountChannelData>> {


    private HttpGetAccountData httpGetAccountData ;

    public GetAccountChannelProcess ( HttpGetAccountData httpGetAccountData ) {
        this.httpGetAccountData = httpGetAccountData;
    }

    @Override
    protected List<DBAccountChannelData> doInBackground ( String... strings ) {
        return getChannelAcccountData(strings[0]);
    }

    @Override
    protected void onPostExecute ( List<DBAccountChannelData> dbAccountChannelData ) {
        super.onPostExecute( dbAccountChannelData );
        httpGetAccountData.onCompleteResponse( dbAccountChannelData );
    }
}
