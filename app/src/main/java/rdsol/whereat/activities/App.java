package rdsol.whereat.activities;

import android.app.Application;
import android.os.Handler;

import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import java.io.File;

import rdsol.whereat.database.preferences.MyPreferences;
import rdsol.whereat.netwox.HandleRequests;
import rdsol.whereat.utils.AndroidUtilities;
import rdsol.whereat.utils.NativeLoader;

import static rdsol.whereat.database.RoomDB.getRoomInstance;
import static rdsol.whereat.utils.AndroidUtilities.API_ACCESS_TOKEN;
import static rdsol.whereat.utils.AndroidUtilities.FCM_TOKEN;
import static rdsol.whereat.utils.Installation.installationID;

public class App extends Application {
    private static App Instance;
    public static volatile Handler applicationHandler = null;
private MyPreferences dbPreferences ;

    @Override
    public void onCreate ( ) {
        super.onCreate();
        HandleRequests.httpCacheDir = new File(  this.getCacheDir(),"http-cache");
        getRoomInstance( this );
        Instance=this;

        applicationHandler = new Handler(getInstance().getMainLooper());

        NativeLoader.initNativeLibs(App.getInstance());
        dbPreferences = new MyPreferences(this);
        homeKeeping ( );
    }
    private void homeKeeping ( ) {
        API_ACCESS_TOKEN= dbPreferences.getAPIToken();
        FCM_TOKEN= dbPreferences.getFCMToken();
        AndroidUtilities.INSTALLATION_ID= dbPreferences.getInstallationID();
        new Thread( ( ) -> {
            if ( dbPreferences.getInstallationID().isEmpty() ) {
                String id = installationID( getBaseContext() );
                dbPreferences.setInstallationID( id );
                AndroidUtilities.INSTALLATION_ID= dbPreferences.getInstallationID();
            }
        } ).start();
        // This line needs to be executed before any usage of EmojiTextView, EmojiEditText or EmojiButton.
        EmojiManager.install(new GoogleEmojiProvider());
        /*new Thread( ( ) -> {
            AndroidUtilities.TIME_REFERENCES time = whatDayTime();
            if ( time != AndroidUtilities.TIME_REFERENCES.UNKNOWN ) {
                if ( time == IS_MORNING || time == IS_AFTERNOON ) {
                    dbPreferences.setIsDarkMode( false );
                }
                if ( time == IS_NIGHT ) {
                    dbPreferences.setIsDarkMode( true );
                }
            }
        } ).start();*/



    }
    public static App getInstance()
    {
        return Instance;
    }
}
