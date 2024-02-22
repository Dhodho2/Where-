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
public final class StreamViewCountStreamViewCountDOA_Impl implements StreamViewCount.StreamViewCountDOA {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<StreamViewCount> __insertionAdapterOfStreamViewCount;

  private final SharedSQLiteStatement __preparedStmtOfClearTable;

  public StreamViewCountStreamViewCountDOA_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStreamViewCount = new EntityInsertionAdapter<StreamViewCount>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `count_stream` (`id`,`id_row`,`count`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, StreamViewCount value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getId_row());
        stmt.bindLong(3, value.getCount());
      }
    };
    this.__preparedStmtOfClearTable = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE  FROM count_stream";
        return _query;
      }
    };
  }

  @Override
  public void save(final StreamViewCount... count_stream) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfStreamViewCount.insert(count_stream);
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
  public LiveData<List<StreamViewCount>> getAll() {
    final String _sql = "SELECT * FROM count_stream";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"count_stream"}, false, new Callable<List<StreamViewCount>>() {
      @Override
      public List<StreamViewCount> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIdRow = CursorUtil.getColumnIndexOrThrow(_cursor, "id_row");
          final int _cursorIndexOfCount = CursorUtil.getColumnIndexOrThrow(_cursor, "count");
          final List<StreamViewCount> _result = new ArrayList<StreamViewCount>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final StreamViewCount _item;
            final int _tmpId_row;
            _tmpId_row = _cursor.getInt(_cursorIndexOfIdRow);
            final int _tmpCount;
            _tmpCount = _cursor.getInt(_cursorIndexOfCount);
            _item = new StreamViewCount(_tmpId_row,_tmpCount);
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
    final String _sql = "SELECT count(id) AS counter FROM count_stream";
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
