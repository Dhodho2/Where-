package rdsol.whereat.datamodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import rdsol.whereat.database.RoomDB;
import rdsol.whereat.database.room.entities.DBNotificationsItems;
import rdsol.whereat.processes.GetNotifications;

public class NotificationViewModel extends AndroidViewModel implements GetNotifications.HttpgetNotifications {
    private final DBNotificationsItems.DBNotificationsItemsDOA liveData;
    private RoomDB roomDB;

    public NotificationViewModel ( @NonNull Application application ) {
        super( application );
        roomDB = RoomDB.getRoomInstance( application );

        liveData = roomDB.getTableNotications();

    }

    public void getData ( ) {
        GetNotifications getNotifications = new GetNotifications( NotificationViewModel.this );
        getNotifications.execute();
    }

    public DBNotificationsItems.DBNotificationsItemsDOA getLiveData ( ) {
        return liveData;

    }


    @Override
    public void onDoneResultNotifications ( List<DBNotificationsItems> items ) {

        if ( items != null ) {
            roomDB.getTableNotications().clearTable();
            for ( DBNotificationsItems item : items
            ) {
             //   Log.e( "xxxx", "onDoneResultNotifications: " + item.getId_rows());
                roomDB.getTableNotications().save( item );
            }
        }
    }
}
