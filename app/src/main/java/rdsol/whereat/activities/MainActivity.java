package rdsol.whereat.activities;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.imaginativeworld.oopsnointernet.ConnectionCallback;
import org.imaginativeworld.oopsnointernet.NoInternetDialog;
import org.imaginativeworld.oopsnointernet.NoInternetSnackbar;

import java.util.List;
import java.util.concurrent.TimeUnit;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import rdsol.whereat.R;
import rdsol.whereat.customeviews.CustomBadgeShape;
import rdsol.whereat.customeviews.badger.Badger;
import rdsol.whereat.customeviews.badger.CountBadge;
import rdsol.whereat.database.preferences.MyPreferences;
import rdsol.whereat.datamodels.DirectMesagesModel;
import rdsol.whereat.datamodels.NotificationViewModel;
import rdsol.whereat.datamodels.UserCreditDataModel;
import rdsol.whereat.fragments.FragmentBillBoards;
import rdsol.whereat.fragments.HomeFragment;
import rdsol.whereat.fragments.MakeShowFragment;
import rdsol.whereat.fragments.NotificationsFragment;
import rdsol.whereat.services.others.GetMoreHomeFeedData;

import static rdsol.whereat.activities.GeneralFragmentHandlerActivity.Buy_Link_Ticks;
import static rdsol.whereat.activities.GeneralFragmentHandlerActivity.DIRECT_MESSAGES;
import static rdsol.whereat.activities.GeneralFragmentHandlerActivity.NOTIFICATION;
import static rdsol.whereat.utils.AndroidUtilities.API_ACCESS_TOKEN;
import static rdsol.whereat.utils.AndroidUtilities.FCM_TOKEN;
import static rdsol.whereat.utils.AndroidUtilities.HAS_THEME_CHANGED;
import static rdsol.whereat.utils.AndroidUtilities.getRealFileSelectedPath;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private MyPreferences mDBPreferences;
    private BottomNavigationView navView;
    private int LastSelected = R.id.navigation_home;
    private PeriodicWorkRequest saveRequest;
    private Fragment home = new HomeFragment();
    private Fragment notificationsFragment = new NotificationsFragment();
    private Fragment fragmentBillBoards = new FragmentBillBoards();
    private Fragment makeShowFragment = new MakeShowFragment();
    private FragmentManager fm = getSupportFragmentManager();
    private Fragment active = home;
    private CountBadge.Factory ovalFactory;
    // No Internet Snackbar
    private NoInternetSnackbar noInternetSnackbar;


    // No Internet Dialog
    private NoInternetDialog noInternetDialog;
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        mDBPreferences = new MyPreferences( this );

        //setTheme( R.style.AppThemeMaterial);
        setTheme( mDBPreferences.getIsDarkMode() ? R.style.AppThemeDarkNoActionBar : R.style.AppThemeMaterialNoActionBar );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        Toolbar toolbar = findViewById( R.id.toolbar );
        rootView = findViewById( R.id.rootView );

        setSupportActionBar( toolbar );

        // getSupportActionBar().setIcon( R.drawable.thumb_on );
        navView = findViewById( R.id.nav_view );
        mDBPreferences.setLastSelectedIntentFilePath( "" );
        ovalFactory = new CountBadge.Factory( this, new CustomBadgeShape( this, .5f, Gravity.END | Gravity.TOP ) );

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_make_show, R.id.navigation_notifications )
                .build();
       /* NavController navController = Navigation.findNavController( this, R.id.nav_host_fragment );
        NavigationUI.setupActionBarWithNavController( this, navController, appBarConfiguration );
        NavigationUI.setupWithNavController( navView, navController );*/

        getSupportActionBar().setTitle( "Where@" + mDBPreferences.getUserName() );
        //  startActivity( new Intent( this,VideoStreamActivity.class ) );
        startReqstLocation();
        //setFrag(HomeFragment.getInstance());
        fm.beginTransaction().add( R.id.frame_layout, notificationsFragment, "3" ).hide( notificationsFragment ).commit();
        fm.beginTransaction().add( R.id.frame_layout, makeShowFragment, "2" ).hide( makeShowFragment ).commit();
        fm.beginTransaction().add( R.id.frame_layout, home, "1" ).commit();
        navView.setOnNavigationItemSelectedListener( item -> {
            LastSelected = item.getItemId();
            switch ( item.getItemId() ) {
                case R.id.navigation_home:
                    fm.beginTransaction().hide( active ).show( home ).commit();
                    active = home;
                    return true;
                case R.id.navigation_make_show:
                    fm.beginTransaction().hide( active ).show( makeShowFragment ).commit();
                    active = makeShowFragment;
                    return true;


                case R.id.navigation_notifications:
                    fm.beginTransaction().hide( active ).show( notificationsFragment ).commit();
                    active = notificationsFragment;
                    return true;
                case R.id.navigation_billboard:
                    fm.beginTransaction().hide( active ).show( fragmentBillBoards ).commit();
                    active = fragmentBillBoards;
                    return true;
            }
            return false;
        } );
        final boolean[] isKeyboardShowing = { false };

        creditDataModel = new ViewModelProvider( this ).get( UserCreditDataModel.class );


        if ( !mDBPreferences.isUserLoggedIn() ) {
            Intent intent = new Intent( this, LogIn.class );
            intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity( new Intent( this, LogIn.class ) );
            finish();
        }

    }

    private CoordinatorLayout rootView;

    private void setFrag ( Fragment fragment ) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace( R.id.frame_layout, fragment );
        transaction.commit();
    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data ) {
        super.onActivityResult( requestCode, resultCode, data );
        if ( data != null && requestCode == 3 ) {

            Uri fileU = data.getData();
            if ( fileU != null ) {
                mDBPreferences.setLastSelectedIntentFilePath( getRealFileSelectedPath( MainActivity.this, fileU ) );
            }
            // Log.e( "xxxx", "onActivityResult: WWWWW  " + getRealFileSelectedPath(MainActivity.this,fileU) );

        }
    }


    @Override
    public boolean onCreateOptionsMenu ( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.main, menu );
        //  MenuItem menuItem  = menu.findItem( R.id.action_search );
        Badger.sett( menu.findItem( R.id.action_oval ), ovalFactory ).setCount( 0 );
        Badger.sett( menu.findItem( R.id.action_dm ), ovalFactory ).setCount( 0 );
        Badger.sett( menu.findItem( R.id.action_notification ), ovalFactory ).setCount( 0 );
        itemAccountCreditBall = menu.findItem( R.id.action_oval );
        itemMessIcon = menu.findItem( R.id.action_dm );
        action_notificationIcon = menu.findItem( R.id.action_notification );
        getBankingData();
        getDirectMessagesCount();
        getNotifications();
        return super.onCreateOptionsMenu( menu );
    }

    private void getNotifications ( ) {
        NotificationViewModel homeViewModel = new ViewModelProvider( this ).get( NotificationViewModel.class );
        homeViewModel.getLiveData().getAllUnSeen().observe( this, datalist -> {
            if ( datalist != null && action_notificationIcon != null ) {

                CountBadge badge = Badger.sett( action_notificationIcon, null );
                badge.setCount( ( datalist.size() ) );
            }

        } );
        homeViewModel.getData();
    }



    @Override
    protected void onResume ( ) {
        super.onResume();
        API_ACCESS_TOKEN = mDBPreferences.getAPIToken();
        FCM_TOKEN = mDBPreferences.getFCMToken();
        if ( HAS_THEME_CHANGED ) {
            //  setTheme(mDBPreferences.getIsDarkMode() ? R.style.AppThemeDark : R.style.AppThemeMaterial);
            HAS_THEME_CHANGED = false;
            if ( Build.VERSION.SDK_INT < 28 ) {
                setTheme( mDBPreferences.getIsDarkMode() ? R.style.AppThemeDarkNoActionBar : R.style.AppThemeMaterialNoActionBar );
                recreate();
            }
        }
        getDirectMessagesCount();
        getNotifications();
        if ( itemAccountCreditBall != null ) {
            getBankingData();


        }

        /*create a service to get data for the home feed periodically*/
        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging( true )
                .build();

        saveRequest =
                new PeriodicWorkRequest.Builder( GetMoreHomeFeedData.class, 8, TimeUnit.MINUTES )
                        .setConstraints( constraints ).setInitialDelay( 3, TimeUnit.MINUTES )
                        .build();

        WorkManager.getInstance( this )
                .enqueue( saveRequest );
        noInternetConnection();
    }

    private void noInternetConnection(){
        // No Internet Dialog
        NoInternetDialog.Builder builder1 = new NoInternetDialog.Builder(this);

        builder1.setConnectionCallback(new ConnectionCallback() { // Optional
            @Override
            public void hasActiveConnection(boolean hasActiveConnection) {
                // ...
            }
        });
       // builder1.setCancelable(false); // Optional
        builder1.setNoInternetConnectionTitle("No Internet"); // Optional
        builder1.setNoInternetConnectionMessage("Check your Internet connection and try again"); // Optional
        builder1.setShowInternetOnButtons(true); // Optional
        builder1.setPleaseTurnOnText("Please turn on"); // Optional
        builder1.setWifiOnButtonText("Wifi"); // Optional
        builder1.setMobileDataOnButtonText("Mobile data"); // Optional
        builder1.setCancelable( true );
        builder1.setOnAirplaneModeTitle("No Internet"); // Optional
        builder1.setOnAirplaneModeMessage("You have turned on the airplane mode."); // Optional
        builder1.setPleaseTurnOffText("Please turn off"); // Optional
        builder1.setAirplaneModeOffButtonText("Airplane mode"); // Optional
        builder1.setShowAirplaneModeOffButtons(true); // Optional

        noInternetDialog = builder1.build();


        // No Internet Snackbar
        NoInternetSnackbar.Builder builder2 = new NoInternetSnackbar.Builder(this, ( ViewGroup ) findViewById(android.R.id.content));

        builder2.setConnectionCallback(new ConnectionCallback() { // Optional
            @Override
            public void hasActiveConnection(boolean hasActiveConnection) {
                // ...
            }
        });
        builder2.setIndefinite(true); // Optional
        builder2.setNoInternetConnectionMessage("No active Internet connection!"); // Optional
        builder2.setOnAirplaneModeMessage("You have turned on the airplane mode!"); // Optional
        builder2.setSnackbarActionText("Settings");
        builder2.setShowActionToDismiss(true);

        builder2.setSnackbarDismissActionText("OK");

        noInternetSnackbar = builder2.build();


    }

    private void getDirectMessagesCount ( ) {
        if ( itemMessIcon != null ) {
            DirectMesagesModel hwModel = new ViewModelProvider( this ).get( DirectMesagesModel.class );
            hwModel.getLiveData().getAllUnRead( mDBPreferences.getUserId() ).observe( this, datalist -> {

                CountBadge badge = Badger.sett( itemMessIcon, null );
                badge.setCount( ( datalist.size() ) );
            } );
        }
    }

    private UserCreditDataModel creditDataModel;


    void getBankingData ( ) {

        creditDataModel.getLiveData().getAll().observe( this, datalist -> {
            if ( datalist != null && datalist.size() > 0 ) {
                CountBadge badge = Badger.sett( itemAccountCreditBall, null );
                mDBPreferences.setLastSetPointsBalance( datalist.get( 0 ).getPoints() );
                badge.setCount( Integer.parseInt( datalist.get( 0 ).getPoints() ) );

            }


        } );
    }

    MenuItem itemAccountCreditBall;
    MenuItem itemMessIcon, action_notificationIcon;

    @Override
    public boolean onOptionsItemSelected ( MenuItem item ) {
        int id = item.getItemId();

        if ( id == R.id.action_dm ) {

            // CountBadge badge = Badger.sett( item, null );
            // badge.setCount(badge.getCount() + 1);

            startActivity( new Intent( this, GeneralFragmentHandlerActivity.class ).putExtra( "fragment_name", DIRECT_MESSAGES ) );
            return true;
        }
        if ( id == R.id.action_notification ) {

            // CountBadge badge = Badger.sett( item, null );
            // badge.setCount(badge.getCount() + 1);

            startActivity( new Intent( this, GeneralFragmentHandlerActivity.class ).putExtra( "fragment_name", NOTIFICATION ) );
            return true;
        }
        if ( id == R.id.action_oval ) {

            // CountBadge badge = Badger.sett( item, null );
            // badge.setCount(badge.getCount() + 1);
            getBankingData();
            startActivity( new Intent( this, GeneralFragmentHandlerActivity.class ).putExtra( "fragment_name", Buy_Link_Ticks ) );
            return true;
        }

        if ( id == R.id.action_search ) {
            // startActivity( new Intent( this, SettingsAtivity.class ) );
            return true;
        }
/*
        if ( id == R.id.action_account ) {
            startActivity( new Intent( this,AccountChannelProfile.class ) );
            return true;
        }*/
        if ( id == R.id.action_settings_view ) {
            startActivity( new Intent( this, Settings.class ) );
            return true;
        }
       /* if ( id == R.id.action_logging ) {
            startActivity( new Intent( this, LogIn.class ) );
            return true;
        }*/


        return super.onOptionsItemSelected( item );
    }

    private final int PERMISSIONS_REQUEST_CODE = 7685;

    @AfterPermissionGranted (PERMISSIONS_REQUEST_CODE)
    private void startReqstLocation ( ) {
        String[] perms = { Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE };
        if ( EasyPermissions.hasPermissions( this, perms ) ) {

        } else {
            // if(EasyPermissions.somePermissionPermanentlyDenied(this,))
            EasyPermissions.requestPermissions( this, "The App needs permissions !", PERMISSIONS_REQUEST_CODE, perms );
        }
    }

    @Override
    public void onPermissionsGranted ( int requestCode, @NonNull List<String> perms ) {

    }

    @Override
    public void onPermissionsDenied ( int requestCode, @NonNull List<String> perms ) {
        if ( EasyPermissions.somePermissionPermanentlyDenied( this, perms ) ) {
            new AppSettingsDialog.Builder( this ).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults ) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        EasyPermissions.onRequestPermissionsResult( requestCode, permissions, grantResults, this );
    }
    @Override
    protected void onPause() {
        super.onPause();
        navView.setSelectedItemId( LastSelected );
        // No Internet Dialog
        if (noInternetDialog != null) {
            noInternetDialog.destroy();
        }

        // No Internet Snackbar
        if (noInternetSnackbar != null) {
            noInternetSnackbar.destroy();
        }
    }
    @Override
    protected void onDestroy ( ) {
        super.onDestroy();
        mDBPreferences.setLastSelectedIntentFilePath( "" );
        if ( saveRequest != null ) {
            WorkManager.getInstance( this ).cancelAllWork();

        }

    }
}
