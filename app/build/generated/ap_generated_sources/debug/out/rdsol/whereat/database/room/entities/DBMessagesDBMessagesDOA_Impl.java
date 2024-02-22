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
public final class DBMessagesDBMessagesDOA_Impl implements DBMessages.DBMessagesDOA {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DBMessages> __insertionAdapterOfDBMessages;

  private final SharedSQLiteStatement __preparedStmtOfSetMessageAsSeen;

  private final SharedSQLiteStatement __preparedStmtOfClearTable;

  public DBMessagesDBMessagesDOA_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDBMessages = new EntityInsertionAdapter<DBMessages>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `direct_messages` (`message`,`dated`,`from_username`,`from_full_name`,`profile_image`,`id_rows`,`from_id_user`,`to_id_user`,`is_sent`,`is_seen_by_to_id_user`,`to_is_deleted`,`id`) VALUES (?,?,?,?,?,?,?,?,?,?,?,nullif(?, 0))";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DBMessages value) {
        if (value.getMessage() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getMessage());
        }
        if (value.getDated() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getDated());
        }
        if (value.getFrom_username() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getFrom_username());
        }
        if (value.getFrom_full_name() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getFrom_full_name());
        }
        if (value.getProfile_image() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getProfile_image());
        }
        stmt.bindLong(6, value.getId_rows());
        stmt.bindLong(7, value.getFrom_id_user());
        stmt.bindLong(8, value.getTo_id_user());
        stmt.bindLong(9, value.getIs_sent());
        stmt.bindLong(10, value.getIs_seen_by_to_id_user());
        stmt.bindLong(11, value.getTo_is_deleted());
        stmt.bindLong(12, value.getId());
      }
    };
    this.__preparedStmtOfSetMessageAsSeen = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE direct_messages SET is_seen_by_to_id_user = 1  WHERE id_rows =?";
        return _query;
      }
    };
    this.__preparedStmtOfClearTable = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE  FROM direct_messages";
        return _query;
      }
    };
  }

  @Override
  public void save(final DBMessages... imts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDBMessages.insert(imts);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void setMessageAsSeen(final int id_rows) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfSetMessageAsSeen.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id_rows);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfSetMessageAsSeen.release(_stmt);
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
  public LiveData<List<DBMessages>> getAllUnRead(final String notmeID) {
    final String _sql = "SELECT * FROM direct_messages WHERE is_seen_by_to_id_user = 0 AND  from_id_user !=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (notmeID == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, notmeID);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"direct_messages"}, false, new Callable<List<DBMessages>>() {
      @Override
      public List<DBMessages> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfDated = CursorUtil.getColumnIndexOrThrow(_cursor, "dated");
          final int _cursorIndexOfFromUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "from_username");
          final int _cursorIndexOfFromFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "from_full_name");
          final int _cursorIndexOfProfileImage = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_image");
          final int _cursorIndexOfIdRows = CursorUtil.getColumnIndexOrThrow(_cursor, "id_rows");
          final int _cursorIndexOfFromIdUser = CursorUtil.getColumnIndexOrThrow(_cursor, "from_id_user");
          final int _cursorIndexOfToIdUser = CursorUtil.getColumnIndexOrThrow(_cursor, "to_id_user");
          final int _cursorIndexOfIsSent = CursorUtil.getColumnIndexOrThrow(_cursor, "is_sent");
          final int _cursorIndexOfIsSeenByToIdUser = CursorUtil.getColumnIndexOrThrow(_cursor, "is_seen_by_to_id_user");
          final int _cursorIndexOfToIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "to_is_deleted");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<DBMessages> _result = new ArrayList<DBMessages>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final DBMessages _item;
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final String _tmpDated;
            _tmpDated = _cursor.getString(_cursorIndexOfDated);
            final String _tmpFrom_username;
            _tmpFrom_username = _cursor.getString(_cursorIndexOfFromUsername);
            final String _tmpFrom_full_name;
            _tmpFrom_full_name = _cursor.getString(_cursorIndexOfFromFullName);
            final String _tmpProfile_image;
            _tmpProfile_image = _cursor.getString(_cursorIndexOfProfileImage);
            final int _tmpId_rows;
            _tmpId_rows = _cursor.getInt(_cursorIndexOfIdRows);
            final int _tmpFrom_id_user;
            _tmpFrom_id_user = _cursor.getInt(_cursorIndexOfFromIdUser);
            final int _tmpTo_id_user;
            _tmpTo_id_user = _cursor.getInt(_cursorIndexOfToIdUser);
            final int _tmpIs_sent;
            _tmpIs_sent = _cursor.getInt(_cursorIndexOfIsSent);
            final int _tmpIs_seen_by_to_id_user;
            _tmpIs_seen_by_to_id_user = _cursor.getInt(_cursorIndexOfIsSeenByToIdUser);
            final int _tmpTo_is_deleted;
            _tmpTo_is_deleted = _cursor.getInt(_cursorIndexOfToIsDeleted);
            _item = new DBMessages(_tmpMessage,_tmpDated,_tmpFrom_username,_tmpFrom_full_name,_tmpProfile_image,_tmpId_rows,_tmpFrom_id_user,_tmpTo_id_user,_tmpIs_sent,_tmpIs_seen_by_to_id_user,_tmpTo_is_deleted);
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
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
  public LiveData<List<DBMessages>> getAll(final String notmeID) {
    final String _sql = "SELECT * FROM direct_messages WHERE from_id_user !=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (notmeID == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, notmeID);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"direct_messages"}, false, new Callable<List<DBMessages>>() {
      @Override
      public List<DBMessages> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfDated = CursorUtil.getColumnIndexOrThrow(_cursor, "dated");
          final int _cursorIndexOfFromUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "from_username");
          final int _cursorIndexOfFromFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "from_full_name");
          final int _cursorIndexOfProfileImage = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_image");
          final int _cursorIndexOfIdRows = CursorUtil.getColumnIndexOrThrow(_cursor, "id_rows");
          final int _cursorIndexOfFromIdUser = CursorUtil.getColumnIndexOrThrow(_cursor, "from_id_user");
          final int _cursorIndexOfToIdUser = CursorUtil.getColumnIndexOrThrow(_cursor, "to_id_user");
          final int _cursorIndexOfIsSent = CursorUtil.getColumnIndexOrThrow(_cursor, "is_sent");
          final int _cursorIndexOfIsSeenByToIdUser = CursorUtil.getColumnIndexOrThrow(_cursor, "is_seen_by_to_id_user");
          final int _cursorIndexOfToIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "to_is_deleted");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<DBMessages> _result = new ArrayList<DBMessages>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final DBMessages _item;
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final String _tmpDated;
            _tmpDated = _cursor.getString(_cursorIndexOfDated);
            final String _tmpFrom_username;
            _tmpFrom_username = _cursor.getString(_cursorIndexOfFromUsername);
            final String _tmpFrom_full_name;
            _tmpFrom_full_name = _cursor.getString(_cursorIndexOfFromFullName);
            final String _tmpProfile_image;
            _tmpProfile_image = _cursor.getString(_cursorIndexOfProfileImage);
            final int _tmpId_rows;
            _tmpId_rows = _cursor.getInt(_cursorIndexOfIdRows);
            final int _tmpFrom_id_user;
            _tmpFrom_id_user = _cursor.getInt(_cursorIndexOfFromIdUser);
            final int _tmpTo_id_user;
            _tmpTo_id_user = _cursor.getInt(_cursorIndexOfToIdUser);
            final int _tmpIs_sent;
            _tmpIs_sent = _cursor.getInt(_cursorIndexOfIsSent);
            final int _tmpIs_seen_by_to_id_user;
            _tmpIs_seen_by_to_id_user = _cursor.getInt(_cursorIndexOfIsSeenByToIdUser);
            final int _tmpTo_is_deleted;
            _tmpTo_is_deleted = _cursor.getInt(_cursorIndexOfToIsDeleted);
            _item = new DBMessages(_tmpMessage,_tmpDated,_tmpFrom_username,_tmpFrom_full_name,_tmpProfile_image,_tmpId_rows,_tmpFrom_id_user,_tmpTo_id_user,_tmpIs_sent,_tmpIs_seen_by_to_id_user,_tmpTo_is_deleted);
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
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
  public LiveData<List<DBMessages>> getAllChatsFor(final String from_id_user, final String meID) {
    final String _sql = "SELECT * FROM direct_messages  WHERE from_id_user =? OR to_id_user =?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (from_id_user == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, from_id_user);
    }
    _argIndex = 2;
    if (meID == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, meID);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"direct_messages"}, false, new Callable<List<DBMessages>>() {
      @Override
      public List<DBMessages> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfDated = CursorUtil.getColumnIndexOrThrow(_cursor, "dated");
          final int _cursorIndexOfFromUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "from_username");
          final int _cursorIndexOfFromFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "from_full_name");
          final int _cursorIndexOfProfileImage = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_image");
          final int _cursorIndexOfIdRows = CursorUtil.getColumnIndexOrThrow(_cursor, "id_rows");
          final int _cursorIndexOfFromIdUser = CursorUtil.getColumnIndexOrThrow(_cursor, "from_id_user");
          final int _cursorIndexOfToIdUser = CursorUtil.getColumnIndexOrThrow(_cursor, "to_id_user");
          final int _cursorIndexOfIsSent = CursorUtil.getColumnIndexOrThrow(_cursor, "is_sent");
          final int _cursorIndexOfIsSeenByToIdUser = CursorUtil.getColumnIndexOrThrow(_cursor, "is_seen_by_to_id_user");
          final int _cursorIndexOfToIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "to_is_deleted");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<DBMessages> _result = new ArrayList<DBMessages>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final DBMessages _item;
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final String _tmpDated;
            _tmpDated = _cursor.getString(_cursorIndexOfDated);
            final String _tmpFrom_username;
            _tmpFrom_username = _cursor.getString(_cursorIndexOfFromUsername);
            final String _tmpFrom_full_name;
            _tmpFrom_full_name = _cursor.getString(_cursorIndexOfFromFullName);
            final String _tmpProfile_image;
            _tmpProfile_image = _cursor.getString(_cursorIndexOfProfileImage);
            final int _tmpId_rows;
            _tmpId_rows = _cursor.getInt(_cursorIndexOfIdRows);
            final int _tmpFrom_id_user;
            _tmpFrom_id_user = _cursor.getInt(_cursorIndexOfFromIdUser);
            final int _tmpTo_id_user;
            _tmpTo_id_user = _cursor.getInt(_cursorIndexOfToIdUser);
            final int _tmpIs_sent;
            _tmpIs_sent = _cursor.getInt(_cursorIndexOfIsSent);
            final int _tmpIs_seen_by_to_id_user;
            _tmpIs_seen_by_to_id_user = _cursor.getInt(_cursorIndexOfIsSeenByToIdUser);
            final int _tmpTo_is_deleted;
            _tmpTo_is_deleted = _cursor.getInt(_cursorIndexOfToIsDeleted);
            _item = new DBMessages(_tmpMessage,_tmpDated,_tmpFrom_username,_tmpFrom_full_name,_tmpProfile_image,_tmpId_rows,_tmpFrom_id_user,_tmpTo_id_user,_tmpIs_sent,_tmpIs_seen_by_to_id_user,_tmpTo_is_deleted);
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
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
    final String _sql = "SELECT count(id) AS counter FROM direct_messages";
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
