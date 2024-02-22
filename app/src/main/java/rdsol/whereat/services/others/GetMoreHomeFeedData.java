package rdsol.whereat.services.others;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;

import rdsol.whereat.callbacks.HttpReqCallBackData;
import rdsol.whereat.database.RoomDB;
import rdsol.whereat.database.room.entities.DBHomeFeed;
import rdsol.whereat.processes.GetHomeFeedProcess;

public class GetMoreHomeFeedData extends Worker implements HttpReqCallBackData {

    private GetHomeFeedProcess getHomeFeedProcess ;
private RoomDB  roomDB ;

    public GetMoreHomeFeedData ( @NonNull Context context, @NonNull WorkerParameters workerParams ) {
        super( context, workerParams );
        getHomeFeedProcess = new GetHomeFeedProcess( GetMoreHomeFeedData.this  );
          roomDB = RoomDB.getRoomInstance( context );
        getHomeFeedProcess.execute(  );
    }

    @NonNull
    @Override
    public Result doWork ( ) {

        return Result.success();
    }

    @Override
    public void onCompleteResponse ( List<DBHomeFeed> objects ) {
        if(objects != null && objects.size()>0){
            roomDB.getTables().clearTable();
        }

        for ( DBHomeFeed items:
                objects ) {
            roomDB.getTables().save( items );
        }
    }
}
