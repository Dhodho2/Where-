package rdsol.whereat.database.room.entities;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

@Entity (tableName = "direct_messages")
public class DBMessages {

    private String message , dated,from_username,from_full_name ,profile_image;
    private int id_rows,from_id_user,to_id_user,is_sent,is_seen_by_to_id_user,to_is_deleted;

    @PrimaryKey (autoGenerate = true)
    private int id;
    @Dao
    public interface DBMessagesDOA{
        @Insert
        void save ( DBMessages... imts );

        @Query ("UPDATE direct_messages SET is_seen_by_to_id_user = 1  WHERE id_rows =:id_rows")
        void setMessageAsSeen ( int id_rows );

        @Query ("SELECT * FROM direct_messages WHERE is_seen_by_to_id_user = 0 AND  from_id_user !=:notmeID")
        LiveData<List<DBMessages>> getAllUnRead (String notmeID );

        @Query ("SELECT * FROM direct_messages WHERE from_id_user !=:notmeID")
        LiveData<List<DBMessages>> getAll (String notmeID );


       @Query ("SELECT * FROM direct_messages  WHERE from_id_user =:from_id_user OR to_id_user =:meID")
        LiveData<List<DBMessages>> getAllChatsFor (String from_id_user ,String meID);

        @Query ("DELETE  FROM direct_messages")
        void clearTable();

        @Query ("SELECT count(id) AS counter FROM direct_messages")
        int count ( );

    }
    @Ignore
    public DBMessages ( ) {
    }

    public DBMessages ( String message, String dated, String from_username, String from_full_name, String profile_image, int id_rows, int from_id_user, int to_id_user, int is_sent, int is_seen_by_to_id_user, int to_is_deleted ) {
        this.message = message;
        this.dated = dated;
        this.from_username = from_username;
        this.from_full_name = from_full_name;
        this.profile_image = profile_image;
        this.id_rows = id_rows;
        this.from_id_user = from_id_user;
        this.to_id_user = to_id_user;
        this.is_sent = is_sent;
        this.is_seen_by_to_id_user = is_seen_by_to_id_user;
        this.to_is_deleted = to_is_deleted;
    }

    public String getMessage ( ) {
        return message;
    }

    public void setMessage ( String message ) {
        this.message = message;
    }

    public String getDated ( ) {
        return dated;
    }

    public void setDated ( String dated ) {
        this.dated = dated;
    }

    public String getFrom_username ( ) {
        return from_username;
    }

    public void setFrom_username ( String from_username ) {
        this.from_username = from_username;
    }

    public String getFrom_full_name ( ) {
        return from_full_name;
    }

    public void setFrom_full_name ( String from_full_name ) {
        this.from_full_name = from_full_name;
    }

    public String getProfile_image ( ) {
        return profile_image;
    }

    public void setProfile_image ( String profile_image ) {
        this.profile_image = profile_image;
    }

    public int getId_rows ( ) {
        return id_rows;
    }

    public void setId_rows ( int id_rows ) {
        this.id_rows = id_rows;
    }

    public int getFrom_id_user ( ) {
        return from_id_user;
    }

    public void setFrom_id_user ( int from_id_user ) {
        this.from_id_user = from_id_user;
    }

    public int getTo_id_user ( ) {
        return to_id_user;
    }

    public void setTo_id_user ( int to_id_user ) {
        this.to_id_user = to_id_user;
    }

    public int getIs_sent ( ) {
        return is_sent;
    }

    public void setIs_sent ( int is_sent ) {
        this.is_sent = is_sent;
    }

    public int getIs_seen_by_to_id_user ( ) {
        return is_seen_by_to_id_user;
    }

    public void setIs_seen_by_to_id_user ( int is_seen_by_to_id_user ) {
        this.is_seen_by_to_id_user = is_seen_by_to_id_user;
    }

    public int getTo_is_deleted ( ) {
        return to_is_deleted;
    }

    public void setTo_is_deleted ( int to_is_deleted ) {
        this.to_is_deleted = to_is_deleted;
    }

    public int getId ( ) {
        return id;
    }

    public void setId ( int id ) {
        this.id = id;
    }
}
