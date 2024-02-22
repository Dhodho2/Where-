package rdsol.whereat.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;

import java.io.File;
import java.text.SimpleDateFormat;

import rdsol.whereat.R;
import rdsol.whereat.callbacks.HttpReqCallBack;
import rdsol.whereat.database.preferences.MyPreferences;
import rdsol.whereat.processes.SubmitShowScheduleProcess;

import static rdsol.whereat.utils.AndroidUtilities.getRealFileSelectedPath;

public class ScheduleShow extends Fragment implements HttpReqCallBack {
    private MyPreferences myPreferences;
    private String LastSelectedImagePath = "";
    private MaterialButton save, start_time, end_time, date_of_show;
    private ProgressDialog progressDialog;
    private TextInputEditText postPriceAmount, postTitlee;
    private SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );
    private SimpleDateFormat time = new SimpleDateFormat( "HH:mm" );
    private ImageView image_thumbnail;

    public static ScheduleShow getInstance ( ) {

        return new ScheduleShow();
    }

    @Override
    public void onViewCreated ( @NonNull View view, @Nullable Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        myPreferences = new MyPreferences( getContext() );
    }

    @Nullable
    @Override
    public View onCreateView ( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View root = inflater.inflate( R.layout.fragment_schedule_show, container, false );
        progressDialog = new ProgressDialog( getContext() );
        date_of_show = root.findViewById( R.id.date_of_show );
        save = root.findViewById( R.id.save );
        image_thumbnail = root.findViewById( R.id.image_thumbnail );
        end_time = root.findViewById( R.id.end_time );
        postPriceAmount = root.findViewById( R.id.postPriceAmount );
        postPriceAmount.setText( "3" );
        postPriceAmount.setVisibility( View.GONE );
        start_time = root.findViewById( R.id.start_time );
        postTitlee = root.findViewById( R.id.postTitlee );
        image_thumbnail.setOnClickListener( ev -> {

            startActivityForResult( new Intent( Intent.ACTION_GET_CONTENT ).setType( "image/*" ), 123 );
        } );

        date_of_show.setOnClickListener( ev -> {

            new SingleDateAndTimePickerDialog.Builder( getContext() )
                    .curved()
                    .setDayFormatter( format )
                    .titleTextColor( DynamicColorUtils.getRandomColor( R.attr.maintextColor ) )
                    .title( "Select Date" )
                    .displayMinutes( false )
                    .displayHours( false )
                    .displayDays( true )
                    //.displayMonth(true )
                    .displayYears( false )
                    // .setDisplayDaysOfMonth(true )
                    .listener( date -> date_of_show.setText( format.format( date ) ) )
                    .display();
        } );
        end_time.setOnClickListener( ev -> {

            new SingleDateAndTimePickerDialog.Builder( getContext() )
                    .curved()
                    .setDayFormatter( time )
                    .displayDays( false )
                    .titleTextColor( DynamicColorUtils.getRandomColor( R.attr.maintextColor ) )
                    .title( "Select End Time" )
                    .listener( date -> end_time.setText( time.format( date ) ) )
                    .display();
        } );
        start_time.setOnClickListener( ev -> {

            new SingleDateAndTimePickerDialog.Builder( getContext() )
                    .curved()
                    .displayDays( false )
                    .setDayFormatter( time )

                    .titleTextColor( DynamicColorUtils.getRandomColor( R.attr.maintextColor ) )
                    .title( "Select Start Time" )
                    .listener( date -> start_time.setText( time.format( date ) ) )
                    .display();
        } );

        save.setOnClickListener( ev -> {
            String date_of_show_ = date_of_show.getText().toString().trim() ;
            String postTitlee_ = postTitlee.getText().toString().trim();


            if ( postTitlee_.contains( "SET DATE" ) ) {
                DynamicToast.makeError( getContext(), "Show Tittle/Description Required !" ).show();
                return;
            }
            if ( date_of_show_.contains( "SET DATE" ) ) {
                DynamicToast.makeError( getContext(), "Show Date Required !" ).show();
                return;
            }
            String start_time_ = start_time.getText().toString().trim();
            String end_time_ = end_time.getText().toString().trim();
            if ( start_time_.contains( "START TIME" ) ) {
                DynamicToast.makeError( getContext(), "Show Start Time Required !" ).show();
                return;
            }
            if ( end_time_.contains( "END TIME" ) ) {
                DynamicToast.makeError( getContext(), "Show End Time Required !" ).show();
                return;
            }

            String postPriceAmount_ = postPriceAmount.getText().toString().trim();
            if ( postPriceAmount_.isEmpty() ) {
                DynamicToast.makeError( getContext(), "Show Price/Charge Required !" ).show();
                return;
            }
            showProgressDialog( true );
        //    LastSelectedImagePath
            SubmitShowScheduleProcess scheduleProcess = new SubmitShowScheduleProcess( ScheduleShow.this );
            scheduleProcess.execute(   postTitlee_, postPriceAmount_, date_of_show_, start_time_ , end_time_, LastSelectedImagePath );


        } );

        return root;
    }

    @Override
    public void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data ) {
        super.onActivityResult( requestCode, resultCode, data );
        if ( requestCode == 123 ) {
            assert data != null;
            Uri fileU = data.getData();
            if ( fileU != null ) {
                LastSelectedImagePath = ( getRealFileSelectedPath( getContext(), fileU ) );
                if ( !LastSelectedImagePath.isEmpty() ) {
                    Uri videoURI = Uri.parse( LastSelectedImagePath );
                    Log.e( "xxxx", "onCreateView: " + myPreferences.getLastSelectedIntentFilePath() );
                  /*  MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(getContext(), videoURI);*/
                   /* Bitmap bitmap = retriever
                            .getFrameAtTime(100000,MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);*/

                    Glide.with( getContext() )

                            .load( new File( LastSelectedImagePath ) )
                            .thumbnail( Glide.with( getContext() ).load( videoURI ) )
                            .into( image_thumbnail );

                }
            }
        }

    }


    private void showProgressDialog ( final boolean isToShow ) {

        if ( isToShow ) {
            if ( !progressDialog.isShowing() ) {
                progressDialog.setMessage( "Processing..." );
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
        if(response.equals( "ok" )){
            DynamicToast.makeSuccess( getContext(),"Show Submitted" ).show();
            postTitlee.setText( "" ); postPriceAmount.setText( "" );date_of_show.setText( "Set Date" ); start_time.setText( " Start Time" );  end_time.setText( "End Time" );
            LastSelectedImagePath = "";
            image_thumbnail.setImageResource(  R.drawable. ic_main_icon);
        }
    }
}
