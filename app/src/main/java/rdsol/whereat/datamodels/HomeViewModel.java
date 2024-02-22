package rdsol.whereat.datamodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import rdsol.whereat.callbacks.HttpReqCallBackData;
import rdsol.whereat.database.RoomDB;
import rdsol.whereat.database.room.entities.DBHomeFeed;
import rdsol.whereat.processes.GetHomeFeedProcess;

public class HomeViewModel extends AndroidViewModel implements HttpReqCallBackData {
    private final DBHomeFeed.DBHomeFeedDOA liveData;
    private RoomDB roomDB;

    public HomeViewModel ( @NonNull Application application ) {
        super( application );
        roomDB = RoomDB.getRoomInstance( application );

        liveData = roomDB.getTables();
        getHomeFeedData();
    }


    public DBHomeFeed.DBHomeFeedDOA getLiveData ( ) {
        return liveData;
    }


    public void getHomeFeedData ( ) {
        GetHomeFeedProcess getHomeFeedProcess = new GetHomeFeedProcess( HomeViewModel.this );
        getHomeFeedProcess.execute();
    }

    @Override
    public void onCompleteResponse ( List<DBHomeFeed> objects ) {
        if ( objects != null && objects.size() > 0 ) {
            roomDB.getTables().clearTable();
        }
        for ( DBHomeFeed items :
                objects ) {
            roomDB.getTables().save( items );
        }
    }
}