package rdsol.whereat.callbacks;

import java.util.List;

import rdsol.whereat.database.room.entities.DBAccountChannelData;
import rdsol.whereat.database.room.entities.DBHomeFeed;

public interface HttpGetAccountData {

    void onCompleteResponse( List<DBAccountChannelData> objects );
}
