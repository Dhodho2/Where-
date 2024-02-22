package rdsol.whereat.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import rdsol.whereat.database.room.entities.AccountChannel;
import rdsol.whereat.database.room.entities.DBAccountChannelData;
import rdsol.whereat.database.room.entities.DBBankDetails;
import rdsol.whereat.database.room.entities.DBCommentMessage;
import rdsol.whereat.database.room.entities.DBHomeFeed;
import rdsol.whereat.database.room.entities.DBMessages;
import rdsol.whereat.database.room.entities.DBNotificationsItems;
import rdsol.whereat.database.room.entities.StreamViewCount;


@Database (entities = { DBCommentMessage.class, DBMessages.class,
        DBNotificationsItems.class, DBBankDetails.class , DBHomeFeed.class,
        StreamViewCount.class , AccountChannel.class ,
        DBAccountChannelData.class }, version = 23, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    private static volatile RoomDB INSTANCE;

    // Use this to call on any place
    public static RoomDB roomConnect ( ) {
        return INSTANCE;
    }

    public static RoomDB getRoomInstance ( final Context context ) {
        if ( INSTANCE == null ) {
            synchronized ( RoomDB.class ) {

                INSTANCE = Room.databaseBuilder( context.getApplicationContext(),
                        RoomDB.class, "data_local_at" )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }

        return INSTANCE;
    }

    public abstract DBCommentMessage.CommentMessageDOA getTable ( );
    public abstract DBHomeFeed.DBHomeFeedDOA getTables ( );
    public abstract StreamViewCount.StreamViewCountDOA getStreamTables ( );
    public abstract AccountChannel.AccountChannelDOA getTableAccountChannel ( );
    public abstract DBAccountChannelData.DBAccountChannelDataDOA getTableDBAccountChannelData ( );
    public abstract DBBankDetails.DBBankDetailsDOA getTableDBBankDetailsData ( );
    public abstract DBNotificationsItems.DBNotificationsItemsDOA getTableNotications ( );
    public abstract DBMessages.DBMessagesDOA getTableDirectMessages ( );



}
