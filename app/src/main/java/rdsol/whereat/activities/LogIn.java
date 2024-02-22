package rdsol.whereat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONException;
import org.json.JSONObject;

import rdsol.whereat.R;
import rdsol.whereat.callbacks.HttpReqCallBack;
import rdsol.whereat.database.preferences.MyPreferences;
import rdsol.whereat.processes.LoginProcess;

import static rdsol.whereat.utils.AndroidUtilities.INSTALLATION_ID;
import static rdsol.whereat.utils.AndroidUtilities.hideKeyboard;
import static rdsol.whereat.utils.AndroidUtilities.isConnectedToNewtWork;

public class LogIn extends BaseActivity implements HttpReqCallBack {
    private MaterialButton loginButton;
    private EditText user_email, user_password;
    private LinearLayout loadingProgressBarLayout;
    private MyPreferences myPreferences;
    private TextView txt_create_account ;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_log_in );
        txt_create_account = findViewById( R.id.txt_create_account );
        loginButton = findViewById( R.id.loginButton );
        user_email = findViewById( R.id.user_email );
        user_password = findViewById( R.id.user_password );
        loadingProgressBarLayout = findViewById( R.id.loadingProgressBarLayout );
        myPreferences = new MyPreferences( this );

        txt_create_account.setOnClickListener( ev->{
            startActivity( new Intent( LogIn.this , RegisterUser.class ) );
        } );
        loginButton.setOnClickListener( ev -> {
            String email = user_email.getText().toString().trim();
            String password = user_password.getText().toString().trim();
            if ( email.isEmpty() ) {
                user_email.setError( "Required !" );
                return;
            }
            if ( password.isEmpty() ) {
                user_password
                        .setError( "Required !" );
                return;
            }

            if(!isConnectedToNewtWork(LogIn.this)){
                DynamicToast.makeError( LogIn.this, "Please check your Connection" ).show();
                return;
            }
            loginButton.setEnabled( false );

            loadingProgressBarLayout.setVisibility( View.VISIBLE );

            hideKeyboard( loginButton, LogIn.this );
            LoginProcess loginProcess = new LoginProcess( this );

            loginProcess.execute( email, password );
        } );
    }


    @Override
    public void onCompleteResponse ( String response ) {
        loadingProgressBarLayout.setVisibility( View.GONE );
        loginButton.setEnabled( true );

        if ( "connection".equals( response ) ) {
            DynamicToast.makeError( LogIn.this, "Please check your Connection" ).show();
        } else if ( "fail".equals( response ) ) {
            DynamicToast.makeError( LogIn.this, "Please use correct details" ).show();
        } else if ( "success".equals( response ) ) {
            DynamicToast.makeSuccess( LogIn.this, "Welcome" ).show();
            clearAllActivitiesToOpenNewActivity( LogIn.this, MainActivity.class );
        } else {
            try {
                JSONObject respo = new JSONObject( response );
                String status = respo.getString( "status" );
                if(status .equals( "none2" ) || status .equals( "none3" )){
                    DynamicToast.makeError( LogIn.this, "Please use correct details !" ).show();
                }else if(status.equals( "auth" )){
                    DynamicToast.makeError( LogIn.this, "Please use correct password details !" ).show();
                }else if(status.equals( "ok" )) {
                    //  {"status":"ok","id_roles":1,"jwt":"eyJ0eXAiOiJKV1QiLCJhbGciOiJub25lIiwianRpIjoiNGYxZzIzYTEyYWEifQ.eyJpc3MiOiJodHRwczpcL1wvIiwiYXVkIjoiaHR0cHM6XC9cL3phIiwianRpIjoiNGYxZzIzYTEyYWEiLCJpYXQiOjE1ODkzNjYwMDQsIm5iZiI6MTU4OTM2NjA2NCwiZXhwIjoxNTg5MzY5NjA0LCJ1c2VyIjoxLCJyb2xlIjoxLCJtb2R1bGVzIjoiMSwyLDMifQ.","email":"kajivakinsley@gmail.com","username":"me","id_user_account_types":"1","full_name":"Kinsley Kajiva"}
                    //logInUser: {"status":"ok","id_roles":1,"jwt":"eyJ0eXAiOiJKV1QiLCJhbGciOiJub25lIiwianRpIjoiNGYxZzIzYTEyYWEifQ.eyJpc3MiOiJodHRwczpcL1wvIiwiYXVkIjoiaHR0cHM6XC9cL3phIiwianRpIjoiNGYxZzIzYTEyYWEiLCJpYXQiOjE1ODkzNjYwMDQsIm5iZiI6MTU4OTM2NjA2NCwiZXhwIjoxNTg5MzY5NjA0LCJ1c2VyIjoxLCJyb2xlIjoxLCJtb2R1bGVzIjoiMSwyLDMifQ.","email":"kajivakinsley@gmail.com","username":"me","id_user_account_types":"1","full_name":"Kinsley Kajiva"}
                    DynamicToast.makeSuccess( LogIn.this, "Welcome" ).show();
                    myPreferences.setUserLoggedIn( true );
                    myPreferences.setInstallationID(INSTALLATION_ID );
                    myPreferences.setEmail( respo.getString( "email" ) );
                    myPreferences.setAPIToken( respo.getString( "jwt" ) );
                    myPreferences.setUserName( respo.getString( "username" ) );
                    myPreferences.setFullName(  respo.getString( "full_name" ) );
                    myPreferences.setUserId(  respo.getString( "user_id" ) );
                    myPreferences.setAccountBusiness( respo.getString( "id_user_account_types" ).equals( "2" ) );
                    //Log.e("xxxxxvvvv", "onCompleteResponse: "+ myPreferences.getAccountBusiness() );
                    clearAllActivitiesToOpenNewActivity( LogIn.this, MainActivity.class );

                }
            } catch ( JSONException e ) {
                e.printStackTrace();
                DynamicToast.makeError( LogIn.this, "Fatal Error Occurred !" ).show();
            }


        }

    }
}
