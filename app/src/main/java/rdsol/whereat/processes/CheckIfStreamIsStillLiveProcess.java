package rdsol.whereat.processes;

import android.os.AsyncTask;

import static rdsol.whereat.netwox.HandleRequests.updateStreamData;

public class CheckIfStreamIsStillLiveProcess extends AsyncTask<String,Void,String> {


    private HttpCheckIfStreamIsSTillLive httpCheckIfStreamIsSTillLive ;

    public CheckIfStreamIsStillLiveProcess ( HttpCheckIfStreamIsSTillLive httpCheckIfStreamIsSTillLive ) {
        this.httpCheckIfStreamIsSTillLive = httpCheckIfStreamIsSTillLive;
    }

    @Override
    protected String doInBackground ( String... strings ) {
        return updateStreamData(strings[0]);
    }

    @Override
    protected void onPostExecute ( String s ) {
        super.onPostExecute( s );
        httpCheckIfStreamIsSTillLive.onDoneCheckingStreamLivenessResult( s );
    }

    public interface HttpCheckIfStreamIsSTillLive{
        void onDoneCheckingStreamLivenessResult(String resposne);
    }

}
