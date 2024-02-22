package rdsol.whereat.services.others;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import rdsol.whereat.database.RoomDB;
import rdsol.whereat.database.room.entities.StreamViewCount;
import rdsol.whereat.processes.GetViewsCountForStreamProcess;

import static rdsol.whereat.activities.ViewWatchRead.MEDIA_ID_ROW;

public class GetStreamViewsDataService  extends Worker implements  GetViewsCountForStreamProcess.HttpGetViewsCounter {

    private GetViewsCountForStreamProcess mProcess ;
    private RoomDB roomDB ;

    public GetStreamViewsDataService ( @NonNull Context context, @NonNull WorkerParameters workerParams ) {
        super( context, workerParams );
        mProcess = new GetViewsCountForStreamProcess( GetStreamViewsDataService.this  );
        roomDB = RoomDB.getRoomInstance( context );
        mProcess.execute(  MEDIA_ID_ROW);
    }

    @NonNull
    @Override
    public Result doWork ( ) {

        return Result.success();
    }

    @Override
    public void onResultDone ( StreamViewCount objects ) {
        if ( objects != null ) {

            roomDB.getStreamTables().clearTable();
            roomDB.getStreamTables().save( objects );
        }


    }
}
