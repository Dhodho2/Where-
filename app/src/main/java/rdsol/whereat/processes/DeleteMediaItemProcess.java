package rdsol.whereat.processes;

import android.os.AsyncTask;

import rdsol.whereat.callbacks.HttpReqCallBack;

import static rdsol.whereat.netwox.HandleRequests.likeDeleteMediaItem;

public class DeleteMediaItemProcess   extends AsyncTask<String,Void, String> {
    private HttpReqCallBack httpReqCallBack;

    public DeleteMediaItemProcess ( HttpReqCallBack httpReqCallBack ) {
        this.httpReqCallBack = httpReqCallBack;
    }

    @Override
    protected String doInBackground ( String... s ) {
        return likeDeleteMediaItem(s[0]);
    }

    @Override
    protected void onPostExecute (String dbHomeFeeds ) {
        super.onPostExecute( dbHomeFeeds );
        httpReqCallBack.onCompleteResponse( dbHomeFeeds );
    }
}
