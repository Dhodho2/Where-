package rdsol.whereat.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.material.button.MaterialButton;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONException;
import org.json.JSONObject;

import rdsol.whereat.R;
import rdsol.whereat.callbacks.HttpReqCallBack;
import rdsol.whereat.processes.RegisterUserProcess;

import static rdsol.whereat.utils.AndroidUtilities.isValidMail;
import static rdsol.whereat.utils.AndroidUtilities.isValidMobileNumber;

public class RegisterUser extends BaseActivity  implements HttpReqCallBack {
int account_slected = 0;
    private EditText user_full_name ,user_name , user_email, user_phone ,user_password   ;
    private MaterialButton loginButton;
    private LinearLayout loadingProgressBarLayout;
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register_user );

        user_full_name = findViewById( R.id. user_full_name);
        user_name = findViewById( R.id. user_name);
        user_email = findViewById( R.id.user_email );
        user_phone  = findViewById( R.id.user_phone );
        loginButton  = findViewById( R.id.loginButton );
        loadingProgressBarLayout  = findViewById( R.id.loadingProgressBarLayout );
        user_password = findViewById( R.id. user_password);
        Spinner spinner = findViewById( R.id.account_type );
        loadingProgressBarLayout.setVisibility( View.INVISIBLE );
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( this,R.array.account_types , android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner.setAdapter(adapter  );
        // spinner.setPrompt(capitalizeFirstLetter( myPreferences.getLanguage() )  );
        int spinnerPosi = adapter.getPosition( "Personal Account" );
        spinner.setSelection( spinnerPosi );

        loginButton.setOnClickListener( ev->{
            String user_full_name_ = user_full_name.getText().toString().trim();

            if(user_full_name_.isEmpty()){
                user_full_name.setError( "Required" );
                return;
            }
              String user_name_ = user_name.getText().toString().trim();

            if(user_name_.isEmpty()){
                user_name.setError( "Required" );
                return;
            }
              String user_email_ = user_email.getText().toString().trim();

            if(user_email_.isEmpty() ){
                user_email.setError( "Required" );
                return;
            }
            if(!isValidMail(user_email_) ){

                user_email.setError( "Valid Email Required" );
                return;
            }
              String user_phone_ = user_phone.getText().toString().trim();

            if(user_phone_.isEmpty()){
                user_phone.setError( "Required" );
                return;
            }
            if(!isValidMobileNumber(user_phone_) ){

                user_phone.setError( "Valid Phone Required" );
                return;
            }
              String user_password_ = user_password.getText().toString().trim();

            if(user_password_.isEmpty()){
                user_password.setError( "Required" );
                return;
            }
            loginButton.setEnabled( false );

            loadingProgressBarLayout.setVisibility( View.VISIBLE );
            RegisterUserProcess  registerUserProcess = new RegisterUserProcess(RegisterUser.this);
            registerUserProcess.execute(  user_full_name_ ,user_name_ , user_email_, user_phone_ ,user_password_ ,account_slected+"" );

        } );

        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected ( AdapterView<?> parent, View view, int position, long id ) {
               // Log.e( "xxxx", "onItemClick: HHH:: " + position + " ::Langu:: " + LANGUAGES[position]);
                if(position == 0){
                    // personal
                    loginButton.setText("Create Account");
                }else{
                    // business
                    loginButton.setText("Create ShowRoom");
                }
                account_slected = position;

            }

            @Override
            public void onNothingSelected ( AdapterView<?> parent ) {

            }
        } );
    }

    @Override
    public void onCompleteResponse ( String response ) {
        loadingProgressBarLayout.setVisibility( View.GONE );
        loginButton.setEnabled( true );
        if ( "connection".equals( response ) ) {
            DynamicToast.makeError( RegisterUser.this, "Please check your Connection" ).show();
        } else if ( "fail".equals( response ) ) {
            DynamicToast.makeError( RegisterUser.this, "Please use correct details" ).show();
        } else if ( "success".equals( response ) ) {
            DynamicToast.makeSuccess( RegisterUser.this, "Welcome" ).show();
            clearAllActivitiesToOpenNewActivity( RegisterUser.this, MainActivity.class );
        } else {
            try {
            JSONObject respo = new JSONObject( response );
                String status = respo.getString( "status" );
                if(status.equals( "ok" )){
                    DynamicToast.makeSuccess( RegisterUser.this, "Welcome,Account has been created, Please Log In using your details ." ).show();
                    onBackPressed();
                }else{
                    DynamicToast.makeError( RegisterUser.this, "Please Try again." ).show();
                }
        } catch ( JSONException e ) {
                e.printStackTrace();
                DynamicToast.makeError( RegisterUser.this, "Fatal Error Occurred !" ).show();
            }

        }
    }
}
