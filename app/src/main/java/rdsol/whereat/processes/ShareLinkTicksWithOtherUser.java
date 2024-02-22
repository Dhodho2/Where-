package rdsol.whereat.processes;

import android.os.AsyncTask;

import static rdsol.whereat.netwox.HandleRequests.shareCreditWithOtherUser;

public class ShareLinkTicksWithOtherUser extends AsyncTask<String,Void,String> {
    private HttpShareLinkTicksToTothers httpShareLinkTicksToTothers ;


    public ShareLinkTicksWithOtherUser ( HttpShareLinkTicksToTothers httpShareLinkTicksToTothers ) {
        this.httpShareLinkTicksToTothers = httpShareLinkTicksToTothers;
    }

    @Override
    protected String doInBackground ( String... s ) {
        return shareCreditWithOtherUser(s[0] , s[1]);
    }

    @Override
    protected void onPostExecute ( String s ) {
        super.onPostExecute( s );
        httpShareLinkTicksToTothers.onDoneSharingCredit( s );
    }

    public interface HttpShareLinkTicksToTothers{
        void onDoneSharingCredit(String response);
    }
}
