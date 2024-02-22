package rdsol.whereat.processes;

import android.os.AsyncTask;

import rdsol.whereat.callbacks.HttpReqCallBack;

import static rdsol.whereat.netwox.HandleRequests.uploadProFilePicture;

public class UploadProFilePictureProcess extends AsyncTask<String,Void,String> {
    private HttpReqCallBack httpReqCallBack;

    public UploadProFilePictureProcess ( HttpReqCallBack httpReqCallBack ) {
        this.httpReqCallBack = httpReqCallBack;
    }

    @Override
    protected String doInBackground ( String... strings ) {
        return uploadProFilePicture(strings[0]);
    }

    @Override
    protected void onPostExecute ( String s ) {
        super.onPostExecute( s );
        httpReqCallBack.onCompleteResponse( s );;
    }
}
