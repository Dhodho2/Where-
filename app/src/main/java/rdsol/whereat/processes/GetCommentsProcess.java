package rdsol.whereat.processes;

import android.os.AsyncTask;

import java.util.List;

import rdsol.whereat.callbacks.HttpReqCallCommentsBackData;
import rdsol.whereat.database.room.entities.DBCommentMessage;

import static rdsol.whereat.netwox.HandleRequests.getComments;

public class GetCommentsProcess extends AsyncTask<String ,Void, List<DBCommentMessage>> {

    private HttpReqCallCommentsBackData httpReqCallCommentsBackData;

    public GetCommentsProcess ( HttpReqCallCommentsBackData httpReqCallCommentsBackData ) {
        this.httpReqCallCommentsBackData = httpReqCallCommentsBackData;
    }

    @Override
    protected List<DBCommentMessage> doInBackground ( String... strings ) {
        return getComments( Integer.parseInt( strings[0].isEmpty() ? "1" : strings[0] ) );
    }

    @Override
    protected void onPostExecute ( List<DBCommentMessage> dbCommentMessages ) {
        super.onPostExecute( dbCommentMessages );
        httpReqCallCommentsBackData.onCompleteResponse( dbCommentMessages );
    }
}
