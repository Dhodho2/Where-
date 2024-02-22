package rdsol.whereat.processes;

import android.os.AsyncTask;

import rdsol.whereat.callbacks.HttpReqCallBack;

import static rdsol.whereat.netwox.HandleRequests.submitShowSchedule;

public class SubmitShowScheduleProcess extends AsyncTask <String,Void,String> {
    private HttpReqCallBack httpReqCallBack;

    public SubmitShowScheduleProcess ( HttpReqCallBack httpReqCallBack ) {
        this.httpReqCallBack = httpReqCallBack;
    }

    @Override
    protected String doInBackground ( String... s ) {
        return submitShowSchedule(s[0] , s[1] , s[2] , s[3] , s[4] , s[5]  );
    }

    @Override
    protected void onPostExecute ( String s ) {
        super.onPostExecute( s );
        httpReqCallBack.onCompleteResponse( s );;
    }
}
