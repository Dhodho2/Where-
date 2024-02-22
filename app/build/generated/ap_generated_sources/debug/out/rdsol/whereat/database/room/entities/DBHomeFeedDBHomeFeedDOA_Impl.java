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
public final class DBHomeFeedDBHomeFeedDOA_Impl implements DBHomeFeed.DBHomeFeedDOA {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DBHomeFeed> __insertionAdapterOfDBHomeFeed;

  private final SharedSQLiteStatement __preparedStmtOfClearTable;

  public DBHomeFeedDBHomeFeedDOA_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDBHomeFeed = new EntityInsertionAdapter<DBHomeFeed>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `home_feed` (`id`,`id_row`,`liked`,`comments_count`,`is_schedule`,`is_image`,`likes_count`,`check_if_following`,`title`,`video_url`,`video_thumbnail_image`,`dated`,`description`,`owner`,`owner_avatar`,`account_full_name`,`account_name`,`rating`,`ondate`,`price`,`start_end_time`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DBHomeFeed value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getId_row());
        stmt.bindLong(3, value.getLiked());
        stmt.bindLong(4, value.getComments_count());
        stmt.bindLong(5, value.getIs_schedule());
        stmt.bindLong(6, value.getIs_image());
        stmt.bindLong(7, value.getLikes_count());
        stmt.bindLong(8, value.getCheck_if_following());
        if (value.getTitle() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getTitle());
        }
        if (value.getVideo_url() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getVideo_url());
        }
        if (value.getVideo_thumbnail_image() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getVideo_thumbnail_image());
        }
        if (value.getDated() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getDated());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getDescription());
        }
        if (value.getOwner() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getOwner());
        }
        if (value.getOwner_avatar() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.getOwner_avatar());
        }
        if (value.getAccount_full_name() == null) {
          stmt.bindNull(16);
        } else {
          stmt.bindString(16, value.getAccount_full_name());
        }
        if (value.getAccount_name() == null) {
          stmt.bindNull(17);
        } else {
          stmt.bindString(17, value.getAccount_name());
        }
        if (value.getRating() == null) {
          stmt.bindNull(18);
        } else {
          stmt.bindString(18, value.getRating());
        }
        if (value.getOndate() == null) {
          stmt.bindNull(19);
        } else {
          stmt.bindString(19, value.getOndate());
        }
        if (value.getPrice() == null) {
          stmt.bindNull(20);
        } else {
          stmt.bindString(20, value.getPrice());
        }
        if (value.getStart_end_time() == null) {
          stmt.bindNull(21);
        } else {
          stmt.bindString(21, value.getStart_end_time());
        }
      }
    };
    this.__preparedStmtOfClearTable = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE  FROM home_feed";
        return _query;
      }
    };
  }

  @Override
  public void save(final DBHomeFeed... imts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDBHomeFeed.insert(imts);
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
  public LiveData<List<DBHomeFeed>> getAll() {
    final String _sql = "SELECT * FROM home_feed";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"home_feed"}, false, new Callable<List<DBHomeFeed>>() {
      @Override
      public List<DBHomeFeed> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIdRow = CursorUtil.getColumnIndexOrThrow(_cursor, "id_row");
          final int _cursorIndexOfLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "liked");
          final int _cursorIndexOfCommentsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "comments_count");
          final int _cursorIndexOfIsSchedule = CursorUtil.getColumnIndexOrThrow(_cursor, "is_schedule");
          final int _cursorIndexOfIsImage = CursorUtil.getColumnIndexOrThrow(_cursor, "is_image");
          final int _cursorIndexOfLikesCount = CursorUtil.getColumnIndexOrThrow(_cursor, "likes_count");
          final int _cursorIndexOfCheckIfFollowing = CursorUtil.getColumnIndexOrThrow(_cursor, "check_if_following");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfVideoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "video_url");
          final int _cursorIndexOfVideoThumbnailImage = CursorUtil.getColumnIndexOrThrow(_cursor, "video_thumbnail_image");
          final int _cursorIndexOfDated = CursorUtil.getColumnIndexOrThrow(_cursor, "dated");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfOwner = CursorUtil.getColumnIndexOrThrow(_cursor, "owner");
          final int _cursorIndexOfOwnerAvatar = CursorUtil.getColumnIndexOrThrow(_cursor, "owner_avatar");
          final int _cursorIndexOfAccountFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "account_full_name");
          final int _cursorIndexOfAccountName = CursorUtil.getColumnIndexOrThrow(_cursor, "account_name");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfOndate = CursorUtil.getColumnIndexOrThrow(_cursor, "ondate");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfStartEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_end_time");
          final List<DBHomeFeed> _result = new ArrayList<DBHomeFeed>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final DBHomeFeed _item;
            final int _tmpId_row;
            _tmpId_row = _cursor.getInt(_cursorIndexOfIdRow);
            final int _tmpLiked;
            _tmpLiked = _cursor.getInt(_cursorIndexOfLiked);
            final int _tmpComments_count;
            _tmpComments_count = _cursor.getInt(_cursorIndexOfCommentsCount);
            final int _tmpIs_schedule;
            _tmpIs_schedule = _cursor.getInt(_cursorIndexOfIsSchedule);
            final int _tmpIs_image;
            _tmpIs_image = _cursor.getInt(_cursorIndexOfIsImage);
            final int _tmpLikes_count;
            _tmpLikes_count = _cursor.getInt(_cursorIndexOfLikesCount);
            final int _tmpCheck_if_following;
            _tmpCheck_if_following = _cursor.getInt(_cursorIndexOfCheckIfFollowing);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpVideo_url;
            _tmpVideo_url = _cursor.getString(_cursorIndexOfVideoUrl);
            final String _tmpVideo_thumbnail_image;
            _tmpVideo_thumbnail_image = _cursor.getString(_cursorIndexOfVideoThumbnailImage);
            final String _tmpDated;
            _tmpDated = _cursor.getString(_cursorIndexOfDated);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpOwner;
            _tmpOwner = _cursor.getString(_cursorIndexOfOwner);
            final String _tmpOwner_avatar;
            _tmpOwner_avatar = _cursor.getString(_cursorIndexOfOwnerAvatar);
            final String _tmpAccount_full_name;
            _tmpAccount_full_name = _cursor.getString(_cursorIndexOfAccountFullName);
            final String _tmpAccount_name;
            _tmpAccount_name = _cursor.getString(_cursorIndexOfAccountName);
            final String _tmpRating;
            _tmpRating = _cursor.getString(_cursorIndexOfRating);
            final String _tmpOndate;
            _tmpOndate = _cursor.getString(_cursorIndexOfOndate);
            final String _tmpPrice;
            _tmpPrice = _cursor.getString(_cursorIndexOfPrice);
            final String _tmpStart_end_time;
            _tmpStart_end_time = _cursor.getString(_cursorIndexOfStartEndTime);
            _item = new DBHomeFeed(_tmpId_row,_tmpTitle,_tmpVideo_url,_tmpVideo_thumbnail_image,_tmpDated,_tmpDescription,_tmpOwner,_tmpOwner_avatar,_tmpLiked,_tmpComments_count,_tmpAccount_full_name,_tmpAccount_name,_tmpIs_schedule,_tmpIs_image,_tmpOndate,_tmpPrice,_tmpStart_end_time,_tmpLikes_count,_tmpCheck_if_following,_tmpRating);
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
  public LiveData<List<DBHomeFeed>> getAllScheduledOnly() {
    final String _sql = "SELECT * FROM home_feed  WHERE is_schedule= '1'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"home_feed"}, false, new Callable<List<DBHomeFeed>>() {
      @Override
      public List<DBHomeFeed> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIdRow = CursorUtil.getColumnIndexOrThrow(_cursor, "id_row");
          final int _cursorIndexOfLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "liked");
          final int _cursorIndexOfCommentsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "comments_count");
          final int _cursorIndexOfIsSchedule = CursorUtil.getColumnIndexOrThrow(_cursor, "is_schedule");
          final int _cursorIndexOfIsImage = CursorUtil.getColumnIndexOrThrow(_cursor, "is_image");
          final int _cursorIndexOfLikesCount = CursorUtil.getColumnIndexOrThrow(_cursor, "likes_count");
          final int _cursorIndexOfCheckIfFollowing = CursorUtil.getColumnIndexOrThrow(_cursor, "check_if_following");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfVideoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "video_url");
          final int _cursorIndexOfVideoThumbnailImage = CursorUtil.getColumnIndexOrThrow(_cursor, "video_thumbnail_image");
          final int _cursorIndexOfDated = CursorUtil.getColumnIndexOrThrow(_cursor, "dated");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfOwner = CursorUtil.getColumnIndexOrThrow(_cursor, "owner");
          final int _cursorIndexOfOwnerAvatar = CursorUtil.getColumnIndexOrThrow(_cursor, "owner_avatar");
          final int _cursorIndexOfAccountFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "account_full_name");
          final int _cursorIndexOfAccountName = CursorUtil.getColumnIndexOrThrow(_cursor, "account_name");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfOndate = CursorUtil.getColumnIndexOrThrow(_cursor, "ondate");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfStartEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_end_time");
          final List<DBHomeFeed> _result = new ArrayList<DBHomeFeed>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final DBHomeFeed _item;
            final int _tmpId_row;
            _tmpId_row = _cursor.getInt(_cursorIndexOfIdRow);
            final int _tmpLiked;
            _tmpLiked = _cursor.getInt(_cursorIndexOfLiked);
            final int _tmpComments_count;
            _tmpComments_count = _cursor.getInt(_cursorIndexOfCommentsCount);
            final int _tmpIs_schedule;
            _tmpIs_schedule = _cursor.getInt(_cursorIndexOfIsSchedule);
            final int _tmpIs_image;
            _tmpIs_image = _cursor.getInt(_cursorIndexOfIsImage);
            final int _tmpLikes_count;
            _tmpLikes_count = _cursor.getInt(_cursorIndexOfLikesCount);
            final int _tmpCheck_if_following;
            _tmpCheck_if_following = _cursor.getInt(_cursorIndexOfCheckIfFollowing);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpVideo_url;
            _tmpVideo_url = _cursor.getString(_cursorIndexOfVideoUrl);
            final String _tmpVideo_thumbnail_image;
            _tmpVideo_thumbnail_image = _cursor.getString(_cursorIndexOfVideoThumbnailImage);
            final String _tmpDated;
            _tmpDated = _cursor.getString(_cursorIndexOfDated);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpOwner;
            _tmpOwner = _cursor.getString(_cursorIndexOfOwner);
            final String _tmpOwner_avatar;
            _tmpOwner_avatar = _cursor.getString(_cursorIndexOfOwnerAvatar);
            final String _tmpAccount_full_name;
            _tmpAccount_full_name = _cursor.getString(_cursorIndexOfAccountFullName);
            final String _tmpAccount_name;
            _tmpAccount_name = _cursor.getString(_cursorIndexOfAccountName);
            final String _tmpRating;
            _tmpRating = _cursor.getString(_cursorIndexOfRating);
            final String _tmpOndate;
            _tmpOndate = _cursor.getString(_cursorIndexOfOndate);
            final String _tmpPrice;
            _tmpPrice = _cursor.getString(_cursorIndexOfPrice);
            final String _tmpStart_end_time;
            _tmpStart_end_time = _cursor.getString(_cursorIndexOfStartEndTime);
            _item = new DBHomeFeed(_tmpId_row,_tmpTitle,_tmpVideo_url,_tmpVideo_thumbnail_image,_tmpDated,_tmpDescription,_tmpOwner,_tmpOwner_avatar,_tmpLiked,_tmpComments_count,_tmpAccount_full_name,_tmpAccount_name,_tmpIs_schedule,_tmpIs_image,_tmpOndate,_tmpPrice,_tmpStart_end_time,_tmpLikes_count,_tmpCheck_if_following,_tmpRating);
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
    final String _sql = "SELECT count(id) AS counter FROM home_feed";
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
