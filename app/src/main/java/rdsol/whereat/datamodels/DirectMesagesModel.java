package rdsol.whereat.datamodels;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import rdsol.whereat.database.RoomDB;
import rdsol.whereat.database.room.entities.DBMessages;

import static rdsol.whereat.netwox.HandleRequests.getMyDirectMessages;

public class DirectMesagesModel extends AndroidViewModel  {
    private final DBMessages.DBMessagesDOA liveData;
    private RoomDB roomDB;

    public DirectMesagesModel ( @NonNull Application application ) {
        super( application );
        roomDB = RoomDB.getRoomInstance( application );

        liveData = roomDB.getTableDirectMessages();
        getData();
    }


    public DBMessages.DBMessagesDOA getLiveData ( ) {
        return liveData;
    }


    public void getData ( ) {
        new AsyncTask<Void, Void, List<DBMessages>>(){
            @Override
            protected List<DBMessages> doInBackground ( Void... voids ) {
                return getMyDirectMessages();
            }
        }.execute(  );
    }

}