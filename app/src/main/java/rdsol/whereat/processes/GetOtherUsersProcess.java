package rdsol.whereat.processes;

import android.os.AsyncTask;

import java.util.List;

import rdsol.whereat.database.room.entities.AccountChannel;

import static rdsol.whereat.netwox.HandleRequests.getOtherUsersAccounts;

public class GetOtherUsersProcess extends AsyncTask<Void,Void, List<AccountChannel>> {
private HttpReqGetOtherUsers httpReqGetOtherUsers;

    public GetOtherUsersProcess ( HttpReqGetOtherUsers httpReqGetOtherUsers ) {
        this.httpReqGetOtherUsers = httpReqGetOtherUsers;
    }

    @Override
    protected List<AccountChannel> doInBackground ( Void... voids ) {
        return getOtherUsersAccounts();
    }


    @Override
    protected void onPostExecute ( List<AccountChannel> dbAccountCData ) {
        super.onPostExecute( dbAccountCData );
        //Log.e( "xxxx", "onPostExecute: " + dbAccountCData.size());
        httpReqGetOtherUsers.onFinnishedFoundResults(dbAccountCData  );
    }


   public interface HttpReqGetOtherUsers{
        void onFinnishedFoundResults(List<AccountChannel> list );
    }
}
