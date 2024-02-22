package rdsol.whereat.processes;

import android.os.AsyncTask;

import static rdsol.whereat.netwox.HandleRequests.setChatMessageAsSeen;

public class NotifyDataAsReadAndSeenProcess extends AsyncTask<String ,Void,String> {

    private HttpNotifyDataAsReadAndSeenCallBack seenCallBack ;


    public NotifyDataAsReadAndSeenProcess ( HttpNotifyDataAsReadAndSeenCallBack seenCallBack ) {
        this.seenCallBack = seenCallBack;
    }

    @Override
    protected String doInBackground ( String... s ) {
        return s[0].equals( "dm" ) ? setChatMessageAsSeen(s[1]) : null;
    }

    @Override
    protected void onPostExecute ( String s ) {
        super.onPostExecute( s );
        seenCallBack.onDoneDelivery( s );
    }

    public interface HttpNotifyDataAsReadAndSeenCallBack{
        void onDoneDelivery(String response);
    }
}
