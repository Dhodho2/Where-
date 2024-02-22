package rdsol.whereat.callbacks;

import java.util.List;

import rdsol.whereat.database.room.entities.DBHomeFeed;

public interface HttpReqCallBackData {
    void onCompleteResponse( List<DBHomeFeed> objects );
}
