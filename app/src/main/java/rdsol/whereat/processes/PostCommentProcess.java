package rdsol.whereat.processes;

import android.os.AsyncTask;

import rdsol.whereat.callbacks.HttpReqCallBack;

import static rdsol.whereat.netwox.HandleRequests.postComments;

public class PostCommentProcess extends AsyncTask<String,Void,String> {

    private HttpReqCallBack httpReqCallBack;

    public PostCommentProcess ( HttpReqCallBack httpReqCallBack ) {
        this.httpReqCallBack = httpReqCallBack;
    }

    @Override
    protected String doInBackground ( String... strings ) {
        return postComments( Integer.parseInt( strings[0] ),strings[1]);
    }

    @Override
    protected void onPostExecute ( String s ) {
        super.onPostExecute( s );
        httpReqCallBack.onCompleteResponse( s );
    }
}
