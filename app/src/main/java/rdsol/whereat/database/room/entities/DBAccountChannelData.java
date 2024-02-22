package rdsol.whereat.database.room.entities;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

@Entity (tableName = "account_data")
public class DBAccountChannelData {
    @PrimaryKey (autoGenerate = true)
    private int id;
    private int id_row;
    private int liked , comments_count;
    private String image_url ,title,dated,description  ;
    @Dao
    public interface DBAccountChannelDataDOA{
        @Insert
        void save ( DBAccountChannelData... imts );

        @Query ("SELECT * FROM account_data")
        LiveData<List<DBAccountChannelData>> getAll ( );

        @Query ("DELETE  FROM account_data")
        void clearTable();

        @Query ("SELECT count(id) AS counter FROM account_data")
        int count ( );

    }

    public DBAccountChannelData ( int id_row, String image_url, String title, String dated, String description, int  liked ,int  comments_count ) {
        this.id_row = id_row;
        this.image_url = image_url;
        this.title = title;
        this.dated = dated;
        this.description = description;
        this.liked = liked;
        this.comments_count = comments_count;
    }

    public int getLiked ( ) {
        return liked;
    }

    public void setLiked ( int liked ) {
        this.liked = liked;
    }

    public int getComments_count ( ) {
        return comments_count;
    }

    public void setComments_count ( int comments_count ) {
        this.comments_count = comments_count;
    }

    public int getId ( ) {
        return id;
    }

    public void setId ( int id ) {
        this.id = id;
    }

    public int getId_row ( ) {
        return id_row;
    }

    public void setId_row ( int id_row ) {
        this.id_row = id_row;
    }

    public String getImage_url ( ) {
        return image_url;
    }

    public void setImage_url ( String image_url ) {
        this.image_url = image_url;
    }

    public String getTitle ( ) {
        return title;
    }

    public void setTitle ( String title ) {
        this.title = title;
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

}
