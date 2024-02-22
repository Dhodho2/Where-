package rdsol.whereat.processes;

import android.os.AsyncTask;

import static rdsol.whereat.netwox.HandleRequests.setNotificationAsSeen;

public class UpdateNotificationAsSeen extends AsyncTask<String ,Void,String> {
    @Override
    protected String doInBackground ( String... strings ) {
        return setNotificationAsSeen(strings[0]);
    }


    @Override
    protected void onPostExecute ( String s ) {
        super.onPostExecute( s );
    }
}
