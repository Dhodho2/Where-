package rdsol.whereat.processes;

import android.os.AsyncTask;

import rdsol.whereat.callbacks.HttpReqCallBack;

import static rdsol.whereat.netwox.HandleRequests.registerNewUser;

public class RegisterUserProcess extends AsyncTask<String,Void,String> {

    private HttpReqCallBack httpReqCallBack;

    public RegisterUserProcess ( HttpReqCallBack httpReqCallBack ) {
        this.httpReqCallBack = httpReqCallBack;
    }

    @Override
    protected String doInBackground ( String... strings ) {
        return registerNewUser(strings[0] , strings[1],strings[2] , strings[3],strings[4] , strings[5]);

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