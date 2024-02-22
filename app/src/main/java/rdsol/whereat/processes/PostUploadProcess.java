package rdsol.whereat.processes;

import android.os.AsyncTask;

import rdsol.whereat.callbacks.HttpReqCallBack;

import static rdsol.whereat.netwox.HandleRequests.uploadPost;

public class PostUploadProcess extends AsyncTask<String,Void,String> {
    private HttpReqCallBack httpReqCallBack;

    public PostUploadProcess ( HttpReqCallBack httpReqCallBack ) {
        this.httpReqCallBack = httpReqCallBack;
    }

    @Override
    protected String doInBackground ( String... s ) {
        return uploadPost(s[0],s[1],s[2]);
    }

    @Override
    protected void onPostExecute ( String s ) {
        super.onPostExecute( s );
        httpReqCallBack.onCompleteResponse( s );
    }
}
