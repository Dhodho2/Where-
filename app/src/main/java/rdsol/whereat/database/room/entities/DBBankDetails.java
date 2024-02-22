package rdsol.whereat.database.room.entities;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

@Entity (tableName = "account_details")
public class DBBankDetails {
    @PrimaryKey (autoGenerate = true)
    private int id;


    @Dao
    public interface DBBankDetailsDOA{
        @Insert
        void save ( DBBankDetails... imts );

        @Query ("SELECT * FROM account_details")
        LiveData<List<DBBankDetails>> getAll ( );

        @Query ("DELETE  FROM account_details")
        void clearTable();

        @Query ("SELECT count(id) AS counter FROM account_details")
        int count ( );

    }

    private String points , lastCheckedOnDate;

    public int getId ( ) {
        return id;
    }

    public void setId ( int id ) {
        this.id = id;
    }

    public DBBankDetails ( String points, String lastCheckedOnDate ) {
        this.points = points;
        this.lastCheckedOnDate = lastCheckedOnDate;
    }

    public String getPoints ( ) {
        return points;
    }

    public void setPoints ( String points ) {
        this.points = points;
    }

    public String getLastCheckedOnDate ( ) {
        return lastCheckedOnDate;
    }

    public void setLastCheckedOnDate ( String lastCheckedOnDate ) {
        this.lastCheckedOnDate = lastCheckedOnDate;
    }
}
