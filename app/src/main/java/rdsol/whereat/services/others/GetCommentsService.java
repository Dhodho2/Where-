package rdsol.whereat.services.others;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;

import rdsol.whereat.callbacks.HttpReqCallCommentsBackData;
import rdsol.whereat.database.RoomDB;
import rdsol.whereat.database.room.entities.DBCommentMessage;
import rdsol.whereat.processes.GetCommentsProcess;

import static rdsol.whereat.activities.ViewWatchRead.MEDIA_ID_ROW;

public class GetCommentsService    extends Worker implements HttpReqCallCommentsBackData {

    private GetCommentsProcess mProcess ;
    private RoomDB roomDB ;

    public GetCommentsService ( @NonNull Context context, @NonNull WorkerParameters workerParams ) {
        super( context, workerParams );
        mProcess = new GetCommentsProcess( GetCommentsService.this  );
        roomDB = RoomDB.getRoomInstance( context );
        mProcess.execute(  MEDIA_ID_ROW);
    }

    @NonNull
    @Override
    public Result doWork ( ) {
        Log.e( "xxxx", "doWork: comments service"  );
        return Result.success();
    }


    @Override
    public void onCompleteResponse ( List<DBCommentMessage> objects ) {
        if ( objects != null ) {

            roomDB.getTable().clearTable();
            for ( DBCommentMessage obj:  objects) {
                roomDB.getTable().save( obj );
            }

        }

}
}
