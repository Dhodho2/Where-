package rdsol.whereat.processes;

import android.os.AsyncTask;

import static rdsol.whereat.netwox.HandleRequests.getRandomStreamLink;

public class GenarateStreamingLinkProcess extends AsyncTask<Void,Void,String[]> {

    private HttpGetStreamingData httpGetStreamingData ;

    public GenarateStreamingLinkProcess ( HttpGetStreamingData httpGetStreamingData ) {
        this.httpGetStreamingData = httpGetStreamingData;
    }

    @Override
    protected String[] doInBackground ( Void... voids ) {
        return getRandomStreamLink();
    }

    @Override
    protected void onPostExecute ( String[] strings ) {
        super.onPostExecute( strings );
        httpGetStreamingData.onDoneGettingStreamingLink( strings );
    }

    public interface HttpGetStreamingData{
        void onDoneGettingStreamingLink(String [] response);
    }
}
