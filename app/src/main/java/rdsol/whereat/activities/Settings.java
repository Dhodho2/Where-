package rdsol.whereat.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import rdsol.whereat.R;
import rdsol.whereat.callbacks.HttpReqCallBack;
import rdsol.whereat.callbacks.UpdateLoginCalls;
import rdsol.whereat.database.preferences.MyPreferences;
import rdsol.whereat.processes.UpdateLoginProcess;
import rdsol.whereat.processes.UploadProFilePictureProcess;
import rdsol.whereat.utils.AndroidUtilities;

import static rdsol.whereat.netwox.HandleRequests.API_STORAGE;
import static rdsol.whereat.utils.AndroidUtilities.getRealFileSelectedPath;
import static rdsol.whereat.utils.AndroidUtilities.setDrawable;
import static rdsol.whereat.utils.AndroidUtilities.simpleAlertButtons;

public class Settings extends BaseActivity implements UpdateLoginCalls, HttpReqCallBack {
    private MyPreferences dbPreferences;
    private LinearLayout llEditProfile;
    private TextView loading_status, NameOfUser, email_user;
    private UpdateLoginProcess updateLoginProcess;
    private DialogInterface dialogInterface;
    private CircleImageView profileImage;
    private String ProfilePicSelectedLast = "";

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        dbPreferences = new MyPreferences( this );
        setTheme( dbPreferences.getIsDarkMode() ? R.style.AppThemeDark : R.style.AppThemeMaterial );
        // setTheme(  R.style.AppThemeDark  );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_settings );

        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setTitle( "Settings" );
        Switch is_cache_comments = findViewById( R.id.is_cache_comments );
        Switch is_darkMode = findViewById( R.id.is_darkMode );
        Switch is_frontCameraMode = findViewById( R.id.is_frontCameraMode );
        MaterialButton logout = findViewById( R.id.logout );
        profileImage = findViewById( R.id.profileImage );
        NameOfUser = findViewById( R.id.NameOfUser );
        email_user = findViewById( R.id.email_user );
        llEditProfile = findViewById( R.id.llEditProfile );

        progressDialog = new ProgressDialog( this );
        NameOfUser.setText( dbPreferences.getUserName() );
        email_user.setText( dbPreferences.getEmail() );

        is_frontCameraMode.setChecked( dbPreferences.getIsFrontCameraMode() );
        is_darkMode.setChecked( dbPreferences.getIsDarkMode() );
        is_cache_comments.setChecked( dbPreferences.getIsCacheCommentsEnabled() );


        is_frontCameraMode.setOnCheckedChangeListener( ( buttonView, isChecked ) -> {
            dbPreferences.setIsFrontCameraMode( isChecked );

            if ( isChecked ) {
                DynamicToast.make( Settings.this, "Now Using Back Camera Mode", setDrawable( Settings.this, R.drawable.ic_info_white_24dp ) ).show();


            }
            if ( !isChecked ) {
                DynamicToast.make( Settings.this, "Now Using Front Camera Mode", setDrawable( Settings.this, R.drawable.ic_info_white_24dp ) ).show();
            }

        } );
        is_cache_comments.setOnCheckedChangeListener( ( buttonView, isChecked ) -> {
            dbPreferences.setIsCacheCommentsEnabled( isChecked );

            if ( isChecked ) {
                DynamicToast.make( Settings.this, "Comments Cache is Enabled", setDrawable( Settings.this, R.drawable.ic_info_white_24dp ) ).show();

            }
            if ( !isChecked ) {
                DynamicToast.make( Settings.this, "Comments Cache is Disabled", setDrawable( Settings.this, R.drawable.ic_info_white_24dp ) ).show();
            }

        } );
        is_darkMode.setOnCheckedChangeListener( ( buttonView, isChecked ) -> {
            dbPreferences.setIsDarkMode( isChecked );

            if ( isChecked ) {

                setTheme( R.style.AppThemeDark );
                is_darkMode.setChecked( isChecked );
                //  Log.e( "xxxx", "onCreate:GG " + isChecked );
                DynamicToast.make( Settings.this, "Changed to Dark Mode (Restart the App)", setDrawable( Settings.this, R.drawable.ic_info_white_24dp ) ).show();

            }
            if ( !isChecked ) {
                setTheme( R.style.AppThemeMaterial );
                is_darkMode.setChecked( isChecked );

                // Log.e( "xxxx", "onCreate:KKKK " + isChecked );
                DynamicToast.make( Settings.this, "Changed to Light Mode (Restart the App)", setDrawable( Settings.this, R.drawable.ic_info_white_24dp ) ).show();
            }
            AndroidUtilities.HAS_THEME_CHANGED = true;
            if ( Build.VERSION.SDK_INT < 28 ) {

                recreate();
            }
            //
        } );

        llEditProfile.setOnLongClickListener( ev -> {
            editProfile( Settings.this );
            return false;
        } );
        logout.setOnClickListener( ev -> {
            simpleAlertButtons( Settings.this, "LogOut Confirmation ", "Are you sure you want to Log Out ?",
                    ( dialog, w ) -> {
                        dbPreferences.LogOut();
                        dialog.dismiss();
                        clearAllActivitiesToOpenNewActivity(Settings.this,SplashScreen.class);

                    }, ( d, w ) -> d.dismiss(), "Yes, Logout", "Cancel" );
        } );
        profileImage.setOnClickListener( ev -> {
            simpleAlertButtons( Settings.this, "Upload Check ", "Are you sure you want to upload new Profile Pic ?",
                    ( dialog, w ) -> {
                        startActivityForResult( new Intent( Intent.ACTION_GET_CONTENT ).setType( "image/*" ), 788 );
                    }, ( d, w ) -> d.dismiss(), "Yes", "Cancel" );

        } );
        Glide.with( this )
                .load( dbPreferences.getPropic() )
                .diskCacheStrategy( DiskCacheStrategy.ALL )
                .placeholder( R.drawable.ic_account_circle_deep_purple_600_24dp )
                .into( profileImage );
        //   Log.e( "xxxx", ";;;;;;;;: "+dbPreferences.getPropic() );

    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data ) {
        super.onActivityResult( requestCode, resultCode, data );

        if ( requestCode == 788 ) {
            assert data != null;
            Uri fileU = data.getData();
            if ( fileU != null ) {
                ProfilePicSelectedLast = getRealFileSelectedPath( Settings.this, fileU );
                UploadProFilePictureProcess uploadProFilePictureProcess = new UploadProFilePictureProcess( Settings.this );
                uploadProFilePictureProcess.execute( ProfilePicSelectedLast );
                showProgressDialog( true );

            }
        }
    }

    public void editProfile ( Context context ) {
        AlertDialog.Builder builder = new AlertDialog.Builder( context );


        View dialogView = LayoutInflater.from( context ).inflate( R.layout.edit_user_profile_dialog, null );

        builder.setView( dialogView );
        builder.setCancelable( false );

        loading_status = dialogView.findViewById( R.id.loading_status );
        EditText user_name = dialogView.findViewById( R.id.user_name );
        user_name.setText( dbPreferences.getUserName() );
        EditText user_email = dialogView.findViewById( R.id.user_email );
        user_email.setText( dbPreferences.getEmail() );
        EditText user_password = dialogView.findViewById( R.id.user_password );

        builder.setNegativeButton( "Cancel", null );
        builder.setPositiveButton( "Update", ( ( dialog, which ) -> {
            dialogInterface = dialog;
            String user_name_ = user_name.getText().toString();
            String user_email_ = user_email.getText().toString();
            String user_password_ = user_password.getText().toString();

            if ( user_name_.isEmpty() ) {
                user_name.setError( "Required !" );
                return;
            }
            if ( user_email_.isEmpty() ) {
                user_email.setError( "Required !" );
                return;
            }
            dbPreferences.setEmail( user_email_ );
            dbPreferences.setUserName( user_name_ );
            NameOfUser.setText( dbPreferences.getUserName() );
            email_user.setText( dbPreferences.getEmail() );
            loading_status.setVisibility( View.VISIBLE );
            updateLoginProcess = new UpdateLoginProcess( Settings.this );
            updateLoginProcess.execute( user_name_, user_email_, user_password_ );

        } ) );

        builder.create().show();

    }

    @Override
    public void onUpdateLoginResponse ( String response ) {
        loading_status.setVisibility( View.INVISIBLE );
        updateLoginProcess = null;
        dialogInterface.dismiss();
        DynamicToast.makeSuccess( Settings.this, "Updated Details" ).show();
    }

    private ProgressDialog progressDialog;

    private void showProgressDialog ( final boolean isToShow ) {

        if ( isToShow ) {
            if ( !progressDialog.isShowing() ) {
                progressDialog.setMessage( "UpLoading..." );
                progressDialog.setCancelable( false );
                progressDialog.show();
            }
        } else {
            if ( progressDialog.isShowing() ) {
                progressDialog.dismiss();
            }
        }

    }

    @Override
    public void onCompleteResponse ( String response ) {
        showProgressDialog( false );
        try {
            JSONObject jsonObject = new JSONObject( response );
            String status = jsonObject.getString( "status" );
            if ( status.equals( "ok" ) ) {
                dbPreferences.setPropic( API_STORAGE + jsonObject.getString( "file" ) );
                DynamicToast.makeSuccess( this, "Image Uploaded" );
                Glide.with( this )
                        .load( dbPreferences.getPropic() )
                        .placeholder( R.drawable.ic_account_circle_deep_purple_600_24dp )
                        .into( profileImage );
            } else {
                DynamicToast.makeError( this, "Please try again" );
            }
        } catch ( JSONException e ) {
            e.printStackTrace();
        }
    }
}
