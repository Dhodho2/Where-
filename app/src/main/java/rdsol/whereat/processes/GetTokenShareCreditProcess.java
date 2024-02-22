package rdsol.whereat.processes;

import android.os.AsyncTask;

import static rdsol.whereat.netwox.HandleRequests.shareCreditUsingLink;

public class GetTokenShareCreditProcess extends AsyncTask<String,Void,String> {
    private getSharedCreditLink getSharedCreditLink;


    public GetTokenShareCreditProcess ( GetTokenShareCreditProcess.getSharedCreditLink getSharedCreditLink ) {
        this.getSharedCreditLink = getSharedCreditLink;
    }

    @Override
    protected String doInBackground ( String... strings ) {
        return shareCreditUsingLink(strings[0]);
    }

    @Override
    protected void onPostExecute ( String s ) {
        super.onPostExecute( s );
        getSharedCreditLink .onDoneSharebleLink( s );
    }

    public interface getSharedCreditLink{
        void onDoneSharebleLink(String link);
    }
}
