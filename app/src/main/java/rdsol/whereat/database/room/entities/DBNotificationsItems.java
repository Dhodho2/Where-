package rdsol.whereat.database.room.entities;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

@Entity (tableName = "notifications_all")
public class DBNotificationsItems {

    @Dao
    public interface DBNotificationsItemsDOA {
        @Insert
        void save ( DBNotificationsItems... imts );

        @Query ("SELECT * FROM notifications_all WHERE is_seen = 0")
        LiveData<List<DBNotificationsItems>> getAllUnSeen ( );

        @Query ("SELECT * FROM notifications_all")
        LiveData<List<DBNotificationsItems>> getAll ( );

        @Query ("DELETE  FROM notifications_all")
        void clearTable();

        @Query ("SELECT count(id) AS counter FROM notifications_all")
        int count ( );

    }
    @PrimaryKey (autoGenerate = true)
    private int id;
    private int id_rows,TYPE,is_image,is_schedule,is_random_live,is_seen;

    private String media_id , post_title,description,media,profile_image,post_by_username,post_by_full_name ,price,ondate,start_end_time;

    public DBNotificationsItems ( int id_rows, int TYPE, int is_image,
                                  int is_schedule, int is_random_live,
                                  String media_id, String post_title,
                                  String description, String media,
                                  String profile_image, String post_by_username,
                                  String post_by_full_name, String price,
                                  String ondate, String start_end_time,int is_seen ) {
        this.id_rows = id_rows;
        this.is_seen = is_seen;
        this.TYPE = TYPE;
        this.is_image = is_image;
        this.is_schedule = is_schedule;
        this.is_random_live = is_random_live;
        this.media_id = media_id;
        this.post_title = post_title;
        this.description = description;
        this.media = media;
        this.profile_image = profile_image;
        this.post_by_username = post_by_username;
        this.post_by_full_name = post_by_full_name;
        this.price = price;
        this.ondate = ondate;
        this.start_end_time = start_end_time;
    }

    public int getIs_seen ( ) {
        return is_seen;
    }

    public void setIs_seen ( int is_seen ) {
        this.is_seen = is_seen;
    }

    public int getId ( ) {
        return id;
    }

    public void setId ( int id ) {
        this.id = id;
    }

    public int getId_rows ( ) {
        return id_rows;
    }

    public void setId_rows ( int id_rows ) {
        this.id_rows = id_rows;
    }

    public int getTYPE ( ) {
        return TYPE;
    }

    public void setTYPE ( int TYPE ) {
        this.TYPE = TYPE;
    }

    public int getIs_image ( ) {
        return is_image;
    }

    public void setIs_image ( int is_image ) {
        this.is_image = is_image;
    }

    public int getIs_schedule ( ) {
        return is_schedule;
    }

    public void setIs_schedule ( int is_schedule ) {
        this.is_schedule = is_schedule;
    }

    public int getIs_random_live ( ) {
        return is_random_live;
    }

    public void setIs_random_live ( int is_random_live ) {
        this.is_random_live = is_random_live;
    }

    public String getMedia_id ( ) {
        return media_id;
    }

    public void setMedia_id ( String media_id ) {
        this.media_id = media_id;
    }

    public String getPost_title ( ) {
        return post_title;
    }

    public void setPost_title ( String post_title ) {
        this.post_title = post_title;
    }

    public String getDescription ( ) {
        return description;
    }

    public void setDescription ( String description ) {
        this.description = description;
    }

    public String getMedia ( ) {
        return media;
    }

    public void setMedia ( String media ) {
        this.media = media;
    }

    public String getProfile_image ( ) {
        return profile_image;
    }

    public void setProfile_image ( String profile_image ) {
        this.profile_image = profile_image;
    }

    public String getPost_by_username ( ) {
        return post_by_username;
    }

    public void setPost_by_username ( String post_by_username ) {
        this.post_by_username = post_by_username;
    }

    public String getPost_by_full_name ( ) {
        return post_by_full_name;
    }

    public void setPost_by_full_name ( String post_by_full_name ) {
        this.post_by_full_name = post_by_full_name;
    }

    public String getPrice ( ) {
        return price;
    }

    public void setPrice ( String price ) {
        this.price = price;
    }

    public String getOndate ( ) {
        return ondate;
    }

    public void setOndate ( String ondate ) {
        this.ondate = ondate;
    }

    public String getStart_end_time ( ) {
        return start_end_time;
    }

    public void setStart_end_time ( String start_end_time ) {
        this.start_end_time = start_end_time;
    }
}
