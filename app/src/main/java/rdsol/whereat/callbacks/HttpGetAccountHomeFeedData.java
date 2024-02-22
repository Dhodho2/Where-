package rdsol.whereat.callbacks;

import java.util.List;

import rdsol.whereat.database.room.entities.AccountChannel;
import rdsol.whereat.database.room.entities.DBAccountChannelData;

public interface HttpGetAccountHomeFeedData {

    void onCompleteResponse( List<AccountChannel> objects );
}
