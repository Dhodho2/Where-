package rdsol.whereat.callbacks;

import java.util.List;

import rdsol.whereat.database.room.entities.DBCommentMessage;
import rdsol.whereat.database.room.entities.DBHomeFeed;

public interface HttpReqCallCommentsBackData {
    void onCompleteResponse( List<DBCommentMessage> objects );
}
