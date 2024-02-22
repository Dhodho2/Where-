package rdsol.whereat.database;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import rdsol.whereat.database.room.entities.AccountChannel;
import rdsol.whereat.database.room.entities.AccountChannelAccountChannelDOA_Impl;
import rdsol.whereat.database.room.entities.DBAccountChannelData;
import rdsol.whereat.database.room.entities.DBAccountChannelDataDBAccountChannelDataDOA_Impl;
import rdsol.whereat.database.room.entities.DBBankDetails;
import rdsol.whereat.database.room.entities.DBBankDetailsDBBankDetailsDOA_Impl;
import rdsol.whereat.database.room.entities.DBCommentMessage;
import rdsol.whereat.database.room.entities.DBCommentMessageCommentMessageDOA_Impl;
import rdsol.whereat.database.room.entities.DBHomeFeed;
import rdsol.whereat.database.room.entities.DBHomeFeedDBHomeFeedDOA_Impl;
import rdsol.whereat.database.room.entities.DBMessages;
import rdsol.whereat.database.room.entities.DBMessagesDBMessagesDOA_Impl;
import rdsol.whereat.database.room.entities.DBNotificationsItems;
import rdsol.whereat.database.room.entities.DBNotificationsItemsDBNotificationsItemsDOA_Impl;
import rdsol.whereat.database.room.entities.StreamViewCount;
import rdsol.whereat.database.room.entities.StreamViewCountStreamViewCountDOA_Impl;

@SuppressWarnings({"unchecked", "deprecation"})
public final class RoomDB_Impl extends RoomDB {
  private volatile DBCommentMessage.CommentMessageDOA _commentMessageDOA;

  private volatile DBHomeFeed.DBHomeFeedDOA _dBHomeFeedDOA;

  private volatile StreamViewCount.StreamViewCountDOA _streamViewCountDOA;

  private volatile AccountChannel.AccountChannelDOA _accountChannelDOA;

  private volatile DBAccountChannelData.DBAccountChannelDataDOA _dBAccountChannelDataDOA;

  private volatile DBBankDetails.DBBankDetailsDOA _dBBankDetailsDOA;

  private volatile DBNotificationsItems.DBNotificationsItemsDOA _dBNotificationsItemsDOA;

  private volatile DBMessages.DBMessagesDOA _dBMessagesDOA;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(23) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `comments` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `id_media_post` INTEGER NOT NULL, `messageText` TEXT, `NameOfPersonChat` TEXT, `userType` TEXT, `messageStatus` TEXT, `dated` TEXT, `messageTime` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `direct_messages` (`message` TEXT, `dated` TEXT, `from_username` TEXT, `from_full_name` TEXT, `profile_image` TEXT, `id_rows` INTEGER NOT NULL, `from_id_user` INTEGER NOT NULL, `to_id_user` INTEGER NOT NULL, `is_sent` INTEGER NOT NULL, `is_seen_by_to_id_user` INTEGER NOT NULL, `to_is_deleted` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `notifications_all` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `id_rows` INTEGER NOT NULL, `TYPE` INTEGER NOT NULL, `is_image` INTEGER NOT NULL, `is_schedule` INTEGER NOT NULL, `is_random_live` INTEGER NOT NULL, `is_seen` INTEGER NOT NULL, `media_id` TEXT, `post_title` TEXT, `description` TEXT, `media` TEXT, `profile_image` TEXT, `post_by_username` TEXT, `post_by_full_name` TEXT, `price` TEXT, `ondate` TEXT, `start_end_time` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `account_details` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `points` TEXT, `lastCheckedOnDate` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `home_feed` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `id_row` INTEGER NOT NULL, `liked` INTEGER NOT NULL, `comments_count` INTEGER NOT NULL, `is_schedule` INTEGER NOT NULL, `is_image` INTEGER NOT NULL, `likes_count` INTEGER NOT NULL, `check_if_following` INTEGER NOT NULL, `title` TEXT, `video_url` TEXT, `video_thumbnail_image` TEXT, `dated` TEXT, `description` TEXT, `owner` TEXT, `owner_avatar` TEXT, `account_full_name` TEXT, `account_name` TEXT, `rating` TEXT, `ondate` TEXT, `price` TEXT, `start_end_time` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `count_stream` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `id_row` INTEGER NOT NULL, `count` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `account_c` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `id_row` INTEGER NOT NULL, `username` TEXT, `full_name` TEXT, `image_url` TEXT, `user_id` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `account_data` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `id_row` INTEGER NOT NULL, `liked` INTEGER NOT NULL, `comments_count` INTEGER NOT NULL, `image_url` TEXT, `title` TEXT, `dated` TEXT, `description` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'bb94c74f3b65718a2789a408224ea33d')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `comments`");
        _db.execSQL("DROP TABLE IF EXISTS `direct_messages`");
        _db.execSQL("DROP TABLE IF EXISTS `notifications_all`");
        _db.execSQL("DROP TABLE IF EXISTS `account_details`");
        _db.execSQL("DROP TABLE IF EXISTS `home_feed`");
        _db.execSQL("DROP TABLE IF EXISTS `count_stream`");
        _db.execSQL("DROP TABLE IF EXISTS `account_c`");
        _db.execSQL("DROP TABLE IF EXISTS `account_data`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsComments = new HashMap<String, TableInfo.Column>(8);
        _columnsComments.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("id_media_post", new TableInfo.Column("id_media_post", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("messageText", new TableInfo.Column("messageText", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("NameOfPersonChat", new TableInfo.Column("NameOfPersonChat", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("userType", new TableInfo.Column("userType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("messageStatus", new TableInfo.Column("messageStatus", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("dated", new TableInfo.Column("dated", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsComments.put("messageTime", new TableInfo.Column("messageTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysComments = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesComments = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoComments = new TableInfo("comments", _columnsComments, _foreignKeysComments, _indicesComments);
        final TableInfo _existingComments = TableInfo.read(_db, "comments");
        if (! _infoComments.equals(_existingComments)) {
          return new RoomOpenHelper.ValidationResult(false, "comments(rdsol.whereat.database.room.entities.DBCommentMessage).\n"
                  + " Expected:\n" + _infoComments + "\n"
                  + " Found:\n" + _existingComments);
        }
        final HashMap<String, TableInfo.Column> _columnsDirectMessages = new HashMap<String, TableInfo.Column>(12);
        _columnsDirectMessages.put("message", new TableInfo.Column("message", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDirectMessages.put("dated", new TableInfo.Column("dated", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDirectMessages.put("from_username", new TableInfo.Column("from_username", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDirectMessages.put("from_full_name", new TableInfo.Column("from_full_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDirectMessages.put("profile_image", new TableInfo.Column("profile_image", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDirectMessages.put("id_rows", new TableInfo.Column("id_rows", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDirectMessages.put("from_id_user", new TableInfo.Column("from_id_user", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDirectMessages.put("to_id_user", new TableInfo.Column("to_id_user", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDirectMessages.put("is_sent", new TableInfo.Column("is_sent", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDirectMessages.put("is_seen_by_to_id_user", new TableInfo.Column("is_seen_by_to_id_user", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDirectMessages.put("to_is_deleted", new TableInfo.Column("to_is_deleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDirectMessages.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDirectMessages = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDirectMessages = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDirectMessages = new TableInfo("direct_messages", _columnsDirectMessages, _foreignKeysDirectMessages, _indicesDirectMessages);
        final TableInfo _existingDirectMessages = TableInfo.read(_db, "direct_messages");
        if (! _infoDirectMessages.equals(_existingDirectMessages)) {
          return new RoomOpenHelper.ValidationResult(false, "direct_messages(rdsol.whereat.database.room.entities.DBMessages).\n"
                  + " Expected:\n" + _infoDirectMessages + "\n"
                  + " Found:\n" + _existingDirectMessages);
        }
        final HashMap<String, TableInfo.Column> _columnsNotificationsAll = new HashMap<String, TableInfo.Column>(17);
        _columnsNotificationsAll.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationsAll.put("id_rows", new TableInfo.Column("id_rows", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationsAll.put("TYPE", new TableInfo.Column("TYPE", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationsAll.put("is_image", new TableInfo.Column("is_image", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationsAll.put("is_schedule", new TableInfo.Column("is_schedule", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationsAll.put("is_random_live", new TableInfo.Column("is_random_live", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationsAll.put("is_seen", new TableInfo.Column("is_seen", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationsAll.put("media_id", new TableInfo.Column("media_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationsAll.put("post_title", new TableInfo.Column("post_title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationsAll.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationsAll.put("media", new TableInfo.Column("media", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationsAll.put("profile_image", new TableInfo.Column("profile_image", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationsAll.put("post_by_username", new TableInfo.Column("post_by_username", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationsAll.put("post_by_full_name", new TableInfo.Column("post_by_full_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationsAll.put("price", new TableInfo.Column("price", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationsAll.put("ondate", new TableInfo.Column("ondate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotificationsAll.put("start_end_time", new TableInfo.Column("start_end_time", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNotificationsAll = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNotificationsAll = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNotificationsAll = new TableInfo("notifications_all", _columnsNotificationsAll, _foreignKeysNotificationsAll, _indicesNotificationsAll);
        final TableInfo _existingNotificationsAll = TableInfo.read(_db, "notifications_all");
        if (! _infoNotificationsAll.equals(_existingNotificationsAll)) {
          return new RoomOpenHelper.ValidationResult(false, "notifications_all(rdsol.whereat.database.room.entities.DBNotificationsItems).\n"
                  + " Expected:\n" + _infoNotificationsAll + "\n"
                  + " Found:\n" + _existingNotificationsAll);
        }
        final HashMap<String, TableInfo.Column> _columnsAccountDetails = new HashMap<String, TableInfo.Column>(3);
        _columnsAccountDetails.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccountDetails.put("points", new TableInfo.Column("points", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccountDetails.put("lastCheckedOnDate", new TableInfo.Column("lastCheckedOnDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAccountDetails = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAccountDetails = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAccountDetails = new TableInfo("account_details", _columnsAccountDetails, _foreignKeysAccountDetails, _indicesAccountDetails);
        final TableInfo _existingAccountDetails = TableInfo.read(_db, "account_details");
        if (! _infoAccountDetails.equals(_existingAccountDetails)) {
          return new RoomOpenHelper.ValidationResult(false, "account_details(rdsol.whereat.database.room.entities.DBBankDetails).\n"
                  + " Expected:\n" + _infoAccountDetails + "\n"
                  + " Found:\n" + _existingAccountDetails);
        }
        final HashMap<String, TableInfo.Column> _columnsHomeFeed = new HashMap<String, TableInfo.Column>(21);
        _columnsHomeFeed.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeFeed.put("id_row", new TableInfo.Column("id_row", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeFeed.put("liked", new TableInfo.Column("liked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeFeed.put("comments_count", new TableInfo.Column("comments_count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeFeed.put("is_schedule", new TableInfo.Column("is_schedule", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeFeed.put("is_image", new TableInfo.Column("is_image", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeFeed.put("likes_count", new TableInfo.Column("likes_count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeFeed.put("check_if_following", new TableInfo.Column("check_if_following", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeFeed.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeFeed.put("video_url", new TableInfo.Column("video_url", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeFeed.put("video_thumbnail_image", new TableInfo.Column("video_thumbnail_image", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeFeed.put("dated", new TableInfo.Column("dated", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeFeed.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeFeed.put("owner", new TableInfo.Column("owner", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeFeed.put("owner_avatar", new TableInfo.Column("owner_avatar", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeFeed.put("account_full_name", new TableInfo.Column("account_full_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeFeed.put("account_name", new TableInfo.Column("account_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeFeed.put("rating", new TableInfo.Column("rating", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeFeed.put("ondate", new TableInfo.Column("ondate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeFeed.put("price", new TableInfo.Column("price", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHomeFeed.put("start_end_time", new TableInfo.Column("start_end_time", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHomeFeed = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesHomeFeed = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoHomeFeed = new TableInfo("home_feed", _columnsHomeFeed, _foreignKeysHomeFeed, _indicesHomeFeed);
        final TableInfo _existingHomeFeed = TableInfo.read(_db, "home_feed");
        if (! _infoHomeFeed.equals(_existingHomeFeed)) {
          return new RoomOpenHelper.ValidationResult(false, "home_feed(rdsol.whereat.database.room.entities.DBHomeFeed).\n"
                  + " Expected:\n" + _infoHomeFeed + "\n"
                  + " Found:\n" + _existingHomeFeed);
        }
        final HashMap<String, TableInfo.Column> _columnsCountStream = new HashMap<String, TableInfo.Column>(3);
        _columnsCountStream.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountStream.put("id_row", new TableInfo.Column("id_row", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountStream.put("count", new TableInfo.Column("count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCountStream = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCountStream = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCountStream = new TableInfo("count_stream", _columnsCountStream, _foreignKeysCountStream, _indicesCountStream);
        final TableInfo _existingCountStream = TableInfo.read(_db, "count_stream");
        if (! _infoCountStream.equals(_existingCountStream)) {
          return new RoomOpenHelper.ValidationResult(false, "count_stream(rdsol.whereat.database.room.entities.StreamViewCount).\n"
                  + " Expected:\n" + _infoCountStream + "\n"
                  + " Found:\n" + _existingCountStream);
        }
        final HashMap<String, TableInfo.Column> _columnsAccountC = new HashMap<String, TableInfo.Column>(6);
        _columnsAccountC.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccountC.put("id_row", new TableInfo.Column("id_row", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccountC.put("username", new TableInfo.Column("username", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccountC.put("full_name", new TableInfo.Column("full_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccountC.put("image_url", new TableInfo.Column("image_url", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccountC.put("user_id", new TableInfo.Column("user_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAccountC = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAccountC = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAccountC = new TableInfo("account_c", _columnsAccountC, _foreignKeysAccountC, _indicesAccountC);
        final TableInfo _existingAccountC = TableInfo.read(_db, "account_c");
        if (! _infoAccountC.equals(_existingAccountC)) {
          return new RoomOpenHelper.ValidationResult(false, "account_c(rdsol.whereat.database.room.entities.AccountChannel).\n"
                  + " Expected:\n" + _infoAccountC + "\n"
                  + " Found:\n" + _existingAccountC);
        }
        final HashMap<String, TableInfo.Column> _columnsAccountData = new HashMap<String, TableInfo.Column>(8);
        _columnsAccountData.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccountData.put("id_row", new TableInfo.Column("id_row", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccountData.put("liked", new TableInfo.Column("liked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccountData.put("comments_count", new TableInfo.Column("comments_count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccountData.put("image_url", new TableInfo.Column("image_url", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccountData.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccountData.put("dated", new TableInfo.Column("dated", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccountData.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAccountData = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAccountData = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAccountData = new TableInfo("account_data", _columnsAccountData, _foreignKeysAccountData, _indicesAccountData);
        final TableInfo _existingAccountData = TableInfo.read(_db, "account_data");
        if (! _infoAccountData.equals(_existingAccountData)) {
          return new RoomOpenHelper.ValidationResult(false, "account_data(rdsol.whereat.database.room.entities.DBAccountChannelData).\n"
                  + " Expected:\n" + _infoAccountData + "\n"
                  + " Found:\n" + _existingAccountData);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "bb94c74f3b65718a2789a408224ea33d", "2a9413f91568bdb5a947cf266f474106");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "comments","direct_messages","notifications_all","account_details","home_feed","count_stream","account_c","account_data");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `comments`");
      _db.execSQL("DELETE FROM `direct_messages`");
      _db.execSQL("DELETE FROM `notifications_all`");
      _db.execSQL("DELETE FROM `account_details`");
      _db.execSQL("DELETE FROM `home_feed`");
      _db.execSQL("DELETE FROM `count_stream`");
      _db.execSQL("DELETE FROM `account_c`");
      _db.execSQL("DELETE FROM `account_data`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public DBCommentMessage.CommentMessageDOA getTable() {
    if (_commentMessageDOA != null) {
      return _commentMessageDOA;
    } else {
      synchronized(this) {
        if(_commentMessageDOA == null) {
          _commentMessageDOA = new DBCommentMessageCommentMessageDOA_Impl(this);
        }
        return _commentMessageDOA;
      }
    }
  }

  @Override
  public DBHomeFeed.DBHomeFeedDOA getTables() {
    if (_dBHomeFeedDOA != null) {
      return _dBHomeFeedDOA;
    } else {
      synchronized(this) {
        if(_dBHomeFeedDOA == null) {
          _dBHomeFeedDOA = new DBHomeFeedDBHomeFeedDOA_Impl(this);
        }
        return _dBHomeFeedDOA;
      }
    }
  }

  @Override
  public StreamViewCount.StreamViewCountDOA getStreamTables() {
    if (_streamViewCountDOA != null) {
      return _streamViewCountDOA;
    } else {
      synchronized(this) {
        if(_streamViewCountDOA == null) {
          _streamViewCountDOA = new StreamViewCountStreamViewCountDOA_Impl(this);
        }
        return _streamViewCountDOA;
      }
    }
  }

  @Override
  public AccountChannel.AccountChannelDOA getTableAccountChannel() {
    if (_accountChannelDOA != null) {
      return _accountChannelDOA;
    } else {
      synchronized(this) {
        if(_accountChannelDOA == null) {
          _accountChannelDOA = new AccountChannelAccountChannelDOA_Impl(this);
        }
        return _accountChannelDOA;
      }
    }
  }

  @Override
  public DBAccountChannelData.DBAccountChannelDataDOA getTableDBAccountChannelData() {
    if (_dBAccountChannelDataDOA != null) {
      return _dBAccountChannelDataDOA;
    } else {
      synchronized(this) {
        if(_dBAccountChannelDataDOA == null) {
          _dBAccountChannelDataDOA = new DBAccountChannelDataDBAccountChannelDataDOA_Impl(this);
        }
        return _dBAccountChannelDataDOA;
      }
    }
  }

  @Override
  public DBBankDetails.DBBankDetailsDOA getTableDBBankDetailsData() {
    if (_dBBankDetailsDOA != null) {
      return _dBBankDetailsDOA;
    } else {
      synchronized(this) {
        if(_dBBankDetailsDOA == null) {
          _dBBankDetailsDOA = new DBBankDetailsDBBankDetailsDOA_Impl(this);
        }
        return _dBBankDetailsDOA;
      }
    }
  }

  @Override
  public DBNotificationsItems.DBNotificationsItemsDOA getTableNotications() {
    if (_dBNotificationsItemsDOA != null) {
      return _dBNotificationsItemsDOA;
    } else {
      synchronized(this) {
        if(_dBNotificationsItemsDOA == null) {
          _dBNotificationsItemsDOA = new DBNotificationsItemsDBNotificationsItemsDOA_Impl(this);
        }
        return _dBNotificationsItemsDOA;
      }
    }
  }

  @Override
  public DBMessages.DBMessagesDOA getTableDirectMessages() {
    if (_dBMessagesDOA != null) {
      return _dBMessagesDOA;
    } else {
      synchronized(this) {
        if(_dBMessagesDOA == null) {
          _dBMessagesDOA = new DBMessagesDBMessagesDOA_Impl(this);
        }
        return _dBMessagesDOA;
      }
    }
  }
}
