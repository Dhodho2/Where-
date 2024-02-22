package rdsol.whereat.database.room.entities;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class DBCommentMessageCommentMessageDOA_Impl implements DBCommentMessage.CommentMessageDOA {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DBCommentMessage> __insertionAdapterOfDBCommentMessage;

  private final SharedSQLiteStatement __preparedStmtOfClearTable;

  public DBCommentMessageCommentMessageDOA_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDBCommentMessage = new EntityInsertionAdapter<DBCommentMessage>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `comments` (`id`,`id_media_post`,`messageText`,`NameOfPersonChat`,`userType`,`messageStatus`,`dated`,`messageTime`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DBCommentMessage value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getId_media_post());
        if (value.getMessageText() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getMessageText());
        }
        if (value.getNameOfPersonChat() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getNameOfPersonChat());
        }
        if (value.getUserType() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getUserType());
        }
        if (value.getMessageStatus() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getMessageStatus());
        }
        if (value.getDated() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getDated());
        }
        stmt.bindLong(8, value.getMessageTime());
      }
    };
    this.__preparedStmtOfClearTable = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE  FROM comments";
        return _query;
      }
    };
  }

  @Override
  public void save(final DBCommentMessage... imts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDBCommentMessage.insert(imts);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void clearTable() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfClearTable.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfClearTable.release(_stmt);
    }
  }

  @Override
  public LiveData<List<DBCommentMessage>> getAll(final int id_media_post) {
    final String _sql = "SELECT * FROM comments WHERE id_media_post= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id_media_post);
    return __db.getInvalidationTracker().createLiveData(new String[]{"comments"}, false, new Callable<List<DBCommentMessage>>() {
      @Override
      public List<DBCommentMessage> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIdMediaPost = CursorUtil.getColumnIndexOrThrow(_cursor, "id_media_post");
          final int _cursorIndexOfMessageText = CursorUtil.getColumnIndexOrThrow(_cursor, "messageText");
          final int _cursorIndexOfNameOfPersonChat = CursorUtil.getColumnIndexOrThrow(_cursor, "NameOfPersonChat");
          final int _cursorIndexOfUserType = CursorUtil.getColumnIndexOrThrow(_cursor, "userType");
          final int _cursorIndexOfMessageStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "messageStatus");
          final int _cursorIndexOfDated = CursorUtil.getColumnIndexOrThrow(_cursor, "dated");
          final int _cursorIndexOfMessageTime = CursorUtil.getColumnIndexOrThrow(_cursor, "messageTime");
          final List<DBCommentMessage> _result = new ArrayList<DBCommentMessage>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final DBCommentMessage _item;
            _item = new DBCommentMessage();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final int _tmpId_media_post;
            _tmpId_media_post = _cursor.getInt(_cursorIndexOfIdMediaPost);
            _item.setId_media_post(_tmpId_media_post);
            final String _tmpMessageText;
            _tmpMessageText = _cursor.getString(_cursorIndexOfMessageText);
            _item.setMessageText(_tmpMessageText);
            final String _tmpNameOfPersonChat;
            _tmpNameOfPersonChat = _cursor.getString(_cursorIndexOfNameOfPersonChat);
            _item.setNameOfPersonChat(_tmpNameOfPersonChat);
            final String _tmpUserType;
            _tmpUserType = _cursor.getString(_cursorIndexOfUserType);
            _item.setUserType(_tmpUserType);
            final String _tmpMessageStatus;
            _tmpMessageStatus = _cursor.getString(_cursorIndexOfMessageStatus);
            _item.setMessageStatus(_tmpMessageStatus);
            final String _tmpDated;
            _tmpDated = _cursor.getString(_cursorIndexOfDated);
            _item.setDated(_tmpDated);
            final long _tmpMessageTime;
            _tmpMessageTime = _cursor.getLong(_cursorIndexOfMessageTime);
            _item.setMessageTime(_tmpMessageTime);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<DBCommentMessage>> getAll() {
    final String _sql = "SELECT * FROM comments";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"comments"}, false, new Callable<List<DBCommentMessage>>() {
      @Override
      public List<DBCommentMessage> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIdMediaPost = CursorUtil.getColumnIndexOrThrow(_cursor, "id_media_post");
          final int _cursorIndexOfMessageText = CursorUtil.getColumnIndexOrThrow(_cursor, "messageText");
          final int _cursorIndexOfNameOfPersonChat = CursorUtil.getColumnIndexOrThrow(_cursor, "NameOfPersonChat");
          final int _cursorIndexOfUserType = CursorUtil.getColumnIndexOrThrow(_cursor, "userType");
          final int _cursorIndexOfMessageStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "messageStatus");
          final int _cursorIndexOfDated = CursorUtil.getColumnIndexOrThrow(_cursor, "dated");
          final int _cursorIndexOfMessageTime = CursorUtil.getColumnIndexOrThrow(_cursor, "messageTime");
          final List<DBCommentMessage> _result = new ArrayList<DBCommentMessage>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final DBCommentMessage _item;
            _item = new DBCommentMessage();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final int _tmpId_media_post;
            _tmpId_media_post = _cursor.getInt(_cursorIndexOfIdMediaPost);
            _item.setId_media_post(_tmpId_media_post);
            final String _tmpMessageText;
            _tmpMessageText = _cursor.getString(_cursorIndexOfMessageText);
            _item.setMessageText(_tmpMessageText);
            final String _tmpNameOfPersonChat;
            _tmpNameOfPersonChat = _cursor.getString(_cursorIndexOfNameOfPersonChat);
            _item.setNameOfPersonChat(_tmpNameOfPersonChat);
            final String _tmpUserType;
            _tmpUserType = _cursor.getString(_cursorIndexOfUserType);
            _item.setUserType(_tmpUserType);
            final String _tmpMessageStatus;
            _tmpMessageStatus = _cursor.getString(_cursorIndexOfMessageStatus);
            _item.setMessageStatus(_tmpMessageStatus);
            final String _tmpDated;
            _tmpDated = _cursor.getString(_cursorIndexOfDated);
            _item.setDated(_tmpDated);
            final long _tmpMessageTime;
            _tmpMessageTime = _cursor.getLong(_cursorIndexOfMessageTime);
            _item.setMessageTime(_tmpMessageTime);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public int count() {
    final String _sql = "SELECT count(id) AS counter FROM comments";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
