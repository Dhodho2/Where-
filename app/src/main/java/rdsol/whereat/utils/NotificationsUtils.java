package rdsol.whereat.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import rdsol.whereat.R;
import rdsol.whereat.activities.ViewWatchRead;

import static android.content.Context.ALARM_SERVICE;

public class NotificationsUtils {
    private static final int DAILY_REMINDER_REQUEST_CODE = 345343;


    // fire - Fire Emoji (U+1F525)

    // U+1F44F clap

public static void setShowReminder( Context context ){
    Calendar cal = Calendar.getInstance( TimeZone.getDefault(), Locale.getDefault());
//cal.add(Calendar.SECOND, 10);

    cal.set(Calendar.DATE,19);  //1-31
    cal.set(Calendar.MONTH,Calendar.DECEMBER);  //first month is 0!!! January is zero!!!
    cal.set(Calendar.YEAR,2012);//year...

    cal.set(Calendar.HOUR_OF_DAY, 16);  //HOUR
    cal.set(Calendar.MINUTE, 39);       //MIN
    cal.set(Calendar.SECOND, 10);       //SEC

    //Create a new PendingIntent and add it to the AlarmManager
    Intent intent = new Intent(context, ViewWatchRead.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(context,
            12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    AlarmManager am =      (AlarmManager) context.getSystemService( ALARM_SERVICE);
    assert am != null;
    am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
            pendingIntent);
}
    public static void cancelReminder(Context context,Class<?> cls)
    {
        // Disable a receiver
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }
    private static Calendar resolvelDateTime(final String yyyy_mm_dd__hr_mins){
    String year_part =  yyyy_mm_dd__hr_mins.split( " " )[0];
    String time_part =  yyyy_mm_dd__hr_mins.split( " " )[1];//23:23
    String[] arr = year_part.split( "-" );
    int year = Integer.parseInt( arr[0] );
    int month = Integer.parseInt( arr[1] );
    int day = Integer.parseInt( arr[2] );
    int hour = Integer.parseInt( time_part.split( ":" )[0] );
    int mins = Integer.parseInt( time_part.split( ":" )[1] );
        hour = hour == 0 ? hour : hour-1;
        month = month == 0 ? month : month-1;
        Calendar calendar = Calendar.getInstance();
        Calendar setcalendar = Calendar.getInstance();
        setcalendar.set(Calendar.YEAR, year);
        setcalendar.set(Calendar.DATE, day);
        setcalendar.set(Calendar.MONTH,month);  //first month is 0!!! January is zero!!!
        setcalendar.set(Calendar.HOUR_OF_DAY, hour);
        setcalendar.set(Calendar.MINUTE, mins);
        setcalendar.set(Calendar.SECOND, 0);
        return  setcalendar;
    }
    public static void setReminder(Context context,Class<?> cls,String time)
    {
        Calendar today = Calendar.getInstance();
        Calendar calendar = resolvelDateTime(time);
        // cancel already scheduled reminders
        cancelReminder(context,cls);

        if(!calendar.before(today)){
            DynamicToast.makeWarning( context,"Cant Set Reminder to this show as it has passed already" ) .show();
            calendar.add(Calendar.DATE,1);
            return;
        }

        // Enable a receiver
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                DAILY_REMINDER_REQUEST_CODE, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        assert am != null;
        am.setExact( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);
      //  am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),         AlarmManager.INTERVAL_DAY, pendingIntent);
        DynamicToast.make( context,"Reminder Has been set" ).show();
    }
    public static void showNotification(Context context,Class<?> cls,String title,String content)
    {

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(
                DAILY_REMINDER_REQUEST_CODE,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notification = builder.setContentTitle(title)
                .setContentText(content).setAutoCancel(true)
                .setSound(alarmSound).setSmallIcon( R.drawable.ic_main_icon)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(DAILY_REMINDER_REQUEST_CODE, notification);
    }

}
