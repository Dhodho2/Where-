package rdsol.whereat.pojos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import rdsol.whereat.callbacks.HttpReqCallCommentsBackData;
import rdsol.whereat.database.RoomDB;
import rdsol.whereat.database.room.entities.DBCommentMessage;
import rdsol.whereat.processes.GetCommentsProcess;

public class CommentsMessagesDataModel extends AndroidViewModel implements HttpReqCallCommentsBackData {

    private final DBCommentMessage.CommentMessageDOA liveData;
    RoomDB roomDB;

    public CommentsMessagesDataModel ( @NonNull Application application ) {
        super( application );
        roomDB = RoomDB.getRoomInstance( application );
        liveData = roomDB.getTable();
        roomDB.getTable().clearTable();
       // lastComments(  );
    }

    public void lastComments ( int media_id  ) {
        GetCommentsProcess getCommentsProcess = new GetCommentsProcess( CommentsMessagesDataModel.this );
        getCommentsProcess.execute( media_id + "" );
    }

    public DBCommentMessage.CommentMessageDOA getLiveData ( ) {
        return liveData;
    }

    @Override
    public void onCompleteResponse ( List<DBCommentMessage> objects ) {
        if(objects.size() > 0){
            roomDB.getTable().clearTable();
        for ( DBCommentMessage comment :     objects ) {

            roomDB.getTable().save( comment );
        }}

    }
}
