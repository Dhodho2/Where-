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
public final class DBNotificationsItemsDBNotificationsItemsDOA_Impl implements DBNotificationsItems.DBNotificationsItemsDOA {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DBNotificationsItems> __insertionAdapterOfDBNotificationsItems;

  private final SharedSQLiteStatement __preparedStmtOfClearTable;

  public DBNotificationsItemsDBNotificationsItemsDOA_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDBNotificationsItems = new EntityInsertionAdapter<DBNotificationsItems>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `notifications_all` (`id`,`id_rows`,`TYPE`,`is_image`,`is_schedule`,`is_random_live`,`is_seen`,`media_id`,`post_title`,`description`,`media`,`profile_image`,`post_by_username`,`post_by_full_name`,`price`,`ondate`,`start_end_time`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DBNotificationsItems value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getId_rows());
        stmt.bindLong(3, value.getTYPE());
        stmt.bindLong(4, value.getIs_image());
        stmt.bindLong(5, value.getIs_schedule());
        stmt.bindLong(6, value.getIs_random_live());
        stmt.bindLong(7, value.getIs_seen());
        if (value.getMedia_id() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getMedia_id());
        }
        if (value.getPost_title() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getPost_title());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getDescription());
        }
        if (value.getMedia() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getMedia());
        }
        if (value.getProfile_image() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getProfile_image());
        }
        if (value.getPost_by_username() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getPost_by_username());
        }
        if (value.getPost_by_full_name() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getPost_by_full_name());
        }
        if (value.getPrice() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.getPrice());
        }
        if (value.getOndate() == null) {
          stmt.bindNull(16);
        } else {
          stmt.bindString(16, value.getOndate());
        }
        if (value.getStart_end_time() == null) {
          stmt.bindNull(17);
        } else {
          stmt.bindString(17, value.getStart_end_time());
        }
      }
    };
    this.__preparedStmtOfClearTable = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE  FROM notifications_all";
        return _query;
      }
    };
  }

  @Override
  public void save(final DBNotificationsItems... imts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDBNotificationsItems.insert(imts);
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
  public LiveData<List<DBNotificationsItems>> getAllUnSeen() {
    final String _sql = "SELECT * FROM notifications_all WHERE is_seen = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"notifications_all"}, false, new Callable<List<DBNotificationsItems>>() {
      @Override
      public List<DBNotificationsItems> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIdRows = CursorUtil.getColumnIndexOrThrow(_cursor, "id_rows");
          final int _cursorIndexOfTYPE = CursorUtil.getColumnIndexOrThrow(_cursor, "TYPE");
          final int _cursorIndexOfIsImage = CursorUtil.getColumnIndexOrThrow(_cursor, "is_image");
          final int _cursorIndexOfIsSchedule = CursorUtil.getColumnIndexOrThrow(_cursor, "is_schedule");
          final int _cursorIndexOfIsRandomLive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_random_live");
          final int _cursorIndexOfIsSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "is_seen");
          final int _cursorIndexOfMediaId = CursorUtil.getColumnIndexOrThrow(_cursor, "media_id");
          final int _cursorIndexOfPostTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "post_title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfMedia = CursorUtil.getColumnIndexOrThrow(_cursor, "media");
          final int _cursorIndexOfProfileImage = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_image");
          final int _cursorIndexOfPostByUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "post_by_username");
          final int _cursorIndexOfPostByFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "post_by_full_name");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfOndate = CursorUtil.getColumnIndexOrThrow(_cursor, "ondate");
          final int _cursorIndexOfStartEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_end_time");
          final List<DBNotificationsItems> _result = new ArrayList<DBNotificationsItems>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final DBNotificationsItems _item;
            final int _tmpId_rows;
            _tmpId_rows = _cursor.getInt(_cursorIndexOfIdRows);
            final int _tmpTYPE;
            _tmpTYPE = _cursor.getInt(_cursorIndexOfTYPE);
            final int _tmpIs_image;
            _tmpIs_image = _cursor.getInt(_cursorIndexOfIsImage);
            final int _tmpIs_schedule;
            _tmpIs_schedule = _cursor.getInt(_cursorIndexOfIsSchedule);
            final int _tmpIs_random_live;
            _tmpIs_random_live = _cursor.getInt(_cursorIndexOfIsRandomLive);
            final int _tmpIs_seen;
            _tmpIs_seen = _cursor.getInt(_cursorIndexOfIsSeen);
            final String _tmpMedia_id;
            _tmpMedia_id = _cursor.getString(_cursorIndexOfMediaId);
            final String _tmpPost_title;
            _tmpPost_title = _cursor.getString(_cursorIndexOfPostTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpMedia;
            _tmpMedia = _cursor.getString(_cursorIndexOfMedia);
            final String _tmpProfile_image;
            _tmpProfile_image = _cursor.getString(_cursorIndexOfProfileImage);
            final String _tmpPost_by_username;
            _tmpPost_by_username = _cursor.getString(_cursorIndexOfPostByUsername);
            final String _tmpPost_by_full_name;
            _tmpPost_by_full_name = _cursor.getString(_cursorIndexOfPostByFullName);
            final String _tmpPrice;
            _tmpPrice = _cursor.getString(_cursorIndexOfPrice);
            final String _tmpOndate;
            _tmpOndate = _cursor.getString(_cursorIndexOfOndate);
            final String _tmpStart_end_time;
            _tmpStart_end_time = _cursor.getString(_cursorIndexOfStartEndTime);
            _item = new DBNotificationsItems(_tmpId_rows,_tmpTYPE,_tmpIs_image,_tmpIs_schedule,_tmpIs_random_live,_tmpMedia_id,_tmpPost_title,_tmpDescription,_tmpMedia,_tmpProfile_image,_tmpPost_by_username,_tmpPost_by_full_name,_tmpPrice,_tmpOndate,_tmpStart_end_time,_tmpIs_seen);
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
  public LiveData<List<DBNotificationsItems>> getAll() {
    final String _sql = "SELECT * FROM notifications_all";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"notifications_all"}, false, new Callable<List<DBNotificationsItems>>() {
      @Override
      public List<DBNotificationsItems> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIdRows = CursorUtil.getColumnIndexOrThrow(_cursor, "id_rows");
          final int _cursorIndexOfTYPE = CursorUtil.getColumnIndexOrThrow(_cursor, "TYPE");
          final int _cursorIndexOfIsImage = CursorUtil.getColumnIndexOrThrow(_cursor, "is_image");
          final int _cursorIndexOfIsSchedule = CursorUtil.getColumnIndexOrThrow(_cursor, "is_schedule");
          final int _cursorIndexOfIsRandomLive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_random_live");
          final int _cursorIndexOfIsSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "is_seen");
          final int _cursorIndexOfMediaId = CursorUtil.getColumnIndexOrThrow(_cursor, "media_id");
          final int _cursorIndexOfPostTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "post_title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfMedia = CursorUtil.getColumnIndexOrThrow(_cursor, "media");
          final int _cursorIndexOfProfileImage = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_image");
          final int _cursorIndexOfPostByUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "post_by_username");
          final int _cursorIndexOfPostByFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "post_by_full_name");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfOndate = CursorUtil.getColumnIndexOrThrow(_cursor, "ondate");
          final int _cursorIndexOfStartEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_end_time");
          final List<DBNotificationsItems> _result = new ArrayList<DBNotificationsItems>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final DBNotificationsItems _item;
            final int _tmpId_rows;
            _tmpId_rows = _cursor.getInt(_cursorIndexOfIdRows);
            final int _tmpTYPE;
            _tmpTYPE = _cursor.getInt(_cursorIndexOfTYPE);
            final int _tmpIs_image;
            _tmpIs_image = _cursor.getInt(_cursorIndexOfIsImage);
            final int _tmpIs_schedule;
            _tmpIs_schedule = _cursor.getInt(_cursorIndexOfIsSchedule);
            final int _tmpIs_random_live;
            _tmpIs_random_live = _cursor.getInt(_cursorIndexOfIsRandomLive);
            final int _tmpIs_seen;
            _tmpIs_seen = _cursor.getInt(_cursorIndexOfIsSeen);
            final String _tmpMedia_id;
            _tmpMedia_id = _cursor.getString(_cursorIndexOfMediaId);
            final String _tmpPost_title;
            _tmpPost_title = _cursor.getString(_cursorIndexOfPostTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpMedia;
            _tmpMedia = _cursor.getString(_cursorIndexOfMedia);
            final String _tmpProfile_image;
            _tmpProfile_image = _cursor.getString(_cursorIndexOfProfileImage);
            final String _tmpPost_by_username;
            _tmpPost_by_username = _cursor.getString(_cursorIndexOfPostByUsername);
            final String _tmpPost_by_full_name;
            _tmpPost_by_full_name = _cursor.getString(_cursorIndexOfPostByFullName);
            final String _tmpPrice;
            _tmpPrice = _cursor.getString(_cursorIndexOfPrice);
            final String _tmpOndate;
            _tmpOndate = _cursor.getString(_cursorIndexOfOndate);
            final String _tmpStart_end_time;
            _tmpStart_end_time = _cursor.getString(_cursorIndexOfStartEndTime);
            _item = new DBNotificationsItems(_tmpId_rows,_tmpTYPE,_tmpIs_image,_tmpIs_schedule,_tmpIs_random_live,_tmpMedia_id,_tmpPost_title,_tmpDescription,_tmpMedia,_tmpProfile_image,_tmpPost_by_username,_tmpPost_by_full_name,_tmpPrice,_tmpOndate,_tmpStart_end_time,_tmpIs_seen);
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
    final String _sql = "SELECT count(id) AS counter FROM notifications_all";
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
