package rdsol.whereat.processes;

import android.os.AsyncTask;

import rdsol.whereat.callbacks.HttpReqCallBack;

import static rdsol.whereat.netwox.HandleRequests.logInUser;

public class LoginProcess extends AsyncTask <String,Void,String>{

    private HttpReqCallBack httpReqCallBack;

    public LoginProcess ( HttpReqCallBack httpReqCallBack ) {
        this.httpReqCallBack = httpReqCallBack;
    }

    @Override
    protected String doInBackground ( String... strings ) {
        return logInUser(strings[0] , strings[1]);

       /* try {
            Thread.sleep( 8_000 );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
        return "success";*/
    }

    @Override
    protected void onPostExecute ( String s ) {
        super.onPostExecute( s );
        httpReqCallBack.onCompleteResponse( s );
    }
}
