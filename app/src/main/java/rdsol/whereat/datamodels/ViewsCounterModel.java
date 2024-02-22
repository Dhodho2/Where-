package rdsol.whereat.datamodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import rdsol.whereat.database.RoomDB;
import rdsol.whereat.database.room.entities.StreamViewCount;
import rdsol.whereat.processes.GetViewsCountForStreamProcess;

import static rdsol.whereat.activities.ViewWatchRead.MEDIA_ID_ROW;

public class ViewsCounterModel extends AndroidViewModel implements GetViewsCountForStreamProcess.HttpGetViewsCounter {
    private final StreamViewCount.StreamViewCountDOA liveData;
    private RoomDB roomDB;


    public ViewsCounterModel ( @NonNull Application application ) {
        super( application );
        roomDB = RoomDB.getRoomInstance( application );

        liveData = roomDB.getStreamTables();
        getData();
    }


    public StreamViewCount.StreamViewCountDOA getLiveData (  ) {

        return liveData;
    }


    public void getData ( ) {
        GetViewsCountForStreamProcess mProcess = new GetViewsCountForStreamProcess( ViewsCounterModel.this );
        mProcess.execute(MEDIA_ID_ROW);
    }



    @Override
    public void onResultDone ( StreamViewCount objects ) {
        if ( objects != null ) {
            roomDB.getStreamTables().clearTable();
            roomDB.getStreamTables().save( objects );
        }


    }
}