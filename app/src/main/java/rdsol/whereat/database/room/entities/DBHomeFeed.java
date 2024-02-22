package rdsol.whereat.database.room.entities;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

@Entity (tableName = "home_feed")
public class DBHomeFeed {

    @PrimaryKey (autoGenerate = true)
    private int id;

    private int id_row;
    private int liked , comments_count ,is_schedule,is_image,likes_count ,check_if_following;

    private String title,video_url,video_thumbnail_image,dated,description,owner,owner_avatar , account_full_name , account_name ,rating;
    String ondate,  price,start_end_time;



    public DBHomeFeed (  int id_row, String title, String video_url, String video_thumbnail_image,
                         String dated, String description, String owner ,String owner_avatar,int liked ,
                         int comments_count ,String account_full_name , String account_name,int is_schedule,
                         int is_image ,String ondate ,String price,String start_end_time ,int likes_count ,int check_if_following,String rating ) {

        this.ondate = ondate;
        this.price = price;
        this.rating = rating;
        this.likes_count = likes_count;
        this.start_end_time = start_end_time;
        this.id_row = id_row;
        this.title = title;
        this.video_url = video_url;
        this.video_thumbnail_image = video_thumbnail_image;
        this.dated = dated;
        this.description = description;
        this.owner = owner;
        this.owner_avatar = owner_avatar;
        this.liked = liked;
        this.comments_count = comments_count;
        this.account_full_name = account_full_name;
        this.account_name = account_name;
        this.is_schedule = is_schedule;
        this.is_image = is_image;
        this.check_if_following = check_if_following;
    }

    public String getRating ( ) {
        return rating;
    }

    public void setRating ( String rating ) {
        this.rating = rating;
    }

    public int getCheck_if_following ( ) {
        return check_if_following;
    }

    public void setCheck_if_following ( int check_if_following ) {
        this.check_if_following = check_if_following;
    }

    public int getLikes_count ( ) {
        return likes_count;
    }

    public void setLikes_count ( int likes_count ) {
        this.likes_count = likes_count;
    }

    public int getIs_schedule ( ) {
        return is_schedule;
    }

    public void setIs_schedule ( int is_schedule ) {
        this.is_schedule = is_schedule;
    }

    public int getIs_image ( ) {
        return is_image;
    }

    public void setIs_image ( int is_image ) {
        this.is_image = is_image;
    }

    public String getOndate ( ) {
        return ondate;
    }

    public void setOndate ( String ondate ) {
        this.ondate = ondate;
    }

    public String getPrice ( ) {
        return price;
    }

    public void setPrice ( String price ) {
        this.price = price;
    }

    public String getStart_end_time ( ) {
        return start_end_time;
    }

    public void setStart_end_time ( String start_end_time ) {
        this.start_end_time = start_end_time;
    }

    public String getAccount_full_name ( ) {
        return account_full_name;
    }

    public void setAccount_full_name ( String account_full_name ) {
        this.account_full_name = account_full_name;
    }

    public String getAccount_name ( ) {
        return account_name;
    }

    public void setAccount_name ( String account_name ) {
        this.account_name = account_name;
    }

    public int getComments_count ( ) {
        return comments_count;
    }

    public void setComments_count ( int comments_count ) {
        this.comments_count = comments_count;
    }

    public int getLiked ( ) {
        return liked;
    }

    public void setLiked ( int liked ) {
        this.liked = liked;
    }

    public String getOwner_avatar ( ) {
        return owner_avatar;
    }

    public void setOwner_avatar ( String owner_avatar ) {
        this.owner_avatar = owner_avatar;
    }

    public int getId_row ( ) {
        return id_row;
    }

    public void setId_row ( int id_row ) {
        this.id_row = id_row;
    }

    public int getId ( ) {
        return id;
    }

    public void setId ( int id ) {
        this.id = id;
    }

    public String getTitle ( ) {
        return title;
    }

    public void setTitle ( String title ) {
        this.title = title;
    }

    public String getVideo_url ( ) {
        return video_url;
    }

    public void setVideo_url ( String video_url ) {
        this.video_url = video_url;
    }

    public String getVideo_thumbnail_image ( ) {
        return video_thumbnail_image;
    }

    public void setVideo_thumbnail_image ( String video_thumbnail_image ) {
        this.video_thumbnail_image = video_thumbnail_image;
    }

    public String getDated ( ) {
        return dated;
    }

    public void setDated ( String dated ) {
        this.dated = dated;
    }

    public String getDescription ( ) {
        return description;
    }

    public void setDescription ( String description ) {
        this.description = description;
    }

    public String getOwner ( ) {
        return owner;
    }

    public void setOwner ( String owner ) {
        this.owner = owner;
    }
    @Dao
    public interface DBHomeFeedDOA{
        @Insert
        void save ( DBHomeFeed... imts );

        @Query ("SELECT * FROM home_feed")
        LiveData<List<DBHomeFeed>> getAll ( );
        @Query ("SELECT * FROM home_feed  WHERE is_schedule= '1'")
        LiveData<List<DBHomeFeed>> getAllScheduledOnly ();

        @Query ("DELETE  FROM home_feed")
        void clearTable();

        @Query ("SELECT count(id) AS counter FROM home_feed")
        int count ( );

    }
}
