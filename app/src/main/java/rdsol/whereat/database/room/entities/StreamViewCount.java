package rdsol.whereat.database.room.entities;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

@Entity (tableName = "count_stream")
public class StreamViewCount {

    @Dao
    public interface StreamViewCountDOA{
        @Insert
        void save ( StreamViewCount... count_stream );

        @Query ("SELECT * FROM count_stream")
        LiveData<List<StreamViewCount>> getAll ( );

        @Query ("DELETE  FROM count_stream")
        void clearTable();

        @Query ("SELECT count(id) AS counter FROM count_stream")
        int count ( );

    }
    @PrimaryKey (autoGenerate = true)
    private int id;

    private int id_row ,count;


    public StreamViewCount ( int id_row, int count ) {
        this.id_row = id_row;
        this.count = count;
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

    public int getCount ( ) {
        return count;
    }

    public void setCount ( int count ) {
        this.count = count;
    }
}
