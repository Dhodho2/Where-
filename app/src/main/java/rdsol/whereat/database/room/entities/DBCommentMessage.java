package rdsol.whereat.database.room.entities;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

/**
 * Created by madhur on 17/01/15.
 */

@Entity (tableName = "comments")
public class DBCommentMessage {
    @PrimaryKey (autoGenerate = true)
    private int id;
    private int id_media_post;
    private String messageText,NameOfPersonChat ;
    private String userType;
    private String messageStatus,dated;

    public String getDated ( ) {
        return dated;
    }

    public void setDated ( String dated ) {
        this.dated = dated;
    }

    public String getNameOfPersonChat ( ) {
        return NameOfPersonChat;
    }

    public void setNameOfPersonChat ( String nameOfPersonChat ) {
        NameOfPersonChat = nameOfPersonChat;
    }

    public int getId_media_post ( ) {
        return id_media_post;
    }

    public void setId_media_post ( int id_media_post ) {
        this.id_media_post = id_media_post;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    private long messageTime;

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getMessageText() {

        return messageText;
    }

    public int getId ( ) {
        return id;
    }

    public void setId ( int id ) {
        this.id = id;
    }

    public String getUserType() {
        return userType;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    @Dao
    public interface CommentMessageDOA{
        @Insert
        void save ( DBCommentMessage... imts );

        @Query ("SELECT * FROM comments WHERE id_media_post= :id_media_post")
        LiveData<List<DBCommentMessage>> getAll ( int id_media_post );
        @Query ("SELECT * FROM comments")
        LiveData<List<DBCommentMessage>> getAll ( );

        @Query ("DELETE  FROM comments")
        void clearTable();

        @Query ("SELECT count(id) AS counter FROM comments")
        int count ( );

    }
}
