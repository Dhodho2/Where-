package rdsol.whereat.processes;

import android.os.AsyncTask;

import rdsol.whereat.database.room.entities.StreamViewCount;

import static rdsol.whereat.netwox.HandleRequests.getStreamViewsCount;

public class GetViewsCountForStreamProcess extends AsyncTask<String,Void,StreamViewCount> {
    private  HttpGetViewsCounter httpGetViewsCounter;

    public GetViewsCountForStreamProcess ( HttpGetViewsCounter httpGetViewsCounter ) {
        this.httpGetViewsCounter = httpGetViewsCounter;
    }

    @Override
    protected StreamViewCount doInBackground ( String... strings ) {
        return getStreamViewsCount(strings[0]);
    }

    @Override
    protected void onPostExecute ( StreamViewCount s ) {
        super.onPostExecute( s );
        httpGetViewsCounter.onResultDone( s );
    }

    public interface HttpGetViewsCounter {
        void onResultDone(StreamViewCount counter);
    }
}
