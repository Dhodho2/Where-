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
public final class DBBankDetailsDBBankDetailsDOA_Impl implements DBBankDetails.DBBankDetailsDOA {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DBBankDetails> __insertionAdapterOfDBBankDetails;

  private final SharedSQLiteStatement __preparedStmtOfClearTable;

  public DBBankDetailsDBBankDetailsDOA_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDBBankDetails = new EntityInsertionAdapter<DBBankDetails>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `account_details` (`id`,`points`,`lastCheckedOnDate`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DBBankDetails value) {
        stmt.bindLong(1, value.getId());
        if (value.getPoints() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getPoints());
        }
        if (value.getLastCheckedOnDate() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getLastCheckedOnDate());
        }
      }
    };
    this.__preparedStmtOfClearTable = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE  FROM account_details";
        return _query;
      }
    };
  }

  @Override
  public void save(final DBBankDetails... imts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDBBankDetails.insert(imts);
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
  public LiveData<List<DBBankDetails>> getAll() {
    final String _sql = "SELECT * FROM account_details";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"account_details"}, false, new Callable<List<DBBankDetails>>() {
      @Override
      public List<DBBankDetails> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPoints = CursorUtil.getColumnIndexOrThrow(_cursor, "points");
          final int _cursorIndexOfLastCheckedOnDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastCheckedOnDate");
          final List<DBBankDetails> _result = new ArrayList<DBBankDetails>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final DBBankDetails _item;
            final String _tmpPoints;
            _tmpPoints = _cursor.getString(_cursorIndexOfPoints);
            final String _tmpLastCheckedOnDate;
            _tmpLastCheckedOnDate = _cursor.getString(_cursorIndexOfLastCheckedOnDate);
            _item = new DBBankDetails(_tmpPoints,_tmpLastCheckedOnDate);
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
    final String _sql = "SELECT count(id) AS counter FROM account_details";
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
