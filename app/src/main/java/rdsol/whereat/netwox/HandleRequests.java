package rdsol.whereat.netwox;

import android.util.Log;
import android.webkit.MimeTypeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cache;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rdsol.whereat.database.RoomDB;
import rdsol.whereat.database.room.entities.AccountChannel;
import rdsol.whereat.database.room.entities.DBAccountChannelData;
import rdsol.whereat.database.room.entities.DBBankDetails;
import rdsol.whereat.database.room.entities.DBCommentMessage;
import rdsol.whereat.database.room.entities.DBHomeFeed;
import rdsol.whereat.database.room.entities.DBMessages;
import rdsol.whereat.database.room.entities.DBNotificationsItems;
import rdsol.whereat.database.room.entities.StreamViewCount;
import rdsol.whereat.pojos.Status;
import rdsol.whereat.utils.AndroidUtilities;

import static rdsol.whereat.utils.AndroidUtilities.API_ACCESS_TOKEN;
import static rdsol.whereat.utils.AndroidUtilities.FCM_TOKEN;
import static rdsol.whereat.utils.AndroidUtilities.INSTALLATION_ID;
import static rdsol.whereat.utils.AndroidUtilities.isValidMail;

public class HandleRequests {

    public static final String BASE_URL = "http://104.248.36.63/";
    public static final String RTMP_URL = "rtmp://104.248.36.63:1935/live";
    private static final String APP_FOLDER = "where_server_app/";
    private static final String API_URL = BASE_URL + APP_FOLDER +"app/api/mobile-app/";
    public static final String API_STORAGE = BASE_URL + APP_FOLDER;
    public static int LAST_COMMENT_ID = 0;
    public static File httpCacheDir;

    public static OkHttpClient getHttpClientCacheer ( ) {

        int cacheSize = 1024 * 1024 * 100; // 100 MB
        Cache cache = new Cache( httpCacheDir, cacheSize );
        return new OkHttpClient.Builder().addNetworkInterceptor( new CacheOKHttpInterceptor() ).cache( cache ).build();
    }

    private static OkHttpClient getHttpClient ( ) {
        OkHttpClient client = new OkHttpClient();

//        client.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
//        client.setReadTimeout(15, TimeUnit.SECONDS);    // socket timeout


        return getHttpClientCacheer();
    }

    private static Request paramsMaker ( String url, HashMap<String, String> params ) {
        FormBody.Builder formBody = new FormBody.Builder();

        formBody.add( "fcm", FCM_TOKEN );
        formBody.add( "device_id", INSTALLATION_ID );
        formBody.add( "device_id", INSTALLATION_ID );
        // load the defaults
        for ( Map.Entry<String, String> entry : params.entrySet() ) {
            String k = entry.getKey();
            String v = entry.getValue();
            formBody.add( k, v );
        }

        return new Request.Builder()
                .url( url )
                .post( formBody.build() )
                .build();

    }

    // getaccounts.php
    public static JSONObject LAST_ACCOUNT_DETAILS = null;
    public static String LAST_ACCOUNT_DETAILS_FOLLOWS, posts_counter = "0", followers_counter = "0", following_counter = "0";



    public static String setNotificationAsSeen ( String record_id  ) {
        final String result = null;

        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();

        mBodyBuilder.addFormDataPart( "record_id", record_id );
        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "notify-nofication-as-seen.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();
        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "hhhuuuuuu: " + res );
            if ( response.isSuccessful() ) {


                return new JSONObject( res ).getString( "status" ) ;

            }
        } catch ( IOException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "ttthhhhhhh: " + e.getMessage() );

        } catch ( JSONException e ) {
            e.printStackTrace();
        }
        return result;
    }



    public static List<DBNotificationsItems> getMyNotifications ( ) {
        final List<DBNotificationsItems> result = new ArrayList<>();

        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();

        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "getuserNotifications.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();
        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "vvvgggg: " + res );
            if ( response.isSuccessful() ) {

                try {
                    JSONObject data = new JSONObject( res );


                    JSONArray arr = data.getJSONArray( "data" );


                    for ( int i = 0; i < arr.length(); i++ ) {


                        JSONObject j = arr.getJSONObject( i );
                        int id_rows = j.getInt( "id" );
                        int is_seen = j.getInt( "is_seen" );
                        String media_id = j.getString( "media_id" );
                        String post_title = j.getString( "post_title" );
                        String description = j.getString( "descriptions" );
                        String media = API_STORAGE +j.getString( "media" );
                        int is_image = j.getInt( "is_image" );
                        int is_schedule   = j.getInt( "is_schedule" );
                        String price = j.getString( "price" );
                        String ondate = j.getString( "dated" );
                        String start_end_time = j.getString( "start_end_time" );
                        int is_random_live = j.getInt( "is_random_live" );
                        String post_by_username = j.getString( "post_by_username" );
                        String post_by_full_name = j.getString( "post_by_full_name" );


                        String profile_image = API_STORAGE +  j.getString( "profile_image" );


                        DBNotificationsItems obj = new DBNotificationsItems(id_rows,0,is_image,is_schedule,
                                is_random_live,media_id,post_title,
                                description,media,profile_image,
                                post_by_username,
                                post_by_full_name,
                                price,ondate,start_end_time,is_seen
                                );

                        result.add( obj );

                    }

                } catch ( JSONException e ) {
                    e.printStackTrace();
                }
                return result;
            }
        } catch ( IOException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "dvdvvddvvd: " + e.getMessage() );

        }
        return result;
    }




    public static String setChatMessageAsSeen ( String message_id  ) {
        final String result = null;

        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();



        mBodyBuilder.addFormDataPart( "id_message", message_id );
        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "update-read-message.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();
        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "jjjjjjjj: " + res );
            if ( response.isSuccessful() ) {


                return new JSONObject( res ).getString( "status" ) ;

            }
        } catch ( IOException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "jjjjkkk: " + e.getMessage() );

        } catch ( JSONException e ) {
            e.printStackTrace();
        }
        return result;
    }


    public static String saveMessage ( String message ,String to_user ) {
        final String result = null;

        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();


        mBodyBuilder.addFormDataPart( "to_user_id", to_user );
        mBodyBuilder.addFormDataPart( "message", message );
        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "save-message.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();
        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "vvvgggg: " + res );
            if ( response.isSuccessful() ) {


                return new JSONObject( res ).getString( "status" ) ;

            }
        } catch ( IOException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "dvdvvddvvd: " + e.getMessage() );

        } catch ( JSONException e ) {
            e.printStackTrace();
        }
        return result;
    }


    public static List<DBMessages> getMyDirectMessages ( ) {
        final List<DBMessages> result = new ArrayList<>();

        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();

        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "get-messages.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();
        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "vvvgggg: " + res );
            if ( response.isSuccessful() ) {

                try {
                    JSONObject data = new JSONObject( res );


                    JSONArray arr = data.getJSONArray( "data" );
                    if(arr.length() > 0){
                        RoomDB.roomConnect().getTableDirectMessages().clearTable();
                    }

                    for ( int i = 0; i < arr.length(); i++ ) {


                        JSONObject j = arr.getJSONObject( i );
                        int id_rows = j.getInt( "id" );
                        int from_id_user = j.getInt( "from_id_user" );
                        int to_id_user = j.getInt( "to_id_user" );
                        int is_sent = j.getInt( "is_sent" );
                        int is_seen_by_to_id_user = j.getInt( "is_seen_by_to_id_user" );
                        int to_is_deleted = j.getInt( "to_is_deleted" );
                        String message = j.getString( "message" );
                        String dated = j.getString( "dated" );
                        String profile_image = API_STORAGE +  j.getString( "profile_image" );
                        String from_username = j.getString( "from_username" );
                        String from_full_name = j.getString( "from_full_name" );

                        DBMessages obj = new DBMessages( message, dated, from_username, from_full_name, profile_image, id_rows, from_id_user, from_id_user, is_sent, is_seen_by_to_id_user, to_is_deleted );

                        result.add( obj );
                        RoomDB.roomConnect().getTableDirectMessages().save( obj );

                    }

                } catch ( JSONException e ) {
                    e.printStackTrace();
                }
                return result;
            }
        } catch ( IOException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "dvdvvddvvd: " + e.getMessage() );

        }
        return result;
    }


    public static String shareCreditUsingLink ( final String ticksAmount ) {
        final String result = null;

        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();


        mBodyBuilder.addFormDataPart( "ticksAmount", ticksAmount );
        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "get-credit-share-link.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();
        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "vvvgggg: " + res );
            if ( response.isSuccessful() ) {


                return !res.equals( "error" ) ? res : null;

            }
        } catch ( IOException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "dvdvvddvvd: " + e.getMessage() );

        }
        return result;
    }


    public static String shareCreditWithOtherUser ( final String ticksAmount, String targetUser ) {
        final String result = null;

        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();


        //  mBodyBuilder.addFormDataPart( "creditAmount", creditAmounta );
        mBodyBuilder.addFormDataPart( "targetUser", targetUser );
        mBodyBuilder.addFormDataPart( "ticksAmount", ticksAmount );
        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "share_credit_with.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();
        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "oooooooo: " + res );
            if ( response.isSuccessful() ) {
                JSONObject jsonObject = new JSONObject( res );

                return jsonObject.getString( "status" );

            }
        } catch ( IOException | JSONException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "nnnnnnnnnn: " + e.getMessage() );

        }
        return result;
    }


    public static String[] getRandomStreamLink ( ) {

        final String[] result = { "data", "media_id" };
        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();

        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "get-new-random-streaming-link.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();

        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "FFFFFFFFFFF: " + res );
            if ( response.isSuccessful() ) {
                JSONObject jsonObject = new JSONObject( res );
                String status = jsonObject.getString( "status" );
                if ( status.equals( "ok" ) ) {
                    return new String[]{ jsonObject.getString( "data" ), jsonObject.getString( "media_id" ) };
                }


                return null;

                //  return res;
            }
        } catch ( IOException | JSONException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "uploadPost: " + e.getMessage() );

        }

        return null;
    }


    public static String updateStreamData ( final String media_id ) {

        final String result = "connection";
        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();


        mBodyBuilder.addFormDataPart( "media_id", media_id );
        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "is-stream-still-live.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();

        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "WWWWWWW: " + res );
            if ( response.isSuccessful() ) {
                JSONObject jsonObject = new JSONObject( res );
                String status = jsonObject.getString( "status" );


                return jsonObject.getString( "data" );

                //  return res;
            }
        } catch ( IOException | JSONException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "uploadPost: " + e.getMessage() );

        }

        return "";
    }


    public static List<AccountChannel> getOtherUsersAccounts ( ) {

        final String result = "connection";
        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();


        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "getotheraccounts.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();
        List<AccountChannel> item = new ArrayList<>();
        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "jjjjjjjj: " + res );
            if ( response.isSuccessful() ) {
                JSONObject jsonObject = new JSONObject( res );
                String status = jsonObject.getString( "status" );
                if ( status.equals( "ok" ) ) {
// API_STORAGE+media,

                    JSONArray jsonArray = jsonObject.getJSONArray( "data" );

                    for ( int i = 0; i < jsonArray.length(); i++ ) {
                        //{"id":"2","username":"Kinsley","id_user_account_types":"0","full_name":"Kinsley Kajiva","profile_image":"storage\/files\/whereat\/media\/files\/user.png","rating":"1"}
                        JSONObject object = jsonArray.getJSONObject( i );
                        int id_row = object.getInt( "id" );

                        String username = object.getString( "username" );
                        String id_user_account_types = object.getString( "id_user_account_types" );


                        String full_name = object.getString( "full_name" );
                        String profile_image = object.getString( "profile_image" );
                        String rating = object.getString( "rating" );


                        item.add( new AccountChannel(
                                id_row, username, full_name, API_STORAGE + profile_image, id_row + ""
                        ) );
                    }
                    return item;

                } else {
                    return Collections.emptyList();
                }
                //  return res;
            }
        } catch ( IOException | JSONException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "uploadPost: " + e.getMessage() );

        }

        return Collections.emptyList();
    }


    public static String userPayForShow ( final String medai_id ) {
        final String result = null;

        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();


        //  mBodyBuilder.addFormDataPart( "creditAmount", creditAmounta );
        mBodyBuilder.addFormDataPart( "media_id", medai_id );
        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "redeem_to_pay_for_show.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();
        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "AAAAAA: " + res );
            if ( response.isSuccessful() ) {
                JSONObject jsonObject = new JSONObject( res );

                return jsonObject.getString( "status" );

            }
        } catch ( IOException | JSONException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "DDDDDSSSS: " + e.getMessage() );

        }
        return result;
    }


    public static String checkIfUserHasPaidForShow ( final String medai_id ) {
        final String result = null;

        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();


        mBodyBuilder.addFormDataPart( "medai_id", medai_id );
        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "check-if-user-has-paid-for-show.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();
        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "AAAAAA: " + res );
            if ( response.isSuccessful() ) {
                JSONObject jsonObject = new JSONObject( res );

                return jsonObject.getString( "data" );

            }
        } catch ( IOException | JSONException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "DDDDDSSSS: " + e.getMessage() );

        }
        return result;
    }

    public static DBBankDetails getUserBalanceDetails ( ) {
        final DBBankDetails result = null;

        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();


        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "get-user-account-details.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();
        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "EEEEEEEEEEEEEE: " + res );
            if ( response.isSuccessful() ) {
                JSONObject jsonObject = new JSONObject( res );
                JSONObject j = jsonObject.getJSONObject( "balance" );
                String points = j.getString( "points" );

                return new DBBankDetails( points, AndroidUtilities.currentTime() );
            }
        } catch ( IOException | JSONException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "vvvvvv: " + e.getMessage() );

        }
        return result;
    }

    public static StreamViewCount getStreamViewsCount ( String media_id ) {

        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();

        mBodyBuilder.addFormDataPart( "media_id", media_id );
        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "get-streamViews.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();
        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "gggkkkkkkk: " + res );
            if ( response.isSuccessful() ) {
                JSONObject jsonObject = new JSONObject( res );

                return new StreamViewCount( jsonObject.getInt( "id_media" ), jsonObject.getInt( "counter" ) );
            }
        } catch ( IOException | JSONException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "wwwwwwwwwww: " + e.getMessage() );

        }
        return null;
    }

    public static String likeDeleteMediaItem ( final String item_id ) {
        final String result = "connection";

        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();

        mBodyBuilder.addFormDataPart( "item_id", item_id );
        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "delete-media.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();
        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "fffffffff: " + res );
            if ( response.isSuccessful() ) {
                JSONObject jsonObject = new JSONObject( res );
                return jsonObject.getString( "status" );
            }
        } catch ( IOException | JSONException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "pppppppp: " + e.getMessage() );

        }
        return result;
    }

    public static String unLikeMedeaItem ( final String item_id ) {
        final String result = "connection";

        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();

        mBodyBuilder.addFormDataPart( "item_id", item_id );
        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "unlike-media.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();
        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "fffffffff: " + res );
            if ( response.isSuccessful() ) {
                return res;
            }
        } catch ( IOException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "pppppppp: " + e.getMessage() );

        }
        return result;
    }


    public static String submitShowSchedule ( final String titl, String price, String date, String startTime, String endTime, String ImageMedia ) {
        final String result = "connection";

        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();

        mBodyBuilder.addFormDataPart( "titl", titl );
        mBodyBuilder.addFormDataPart( "price", price );
        mBodyBuilder.addFormDataPart( "date", date );
        mBodyBuilder.addFormDataPart( "startTime", startTime );
        mBodyBuilder.addFormDataPart( "endTime", endTime );
        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );


        File file = new File( ImageMedia );
        String type = MimeTypeMap.getFileExtensionFromUrl( file.getAbsolutePath() );
        mBodyBuilder.addFormDataPart( "file_posted", file.getName(), RequestBody.create( MediaType.parse( type ), file ) );
        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();

        Request request = new Request.Builder()
                .url( API_URL + "upload-schedule.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();
        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "sssssss: " + res );
            if ( response.isSuccessful() ) {
                JSONObject jsonObject = new JSONObject( res );
                return jsonObject.getString( "status" );
            }
        } catch ( IOException | JSONException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "ggghhhhhh: " + e.getMessage() );

        }
        return result;
    }


    public static String likeMedeaItem ( final String item_id ) {
        final String result = "connection";

        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();

        mBodyBuilder.addFormDataPart( "item_id", item_id );
        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "like-media.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();
        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "fffffffff: " + res );
            if ( response.isSuccessful() ) {
                return res;
            }
        } catch ( IOException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "pppppppp: " + e.getMessage() );

        }
        return result;
    }

    public static List<AccountChannel> getFollowingChannelAcccounts ( ) {

        final String result = "connection";
        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();

        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "getaccounts.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();
        List<AccountChannel> item = new ArrayList<>();
        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "uuuuuu: " + res );
            if ( response.isSuccessful() ) {
                JSONObject jsonObject = new JSONObject( res );
                String status = jsonObject.getString( "status" );
                if ( status.equals( "ok" ) ) {
// API_STORAGE+media,
                    JSONArray jsonArray = jsonObject.getJSONArray( "data" );

                    for ( int i = 0; i < jsonArray.length(); i++ ) {
                        JSONObject object = jsonArray.getJSONObject( i );
                        int id_row = object.getInt( "id" );

                        String username = object.getString( "username" );
                        String id_user_account_types = object.getString( "id_user_account_types" );
                        String media = object.getString( "profile_image" );
                        String full_name = object.getString( "full_name" );
                        String user_id = object.getString( "user_id" );

                        item.add( new AccountChannel(
                                id_row, username, full_name, API_STORAGE + media, user_id

                        ) );
                    }
                    return item;

                } else {
                    return Collections.emptyList();
                }
                //  return res;
            }
        } catch ( IOException | JSONException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "uploadPost: " + e.getMessage() );

        }

        return Collections.emptyList();
    }

    public static List<DBAccountChannelData> getChannelAcccountData ( String id_account ) {

        final String result = "connection";
        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();

        mBodyBuilder.addFormDataPart( "id_userAccount", id_account );
        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "getaccount-data.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();
        List<DBAccountChannelData> item = new ArrayList<>();
        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "lllllllll: " + res );
            if ( response.isSuccessful() ) {
                JSONObject jsonObject = new JSONObject( res );
                String status = jsonObject.getString( "status" );
                if ( status.equals( "ok" ) ) {
// API_STORAGE+media,

                    JSONArray jsonArray = jsonObject.getJSONArray( "data" );
                    LAST_ACCOUNT_DETAILS = jsonObject.getJSONObject( "account" );
                    LAST_ACCOUNT_DETAILS_FOLLOWS = jsonObject.getString( "follows" );
                    posts_counter = jsonObject.getString( "posts_counter" );
                    followers_counter = jsonObject.getInt( "followers_counter" ) + "";
                    following_counter = jsonObject.getInt( "following_counter" ) + "";
                    for ( int i = 0; i < jsonArray.length(); i++ ) {
                        JSONObject object = jsonArray.getJSONObject( i );
                        int id_row = object.getInt( "id" );
                        int id_users = object.getInt( "id_users" );
                        String title = object.getString( "title" );
                        String description = object.getString( "description" );
                        String media = object.getString( "media" );
                        String dated = object.getString( "dated" );
                        int liked = object.getInt( "liked" );
                        int comments_count = object.getInt( "comments_count" );
                        item.add( new DBAccountChannelData(
                                id_row, API_STORAGE + media,
                                title, dated, description, liked, comments_count
                        ) );
                    }
                    return item;

                } else {
                    return Collections.emptyList();
                }
                //  return res;
            }
        } catch ( IOException | JSONException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "uploadPost: " + e.getMessage() );

        }

        return Collections.emptyList();
    }

    public static String unFollow ( int user_id_account ) {
        final String result = "connection";
        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();
        mBodyBuilder.addFormDataPart( "id_user_following", user_id_account + "" );
        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
        Request request = new Request.Builder()
                .url( API_URL + "un-follow.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();

        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "unFollow: " + res );
            if ( response.isSuccessful() ) {
                JSONObject jsonObject = new JSONObject( res );
                String status = jsonObject.getString( "status" );
                if ( status.equals( "ok" ) ) {

                    return "ok";

                } else {
                    return "fail";
                }
            }
        } catch ( IOException | JSONException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "unFollow: " + e.getMessage() );
        }

        return result;
    }


    public static String getFollowers ( ) {
        final String result = "connection";
        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();


        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "read-followers.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();

        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "follow: " + res );
            if ( response.isSuccessful() ) {
                JSONObject jsonObject = new JSONObject( res );
                String status = jsonObject.getString( "status" );
                if ( status.equals( "ok" ) ) {

                    return "ok";

                } else {
                    return "fail";
                }
            }
        } catch ( IOException | JSONException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "follow: " + e.getMessage() );
        }

        return result;
    }

    public static String follow ( int targetID ) {
        final String result = "connection";
        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();

        mBodyBuilder.addFormDataPart( "id_user_follows", targetID + "" );

        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "write-followers.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();

        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "follow: " + res );
            if ( response.isSuccessful() ) {
                JSONObject jsonObject = new JSONObject( res );
                String status = jsonObject.getString( "status" );
                if ( status.equals( "ok" ) ) {

                    return "ok";

                } else {
                    return "fail";
                }
            }
        } catch ( IOException | JSONException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "follow: " + e.getMessage() );
        }

        return "fail";
    }

    public static String postComments ( int media_id, String comment ) {
        final String result = "connection";
        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();

        mBodyBuilder.addFormDataPart( "id_media_post", media_id + "" );
        mBodyBuilder.addFormDataPart( "comment", comment );
        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "post-comments.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();

        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "postComments: " + res );
            if ( response.isSuccessful() ) {
                JSONObject jsonObject = new JSONObject( res );
                String status = jsonObject.getString( "status" );
                if ( status.equals( "ok" ) ) {

                    return "ok";

                } else {
                    return "fail";
                }
            }
        } catch ( IOException | JSONException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "postComments: " + e.getMessage() );
        }

        return "fail";
    }

    public static List<DBCommentMessage> getComments ( int media_id ) {
        final String result = "connection";
        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();

        mBodyBuilder.addFormDataPart( "id_media_post", media_id + "" );
        mBodyBuilder.addFormDataPart( "id_lastComment", LAST_COMMENT_ID + "" );
        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "read-comments.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();
        List<DBCommentMessage> item = new ArrayList<>();
        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "getComments: " + res );
            if ( response.isSuccessful() ) {
                JSONObject jsonObject = new JSONObject( res );
                String status = jsonObject.getString( "status" );
                if ( status.equals( "ok" ) ) {

                    JSONArray jsonArray = jsonObject.getJSONArray( "data" );
                    if ( jsonArray.length() < 1 && LAST_COMMENT_ID > 0 ) {
                        LAST_COMMENT_ID = 0;
                        return getComments( media_id );

                    }
                    for ( int i = 0; i < jsonArray.length(); i++ ) {
                        JSONObject object = jsonArray.getJSONObject( i );
                        int id_row = object.getInt( "id" );
                        LAST_COMMENT_ID = id_row;
                        int id_media_post = object.getInt( "id_media_post" );
                        // int id_user = object.getInt( "id_user" );

                        String comment = object.getString( "comment" );
                        String username = object.getString( "username" );
                        String dated = object.getString( "dated" );
                        DBCommentMessage obj = new DBCommentMessage();
                        obj.setId_media_post( id_media_post );
                        obj.setMessageText( comment );
                        obj.setDated( dated );
                        obj.setMessageTime( 12 );
                        obj.setNameOfPersonChat( username );
                        obj.setMessageStatus( Status.DELIVERED );
                        item.add( obj );
                    }
                    return item;

                } else {
                    return Collections.emptyList();
                }
                //  return res;
            }
        } catch ( IOException | JSONException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "uploadPost: " + e.getMessage() );

        }

        return Collections.emptyList();
    }

    public static List<DBHomeFeed> getHomeFeedData ( ) {

        final String result = "connection";
        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();

        mBodyBuilder.addFormDataPart( "fcm", FCM_TOKEN );
        mBodyBuilder.addFormDataPart( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();
//
        Request request = new Request.Builder()
                .url( API_URL + "home-feed.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();
        List<DBHomeFeed> item = new ArrayList<>();
        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "getHomeFeedData: " + res );
            if ( response.isSuccessful() ) {
                JSONObject jsonObject = new JSONObject( res );
                String status = jsonObject.getString( "status" );
                if ( status.equals( "ok" ) ) {

                    JSONArray jsonArray = jsonObject.getJSONArray( "data" );
                    for ( int i = 0; i < jsonArray.length(); i++ ) {
                        JSONObject object = jsonArray.getJSONObject( i );
                        int id_row = object.getInt( "id" );
                        int id_users = object.getInt( "id_users" );
                        String title = object.getString( "title" );
                        String profile_image = object.getString( "profile_image" );
                        String description = object.getString( "description" );
                        String account_full_name = object.getString( "account_full_name" );
                        String account_name = object.getString( "account_name" );
                        String media = object.getString( "media" );
                        String dated = object.getString( "dated" );
                        String ondate = object.getString( "ondate" );
                        String price = object.getString( "price" );
                        int liked = object.getInt( "liked" );
                        String start_end_time = object.getString( "start_end_time" );
                        String rating = object.getString( "rating" );
                        int comments_count = object.getInt( "comments_count" );
                        int is_image = object.getInt( "is_image" );
                        int is_schedule = object.getInt( "is_schedule" );
                        int likes_count = object.getInt( "likes_count" );
                        int check_if_following = object.getInt( "check_if_following" );

                        item.add( new DBHomeFeed(
                                id_row,
                                title,
                                API_STORAGE + media,
                                API_STORAGE + profile_image,
                                dated,
                                description,
                                id_users + "", API_STORAGE + profile_image,
                                liked, comments_count, account_full_name,
                                account_name, is_schedule, is_image, ondate, price, start_end_time, likes_count, check_if_following, rating


                        ) );
                    }
                    return item;

                } else {
                    return Collections.emptyList();
                }
                //  return res;
            }
        } catch ( IOException | JSONException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "uploadPost: " + e.getMessage() );

        }

        return Collections.emptyList();
    }

    public static String uploadProFilePicture ( String mediaFile ) {

        final String result = "connection";
        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();

        File file = new File( mediaFile );
        String type = MimeTypeMap.getFileExtensionFromUrl( file.getAbsolutePath() );
        mBodyBuilder.addFormDataPart( "file_posted", file.getName(), RequestBody.create( MediaType.parse( type ), file ) );
        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();

        Request request = new Request.Builder()
                .url( API_URL + "post-profile-pic.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();

        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "kkkkkkkkkk: " + res );
            if ( response.isSuccessful() ) {
                return res;
            }
        } catch ( IOException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "mmmmmmmmm: " + e.getMessage() );

        }

        return result;
    }

    public static String uploadPost ( String postTitle, String description, String mediaFile ) {

        final String result = "connection";
        MultipartBody.Builder mBodyBuilder = new MultipartBody.Builder();
        mBodyBuilder.addFormDataPart( "postTitle", postTitle );
        mBodyBuilder.addFormDataPart( "description", description );
        mBodyBuilder.addFormDataPart( "API_ACCESS_TOKEN", API_ACCESS_TOKEN );
        File file = new File( mediaFile );
        String type = MimeTypeMap.getFileExtensionFromUrl( file.getAbsolutePath() );
        mBodyBuilder.addFormDataPart( "file_posted", file.getName(), RequestBody.create( MediaType.parse( type ), file ) );
        RequestBody formBodyy = mBodyBuilder.setType( MultipartBody.FORM ).build();

        Request request = new Request.Builder()
                .url( API_URL + "post-handle.php" )
                .addHeader( "API_ACCESS_TOKEN", API_ACCESS_TOKEN )
                .addHeader( "HTTP_AUTHORIZATION", API_ACCESS_TOKEN )
                .addHeader( "Header12", API_ACCESS_TOKEN )
                .addHeader( "Accept", "application/json" )
                .method( "POST", formBodyy )
                .post( formBodyy )
                .build();

        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "uploadPostOOb: " + res );
            if ( response.isSuccessful() ) {
                return res;
            }
        } catch ( IOException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "uploadPostkjD: " + e.getMessage() );

        }

        return result;
    }

    public static String registerNewUser ( String user_full_name,
                                           String user_name, String user_email, String user_phone, String user_password, String account_type ) {
        String result = "fail";
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add( "password", user_password );
        formBody.add( "username", user_name );
        formBody.add( "email", user_email );
        formBody.add( "fcm", FCM_TOKEN );
        formBody.add( "deviceID", INSTALLATION_ID );
        formBody.add( "full_name", user_full_name );
        formBody.add( "contact_number", user_phone );

        formBody.add( "id_user_account_types", account_type );

        RequestBody formBodyy = formBody.build();

        Request request = new Request.Builder()
                .url( API_URL + "register-user.php" )
                .post( formBodyy )
                .build();


        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "registerNewUser: " + res );
            if ( response.isSuccessful() ) {
                return res;
            }
        } catch ( IOException e ) {
            e.printStackTrace();
            return "connection";
        }

        return result;
    }


    public static String logInUser ( final String username, final String password ) {
        String result = "fail";
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add( "password", password );
        formBody.add( "username", username );
        formBody.add( "fcm", FCM_TOKEN );

        String ty = isValidMail( username ) ? "email" : "phone";
        formBody.add( "type", ty );
        Log.e( "xxxx", "kkklogInUser: " + ty );
        formBody.add( "deviceID", INSTALLATION_ID );

        RequestBody formBodyy = formBody.build();

        Request request = new Request.Builder()
                .url( API_URL + "login.php" )
                .post( formBodyy )
                .build();


        try {
            Response response = getHttpClient().newCall( request ).execute();
            String res = response.body().string();
            Log.e( "xxxx", "logInUser: " + res );
            //return res;
            if ( response.isSuccessful() ) {
                //String res =  response.body().string();
                Log.e( "xxxx", "logInUser: " + res );
                return res;
            }
        } catch ( IOException e ) {
            e.printStackTrace();
            Log.e( "xxxx", "errrlogInUser: " + e.getMessage() );
            return "connection";
        }

        return result;
    }


}
