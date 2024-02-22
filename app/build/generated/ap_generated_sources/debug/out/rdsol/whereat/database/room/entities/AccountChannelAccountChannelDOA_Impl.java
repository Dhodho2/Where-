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
public final class AccountChannelAccountChannelDOA_Impl implements AccountChannel.AccountChannelDOA {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AccountChannel> __insertionAdapterOfAccountChannel;

  private final SharedSQLiteStatement __preparedStmtOfClearTable;

  public AccountChannelAccountChannelDOA_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAccountChannel = new EntityInsertionAdapter<AccountChannel>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `account_c` (`id`,`id_row`,`username`,`full_name`,`image_url`,`user_id`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, AccountChannel value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getId_row());
        if (value.getUsername() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getUsername());
        }
        if (value.getFull_name() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getFull_name());
        }
        if (value.getImage_url() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getImage_url());
        }
        if (value.getUser_id() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getUser_id());
        }
      }
    };
    this.__preparedStmtOfClearTable = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE  FROM account_c";
        return _query;
      }
    };
  }

  @Override
  public void save(final AccountChannel... imts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfAccountChannel.insert(imts);
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
  public LiveData<List<AccountChannel>> getAll() {
    final String _sql = "SELECT * FROM account_c";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"account_c"}, false, new Callable<List<AccountChannel>>() {
      @Override
      public List<AccountChannel> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIdRow = CursorUtil.getColumnIndexOrThrow(_cursor, "id_row");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "full_name");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "image_url");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
          final List<AccountChannel> _result = new ArrayList<AccountChannel>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final AccountChannel _item;
            final int _tmpId_row;
            _tmpId_row = _cursor.getInt(_cursorIndexOfIdRow);
            final String _tmpUsername;
            _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            final String _tmpFull_name;
            _tmpFull_name = _cursor.getString(_cursorIndexOfFullName);
            final String _tmpImage_url;
            _tmpImage_url = _cursor.getString(_cursorIndexOfImageUrl);
            final String _tmpUser_id;
            _tmpUser_id = _cursor.getString(_cursorIndexOfUserId);
            _item = new AccountChannel(_tmpId_row,_tmpUsername,_tmpFull_name,_tmpImage_url,_tmpUser_id);
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
    final String _sql = "SELECT count(id) AS counter FROM account_c";
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
