package rdsol.whereat.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import rdsol.whereat.activities.App;

/**
 * Created by madhur on 3/1/15.
 */
public class AndroidUtilities {

    public static float density = 1;
    public static int statusBarHeight = 0;
    public static Point displaySize = new Point();
    public static boolean HAS_THEME_CHANGED = false;
    public static volatile String API_ACCESS_TOKEN = "";
    public static String INSTALLATION_ID = "";
    public static volatile String FCM_TOKEN = "";
    public enum TIME_REFERENCES {
        IS_DAY, IS_NIGHT, IS_MORNING, IS_AFTERNOON, UNKNOWN
    }


    public static TIME_REFERENCES whatDayTime() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            return TIME_REFERENCES.IS_MORNING;
        } else if (timeOfDay >= 12 && timeOfDay < 16) {

            return TIME_REFERENCES.IS_AFTERNOON;
        } else if (timeOfDay >= 16 && timeOfDay < 21) {

            return TIME_REFERENCES.IS_NIGHT;
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            return TIME_REFERENCES.IS_NIGHT;
        }
        return TIME_REFERENCES.UNKNOWN;
    }
    static {
        density = App.getInstance().getResources().getDisplayMetrics().density;
        checkDisplaySize();
    }

    public static String ellipseString(final String str , final int limit){
        String[] exp = str.split( "" );
        StringBuilder builder = new StringBuilder(  );
        for ( int i = 0; i < exp.length; i++ ) {
            if((i+1) < limit){
                builder.append( exp[i] );
            }

        }
        builder.append(".." );
        return  builder.toString();
    }


    public static int dp(float value) {
        return (int)Math.ceil(density * value);
    }

    public static void runOnUIThread(Runnable runnable) {
        runOnUIThread(runnable, 0);
    }

    public static void runOnUIThread(Runnable runnable, long delay) {
        if (delay == 0) {
            App.applicationHandler.post(runnable);
        } else {
            App.applicationHandler.postDelayed(runnable, delay);
        }
    }

    public native static void loadBitmap(String path, Bitmap bitmap, int scale, int width, int height, int stride);


    public static boolean copyFile(InputStream sourceFile, File destFile) throws IOException {
        OutputStream out = new FileOutputStream(destFile);
        byte[] buf = new byte[4096];
        int len;
        while ((len = sourceFile.read(buf)) > 0) {
            Thread.yield();
            out.write(buf, 0, len);
        }
        out.close();
        return true;
    }

    public static boolean copyFile(File sourceFile, File destFile) throws IOException {
        if(!destFile.exists()) {
            destFile.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } catch (Exception e) {
            //FileLog.e("tmessages", e);
            return false;
        } finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        }
        return true;
    }

    public static void checkDisplaySize() {
        try {
            WindowManager manager = (WindowManager) App.getInstance().getSystemService(Context.WINDOW_SERVICE);
            if (manager != null) {
                Display display = manager.getDefaultDisplay();
                if (display != null) {
                    if ( Build.VERSION.SDK_INT < 13) {
                        displaySize.set(display.getWidth(), display.getHeight());
                    } else {
                        display.getSize(displaySize);
                    }
                }
            }
        } catch (Exception e) {
        }
    }
    /***/
    public static boolean isPasswordValid(String password) {
        String start_of_string = "^";
        String a_digit_must_occur_at_least_once = "(?=.*[0-9])";
        String a_lower_case_letter_must_occur_at_least_once = "(?=.*[a-z])";
        String an_upper_case_letter_must_occur_at_least_once = "(?=.*[A-Z])";
        String a_special_charactermust_occur_at_leastonce = "(?=.*[@#$%^&+=])";
        String no_whitespace_allowed_in_the_entire_string = "(?=\\S+$)";
        String anything_at_least_eight_places_though = ".{8,}";
        String end_of_string = "$";
        return password.matches(start_of_string + a_digit_must_occur_at_least_once +
                a_lower_case_letter_must_occur_at_least_once + an_upper_case_letter_must_occur_at_least_once +
                a_special_charactermust_occur_at_leastonce + no_whitespace_allowed_in_the_entire_string + anything_at_least_eight_places_though + end_of_string);
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static String currentTime ( ) {
        Calendar c = Calendar.getInstance();
        @SuppressLint ("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

        return df.format( c.getTime() );
    }

    public static void setUpFcm( Context mContext ) {
       /* FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener( task -> {
                    if (task.isSuccessful()) {
                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        if (!token.isEmpty()) {
                            new DBPreferences(mContext).setFCMToken(token);
                            HelperUtils.FCM_TOKEN = token;
                            // Toast.makeText(context, token, Toast.LENGTH_SHORT).show();
                        }
                    }

                });*/
    }
    /**
     * Method to check whether App has permissions, it is executed if android version is 6 & above
     * Need to send array of permissions.
     *
     * @param context     -  context of the activity
     * @param permissions - permissions array
     * @return - Has permissions or not
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null &&
                permissions != null) {
            for (String permission : permissions) {
                if ( ActivityCompat.checkSelfPermission(context, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    public static int getViewInset(View view) {
        if (view == null || Build.VERSION.SDK_INT < 21) {
            return 0;
        }
        try {
            Field mAttachInfoField = View.class.getDeclaredField("mAttachInfo");
            mAttachInfoField.setAccessible(true);
            Object mAttachInfo = mAttachInfoField.get(view);
            if (mAttachInfo != null) {
                Field mStableInsetsField = mAttachInfo.getClass().getDeclaredField("mStableInsets");
                mStableInsetsField.setAccessible(true);
                Rect insets = (Rect)mStableInsetsField.get(mAttachInfo);
                return insets.bottom;
            }
        } catch (Exception e) {
            // FileLog.e("tmessages", e);
        }
        return 0;
    }
    /**
     * This make the first letter of the String to a capital letter.
     */
    public static String capitalizeFirstLetter(String string) {
        return string.isEmpty() || string == null ? string : string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static Drawable setDrawable( Context context, int item) {
        return ContextCompat.getDrawable(context, item );
    }

    public static void scaleTextSize ( int rate, final TextView... views ) {
        for ( TextView txtView : views ) {
            if ( rate == 1 ) {
                rate = 14;
            }
            if ( rate == 2 ) {
                rate = 16;
            }
            if ( rate == 3 ) {
                rate = 18;
            }
            if ( rate == 4 ) {
                rate = 20;
            }

            txtView.setTextSize( TypedValue.COMPLEX_UNIT_SP, rate );
        }
    }
    public static void clearAllActivitiesToOpenNewActivity(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    /**
     * Fetch the color from the resources xml file safely.
     * */
    public static int getColorCustome(Context context , int colorId){
        return Build.VERSION.SDK_INT >= 23 ? ContextCompat.getColor ( context , colorId ) :
                context.getResources ().getColor ( colorId );
    }

    public static String firstLatterUpperCase ( String str ) {

        return str.substring ( 0 , 1 ).toUpperCase () + str.substring( 1 );
    }

    public static void simpleAlertButtonsWithLayout ( Context context, String title, String message, final DialogInterface.OnClickListener positiveListener,
                                                      final DialogInterface.OnClickListener negativeListener, String OkPositive, String cancelNegative, View view ) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder( context );
        builder.setCancelable( true );
        builder.setTitle( title );
        builder.setView( view );
        builder.setMessage( message );
        builder.setPositiveButton( OkPositive, positiveListener );
        builder.setNegativeButton( cancelNegative, negativeListener );
        builder.show ();
    }

    public static void simpleAlertButtons (boolean cancellable,Context context , String title , String message , final DialogInterface.OnClickListener positiveListener ,
                                           final DialogInterface.OnClickListener negativeListener  , String OkPositive , String cancelNegative) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setCancelable ( cancellable );
        builder.setTitle ( title );
        builder.setMessage ( message );
        builder.setPositiveButton ( OkPositive  , positiveListener );
        builder.setNegativeButton ( cancelNegative , negativeListener );
        builder.show ();
    }

    public static void simpleAlertButtons (Context context , String title , String message , final DialogInterface.OnClickListener positiveListener ,
                                           final DialogInterface.OnClickListener negativeListener  , String OkPositive , String cancelNegative) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setCancelable ( true );
        builder.setTitle ( title );
        builder.setMessage ( message );
        builder.setPositiveButton ( OkPositive  , positiveListener );
        builder.setNegativeButton ( cancelNegative , negativeListener );
        builder.show ();
    }

    public static void simpleOkAlert(Context context, String title, String message, final DialogInterface.OnClickListener positiveListener) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, positiveListener);

        builder.show();
    }

    public static void simpleAlert(Context context, String title, String message, final String OK, final String CANCEL, final DialogInterface.OnClickListener positiveListener, final DialogInterface.OnClickListener negativeListener) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(OK, positiveListener);
        builder.setNegativeButton(CANCEL, negativeListener);
        builder.show();
    }

    public static void simpleAlert(Context context, String title, String message, final DialogInterface.OnClickListener positiveListener, final DialogInterface.OnClickListener negativeListener) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setCancelable ( true );
        builder.setTitle ( title );
        builder.setMessage ( message );
        builder.setPositiveButton ( android.R.string.ok , positiveListener );
        builder.setNegativeButton ( android.R.string.cancel , negativeListener );
        builder.show ();
    }

    public static void simpleAlert (Context context , String title , String message) {
        MaterialAlertDialogBuilder builder1 = new MaterialAlertDialogBuilder(context );
        builder1.setCancelable ( true );
        builder1.setTitle ( title );
        builder1.setMessage ( message );
        builder1.setNegativeButton ( android.R.string.ok , null );
        builder1.show();
    }

    public static int RandomInt(int min, int max) {
        int randomNum = new Random().nextInt((max - min) + 1) + min;
        return randomNum;
    }

    /**
     * Method to hide soft keyboard from any view
     *
     * @param view    - View from which Keyboard to hide
     * @param context - Context of the calling activity
     */
    public static void hideKeyboard(View view, Context context) {
        InputMethodManager
                inputMethodManager = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Method to forcefully show the soft keyboard.
     *
     * @param view    - View on which keyboard is to be shown
     * @param context - Context of the calling activity
     */
    public static void showKeyboard(View view, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }
    public static Drawable getDrawableCustome( Context context , int iconId){
        return ContextCompat.getDrawable ( context , iconId );
    }


    public static int weeksCalc (String lastDate){
        Calendar cal1 = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );
        try {
            Date date = format.parse( lastDate);
            cal1.setTime( date);
            Calendar calToday = Calendar.getInstance();
            calToday.setTime( new Date(  ) );
            int  days = getWeeksBetween(  cal1 , calToday );
            return days;
        } catch ( ParseException e ) {
            e.printStackTrace();
        }
        return  0;
    }

    /**@return 0 - same week or 1 end date is greater than start date or -1 end date is the week before start date. */

    public static int  getWeeksBetween( Calendar start , Calendar end ){
        start.set( Calendar.HOUR_OF_DAY , 0 );
        start.set( Calendar.MINUTE , 0 );
        start.set( Calendar.SECOND , 0 );
        int start_ = (int) TimeUnit.MILLISECONDS.toDays( start.getTimeInMillis() ) - start.get( Calendar.DAY_OF_WEEK );
        int end_ = (int)  TimeUnit.MILLISECONDS.toDays( end.getTimeInMillis() );
        return (end_ - start_) / 7 ;
    }
    private static boolean validatePhoneNumber(String phoneNo) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{10}")) return true;
            //validating phone number with -, . or spaces
        else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
            //validating phone number with extension length from 3 to 5
        else if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
            //validating phone number where area code is in braces ()
        else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
        //checks +91, then digits between 7-9 then validates 9 digits no in the end. hope this helps!
        else if(phoneNo.matches("^(\\\\+91?)[789]{1}\\\\d{9}$")) return true;
            //return false if nothing matches the input
        else return false;

    }
    public static boolean isValidMail(String email) {

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(EMAIL_STRING).matcher(email).matches();

    }
    private boolean isValidMobile(String phone) {
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        return false;
    }
    public static boolean isValidMobileNumber(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
    public static long saveToday(){
        Calendar to = Calendar.getInstance();
        return to.getTimeInMillis();
    }

    public static boolean isConnectedToNewtWork(Context  context){
        return isConnected( context ) || isMobileConnected( context  ) || isWifiConnected( context );
    }

    private static boolean isWifiConnected ( Context context ) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) return false;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
    }

    private static boolean isConnected ( Context context ) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) return false;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private static boolean isMobileConnected ( Context context ) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) return false;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
    }
    /**
     * Method for return file path of Gallery image/ Document / Video / Audio
     *
     * @param context - context of the application or class
     * @param uri - uri to get the path
     * @return          - path of the selected image file from gallery
     */
    public static String getRealFileSelectedPath(final Context context, final Uri uri)
    {
        // check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri))
        {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri))
            {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type))
                {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri))
            {
                final String id = DocumentsContract.getDocumentId(uri);

                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri))
            {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;

                if ("image".equals(type))
                {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }
                else if ("video".equals(type))
                {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                }
                else if ("audio".equals(type))
                {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme()))
        {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme()))
        {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context           - The context.
     * @param uri               - The Uri to query.
     * @param selection         - (Optional) Filter used in the query.
     * @param selectionArgs     - (Optional) Selection arguments used in the query.
     * @return                  - The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri,
                                        String selection, String[] selectionArgs)
    {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try
        {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);

            if (cursor != null && cursor.moveToFirst())
            {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        }
        finally
        {
            if (cursor != null)
                cursor.close();
        }

        return null;
    }

    /**
     * @param uri      - The Uri to check.
     * @return         - Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri)
    {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri       - The Uri to check.
     * @return          - Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri)
    {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri       - The Uri to check.
     * @return          - Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri)
    {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri       - The Uri to check.
     * @return          - Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri)
    {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
