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
public final class DBAccountChannelDataDBAccountChannelDataDOA_Impl implements DBAccountChannelData.DBAccountChannelDataDOA {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DBAccountChannelData> __insertionAdapterOfDBAccountChannelData;

  private final SharedSQLiteStatement __preparedStmtOfClearTable;

  public DBAccountChannelDataDBAccountChannelDataDOA_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDBAccountChannelData = new EntityInsertionAdapter<DBAccountChannelData>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `account_data` (`id`,`id_row`,`liked`,`comments_count`,`image_url`,`title`,`dated`,`description`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DBAccountChannelData value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getId_row());
        stmt.bindLong(3, value.getLiked());
        stmt.bindLong(4, value.getComments_count());
        if (value.getImage_url() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getImage_url());
        }
        if (value.getTitle() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getTitle());
        }
        if (value.getDated() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getDated());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getDescription());
        }
      }
    };
    this.__preparedStmtOfClearTable = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE  FROM account_data";
        return _query;
      }
    };
  }

  @Override
  public void save(final DBAccountChannelData... imts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDBAccountChannelData.insert(imts);
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
  public LiveData<List<DBAccountChannelData>> getAll() {
    final String _sql = "SELECT * FROM account_data";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"account_data"}, false, new Callable<List<DBAccountChannelData>>() {
      @Override
      public List<DBAccountChannelData> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIdRow = CursorUtil.getColumnIndexOrThrow(_cursor, "id_row");
          final int _cursorIndexOfLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "liked");
          final int _cursorIndexOfCommentsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "comments_count");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "image_url");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDated = CursorUtil.getColumnIndexOrThrow(_cursor, "dated");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final List<DBAccountChannelData> _result = new ArrayList<DBAccountChannelData>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final DBAccountChannelData _item;
            final int _tmpId_row;
            _tmpId_row = _cursor.getInt(_cursorIndexOfIdRow);
            final int _tmpLiked;
            _tmpLiked = _cursor.getInt(_cursorIndexOfLiked);
            final int _tmpComments_count;
            _tmpComments_count = _cursor.getInt(_cursorIndexOfCommentsCount);
            final String _tmpImage_url;
            _tmpImage_url = _cursor.getString(_cursorIndexOfImageUrl);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDated;
            _tmpDated = _cursor.getString(_cursorIndexOfDated);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            _item = new DBAccountChannelData(_tmpId_row,_tmpImage_url,_tmpTitle,_tmpDated,_tmpDescription,_tmpLiked,_tmpComments_count);
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
    final String _sql = "SELECT count(id) AS counter FROM account_data";
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
