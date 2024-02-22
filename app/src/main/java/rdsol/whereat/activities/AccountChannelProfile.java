package rdsol.whereat.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rdsol.whereat.R;
import rdsol.whereat.adapters.recyclerview.AccountChannelDataRecycler;
import rdsol.whereat.callbacks.HttpGetAccountData;
import rdsol.whereat.callbacks.HttpReqCallBack;
import rdsol.whereat.customeviews.GridSpacingItemDecoration;
import rdsol.whereat.database.preferences.MyPreferences;
import rdsol.whereat.database.room.entities.DBAccountChannelData;
import rdsol.whereat.processes.FollowingProcess;
import rdsol.whereat.processes.GetAccountChannelProcess;
import rdsol.whereat.processes.UnFollowingProcess;

import static rdsol.whereat.activities.GeneralFragmentHandlerActivity.DIRECT_CHAT_MESSAGES;
import static rdsol.whereat.netwox.HandleRequests.API_STORAGE;
import static rdsol.whereat.netwox.HandleRequests.LAST_ACCOUNT_DETAILS;
import static rdsol.whereat.netwox.HandleRequests.LAST_ACCOUNT_DETAILS_FOLLOWS;
import static rdsol.whereat.netwox.HandleRequests.followers_counter;
import static rdsol.whereat.netwox.HandleRequests.following_counter;
import static rdsol.whereat.netwox.HandleRequests.posts_counter;

public class AccountChannelProfile extends BaseActivity implements HttpGetAccountData, HttpReqCallBack {

    private AccountChannelDataRecycler dataRecycler;
    private String account = "1";
    private MaterialButton followBtn;
    private MyPreferences dbPreferences;
    private List<DBAccountChannelData> data = new ArrayList<>();
    private SwipeRefreshLayout swiperootView;
    private com.google.android.material.chip.Chip postsAcc ,followersAcc,followingsAcc;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        dbPreferences = new MyPreferences( this );
        setTheme( dbPreferences.getIsDarkMode() ? R.style.AppThemeDarkNoActionBar : R.style.AppThemeMaterialNoActionBar );
        setContentView( R.layout.activity_account_channel_profile );
        Toolbar toolbar = findViewById( R.id.toolbar );

        if ( getIntent().hasExtra( "account" ) ) {
            account = getIntent().getStringExtra( "account" );
        }

        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setTitle( "Account" );
        progressDialog = new ProgressDialog( this );

        initviews();
        getAccountData();


    }

    private void getAccountData ( ) {
        swiperootView.setRefreshing( true );
       // showProgressDialog( true );
        GetAccountChannelProcess getAccountChannelProcess = new GetAccountChannelProcess( AccountChannelProfile.this );
        getAccountChannelProcess.execute( account );
    }

    private ProgressDialog progressDialog;
    RecyclerView recyclerView;

    private void showProgressDialog ( final boolean isToShow ) {

        if ( isToShow ) {
            if ( !progressDialog.isShowing() ) {
                progressDialog.setMessage( "Loading..." );
                progressDialog.setCancelable( false );
                progressDialog.show();
            }
        } else {
            if ( progressDialog.isShowing() ) {
                progressDialog.dismiss();
            }
        }

    }

    private void initviews ( ) {



        postsAcc = findViewById( R.id.postsAcc );
        followersAcc = findViewById( R.id.followersAcc );
        followingsAcc = findViewById( R.id.followingsAcc );
        swiperootView = findViewById( R.id.swiperootView );
        followBtn = findViewById( R.id.followBtn );
        recyclerView = findViewById( R.id.profile_items );
        recyclerView.setHasFixedSize( true );
        recyclerView.setVisibility( View.VISIBLE );
        dataRecycler = new AccountChannelDataRecycler( data, this );
        recyclerView.setAdapter( dataRecycler );
        int spanCount = 4; // columns
        int spacing = 20; // px
        boolean includeEdge = true;
        swiperootView.setRefreshing( true );
        recyclerView.addItemDecoration( new GridSpacingItemDecoration( spanCount, spacing, includeEdge ) );
        if(account .equals( dbPreferences.getUserId() )){
            followBtn.setVisibility( View.GONE );
        }
        followBtn.setOnClickListener( ev -> {
            showProgressDialog( true );
            if ( LAST_ACCOUNT_DETAILS_FOLLOWS.equals( "true" ) ) {
                UnFollowingProcess unFollowingProcess = new UnFollowingProcess( AccountChannelProfile.this );
                unFollowingProcess.execute( account );
            } else {

                FollowingProcess followingProcess = new FollowingProcess( AccountChannelProfile.this );
                followingProcess.execute( account );
            }

        } );
    }

    @Override
    public void onCompleteResponse ( List<DBAccountChannelData> objects ) {
     //   showProgressDialog( false );
        swiperootView.setRefreshing( false );
        data = objects;
        dataRecycler = new AccountChannelDataRecycler( data, this );
        recyclerView.setAdapter( dataRecycler );
        //  Log.e( "xxxx", "onCompleteResponse: " + LAST_ACCOUNT_DETAILS );
        if ( LAST_ACCOUNT_DETAILS_FOLLOWS.equals( "true" ) ) {
            followBtn.setText( "Exit Showroom" );
        } else {
            followBtn.setText( "Enter Showroom" );
        }
        // Log.e( "xxxx", "onCompleteResponse: " + LAST_ACCOUNT_DETAILS_FOLLOWS);
        TextView NameOfUser = findViewById( R.id.NameOfUser );
        ImageView profileImage = findViewById( R.id.profileImage );
        CircleImageView dmBtn = findViewById( R.id.dmBtn );
        try {
            NameOfUser.setText( LAST_ACCOUNT_DETAILS.getString( "username" ) );
            TextView email_user = findViewById( R.id.email_user );
            email_user.setText( LAST_ACCOUNT_DETAILS.getString( "email" ) );
            if(!account .equals( dbPreferences.getUserId() )){
                dmBtn.setVisibility( View.VISIBLE );
            }

            postsAcc.setText( posts_counter + " Posts" );
            followersAcc.setText( followers_counter + " Entries" );
            followingsAcc.setText( following_counter + " Followings" );
            //NameOfUser.setText(  );

            Glide.with( this ).load( API_STORAGE + LAST_ACCOUNT_DETAILS.getString( "profile_image" ) ).
                    placeholder( R.drawable.ic_account_circle_deep_purple_600_24dp ).into( profileImage );
          //  Log.e( "xxxx", "onCompleteResponse: " + API_STORAGE + LAST_ACCOUNT_DETAILS.getString( "profile_image" ) );
        } catch ( JSONException e ) {
            e.printStackTrace();
        }
        dmBtn.setOnClickListener( vv->{
            startActivity( new Intent( this, GeneralFragmentHandlerActivity.class ).putExtra( "fragment_name",DIRECT_CHAT_MESSAGES  )
                    .putExtra( "user_chats", account + "" )
                    .putExtra( "user_username", NameOfUser.getText() + "" )
            );
        } );
        profileImage.setOnClickListener( ev->{
            if(account .equals( dbPreferences.getUserId() )){
                startActivity( new Intent( this,Settings.class ) );
            }else {


            }

        } );

    }

    @Override
    public void onCompleteResponse ( String response ) {
        showProgressDialog( false );
        Log.e( "xxxx", "onCompleteResponse:hhhhhhhhhhhhhhhhhhhhhhhhhhhh " + response );


        if ( response.equals( "ok" ) ) {

            getAccountData();
            //LAST_ACCOUNT_DETAILS_FOLLOWS =  !LAST_ACCOUNT_DETAILS_FOLLOWS.equals( "true" ) ? "false":"true";
                    /*if ( LAST_ACCOUNT_DETAILS_FOLLOWS.equals( "true" ) ) {
                        followBtn.setText( "Follow" );
                    } else {
                        followBtn.setText( "UnFollow" );
                    }*/
        }

    }
}
