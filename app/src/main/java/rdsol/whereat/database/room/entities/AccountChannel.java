package rdsol.whereat.database.room.entities;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

@Entity (tableName = "account_c")
public class AccountChannel {


    @PrimaryKey (autoGenerate = true)
    private int id;
    private int id_row;
    private String username , full_name ,image_url,user_id;

    @Dao
    public interface AccountChannelDOA{
        @Insert
        void save ( AccountChannel... imts );

        @Query ("SELECT * FROM account_c")
        LiveData<List<AccountChannel>> getAll ( );

        @Query ("DELETE  FROM account_c")
        void clearTable();

        @Query ("SELECT count(id) AS counter FROM account_c")
        int count ( );

    }

    public AccountChannel ( int id_row, String username, String full_name, String image_url,String user_id ) {
        this.id_row = id_row;
        this.username = username;
        this.full_name = full_name;
        this.user_id = user_id;
        this.image_url = image_url;
    }

    public String getUser_id ( ) {
        return user_id;
    }

    public void setUser_id ( String user_id ) {
        this.user_id = user_id;
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

    public String getUsername ( ) {
        return username;
    }

    public void setUsername ( String username ) {
        this.username = username;
    }

    public String getFull_name ( ) {
        return full_name;
    }

    public void setFull_name ( String full_name ) {
        this.full_name = full_name;
    }

    public String getImage_url ( ) {
        return image_url;
    }

    public void setImage_url ( String image_url ) {
        this.image_url = image_url;
    }
}
