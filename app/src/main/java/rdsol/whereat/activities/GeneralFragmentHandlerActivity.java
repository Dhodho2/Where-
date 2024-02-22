package rdsol.whereat.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import rdsol.whereat.R;
import rdsol.whereat.database.preferences.MyPreferences;
import rdsol.whereat.fragments.CreditPaymentCardPayment;
import rdsol.whereat.fragments.DirectMessagesFragmentChatList;
import rdsol.whereat.fragments.FragementChatView;
import rdsol.whereat.fragments.FragmentNotifications;
import rdsol.whereat.fragments.PurchaseLinkTicks;
import rdsol.whereat.fragments.ScheduleShow;

import static rdsol.whereat.netwox.HandleRequests.API_STORAGE;

public class GeneralFragmentHandlerActivity extends BaseActivity {
    private MyPreferences myPreferences ;
    Fragment fragment;
    public static String Buy_Link_Ticks = "Buy Link Ticks";
    public static String Schedule_Show = "Schedule Show";
    public static String NOTIFICATION = "Notifications";
    public static String DIRECT_MESSAGES = "Direct Messages";
    public static String DIRECT_CHAT_MESSAGES = "Message";
    public static String URL_INTENT = "";

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        initObjects();
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_general_fragment_handler );
      String fragment_name =   getIntent().getStringExtra( "fragment_name" );
      if(fragment_name.equals( Buy_Link_Ticks)){
          fragment = new CreditPaymentCardPayment() ;
      }else if(fragment_name.equals( "Purchase Link Tickets" )){
          fragment = new PurchaseLinkTicks() ;
      }else if(fragment_name.equals( NOTIFICATION )){
          fragment = new FragmentNotifications() ;
      }else if(fragment_name.equals( DIRECT_CHAT_MESSAGES )){
          fragment = new FragementChatView() ;
      }else if(fragment_name.equals( DIRECT_MESSAGES )){
          fragment = new DirectMessagesFragmentChatList() ;
      }else if(fragment_name.equals( Schedule_Show )){
          fragment = new ScheduleShow() ;
      }else{
          // this is link Redeeming
          Intent intent = getIntent();
          URL_INTENT = intent.getAction().replace( API_STORAGE+"justify-share?credit=","" );
          fragment = new CreditPaymentCardPayment() ;

      }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace( R.id.frame_layout, fragment);
        transaction.commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        if(getIntent().hasExtra( "user_username" )){
            getSupportActionBar().setTitle( getIntent().getStringExtra( "user_username" ) );
        }else{
            getSupportActionBar().setTitle( fragment_name );
        }

    }

    @Override
    protected void initObjects ( ) {
        super.initObjects();
        myPreferences = new MyPreferences( this );
        setTheme( myPreferences.getIsDarkMode() ? R.style.AppThemeDark : R.style.AppThemeMaterial );
    }
}
